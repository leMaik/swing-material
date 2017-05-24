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
        setUndecorated(true);
        if (Utils.isTranslucencySupported()) {
            setBackground(MaterialColor.TRANSPARENT);
            //This default behavior should NOT be set here!
            //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            super.setContentPane((shadowPane = new ShadowPane()));
            contentPane = new JPanel();
            //shadowPane.setLayout(new BorderLayout());
            shadowPane.add(contentPane);
            contentPane.setLayout(null);
            contentPane.setVisible(true);
        } else {
            contentPane = super.getContentPane();
            contentPane.setLayout(null);
        }
        getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
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
        getContentPane().setBackground(MaterialColor.GREY_850);
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
        getContentPane().setBackground(MaterialColor.GREY_50);
        getContentPane().setForeground(MaterialColor.GREY_800);
        return this;
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #setWindowLocation(int, int)}.
     * @param x the <i>x</i>-coordinate of the new location's
     *          top-left corner in the parent's coordinate space
     * @param y the <i>y</i>-coordinate of the new location's
     *          top-left corner in the parent's coordinate space
     * @deprecated use {@link #setWindowLocation(int, int)} instead.
     */
    @Override
    @Deprecated
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
    }
    
    /**
     * Alternative method to {@link JFrame#setLocation(int, int)}. Use it to set
     * a new location on screen for this window, this method takes into
     * consideration the shadow offsets so you don't have to worry about it.
     * @param x the x-coordinate of the new location's top-left corner in the desktop
     * @param y the y-coordinate of the new location's top-left corner in the desktop
     */
    public void setWindowLocation(int x, int y) {
        if (!isShadowed()) {
            super.setLocation(x, y);
        } else {
            super.setLocation(x - MaterialShadow.OFFSET_LEFT, y - MaterialShadow.OFFSET_TOP);
        }
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #setWindowLocation(java.awt.Point)}.
     * @param p the point defining the top-left corner
     *          of the new location, given in the coordinate space of this
     *          component's parent
     * @deprecated use {@link #setWindowLocation(java.awt.Point)} instead.
     */
    @Override
    @Deprecated
    public void setLocation(Point p) {
        super.setLocation(p);
    }
    
    /**
     * Alternative method to {@link JFrame#setLocation(java.awt.Point)}. Use it
     * to set a new location on screen for this window, this method takes into
     * consideration the shadow offsets so you don't have to worry about it.
     * @param p the point defining the top-left corner of the new location in the desktop
     */
    public void setWindowLocation(Point p) {
        this.setWindowLocation(p.x, p.y);
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #getXLocation()}.
     * @deprecated use {@link #getXLocation()} instead.
     */
    @Override
    @Deprecated
    public int getX() {
        return super.getX();
    }
    
    /**
     * Alternative method to {@link JFrame#getX()}. Use it to get the location
     * of this window in the desktop, excluding any margins present because of
     * the shadow.
     * @return the current x coordinate of this window's origin
     */
    public int getXLocation() {
        return super.getX() + (!isShadowed() ? 0:MaterialShadow.OFFSET_LEFT);
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #getYLocation()}.
     * @deprecated use {@link #getYLocation()} instead.
     */
    @Override
    @Deprecated
    public int getY() {
        return super.getY();
    }
    
    /**
     * Alternative method to {@link JFrame#getY()}. Use it to get the location
     * of this window in the desktop, excluding any margins present because of
     * the shadow.
     * @return the current y coordinate of this window's origin
     */
    public int getYLocation() {
        return super.getY() + (!isShadowed() ? 0:MaterialShadow.OFFSET_TOP);
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #getWindowLocation()}.
     * @deprecated use {@link #getWindowLocation()} instead.
     */
    @Override
    @Deprecated
    public Point getLocation() {
        return super.getLocation();
    }
    
    /**
     * Alternative method to {@link JFrame#getLocation()}. Use it to get the
     * location of this window in the desktop, excluding any margins present
     * because of the shadow.
     * @return the current location of this window's origin
     */
    public Point getWindowLocation() {
        return new Point(getXLocation(), getYLocation());
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #getWindowWidth()}.
     * @deprecated use {@link #getWindowWidth()} instead.
     */
    @Override
    @Deprecated
    public int getWidth() {
        return super.getWidth();
    }
    
    /**
     * Alternative method to {@link JFrame#getWidth()}. Use it to get the width
     * of this window in the desktop, excluding any margins present because of
     * the shadow.
     * @return the current width of this window
     */
    public int getWindowWidth() {
        return super.getWidth() - (!isShadowed() ?
                0:MaterialShadow.OFFSET_LEFT+MaterialShadow.OFFSET_RIGHT);
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #getWindowHeight()}.
     * @deprecated use {@link #getWindowHeight()} instead.
     */
    @Override
    @Deprecated
    public int getHeight() {
        return super.getHeight();
    }
    
    /**
     * Alternative method to {@link JFrame#getHeight()}. Use it to get the
     * height of this window in the desktop, excluding any margins present
     * because of the shadow.
     * @return the current height of this window
     */
    public int getWindowHeight() {
        return super.getHeight() - (!isShadowed() ?
                0:MaterialShadow.OFFSET_TOP+MaterialShadow.OFFSET_BOTTOM);
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #setWindowSize(int, int)}.
     * @param width  the new width of this component in pixels
     * @param height the new height of this component in pixels
     * @deprecated use {@link #setWindowSize(int, int)} instead.
     */
    @Override
    @Deprecated
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }
    
    /**
     * Alternative method to {@link JFrame#setSize(int, int)}. Use it to set the
     * size of this window, and any margins needed for the shadow will be
     * automatically added.
     * @param width  the new width for this window
     * @param height the new height for this window
     */
    public void setWindowSize(int width, int height) {
        if (!isShadowed()) {
            super.setSize(width, height);
        } else {
            super.setSize(width 
                + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT,
                height + MaterialShadow.OFFSET_TOP
                + MaterialShadow.OFFSET_BOTTOM);
        }
        if (!isVisible()) {
            doLayout();
        }
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #setWindowSize(java.awt.Dimension)}.
     * @param d the dimension specifying the new size of this component
     * @deprecated use {@link #setWindowSize(java.awt.Dimension)} instead.
     */
    @Override
    @Deprecated
    public void setSize(Dimension d) {
        super.setSize(d);
    }
    
    /**
     * Alternative method to {@link JFrame#setSize(java.awt.Dimension)}. Use it
     * to set the size of this window, and any margins needed for the shadow
     * will be automatically added.
     * @param d a {@link Dimension} representing the new size for this window
     */
    public void setWindowSize(Dimension d) {
        //contentPane.setSize(d);
        this.setWindowSize(d.width, d.height);
    }
    
    /**
     * {@inheritDoc}
     * <p/>
     * <b>NOTE:</b> If this window is casting a shadow, calling this method
     * might provide inaccurate results unless you take into account the margin
     * constants found in {@link MaterialShadow}. In order to make things easier
     * to handle, it is preferable to use {@link #getWindowSize()}.
     * @return a {@link Dimension} object that indicates the size of this window
     * @deprecated use {@link #getWindowSize()} instead.
     */
    @Override
    @Deprecated
    public Dimension getSize() {
        return super.getSize();
    }
    
    /**
     * Alternative method to {@link JFrame#setSize(java.awt.Dimension)}, returns
     * the size of this window in the form of a {@link Dimension} object. Use it
     * to get the size of this window in the desktop, excluding any margins that
     * could be present because of the shadow. The {@code height} field of the
     * {@code Dimension} object contains this window's height, and the
     * {@code width} field contains this window's width.
     * @return a {@link Dimension} object that indicates the size of this window
     * @see #setWindowSize
     */
    public Dimension getWindowSize() {
        return new Dimension(getWindowWidth(), getWindowHeight());
    }
    
    @Override
    public void setExtendedState(int state) {
        if (shadowPane != null)
            shadowPane.setDeployed((state & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH);
        super.setExtendedState(state);
    }
    
    /**
     * Use this method to check if this {@code MaterialFrame} is casting a
     * shadow. This affects how the window is positioned.<br/>
     * <br/>
     * For a window to be shadowed, the current GraphicsEnvironment must support
     * per-pixel translucency. Also, when maximized, the shadow is automatically
     * hidden so the contents can fill the available area.
     * @return {@code true} if this window is currently casting a shadow on the
     *         desktop, {@code false} otherwise.
     */
    private boolean isShadowed() {
        return shadowPane != null && shadowPane.isDeployed();
    }
    
    /**
     * A {@code ShadowPane} provides the space needed for the shadow of a {@link
     * MaterialWindow} to be displayed.
     */
    protected class ShadowPane extends JPanel {
        private final MaterialShadow shadow;
        private boolean deployed;

        ShadowPane() {
            shadow = new MaterialShadow();
            setLayout(new BorderLayout());
            setOpaque(false);
            setDeployed(true);
        }

        /**
         * Checks if this {@link ShadowPane} is deployed or not. When
         * deployed, this panel will display continously a shadow below the
         * window.
         * @return {@code true} if this {@link ShadowPane} is deployed,
         *         {@code false} otherwise.
         */
        public boolean isDeployed() {
            return deployed;
        }

        /**
         * Allows to set if this {@link ShadowPane} is deployed or not. When
         * deployed, this panel will display continously a shadow below the
         * window.
         * @param deployed a {@code boolean} being {@code true} to deploy the
         *                 shadow, and {@code false} to hide it
         */
        public void setDeployed(boolean deployed) {
            this.deployed = deployed;
            if (deployed) {
                setBorder(new EmptyBorder(MaterialShadow.OFFSET_TOP, MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_BOTTOM, MaterialShadow.OFFSET_RIGHT));
            } else {
                setBorder(new EmptyBorder(0, 0, 0, 0));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isDeployed()) {
                Graphics2D g2d = (Graphics2D) g;
                g.clearRect(0, 0, getWidth(), getHeight());
                g2d.setComposite(AlphaComposite.SrcOver);
                g2d.drawImage(shadow.render(getWidth(), getHeight(), 1, 2, MaterialShadow.Type.SQUARE), 0, 0, null);

                g.setClip(new RoundRectangle2D.Float(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP,
                        getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT,
                        getHeight() - MaterialShadow.OFFSET_TOP - MaterialShadow.OFFSET_BOTTOM, 3, 3));
                super.paintComponent(g);
            }
        }
    }
}