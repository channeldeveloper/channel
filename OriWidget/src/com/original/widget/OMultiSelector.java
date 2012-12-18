/*
 *  com.original.widget.OMultiSelector.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.ChoiceSelectionEvent;
import com.original.widget.event.ChoiceSelectionListener;
import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.MultiSelectorModel;
import com.original.widget.plaf.OMultiSelectorUI;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
/**
 * (Class Annotation.)
 * 多项选择器 组件
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-2 23:34:54
 */
public class OMultiSelector extends JPanel implements MouseListener,
        MouseMotionListener, CustomDrawable {
    private static final String uiClassID = "OMultiSelectorUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OMultiSelectorUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	private MultiSelectorModel model;
    private DrawAreaChangeListener listener;
    
    
    /**
     * constructor.
     */
    public OMultiSelector(){
        init(new MultiSelectorModel(MultiSelectorModel.SELECTORTYPE.SIMPLE));
    }
    public OMultiSelector(MultiSelectorModel model){
       init(model);
    }
    /**
     * initialize some event handlers.
     */
    private void init(MultiSelectorModel model){
        setModel(model);
		setOpaque(false);
		this.setSize(204, 216);
        this.setMinimumSize(new Dimension(204, 216));
        setBorder(null);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        listener  = new DrawAreaChangeListener(this);
		this.model.addChangeListener(listener);
    }

     //模型设置部分
    public void setModel(MultiSelectorModel model) {
        this.model = model;
    }
     public MultiSelectorModel getModel() {
        return model;
    }


    //设置UI
    public void setUI(OMultiSelectorUI ui) {
		super.setUI(ui);
	}

    @Override
    public OMultiSelectorUI getUI() {
		return (OMultiSelectorUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}



    /** mouse event processing **/
    public void mouseClicked(MouseEvent e) {
        Point pt = e.getPoint();
        getUI().procMouseClicked(e);
        
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
        this.repaint();
    }


    public void mouseExited(MouseEvent e) {
        

    }

    /** mouse move event processing **/
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void forceCustomDraw() {
        repaint();
    }


    List<ChoiceSelectionListener> selectListenerList = new ArrayList<ChoiceSelectionListener>();

     public void addSelectionListener(ChoiceSelectionListener l) {
        selectListenerList.add(l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(ChoiceSelectionListener l) {
        selectListenerList.remove( l);
    }
    public void fireSelectionChanged(int selectIndex, Object data) {

        Iterator<ChoiceSelectionListener> iter = selectListenerList.iterator();
        while(iter.hasNext()){
            iter.next().choiceSelectChange(
                    new ChoiceSelectionEvent(this, selectIndex,data)
                    );
        }
    }

    public void setSelectedIndex(int index){
        this.model.setSelectedIndex(index);
        repaint();
    }

}
