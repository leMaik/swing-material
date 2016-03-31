package de.craften.ui.swingmaterial;


import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A ripple effect.
 */
public class RippleEffect {
    private final List<RippleAnimation> ripples = new ArrayList<>();
    private final JComponent target;
    private final SwingTimerTimingSource timer;

    private RippleEffect(final JComponent component) {
        this.target = component;

        timer = new SwingTimerTimingSource();
        timer.init();

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final RippleAnimation ripple = new RippleAnimation(e.getPoint());
                ripples.add(ripple);
                ripple.start();
            }
        });
    }

    /**
     * Paints this effect.
     *
     * @param g canvas
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (RippleAnimation rippleAnimation : ripples) {
            float rippleOpacity = (float) rippleAnimation.rippleOpacity;
            Point rippleCenter = rippleAnimation.rippleCenter;
            int rippleRadius = rippleAnimation.rippleRadius;

            Color fg = target.getForeground();
            g2.setColor(new Color(fg.getRed() / 255f, fg.getGreen() / 255f, fg.getBlue() / 255f, rippleOpacity));
            g2.fillOval(rippleCenter.x - MaterialShadow.OFFSET_LEFT - rippleRadius, rippleCenter.y - MaterialShadow.OFFSET_TOP - rippleRadius, 2 * rippleRadius, 2 * rippleRadius);
        }
    }

    /**
     * Creates a ripple effect for the given component. You need to call {@link #paint(Graphics)} in your
     * drawing method to actually paint this effect.
     *
     * @param target target component
     * @return ripple effect for that component
     * @see MaterialButton for an example of how the ripple effect is used
     */
    public static RippleEffect applyTo(JComponent target) {
        return new RippleEffect(target);
    }

    /**
     * A ripple animation (one ripple circle after one click).
     */
    public class RippleAnimation {
        private final Point rippleCenter;
        private int rippleRadius = 25;
        private double rippleOpacity = 0;

        private RippleAnimation(Point rippleCenter) {
            this.rippleCenter = rippleCenter;
        }

        void start() {
            rippleCenter.setLocation(rippleCenter);
            Animator rippleAnimator = new Animator.Builder(timer)
                    .setDuration(1000, TimeUnit.MILLISECONDS)
                    .setEndBehavior(Animator.EndBehavior.HOLD)
                    .setInterpolator(new AccelerationInterpolator(0.2, 0.19))
                    .addTarget(PropertySetter.getTarget(this, "rippleRadius", 0, 100, target.getWidth(), target.getWidth()))
                    .addTarget(PropertySetter.getTarget(this, "rippleOpacity", 0.0, 0.4, 0.3, 0.0))
                    .build();
            rippleAnimator.addTarget(new TimingTargetAdapter() {
                @Override
                public void end(Animator source) {
                    ripples.remove(RippleAnimation.this);
                }
            });
            rippleAnimator.start();
        }

        @Deprecated
        public double getRippleOpacity() {
            return rippleOpacity;
        }

        @Deprecated
        public void setRippleOpacity(double rippleOpacity) {
            this.rippleOpacity = rippleOpacity;
            target.repaint();
        }

        @Deprecated
        public int getRippleRadius() {
            return rippleRadius;
        }

        @Deprecated
        public void setRippleRadius(int rippleRadius) {
            this.rippleRadius = rippleRadius;
            target.repaint();
        }
    }
}
