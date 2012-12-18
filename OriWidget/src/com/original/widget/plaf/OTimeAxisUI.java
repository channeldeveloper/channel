/**
 *  com.original.widget.comp.arrowedpanel.view;
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import com.original.widget.OTimeAxis;
import com.original.widget.OWinDialog;
import com.original.widget.date.OriDate;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.event.BlockSelectionEvent;
import com.original.widget.event.BlockSelectionListener;
import com.original.widget.model.TimeAxisModel;
import com.original.widget.model.TimeAxisModel.RulerMeterDef;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * (Class Annotation.)
 * 时间轴UI
 * @updator Changjian Hu
 * @author   Min NI, Xueyong SONG
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-2 23:50:04
 */
public class OTimeAxisUI extends BasicPanelUI {
    private OTimeAxis comp;

    //最上面预留的地方绘制刻度
    private final static int UPLEFTHEIGHT = 10;
    private RoundRectangle2D rectBrick = null;
    private Rectangle2D rectSlideArea = null;
    private Area areaTriangle = null;

    //几个变量用于控制鼠标的拖动状态
    private int dragmode = -1;
    private Point dragStartPoint = null;

    //变量用户控制是否在于输入状态
    private boolean blockInputMode = false;
    
    //这两个变量用于控制移动滑块的状况
    private double l_move_left = 0.0f;
    private double r_move_left = 0.0f;
    //该变量用于计算用户拖拽来改变缩放比例
    private double scaleDragQuota =0.0;

    OWinDialog innerWin = null;

    public static ComponentUI createUI(JComponent c) {
        return new OTimeAxisUI(c);
    }

    public OTimeAxisUI(JComponent com) {
        comp = (OTimeAxis) com;
    }

    @Override
    public void update(Graphics g, JComponent c) {
        //draw the background
        drawBackground(g);
        //绘制两边的字符（时间阶段）
        drawTimePeriodInfo(g);
        //绘制滑动区域
        drawSlideArea(g);
        //绘制上部区域
        drawUperRuler(g);
        //绘制当前时间控制块
        drawResetTriangle(g);

    }
    //绘制重置按钮
    protected void drawResetTriangle(Graphics g){
        double height = UPLEFTHEIGHT*2+2;
        int pos = comp.getWidth()/4;
        GeneralPath path = new GeneralPath();
        path.moveTo(pos-UPLEFTHEIGHT/2, height);
        path.lineTo(pos+UPLEFTHEIGHT/2, height);
        path.lineTo(pos, height-UPLEFTHEIGHT*2/3);
        path.closePath();
        areaTriangle = new Area(path);
        OriPainter.fillAreaWithSingleColor(g, areaTriangle, new Color(110,112,109));
    }
    //绘制上部Ruler
    protected void drawUperRuler(Graphics g){
        double height = UPLEFTHEIGHT*2+2;
        Point2D pt1 = new Point2D.Double(0, height);
        Point2D pt2 = new Point2D.Double(comp.getWidth(), height);
        OriPainter.drawLine(g, pt1, pt2, Color.WHITE, 1.0f);
        //draw the meters of the ruler.
        Rectangle2D r = new Rectangle2D.Double(pt1.getX(), pt1.getY()-UPLEFTHEIGHT*2-2,
                comp.getWidth(), UPLEFTHEIGHT*2+2);

        drawRulerMeter(g, r, 1);
    }
    
