package de.craften.ui.swingmaterial;

import de.craften.ui.swingmaterial.util.SafePropertySetter;
import de.craften.ui.swingmaterial.util.SafePropertySetter.Property;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Interpolator;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * A Material Design progress spinner. The color can be set using {@link #setForeground(Color)}.
 *
 * @see <a href="https://www.google.com/design/spec/components/progress-activity.html">Progress &amp; activity (Google design guidelines)</a>
 */
public class MaterialProgressSpinner extends JComponent {
    private final Property<Integer> startArc = SafePropertySetter.animatableProperty(this, 270);
    private final Property<Integer> arcSize = SafePropertySetter.animatableProperty(this, 0);
    private final Property<Integer> rotation = SafePropertySetter.animatableProperty(this, 0);

    /**
     * Creates a new progress spinner.
     */
    public MaterialProgressSpinner() {
        //animation contants from https://github.com/PolymerElements/paper-spinner
        final int ARCSIZE = 270;
        final int ARCTIME = 1333;
        final int ARCSTARTROT = 216;

        SwingTimerTimingSource timer = new SwingTimerTimingSource();
        Animator animator = new Animator.Builder(timer)
                .setDuration(4 * ARCTIME, TimeUnit.MILLISECONDS)
                .setRepeatCount(Long.MAX_VALUE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(SafePropertySetter.getTarget(startArc, 0, -270, -2 * 270, -3 * 270, -4 * 270))
                .setInterpolator(new Interpolator() {
                    private final Interpolator spline = new SplineInterpolator(0.4, 0, 0.2, 1);

                    @Override
                    public double interpolate(double v) {
                        if (v < 0.125) {
                            return spline.interpolate(v * 8) / 4;
                        } else if (v < 0.25) {
                            return 0.25;
                        } else if (v < 0.375) {
                            return spline.interpolate((v - 0.25) * 8) / 4 + 0.25;
                        } else if (v < 0.5) {
                            return 0.5;
                        } else if (v < 0.625) {
                            return spline.interpolate((v - 0.5) * 8) / 4 + 0.5;
                        } else if (v < 0.75) {
                            return 0.75;
                        } else if (v < 0.875) {
                            return spline.interpolate((v - 0.75) * 8) / 4 + 0.75;
                        } else {
                            return 1;
                        }
                    }
                })
                .build();
        animator.start();
        Animator animator2 = new Animator.Builder(timer)
                .setDuration(ARCTIME, TimeUnit.MILLISECONDS)
                .setRepeatCount(Long.MAX_VALUE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(SafePropertySetter.getTarget(arcSize, 0, 270, 0))
                .setInterpolator(new Interpolator() {
                    private final Interpolator spline = new SplineInterpolator(0.4, 0, 0.2, 1);

                    @Override
                    public double interpolate(double v) {
                        if (v < 0.5) {
                            return spline.interpolate(v * 2) / 2;
                        } else {
                            return spline.interpolate((v - 0.5) * 2) / 2 + 0.5;
                        }
                    }
                })
                .build();
        animator2.start();
        Animator animator3 = new Animator.Builder(timer)
                .setDuration(360 * ARCTIME / (ARCSTARTROT + (360 - ARCSIZE)), TimeUnit.MILLISECONDS)
                .setRepeatCount(Long.MAX_VALUE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(SafePropertySetter.getTarget(rotation, 360, 0))
                .build();
        animator3.start();
        timer.init();

        setPreferredSize(new Dimension(50, 50));
        setLayout(null);
        setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        g2.drawArc(5, 5, getWidth() - 10, getWidth() - 10, startArc.getValue() + rotation.getValue() + 90, Math.max(1, arcSize.getValue()));
    }
}
