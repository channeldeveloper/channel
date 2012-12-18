/*
 *  com.original.widget.OObjEditor.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.event.GranularListSelectionEvent;
import com.original.widget.event.GranularListSelectionListener;
import com.original.widget.model.GranularListModel;
import com.original.widget.model.GranularListModel.OGranularItem;
import com.original.widget.plaf.OGranularListUI;
import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;



import java.awt.Dimension;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollBar;
/**
 * (Class Annotation.)
 *  自绘制列表（多粒度）
 *   v1, 只实现基本的功能，以后可以根据这个版本进行修改
 *   v2. 去除不必要的内容，实现基本功能
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 6, 2012 11:23:58 PM
 */
public class OGranularList extends JPanel implements 
        AdjustmentListener, MouseWheelListener, MouseListener, KeyListener {
    private static final String uiClassID = "OGranularListUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OGranularListUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private GranularListModel model;
    private DrawAreaChangeListener listener;
    private OScrollBar verticalbar;
    List<GranularListSelectionListener> selectListenerList = new ArrayList<GranularListSelectionListener>();

    /**
     * constructor.
     */
    public OGranularList(){
        init();
    }
    
    /**
     * initialize some event handlers.
     */
    private void init(){
        setModel(new GranularListModel());
		setOpaque(false);
		setBorder(null);
        //mode property listner
		
        this.setLayout(new BorderLayout());
        //创建一个滚动条
        verticalbar=new OScrollBar(
                JScrollBar.VERTICAL, model.getBackgroundcolor());
        add(verticalbar, BorderLayout.EAST);
        verticalbar.addAdjustmentListener(this);

        //this data will be updated soon.
        verticalbar.setMaximum(100);
        verticalbar.setMinimum(0);
        verticalbar.setValue(10);

        model.setSelectedIndex(-1);
        this.addMouseListener(this);
        this.addKeyListener(this);
        addMouseWheelListener(this);
        
    }
    
     //模型设置部分
    public void setModel(GranularListModel model) {
        this.model = model;
    }
     public GranularListModel getModel() {
        return model;
    }
    
    //API AREA
    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }
    public int getSelectedSubIndex(){
        return model.getSelectedSubIndex();
    }
    //增加Item 
    public void addItem(OGranularItem item){
        if(item.getLines()>1)
        model.add(item);
    }
    //批量增加
    public void addItemList(List<OGranularItem> items){
        Iterator<OGranularItem> iter = items.iterator();
        while(iter.hasNext())
            addItem(iter.next());

    }
    public OGranularItem createBlankItem(){
        return model.createBlankItem();
    }

    /**
     * {@inheritDoc}
     */
    public void addSelectionListener(GranularListSelectionListener l) {
        selectListenerList.add(l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(GranularListSelectionListener l) {
        selectListenerList.remove( l);
    }
    public void fireSelectionChanged(int selectIndex, int subSelectIndex, boolean
            isAdjusting) {

        Iterator<GranularListSelectionListener> iter = selectListenerList.iterator();
        while(iter.hasNext()){
            iter.next().valueChanged(
                    new GranularListSelectionEvent(this, selectIndex,
                    subSelectIndex, isAdjusting)
                    );
        }
    }
    public void fireSelectionChanged(boolean isAdjusting){
        fireSelectionChanged(getSelectedIndex(),
                this.getSelectedSubIndex(), isAdjusting);
    }
    //API AREA

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
    public OGranularListUI getUI() {
		return (OGranularListUI) ui;
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

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP){
            getUI().selectPrevious(this);
        }else if( e.getKeyCode()==KeyEvent.VK_DOWN ){
            getUI().selectNext(this);
        }else if(e.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            getUI().pageNext(this);
        }else if(e.getKeyCode()==KeyEvent.VK_PAGE_UP){
            getUI().pagePrevious(this);
        }else if(e.getKeyCode()==KeyEvent.VK_ENTER){
            if(this.getSelectedIndex()!=-1)
                this.fireSelectionChanged(true);
        }
        repaint();
    }

    //exposure some APIs for others to invoke.
    //暴露一些接口供调用，主要是翻页，上下处理。
    //0-up, 1-down, 2-page down, 3- pageup.
    public void navigate(int type){
        switch(type){
            case 0:
                getUI().selectPrevious(this);
                break;
            case 1:
                getUI().selectNext(this);
                break;
            case 2:
                getUI().pageNext(this);
                break;
            case 3:
                getUI().pagePrevious(this);
                break;
        }
        repaint();
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

    public void selectFirst(){
        model.setFirstSelected();
    }

    public String makeSelection(){
        if(this.getSelectedIndex()==-1) return "";
        return model.getSel();
    }

    public Map<String, Object> makeObjSelection(){
        Map<String, Object> ret = new HashMap<String, Object>();
        if(this.getSelectedIndex()==-1) return ret;

        model.getObjSel(ret);
        return ret;
    }
}
