/*
 *  com.original.widget.OObjEditor.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.CustomItemEvent;
import com.original.widget.event.CustomItemSelectListener;
import com.original.widget.model.CustomdrawnListModel;
import com.original.widget.model.CustomdrawnListModel.OCustomDrawListItem;
import com.original.widget.plaf.OCustomdrawnListUI;
import com.original.widget.plaf.OGranularListUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;



import java.awt.Dimension;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JScrollBar;
/**
 * (Class Annotation.)
 *  自绘制列表-但粒度
 *   v1, 只实现基本的功能，以后可以根据这个版本进行修改
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   Nov 24, 2012 20:44:58 PM
 */
public class OCustomdrawnList extends JPanel implements 
        AdjustmentListener, MouseWheelListener, MouseListener {
    private static final String uiClassID = "OCustomdrawnListUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OCustomdrawnListUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private CustomdrawnListModel model;
    //private DrawAreaChangeListener listener;
    private OScrollBar verticalbar;
    List<CustomItemSelectListener> selectListenerList = new ArrayList<CustomItemSelectListener>();

    /**
     * constructor.
     */
    public OCustomdrawnList(){
        init();
    }
    
    /**
     * initialize some event handlers.
     */
    private void init(){
        setModel(new CustomdrawnListModel());
		setOpaque(false);
		setBorder(null);
        //mode property listner
		
        this.setLayout(new BorderLayout());
        //创建一个滚动条
        verticalbar=new OScrollBar(
                JScrollBar.VERTICAL, Color.WHITE);
        add(verticalbar, BorderLayout.EAST);
        verticalbar.addAdjustmentListener(this);

        //this data will be updated soon.
        verticalbar.setMaximum(100);
        verticalbar.setMinimum(0);
        verticalbar.setValue(10);

        this.addMouseListener(this);
        addMouseWheelListener(this);
        
    }
    
     //模型设置部分
    public void setModel(CustomdrawnListModel model) {
        this.model = model;
    }
     public CustomdrawnListModel getModel() {
        return model;
    }
    
    //增加Item 
    public void addItem(OCustomDrawListItem item){
        model.add(item);
    }
    //批量增加
    public void addItemList(List<OCustomDrawListItem> items){
        Iterator<OCustomDrawListItem> iter = items.iterator();
        while(iter.hasNext())
            addItem(iter.next());

    }
    public OCustomDrawListItem createBlankItem(){
        return model.createBlankItem();
    }

    /**
     * {@inheritDoc}
     */
    public void addSelectionListener(CustomItemSelectListener l) {
        selectListenerList.add(l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(CustomItemSelectListener l) {
        selectListenerList.remove(l);
    }
    public void fireSelectionChanged(OCustomDrawListItem item) {

        Iterator<CustomItemSelectListener> iter = selectListenerList.iterator();
        while(iter.hasNext()){
            iter.next().itemSelectChange(
                    new CustomItemEvent(item, item.getAnchor()));
                    
        }
    }
   
    @Override
    public boolean isFocusTraversable(){
        return true;
    }


    
    public OScrollBar getScrollBar(){
        return this.verticalbar;
    }
    
    //设置UI
    public void setUI(OGranularListUI ui) {
		super.setUI(ui);
	}
    
    @Override
    public OCustomdrawnListUI getUI() {
		return (OCustomdrawnListUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}
    

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        getUI().resetScrollPos(e.getValue(), this);
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        getUI().procMouseWheelMoved(e, this);
        this.requestFocus();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        getUI().procMouseClicked(e, this);
        this.requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clearData(){
        model.clear();
        getUI().clearMemory();
    }
    public void clear(){
        model.clear();
        getUI().clearMemory();
        repaint();
    }

   
}
