package de.craften.ui.swingmaterial.util;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.awt.Color.blue;
import static java.awt.Color.green;

/**
 * A fast Gaussian Blur implementation (linear time).
 *
 * @see <a href="http://blog.ivank.net/fastest-gaussian-blur.html">Fastest Gaussian Blur (in linear time)</a>
 */
public class FastGaussianBlur {
    public static void blur(BufferedImage image, final double radius) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final int[] pixels = new int[w * h];
        image.getRGB(0, 0, w, h, pixels, 0, w);

        int[][] channels = new int[4][];
        for (int channel = 0; channel <= 3; channel++) {
            int[] red = new int[w * h];
            int[] blurRed = new int[w * h];
            for (int i = 0; i < w * h; i++) {
                red[i] = (pixels[i] >> (channel * 8)) & 0xff;
            }
            gaussBlur_4(red, blurRed, w, h, radius);
            channels[channel] = blurRed;
        }

        for (int i = 0; i < w * h; i++) {
            pixels[i] = channels[0][i] | channels[1][i] << 8 | channels[2][i] << 16 | channels[3][i] << 24;
        }

        image.setRGB(0, 0, w, h, pixels, 0, w);
    }

    private static int[] boxesForGauss(double sigma, int n) {// standard deviation, number of boxes
        double wIdeal = Math.sqrt((12 * sigma * sigma / n) + 1);  // Ideal averaging filter width
        int wl = (int) Math.floor(wIdeal);
        if (wl % 2 == 0) {
            wl--;
        }
        int wu = wl + 2;

        double mIdeal = (12 * sigma * sigma - n * wl * wl - 4 * n * wl - 3 * n) / (-4 * wl - 4);
        int m = (int) Math.round(mIdeal);

        int[] sizes = new int[n];
        for (int i = 0; i < n; i++) {
            sizes[i] = i < m ? wl : wu;
        }
        return sizes;
    }

    private static void gaussBlur_4(int[] scl, int[] tcl, int w, int h, double r) {
        int[] bxs = boxesForGauss(r, 3);
        boxBlur_4(scl, tcl, w, h, (bxs[0] - 1) / 2);
        boxBlur_4(tcl, scl, w, h, (bxs[1] - 1) / 2);
        boxBlur_4(scl, tcl, w, h, (bxs[2] - 1) / 2);
    }

    private static void boxBlur_4(int[] scl, int[] tcl, int w, int h, double r) {
        System.arraycopy(scl, 0, tcl, 0, scl.length);
        boxBlurH_4(tcl, scl, w, h, r);
        boxBlurT_4(scl, tcl, w, h, r);
    }

    private static void boxBlurH_4(int[] scl, int[] tcl, int w, int h, double r) {
        double iarr = 1.0 / (r + r + 1);
        for (int i = 0; i < h; i++) {
            int ti = i * w;
            int li = ti;
            int ri = (int) (ti + r);
            int fv = scl[ti];
            int lv = scl[ti + w - 1];
            double val = (r + 1) * fv;
            for (int j = 0; j < r; j++) {
                val += scl[ti + j];
            }
            for (int j = 0; j <= r; j++) {
                val += scl[ri++] - fv;
                tcl[ti++] = (int) Math.round(val * iarr);
            }
            for (int j = (int) (r + 1); j < w - r; j++) {
                val += scl[ri++] - scl[li++];
                tcl[ti++] = (int) Math.round(val * iarr);
            }
            for (int j = (int) (w - r); j < w; j++) {
                val += lv - scl[li++];
                tcl[ti++] = (int) Math.round(val * iarr);
            }
        }
    }

    private static void boxBlurT_4(int[] scl, int[] tcl, int w, int h, double r) {
        double iarr = 1.0 / (r + r + 1);
        for (int i = 0; i < w; i++) {
            int ti = i;
            int li = ti;
            int ri = (int) (ti + r * w);
            int fv = scl[ti];
            int lv = scl[ti + w * (h - 1)];
            double val = (r + 1) * fv;
            for (int j = 0; j < r; j++) {
                val += scl[ti + j * w];
            }
            for (int j = 0; j <= r; j++) {
                val += scl[ri] - fv;
                tcl[ti] = (int) Math.round(val * iarr);
                ri += w;
                ti += w;
            }
            for (int j = (int) (r + 1); j < h - r; j++) {
                val += scl[ri] - scl[li];
                tcl[ti] = (int) Math.round(val * iarr);
                li += w;
                ri += w;
                ti += w;
            }
            for (int j = (int) (h - r); j < h; j++) {
                val += lv - scl[li];
                tcl[ti] = (int) Math.round(val * iarr);
                li += w;
                ti += w;
            }
        }
    }
}
