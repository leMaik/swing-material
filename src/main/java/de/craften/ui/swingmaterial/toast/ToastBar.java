package de.craften.ui.swingmaterial.toast;

import de.craften.ui.swingmaterial.util.SafePropertySetter;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * A bar that displays toasts.
 *
 * @see <a href="https://www.google.com/design/spec/components/snackbars-toasts.html">Snackbars and toasts</a>
 */
public class ToastBar extends JComponent {
    private final SwingTimerTimingSource timer;
    private Queue<Toast> toasts = new LinkedList<>();
    private Toast currentToast = null;
    private boolean animationRunning = false;

    public ToastBar() {
        setLayout(null);
        timer = new SwingTimerTimingSource();
        timer.init();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (currentToast != null) {
                    currentToast.setSize(getWidth(), currentToast.getHeight());
                }
            }
        });
    }

    /**
     * Displays the toast or queues it for display if another toast is already displayed.
     *
     * @param toast toast
     */
    public void display(Toast toast) {
        toasts.add(toast);
        if (toasts.size() == 1) {
            displayNextToast();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        if (currentToast != null) {
            return super.contains(x, y);
        } else {
            return false;
        }
    }

    private void displayNextToast() {
        if (animationRunning) {
            return;
        }

        currentToast = toasts.poll();
        if (currentToast != null) {
            add(currentToast);
            currentToast.setSize(getWidth(), 48);

            animationRunning = true;
            new Animator.Builder(timer)
                    .setDuration(250, TimeUnit.MILLISECONDS)
                    .setEndBehavior(Animator.EndBehavior.HOLD)
                    .setInterpolator(new SplineInterpolator(0.1, 0.3, 0.45, 1))
                    .addTarget(SafePropertySetter.getTarget(new SafePropertySetter.Setter<Integer>() {
                        @Override
                        public void setValue(Integer value) {
                            currentToast.setYOffset(value);
                            repaint();
                        }
                    }, getHeight(), 0))
                    .build().start();
            new Animator.Builder(timer)
                    .setStartDelay(250 + 3000, TimeUnit.MILLISECONDS)
                    .setDuration(250, TimeUnit.MILLISECONDS)
                    .setEndBehavior(Animator.EndBehavior.HOLD)
                    .setInterpolator(new SplineInterpolator(0.55, 0, 0.9, 0.7))
                    .addTarget(SafePropertySetter.getTarget(new SafePropertySetter.Setter<Integer>() {
                        @Override
                        public void setValue(Integer value) {
                            currentToast.setYOffset(value);
                            repaint();
                        }
                    }, 0, getHeight() + 1))
                    .addTarget(new TimingTargetAdapter() {
                        @Override
                        public void end(Animator source) {
                            if (currentToast != null) {
                                remove(currentToast);
                                currentToast = null;
                            }

                            new javax.swing.Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    animationRunning = false;
                                    displayNextToast();
                                }
                            }).start();
                        }
                    })
                    .build().start();
        }
    }
}
