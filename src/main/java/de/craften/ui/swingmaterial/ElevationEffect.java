package de.craften.ui.swingmaterial;

import de.craften.ui.swingmaterial.util.SafePropertySetter;
import org.jdesktop.core.animation.timing.Animator;
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
    protected final JComponent target;
    private Animator animator;
    protected final SafePropertySetter.Property<Double> level;
    protected int targetLevel = 0;

    protected final MaterialShadow shadow;
    protected int borderRadius = 2;

    private ElevationEffect(final JComponent component, int level) {
        this.target = component;

        timer = new SwingTimerTimingSource();
        timer.init();

        this.level = SafePropertySetter.animatableProperty(target, (double) level);
        this.targetLevel = level;
        shadow = new MaterialShadow();
    }

    /**
     * Gets the elevation level.
     * @return elevation level [0~5]
     */
    public int getLevel() {
        return targetLevel;
    }

    /**
     * Sets the elevation level.
     * @param level elevation level [0~5]
     */
    public void setLevel(int level) {
        if (target.isShowing()) {
            if (level != targetLevel) {
                if (animator != null) {
                    animator.stop();
                }
                animator = new Animator.Builder(timer)
                        .setDuration(500, TimeUnit.MILLISECONDS)
                        .setEndBehavior(Animator.EndBehavior.HOLD)
                        .setInterpolator(new SplineInterpolator(0.55, 0, 0.1, 1))
                        .addTarget(SafePropertySetter.getTarget(this.level, this.level.getValue(), (double) level))
                        .build();
                animator.start();
            } else {
                animator = null;
            }
        } else {
            animator = null;
            this.level.setValue((double)level);
        }
        targetLevel = level;
    }

    /**
     * Gets the current border radius of the component casting a shadow. This
     * should be updated by the target component if such a property exists for
     * it and is modified.
     * @return the current border radius casted on the shadow, in pixels.
     */
    public int getBorderRadius() {
        return borderRadius;
    }

    /**
     * Sets the current border radius of the component casting a shadow. This
     * should be updated by the target component if such a property exists for
     * it and is modified.
     * @param borderRadius the new border radius casted on the shadow, in pixels.
     */
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }
    
    /**
     * Paints this effect.
     * @param g canvas
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(target.getParent().getBackground());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g.drawImage(shadow.render(target.getWidth(), target.getHeight(), borderRadius, level.getValue(), MaterialShadow.Type.SQUARE), 0, 0, null);
    }

    /**
     * Creates an elevation effect for the given component. Each component is
     * responsible of calling {@link #paint(Graphics)} in order to display the
     * effect. For this to work, there should be an offset between the contents
     * of the component and its actual bounds, these values can be found in
     * {@link MaterialShadow}.
     * @param target the target of the resulting {@code ElevationEffect}
     * @param level  the initial elevation level [0~5]
     * @return an {@code ElevationEffect} object providing support for painting
     *         ripples
     * @see MaterialShadow#OFFSET_TOP
     * @see MaterialShadow#OFFSET_BOTTOM
     * @see MaterialShadow#OFFSET_LEFT
     * @see MaterialShadow#OFFSET_RIGHT
     */
    public static ElevationEffect applyTo(JComponent target, int level) {
        return new ElevationEffect(target, level);
    }

    /**
     * Creates an elevation effect with a circular shadow for the given
     * component. Each component is responsible of calling {@link
     * #paint(Graphics)} in order to display the effect. For this to work,
     * there should be an offset between the contents of the component and its
     * actual bounds, these values can be found in {@link MaterialShadow}.
     * @param target the target of the resulting {@code ElevationEffect}
     * @param level  the initial elevation level [0~5]
     * @return an {@code ElevationEffect} object providing support for painting
     *         ripples
     * @see MaterialShadow#OFFSET_TOP
     * @see MaterialShadow#OFFSET_BOTTOM
     * @see MaterialShadow#OFFSET_LEFT
     * @see MaterialShadow#OFFSET_RIGHT
     */
    public static ElevationEffect applyCirularTo(JComponent target, int level) {
        return new ElevationEffect.Circular(target, level);
    }

    /**
     * An elevation effect with a circular shadow.
     */
    public static class Circular extends ElevationEffect {
        private Circular(JComponent component, int level) {
            super(component, level);
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(shadow.render(target.getWidth(), target.getHeight(), borderRadius, level.getValue(), MaterialShadow.Type.CIRCULAR), 0, 0, null);
        }
    }
}
