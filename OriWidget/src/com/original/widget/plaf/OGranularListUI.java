/*
 *  com.original.widget.plaf.OGranularListUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OGranularList;
import com.original.widget.model.GranularListModel;
import com.original.widget.model.GranularListModel.OGranularItem;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 *  多粒度列表绘制类
 *   修改记录
 *   1. 修正了老杨提出的静态对象应用的Bug，就是多个组件互相干涉现象 2012-05-27
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 7, 2012 11:13:02 PM
 */
public class OGranularListUI extends BasicPanelUI {
    //边框预留额度
    private int bordersize = 1;
    //不同块之间间隔
    private int item_cell_spacing = 5;
    //滚动控制标志
    private int m_cpos = 0;
    //对块数据进行临时记录便于规划
    private Map<OGranularItem, Integer> positions = new HashMap<OGranularItem, Integer>();
    
    //private static OGranularListUI cui;
    private OGranularList comp;
    private GranularListModel model;

    public static ComponentUI createUI(JComponent c) {
        return new OGranularListUI(c);
    }

    public OGranularListUI(JComponent com) {
        comp = (OGranularList)com;
    	//model = comp.getModel();
    }
    public void resetScrollPos(int pos, OGranularList comp){
        m_cpos = pos;
    }
    public void clearMemory(){
        positions.clear();
        
    }
    /**
     * 重绘制组件
     */
    //public void redraw() {
    //    if (model == null){
//			model = comp.getModel();
//		}
//		model.setSize(comp.getHeight(), comp.getWidth());
 //   }

    @Override
    public void update(Graphics g, JComponent c){
        model = comp.getModel();
        //int width = model.getWidth();
        //int height = model.getHeight();

        g.setClip(bordersize,bordersize,
        comp.getWidth()-bordersize*2-comp.getScrollBar().getWidth(),
        comp.getHeight()-bordersize*2);
        adjustScrollBar(comp, model);
        drawItems(g, comp, model);
        super.update(g,c);
    }

