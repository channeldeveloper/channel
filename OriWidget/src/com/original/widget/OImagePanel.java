/*
 *  com.original.widget.OImagePanel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.ImagePanelModel;
import com.original.widget.plaf.OImagePanelUI;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:09:33
 */
public class OImagePanel extends JPanel implements CustomDrawable {

    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "OImagePanelUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OImagePanelUI");
    }
    /**
     *
     */
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private ImagePanelModel model;
    private DrawAreaChangeListener listener;

    /**
     *
     * @param _title
     * @param _data
     * @param _image
     */
    public OImagePanel(String _title, Object _data, Image _image) {
        this(_title, _data, 120, 90, _image);
    }

    /**
     * 
     * @param _title
     * @param _data
     * @param width
     * @param height
     * @param _image
     */
    public OImagePanel(String _title, Object _data, int width, int height, Image _image) {
        //model
        setModel(new ImagePanelModel(_title, _data, width, height, _image));
        //size
        this.setPanelSize(width, height);
        setLayout(null);
        //mode property listner
        listener = new DrawAreaChangeListener(this);
        this.model.addChangeListener(listener);
        init();
    }

    private void init() {
        this.model.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        this.setLayout(null);
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
        this.setOpaque(false);
    }

    public Object getUserData() {
        return model.getData();
    }

    public void setUserData(Object o) {
        model.setData(o);
    }

    // //////////////////UI机制///////////////////////
    /**
     * Returns the look and feel (L&F) object that renders this component.
     *
     * @return the PanelUI object that renders this component
     * @since 1.4
     */
    @Override
    public OImagePanelUI getUI() {
        return (OImagePanelUI) ui;
    }

    /**
     * Sets the look and feel (L&F) object that renders this component.
     *
     * @param ui
     *            the PanelUI L&F object
     * @see UIDefaults#getUI
     * @since 1.4
     * @beaninfo bound: true hidden: true attribute: visualUpdate true
     *           description: The UI object that implements the Component's
     *           LookAndFeel.
     */
    public void setUI(OImagePanelUI ui) {
        super.setUI(ui);
    }

    /**
     * Returns a string that specifies the name of the L&F class that renders
     * this component.
     *
     * @return "PanelUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     * @beaninfo expert: true description: A string that specifies the name of
     *           the L&F class.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    public void setBackgroundColor(Color color) {
        model.setBackgroundcolor(color);
    }

    // //////////////////UI机制///////////////////////
    // Model functions begin
    /**
     * setPanelSize(int width, int height) 设置面板尺寸（宽度、高度），整数版本
     *
     * @param width
     * @param height
     */
    public void setPanelSize(int width, int height) {
        super.setSize(width, height);

        size.setSize(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);

        model.setHeight(height);
        model.setWidth(width);

        this.repaint();
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        getModel().setTitle(title);
    }

    /**
     * @param _orientation
     */
    public void setOrientation(String _orientation) {
        getModel().setOrientation(_orientation);
    }

    /**
     * @param _data
     */
    public void setData(Object _data) {
        getModel().setData(_data);
    }

    /**
     * @param model the model to set
     */
    public void setModel(ImagePanelModel model) {
        this.model = model;
    }

    /**
     * @return the model
     */
    public ImagePanelModel getModel() {
        return model;
    }

    @Override
    public void forceCustomDraw() {
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());

        getUI().redraw();
        repaint();
    }

    public boolean isSelected() {
        return model.isSelected();
    }

    public void setSelected(boolean flag) {
        model.setSelected(flag);
    }
}
