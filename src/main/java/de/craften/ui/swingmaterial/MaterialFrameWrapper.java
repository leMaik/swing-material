package de.craften.ui.swingmaterial;

import de.craften.ui.swingmaterial.fonts.MaterialIcons;
import de.craften.ui.swingmaterial.fonts.Roboto;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A {@code MaterialFrameWrapper} is a set of components meant to provide
 * standard appearance and behavior to a {@link MaterialFrame}, like a title bar
 * with command buttons, iconifying/deiconifying and resizing.
 * 
 * @author DragShot
 */
public class MaterialFrameWrapper {
    /** The frame this object wraps around. */
    private MaterialFrame frame;
    private MaterialTitleBar titleBar;

    public MaterialFrameWrapper(MaterialFrame frame) {
        this.frame = frame;
        titleBar = new MaterialTitleBar(frame);
    }
    
    /**
     * Wraps the decorators bundled with this {@code MaterialFrameWrapper}
     * around the target frame's container panel. This is called by the frame
     * once its container panel is set and in place.
     * @param contentPane 
     */
    public void wrapAround(Container contentPane) {
        Container root = contentPane.getParent();
        root.add(titleBar);
        titleBar.setVisible(true);
    }
    
    /**
     * Sets the default color of this wrapper. This affects the decorations
     * bundled with it, like the title bar and control buttons.
     * @param color the new color of this wrapper.
     * @see MaterialFrame#setColor(java.awt.Color)
     */
    public void setColor(Color color) {
        titleBar.setBackground(color);
    }
    
    /**
     * Gets the default color of this wrapper. More precisely, the color of the
     * decorations bundled with it, like the title bar and control buttons.
     * @return the current color of this wrapper.
     * @see MaterialFrame#getColor()
     */
    public Color getColor() {
        return titleBar.getBackground();
    }
    
    /**
     * This method is called by the {@link MaterialFrame} being wrapped when the
     * title in the title bar needs to be updated.
     */
    public void updateTitle() {
        titleBar.setTitle(frame.getTitle());
    }
    
    /**
     * Causes this wrapper to lay out its components around the frame's
     * container pane. It is called from {@link MaterialFrame#doLayout()}, which
     * is triggered each time the frame is resized. You can also request the
     * layout to be done by calling {@link JFrame#validate()}.
     * @see JFrame#validate
     */
    public void doLayout() {
        titleBar.setBounds(0, 0, frame.getWindowWidth(), titleBar.getHeight());
        titleBar.doLayout();
        frame.getContentPane().setBounds(0, titleBar.getHeight(),
                frame.getWindowWidth(),
                frame.getWindowHeight() - titleBar.getHeight());
    }
    