    //绘制滑动区域轴
    protected void drawSlideArea(Graphics g){
        TimeAxisModel model = comp.getModel();
        float slideareapercent = model.getSlideAreaPercent();
        double size = comp.getWidth()* (1-slideareapercent)/2;

        //let draw the area. will be removed soon (with real color.
        double height = (comp.getHeight() - UPLEFTHEIGHT)/2+UPLEFTHEIGHT+4;
        Point2D pt1 = new Point2D.Double(size, height);
        Point2D pt2 = new Point2D.Double(comp.getWidth()-size, height);
        //here, let draw the background information.
        //OriPainter.drawLine(g, pt1, pt2, Color.BLACK, 1.0f);
        drawSandglass(g, size, height);
        Rectangle2D r = new Rectangle2D.Double(pt1.getX(), pt1.getY()-UPLEFTHEIGHT*2-2,
                comp.getWidth()-size*2, UPLEFTHEIGHT*2+2);
        //draw the meters of the ruler.
        rectSlideArea = r;
        drawRulerMeter(g, r, 0);
        //draw the slide block
        
        
        if(this.blockInputMode){
            drawSlideInputEditor(g, r, model);
        }
        else
            drawSlideBlock(g, r, model);
    }
    //绘制漏斗
    private void drawSandglass(Graphics g, double size, double height){
        int barsize = 6;
        int sandglassize = barsize*4;
        Area area = new Area();
        Area leftsandglass = null;
        Area rightsandglass = null;
        //Draw the middle.
        Rectangle2D r = new Rectangle2D.Double(0, height-barsize/2, comp.getWidth(), barsize/2);
        area.add(new Area(r));
        r = new Rectangle2D.Double(0, height, comp.getWidth(), barsize/2);
        area.add(new Area(r));
        //left part.
        GeneralPath path = new GeneralPath();
        path.moveTo(0, height-sandglassize/2);
        path.lineTo(size-sandglassize-4, height-sandglassize/2);
        path.curveTo(size-sandglassize/2-4, height-sandglassize/2,
                size-sandglassize/2, height-sandglassize/2, size, height-barsize/2);
        path.lineTo(size, height+barsize/2);

        path.curveTo(size, height+barsize/2,
                size-sandglassize/2,
                height+sandglassize/2,size-sandglassize/2-4, height+sandglassize/2 );

        path.lineTo(0, height+sandglassize/2);
        path.closePath();
        area.add((leftsandglass=new Area(path)));

        //right part.
        path.reset();
        path.moveTo(comp.getWidth()-size, height-barsize/2);
        path.curveTo(comp.getWidth()-size, height-barsize/2,
                comp.getWidth()-size+sandglassize/2, height-sandglassize/2,
                comp.getWidth()-size+sandglassize/2+4, height-sandglassize/2);
        path.lineTo(comp.getWidth(), height-sandglassize/2);
        path.lineTo(comp.getWidth(), height+sandglassize/2);
        path.lineTo(comp.getWidth()-size+sandglassize/2+4, height+sandglassize/2);
        path.curveTo(comp.getWidth()-size+sandglassize/2+4, height+sandglassize/2,
                comp.getWidth()-size+sandglassize/2, height+sandglassize/2,
                comp.getWidth()-size, height+barsize/2);
        path.closePath();
        area.add((rightsandglass=new Area(path)));
        
        OriPainter.basicDrawDropShadow(g, area, Color.BLACK, 4, 8, Math.PI*3/2, 0.4f);

        OriPainter.fillAreaWithSingleColor(g, area, new Color(76, 191, 212));
        
        
        Area top = (Area)area.clone();
        top.subtract(new Area(new Rectangle2D.Double(0, height+2, comp.getWidth(), sandglassize/2)));
        OriPainter.fillAreaWithSingleColor(g, top, new Color(204,232,236));
        OriPainter.drawAreaBorderWithSingleColor(g, top, new Color(175,225,236), 1.0f);
        Area bottom = (Area)area.clone();
        bottom.subtract(new Area(new Rectangle2D.Double(0, height-sandglassize/2, comp.getWidth(), sandglassize/2)));
        OriPainter.fillAreaWithSingleColor(g, bottom, new Color(150,208,220));
        
        drawContainer(g, leftsandglass, 0.35f, false);
        drawContainer(g, rightsandglass, 0.35f, true);
    }
    //绘制一个沙漏容器，以及调整对应的内容
    //Updated：修复了一个绘制上的Bug，包括曲线和移动位置方面
    private void drawContainer(Graphics g, Area area, float divid, boolean isRight){
        Area shrink = GeomOperator.centerFixShrinkCopy(area, 2);
        //保证区域靠近边界
        if(isRight){
            double offsetx = area.getBounds2D().getMaxX() - shrink.getBounds2D().getMaxX();
            GeomOperator.offset(shrink, offsetx,0);
        }
        else{
            GeomOperator.offset(shrink, -shrink.getBounds2D().getX(), 0);
        }
        Rectangle2D r = shrink.getBounds2D();
        Area top = (Area)shrink.clone();

        if(isRight){
            OriPainter.fillAreaWithSingleColor(g, shrink, new Color(218,218,218,100));
            
            OriPainter.basicDrawDropShadow(g, shrink,
                    new Color(150,208,220), 1, 0, Math.PI*3/2, 0.3f);
            
            OriPainter.gradientPartlyFillArea(g, shrink, new Color(16,112,188,0), new Color(16,112,188,100),
                    new Color(16,112,188),
                false,comp.getModel().getRpercent(), true);

            OriPainter.drawAreaBorderWithSingleColor(g, shrink,
                    new Color(150,208,220,40), 1.0f);
            top.subtract(new Area(new Rectangle2D.Double(r.getX(), r.getY()+r.getHeight()*divid,
                    r.getWidth(), r.getHeight())));

            OriPainter.blkGradientMiddleFillArea(g, top,
                     new Color(226, 239,248,50),new Color(226, 239,248,150), 0.24f, true);
        }
        else{
            OriPainter.fillAreaWithSingleColor(g, shrink, new Color(218,218,218,100));
            
            OriPainter.basicDrawDropShadow(g, shrink,
                    new Color(150,208,220), 1, 0, Math.PI*3/2, 0.3f);
            OriPainter.gradientPartlyFillArea(g, shrink, new Color(16,112,188,100), new Color(16,112,188,0),
                    new Color(16,112,188),
                false,comp.getModel().getLpercent(), false);

            OriPainter.drawAreaBorderWithSingleColor(g, shrink,
                    new Color(150,208,220,40), 1.0f);
            top.subtract(new Area(new Rectangle2D.Double(r.getX(), r.getY()+r.getHeight()*divid,
                    r.getWidth(), r.getHeight())));

            OriPainter.blkGradientMiddleFillArea(g, top,
                     new Color(226, 239,248,50),new Color(226, 239,248,150), 0.24f, true);
            //OriPainter.blkGradientMiddleFillArea(g, top,
            //         new Color(226, 239,248,50),new Color(226, 239,248,150), 0.24f, true);
        }
    }

