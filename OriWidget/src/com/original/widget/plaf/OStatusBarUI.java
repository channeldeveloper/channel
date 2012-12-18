/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.plaf;

import com.original.widget.OImageLoad;
import com.original.widget.OStatusBar;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.StatusBarModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * 状态栏绘制UI类
 * @author Changjian Hu
 */
public class OStatusBarUI extends BasicPanelUI {
    private OStatusBar comp;
    private Rectangle2D rectTime = null;
    
    Map<Integer, Area> areas = new HashMap<Integer, Area>();
    public static ComponentUI createUI(JComponent c) {
        return new OStatusBarUI(c);
    }

    public OStatusBarUI(JComponent com) {
        comp = (OStatusBar) com;
    }

    @Override
    public void update(Graphics g, JComponent c){
        //绘制背景
        paintBackground(g);
        //绘制左边的图标
        drawLeftPart(g);
        //绘制右边提示信息
        drawRightPart(g);
    }
    //绘制右边提醒信息
    protected void drawRightPart(Graphics g){
        //draw time.
        StatusBarModel model = comp.getModel();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //draw single.
        float signal = model.getSignal();
        int num = (int)((signal*10)/2);
        int offsetx = comp.getWidth()-100;
        int offsety = (comp.getHeight()-12)/2;

        areas.put(3, new Area(new Rectangle(offsetx, offsety, 15, 12)));
        
        Area area = new Area();
        for(int i=0;i<5;i++){
            Rectangle r = new Rectangle(offsetx, offsety+9-i*2, 3, 3+i*2);
            area.add(new Area(r));
            if(i+1<=num)
                g2d.setPaint(Color.BLACK);
            else
                g2d.setPaint(new Color(166,166,166));
            g2d.fill(r);
            offsetx+=4;
        }
        OriPainter.basicDrawDropShadow(g, area, Color.BLACK, 2, 2, Math.PI*3/2, 0.4f);
        offsetx+=6;
        //draw battery.
        offsety+=6;
        GeneralPath path = new GeneralPath();
        path.moveTo(offsetx, offsety);
        path.lineTo(offsetx, offsety-2);
        path.lineTo(offsetx+3.5, offsety-2);
        path.lineTo(offsetx+3.5, offsety-5);
        path.lineTo(offsetx+18, offsety-5);
        path.lineTo(offsetx+18, offsety+5);
        path.lineTo(offsetx+3.5, offsety+5);
        path.lineTo(offsetx+3.5, offsety+2);
        path.lineTo(offsetx, offsety+2);
        path.closePath();
        area = new Area(path);

        OriPainter.basicDrawDropShadow(g, area, Color.BLACK, 2, 2, Math.PI*3/2, 0.4f);
        OriPainter.fillAreaWithSingleColor(g, area, Color.WHITE);
        OriPainter.drawAreaBorderWithSingleColor(g, area, Color.BLACK, 2.0f);
        num = (int)((model.getBattery()*10)/2);

        double width = (model.getBattery()*12);
        Rectangle2D r = new Rectangle2D.Double(offsetx+5+(12-width),
                offsety-3+0.5, width, 6.5);

        OriPainter.fillAreaWithSingleColor(g, new Area(r), Color.BLACK);

        areas.put(4, new Area(new Rectangle(offsetx, offsety-3, 20, 12)));

        offsetx+=24;
        r = new Rectangle2D.Double(offsetx, 3, comp.getWidth()-offsetx, comp.getHeight()-2);
        OriPainter.drawStringInAreaAlign(g, new Area(r), model.getTime(), new Font("Tahoma",Font.PLAIN, 12),
                Color.BLACK, JTextField.LEFT_ALIGNMENT, 2);
        areas.put(5, new Area(r));
        rectTime = r;
    }

    //绘制前面图标
    protected void drawLeftPart(Graphics g){
        BufferedImage home = (BufferedImage)OImageLoad.getImageRel("icons/home.png");
        BufferedImage timeperiod = (BufferedImage)OImageLoad.getImageRel("icons/time.png");
        if(home==null || timeperiod==null) return;

        int iconwidth = home.getWidth();
        int iconheight = home.getHeight();
        int offsetx = 18;
        int offsety = (comp.getHeight()-iconheight)/2;
        int padding = 10;

        OriPainter.drawImage(g, home, offsetx, offsety);
        areas.put(1, new Area(new Rectangle(offsetx, offsety, iconwidth, iconheight)));
        OriPainter.drawImage(g, timeperiod, offsetx+padding+iconwidth, offsety);
        areas.put(2, new Area(new Rectangle(offsetx+padding+iconwidth, offsety, iconwidth, iconheight)));
    }
    //绘制背景
    protected void paintBackground(Graphics g){
        Rectangle2D r = new Rectangle2D.Double(0,0, comp.getWidth(), comp.getHeight()-1);
        Area area = new Area(r);
        OriPainter.gradientFillArea(g, area, new Color(227,230,231), new Color(203,209,210),
                true);

        //外部描边
        OriPainter.drawAreaBorderWithSingleColor(g, area, new Color(0,0,0,26), 1.0f);
        //内部描边
        Area inner = GeomOperator.centerFixShrinkCopy(area, 1.0);
        OriPainter.drawAreaBorderWithSingleColor(g, inner, Color.WHITE, 0.5f);
    }

    public Rectangle2D getRectTime() {
        return rectTime;
    }

    public void setRectTime(Rectangle2D rectTime) {
        this.rectTime = rectTime;
    }


    //获取鼠标所在位置
    public int getMouseLocArea(Point pt){
        int ret = -1;
        Iterator<Integer> iter = areas.keySet().iterator();
        while(iter.hasNext()){
            Integer key = iter.next();
            if(areas.get(key).contains(pt)){
                ret = key;
                break;
            }
        }
        return ret;
    }    
}
