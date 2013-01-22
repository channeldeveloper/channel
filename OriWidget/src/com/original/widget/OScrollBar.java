package com.original.widget;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;

import com.original.widget.event.ScrollChangeListener;
import com.original.widget.model.ScrollBarModel;
import com.original.widget.plaf.OScrollBarUI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ChangjianHu
 */
public class OScrollBar extends JScrollBar {
    private static final String uiClassID = "OScrollBarUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OScrollBarUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private ScrollBarModel model;
    private ScrollChangeListener additionListener;
	
    //初始化对象
    public OScrollBar(int orientation, Color trackColor) {
        super(orientation);
        if(model==null)
        model = new ScrollBarModel();
        //this.setUI(new OScrollBarUI());
//        model.setTrackColor(trackColor);
        model.setBarcolor(trackColor);
        //
        setOpaque(false);
		additionListener = new ScrollChangeListener(this);
		this.model.addChangeListener(additionListener);
        super.setOrientation(orientation);
	}

    public void addChangeListern(ChangeListener listener){
        this.model.addChangeListener(listener);
    }

    //模型设置部分
    public void setModel(ScrollBarModel model) {
        this.model = model;
    }

    @Override
    public ScrollBarModel getModel() {
        if(model==null)
            model = new ScrollBarModel();
        return model;
    }
    public void setUI(OScrollBarUI ui) {
		super.setUI(ui);
	}

    @Override
    public OScrollBarUI getUI() {
		return (OScrollBarUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}
    

    
    //public OScrollBar(int orientation){
    //    super(orientation);
    //    this.setUI(new OriScrollBarUI());
    //}
    public void fireAdjustmentValueChanged(int id, int type, int value,
                         boolean isAdjusting){
        Object  [] listeners = listenerList.getListenerList();
         AdjustmentEvent   e = null;
         for (int i = listeners.length - 2; i >= 0; i -= 2) {
             if (listeners[i]==AdjustmentListener.class) {
                 if (e == null) {
                     e = new AdjustmentEvent  (this, id, type, value, isAdjusting);
                 }
                ((AdjustmentListener  )listeners[i+1]).adjustmentValueChanged(e);
             }
        }

    }
}
