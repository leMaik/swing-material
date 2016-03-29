package de.craften.ui.swingmaterial;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;


/**
 * A Material Design button.
 *
 * @see <a href="https://www.google.com/design/spec/components/buttons.html">Buttons (Google design guidelines)</a>
 */
public class MaterialButton extends JButton {
    private Animator rippleAnimator;
    private int rippleRadius = 25;
    private double rippleOpacity = 0;
    private Point rippleCenter = new Point(0, 0);
    private BufferedImage shadow;
    private boolean raised = false;
    private boolean isMousePressed = false;

    public MaterialButton() {
        final SwingTimerTimingSource timer = new SwingTimerTimingSource();
        timer.init();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                isMousePressed = true;

                if (rippleAnimator != null) {
                    rippleAnimator.stop();
                }

                rippleCenter.setLocation(mouseEvent.getPoint());
                rippleAnimator = new Animator.Builder(timer)
                        .setDuration(1000, TimeUnit.MILLISECONDS)
                        .setEndBehavior(Animator.EndBehavior.HOLD)
                        .setInterpolator(new AccelerationInterpolator(0.2, 0.19))
                        .addTarget(PropertySetter.getTarget(MaterialButton.this, "rippleRadius", 0, 100, getWidth(), getWidth()))
                        .addTarget(PropertySetter.getTarget(MaterialButton.this, "rippleOpacity", 0.0, 0.4, 0.3, 0.0))
                        .build();
                rippleAnimator.start();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                isMousePressed = false;
                repaint();
            }
        });

        setFont(Roboto.MEDIUM.deriveFont(14f));
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        shadow = null;
    }

    @Override
    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
        shadow = null;
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        shadow = null;
    }

    @Deprecated
    public int getRippleRadius() {
        return rippleRadius;
    }

    @Deprecated
    public void setRippleRadius(int rippleRadius) {
        this.rippleRadius = rippleRadius;
        repaint();
    }

    @Deprecated
    public double getRippleOpacity() {
        return rippleOpacity;
    }

    @Deprecated
    public void setRippleOpacity(double rippleOpacity) {
        this.rippleOpacity = rippleOpacity;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getParent().getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (isEnabled()) {
            if (shadow == null || shadow.getWidth() != getWidth() || shadow.getHeight() != getHeight()) {
                if (isMousePressed) {
                    shadow = MaterialShadow.renderShadow(getWidth(), getHeight(), 3);
                } else if (isFocusOwner() || isRaised()) {
                    shadow = MaterialShadow.renderShadow(getWidth(), getHeight(), 1);
                }
            }
            g2.drawImage(shadow, 0, 0, null);
        }

        g2.translate(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP);

        final int offset_lr = MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT;
        final int offset_td = MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM;

        if (isEnabled()) {
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));

            if (isFocusOwner()) {
                g2.setColor(new Color(1, 1, 1, 0.2f));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));
            }
        } else {
            Color bg = getBackground();
            g2.setColor(new Color(bg.getRed() / 255f, bg.getGreen() / 255f, bg.getBlue() / 255f, 0.6f));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));
        }

        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - offset_lr - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - offset_td - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setFont(getFont());
        if (isEnabled()) {
            g2.setColor(getForeground());
        } else {
            Color fg = getForeground();
            g2.setColor(new Color(fg.getRed() / 255f, fg.getGreen() / 255f, fg.getBlue() / 255f, 0.6f));
        }
        g2.drawString(getText().toUpperCase(), x, y);

        if (isEnabled()) {
            Color fg = getForeground();
            g2.setColor(new Color(fg.getRed() / 255f, fg.getGreen() / 255f, fg.getBlue() / 255f, (float) rippleOpacity));
            g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, 3, 3));
            g2.fillOval(rippleCenter.x - MaterialShadow.OFFSET_LEFT - rippleRadius, rippleCenter.y - MaterialShadow.OFFSET_TOP - rippleRadius, 2 * rippleRadius, 2 * rippleRadius);
        }
    }

    @Override
    protected void paintBorder(Graphics graphics) {
    }

    public boolean isRaised() {
        return raised;
    }

    public void setRaised(boolean raised) {
        this.raised = raised;
        shadow = null;
        repaint();
    }
}
