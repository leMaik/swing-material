package de.craften.ui.swingmaterial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

/**
 * A Material Design icon button.
 *
 * @see <a href="https://www.google.com/design/spec/components/buttons.html">Buttons (Google design guidelines)</a>
 */
public class MaterialIconButton extends JButton {
    private RippleEffect ripple;

    /**
     * Creates a new button.
     */
    public MaterialIconButton() {
        ripple = RippleEffect.applyFixedTo(this);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        repaint();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        repaint();
    }

    @Override
    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
        repaint();
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (isEnabled()) {
            if (getIcon() != null) {
                getIcon().paintIcon(this, g2, 12, 12);
            }
            g2.setColor(getForeground());
            ripple.paint(g2);
        } else if (getIcon() != null) {
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2.setComposite(ac);
            getIcon().paintIcon(this, g2, 12, 12);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        //intentionally left blank
    }
}