    protected static class MaterialTitleBar extends JPanel
                implements ActionListener {
        private FrameControler control;
        private MaterialFrame frame;
        
        private MaterialButton btnClose;
        private MaterialButton btnMinimize;
        private MaterialButton btnMaximize;
        private MaterialButton btnRestore;
        
        private JLabel titleLabel;
        
        private boolean ready = false;
        
        protected MaterialTitleBar(MaterialFrame frame) {
            this.frame = frame;
            control = new FrameControler(frame);
            Font font = MaterialIcons.ICON_FONT.deriveFont(20f);
            btnClose = new MaterialButton();
            btnClose.setType(MaterialButton.Type.FLAT);
            //btnClose.setText("X");
            btnClose.setFont(font);
            btnClose.setText(String.valueOf(MaterialIcons.CLOSE));
            btnClose.setBackground(MaterialColor.RED_500);
            btnClose.setRippleColor(MaterialColor.RED__100);
            btnClose.setForeground(MaterialColor.WHITE);
            btnClose.addActionListener(this);
            btnClose.setFocusable(false);
            btnClose.setBorderRadius(1);
            add(btnClose);
            btnClose.setVisible(true);
            btnMinimize = new MaterialButton();
            btnMinimize.setType(MaterialButton.Type.FLAT);
            //btnMinimize.setText("_");
            btnMinimize.setFont(font);
            btnMinimize.setText(String.valueOf(MaterialIcons.EXPAND_MORE));
            btnMinimize.addActionListener(this);
            btnMinimize.setFocusable(false);
            btnMinimize.setBorderRadius(1);
            add(btnMinimize);
            btnMinimize.setVisible(true);
            btnMaximize = new MaterialButton();
            btnMaximize.setType(MaterialButton.Type.FLAT);
            //btnMaximize.setText("M");
            btnMaximize.setFont(font);
            btnMaximize.setText(String.valueOf(MaterialIcons.FULLSCREEN));
            btnMaximize.addActionListener(this);
            btnMaximize.setFocusable(false);
            btnMaximize.setBorderRadius(1);
            add(btnMaximize);
            btnMaximize.setVisible(true);
            btnRestore = new MaterialButton();
            btnRestore.setType(MaterialButton.Type.FLAT);
            //btnRestore.setText("R");
            btnRestore.setFont(font);
            btnRestore.setText(String.valueOf(MaterialIcons.FULLSCREEN_EXIT));
            btnRestore.addActionListener(this);
            btnRestore.setFocusable(false);
            btnRestore.setBorderRadius(1);
            add(btnRestore);
            btnRestore.setVisible(false);
            titleLabel = new JLabel();
            titleLabel.setText("");
            titleLabel.setFont(Roboto.MEDIUM.deriveFont(24f));
            add(titleLabel);
            titleLabel.setVisible(true);
            setMinimumSize(new java.awt.Dimension(48, 48));
            setSize(48, 48);
            MouseAdapter mouseon = getControlListener();
            addMouseListener(mouseon);
            addMouseMotionListener(mouseon);
            ready = true;
            setBackground(MaterialColor.INDIGO_500);
        }
        
        /**
         * Sets the title of this {@link MaterialTitleBar}.
         * @param title the title to display
         */
        public void setTitle(String title) {
            titleLabel.setText(title);
        }
        
        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if (ready) {
                Color btn;
                if (Utils.isDark(bg)) {
                    btn = Utils.brighten(bg);
                    titleLabel.setForeground(MaterialColor.WHITE);
                } else {
                    btn = Utils.darken(bg);
                    titleLabel.setForeground(MaterialColor.BLACK);
                }
                btnMinimize.setBackground(btn);
                btnMinimize.setRippleColor(bg);
                btnMaximize.setBackground(btn);
                btnMaximize.setRippleColor(bg);
                btnRestore.setBackground(btn);
                btnRestore.setRippleColor(bg);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == btnClose) {
                control.close();
            } else if (evt.getSource() == btnMinimize) {
                control.iconify();
            } else if (evt.getSource() == btnMaximize) {
                control.maximize();
            } else if (evt.getSource() == btnRestore) {
                control.restore();
            }
        }
        
        @Override
        public void doLayout() {
            boolean max = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
            int hpad = MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT;
            int vpad = MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM;
            btnClose.setBounds(this.getWidth() - 48 - MaterialShadow.OFFSET_LEFT - 10,
                0 - MaterialShadow.OFFSET_TOP, 48 + hpad, 24 + vpad);
            if (frame.isResizable()) {
                if (max) {
                    btnRestore.setBounds(this.getWidth() - 78 - MaterialShadow.OFFSET_LEFT - 10,
                        0 - MaterialShadow.OFFSET_TOP, 30 + hpad, 24 + vpad);
                    btnMaximize.setBounds(0,0,0,0);
                    btnMaximize.setVisible(false);
                    btnRestore.setVisible(true);
                } else {
                    btnMaximize.setBounds(this.getWidth() - 78 - MaterialShadow.OFFSET_LEFT - 10,
                        0 - MaterialShadow.OFFSET_TOP, 30 + hpad, 24 + vpad);
                    btnRestore.setBounds(0,0,0,0);
                    btnMaximize.setVisible(true);
                    btnRestore.setVisible(false);
                }
                btnMinimize.setBounds(this.getWidth() - 108 - MaterialShadow.OFFSET_LEFT - 10,
                    0 - MaterialShadow.OFFSET_TOP, 30 + hpad, 24 + vpad);
            } else {
                btnMinimize.setBounds(this.getWidth() - 78 - MaterialShadow.OFFSET_LEFT - 10,
                    0 - MaterialShadow.OFFSET_TOP, 30 + hpad, 24 + vpad);
                btnMaximize.setBounds(0,0,0,0);
                btnMaximize.setVisible(false);
                btnRestore.setBounds(0,0,0,0);
                btnRestore.setVisible(false);
            }
            titleLabel.setBounds(MaterialShadow.OFFSET_LEFT, 0,
                this.getWidth() - MaterialShadow.OFFSET_TOP*2
                - (btnClose.getWidth()-MaterialShadow.OFFSET_LEFT)
                - Math.max(btnRestore.getWidth()-hpad,0)
                - Math.max(btnMaximize.getWidth()-hpad,0)
                - (btnMinimize.getWidth()-hpad), getMinimumSize().height);
        }
        
