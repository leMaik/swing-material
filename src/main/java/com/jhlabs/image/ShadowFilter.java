/**
 * Copyright 2005 Huxtable.com. All rights reserved.
 */
package com.jhlabs.image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class ShadowFilter extends AbstractBufferedImageOp {

    static final long serialVersionUID = 6310370419462785691L;

    private int radius = 5;
    private int xOffset = 5;
    private int yOffset = 5;
    private float opacity = 0.5f;
    private boolean addMargins = false;
    private boolean shadowOnly = false;
    private int shadowColor = 0xff000000;

    public ShadowFilter() {
    }

    public ShadowFilter(int radius, int xOffset, int yOffset, float opacity) {
        this.radius = radius;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.opacity = opacity;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    /**
     * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will take.
     *
     * @param radius the radius of the blur in pixels.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get the radius of the kernel.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setAddMargins(boolean addMargins) {
        this.addMargins = addMargins;
    }

    public boolean getAddMargins() {
        return addMargins;
    }

    public void setShadowOnly(boolean shadowOnly) {
        this.shadowOnly = shadowOnly;
    }

    public boolean getShadowOnly() {
        return shadowOnly;
    }

    protected void transformSpace(Rectangle r) {
        if (addMargins) {
            r.width += Math.abs(xOffset) + 2 * radius;
            r.height += Math.abs(yOffset) + 2 * radius;
        }
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        if (dst == null) {
            if (addMargins) {
                ColorModel cm = src.getColorModel();
                dst = new BufferedImage(cm, cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), cm.isAlphaPremultiplied(), null);
            } else
                dst = createCompatibleDestImage(src, null);
        }

        // Make a black mask from the image's alpha channel 
        float[][] extractAlpha = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, opacity}
        };
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        new BandCombineOp(extractAlpha, null).filter(src.getRaster(), shadow.getRaster());
        shadow = new GaussianFilter(radius).filter(shadow, null);

        Graphics2D g = dst.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (addMargins) {
            int radius2 = radius / 2;
            int topShadow = Math.max(0, radius - yOffset);
            int leftShadow = Math.max(0, radius - xOffset);
            g.translate(topShadow, leftShadow);
        }
        g.drawRenderedImage(shadow, AffineTransform.getTranslateInstance(xOffset, yOffset));
        if (!shadowOnly) {
            g.setComposite(AlphaComposite.SrcOver);
            g.drawRenderedImage(src, null);
        }
        g.dispose();

        return dst;
    }

    public String toString() {
        return "Stylize/Drop Shadow...";
    }
}