    //绘制时间滑动块
    private void drawSlideBlock(Graphics g, Rectangle2D r, TimeAxisModel model){
        long diff_all = OriDate.dateDiff(model.getSlideAreaStartDate(), model.getSlideAreaAEndDate(),
                Calendar.MINUTE);
        long diff = OriDate.dateDiff(model.getSlideAreaStartDate(), model.getTimeviewStartDate(),
                Calendar.MINUTE);
        long diff_view = OriDate.dateDiff(model.getTimeviewStartDate(), model.getTimeviewEndDate(),
                Calendar.MINUTE);
        double offsetx = r.getWidth()*diff/diff_all;
        double width = r.getWidth()*diff_view/diff_all;
        double offsety = r.getY()+r.getHeight()/2-6;
        if(dragmode==2)
            width+= this.scaleDragQuota;
        else if(dragmode==3){
            offsetx += this.scaleDragQuota;
            width-= this.scaleDragQuota;
        }
        RoundRectangle2D rect = new RoundRectangle2D.Double(r.getX()+offsetx, offsety, width, 25.0, 4, 4);
        Area brick = new Area(rect);
        Area inner = null;
        brick.subtract((inner=GeomOperator.centerFixShrinkCopy(brick, 1)));
        rectBrick = rect;

        
        //OriPainter.fillAreaWithSingleColor(g, brick, new Color(233,233,233,200));
        OriPainter.drawAreaBorderWithSingleColor(g, brick, Color.white, 1f);
        OriPainter.basicDrawDropShadowEx(g, brick, Color.BLACK, 3, 6,
                Math.PI*3/2, 0.4f, 3);
        OriPainter.fillAreaWithSingleColor(g, inner, new Color(233,233,233,140));
    }
    //绘制日期输入状态
    private void drawSlideInputEditor(Graphics g, Rectangle2D r, TimeAxisModel model){
      
         long diff_all = OriDate.dateDiff(model.getSlideAreaStartDate(), model.getSlideAreaAEndDate(),
                Calendar.MINUTE);
        long diff = OriDate.dateDiff(model.getSlideAreaStartDate(), model.getTimeviewStartDate(),
                Calendar.MINUTE);
        long diff_view = OriDate.dateDiff(model.getTimeviewStartDate(), model.getTimeviewEndDate(),
                Calendar.MINUTE);
        double offsetx = r.getWidth()*diff/diff_all;
        double width = r.getWidth()*diff_view/diff_all;
        double offsety = r.getY()+r.getHeight()/2-6;
        if(dragmode==2)
            width+= this.scaleDragQuota;
        else if(dragmode==3){
            offsetx += this.scaleDragQuota;
            width-= this.scaleDragQuota;
        }
        width = 120;
        RoundRectangle2D rect = new RoundRectangle2D.Double(r.getX()+offsetx, offsety, width, 25.0, 4, 4);
        Area brick = new Area(rect);
        Area inner = null;
        brick.subtract((inner=GeomOperator.centerFixShrinkCopy(brick, 2)));
        rectBrick = rect;


        //OriPainter.fillAreaWithSingleColor(g, brick, new Color(233,233,233,200));
        OriPainter.drawAreaBorderWithSingleColor(g, brick, Color.white, 1f);
        //OriPainter.fillAreaWithSingleColor(g, inner, new Color(233,233,233));
        OriPainter.gradientFillArea(g, inner, new Color(249,249,249), new Color(198,198,198), true);
        OriPainter.drawAreaBorderWithSingleColor(g, inner,
                new Color(198,198,198), 1);
        brick = GeomOperator.centerFixShrinkCopy(brick, 1);
        
        inner = GeomOperator.offsetCopy(inner, 0, 2);
        Date dt = OriDate.midDate(comp.getModel().getTimeviewStartDate(),
                    comp.getModel().getTimeviewEndDate());
        String sLabel = OriDate.formatDate(dt, "yyyy  MM  dd");
        OriPainter.drawStringInAreaCenter(g, inner, sLabel, model.getInputSFont(),  Color.BLACK);
    }
    
