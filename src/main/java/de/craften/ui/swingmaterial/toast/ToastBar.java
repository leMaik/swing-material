package de.craften.ui.swingmaterial.toast;

import de.craften.ui.swingmaterial.util.SafePropertySetter;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.event.*;
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
    private boolean animationRunning = false;

    public ToastBar() {
        setLayout(null);
        timer = new SwingTimerTimingSource();
        timer.init();
    }

    /**
     * Displays the toast or queues it for display if another toast is already displayed.
     *
     * @param toast toast
     */
    public void display(Toast toast) {
        toasts.add(toast);
        if (toasts.size() == 1 && !animationRunning) {
            displayNextToast();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return animationRunning && super.contains(x, y);
    }

    private synchronized void displayNextToast() {
        if (animationRunning) {
            return;
        }

        final Toast currentToast = toasts.poll();
        if (currentToast != null) {
            add(currentToast);
            currentToast.setSize(getWidth(), 48);

            final ComponentListener resizeListener = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    currentToast.setSize(getWidth(), currentToast.getHeight());
                    repaint();
                }
            };
            addComponentListener(resizeListener);

            animationRunning = true;
            new Animator.Builder(timer)
                    .setDuration(250, TimeUnit.MILLISECONDS)
                    .setInterpolator(new SplineInterpolator(0.1, 0.3, 0.45, 1))
                    .addTarget(SafePropertySetter.getTarget(new SafePropertySetter.Setter<Integer>() {
                        @Override
                        public void setValue(Integer value) {
                            if (value != null) {
                                currentToast.setYOffset(value);
                            }
                            repaint();
                        }
                    }, getHeight(), 0))
                    .addTarget(new TimingTargetAdapter() {
                        @Override
                        public void end(Animator source) {
                            currentToast.setYOffset(0);
                        }
                    })
                    .build().start();

            new Animator.Builder(timer)
                    .setStartDelay(250 + 3000, TimeUnit.MILLISECONDS)
                    .setDuration(250, TimeUnit.MILLISECONDS)
                    .setInterpolator(new SplineInterpolator(0.55, 0, 0.9, 0.7))
                    .addTarget(SafePropertySetter.getTarget(new SafePropertySetter.Setter<Integer>() {
                        @Override
                        public void setValue(Integer value) {
                            if (value != null) {
                                currentToast.setYOffset(value);
                            }
                            repaint();
                        }
                    }, 0, getHeight() + 1))
                    .addTarget(new TimingTargetAdapter() {
                        @Override
                        public void end(Animator source) {
                            removeComponentListener(resizeListener);
                            remove(currentToast);

                            animationRunning = false;
                            new javax.swing.Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    displayNextToast();
                                }
                            }).start();
                        }
                    })
                    .build().start();
        }
    }
}
