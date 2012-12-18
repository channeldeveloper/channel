/*
 *  com.original.widget.draw.OriImgProcer.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.draw;

import com.jhlabs.image.ShadowFilter;
import com.thebuzzmedia.imgscalr.Scalr;
import com.thebuzzmedia.imgscalr.Scalr.Method;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 8, 2012 9:06:55 PM
 */
public class OriImgProcer {

    /**
     * 给定一个图像，将该图像缩放到给定大小（长宽），然后进行切割圆角
     * @param image
     * @param width
     * @param height 
     * @param cornerRadius
     * @return
     */
    public static BufferedImage makeScaledRoundedCorner(BufferedImage image, int width,
            int height, int cornerRadius) {
        BufferedImage scaledImg = Scalr.resize(image, Method.QUALITY,
                width, height, Scalr.OP_ANTIALIAS);
        int w = scaledImg.getWidth();
        int h = scaledImg.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(scaledImg, 0, 0, null);

        g2.dispose();

        return output;
    }

    /**
     * 给定内容，获得其阴影
     * @param image
     * @param radius
     * @param distance
     * @param angle
     * @param opacity
     * @return
     */
    public static BufferedImage blurImage(BufferedImage image, float radius,
            float distance, float angle, float opacity) {
        ShadowFilter filter = new ShadowFilter();
        filter.setAngle(angle);
        filter.setDistance(distance);
        filter.setRadius(radius);
        filter.setOpacity(opacity);

        return filter.filter(image, null);
    }

    /**
     * 绘制向下的双箭头
     * @param arrowclr
     * @return
     */
    public static BufferedImage createDownArrow(Color arrowclr) {
        BufferedImage bi = new BufferedImage(40, 40, 2);
        Graphics2D g2d = bi.createGraphics();
        GeneralPath path = new GeneralPath();
        int x = 10;
        int y = 20;
        int width1 = 12;
        path.moveTo(x + width1 / 2, y - width1 / 3);
        path.lineTo(x, y);
        path.moveTo(x + width1 / 2, y - width1 / 3);
        path.lineTo(x + width1, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.setPaint(arrowclr);
        g2d.draw(path);

        y += 4;
        path = new GeneralPath();
        path.moveTo(x + width1 / 2, y - width1 / 3);
        path.lineTo(x, y);
        path.moveTo(x + width1 / 2, y - width1 / 3);
        path.lineTo(x + width1, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.setPaint(arrowclr);
        g2d.draw(path);

        g2d.dispose();
        return bi;
    }

    /**
     * 绘制向上的双箭头
     * @param arrowclr
     * @return
     */
    public static BufferedImage createUpArrow(Color arrowclr) {
        BufferedImage bi = new BufferedImage(40, 40, 2);
        Graphics2D g2d = bi.createGraphics();

        GeneralPath path = new GeneralPath();
        int x = 10;
        int y = 15;
        int width1 = 12;
        path.moveTo(x + width1 / 2, y + width1 / 3);
        path.lineTo(x, y);
        path.moveTo(x + width1 / 2, y + width1 / 3);
        path.lineTo(x + width1, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.setPaint(arrowclr);
        g2d.draw(path);

        y += 4;
        path = new GeneralPath();
        path.moveTo(x + width1 / 2, y + width1 / 3);
        path.lineTo(x, y);
        path.moveTo(x + width1 / 2, y + width1 / 3);
        path.lineTo(x + width1, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.setPaint(arrowclr);
        g2d.draw(path);

        g2d.dispose();
        return bi;
    }
}
