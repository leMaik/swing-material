package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A material panel with a z index.
 */
public class MaterialPanel extends JPanel {
    private final ElevationEffect elevation;

    public MaterialPanel() {
        elevation = ElevationEffect.applyTo(this, 1);
        setBorder(new EmptyBorder(MaterialShadow.OFFSET_TOP, MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_BOTTOM, MaterialShadow.OFFSET_RIGHT));
    }

    public int getElevation() {
        return elevation.getLevel();
    }

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