        private MouseAdapter getControlListener() {
            return new MouseAdapter() {
                boolean moving=false;
                int[][] movs={{0,0},{0,0}};

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                        if (frame.isResizable()) {
                            if ((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                                control.restore();
                            } else {
                                control.maximize();
                            }
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1
                        && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH) {
                        moving = true;
                        movs[1][0] = e.getXOnScreen();
                        movs[1][1] = e.getYOnScreen();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1
                        && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH) {
                        moving = false;
                    }
                }

                @Override
                @SuppressWarnings("deprecated")
                public void mouseDragged(MouseEvent e){
                    if (moving) {
                        System.arraycopy(movs[1], 0, movs[0], 0, 2);
                        movs[1][0] = e.getXOnScreen();
                        movs[1][1] = e.getYOnScreen();
                        int mx = movs[1][0] - movs[0][0];
                        int my = movs[1][1] - movs[0][1];
                        if (mx != 0 || my != 0) {
                            frame.setLocation(frame.getX() + mx, frame.getY() + my);
                        }
                    }
                }
            };
        }
    }
    
    /**
     * A {@code FrameControler} provides an easy way to perform operations that
     * change the window state and size as minimizing, maximizing and restoring.
     */
    protected static class FrameControler {
        /** The {@link MaterialFrame} being controled. */
        MaterialFrame frame;

        protected FrameControler(MaterialFrame frame) {
            this.frame = frame;
        }
        
        /**
         * This method iconifies (minimizes) a frame into the task bar. The
         * maximized bits are not affected.
         */
        public void iconify() {
            int state = frame.getExtendedState();
            // Set the iconified bit
            state |= Frame.ICONIFIED;
            // Iconify the frame
            frame.setExtendedState(state);
        }

        /**
         * This method deiconifies a frame from the task bar. The maximized bits
         * are not affected.
         */
        public void deiconify() {
            int state = frame.getExtendedState();
            // Clear the iconified bit
            state &= ~Frame.ICONIFIED;
            // Deiconify the frame
            frame.setExtendedState(state);
        }

        /**
         * This method restores a frame from a maximized state. The iconified
         * bit is not affected.
         */
        public void restore() {
            int state = frame.getExtendedState();
            // Clear the maximized bits
            state &= ~Frame.MAXIMIZED_BOTH;
            // Minimize the frame
            frame.setExtendedState(state);
        }

        /**
         * This method maximizes a frame. The iconified bit is not affected
         */
        @SuppressWarnings("unchecked")
        public void maximize() {
            //java.awt.Rectangle usableBounds = Utils.getScreenSize();
            //frame.setMaximizedBounds(new java.awt.Rectangle(0, 0, usableBounds.width, usableBounds.height));
            frame.setMaximizedBounds(Utils.getScreenSize());
//            frame.setExtendedState((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
            int state = frame.getExtendedState();
            // Set the maximized bits
            state |= Frame.MAXIMIZED_BOTH;
            // Maximize the frame
            frame.setExtendedState(state);
        }
        
        /**
         * This method closes a frame.
         */
        public void close(){
            frame.processWindowEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}