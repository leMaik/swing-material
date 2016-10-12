package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.Format;

/**
 * A Material Design formatted field.
 */
public class MaterialFormattedTextField extends JFormattedTextField {
	private MaterialTextField.FloatingLabel floatingLabel;
	private MaterialTextField.Line line;
	private String hint = "";

	/**
	 * Creates a new field with no mask.
	 */
	public MaterialFormattedTextField() {
		floatingLabel = new MaterialTextField.FloatingLabel(this);
		line = new MaterialTextField.Line(this);
		initMaterialFormattedTextField();
	}

	/**
	 * Creates a new field with the value. This will
	 * create an <code>AbstractFormatterFactory</code> based on the
	 * type of <code>value</code>.
	 *
	 * @param value Initial value
	 */
	public MaterialFormattedTextField(Object value) {
		super(value);
	}

	/**
	 * Creates a new field. <code>format</code> is
	 * wrapped in an appropriate <code>AbstractFormatter</code> which is
	 * then wrapped in an <code>AbstractFormatterFactory</code>.
	 *
	 * @param format Format used to look up an AbstractFormatter
	 */
	public MaterialFormattedTextField(Format format) {
		super(format);
	}

	/**
	 * Creates a new field with the specified <code>AbstractFormatter</code>.
	 *
	 * @param formatter AbstractFormatter to use for formatting.
	 */
	public MaterialFormattedTextField(AbstractFormatter formatter) {
		this(new DefaultFormatterFactory(formatter));
	}

	/**
	 * Creates a new field with the specified
	 * <code>AbstractFormatterFactory</code>.
	 *
	 * @param factory AbstractFormatterFactory used for formatting.
	 */
	public MaterialFormattedTextField(AbstractFormatterFactory factory) {
		this();
		setFormatterFactory(factory);
	}


	/**
	 * Creates a new field with the specified
	 * <code>AbstractFormatterFactory</code> and initial value.
	 *
	 * @param factory <code>AbstractFormatterFactory</code> used for formatting.
	 * @param currentValue Initial value to use
	 */
	public MaterialFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
		this(currentValue);
		setFormatterFactory(factory);
	}

	/**
	 * Initialize the default values of the field
	 */
	private void initMaterialFormattedTextField() {
		setBorder(null);
		setFont(Roboto.REGULAR.deriveFont(16f));
		floatingLabel.setText("");

		setCaret(new DefaultCaret() {
			@Override
			protected synchronized void damage(Rectangle r) {
				MaterialFormattedTextField.this.repaint(); //fix caret not being removed completely
			}
		});
		getCaret().setBlinkRate(500);
	}

	/**
	 * Gets the text of the floating label.
	 *
	 * @return text of the floating label
	 */
	public String getLabel() {
		return floatingLabel.getText();
	}

	/**
	 * Sets the text of the floating label.
	 *
	 * @param label text of the floating label
	 */
	public void setLabel(String label) {
		floatingLabel.setText(label);
		floatingLabel.update();
		repaint();
	}

	/**
	 * Gets the hint text.
	 *
	 * @return hint text
	 */
	public String getHint() {
		return hint;
	}

	/**
	 * Sets the hint text.
	 *
	 * @param hint hint text
	 */
	public void setHint(String hint) {
		this.hint = hint;
		repaint();
	}

	public void setText(String s) {
		super.setText(s);
		floatingLabel.update();
		line.update();
	}

	@Override
	protected void processFocusEvent(FocusEvent e) {
		super.processFocusEvent(e);
		floatingLabel.update();
		line.update();
		repaint();
	}

	@Override
	protected void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);
		floatingLabel.update();
		line.update();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.translate(0, 9);
		super.paintComponent(g);
		g2.translate(0, -9);

		if (!getHint().isEmpty() && getText().length() == 0 && (getLabel().isEmpty() || isFocusOwner()) && floatingLabel.isFloatingAbove()) {
			g.setFont(Roboto.REGULAR.deriveFont(16f));
			g2.setColor(MaterialColor.MIN_BLACK);
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			g.drawString(getHint(), 0, metrics.getAscent() + 36);
		}

		floatingLabel.paint(g2);

		g2.setColor(MaterialColor.GREY_300);
		g2.fillRect(0, getHeight() - 9, getWidth(), 1);

		g2.setColor(MaterialColor.CYAN_500);
		g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 10, (int) line.getWidth(), 2);
	}

	@Override
	protected void paintBorder(Graphics g) {
		//intentionally left blank
	}

}