    /**
     * 绘制所有块Item
     * @param g
     */
    private void drawItems(Graphics g, OGranularList comp, GranularListModel model) {
        Graphics2D g2d = (Graphics2D)g;
        //避免擦除边框

        g2d.translate(1,1-m_cpos);
        //System.out.println(m_cpos);
        int offsety = bordersize * 2+20;
        Iterator<OGranularItem> iter = model.iter();
        while(iter.hasNext()){
            OGranularItem item = iter.next();
            positions.put(item, offsety); //positions.
            //System.out.println(item.getTitle());
            offsety = drawItemBlock(g2d, item, offsety, comp, model);

            offsety += this.item_cell_spacing;
        }
    }
    /**
     * 绘制每一块
     * @param g
     * @param item
     * @param offsety
     * @return
     */
    private int drawItemBlock(Graphics2D g, OGranularItem item, int offsety,
            OGranularList comp, GranularListModel model){
        int blkHeight = calcItemBlockSize(item);
        //System.out.println(blkHeight);
        int blkWidth = comp.getWidth() - bordersize *2;
        //检查是否Visible
        Rectangle rcView = new Rectangle(0, m_cpos, comp.getWidth(), comp.getHeight() );
        Rectangle rcBlk = new Rectangle (10, offsety, 20, blkHeight);
        //如果不在视窗内，不进行绘制
        if(!rcView.intersects(rcBlk)) return offsety+blkHeight;
        //System.out.println(blkWidth);
        BufferedImage cavans = new BufferedImage(blkWidth,blkHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) cavans.getGraphics();
        g2d.setBackground(new Color(255, 255, 255, 0));

        float offset_x = 8;
        float offset_y = 10;
        int _icon_width = 36;


        g2d.drawImage(item.getImg(), (int)offset_x, (int)offset_y, comp);

        //draw name.
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14) );
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(item.getTitle());
        offset_x +=(36+6);
        offset_y +=hgt*0.5f;
        g2d.setColor(Color.BLACK);
        //g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12) );

        g2d.drawString(item.getTitle(), offset_x, offset_y);
        //others
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12) );

        offset_x += (adv+6);
        g2d.setColor(new Color(106,145,174));
        g2d.drawString(item.getAddition(), offset_x, offset_y);
        g2d.setColor(Color.BLACK);

        //draw addresss
        offset_x = (_icon_width+14);
        g2d.setFont(new Font("Courier New", Font.PLAIN, 12) );
        metrics = g.getFontMetrics(g2d.getFont());

        //hgt = metrics.getHeight();
        int i = 1;
        for(Object s: item.getItems()){
            offset_y += hgt*0.8f;
            if(item.isSelected() && i==item.getSelectedindex()){ //draw selection rectangle.
                g2d.setPaint(new Color(174,217,224));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fillRoundRect((int)offset_x-2, (int)(offset_y-hgt*0.6), comp.getWidth()-_icon_width-40, (int)(hgt), 4, 4);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
                g2d.setColor(Color.WHITE);
                g2d.drawString(s.toString(), offset_x, offset_y);
            }else{
                g2d.setColor(Color.BLACK);
                g2d.drawString(s.toString(), offset_x, offset_y);
            }
            
            i++;
        }
        offset_y += hgt*0.3f;

        if(item.isSelected()){
            g2d.setColor(new Color(65,162,181, 100));
        }else{
            g2d.setColor(Color.BLACK);
        }
        g2d.drawLine((int)offset_x, (int)offset_y, (int)offset_x+20, (int)offset_y);
        g.drawImage(cavans, bordersize+1, offsety, null);
        cavans = null;
        return offsety+blkHeight;

    }

    /**
     * 调整滚动条状况
     */
    private void adjustScrollBar(OGranularList comp, GranularListModel model){
        int height = 40;
        Iterator<OGranularItem> iter = model.iter();
        int visibleAmount = 1000;
        while(iter.hasNext()){
            int blkSize = calcItemBlockSize(iter.next());
            visibleAmount = Math.min(blkSize, visibleAmount);
            height += blkSize;
            height += this.item_cell_spacing;
        }
        
        //height -= comp.getHeight();
        int maxHeight = Math.max(height - comp.getHeight(), 0);
        //comp.getScrollBar().setVisibleAmount(20*4);
        comp.getScrollBar().setMaximum(height);
        comp.getScrollBar().setMinimum(20);
        comp.getScrollBar().setEnabled(maxHeight>0);
        //comp.getScrollBar().setVisibleAmount((height-20)/10);
        comp.getScrollBar().setUnitIncrement(1);//设置拖曳滚动轴时，滚动轴刻度一次的变化量。
        comp.getScrollBar().setBlockIncrement(1);
        comp.getScrollBar().setVisibleAmount(comp.getHeight());
        comp.getScrollBar().repaint();
        //comp.getScrollBar().setVisibleAmount(visibleAmount+this.item_cell_spacing-40/model.size());
    }
    /**
     * 粗略计算一个块的逻辑尺寸
     * @param item
     * @return
     */
    private int calcItemBlockSize(OGranularItem item){
        return item.getLines()*17+4;
    }

    //事件处理相关
    public void procMouseWheelMoved(MouseWheelEvent e, OGranularList comp){
        int adjustamount = 0;
        boolean isadd = false;
        int notches = e.getWheelRotation();
        if (notches < 0)
            isadd = false;
        else
            isadd = true;

        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
              adjustamount = e.getScrollAmount();
        } else { // scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
            adjustamount = e.getScrollAmount();
        }
        int value = comp.getScrollBar().getValue();
        if(isadd) value += adjustamount;
        else value -= adjustamount;
        comp.getScrollBar().setValue(value);
    }

    public void procMouseClicked(MouseEvent e, OGranularList comp){
        model = comp.getModel();
        int pos = e.getY() + m_cpos; //scroll bar.
        procMouseClicked(pos, comp);
        if(e.getClickCount()>=2){
            comp.fireSelectionChanged(true);
        }
        
    }

    /**
     * 处理鼠标按下的事件
     * @param pos
     */

    protected void procMouseClicked(int pos, OGranularList comp){
        OGranularItem item = guessSelectionByPos(pos, comp);
        if(item!=null){
            makeSelection(item, -1, comp);
        }
    }

    /**
     * 根据Mouse位置来猜测一个位置对应的OListItem
     * @param pos
     * @return
     */
    private OGranularItem guessSelectionByPos(int pos, OGranularList comp){
        model = comp.getModel();
        OGranularItem ret = null;
        Iterator<OGranularItem> iter = model.iter();
        while(iter.hasNext()){
            OGranularItem item = iter.next();
            int start = positions.get(item);
            if(pos>start && pos<start+this.calcItemBlockSize(item)){
                ret = item;
                procSubSelection(ret, pos-start);
                break;
            }
        }
        return ret;
    }
    /**
     * 选定一个特定的子项目
     * @param item
     * @param margin
     */
    private void procSubSelection(OGranularItem item, int margin){
        int diff = margin - 20;
        if(diff<=0) {
            item.setSelectedindex(1);
            return;
        } //不能选择标题
        int index = diff/17;
        if(index*17>diff)
            index++;
        item.setSelectedindex(index+1);
    }

    /**
     * Make a selection
     * @param e
     */
    protected void makeSelection(OGranularItem item, int cellIndex, OGranularList comp){
        model = comp.getModel();
        if(model.getSelectedIndex()!=-1){
            model.get(model.getSelectedIndex()).setSelected(false);
        }
        model.setSelectedIndex(model.indexOf(item));
        item.setSelected(true);
        if(cellIndex>0)
            item.setSelectedindex(cellIndex);

        comp.fireSelectionChanged(false);
        comp.repaint();
    }

    //元素或者子元素导航函数区
     public void pageNext(OGranularList comp){
        int value = comp.getScrollBar().getValue();
        value+= comp.getHeight();
        comp.getScrollBar().setValue(value);
        makeSelectionFirstInView(comp);
    }
    public void pagePrevious(OGranularList comp){
        int value = comp.getScrollBar().getValue();
        value-= comp.getHeight();
        comp.getScrollBar().setValue(value);
        makeSelectionFirstInView(comp);
    }
    /**
     * 选择下一个。
     */
    public void selectPrevious(OGranularList comp){
        model = comp.getModel();
        if(model.getSelectedIndex()==-1){ //noselection.
            makeSelectionFirstInView(comp);
            return;
        }
        OGranularItem item = model.get(model.getSelectedIndex());
        if(item.hasPrevious()){
            item.previous();
        }else{
           if(model.getSelectedIndex()>0){
                model.setSelectedIndex(model.getSelectedIndex()-1);
                item.setSelected(false);
                (item  = model.get(model.getSelectedIndex())).setSelected(true);
                item.setSelectedindex(item.getLines()-1);
           }else{ //没有可以移动的内容
               return;
           }
        }
        //

        //调整滚动使得当前选中的内容可见
        adjustView(item, false, comp);
        comp.fireSelectionChanged(false);
    }
    public void selectNext(OGranularList comp){
        model = comp.getModel();
        if(model.getSelectedIndex()==-1){ //noselection.
            makeSelectionFirstInView(comp);
            return;
        }
        OGranularItem item = model.get(model.getSelectedIndex());
        if(item.hasNext()){
            item.next();
        }else{
           if(model.getSelectedIndex()<model.size()-1){
                model.setSelectedIndex(model.getSelectedIndex()+1);
                item.setSelected(false);
                (item  = model.get(model.getSelectedIndex())).setSelected(true);
                item.setSelectedindex(1);
           }else{ //没有可以移动的内容
               return;
           }
        }

        //调整滚动使得当前选中的内容可见
        adjustView(item, true, comp);
        comp.fireSelectionChanged(false);
    }
    /**
     * 调整视图使得选中的部分可见
     * @param item
     */
    private void adjustView(OGranularItem item, boolean next, OGranularList comp){
        //Rectangle rcView = new Rectangle(1, m_cpos, this.getWidth(), this.getHeight() );
        model = comp.getModel();
        int cpos = m_cpos;
        int selectedcellindex = item.getSelectedindex();
        int height =  selectedcellindex*17+10;
        int offsety = positions.get(item);
        //确保显示Item的头。
        if(next){
            if(offsety>cpos+comp.getHeight()) {
                comp.getScrollBar().setValue(offsety);
            }else{
                offsety+=height;
                if(offsety>cpos+comp.getHeight()){
                    comp.getScrollBar().setValue(positions.get(item));
                }
            }
        }else{
            if(offsety<cpos)
                comp.getScrollBar().setValue(offsety);
        }
    }

    /**
     * 选中可视区域中第一区域中的第一个Item
     */
    public void makeSelectionFirstInView(OGranularList comp){
        model = comp.getModel();

        if(positions==null || positions.isEmpty()) return;
        Rectangle rcView = new Rectangle(0, m_cpos,
                comp.getWidth(), comp.getHeight() );
        Iterator<OGranularItem> iter = model.iter();
        while(iter.hasNext()){
            OGranularItem item = iter.next();
            //System.out.println(positions);
            if(!positions.containsKey(item)) continue;
            int offsety = positions.get(item);
            int blkHeight = this.calcItemBlockSize(item);
            Rectangle rcBlk = new Rectangle (10, offsety, 20, blkHeight-4);
            //如果不在视窗内，不进行绘制
            if(!rcView.contains(rcBlk)) continue;
            this.makeSelection(item, 1, comp);
            break;
        }
    }
}
