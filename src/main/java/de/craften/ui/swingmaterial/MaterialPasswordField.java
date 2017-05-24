package de.craften.ui.swingmaterial;

import static de.craften.ui.swingmaterial.MaterialTextField.HINT_OPACITY_MASK;
import static de.craften.ui.swingmaterial.MaterialTextField.LINE_OPACITY_MASK;
import de.craften.ui.swingmaterial.fonts.Roboto;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * A Material Design password field.
 *
 * @see <a href="https://www.google.com/design/spec/components/text-fields.html">Text fields (Google design guidelines)</a>
 */
public class MaterialPasswordField extends JPasswordField {
    private MaterialTextField.FloatingLabel floatingLabel = new MaterialTextField.FloatingLabel(this);
    private MaterialTextField.Line line = new MaterialTextField.Line(this);
    private String hint = "";
    private Color accentColor = MaterialColor.CYAN_500;

    /**
     * Creates a new password field.
     */
    public MaterialPasswordField() {
        setBorder(null);
        setFont(getFont().deriveFont(16f)); //use default font, Roboto's bullet doesn't work on some platforms (i.e. Mac)
        floatingLabel.setText("");
        setOpaque(false);
        setBackground(MaterialColor.TRANSPARENT);

        setCaret(new DefaultCaret() {
            @Override
            protected synchronized void damage(Rectangle r) {
                MaterialPasswordField.this.repaint(); //fix caret not being removed completely
            }
        });
        getCaret().setBlinkRate(500);
    }

    /**
     * Gets the text of the floating label.
     * @return text of the floating label
     */
    public String getLabel() {
        return floatingLabel.getText();
    }

    /**
     * Sets the text of the floating label.
     * @param label text of the floating label
     */
    public void setLabel(String label) {
        floatingLabel.setText(label);
        repaint();
    }

    /**
     * Gets the hint text. The hint text is displayed when this textfield is
     * empty.
     * @return hint text
     */
    public String getHint() {
        return hint;
    }

    /**
     * Sets the hint text. The hint text is displayed when this textfield is
     * empty.
     * @param hint hint text
     */
    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }
    
    /**
     * Gets the color the label changes to when this {@code materialTextField}
     * is focused.
     * @return the {@code "Color"} currently in use for accent. The default
     *         value is {@link MaterialColor#CYAN_300}.
     */
    public Color getAccent() {
        return accentColor;
    }

    /**
     * Sets the color the label changes to when this {@code materialTextField}
     * is focused. The default value is {@link MaterialColor#CYAN_300}.
     * @param accentColor the {@code "Color"} that should be used for accent.
     */
    public void setAccent(Color accentColor) {
        this.accentColor = accentColor;
        floatingLabel.setAccent(accentColor);
    }
    
    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (floatingLabel != null)
            floatingLabel.updateForeground();
    }

    @Override
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

        if (!getHint().isEmpty() && getPassword().length == 0 && (getLabel().isEmpty() || isFocusOwner()) && floatingLabel.isFloatingAbove()) {
            g.setFont(Roboto.REGULAR.deriveFont(16f));
            g2.setColor(Utils.applyAlphaMask(getForeground(), HINT_OPACITY_MASK));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(getHint(), 0, metrics.getAscent() + 36);
        }

        floatingLabel.paint(g2);

        g2.setColor(Utils.applyAlphaMask(getForeground(), LINE_OPACITY_MASK));
        g2.fillRect(0, getHeight() - 9, getWidth(), 1);

        g2.setColor(accentColor);
        g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 10, (int) line.getWidth(), 2);
    }

    @Override
    protected void paintBorder(Graphics g) {
        //intentionally left blank
    }
}
