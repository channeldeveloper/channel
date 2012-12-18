/*
 *  com.original.widget.plaf.ODatetimeEditorUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.ODatetimeEditor;
import com.original.widget.OWinDialog;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.event.BlockSelectionEvent;
import com.original.widget.event.BlockSelectionListener;
import com.original.widget.model.DatetimeModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 *  修改记录
 *   1. 修正了老杨提出的静态对象应用的Bug，就是多个组件互相干涉现象 2012-05-27
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 23:34:21
 */
public class ODatetimeEditorUI extends BasicPanelUI{
    //private static ODatetimeEditorUI cui;
    private ODatetimeEditor comp;
    private DatetimeModel model;

    OWinDialog innerWin = null;
    
    public static ComponentUI createUI(JComponent c) {
        return  new ODatetimeEditorUI(c);
    }
    public ODatetimeEditorUI(){}
    public ODatetimeEditorUI(JComponent com) {
        comp = (ODatetimeEditor)com;
    }


    @Override
    public void update(Graphics g, JComponent c){
        model = comp.getModel();
        int width = comp.getWidth();
        int height = comp.getHeight();

        //圆角长方形
        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                DatetimeModel.CORNERRADIUS/2,
                DatetimeModel.CORNERRADIUS/2,
                width-DatetimeModel.CORNERRADIUS,
                height-DatetimeModel.CORNERRADIUS,
                DatetimeModel.CORNERRADIUS,
                DatetimeModel.CORNERRADIUS);
        //Draw the background frame.
        OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                model.getBackgroundcolor());
        OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
                DatetimeModel.BORDERCOLOR, 1);

        //draw the inner Shadow.
        //calculate the shadow area.
        Area areaOne = new Area(r2d);
        Area areaTwo = GeomOperator.offsetCopy(areaOne, 0, 2.4);
        areaOne.subtract(areaTwo); //areaOne will be the shadow area.
        //generate the shadow image.
        BufferedImage shadow = OriPainter.drawGradientInnerShadow(g,
                areaOne, DatetimeModel.SHADOWCOLOR, model.getBackgroundcolor(),
                40);
        //paint the shadow.
        OriPainter.drawImage(g, shadow, 0, 0);
        //draw the String.

        OriPainter.drawStringInAreaAlign(g, new Area(r2d), genShowText(model),
                model.getFont(),
                Color.BLACK, JTextField.LEFT_ALIGNMENT, DatetimeModel.CORNERRADIUS);
        super.update(g,c);
     }

    private String genShowText(DatetimeModel model){
        String sFormat = "yyyy-MM-dd HH:mm";
        if(!model.isIsfull())
            sFormat = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(sFormat);
        String title = "";
        if(model.getCurdate()!=null)
            title = fm.format(model.getCurdate());
        else
            title = fm.format(new Date());
        return title;
    }

    public void procMouseClick(MouseEvent e, Point pt, ODatetimeEditor comp){
        model = comp.getModel();
        if(innerWin==null){
            int width = 400;
            if(!model.isIsfull())
                width = 250;
            innerWin= new OWinDialog(new Dimension(width, 200));
            innerWin.setLocation(pt);
            //innerWin.setAlwaysOnTop(true);
            innerWin.setModal(true);
            innerWin.pack();
            //innerWin.setAlwaysOnTop(true);
            DateTimePickerPanel pane = new DateTimePickerPanel(model.getCurdate(),
                    model.isIsfull());
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
            pane.setBounds(15, 15, width-30, 175);
            pane.requestFocus();
            pane.setFocusable(true);
            pane.addPickListSelectionListener(comp);
        }
        innerWin.addKeyListener(comp);
        //innerWin.requestFocus();
        innerWin.setVisible(true);
    }

    public void hideInnerWin(ODatetimeEditor comp){
        if(innerWin!=null){
            innerWin.setVisible(false);
            innerWin.dispose();
            innerWin = null;
        }
    }
    public DateTimePickerPanel createDateTimePicker(Date dt, boolean isFull){
        return new DateTimePickerPanel(dt, isFull);
    }
    //inner classes
    class DateTimePickerPanel extends JPanel implements MouseMotionListener ,
        MouseListener, MouseWheelListener {
        private int cell_number = 5;
        private int cell_padding = 6;
        private int cell_width = 68;
        private int cell_height = 168;
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
                Rectangle2D rc = new Rectangle2D.Double(offsetx, offsety+cell_height/2-16,
                        cell_width, 32);
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
            OriPainter.drawStringInArea(g, new Font("Verdana", Font.PLAIN, 18),
                    new Area(rc), String.format(format, num),
                    Color.BLACK);
            Area tmp = new Area((Rectangle2D)rc.clone());
            for(int i=0;i<2;i++){
                GeomOperator.offset(tmp, 0, -32);
                if( (num-i-1>0) || (ind>=3 && num-i-1>=0) )
                OriPainter.drawStringInArea(g, new Font("Verdana", Font.PLAIN, 16),
                    tmp, String.format(format, num-i-1),
                    new Color(0,0,0, (int)((1-(i+1)*0.7/2)*255)));
            }
            tmp = new Area((Rectangle2D)rc.clone());
            for(int i=0;i<2;i++){
                GeomOperator.offset(tmp, 0, 32);
                if((ind==1 && num+i+1<=12) || (ind==0)
                   || (ind==2 && num+i+1 <=validateMaxDay())
                  || (ind==3 && num+i+1<=24)
                  || (ind==4 && num+i+1<=59))
                OriPainter.drawStringInArea(g, new Font("Verdana", Font.PLAIN, 16),
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