    //绘制刻度面板
    private void drawRulerMeter(Graphics g, Rectangle2D r, int type){
        RulerMeterDef runit = comp.getModel().fetchRulerLabelUnit(type);
        //calc the broken item.
        double blksize =  (r.getWidth()/runit.getSblocknum());
        Date dt = runit.getBeginDate();
        double left = runit.getSblockdiff()*blksize;
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1.0f));
        
        double offset = r.getX()-left;
        int count = (int)(runit.getSblocknum()+runit.getSblockdiff())+1;
        double offsety = r.getY()+UPLEFTHEIGHT+2;
        if(type==0){
            g2d.setPaint(Color.BLACK);
            offsety+=2;
        }
        else
            g2d.setPaint(Color.WHITE);
        for(int i=0;i<count;i++){
            Point2D pt1 = new Point2D.Double(offset, offsety+UPLEFTHEIGHT/2);
            Point2D pt2 = new Point2D.Double(offset, offsety+UPLEFTHEIGHT);
            //draw short line.
            if(pt1.getX()>r.getX() && pt1.getX()<=r.getX()+r.getWidth())
                g2d.drawLine((int)pt1.getX(), (int)pt1.getY(), (int)pt2.getX(), (int)pt2.getY());

            int separator = runit.seperatorNumber/runit.increaseNumber;
            //draw long line.
            Date curDate = OriDate.getShiftDate(dt, runit.increaseUnit,
                        runit.increaseNumber*i);
            Point2D pt3 = new Point2D.Double(offset, offsety);
            if(runit.increaseUnit==Calendar.DATE && runit.seperatorNumber>1){
                int day = OriDate.getDatePart(curDate, Calendar.DATE);
                if(day==1)
                    drawLongLabelWithAnnotation(g2d, r, runit, curDate, pt1, pt3, pt2, type);
                if(runit.seperatorNumber==15) {//半月
                    if(day==15)
                        drawLongLabelWithAnnotation(g2d, r, runit, curDate, pt1, pt3, pt2, type);
                }
            }else if(runit.increaseUnit==Calendar.MONTH){
                int month = OriDate.getDatePart(curDate, Calendar.MONTH)+1;
                if(month==1)
                    drawLongLabelWithAnnotation(g2d, r, runit, curDate, pt1, pt3, pt2, type);
                if(month%runit.seperatorNumber==1)
                    drawLongLabelWithAnnotation(g2d, r, runit, curDate, pt1, pt3, pt2, type);
            }
            else if((separator == 1 && i % 2 == 0) ||
                    (separator > 1 && i % separator == 0)){
                
                drawLongLabelWithAnnotation(g2d, r, runit, curDate, pt1, pt3, pt2, type);
            }
            offset+=blksize;
        }
    }
   
    
    //绘制长标尺和对应标注
    private void drawLongLabelWithAnnotation(Graphics2D g2d, Rectangle2D r, RulerMeterDef runit,
            Date curDate, Point2D pt1, Point2D pt3, Point2D pt2, int type){
        if(pt1.getX()>r.getX() && pt1.getX()<=r.getX()+r.getWidth()){
            g2d.drawLine((int)pt3.getX(), (int)pt3.getY(), (int)pt2.getX(), (int)pt2.getY());
            if(runit.drawZeroHour && OriDate.isZeroHour(curDate)){
                OriPainter.drawStringAtPoint(g2d, comp.getModel().getLabelFont(),
                    pt3, OriDate.formatDate(curDate, "yyyy/MM/dd") );
            }else{ //Fix the bug (the top bar)
                if( type==1 && ( (runit.increaseUnit==Calendar.HOUR && runit.seperatorNumber==24) ||
                    (runit.increaseUnit==Calendar.DATE && runit.seperatorNumber==1) )   )
                    OriPainter.drawStringAtPoint(g2d, comp.getModel().getLabelFont(),
                        pt3, OriDate.formatDate(curDate, "MM/dd"));
                else
                    OriPainter.drawStringAtPoint(g2d, comp.getModel().getLabelFont(),
                        pt3, OriDate.formatDate(curDate, runit.formatPattern));
            }
        }
    }
    //绘制背景，以后换背景的时候，可以只需要需改这个函数
    protected void drawBackground(Graphics g){
        //draw the uper part.
        Rectangle r = new Rectangle(0, UPLEFTHEIGHT*2+1 , comp.getWidth(),
                1);
        OriPainter.gradientFillArea(g, new Area(r),
                new Color(128,128,128), new Color(128,128,128),true);
        /*r = new Rectangle(0, UPLEFTHEIGHT+2 , comp.getWidth(),
                UPLEFTHEIGHT);
        OriPainter.gradientFillArea(g, new Area(r),new Color(127,211,211),
                new Color(108,186,186), true);*/
        //draw the below part.
        Rectangle rect = new Rectangle(0, UPLEFTHEIGHT*2+2 , comp.getWidth(),
                comp.getHeight()-(UPLEFTHEIGHT*2+2));
        OriPainter.fillAreaWithSingleColor(g, new Area(rect),
                new Color(203,205,205));
        Graphics2D g2d = (Graphics2D)g;
        Rectangle2D r2d = new Rectangle2D.Double(0, UPLEFTHEIGHT*2+2, comp.getWidth(),
                comp.getHeight()-(UPLEFTHEIGHT*2+2));
        RadialGradientPaint p = new RadialGradientPaint(
             new Point2D.Double(comp.getWidth()/2, comp.getWidth()/2+18),
             (float) (comp.getWidth()/2+100),
             new Point2D.Double(comp.getWidth()/2, comp.getWidth()/2+18),
             new float[] { 0.0f, 0.65f, 1.0f },
             new Color[] { new Color(255,255,255,255),
                new Color(255,255,255,220),
                new Color(255,255,255,0)
              },
            RadialGradientPaint.CycleMethod.NO_CYCLE);
        g2d.setClip(r2d);
        g2d.setPaint(p);
        g2d.fill(r2d);
        g2d.setClip(null);
        
         
    }

    //绘制两边的文字（时间阶段开始和结束时间）
    protected void drawTimePeriodInfo(Graphics g){
        TimeAxisModel model = comp.getModel();
        float slideareapercent = model.getSlideAreaPercent();
        double size = comp.getWidth()* (1-slideareapercent)/2;
        Rectangle2D r = new Rectangle2D.Double(0, comp.getHeight()*2.0/3,
                size, comp.getHeight()/3);

        //draw a arrow.
        int arrowsize = 10;

        drawArrow(g, r, Color.WHITE, arrowsize, true);
        OriPainter.drawStringInAreaAlign(g, new Area(r),
                OriDate.formatDate(model.getTimePeriodStartDate(), "yyyy/MM/dd"),
                model.getLabelFont(), Color.WHITE, JTextField.LEFT_ALIGNMENT, arrowsize);

        String rightLabel = "后来";
        Date dt = null;
        if((dt=model.getTimePeriodEndDate())!=null){
            rightLabel = OriDate.formatDate(dt, "yyyy/MM/dd");
        }
        r = new Rectangle2D.Double(comp.getWidth()-size, comp.getHeight()*2.0/3,
                size, comp.getHeight()/3);
        drawArrow(g, r, Color.WHITE, arrowsize, false);
        OriPainter.drawStringInAreaAlign(g, new Area(r),
                rightLabel,
                model.getLabelFont(), Color.WHITE, JTextField.RIGHT_ALIGNMENT, -arrowsize);
    }
    
    //绘制停止符号
    private void drawArrow(Graphics g, Rectangle2D r, Color clr, int arrowsize, boolean left){
        GeneralPath path = new GeneralPath();
        double offsety =r.getHeight()/3-1;
        int scale = left==true?1:-1;
        double offsetx = left==true?0:r.getWidth();
        path.moveTo(r.getX()+offsetx,r.getY()+offsety);
        path.lineTo(r.getX()+offsetx+arrowsize/2*scale,r.getY()+arrowsize/2+offsety);
        path.lineTo(r.getX()+offsetx,r.getY()+arrowsize+offsety);
        path.closePath();

        OriPainter.fillAreaWithSingleColor(g, new Area(path), clr);
    }

    //事件处理机制
    public void procMousePressed(MouseEvent e){
        int flag = measureRelBetwenMouseAndBrick(e);
        if(flag==0) {//移动滑块
            this.dragmode = 0;
            this.dragStartPoint = e.getPoint();
            calcMoveSpace();
        }else if(flag==3){ //移动滑动区域
            this.dragmode = 1;
            this.dragStartPoint = e.getPoint();
            calcMoveSpace();
        } else if(flag==2){ //右扩
            this.dragmode = 2;
            this.dragStartPoint = e.getPoint();
            calcScaleSpace(2);
        }else if(flag==1){ //左扩
            this.dragmode = 3;
            this.dragStartPoint = e.getPoint();
            calcScaleSpace(3);
        }
    }
    //计算缩放空间
    private void calcScaleSpace(int mode){
        if(mode==2){
            this.r_move_left = rectSlideArea.getWidth()+rectSlideArea.getX()-
                        rectBrick.getX() - rectBrick.getWidth();

            this.l_move_left = rectBrick.getWidth()-1.0; //最小压缩到0
        }else {
            this.r_move_left =  rectBrick.getWidth()-1.0;

            this.l_move_left = rectBrick.getX()-rectSlideArea.getX(); //拉到头
        }
    }
    //计算移动空间
    private void calcMoveSpace(){
        this.r_move_left = rectSlideArea.getWidth()+rectSlideArea.getX()-
                    rectBrick.getX() - rectBrick.getWidth();

        this.l_move_left = rectBrick.getX() - rectSlideArea.getX();
    }

    public void procMouseMoved(MouseEvent e){
        int flag = measureRelBetwenMouseAndBrick(e);
        //System.out.println(flag);
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if(flag==-1) return;
        if(flag==1 || flag==2)
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        else if(flag == 0 || flag==3){
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }else if(flag==5){
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else {
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    //判断光标在滑动块的相对位置
    //-1 0- 内部 1-left edge 2-right edge. 3-timerow.
    private int measureRelBetwenMouseAndBrick(MouseEvent e){
        if(areaTriangle==null) return -1;
        if(areaTriangle.contains(e.getPoint())) return 5;
        if(rectBrick==null) return -1;
        double mx = e.getPoint().getX();
        double my = e.getPoint().getY();

        if(my<rectBrick.getY() ||  my>rectBrick.getY()+rectBrick.getHeight())
            return -1;
        if(rectBrick.contains(e.getPoint())) return 0;
        double lx = rectBrick.getX();
        double rx = rectBrick.getX()+rectBrick.getWidth();

        if(doubleEqual(mx, lx)) return 1;
        if(doubleEqual(mx, rx)) return 2;

        if(this.rectSlideArea.contains(e.getPoint()) &&
                !rectBrick.contains(e.getPoint())) return 3;
        
        return -1;        
    }
    //对比两个Double数据类型变量
    private boolean doubleEqual(double x, double y){
        double delta = 3.0;
        if(x>=y-delta && x<=y+delta) return true;
        return false;
    }

    //使用拖拽来实现即时绘制，并不立即需改背后数据
    public void procMouseDragged(MouseEvent e){
        switch(this.dragmode){
            case 0:
            case 1:
                double dragDelta = e.getX() - this.dragStartPoint.getX();
                dragStartPoint = e.getPoint();
                dragDelta = validateBrickDragMove(dragDelta);
                updateModel(dragDelta, 0);
                comp.repaint();
                calcMoveSpace();
                break;
            case 2:
            case 3:
                scaleDragQuota = e.getX() - this.dragStartPoint.getX();
                scaleDragQuota = validateBrickDragMove(scaleDragQuota);
                
                comp.repaint();
                break;
        }
        
    }
    //判断滑动是否在滑动的范围内
    private double validateBrickDragMove(double dragDelta){
        if(dragDelta>0 && Double.compare(dragDelta, r_move_left)>=0) return r_move_left;
        if(dragDelta<0 && Double.compare(Math.abs(dragDelta),l_move_left)>=0) return -l_move_left;
        return dragDelta;
    }
    
    public void procMouseReleased(MouseEvent e){
        if(dragmode==2 || dragmode==3){
            updateModel(scaleDragQuota, 1);
            
        }
        scaleDragQuota = 0.0;
        dragmode = -1;
        comp.repaint();
        
    }
    //修改后台模型进而触发对应的事情
    private void updateModel(double dragDelta, int mode){
        TimeAxisModel model = comp.getModel();
        if(mode==0){
            long diff_all = OriDate.dateDiff(model.getSlideAreaStartDate(), model.getSlideAreaAEndDate(),
                    Calendar.MINUTE);

            Long change = (long)((dragDelta/this.rectSlideArea.getWidth())*diff_all);
            model.moveTimeAxix(change.intValue(), dragmode);
        }else{
            long diff_all = OriDate.dateDiff(model.getSlideAreaStartDate(), model.getSlideAreaAEndDate(),
                    Calendar.MINUTE);
            Long change = (long)((dragDelta/this.rectSlideArea.getWidth())*diff_all);
            model.scaleTimeAxis(change.intValue(), dragmode);
            //System.out.println(change);
        }
    }


    public void procMouseClicked(MouseEvent e){
        if(this.areaTriangle==null || this.rectBrick==null) return;
        if(this.areaTriangle.contains(e.getPoint())){
            comp.reset();
            //return;
        }
        if(this.rectBrick.contains(e.getPoint()) && e.getClickCount()==2){
            this.blockInputMode=true;
            //comp.repaint();
            //System.out.println("here, I should process input...");
            //return;
        }else if(this.rectBrick.contains(e.getPoint())){
            if(this.blockInputMode==true){
                Point pt = comp.getLocationOnScreen();
                procMouseClick(e, new Point((int)this.rectBrick.getX()-8+pt.x,
                        (int)this.rectBrick.getY()+pt.y
                        +(int)rectBrick.getHeight()/2-110/2-(110-95)/2-3));
            }
        }else{
            this.blockInputMode = false;
            //comp.repaint();
        }

        
    }

    public void procMouseClick(MouseEvent e, Point pt){
        if(innerWin==null){
            int width = 140;

            innerWin= new OWinDialog(new Dimension(width, 110));
            innerWin.getModel().setCornerradius(8);
            innerWin.getModel().setShadowsize(4);
            innerWin.setLocation(pt);
            //innerWin.setAlwaysOnTop(true);
            innerWin.setModal(true);
            innerWin.pack();
            //innerWin.setAlwaysOnTop(true);
            Date dt = OriDate.midDate(comp.getModel().getTimeviewStartDate(),
                    comp.getModel().getTimeviewEndDate());
            DateTimePickerPanel pane = new DateTimePickerPanel(dt, false);
            innerWin.getContentPane().add(pane);
            innerWin.addWindowFocusListener(
                    new WindowFocusListener() {
                    public void windowGainedFocus(WindowEvent e) {
                        //do nothing
                    }
                    public void windowLostFocus(WindowEvent e) {
                        hideInnerWin(null);
                    }
                    });
            pane.setBounds(6, 6, width-12, 100);
            pane.requestFocus();
            pane.setFocusable(true);
            pane.addPickListSelectionListener(comp);
        }
        //innerWin.addKeyListener(comp);
        //innerWin.requestFocus();
        innerWin.setVisible(true);
    }

    public void hideInnerWin(OTimeAxis comp){
        this.blockInputMode = false;
        if(innerWin!=null){
            innerWin.setVisible(false);
            innerWin.dispose();
            innerWin = null;
        }
    }


    //inner class
    public class DateTimePickerPanel extends JPanel implements MouseMotionListener ,
        MouseListener, MouseWheelListener {
        private int cell_number = 5;
        private int cell_padding = 2;
        private int cell_width = 40;
        private int cell_height = 96;
        private int cell_item_height = 18;
        private Date current_date = new Date();

        private Area [] m_area = null;
        private Area m_selArea = null;

        protected List<BlockSelectionListener> listenerList =
                new ArrayList<BlockSelectionListener>();

         public void addPickListSelectionListener(BlockSelectionListener l) {
            listenerList.add(l);
        }

        /**
         * {@inheritDoc}
         */
        public void removeChangeListener(BlockSelectionListener l) {
            listenerList.remove(l);
        }

        protected void firePickListSelection() {
            BlockSelectionEvent e = new BlockSelectionEvent(this, this.current_date);
            for(BlockSelectionListener listener: listenerList){
                listener.blockSelectChange(e);
            }

        }


        public DateTimePickerPanel(Date dt, boolean isfull){
            if(!isfull) cell_number = 3;
            if(dt!=null)
                current_date = dt;
            m_area = new Area[cell_number];
            m_selArea = new Area();
            //this.setOpaque(false);
            initEvents();
        }

        /**
         * 初始化事件信息
         */
        private void initEvents(){
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addMouseWheelListener(this);
        }

        @Override
        public boolean isFocusTraversable(){
            return true;
        }

        @Override
        public void paint(Graphics g){
            //paint the scroll.
            g.setColor(new Color(249,249,249));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            int offsetx = 2;
            int offsety = 0;
            for(int i=0;i<cell_number;i++){
                Area blk = new Area(new RoundRectangle2D.Double(
                        offsetx, offsety, cell_width, cell_height,
                        4, 4));
                m_area[i] = blk;
                //OriPainter.drawAreaInnerShadow(g, rc, Color.BLACK, 3, 0, Math.PI*3/2, 0.4);
                OriPainter.gradientMiddleFillArea(g, blk, new Color(220, 220, 220),
                        Color.WHITE, true);

                //draw the separator line
                Rectangle2D rc = new Rectangle2D.Double(offsetx, offsety+cell_height/2-cell_item_height/2,
                        cell_width, cell_item_height);
                m_selArea.add(new Area(rc));
                OriPainter.drawAreaBorderWithSingleColor(g, new Area(rc), new Color(217,217,217), 1);
                OriPainter.drawAreaBorderWithSingleColor(g, blk, new Color(203,203,203), 1);
                // now let draw string.
                drawLabels(g, i, rc);
                //OriPainter.gradientFillArea(g, blk, new Color(0,0,0,76), new Color(0,0,0, 0), true);
                offsetx += (cell_width+ cell_padding);
            }
        }

        /**
         * 绘制数字信息
         *
         * @param g
         * @param ind
         * @param rc
         */
        private void drawLabels(Graphics g, int ind, Rectangle2D rc){
            Calendar cal = Calendar.getInstance();
            cal.setTime(current_date);
            int num = 0;
            String format = "";
            switch(ind){
                case 0:
                    num = cal.get(Calendar.YEAR);
                    format = "%04d";
                    break;
                case 1:
                    num = cal.get(Calendar.MONTH)+1;
                    format = "%02d";
                    break;
                case 2:
                    num = cal.get(Calendar.DAY_OF_MONTH);
                    format = "%02d";
                    break;
                case 3:
                    num = cal.get(Calendar.HOUR_OF_DAY);
                    format = "%02d";
                    break;
                case 4:
                    num = cal.get(Calendar.MINUTE);
                    format = "%02d";
                    break;
            }
            OriPainter.drawStringInArea(g, new Font("Verdana", Font.BOLD, 12),
                    new Area(rc), String.format(format, num),
                    Color.BLACK);
            Area tmp = new Area((Rectangle2D)rc.clone());
            for(int i=0;i<2;i++){
                GeomOperator.offset(tmp, 0, -cell_item_height);
                if( (num-i-1>0) || (ind>=3 && num-i-1>=0) )
                OriPainter.drawStringInArea(g, new Font("Verdana", Font.PLAIN, 12),
                    tmp, String.format(format, num-i-1),
                    new Color(0,0,0, (int)((1-(i+1)*0.7/2)*255)));
            }
            tmp = new Area((Rectangle2D)rc.clone());
            for(int i=0;i<2;i++){
                GeomOperator.offset(tmp, 0, cell_item_height);
                if((ind==1 && num+i+1<=12) || (ind==0)
                   || (ind==2 && num+i+1 <=validateMaxDay())
                  || (ind==3 && num+i+1<=24)
                  || (ind==4 && num+i+1<=59))
                OriPainter.drawStringInArea(g, new Font("Verdana", Font.PLAIN, 12),
                    tmp, String.format(format, num+i+1),
                    new Color(0,0,0, (int)((1-(i+1)*0.7/2)*255)));
            }

        }
        /**
         * 获取各月合适的天数
         * @return
         */
        private int validateMaxDay(){
            Calendar cal = Calendar.getInstance();
            cal.setTime(current_date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;

            if(year%4==0 && month==2)
                return 29;
            else if(month==2)
                return 28;
            else if(month==1 || month==3  || month==5 ||
                    month==8 || month==10 || month==12)
                return 31;
            else
                return 30;

        }



        private Point ptDragStart = null;
        private boolean mouseDragged = false;
        public void mouseDragged(MouseEvent e) {
            Point pt = e.getPoint();
            if(!mouseDragged) return;
            int type = guessType(ptDragStart);
            if(type==-1) return;
            scrollNumber(pt.y-ptDragStart.y);
            ptDragStart  = e.getPoint();
        }
        private int guessType(Point pt){
            int ret = -1;
            if(pt==null) return ret;
            for(int i=0;i<this.cell_number;i++){
                if(m_area[i].contains(pt)){
                    ret = i;
                    break;
                }
            }
            return ret;
        }
        public void mouseMoved(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
            ptDragStart = e.getPoint();

        }

        public void mouseClicked(MouseEvent e) {
            Point pt = e.getPoint();
            if(m_selArea.contains(pt)){
                //System.out.println("selected!");
                firePickListSelection();
            }
           // throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mousePressed(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
            ptDragStart = e.getPoint();
            mouseDragged = true;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDragged = false;
        }

        public void mouseEntered(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            int amount = 0;
            if(e.getWheelRotation()<0){ //up
                amount = e.getScrollAmount();
            }else{
                amount = -e.getScrollAmount();
            }
            scrollNumber(amount);
        }

        private void scrollNumber(int amount){
            int type = guessType(ptDragStart);
            if(type==-1) return;

            int num = (int)(amount/32*2);
            if(num==0 && amount>0)
                num = 1;
            else if(num==0 && amount<0)
                num = -1;
            Calendar cal = Calendar.getInstance();
            cal.setTime(current_date);
            switch(type){
                case 0:
                    cal.add(Calendar.YEAR, -num);
                    break;
                case 1:
                    cal.add(Calendar.MONTH, -num);
                    break;
                case 2:
                    cal.add(Calendar.DATE, -num);
                    break;
                case 3:
                    cal.add(Calendar.HOUR_OF_DAY, -num);
                    break;
                case 4:
                    cal.add(Calendar.MINUTE, -num);
                    break;
            }
            current_date = cal.getTime();

            repaint();
        }
    }
}
