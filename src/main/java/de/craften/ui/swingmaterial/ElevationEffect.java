package de.craften.ui.swingmaterial;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * An elevation effect.
 */
public class ElevationEffect {
    private final SwingTimerTimingSource timer;
    private final JComponent target;
    private Animator animator;
    private double level = 0;
    private int targetLevel = 0;

    private ElevationEffect(final JComponent component, int level) {
        this.target = component;

        timer = new SwingTimerTimingSource();
        timer.init();

        this.level = level;
        this.targetLevel = level;
    }

    /**
     * Gets the elevation level.
     *
     * @return elevation level (0..5)
     */
    public int getLevel() {
        return targetLevel;
    }

    /**
     * Sets the elevation level.
     *
     * @param level elevation level (0..5)
     */
    public void setLevel(int level) {
        if (animator != null) {
            if (level != targetLevel) {
                animator.stop();
                animator = new Animator.Builder(timer)
                        .setDuration(500, TimeUnit.MILLISECONDS)
                        .setEndBehavior(Animator.EndBehavior.HOLD)
                        .setInterpolator(new SplineInterpolator(0.55, 0, 0.1, 1))
                        .addTarget(PropertySetter.getTarget(this, "actualLevel", this.level, (double) level))
                        .build();
                animator.start();
            }
        } else {
            animator = new Animator.Builder(timer)
                    .setDuration(500, TimeUnit.MILLISECONDS)
                    .setEndBehavior(Animator.EndBehavior.HOLD)
                    .setInterpolator(new SplineInterpolator(0.55, 0, 0.1, 1))
                    .addTarget(PropertySetter.getTarget(this, "actualLevel", this.level, (double) level))
                    .build();
            animator.start();
        }
        targetLevel = level;
    }

    @Deprecated
    public double getActualLevel() {
        return level;
    }

    @Deprecated
    public void setActualLevel(double level) {
        this.level = level;
        target.repaint();
    }

    /**
     * Paints this effect.
     *
     * @param g canvas
     */
    public void paint(Graphics g) {
        g.drawImage(MaterialShadow.renderShadow(target.getWidth(), target.getHeight(), level), 0, 0, null);
    }


    /**
     * Creates an elevation effect for the given component. You need to call {@link #paint(Graphics)} in your
     * drawing method to actually paint this effect.
     *
     * @param target target component
     * @param level  initial elevation level (0..5)
     * @return elevation effect for that component
     * @see MaterialButton for an example of how the ripple effect is used
     */
    public static ElevationEffect applyTo(JComponent target, int level) {
        return new ElevationEffect(target, level);
    }
}
