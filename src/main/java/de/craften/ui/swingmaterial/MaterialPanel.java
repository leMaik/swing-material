package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A JPanel customized for Material components. What makes these panels special
 * is the possibility of assigning them an elevation level. Elevation can help
 * distinguishing elements inside a Material-based GUI, and any changes done to
 * them result in nicely animated transitions, helping to achieve that Material
 * flow.
 * <p/>
 * However, there is a catch: shadows are kinda expensive to compute, as
 * Java2D relies on the CPU for anything other than images, so having a lot
 * of elements with a given elevation (and thus, a shadow) can reduce
 * performance when these elevations change due to the triggered animations.
 * <p/>
 * Letting the components suggest a prefered size based on their contents is
 * still under development, so it is not advised to use your favorite
 * {@link LayoutManager} inside a {@code MaterialPanel} unless you set the
 * prefered, minimum and maximum size of each component by yourself. Currently,
 * the prefereable approach to follow is overriding {@link #doLayout()} and
 * taking care of any arrangements by yourself.
 */
public class MaterialPanel extends JPanel {
    private final ElevationEffect elevation;

    /**
     * Creates a new {@code MaterialPanel}. These panels cast a shadow below
     * them, although technically it is painted inside its borders. If you don't
     * need a shadow to be casted from this panel, use a {@link JPanel} instead.
     */
    public MaterialPanel() {
        elevation = ElevationEffect.applyTo(this, 1);
        setBorder(new EmptyBorder(MaterialShadow.OFFSET_TOP, MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_BOTTOM, MaterialShadow.OFFSET_RIGHT));
    }

    /**
     * Gets the elevation level of this panel. Changes in elevation trigger an
     * animated transition if the component is currently visible, so it is
     * incorrect to assume the returned value will reflect how the resulting
     * shadow looks right now.
     * @return elevation level [0~5]
     * @see ElevationEffect
     */
    public int getElevation() {
        return elevation.getLevel();
    }

    /**
     * Sets the elevation level of this panel. Changes in elevation trigger an
     * animated transition if the component is currently visible, so it will
     * take a little while for the resulting shadow to reflect the level once
     * it is set.
     * @param elevation elevation level [0~5]
     * @see ElevationEffect
     */
    public void setElevation(int elevation) {
        this.elevation.setLevel(elevation);
    }
    
    /**
     * Sets the background color of this panel.
     * Keep on mind that setting a background color in a Material component like
     * this will also set the foreground color to either white or black,
     * depending of how bright or dark is the chosen background color.
     * <p/>
     * <b>NOTE:</b> It is up to the look and feel to honor this property, some
     * may choose to ignore it. To avoid any conflicts, using the
     * <a href="https://docs.oracle.com/javase/7/docs/api/javax/swing/plaf/metal/package-summary.html">
     * Metal Look and Feel</a> is recommended.
     * @param bg the desired background <code>Color</code>
     */
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setForeground(Utils.isDark(bg) ? MaterialColor.WHITE:MaterialColor.DARK_BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        elevation.paint(g);
        g.setClip(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP,
            getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT,
            getHeight() - MaterialShadow.OFFSET_TOP - MaterialShadow.OFFSET_BOTTOM);
        super.paintComponent(g2);
        g.setClip(null);
    }
}
