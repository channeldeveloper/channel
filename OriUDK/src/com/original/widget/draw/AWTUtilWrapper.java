/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget.draw;

import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.Window;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JWindow;

/**
 *
 * @author Anthony Petrov
 */
public class AWTUtilWrapper {

    private static Class<?> awtUtilitiesClass;
    private static Class<?> translucencyClass;
    private static Method mIsTranslucencySupported,  mIsTranslucencyCapable,  mSetWindowShape,  mSetWindowOpacity,  mSetWindowOpaque;
    public static Object PERPIXEL_TRANSPARENT,  TRANSLUCENT,  PERPIXEL_TRANSLUCENT;

    static void init() {
        try {
            awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
            translucencyClass = Class.forName("com.sun.awt.AWTUtilities$Translucency");
            if (translucencyClass.isEnum()) {
                Object[] kinds = translucencyClass.getEnumConstants();
                if (kinds != null) {
                    PERPIXEL_TRANSPARENT = kinds[0];
                    TRANSLUCENT = kinds[1];
                    PERPIXEL_TRANSLUCENT = kinds[2];
                }
            }
            mIsTranslucencySupported = awtUtilitiesClass.getMethod("isTranslucencySupported", translucencyClass);
            mIsTranslucencyCapable = awtUtilitiesClass.getMethod("isTranslucencyCapable", GraphicsConfiguration.class);
            mSetWindowShape = awtUtilitiesClass.getMethod("setWindowShape", Window.class, Shape.class);
            mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
            mSetWindowOpaque = awtUtilitiesClass.getMethod("setWindowOpaque", Window.class, boolean.class);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static {
        init();
    }
    
    private static boolean isSupported(Method method, Object kind) {
        if (awtUtilitiesClass == null ||
                method == null)
        {
            return false;
        }
        try {
            Object ret = method.invoke(null, kind);
            if (ret instanceof Boolean) {
                return ((Boolean)ret).booleanValue();
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean isTranslucencySupported(Object kind) {
        if (translucencyClass == null) {
            return false;
        }
        return isSupported(mIsTranslucencySupported, kind);
    }
    
    public static boolean isTranslucencyCapable(GraphicsConfiguration gc) {
        return isSupported(mIsTranslucencyCapable, gc);
    }
    
    private static void set(Method method, Window window, Object value) {
        if (awtUtilitiesClass == null ||
                method == null)
        {
            return;
        }
        try {
            method.invoke(null, window, value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(AWTUtilWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setWindowShape(Window window, Shape shape) {
        set(mSetWindowShape, window, shape);
    }

    public static void setWindowOpacity(Window window, float opacity) {
        set(mSetWindowOpacity, window, Float.valueOf(opacity));
    }
    
    public static void setWindowOpaque(Window window, boolean opaque) {
        set(mSetWindowOpaque, window, Boolean.valueOf(opaque));
    }

    public static void applySpecialEffect(JWindow win, int cornersize, int opacity){
        setWindowOpacity(win, ((float) opacity) / 100.0f);
        setWindowOpaque(win, false);
        Shape shape = null;
        shape = new RoundRectangle2D.Float(0, 0, win.getWidth(),
                win.getHeight(), cornersize, cornersize);
        setWindowShape(win, shape);
    }

    public static void applySpecialEffect(Window win, int cornersize, int opacity){
        setWindowOpacity(win, ((float) opacity) / 100.0f);
        setWindowOpaque(win, false);
        Shape shape = null;
        shape = new RoundRectangle2D.Float(0, 0, win.getWidth(),
                win.getHeight(), cornersize, cornersize);
        setWindowShape(win, shape);
    }    
}
