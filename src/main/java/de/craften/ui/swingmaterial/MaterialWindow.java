package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A window with Material shadow.
 */
public class MaterialWindow extends JFrame {
    public MaterialWindow() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (ge.getDefaultScreenDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
            setUndecorated(true);
            setBackground(new Color(255, 255, 255, 0));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setContentPane(new ShadowPane());
            getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
            getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent componentEvent) {
                    setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 3, 3));
                }
            });
        }
    }

    private class ShadowPane extends JPanel {
        ShadowPane() {
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(MaterialShadow.OFFSET_TOP, MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_BOTTOM, MaterialShadow.OFFSET_RIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g.clearRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.drawImage(MaterialShadow.renderShadow(getWidth(), getHeight(), 2), 0, 0, null);

            g.setClip(new RoundRectangle2D.Float(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP,
                    getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT,
                    getHeight() - MaterialShadow.OFFSET_TOP - MaterialShadow.OFFSET_BOTTOM, 3, 3));
            super.paintComponent(g);
        }
    }
}