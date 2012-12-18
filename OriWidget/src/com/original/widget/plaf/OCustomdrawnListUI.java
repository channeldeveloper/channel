/*
 *  com.original.widget.plaf.OGranularListUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OCustomdrawnList;
import com.original.widget.model.CustomdrawnListModel;
import com.original.widget.model.CustomdrawnListModel.OCustomDrawListItem;
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
 *  单粒度列表绘制类
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   Nov. 24, 2012 20:47 PM
 */
public class OCustomdrawnListUI extends BasicPanelUI {
    //边框预留额度
    private int bordersize = 1;
    //不同块之间间隔
    private int item_cell_spacing = 1;
    //滚动控制标志
    private int m_cpos = 0;
    //对块数据进行临时记录便于规划
    private Map<OCustomDrawListItem, Integer> positions = new HashMap<OCustomDrawListItem, Integer>();
    
    //private static OGranularListUI cui;
    private OCustomdrawnList comp;
    private CustomdrawnListModel model;

    public static ComponentUI createUI(JComponent c) {
        return new OCustomdrawnListUI(c);
    }

    public OCustomdrawnListUI(JComponent com) {
        comp = (OCustomdrawnList)com;
    	//model = comp.getModel();
    }
    public void resetScrollPos(int pos, OCustomdrawnList comp){
        m_cpos = pos;
    }
    public void clearMemory(){
        positions.clear();
        
    }
  

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
    private void drawItems(Graphics g, OCustomdrawnList comp, CustomdrawnListModel model) {
        Graphics2D g2d = (Graphics2D)g;
        //避免擦除边框
        g2d.translate(1,1-m_cpos);
        //System.out.println(m_cpos);
        int offsety = bordersize * 2+20;
        Iterator<OCustomDrawListItem> iter = model.iter();
        while(iter.hasNext()){
            OCustomDrawListItem item = iter.next();
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
    private int drawItemBlock(Graphics2D g, OCustomDrawListItem item, int offsety,
            OCustomdrawnList comp, CustomdrawnListModel model){
        int blkHeight = calcItemBlockSize(item);

        int blkWidth = comp.getWidth() - bordersize *2;
        //检查是否Visible
        Rectangle rcView = new Rectangle(0, m_cpos, comp.getWidth(), comp.getHeight() );
        Rectangle rcBlk = new Rectangle (10, offsety, 20, blkHeight);
        //如果不在视窗内，不进行绘制
        if(!rcView.intersects(rcBlk)) return offsety+blkHeight;
        BufferedImage cavans = new BufferedImage(blkWidth,blkHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) cavans.getGraphics();
        g2d.setBackground(item.getBackgroundColor());
        g2d.clearRect(0,0, blkWidth, blkHeight);

        float offset_x = 8;
        float offset_y = 10;
        int _icon_width = 36;

        g2d.drawImage(item.getImg(), (int)offset_x, (int)offset_y, comp);

        //draw name.
        g2d.setFont(item.getTitleFont());
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int hgt = metrics.getHeight();
        //int adv = metrics.stringWidth(item.getTitle());
        offset_x +=(_icon_width+6);
        offset_y +=hgt*0.5f;
        g2d.setPaint(item.getDrawColor());
        
        g2d.drawString(item.getTitle(), offset_x, offset_y);

        //others
        g2d.setFont(item.getAdditionFont());
        offset_y +=hgt*1.1f;
        g2d.drawString(item.getAddition(), offset_x, offset_y);
        
        g.drawImage(cavans, bordersize+1, offsety, null);
        cavans = null;
        return offsety+blkHeight;

    }

    /**
     * 调整滚动条状况
     */
    private void adjustScrollBar(OCustomdrawnList comp, CustomdrawnListModel model){
        int height = 40;
        Iterator<OCustomDrawListItem> iter = model.iter();
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
    private int calcItemBlockSize(OCustomDrawListItem item){
        return 50;
    }

    //事件处理相关
    public void procMouseWheelMoved(MouseWheelEvent e, OCustomdrawnList comp){
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

    public void procMouseClicked(MouseEvent e, OCustomdrawnList comp){
        model = comp.getModel();
        int pos = e.getY() + m_cpos; //scroll bar.
        OCustomDrawListItem item = guessSelectionByPos(pos, comp);
        if(item!=null){
            //makeSelection(item, -1, comp);
            comp.fireSelectionChanged(item);
            //System.out.println(item.getTitle());
        }        
    }

   
    /**
     * 根据Mouse位置来猜测一个位置对应的OListItem
     * @param pos
     * @return
     */
    private OCustomDrawListItem guessSelectionByPos(int pos, OCustomdrawnList comp){
        model = comp.getModel();
        OCustomDrawListItem ret = null;
        Iterator<OCustomDrawListItem> iter = model.iter();
        while(iter.hasNext()){
            OCustomDrawListItem item = iter.next();
            int start = positions.get(item);
            if(pos>start && pos<start+this.calcItemBlockSize(item)){
                ret = item;
                break;
            }
        }
        return ret;
    }
   

    
}
