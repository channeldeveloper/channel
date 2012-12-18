/*
 *  com.original.widget.comp.view.OTextFieldUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.OTableHeaderFilter;
import com.original.widget.model.TextBlockModel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * (Class Annotation.)
 * 表格header过滤框组建
 * @author   刘萌萌
 * @encoding UTF-8
 * @version  1.0
 * @create   May 28, 2012 2:33:56 PM
 */
public class OTableHeaderFilterUI extends BasicTextFieldUI {
    // Shared UI object

    private static OTableHeaderFilterUI cui;
    private OTableHeaderFilter comp;
    private TextBlockModel model;
    //清空按钮区域
    private Rectangle emptyRect = null;

    public static ComponentUI createUI(JComponent c) {
        if (cui == null) {
            cui = new OTableHeaderFilterUI(c);
        }
        return cui;
    }

    public OTableHeaderFilterUI(JComponent com) {
        comp = (OTableHeaderFilter) com;
        model = comp.getModel();
    }

    public void redraw() {
        if (model == null) {
            model = comp.getModel();
        }
        model.setSize(comp.getHeight(), comp.getWidth());
    }

    @Override
    public void update(Graphics g, JComponent c) {
        if (model == null) {
            model = comp.getModel();
        }
        int width = model.getWidth();
        int height = model.getHeight();

        //圆角长方形
        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                0,
                TextBlockModel.CORNERRADIUS / 2,
                width - 2,
                height - TextBlockModel.CORNERRADIUS,
                TextBlockModel.CORNERRADIUS,
                TextBlockModel.CORNERRADIUS);
        //Draw the background frame.
        if(comp.isOpaque())
        OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                model.getBackgroundcolor());
        OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
                TextBlockModel.BORDERCOLOR, 1);

        //draw the inner Shadow.
        //calculate the shadow area.
        Area areaOne = new Area(r2d);
        Area areaTwo = GeomOperator.offsetCopy(areaOne, 0, 2.4);
        areaOne.subtract(areaTwo); //areaOne will be the shadow area.
        //generate the shadow image.
        BufferedImage shadow = OriPainter.drawGradientInnerShadow(g,
                areaOne, TextBlockModel.SHADOWCOLOR, model.getBackgroundcolor(),
                40);

        //paint the shadow.
        OriPainter.drawImage(g, shadow, 0, 0);

        //画清除区域
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(comp.getClass().getResource("images/del.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        OriPainter.drawImage(g, bi, width - bi.getWidth() - 6, 10);
        emptyRect = new Rectangle(width - bi.getWidth() - 6, 10, bi.getWidth(), bi.getHeight());

        if (comp._isClicked) {
            //OriPainter.drawStringInAreaCenter(g, areaTwo, comp.getTitle(), new Font("微软雅黑", Font.PLAIN,  16), TextBlockModel.BORDERCOLOR);
            g.drawString(comp.getTitle(), TextBlockModel.CORNERRADIUS / 2, 26);
        }
        //System.out.println("3---"+width);

        super.update(g, c);
    }

    public Rectangle getEmptyRect() {
        return emptyRect;
    }

    public void setEmptyRect(Rectangle emptyRect) {
        this.emptyRect = emptyRect;
    }
}
