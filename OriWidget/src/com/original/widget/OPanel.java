/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.PanelModel;
import com.original.widget.plaf.OPanelUI;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 14:01:07
 */
public class OPanel extends JPanel implements ComponentListener,
        CustomDrawable {

    private static final String uiClassID = "OPanelUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OPanelUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private PanelModel model;
    private DrawAreaChangeListener listener;

    public OPanel(){
        int width = 0;
        int height = 0;
        setModel(new PanelModel(width, height));
        setOpaque(false);
        this.setSize(width, height);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        //mode property listner
        listener = new DrawAreaChangeListener(this);
        this.model.addChangeListener(listener);
        addComponentListener(this);
        initComp();
    }
    //初始化对象
    public OPanel(int width, int height) {
        //model
        setModel(new PanelModel(width, height));
        setOpaque(false);
        this.setSize(width, height);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        //mode property listner
        listener = new DrawAreaChangeListener(this);
        this.model.addChangeListener(listener);
        addComponentListener(this);
        initComp();
    }

    private void initComp() {

        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
    }

    //模型设置部分
    public void setModel(PanelModel model) {
        this.model = model;
    }

    public PanelModel getModel() {
        return model;
    }

    //检测程序大小
    @Override
    public void componentResized(ComponentEvent e) {
        resize();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    public void resize() {
        int width = this.getWidth();
        int height = this.getHeight();
        this.model.setWidth(width);
        this.model.setHeight(height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        resize();
    }
    //设置UI

    public void setUI(OPanelUI ui) {
        super.setUI(ui);
    }

    @Override
    public OPanelUI getUI() {
        return (OPanelUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    public void setImageName(String _imagename) {
        model.setImageName(_imagename);
    }

    public void setArcw(double _arcw) {
        model.setArcw(_arcw);
    }

    public void setArch(double _arch) {
        model.setArch(_arch);
    }

    //强制绘制-当变化的时候
    @Override
    public void forceCustomDraw() {
        getUI().redraw();
        repaint();
    }
}
