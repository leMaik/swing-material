package de.craften.ui.swingmaterial;

import com.jhlabs.image.GaussianFilter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * A renderer for Material shadow.
 */
public class MaterialShadow {
    private static final int offset = 10;

    public static BufferedImage renderShadow(int width, int height, int level) {
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = shadow.createGraphics();
        g.setComposite(AlphaComposite.SrcOver);

        switch (level) {
            case 0:
                break;
            case 1:
                g.drawImage(getShadow(width, height, 0.12f, 3), 0, 1, null);
                g.drawImage(getShadow(width, height, 0.24f, 2), 0, 1, null);
                break;
            case 2:
                g.drawImage(getShadow(width, height, 0.16f, 6), 0, 3, null);
                g.drawImage(getShadow(width, height, 0.23f, 6), 0, 3, null);
                break;
            case 3:
                g.drawImage(getShadow(width, height, 0.19f, 20), 0, 10, null);
                g.drawImage(getShadow(width, height, 0.23f, 6), 0, 6, null);
                break;
            case 4:
                g.drawImage(getShadow(width, height, 0.25f, 28), 0, 14, null);
                g.drawImage(getShadow(width, height, 0.22f, 10), 0, 10, null);
                break;
            case 5:
                g.drawImage(getShadow(width, height, 0.30f, 38), 0, 19, null);
                g.drawImage(getShadow(width, height, 0.22f, 12), 0, 15, null);
                break;
            default:
                throw new IllegalArgumentException("Shadow level must be between 1 and 5 (inclusive)");
        }

        g.dispose();
        return shadow;
    }

    private static BufferedImage getShadow(int width, int height, float opacity, float radius) {
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = shadow.createGraphics();
        g2.setColor(new Color(0, 0, 0, opacity));
        g2.fill(new RoundRectangle2D.Float(offset, offset, shadow.getWidth() - 2 * offset, shadow.getHeight() - 2 * offset, 3, 3));
        g2.dispose();
        return new GaussianFilter(radius).filter(shadow, shadow);
    }
}
