package de.craften.ui.swingmaterial;

import javax.swing.*;
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

    public MaterialPasswordField() {
        setBorder(null);
        setFont(Roboto.REGULAR.deriveFont(16f));
        floatingLabel.setText("");
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
}
