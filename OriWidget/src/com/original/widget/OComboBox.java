/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.model.CustomComboBoxModel;
import com.original.widget.plaf.OComboBoxUI;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 14:01:07
 */
public class OComboBox extends JComboBox {
     private static final String uiClassID = "OComboBoxUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OComboBoxUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
    private CustomComboBoxModel model;
    
     public OComboBox(CustomComboBoxModel.COMBOBOXTYPE type){
         super();
         initComp(type);
     }
     public OComboBox(Object[] itemArray){
       this(itemArray, null);
    }
     public OComboBox(Object[] itemArray, CustomComboBoxModel.COMBOBOXTYPE type){
        super(itemArray);
        initComp(type);
     }
    private void initComp(CustomComboBoxModel.COMBOBOXTYPE type){
        if(type==null)
            type = CustomComboBoxModel.COMBOBOXTYPE.SWITCH;
        setVizModel(new CustomComboBoxModel(type));
        this.setEditable(false);
        setOpaque(false);
        if(type==CustomComboBoxModel.COMBOBOXTYPE.INPUT){
            setBorder(BorderFactory.createEmptyBorder(4, 10, 4,4));
            this.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        }
        else if(type==CustomComboBoxModel.COMBOBOXTYPE.SWITCH){
            setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 4));
            this.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        }
    }
    //模型设置部分
    public void setVizModel(CustomComboBoxModel model) {
        this.model = model;
    }
     public CustomComboBoxModel getVizModel() {
        return model;
    }

    
    //设置UI
    public void setUI(OComboBoxUI ui) {
            super.setUI(ui);
    }

    @Override
    public OComboBoxUI getUI() {
            return (OComboBoxUI) ui;
    }

    @Override
    public String getUIClassID() {
            return uiClassID;
    }

}