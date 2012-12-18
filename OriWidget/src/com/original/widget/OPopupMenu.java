/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.PopupMenuModel;
import com.original.widget.plaf.OPopupMenuUI;
import com.original.widget.plaf.OTextFieldUI;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * PopuMenu
 * @author Hu ChangJian
 */
public class OPopupMenu extends JPopupMenu implements ComponentListener,
        CustomDrawable{
    private static final String uiClassID = "OPopupMenuUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OPopupMenuUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
    private DrawAreaChangeListener listener;
    private PopupMenuModel model;

    public OPopupMenu() {
       super(null);
       initComp(200,200);
    }
    public OPopupMenu(int width, int height) {
        initComp(width, height);
    }
    private void initComp(int width, int height){

        setModel(new PopupMenuModel());
        setOpaque(false);
        setSize(width, height);
        //this.setLightWeightPopupEnabled(false);
        
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        listener  = new DrawAreaChangeListener(this);
	//this.model.addChangeListener(listener);
        addComponentListener(this);
        //this.setFont(model.getFont());
        //this.setForeground(model.getForecolor());
        //this.setBackground(model.getBackgroundcolor());
    }
    //模型设置部分
    public void setModel(PopupMenuModel model) {
        this.model = model;
    }
     public PopupMenuModel getModel() {
        return model;
    }

    
    //检测程序大小
    @Override
    public void componentResized(ComponentEvent e) {
        resize();
    }
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
	public void componentHidden(ComponentEvent e) {}
    @Override
	public void componentMoved(ComponentEvent e) {}
    @Override
    public boolean isFocusTraversable(){
        return true;
    }
    public void resize(){
        int width = this.getWidth();
        int height = this.getHeight();
        //this.model.setWidth(width);
        //this.model.setHeight(height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        resize();
    }
    //设置UI
    public void setUI(OTextFieldUI ui) {
		super.setUI(ui);
	}

    @Override
    public OPopupMenuUI getUI() {
		return (OPopupMenuUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}

   
    //强制绘制-当变化的时候
    @Override
    public void forceCustomDraw() {
        //getUI().redraw(this);
        repaint();
    }
}
