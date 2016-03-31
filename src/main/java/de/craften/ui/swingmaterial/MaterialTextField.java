package de.craften.ui.swingmaterial;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

/**
 * A Material Design single-line text field.
 *
 * @see <a href="https://www.google.com/design/spec/components/text-fields.html">Text fields (Google design guidelines)</a>
 */
public class MaterialTextField extends JTextField {
    private FloatingLabel floatingLabel = new FloatingLabel(this);
    private Line line = new Line(this);
    private String hint = "";

    public MaterialTextField() {
        setBorder(null);
        setFont(Roboto.REGULAR.deriveFont(16f));
        floatingLabel.setText("");
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        floatingLabel.update();
        line.update();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        floatingLabel.update();
        line.update();
    }

    public String getLabel() {
        return floatingLabel.getText();
    }

    public void setLabel(String label) {
        floatingLabel.setText(label);
        repaint();
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

        if (!getHint().isEmpty() && getText().isEmpty() && (getLabel().isEmpty() || isFocusOwner())) {
            g.setFont(Roboto.REGULAR.deriveFont(16f));
            g2.setColor(MaterialColor.MIN_BLACK);
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(getHint(), 0, metrics.getAscent() + 36);
        }

        floatingLabel.paint(g2);

        g2.setColor(MaterialColor.MIN_BLACK);
        g2.fillRect(0, getHeight() - 9, getWidth(), 1);

        g2.setColor(MaterialColor.CYAN_500);
        g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 10, (int) line.getWidth(), 2);
    }

    public static class Line {
        private final SwingTimerTimingSource timer;
        private final JComponent target;
        private Animator animator;
        private double width;

        Line(JComponent target) {
            this.target = target;
            this.timer = new SwingTimerTimingSource();
            timer.init();
            width = 0;
        }

        void update() {
            if (animator != null) {
                animator.stop();
            }
            animator = new Animator.Builder(timer)
                    .setDuration(200, TimeUnit.MILLISECONDS)
                    .setEndBehavior(Animator.EndBehavior.HOLD)
                    .setInterpolator(new SplineInterpolator(0.55, 0, 0.1, 1))
                    .addTarget(PropertySetter.getTarget(this, "width", width, target.isFocusOwner() ? (double) target.getWidth() + 1 : 0d))
                    .build();
            animator.start();
        }

        @Deprecated
        public double getWidth() {
            return width;
        }

        @Deprecated
        public void setWidth(double width) {
            this.width = width;
            target.repaint();
        }
    }

    public static class FloatingLabel {
        private final SwingTimerTimingSource timer;
        private final JTextField target;
        private Animator animator;
        private double y = 36;
        private double fontSize = 16;
        private Color color = MaterialColor.MIN_BLACK;
        private String text;

        FloatingLabel(JTextField target) {
            this.target = target;
            this.timer = new SwingTimerTimingSource();
            timer.init();
        }

        void update() {
            if (animator != null) {
                animator.stop();
            }
            Animator.Builder builder = new Animator.Builder(timer)
                    .setDuration(200, TimeUnit.MILLISECONDS)
                    .setEndBehavior(Animator.EndBehavior.HOLD)
                    .setInterpolator(new SplineInterpolator(0.55, 0, 0.1, 1));
            double targetFontSize = (target.isFocusOwner() || !target.getText().isEmpty()) ? 12d : 16d;
            if (fontSize != targetFontSize) {
                builder.addTarget(PropertySetter.getTarget(this, "fontSize", fontSize, targetFontSize));
            }
            double targetY = target.isFocusOwner() || !target.getText().isEmpty() ? 16d : 36d;
            if (Math.abs(targetY - y) > 0.1) {
                builder.addTarget(PropertySetter.getTarget(this, "y", fontSize, targetY));
            }
            Color targetColor;
            if (target.getText().isEmpty() && target.isFocusOwner()) {
                targetColor = MaterialColor.CYAN_500;
            } else {
                if (target.getText().isEmpty()) {
                    targetColor = MaterialColor.MIN_BLACK;
                } else {
                    targetColor = MaterialColor.LIGHT_BLACK;
                }
            }
            if (!targetColor.equals(color)) {
                builder.addTarget(PropertySetter.getTarget(this, "color", color, targetColor));
            }
            animator = builder.build();
            animator.start();
        }

        String getText() {
            return text;
        }

        void setText(String text) {
            this.text = text;
        }

        @Deprecated
        public double getY() {
            return y;
        }

        @Deprecated
        public void setY(double y) {
            this.y = y;
            target.repaint();
        }

        @Deprecated
        public double getFontSize() {
            return fontSize;
        }

        @Deprecated
        public void setFontSize(double fontSize) {
            this.fontSize = fontSize;
            target.repaint();
        }

        @Deprecated
        public Color getColor() {
            return color;
        }

        @Deprecated
        public void setColor(Color color) {
            this.color = color;
            target.repaint();
        }

        void paint(Graphics2D g) {
            g.setFont(Roboto.REGULAR.deriveFont((float) fontSize));
            g.setColor(color);
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(getText(), 0, metrics.getAscent() + (int) y);
        }
    }
}
