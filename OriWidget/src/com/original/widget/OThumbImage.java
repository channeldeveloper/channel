/*
 *  com.original.widget.OThumbImage.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.ByteBlockModel;
import com.original.widget.plaf.OThumbImageUI;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 * 缩微图，该类只是一个图片缩放呈现而已
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 6, 2012 4:41:04 PM
 */
public class OThumbImage extends JPanel  {
    private static final String uiClassID = "OThumbImageUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OThumbImageUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private ByteBlockModel model;
    private DrawAreaChangeListener listener;

    
    /**
     * constructor.
     */
    public OThumbImage(){
        init();
    }
    /**
     * initialize some event handlers.
     */
    private void init(){
        setModel(new ByteBlockModel());
		setOpaque(false);
		this.setSize(122, 138);
        this.setMinimumSize(new Dimension(122, 138));
        setBorder(null);        
    }

     //模型设置部分
    public void setModel(ByteBlockModel model) {
        this.model = model;
    }
     public ByteBlockModel getModel() {
        return model;
    }

    /**
     * 设置数据
     * @param sFormat
     * @param data
     */
    public void setSrc(String sFormat, byte[] data){
        this.model.setHasdata(true);
        this.model.setDataformat(sFormat);
        this.model.setDatablock(data);
        this.repaint();
    }
    
    //设置UI
    public void setUI(OThumbImageUI ui) {
		super.setUI(ui);
	}

    @Override
    public OThumbImageUI getUI() {
		return (OThumbImageUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}
    
    
}
