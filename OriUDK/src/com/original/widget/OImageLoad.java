/*
 *  com.original.widget.OImageLoad.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-19 10:43:41
 */
public class OImageLoad {

    public static Image getImage(String imgPath) {
        if (imgPath == null) {
            return null;
        }
        if ( imgPath.startsWith("/")) imgPath = imgPath.substring(1);
        BufferedImage img = null;
        try {
            java.net.URL imgURL = OImageLoad.class.getClassLoader().getResource(imgPath);
            img = ImageIO.read(imgURL);
        } catch (Exception e) {
            System.out.println("找不到图片： " + imgPath);
        }
        return img;
    }

    public static Image getImageRel(String imgPath) {
        if (imgPath == null) {
            return null;
        }
        BufferedImage img = null;
        try {
            java.net.URL imgURL = OImageLoad.class.getResource(imgPath);
            img = ImageIO.read(imgURL);
        } catch (Exception e) {
            System.out.println("找不到图片： " + imgPath);
        }
        return img;
    }
    /**
     *
     * @param imgPath
     * @return
     */
    public static ImageIcon getImageIcon(String imgPath) {
        if (imgPath == null) {
            return null;
        }
        ImageIcon icon = null;
        try {
            if (!imgPath.startsWith("/")) {
                imgPath = "/" + imgPath;
            }
            java.net.URL imgURL = OImageLoad.class.getResource(imgPath);
            icon = new ImageIcon(imgURL);
            return icon;
        } catch (Exception e) {
            System.out.println("找不到图片： " + imgPath);
        }
        return icon;
    }

    /**
     *
     * @param icon
     * @return
     */
    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            BufferedImage image = new BufferedImage(w, h, 2);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);//用作观察者的组件,图形上下政图标左上角的 X 坐标,图标左上角的 Y 坐标
            g.dispose();//释放此图形的上下文以及它使用的所有系统资渿
            return image;
        }
    }
}
