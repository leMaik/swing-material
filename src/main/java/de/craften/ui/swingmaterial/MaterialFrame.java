package de.craften.ui.swingmaterial;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;

/**
 * An extension of {@link MaterialWindow} with a classic title bar, control
 * buttons and the ability to drag, minimize and maximize the window as the user
 * wishes. Specially recommended when you want your application to behave like
 * common desktop apps. If you are looking for a behavior more like mobile apps,
 * try {@code MaterialAppFrame}.
 * @author DragShot
 */
public class MaterialFrame extends MaterialWindow {
    /** The wrapper providing Frame appearance and behavior. */
    MaterialFrameWrapper wrapper;
    /** The panel containing the components placed inside this frame. */
    private Container contentPane;
    
    public MaterialFrame() {
        super();
        contentPane = new JPanel();
        contentPane.setLayout(null);
        super.getContentPane().add(contentPane);
        wrapper = new MaterialFrameWrapper(this);
        wrapper.wrapAround(contentPane);
        contentPane.setVisible(true);
        light();
    }

    @Override
    public Container getContentPane() {
        return contentPane == null ? super.getContentPane():contentPane;
    }

    @Override
    public void setContentPane(Container contentPane) {
        this.contentPane = contentPane;
    }
    
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        wrapper.updateTitle();
    }
    
    public void setColor(Color color) {
        wrapper.setColor(color);
    }
    
    public Color getColor() {
        return wrapper.getColor();
    }
    
    @Override
    public void doLayout() {
        super.doLayout();
        wrapper.doLayout();
        getContentPane().doLayout();
    }
    
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
    }
}
