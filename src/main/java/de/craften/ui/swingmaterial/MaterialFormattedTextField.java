package de.craften.ui.swingmaterial;

import de.craften.ui.swingmaterial.util.SafePropertySetter;
import de.craften.ui.swingmaterial.util.SafePropertySetter.Property;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.Builder;
import org.jdesktop.core.animation.timing.Animator.EndBehavior;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

/**
 * Creates a new empty formatted text field.
 */
public class MaterialFormattedTextField extends JFormattedTextField {
	private MaterialFormattedTextField.FloatingLabel floatingLabel = new MaterialFormattedTextField.FloatingLabel(this);
	private MaterialFormattedTextField.Line line = new MaterialFormattedTextField.Line(this);
	private String hint = "";
	private MaskFormatter mask;
	private boolean masked;
	String txt;

	/**
	 * Creates a new formatted text field with mask.
	 */
	public MaterialFormattedTextField(String mask) {
		this.setBorder((Border)null);
		this.setFont(Roboto.REGULAR.deriveFont(16.0F));
		this.floatingLabel.setText("");
		this.setCaret(new DefaultCaret() {
			protected synchronized void damage(Rectangle r) {
				MaterialFormattedTextField.this.repaint();
			}
		});
		this.getCaret().setBlinkRate(500);
		try {
			this.mask = new MaskFormatter(mask);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the text of the floating label.
	 *
	 * @return text of the floating label
	 */
	public String getLabel() {
		return this.floatingLabel.getText();
	}

	/**
	 * Sets the text of the floating label.
	 *
	 * @param label text of the floating label
	 */
	public void setLabel(String label) {
		this.floatingLabel.setText(label);
		this.repaint();
	}

	/**
	 * Gets the hint text.
	 *
	 * @return hint text
	 */
	public String getHint() {
		return this.hint;
	}

	/**
	 * Sets the hint text.
	 *
	 * @param hint hint text
	 */
	public void setHint(String hint) {
		this.hint = hint;
		this.repaint();
	}

	public void setText(String s) {
		super.setText(s);
		this.floatingLabel.update();
		this.line.update();
	}

	protected void processFocusEvent(FocusEvent e) {
		super.processFocusEvent(e);
		this.floatingLabel.update();
		this.line.update();

		masked = !masked;
		txt = this.getText();
		txt = txt.replaceAll("[^0-9]","").trim();
		if(this.masked){
			mask.install(this);
			this.setText(txt);
		}else{
			mask.uninstall();
			if(txt.equals("")){
				this.setText(txt);
			}
		}
	}

	protected void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);
		this.floatingLabel.update();
		this.line.update();
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.translate(0, 9);
		super.paintComponent(g);
		g2.translate(0, -9);
		if(!this.getHint().isEmpty() && this.getText().isEmpty() && (this.getLabel().isEmpty() || this.isFocusOwner()) && this.floatingLabel.isFloatingAbove()) {
			g.setFont(Roboto.REGULAR.deriveFont(16.0F));
			g2.setColor(MaterialColor.MIN_BLACK);
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			g.drawString(this.getHint(), 0, metrics.getAscent() + 36);
		}

		this.floatingLabel.paint(g2);
		g2.setColor(MaterialColor.GREY_300);
		g2.fillRect(0, this.getHeight() - 9, this.getWidth(), 1);
		g2.setColor(MaterialColor.CYAN_500);
		g2.fillRect((int)(((double)this.getWidth() - this.line.getWidth()) / 2.0D), this.getHeight() - 10, (int)this.line.getWidth(), 2);
	}

	protected void paintBorder(Graphics g) {
	}

	/**
	 * A floating label of a text field.
	 */
	public static class FloatingLabel {
		private final SwingTimerTimingSource timer;
		private final JTextField target;
		private Animator animator;
		private final Property<Double> y;
		private final Property<Double> fontSize;
		private final Property<Color> color;
		private String text;

		FloatingLabel(JTextField target) {
			this.target = target;
			this.timer = new SwingTimerTimingSource();
			this.timer.init();
			this.y = SafePropertySetter.animatableProperty(target, Double.valueOf(36.0D));
			this.fontSize = SafePropertySetter.animatableProperty(target, Double.valueOf(16.0D));
			this.color = SafePropertySetter.animatableProperty(target, MaterialColor.MIN_BLACK);
		}

		void update() {
			if(this.animator != null) {
				this.animator.stop();
			}

			Builder builder = (new Builder(this.timer)).setDuration(200L, TimeUnit.MILLISECONDS).setEndBehavior(EndBehavior.HOLD).setInterpolator(new SplineInterpolator(0.4D, 0.0D, 0.2D, 1.0D));
			double targetFontSize = !this.target.isFocusOwner() && this.target.getText().isEmpty()?16.0D:12.0D;
			if(((Double)this.fontSize.getValue()).doubleValue() != targetFontSize) {
				builder.addTarget(SafePropertySetter.getTarget(this.fontSize, new Double[]{(Double)this.fontSize.getValue(), Double.valueOf(targetFontSize)}));
			}

			double targetY = !this.target.isFocusOwner() && this.target.getText().isEmpty()?36.0D:16.0D;
			if(Math.abs(targetY - ((Double)this.y.getValue()).doubleValue()) > 0.1D) {
				builder.addTarget(SafePropertySetter.getTarget(this.y, new Double[]{(Double)this.y.getValue(), Double.valueOf(targetY)}));
			}

			Color targetColor;
			if(this.target.getText().isEmpty() && this.target.isFocusOwner()) {
				targetColor = MaterialColor.CYAN_500;
			} else if(this.target.getText().isEmpty()) {
				targetColor = MaterialColor.MIN_BLACK;
			} else {
				targetColor = MaterialColor.LIGHT_BLACK;
			}

			if(!targetColor.equals(this.color.getValue())) {
				builder.addTarget(SafePropertySetter.getTarget(this.color, new Color[]{(Color)this.color.getValue(), targetColor}));
			}

			this.animator = builder.build();
			this.animator.start();
		}

		String getText() {
			return this.text;
		}

		void setText(String text) {
			this.text = text;
		}

		void paint(Graphics2D g) {
			g.setFont(Roboto.REGULAR.deriveFont(((Double)this.fontSize.getValue()).floatValue()));
			g.setColor((Color)this.color.getValue());
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			g.drawString(this.getText(), 0, metrics.getAscent() + ((Double)this.y.getValue()).intValue());
		}

		boolean isFloatingAbove() {
			return ((Double)this.y.getValue()).doubleValue() < 17.0D;
		}
	}

	/**
	 * An animated line below a text field.
	 */
	public static class Line {
		private final SwingTimerTimingSource timer;
		private final JComponent target;
		private Animator animator;
		private Property<Double> width;

		Line(JComponent target) {
			this.target = target;
			this.timer = new SwingTimerTimingSource();
			this.timer.init();
			this.width = SafePropertySetter.animatableProperty(target, Double.valueOf(0.0D));
		}

		void update() {
			if(this.animator != null) {
				this.animator.stop();
			}

			this.animator = (new Builder(this.timer)).setDuration(200L, TimeUnit.MILLISECONDS).setEndBehavior(EndBehavior.HOLD).setInterpolator(new SplineInterpolator(0.4D, 0.0D, 0.2D, 1.0D)).addTarget(SafePropertySetter.getTarget(this.width, new Double[]{(Double)this.width.getValue(), Double.valueOf(this.target.isFocusOwner()?(double)this.target.getWidth() + 1.0D:0.0D)})).build();
			this.animator.start();
		}

		public double getWidth() {
			return ((Double)this.width.getValue()).doubleValue();
		}
	}
}
