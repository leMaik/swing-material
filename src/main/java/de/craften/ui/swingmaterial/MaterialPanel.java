package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A material panel with elevation.
 */
public class MaterialPanel extends JPanel {
    private final ElevationEffect elevation;

    /**
     * Creates a new panel.
     */
    public MaterialPanel() {
        elevation = ElevationEffect.applyTo(this, 1);
        setBorder(new EmptyBorder(MaterialShadow.OFFSET_TOP, MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_BOTTOM, MaterialShadow.OFFSET_RIGHT));
    }

    /**
     * Gets the elevation level.
     *
     * @return elevation level (0..5)
     */
    public int getElevation() {
        return elevation.getLevel();
    }

    /**
     * Sets the elevation level.
     *
     * @param elevation elevation level (1..5)
     */
    public void setElevation(int elevation) {
        this.elevation.setLevel(elevation);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        elevation.paint(g);
        super.paintComponent(g2);
    }
}
