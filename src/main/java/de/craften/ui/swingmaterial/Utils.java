package de.craften.ui.swingmaterial;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Put miscellaneous utilitary static methods here.
 * 
 * @author DragShot
 */
public class Utils {
    /**
     * An integer flag for <code>getScreenSize()</code>.<br/><br/>
     * Values:<br/>
     *  0: Check if sun.java2d.SunGraphicsEnvironment.getUsableBounds() is
     *     available.<br/>
     *  1: Class/method is available.<br/>
     * -1: Class/method is not available.
     */
    private static int useSun2D = 0;
    private static Method getUsableBounds = null;
    
    /**
     * Checks the area available in the desktop, excluding the taskbar.
     * In order to do this, an attempt to call
     * <code>sun.java2d.SunGraphicsEnvironment.getUsableBounds()</code> is
     * performed. If this can't be done, the method falls back to the default
     * <code>Toolkit.getDefaultToolkit().getScreenSize()</code>, although such
     * method doesn't exclude the taskbar area.
     * @return A Rectangle with the usable area for maximized windows.
     * @author DragShot
     */
    public static Rectangle getScreenSize(){
        Rectangle screen = null;
        if (useSun2D == 0) {
            //Check if sun.java2d.SunGraphicsEnvironment.getUsableBounds()
            //is available.
            try {
                Class sunGE = Class.forName("sun.java2d.SunGraphicsEnvironment");
                Method[] meths = sunGE.getDeclaredMethods();
                useSun2D = -1;
                for (Method meth:meths) {
                    if (meth.getName().equals("getUsableBounds")
                        && Arrays.equals(meth.getParameterTypes(),
                                new Class[]{java.awt.GraphicsDevice.class})
                        && meth.getExceptionTypes().length == 0
                        && meth.getReturnType()
                                .equals(java.awt.Rectangle.class)) {
                        //We found it!
                        getUsableBounds = meth;
                        useSun2D = 1;
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                useSun2D = -1;
            }
        } if (useSun2D == 1) { //Use sun.java2d.SunGraphicsEnvironment.getUsableBounds()
            try {
                Frame frame = new Frame();
                GraphicsConfiguration config = frame.getGraphicsConfiguration();
                screen = (Rectangle)getUsableBounds.invoke(null, config.getDevice());
                frame.dispose();
            } catch (Exception ex) {
                useSun2D = -1;
            }
        } if (useSun2D != 1) { //Do it the traditional way
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            screen = new Rectangle(0, 0, size.width, size.height);
        }
        return screen;
    }
    
    /**
     * Checks if the translucency effect is supported. Java 6 does not support
     * this. Only Java 7 and higher VMs might do, depending of the Graphics
     * Environment and OS.
     * @return <code>true</code> if translucency is supported,
     *         <code>false</code> otherwise.
     * @author DragShot
     */
    public static boolean isTranslucencySupported(){
        boolean nativeTrans;
        if (System.getProperty("java.version").contains("1.6")) {
            System.err.println("Per-pixel translucency is currently not "
                    + "supported.\nPlease upgrade your JRE to at least Java 7 "
                    + "to support this feature.");
            nativeTrans = false;
        } else {
            nativeTrans = GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .isWindowTranslucencySupported(GraphicsDevice
                    .WindowTranslucency.PERPIXEL_TRANSLUCENT);
        }
        return nativeTrans;
    }
    
    /**
     * Determines if a given {@link Color} is dark enough for white text to be
     * seen more easily than black text. This tries to stick to the Material
     * Color Guide as much as possible, and although two or three of the color
     * pairs doesn't match, the results are still good enough.
     * 
     * @param color a {@link Color} to evaluate
     * @return {@code true} if the provided color is dark, {@code false}
     *         otherwise.
     * @author DragShot
     */
    public static boolean isDark (Color color) {
        //return (color.getRed()*0.299 + color.getGreen()*0.587 + color.getBlue()*0.114) < (0.6*255);
        //return (color.getRed() + color.getGreen() + color.getBlue())/3 < (0.63*255);
        return (color.getRed()*0.2125 + color.getGreen()*0.7154 + color.getBlue()*0.0721) < (0.535*255);
        //return (color.getRed()*0.21 + color.getGreen()*0.72 + color.getBlue()*0.07) < (0.54*255);
    }
    
    //Uncomment this block in order to test #isDark() against all the color constants in Material Color
    /*public static void main(String[] args) {
        Field[] fields = MaterialColor.class.getDeclaredFields();
        for (Field field:fields) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    Color.class.isAssignableFrom(field.getType())) {
                try {
                    System.out.println(field.getType().getName()+" "+field.getName()+(isDark((Color)field.get(null)) ? " is dark":" is light"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }*/
}