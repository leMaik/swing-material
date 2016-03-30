package de.craften.ui.swingmaterial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A Material Design button.
 *
 * @see <a href="https://www.google.com/design/spec/components/buttons.html">Buttons (Google design guidelines)</a>
 */
public class MaterialButton extends JButton {
    private RippleEffect ripple;
    private ElevationEffect elevation;
    private boolean raised = false;
    private boolean isMousePressed = false;
    private boolean isMouseOver = false;

    public MaterialButton() {
        ripple = RippleEffect.applyTo(this);
        elevation = ElevationEffect.applyTo(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                isMousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                isMousePressed = false;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isMouseOver = false;
                repaint();
            }
        });

        setFont(Roboto.MEDIUM.deriveFont(14f));
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        elevation.setLevel(getElevation());
    }

    @Override
    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
        elevation.setLevel(getElevation());
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        elevation.setLevel(getElevation());
    }

    private int getElevation() {
        if (isMousePressed) {
            return 2;
        } else if (isRaised() || isFocusOwner() || isMouseOver) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getParent().getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        elevation.paint(g);

        g2.translate(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP);

        final int offset_lr = MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT;
        final int offset_td = MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM;

        if (isEnabled()) {
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));

            if (isFocusOwner()) {
                g2.setColor(new Color(1, 1, 1, 0.2f));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));
            }
        } else {
            Color bg = getBackground();
            g2.setColor(new Color(bg.getRed() / 255f, bg.getGreen() / 255f, bg.getBlue() / 255f, 0.6f));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));
        }

        FontMetrics metrics = g.getFontMetrics(getFont());
        int x = (getWidth() - offset_lr - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - offset_td - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setFont(getFont());
        if (isEnabled()) {
            g2.setColor(getForeground());
        } else {
            Color fg = getForeground();
            g2.setColor(new Color(fg.getRed() / 255f, fg.getGreen() / 255f, fg.getBlue() / 255f, 0.6f));
        }
        g2.drawString(getText().toUpperCase(), x, y);

        if (isEnabled()) {
            g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));
            ripple.paint(g2);
        }
    }

    @Override
    protected void paintBorder(Graphics graphics) {
    }

    public boolean isRaised() {
        return raised;
    }

    public void setRaised(boolean raised) {
        this.raised = raised;
        repaint();
    }
}
