package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A {@code MaterialWindow} is the prefered top-level container for your
 * Material applications. It includes its own nice Material shadow out of the
 * box, as well as some convenient methods.
 */
public class MaterialWindow extends JFrame {
    /**
     * This is the panel that contains the components placed inside this window.
     * If you are wondering what happened to the JFrame's {@code contentPane},
     * see {@link #shadowPane}.
     */
    private Container contentPane;
    /**
     * This panel is used as background of the window itself, so it drops a
     * shadow.
     * @see ShadowPane
     */
    protected ShadowPane shadowPane;
    
    /**
     * Default constructor for a {@link MaterialWindow}. A container for the
     * shadow is deployed if and only if the current graphics evironment
     * supports per-prixel translucency for top-level containers (windows in
     * general).
     */
    public MaterialWindow() {
        super();
        setUndecorated(true);
        if (Utils.isTranslucencySupported()) {
            setBackground(new Color(255, 255, 255, 0));
            //This default behavior should NOT be set here!
            //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            super.setContentPane((shadowPane = new ShadowPane()));
            contentPane = new JPanel();
            shadowPane.add(contentPane);
        } else {
            contentPane = super.getContentPane();
        }
        getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 3, 3));
            }
        });
        light();
    }

    @Override
    public Container getContentPane() {
        return contentPane;
    }

    @Override
    public void setContentPane(Container contentPane) {
        this.contentPane = contentPane;
    }
    
    /**
     * Chainable method to quickly get a {@link MaterialWindow} with a dark
     * background and white foreground. You can append it to your constructor
     * like this: {@code MaterialWindow myWindow = new MaterialWindow.dark()} or
     * just use it alone to switch the foreground/background colors.
     * @return this window
     */
    public MaterialWindow dark() {
        getContentPane().setBackground(MaterialColor.GREY_900);
        getContentPane().setForeground(MaterialColor.WHITE);
        return this;
    }
    
    /**
     * Chainable method to quickly get a {@link MaterialWindow} with a white
     * background and black foreground. You can append it to your constructor
     * like this: {@code MaterialWindow myWindow = new MaterialWindow.dark()} or
     * just use it alone to switch the foreground/background colors.
     * @return this window
     */
    public MaterialWindow light() {
        getContentPane().setBackground(MaterialColor.WHITE);
        getContentPane().setForeground(MaterialColor.GREY_900);
        return this;
    }
    
    /**
     * Alternative method to {@link JFrame#setLocation(int, int)}. Use it to set
     * a new location on screen for this window, this method takes into
     * consideration the shadow offsets so you don't have to worry about it.
     * @param x the x-coordinate of the new location's top-left corner in the desktop
     * @param y the y-coordinate of the new location's top-left corner in the desktop
     */
    public void setWindowLocation(int x, int y) {
        if (shadowPane == null) {
            super.setLocation(x, y);
        } else {
            super.setLocation(x - MaterialShadow.OFFSET_LEFT, y - MaterialShadow.OFFSET_TOP);
        }
    }
    
    /**
     * Alternative method to {@link JFrame#setLocation(java.awt.Point)}. Use it
     * to set a new location on screen for this window, this method takes into
     * consideration the shadow offsets so you don't have to worry about it.
     * @param p the point defining the top-left corner of the new location in the desktop
     */
    public void setWindowLocation(Point p) {
        if (shadowPane == null) {
            super.setLocation(p);
        } else {
            super.setLocation(p.x - MaterialShadow.OFFSET_LEFT, p.y - MaterialShadow.OFFSET_TOP);
        }
    }
    
    /**
     * Alternative method to {@link JFrame#getX()}. Use it to get the location
     * of this frame in the desktop, excluding any margins present because of
     * the shadow.
     * @return the current x coordinate of this window's origin
     */
    public int getXLocation() {
        return super.getX() + (shadowPane == null ? 0:MaterialShadow.OFFSET_LEFT);
    }
    
    /**
     * Alternative method to {@link JFrame#getY()}. Use it to get the location
     * of this frame in the desktop, excluding any margins present because of
     * the shadow.
     * @return the current y coordinate of this window's origin
     */
    public int getYLocation() {
        return super.getY() + (shadowPane == null ? 0:MaterialShadow.OFFSET_TOP);
    }
    
    /**
     * Alternative method to {@link JFrame#getLocation()}. Use it to get the
     * location of this frame in the desktop, excluding any margins present
     * because of the shadow.
     * @return the current location of this window's origin
     */
    public Point getWindowLocation() {
        return new Point(getXLocation(), getYLocation());
    }
    
    /**
     * Alternative method to {@link JFrame#getWidth()}. Use it to get the width
     * of this frame in the desktop, excluding any margins present because of
     * the shadow.
     * @return the current width of this window
     */
    public int getWindowWidth() {
        return super.getWidth() - (shadowPane == null ?
                0:MaterialShadow.OFFSET_LEFT+MaterialShadow.OFFSET_RIGHT);
    }
    
    /**
     * Alternative method to {@link JFrame#getHeight()}. Use it to get the
     * height of this frame in the desktop, excluding any margins present
     * because of the shadow.
     * @return the current height of this window
     */
    public int getWindowHeight() {
        return super.getHeight() - (shadowPane == null ?
                0:MaterialShadow.OFFSET_TOP+MaterialShadow.OFFSET_BOTTOM);
    }
    
    /**
     * Alternative method to {@link JFrame#setSize(int, int)}. Use it to set the
     * size of this window, and any margins needed for the shadow will be
     * automatically added.
     * @param width  the new width for this window
     * @param height the new height for this window
     */
    public void setWindowSize(int width, int height) {
        if (shadowPane == null) {
            super.setSize(width, height);
        } else {
            super.setSize(width 
                + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT,
                height + MaterialShadow.OFFSET_TOP
                + MaterialShadow.OFFSET_BOTTOM);
        }
    }
    
    /**
     * Alternative method to {@link JFrame#setSize(java.awt.Dimension)}. Use it
     * to set the size of this window, and any margins needed for the shadow
     * will be automatically added.
     * @param d a {@link Dimension} representing the new size for this window
     */
    public void setWindowSize(Dimension d) {
        contentPane.setSize(d);
        if (shadowPane == null) {
            super.setSize(d);
        } else {
            super.setSize(new Dimension(d.width 
                    + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT,
                    d.height + MaterialShadow.OFFSET_TOP
                    + MaterialShadow.OFFSET_BOTTOM));
        }
    }
    
    /**
     * A {@code ShadowPane} provides the space needed for the shadow of a {@link
     * MaterialWindow} to be displayed.
     */
    protected class ShadowPane extends JPanel {
        private final MaterialShadow shadow;

        ShadowPane() {
            shadow = new MaterialShadow();
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(MaterialShadow.OFFSET_TOP, MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_BOTTOM, MaterialShadow.OFFSET_RIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g.clearRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.drawImage(shadow.render(getWidth(), getHeight(), 2, MaterialShadow.Type.SQUARE), 0, 0, null);

            g.setClip(new RoundRectangle2D.Float(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP,
                    getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT,
                    getHeight() - MaterialShadow.OFFSET_TOP - MaterialShadow.OFFSET_BOTTOM, 3, 3));
            super.paintComponent(g);
        }
    }
}