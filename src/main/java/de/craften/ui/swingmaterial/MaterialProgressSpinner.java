package de.craften.ui.swingmaterial;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * A Material Design progress spinner. The color can be set using {@link #setForeground(Color)}.
 *
 * @see <a href="https://www.google.com/design/spec/components/progress-activity.html">Progress &amp; activity (Google design guidelines)</a>
 */
public class MaterialProgressSpinner extends JPanel {
    private static final int DURATION = 1200;
    private int startArc = 270;
    private int arcSize = 0;
    private int rotation = 0;
    private int rotation2 = 0;

    /**
     * Creates a new progress spinner.
     */
    public MaterialProgressSpinner() {
        SwingTimerTimingSource timer = new SwingTimerTimingSource();
        Animator animator = new Animator.Builder(timer)
                .setDuration(DURATION, TimeUnit.MILLISECONDS)
                .setRepeatCount(Long.MAX_VALUE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(PropertySetter.getTarget(this, "startArc", 450, 135, 0))
                .build();
        animator.start();
        Animator animator2 = new Animator.Builder(timer)
                .setDuration(DURATION, TimeUnit.MILLISECONDS)
                .setRepeatCount(Long.MAX_VALUE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(PropertySetter.getTarget(this, "arcSize", 0, 270, 0))
                .build();
        animator2.start();
        Animator animator3 = new Animator.Builder(timer)
                .setDuration(DURATION, TimeUnit.MILLISECONDS)
                .setRepeatCount(Long.MAX_VALUE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(PropertySetter.getTarget(this, "rotation", 270, 0))
                .build();
        animator3.start();
        timer.init();

        setPreferredSize(new Dimension(50, 50));
        setLayout(null);
        setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
        setOpaque(false);
    }

    @Deprecated
    public int getStartArc() {
        return startArc;
    }

    @Deprecated
    public void setStartArc(int startArc) {
        this.startArc = startArc;
        repaint();
    }

    @Deprecated
    public int getArcSize() {
        return arcSize;
    }

    @Deprecated
    public void setArcSize(int arcSize) {
        this.arcSize = arcSize;
    }

    @Deprecated
    public int getRotation() {
        return rotation;
    }

    @Deprecated
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Deprecated
    public int getRotation2() {
        return rotation2;
    }

    @Deprecated
    public void setRotation2(int rotation2) {
        this.rotation2 = rotation2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(5, 5, 50 - 10, 50 - 10, startArc + rotation + rotation2 + 90, arcSize);
    }
}
