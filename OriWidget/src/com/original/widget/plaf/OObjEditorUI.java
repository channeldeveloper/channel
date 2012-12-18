/*
 *  com.original.widget.plaf.OObjEditorUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OObjEditor;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.ObjectMesModel;
import com.original.widget.model.ObjectMesModel.INNEROBJTYPE;
import com.original.widget.model.ObjectMesModel.OInnerObject;
import com.original.widget.model.TextBlockModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 * Update List
 * 1. Basic Implements was finished.
 * 2. Operate different kinds of item types.
 * 3. 增加鼠标拖出删除特性
 *    3.1 修复了被编辑块被删除的情况下的光标问题
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 6, 2012 11:26:29 PM
 */
public class OObjEditorUI extends BasicPanelUI {
    //光标闪烁控制符
    private boolean m_cursor_sparkling_light = true;
    //光标位置
    private int m_cpos = -1;
    //视图左界，右界为m_view_x+this.getWidth()
    private int m_view_x = 20;

    //绘图区域处理
    private final int cell_padding = 5+10;

    //各对象之间的间隔
    private int item_spacing = 9;


    //绘图控制用
    private Map<OInnerObject, Integer> positions = new HashMap<OInnerObject, Integer>();
    private Point mouseDraggedStartPoint = null;
    private boolean mouseDraggedOut = false;

    private FontMetrics m_fm = null;
     
// Shared UI object
    //private static OObjEditorUI cui;
    private OObjEditor comp;
    private ObjectMesModel model;

    public static ComponentUI createUI(JComponent c) {
        return new OObjEditorUI(c);
    }

    public OObjEditorUI(JComponent com) {
        comp = (OObjEditor)com;
    	model = comp.getModel();
    }

    public void redraw() {
        if (model == null){
			model = comp.getModel();
		}
		model.setSize(comp.getHeight(), comp.getWidth());
    }

    @Override
    public void update(Graphics g, JComponent c){
        if (model == null){
			model = comp.getModel();
		}
        int width = comp.getWidth();
        int height = comp.getHeight();

        //圆角长方形
        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                TextBlockModel.CORNERRADIUS/2,
                TextBlockModel.CORNERRADIUS/2,
                width-TextBlockModel.CORNERRADIUS,
                height-TextBlockModel.CORNERRADIUS,
                TextBlockModel.CORNERRADIUS,
                TextBlockModel.CORNERRADIUS);
        //Draw the background frame.
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

        onPaint(g);


