package de.craften.ui.swingmaterial;

import de.craften.ui.swingmaterial.util.FastGaussianBlur;
import org.jdesktop.core.animation.timing.KeyFrames;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * A renderer for Material shadows. Shadows are a sign of elevation, and help
 * distinguishing elements inside a Material-based GUI.
 */
public class MaterialShadow {
    /**
     * The default offset between the border of the shadow and the top of a
     * Material component.
     */
    public static final int OFFSET_TOP = 10;
    /**
     * The default offset between the border of the shadow and the left of a
     * Material component.
     */
    public static final int OFFSET_LEFT = 20;
    /**
     * The default offset between the border of the shadow and the bottom of a
     * Material component.
     */
    public static final int OFFSET_BOTTOM = 20;
    /**
     * The default offset between the border of the shadow and the right of a
     * Material component.
     */
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
     * Creates a {@link BufferedImage} containing a shadow projected from a
     * square component of the given width and height.
     *
     * @param width  the component's width, inpixels
     * @param height the component's height, inpixels
     * @param level  the elevation level [0~5]
     * @return A {@link BufferedImage} with the contents of the shadow for a
     *         circular component of the given radius.
     */
    public static BufferedImage renderShadow(int width, int height, double level) {
        return renderShadow(width, height, level, 3);
    }

    /**
     * Creates a {@link BufferedImage} containing a shadow projected from a
     * square component of the given width and height.
     *
     * @param width  the component's width, inpixels
     * @param height the component's height, inpixels
     * @param level  the elevation level [0~5]
     * @param borderRadius an applicable radius to the border of the shadow
     * @return A {@link BufferedImage} with the contents of the shadow for a
     *         circular component of the given radius.
     */
    public static BufferedImage renderShadow(int width, int height, double level, int borderRadius) {
        if (level < 0 || level > 5) {
            throw new IllegalArgumentException("Shadow level must be between 1 and 5 (inclusive)");
        }

        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (width > 0 && height > 0 && level != 0) {
            BufferedImage shadow2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = shadow.createGraphics();
            g.setComposite(AlphaComposite.SrcOver);

            makeShadow(shadow, opacity1.getInterpolatedValueAt(level / 5), radius1.getInterpolatedValueAt(level / 5),
                    0, offset1.getInterpolatedValueAt(level / 5), borderRadius);
            makeShadow(shadow2, opacity2.getInterpolatedValueAt(level / 5), radius2.getInterpolatedValueAt(level / 5),
                    0, offset2.getInterpolatedValueAt(level / 5), borderRadius);
            g.drawImage(shadow2, 0, 0, null);

            g.dispose();
        }

        return shadow;
    }

    /**
     * Creates a {@link BufferedImage} containing a shadow projected from a
     * circular component of the given radius.
     *
     * @param radius the radius length, in pixels
     * @param level  the elevation level [0~5]
     * @return A {@link BufferedImage} with the contents of the shadow for a
     *         circular component of the given radius.
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

    private static void makeShadow(BufferedImage shadow, float opacity, float radius, float leftOffset, float topOffset, int borderRadius) {
        Graphics2D g2 = shadow.createGraphics();
        g2.setColor(new Color(0, 0, 0, opacity));
        g2.fill(new RoundRectangle2D.Float(OFFSET_LEFT + leftOffset, OFFSET_TOP + topOffset,
                shadow.getWidth() - OFFSET_LEFT - OFFSET_RIGHT, shadow.getHeight() - OFFSET_TOP - OFFSET_BOTTOM, borderRadius*2, borderRadius*2));
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
    
    private int pWd, pHt, pRd;
    private double pLv;
    private Type pTp;
    private BufferedImage shadowBg;
    
    /**
     * The types of shadow available for rendering.
     */
    public static enum Type {
        /**
         * A square, classic shadow. For panels, windows and paper components
         * in general.
         */
        SQUARE,
        /**
         * A circular, rounded shadow. Mainly for specific components like FABs.
         */
        CIRCULAR
    }
    
    /**
     * Default constructor for a {@code MaterialShadow}. It is recommended to
     * keep a single instance for each component that requires it. The
     * components bundled in this library already handle this by themselves.
     */
    public MaterialShadow() {}
    
    /**
     * Renders this {@link MaterialShadow} into a {@link BufferedImage} and
     * returns it. A copy of the latest render is kept in case a shadow of the
     * same dimensions and elevation is needed in order to decrease CPU usage
     * when the component is idle.
     * @param width  the witdh of the square component casting a shadow, or
     *               diameter if it is circular.
     * @param height the height of the square component casting a shadow.
     * @param radius the radius of the borders of a square component casting a
     *               shadow.
     * @param level  the depth of the shadow [0~5]
     * @param type   the type of projected shadow, either square or circular
     * @return A {@link BufferedImage} with the contents of the shadow.
     * @see Type#SQUARE
     * @see Type#CIRCULAR
     */
    public BufferedImage render(int width, int height, int radius, double level, Type type) {
        if (pWd != width || pHt != height || pRd != radius || pLv != level || pTp != type) {
            switch (type) {
                case SQUARE:
                    shadowBg = MaterialShadow.renderShadow(width, height, level, radius);
                    break;
                case CIRCULAR:
                    shadowBg = MaterialShadow.renderCircularShadow(width, level);
                    break;
            }
            pWd = width;
            pHt = height;
            pRd = radius;
            pLv = level;
            pTp = type;
        }
        return shadowBg;
    }
}
