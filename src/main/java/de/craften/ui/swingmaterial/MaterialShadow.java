package de.craften.ui.swingmaterial;

import de.craften.ui.swingmaterial.util.FastGaussianBlur;
import org.jdesktop.core.animation.timing.KeyFrames;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * A renderer for Material shadow.
 */
public class MaterialShadow {
    public static final int OFFSET_TOP = 10;
    public static final int OFFSET_LEFT = 20;
    public static final int OFFSET_BOTTOM = 20;
    public static final int OFFSET_RIGHT = 20;

    private static KeyFrames<Float> opacity1 = new KeyFrames.Builder<>(0f)
            .addFrame(0.12f, 1 / 5.0)
            .addFrame(0.16f, 2 / 5.0)
            .addFrame(0.19f, 3 / 5.0)
            .addFrame(0.25f, 4 / 5.0)
            .addFrame(0.30f, 5 / 5.0)
            .build();
    private static KeyFrames<Float> opacity2 = new KeyFrames.Builder<>(0f)
            .addFrame(0.24f, 1 / 5.0)
            .addFrame(0.23f, 2 / 5.0)
            .addFrame(0.23f, 3 / 5.0)
            .addFrame(0.22f, 4 / 5.0)
            .addFrame(0.22f, 5 / 5.0)
            .build();
    private static KeyFrames<Float> radius1 = new KeyFrames.Builder<>(0f)
            .addFrame(3f, 1 / 5.0)
            .addFrame(6f, 2 / 5.0)
            .addFrame(20f, 3 / 5.0)
            .addFrame(28f, 4 / 5.0)
            .addFrame(38f, 5 / 5.0)
            .build();
    private static KeyFrames<Float> radius2 = new KeyFrames.Builder<>(0f)
            .addFrame(2f, 1 / 5.0)
            .addFrame(6f, 2 / 5.0)
            .addFrame(6f, 3 / 5.0)
            .addFrame(10f, 4 / 5.0)
            .addFrame(12f, 5 / 5.0)
            .build();
    private static KeyFrames<Float> offset1 = new KeyFrames.Builder<>(0f)
            .addFrame(1f, 1 / 5.0)
            .addFrame(3f, 2 / 5.0)
            .addFrame(10f, 3 / 5.0)
            .addFrame(14f, 4 / 5.0)
            .addFrame(19f, 5 / 5.0)
            .build();
    private static KeyFrames<Float> offset2 = new KeyFrames.Builder<>(0f)
            .addFrame(1f, 1 / 5.0)
            .addFrame(3f, 2 / 5.0)
            .addFrame(6f, 3 / 5.0)
            .addFrame(10f, 4 / 5.0)
            .addFrame(15f, 5 / 5.0)
            .build();

    /**
     * Creates an image containing shadow with the given width and height.
     *
     * @param width  width
     * @param height height
     * @param level  elevation level (0..5)
     * @return shadow image
     */
    public static BufferedImage renderShadow(int width, int height, double level) {
        if (level < 0 || level > 5) {
            throw new IllegalArgumentException("Shadow level must be between 1 and 5 (inclusive)");
        }

        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (width > 0 && height > 0 && level != 0) {
            BufferedImage shadow2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = shadow.createGraphics();
            g.setComposite(AlphaComposite.SrcOver);

            makeShadow(shadow, opacity1.getInterpolatedValueAt(level / 5), radius1.getInterpolatedValueAt(level / 5),
                    0, offset1.getInterpolatedValueAt(level / 5));
            makeShadow(shadow2, opacity2.getInterpolatedValueAt(level / 5), radius2.getInterpolatedValueAt(level / 5),
                    0, offset2.getInterpolatedValueAt(level / 5));
            g.drawImage(shadow2, 0, 0, null);

            g.dispose();
        }

        return shadow;
    }

    /**
     * Creates an image containing circular shadow with the given radius.
     *
     * @param radius radius
     * @param level  elevation level (0..5)
     * @return shadow image
     */
    public static BufferedImage renderCircularShadow(int radius, double level) {
        if (level < 0 || level > 5) {
            throw new IllegalArgumentException("Shadow level must be between 1 and 5 (inclusive)");
        }

        BufferedImage shadow = new BufferedImage(radius, radius + OFFSET_TOP + OFFSET_BOTTOM, BufferedImage.TYPE_INT_ARGB);
        if (level != 0) {
            BufferedImage shadow2 = new BufferedImage(radius, radius + OFFSET_TOP + OFFSET_BOTTOM, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = shadow.createGraphics();
            g.setComposite(AlphaComposite.SrcOver);

            makeCircularShadow(shadow, opacity1.getInterpolatedValueAt(level / 5), radius1.getInterpolatedValueAt(level / 5),
                    0, offset1.getInterpolatedValueAt(level / 5));
            makeCircularShadow(shadow2, opacity2.getInterpolatedValueAt(level / 5), radius2.getInterpolatedValueAt(level / 5),
                    0, offset2.getInterpolatedValueAt(level / 5));
            g.drawImage(shadow2, 0, 0, null);

            g.dispose();
        }
        return shadow;
    }

    private static void makeShadow(BufferedImage shadow, float opacity, float radius, float leftOffset, float topOffset) {
        Graphics2D g2 = shadow.createGraphics();
        g2.setColor(new Color(0, 0, 0, opacity));
        g2.fill(new RoundRectangle2D.Float(OFFSET_LEFT + leftOffset, OFFSET_TOP + topOffset,
                shadow.getWidth() - OFFSET_LEFT - OFFSET_RIGHT, shadow.getHeight() - OFFSET_TOP - OFFSET_BOTTOM, 3, 3));
        g2.dispose();
        FastGaussianBlur.blur(shadow, radius);
    }

    private static void makeCircularShadow(BufferedImage shadow, float opacity, float radius, float leftOffset, float topOffset) {
        Graphics2D g2 = shadow.createGraphics();
        g2.setColor(new Color(0, 0, 0, opacity));
        g2.fill(new Ellipse2D.Double(OFFSET_LEFT + leftOffset, OFFSET_TOP + topOffset,
                shadow.getWidth() - OFFSET_LEFT - OFFSET_RIGHT, shadow.getWidth() - OFFSET_LEFT - OFFSET_RIGHT));
        g2.dispose();
        FastGaussianBlur.blur(shadow, radius);
    }
}