        super.update(g,c);
     }

     /**
      * 附加绘制函数
      * @param g
      */
     protected void onPaint(Graphics g){
         if(m_fm==null)
            m_fm = g.getFontMetrics();
        drawInnerObjects(g);
        int width = comp.getWidth()-6;   //视图宽度
        if(m_cpos < width){
            m_view_x   =   0;
        }
        if(m_cpos> width){
            m_view_x   =   m_cpos-width;
        }
        if(m_cpos<=cell_padding-1)
            m_cpos = cell_padding-4;

         drawCaret(g);
     }
     /**
     * 绘制内部的对象
     * @param g
     */
    private void drawInnerObjects(Graphics g){
        int bordersizex = 10;
        int bordersizey = 1;
        Graphics2D g2d = (Graphics2D)g;
        //避免擦除边框
        g2d.setClip(bordersizex,bordersizey,
                comp.getWidth()-bordersizex*2,
                comp.getHeight()-bordersizey*2);

        g2d.translate(1-m_view_x,1);
        //
        int offsetx = 1+cell_padding;

        Iterator<OInnerObject> iter = model.iter();
        while(iter.hasNext()){
            OInnerObject obj = iter.next();
            positions.put(obj, offsetx);
            offsetx = drawInnerObject(g2d, obj, offsetx);
            offsetx += this.item_spacing;
        }
    }

    /**
     * 绘制内部对象，这个可以使用特定的Render来画，但这一次简化处理
     * @param g2d
     * @param obj
     * @param offsetx
     * @return
     */
    private int drawInnerObject(Graphics2D g2d, OInnerObject obj, int offsetx){
        int ret = 0;

        g2d.setFont(model.getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int top = (comp.getHeight()-fm.getHeight())/2 + (int)(fm.getHeight()*0.7);
        if(obj.getType() == INNEROBJTYPE.PLAIN){

            //first, draw the surrounding
            RoundRectangle2D r2d = new RoundRectangle2D.Double(offsetx-2, top-fm.getHeight()*0.7-1,
                    fm.stringWidth(obj.getDisplay())+4,
                    fm.getHeight()+2, 8, 8);
            g2d.setPaint(Color.gray);
            float patter[]={2,2};
            int cap=BasicStroke.CAP_SQUARE;
            int join=BasicStroke.JOIN_ROUND;
            BasicStroke dash=new BasicStroke(1.0f,cap,join,10.0f,patter,0.0f);
            g2d.setStroke(dash);
            g2d.draw(r2d);
            //draw the relevant content
            g2d.setColor(Color.BLACK);
            g2d.drawString(obj.getDisplay(), offsetx, top);
            ret = offsetx+ fm.stringWidth(obj.getDisplay());
        }else{
            RoundRectangle2D r2d = new RoundRectangle2D.Double(offsetx-2, top-fm.getHeight()*0.7-1,
                    fm.stringWidth(obj.getDisplay())+4,
                    fm.getHeight()+2, 8, 8);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);


            GradientPaint gp = new GradientPaint((int)r2d.getX(), (int)r2d.getY(),
                new Color(215,244,248), (int)r2d.getX(),(int)r2d.getMaxY(), new Color(153,223,233));
            g2d.setPaint(gp);

            g2d.fill(r2d);
            g2d.setStroke(new BasicStroke(1));
            g2d.setPaint(new Color(27,209,230));

            g2d.draw(r2d);
            //draw the relevant content
            g2d.setColor(Color.BLACK);
            g2d.drawString(obj.getDisplay(), offsetx, top);
            ret = offsetx+ fm.stringWidth(obj.getDisplay());
        }
        return ret;
    }

     /**
      * 设置图标状态，绘制动态闪烁光标
      */
     public void resetCaretFlag(){
         m_cursor_sparkling_light =!m_cursor_sparkling_light;
     }
     /**
      * 绘制闪烁光标
      * @param g
      */

     private void drawCaret(Graphics g){
        if(comp.hasFocus() && m_cursor_sparkling_light){
            Graphics2D g2d = (Graphics2D)g;
            FontMetrics fm = g2d.getFontMetrics();
            int cur_pos = m_cpos;
            g2d.setColor(java.awt.Color.BLACK);
            int top = (comp.getHeight()-fm.getHeight())/2 - (int)(fm.getHeight()*0.05);
            Stroke stroke = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2d.setStroke(stroke);
            cur_pos++;
            g2d.drawLine(cur_pos,top,cur_pos, top+(int)(fm.getHeight()*1.0f));
        }
    }


     /**
      * 处理键入事件
      * @param e
      * @param g
      */
     public void procKeyTyped(KeyEvent e, Graphics g){
         char c = e.getKeyChar();
         OInnerObject obj = guessInnerObj2Response(m_cpos);
            if(obj==null){
                StringBuffer buffer = new StringBuffer();
                obj = model.createObject(INNEROBJTYPE.PLAIN, buffer);
                int index = guessPreviousInnerObjByCaretPos(m_cpos);
                if(index==-1){
                    model.add(0,obj);
                }
                else{
                    model.add(index+1, obj);
                }
                //recalculate.
                drawInnerObjects(g);
                m_cpos = positions.get(obj);
            }
            int charOffset =  calcPotentialPos(obj, m_cpos-positions.get(obj),1);
            //如果是逗号，做分割
            // -
            if((c==',' || c=='，') && charOffset>0){
                StringBuffer buffer = (StringBuffer)obj.getObj();
                String sLeft = buffer.substring(0, charOffset);
                String sRight = buffer.substring(charOffset);
                if(sLeft.length()!=buffer.length()){ //cut two parts
                    buffer.delete(charOffset+1, buffer.length());
                }
                StringBuffer buf = new StringBuffer(sRight);
                OInnerObject newobj = model.createObject(INNEROBJTYPE.PLAIN, buf);
                int ind = model.indexOf(obj);
                model.add(ind+1, newobj);
                drawInnerObjects(g);
                m_cpos = positions.get(newobj);
                //清除特定的选择
                fireChanged(obj, 3);
            }else{
                //normal 情况下
                if(obj.getDisplay().isEmpty() && (c==',' || c=='，'))
                    return;

                int strOldWidth = m_fm.stringWidth(obj.getDisplay());
                if(charOffset!=-1)
                    ((StringBuffer)obj.getObj()).insert(charOffset, c);
                else
                    ((StringBuffer)obj.getObj()).append(c);
                int strNewWidth = m_fm.stringWidth(obj.getDisplay());
                m_cpos += (strNewWidth-strOldWidth);

                //键入新的内容
                fireChanged(obj, 0);
            }
     }
     /**
      * 处理控制键事件
      * @param e
      */
     public void procKeyPressed(KeyEvent e){
         //删除处理
        if(e.getKeyCode()==KeyEvent.VK_DELETE){
            procBackwardDelete();

        }else if( e.getKeyCode()==KeyEvent.VK_BACK_SPACE ){
            //和删除类似
            procForwardDelete();
        }

        //注意，我这里只实现了导航，对于选择部分这期不做
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            leftMove();
            this.fireChanged(null, 2);
        }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            rightMove();
            this.fireChanged(null, 2);
        } else if(e.getKeyCode()==KeyEvent.VK_HOME){
            m_cpos = this.cell_padding-3;
            this.fireChanged(null, 2);
        } else if(e.getKeyCode()==KeyEvent.VK_END){
            if(model.size()<=0) return;
            OInnerObject obj = model.get(model.size()-1);
            //if(obj.getDisplay().isEmpty())
            //    m_cpos = positions.get(obj);
            //else
            m_cpos = positions.get(obj) + m_fm.stringWidth(obj.getDisplay()) +3;
            this.fireChanged(null, 2);
        }
        comp.repaint();
     }
     /**
      * 处理鼠标按下事件      * 
      * @param e
      */
     public void procMousePressed(MouseEvent e){
        mouseDraggedOut = false;
        mouseDraggedStartPoint = e.getPoint();
        int x = e.getX();

        int cpos = -1;
        cpos = guessSuitableCaretPos(x);
        if(cpos==-2) return; //不能设置光标到固定区域

        if(m_cpos!=cpos && cpos!=-1){
            m_cpos=cpos;
            //移动到特定的编辑段
            //fireChanged(guessInnerObj2Response(cpos), 3);
        }else{ //
            int ind = -1;
            ind = guessPreviousInnerObjByCaretPos(x);
            if(ind==-1){
                m_cpos = cell_padding-3;
            }else{
                OInnerObject obj = model.get(ind);
                m_cpos = positions.get(obj)
                    +m_fm.stringWidth(obj.getDisplay())
                    +3;
            }
        }
        
     }

     /**
      * 处理鼠标拽动，判断是否是拖拽删除
      * @param e
      */
     public void procMouseDragger(MouseEvent e){
        Point pt = e.getPoint();
        if(pt.x < this.cell_padding || pt.y <=2 ||
                pt.y >= comp.getHeight()-2)
            mouseDraggedOut = true;
     }
     /**
      * 处理拖拽事件
      * @param e
      */
     public void procMouseReleased(MouseEvent e){
         if(mouseDraggedOut){
            OInnerObject obj = guessInnerObj2Response(this.mouseDraggedStartPoint.x);
            if(obj!=null){
                //Check the relvant items.
                OInnerObject objWithCaret = guessInnerObj2Response(m_cpos);
                if(obj==objWithCaret){ //被编辑的要删除
                    int ind = model.indexOf(obj);
                    if(ind>0){//
                        OInnerObject tmp = model.get(ind-1);
                        m_cpos = positions.get(tmp) +
                                m_fm.stringWidth(tmp.getDisplay()) + 3;
                    }else{
                        m_cpos = this.cell_padding-4;
                    }
                    
                }
                else if(m_cpos > positions.get(obj) + m_fm.stringWidth(obj.getDisplay()))
                    m_cpos -=  (m_fm.stringWidth(obj.getDisplay())+this.item_spacing);

                model.remove(obj);
                //移出特定编辑块
                fireChanged(obj, 2);
            }
        }
     }

     /**
     * 往后删除
     * 在编辑区域内一个字符一个字符删除，否则一块一块删除
     */
    private void procBackwardDelete(){
        OInnerObject obj = guessInnerObj2Response(m_cpos);
        if(obj==null){ //区域删除
            int ind = guessNextInnerObjByCaretPos(m_cpos);
            if(ind==-1) return; // 没有内容需要删除
            model.remove(ind);
        }else{
            int charPos = calcPotentialPos(obj, m_cpos-positions.get(obj),1);
            if(charPos == obj.getDisplay().length()) return ;//无内容可删
            else{
                StringBuffer buf = (StringBuffer)obj.getObj();
                if(charPos+1<buf.length())
                    buf.deleteCharAt(charPos+1);
                else if(charPos>0)
                    buf.deleteCharAt(charPos);
                //删除特定编辑区
                fireChanged(obj, 1);
            }
        }
    }
    /**
     * 往前删除
     * 在编辑区域内一个字符一个字符删除，否则一块一块删除
     */
    private void procForwardDelete(){
        OInnerObject obj = guessInnerObj2Response(m_cpos);
        if(obj==null){ //区域删除
            int ind = guessPreviousInnerObjByCaretPos(m_cpos);
            if(ind==-1) return; // 没有内容需要删除
            model.remove(ind);
            if(ind>0){
                obj = model.get(ind-1);
                m_cpos = positions.get(obj) +
                        m_fm.stringWidth(obj.getDisplay()) + 3;
            }else{
                m_cpos = this.cell_padding-3;
            }

        }else{
            int charPos = calcPotentialPos(obj, m_cpos-positions.get(obj),1);
            if(charPos == -1) return ;//无内容可删
            else{
                StringBuffer buf = (StringBuffer)obj.getObj();
                char ch = buf.charAt(charPos-1);
                buf.deleteCharAt(charPos-1);
                m_cpos -= m_fm.charWidth(ch);

                //删除特定编辑区
                fireChanged(obj, 1);
            }
        }
    }
    /**
     * 移动光标，当月左移动键被按
     * Update LOG
     * * 1. 对于Navigate，添加对块的处理
     */
    private void leftMove(){
        OInnerObject obj = guessInnerObj2Response(m_cpos);
        if(obj==null){
            int ind = guessPreviousInnerObjByCaretPos(m_cpos);
            if(ind==-1) return;
            obj = model.get(ind);
            if(obj.getType()==INNEROBJTYPE.BLOCK){
                m_cpos = positions.get(obj) - 5;
            }else{
                m_cpos = positions.get(obj) + m_fm.stringWidth(obj.getDisplay());
            }
        }else{
            int charPos = calcPotentialPos(obj, m_cpos-positions.get(obj),1);
            if(charPos==-1){ //到了这个编辑区的首部
                int ind = model.indexOf(obj);
                if(ind==0){ //最左边的一个编辑区
                    m_cpos = this.cell_padding-3;
                }else{
                    obj = model.get(ind-1);
                    if(obj.getType()==INNEROBJTYPE.BLOCK)
                        m_cpos = positions.get(obj) +
                                m_fm.stringWidth(obj.getDisplay()) + 3;
                    else
                        m_cpos = positions.get(obj) +
                        m_fm.stringWidth(obj.getDisplay());
                }
                //移动到特定的编辑段
                //fireChanged(obj, 2);
            }else{ //往前移动一个字符
                m_cpos = positions.get(obj) +
                        m_fm.stringWidth(obj.getDisplay().substring(0, charPos-1));
                //移动到特定的编辑段
                //fireChanged(obj, 3);
            }

        }
    }

    /**
     * 移动光标，当月右移动键被按
     * * Update LOG
     * * 1. 对于Navigate，添加对块的处理
     */
    private void rightMove(){
        OInnerObject obj = guessInnerObj2Response(m_cpos);
        if(obj==null){
            int ind = guessNextInnerObjByCaretPos(m_cpos);
            if(ind==-1) return; //最后
            if(model.size()>0){
                obj = model.get(ind);
                if(obj.getType()==INNEROBJTYPE.BLOCK)
                    m_cpos = positions.get(obj) +
                            m_fm.stringWidth(obj.getDisplay())+3;
                else
                    m_cpos = positions.get(model.get(ind));
            }
        } //到了队尾
        else{
            int charPos = calcPotentialPos(obj, m_cpos-positions.get(obj),1);
            if(charPos==obj.getDisplay().length()){ //到了这个编辑区的尾部
                int ind = model.indexOf(obj);
                if(ind==model.size()-1){ //最右边的一个编辑区
                    //移动到最后
                     m_cpos = positions.get(obj)
                               + m_fm.stringWidth(obj.getDisplay()) +3;
                }else{
                    obj = model.get(ind+1);
                    if(obj.getType()==INNEROBJTYPE.BLOCK)
                        m_cpos = positions.get(obj) - 5;
                    else
                        m_cpos = positions.get(obj);
                }
            }else{ //往h后移动一个字符
                if(charPos==-1){
                    if(obj.getDisplay().isEmpty()) return;
                    m_cpos = positions.get(obj) +
                        m_fm.stringWidth(obj.getDisplay().substring(0, 1));
                }
                else
                    m_cpos = positions.get(obj) +
                        m_fm.stringWidth(obj.getDisplay().substring(0, charPos+1));
                //移动到特定的编辑段
                //fireChanged(obj, 3);
            }

        }
    }

    //增加一个用于通知函数，目的可以和相关的内容进行捆绑
    //比如使用通讯录
    //type - 0 - add, 1-delete 2-removed. 3-reforcus
    protected void fireChanged(OInnerObject obj, int type){
        comp.fireDocumentChanged(obj, type);
    }
    
     /**
     * 猜测合理的光标位置
     * Update Log
     *   1. 增加对于特殊对象的处理
     * @param x
     * @return
     */
    private int guessSuitableCaretPos(int x){
        int ret = -1;
        Iterator<OInnerObject> iter = model.iter();
        while(iter.hasNext()){
            OInnerObject obj = iter.next();
            int blkStart = positions.get(obj);
            int blkEnd  = blkStart + m_fm.stringWidth(obj.getDisplay());
            if(x>blkStart && x< blkEnd){
                if(obj.getType()==INNEROBJTYPE.BLOCK)
                    ret = -2; //不能设置鼠标到固定区域
                else
                    ret = blkStart + calcPotentialPos(obj, x-blkStart, 0);
                break;
            }
        }
        return ret;
    }
     /**
     * 猜测那个内部对象处于选中状态
     * @param x
     * @return
     */

    private OInnerObject guessInnerObj2Response(int x){
        OInnerObject ret = null;
        Iterator<OInnerObject> iter = model.iter();
        while(iter.hasNext()){
            OInnerObject obj = iter.next();
            int blkStart = positions.get(obj);
            int blkEnd  = blkStart + m_fm.stringWidth(obj.getDisplay());
            if(x>=blkStart-1 && x<=blkEnd+1){
                ret = obj;
                break;
            }
        }
        return ret;
    }
    /**
     * 如果光标在最后或者在元素之间，获取前面一个元素的索引
     * @param x
     * @return
     */
    private int guessPreviousInnerObjByCaretPos(int x){
        int ret = -1;
        int ind = 0;
        Iterator<OInnerObject> iter = model.iter();
        while(iter.hasNext()){
            OInnerObject obj = iter.next();
            int blkStart = positions.get(obj);
            int blkEnd  = blkStart + m_fm.stringWidth(obj.getDisplay());
            if(x >= blkEnd+2){
                ret = ind;
                //break;
            }
            ind++;
        }
        return ret;
    }

    /**
     * 如果光标在最后或者在元素之间，获取后面一个元素的索引
     * @param x
     * @return
     */
    private int guessNextInnerObjByCaretPos(int x){
        int ret = -1;
        int ind = 0;
        Iterator<OInnerObject> iter = model.iter();
        while(iter.hasNext()){
            OInnerObject obj = iter.next();
            int blkStart = positions.get(obj);
            if(x <= blkStart-2){
                ret = ind;
                break;
            }
            ind++;
        }
        return ret;
    }

    /**
     * 获取在对应区域光标的位置
     * @param obj
     * @param x
     * @return
     */
    private int calcPotentialPos(OInnerObject obj, int w, int type){
        String display = obj.getDisplay();
        if(display.length()==0){
            if(type==0)
                return 0;
            else
                return -1;
        }
        if(w<=0) {
            if(type==0)
                return 0;
            else
                return -1;
        }

        int flag = -1;
        int len = display.length();
        double rw = 0;
        for(int i=0;i<len;i++){
           double lw = 0;
           if(i>=1)
               lw =m_fm.stringWidth(display.substring(0, i-1));;
           double mw = m_fm.stringWidth(display.substring(0, i));
           if(i<len-1)
               rw = m_fm.stringWidth(display.substring(0, i+1));
           else
               rw = m_fm.stringWidth(display);
           if(w>=(lw+mw)/2 && w<=(mw+rw)/2){
               flag = i;
               break;
            }
        }
        if(flag==-1)
            flag = len;
        if(type==0)
            return m_fm.stringWidth(display.substring(0, flag));
        else
            return flag;
    }

    public void replaceCurrent(Object obj, Graphics g){
        OInnerObject curObj = guessInnerObj2Response(m_cpos);
        if(curObj==null) return;

        curObj.setObj(obj);
        curObj.setType(INNEROBJTYPE.BLOCK);
        drawInnerObjects(g);
        m_cpos = positions.get(curObj)+ m_fm.stringWidth(curObj.getDisplay())+3;
        
    }
}
