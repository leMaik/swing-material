package de.craften.ui.swingmaterial.toast;

import de.craften.ui.swingmaterial.Roboto;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A toast.
 */
public abstract class Toast extends JComponent {
    public static final Color BACKGROUND = Color.decode("#323232");
    public static final Font FONT = Roboto.REGULAR.deriveFont(14f);
    public static final int MAX_WIDTH = 568;
    private double yOffset = Double.POSITIVE_INFINITY;

    public Toast() {
        setOpaque(false);
    }

    void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * Paints this toast.
     *
     * @param g graphics canvas
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ((Graphics2D) g).translate(0, yOffset);
        g.setColor(BACKGROUND);
        if (getParent().getWidth() > MAX_WIDTH) {
            g.translate((getWidth() - MAX_WIDTH) / 2, 0);
            g2.fill(new RoundRectangle2D.Float(0, 0, MAX_WIDTH, getHeight(), 4, 4));
        } else {
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
