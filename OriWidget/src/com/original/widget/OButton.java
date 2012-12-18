/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget;

import com.original.widget.draw.OriCharacter;
import com.original.widget.model.LevelButtonModel;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;
import com.original.widget.plaf.OButtonUI;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Changjian
 */
public class OButton extends JButton implements ChangeListener  {

    private static final String uiClassID = "OButtonUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OButtonUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
    private LevelButtonModel cmodel = new LevelButtonModel();
	

    public OButton(String text, Icon icon){
        super(text, icon);
        init();
        
    }
    private void init(){
        setModel(cmodel);
        this.setBorderPainted(false);
        this.setOpaque(false);
        cmodel.addChangeListener(this);
        setLevel(BUTTONLEVEL.SYSTEM);
    }
    public OButton(String text){
        this(text, null);
    }
    public OButton(Icon icon){
        this(null, icon);
    }
    public OButton(){
        this(null, null);
    }
    //设置按钮的层级
    public void setLevel(BUTTONLEVEL level){
        cmodel.setLevel(level);
        this.setFont(cmodel.getButtonTextFont(!OriCharacter.containChinese(this.getText())));
        this.setForeground(cmodel.getButtonTextColor());
    }

    @Override
    public LevelButtonModel getModel(){
        return cmodel;
    }
     
    public void setUI(OButtonUI ui) {
		super.setUI(ui);
	}

    @Override
    public OButtonUI getUI() {
		return (OButtonUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}

    @Override
    public void stateChanged(ChangeEvent e) {
        this.repaint();
    }



}
