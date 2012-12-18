/*
 *  com.original.widget.OImagePanel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DragListener;
import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.event.OCallBack;
import com.original.widget.model.GroupPanelModel;
import com.original.widget.plaf.OGroupPanelUI;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
public class OGroupPanel extends JPanel implements MouseListener, CustomDrawable,OCallBack {

    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "OGroupPanelUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OGroupPanelUI");
    }
    /**
     *
     */
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private GroupPanelModel model;
    private DrawAreaChangeListener listener;
    private JPanel imagePanes;
    private JScrollPane scrollPane;
    private JLabel rightBtn;
    private DragListener dndListener = null;
    private DragSource dragSource = null;
    private MouseListener mouseListener = null;
    private OScrollBar oscrollbar = null;
    private OCheckBox checkbox = new OCheckBox();
    private JPanel path = null;
//    private OButton savebtn = new OButton("保存");
//    private OPathSelector selector =null;

    public OGroupPanel(int width, int height) {
        this(width, height, 0, false, false);
    }

    /**
     * 
     * @param width
     * @param height
     * @param rightoffset 
     * @param _isHorizontal
     * @param _isDrop
     */
    public OGroupPanel(int width, int height, int rightoffset, boolean _isHorizontal, boolean _isDrop) {
        //model
        setModel(new GroupPanelModel(width, height, rightoffset, _isHorizontal, _isDrop));
        //size
        this.model.setBackgroundcolor(Color.white);
        this.setPanelSize(width, height);
        //mode property listner
        listener = new DrawAreaChangeListener(this);
        this.model.addChangeListener(listener);
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(212, 212, 212)));
        imagePanes = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14)) {

            @Override
            public Component add(Component c) {
                super.add(c);
                if (c instanceof OImagePanel) {
                    setImagePanelSize();
                }
                return c;
            }

            @Override
            public void remove(Component c) {
                super.remove(c);
                if (c instanceof OImagePanel) {
                    setImagePanelSize();
                }
            }
        };
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
        scrollPane = new JScrollPane(imagePanes);
//        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
//        scrollPane.setPreferredSize(new Dimension(model.getWidth() - 20 - model.getRightOffset(), model.getHeight()));
//        imagePanes.setPreferredSize(new Dimension(model.getWidth() - 20 - model.getRightOffset(), model.getHeight() - 14));
//        scrollPane.setPreferredSize(new Dimension(model.getWidth() - model.getRightOffset(), model.getHeight()-60));
//        imagePanes.setPreferredSize(new Dimension(model.getWidth() - model.getRightOffset(), model.getHeight() - 74));

        path = new JPanel(new GridBagLayout());
        path.setOpaque(false);
//        selector = new OPathSelector();
//        selector.setCaller(this);
//        selector.setMinimumSize(new Dimension(500, 40));
//        selector.setPreferredSize(new Dimension(500, 40));
//        path.add(selector, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
//                GridBagConstraints.BOTH,
//                new Insets(0, 0, 0, 10), 0, 0));
//        savebtn.setLevel(BUTTONLEVEL.APPLICATION);
//        savebtn.setActionCommand("save");
//        savebtn.setMinimumSize(new Dimension(70, 36));
//        savebtn.setPreferredSize(new Dimension(70, 36));
//        path.add(savebtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
//                GridBagConstraints.NONE,
//                new Insets(0, 0, 0, 10), 0, 0));
        this.add(path, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 5, 0, 0), 0, 0));
        path.setVisible(false);
//        this.add(checkbox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
//                GridBagConstraints.WEST, GridBagConstraints.NONE,
//                new Insets(0, 0, 0, 0), 0, 0));

        if (model.isHorizontal()) {
//            imagePanes.setBackground(model.getNotSelectedColor());
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            oscrollbar = new OScrollBar(Adjustable.HORIZONTAL, model.getNotSelectedColor());
            scrollPane.setHorizontalScrollBar(oscrollbar);
            this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        } else {
            imagePanes.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 14));
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            oscrollbar = new OScrollBar(Adjustable.VERTICAL, model.getSelectedColor());
            scrollPane.setVerticalScrollBar(oscrollbar);
            this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 10, 0, 0), 0, 0));
        }
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        imagePanes.setOpaque(false);
        rightBtn = new JLabel("open");
        rightBtn.setPreferredSize(new Dimension(model.getRightOffset(), 20));
        this.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        this.add(rightBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 10), 0, 0));
        this.setFont(model.getFont());
        rightBtn.setVisible(false);
    }

    public void setMouseListener(MouseListener _listener) {
        if (mouseListener != null) {
            if (rightBtn.isVisible()) {
                rightBtn.removeMouseListener(mouseListener);
            }
            imagePanes.removeMouseListener(mouseListener);
            this.removeMouseListener(_listener);
            checkbox.removeMouseListener(_listener);
        }
        mouseListener = _listener;
        if (mouseListener != null) {
            if (rightBtn.isVisible()) {
                rightBtn.addMouseListener(mouseListener);
            }
            imagePanes.addMouseListener(mouseListener);
            this.addMouseListener(mouseListener);
            checkbox.addMouseListener(_listener);
        }
    }

    public String getPath(){
//        return selector.getPath();
    	return null;
    }
    
    // //////////////////UI机制///////////////////////

    /**
     * Returns the look and feel (L&F) object that renders this component.
     *
     * @return the PanelUI object that renders this component
     * @since 1.4
     */
    @Override
    public OGroupPanelUI getUI() {
        return (OGroupPanelUI) ui;
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
    public void setUI(OGroupPanelUI ui) {
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
//        setMinimumSize(size);
//        setMaximumSize(size);
        setSize(size);
        this.repaint();

        model.setWidth(width);
        model.setHeight(height);
    }

    public void setSelectedColor(Color color) {
        this.model.setSelectColor(color);
    }

    public void setBackgroundColor(Color color) {
        this.model.setBackgroundcolor(color);
    }

    public void setRightComponentVisible(boolean flag) {
        rightBtn.setFont(model.getFont());
        rightBtn.removeMouseListener(mouseListener);
        if (flag && mouseListener != null) {
            rightBtn.addMouseListener(mouseListener);
        }
        this.rightBtn.setVisible(flag);
    }

    public void setImagePaneLayout(int left, int top) {
        imagePanes.setLayout(new FlowLayout(FlowLayout.LEFT, left, top));
        imagePanes.revalidate();
        imagePanes.repaint();
    }

    public void setRightComponentText(String text) {
        this.rightBtn.setText(text);
    }

    public boolean getRightComponentVisible() {
        return this.rightBtn.isVisible();
    }

    public JLabel getRightComponent() {
        return this.rightBtn;
    }

    public void setDropSource(DragSource _dragSource) {
        dragSource = _dragSource;
        setImageDropSource();
    }

    public void setDropListener(DragListener _listener) {
        this.dndListener = _listener;
        if (model.isDrop() && dndListener != null) {
            new DropTarget(this.imagePanes, DnDConstants.ACTION_MOVE, dndListener);
            setImageDropSource();
        }
    }

    private void setImageDropSource() {
        if (model.isDrop() && dndListener != null && dragSource != null) {
            Component[] components = imagePanes.getComponents();
            for (int i = 0; i < components.length; i++) {
                dragSource.createDefaultDragGestureRecognizer(components[i], DnDConstants.ACTION_MOVE, dndListener);
            }
        }
    }

    public void addImagePanel(OImagePanel comp) {
        addImagePanel(comp, true);
    }

    public void setImageSelected(boolean flag) {
        model.setImageSelected(flag);
    }

    public boolean isImageSelected() {
        return model.isImageSelected();
    }

    public void setVisibleCount(int count) {
        model.setVisbleCount(count);
    }

    public void addImagePanel(OImagePanel comp, boolean isDrop) {
        int hgap = ((FlowLayout) imagePanes.getLayout()).getHgap();
        int vgap = ((FlowLayout) imagePanes.getLayout()).getVgap();
        if (model.isHorizontal()) {
            comp.setPanelSize((model.getWidth()-model.getRightOffset()) / model.getVisibleCount() -  hgap, model.getHeight() -  vgap);
        } else {
            comp.setPanelSize(model.getWidth() - 2 * hgap - vgap, model.getHeight() / model.getVisibleCount());
//            imagePanes.setBackground(model.getBackgroundcolor());
//            comp.getModel().setSelectColor(model.getSelectedColor());
        }
        imagePanes.add(comp);
        if (isDrop && model.isDrop() && dndListener != null && dragSource != null) {
            dragSource.createDefaultDragGestureRecognizer(comp, DnDConstants.ACTION_MOVE, dndListener);
        }
        if (this.mouseListener != null && model.isImageSelected()) {
            comp.removeMouseListener(mouseListener);
            comp.addMouseListener(mouseListener);
        }
    }

    public void removeImagePanel(OImagePanel comp) {
        imagePanes.remove(comp);
        if (this.mouseListener != null && model.isImageSelected()) {
            comp.removeMouseListener(mouseListener);
        }
        setImagePanelSize();
    }

    private void setImagePanelSize() {
        int hgap = ((FlowLayout) imagePanes.getLayout()).getHgap();
        int vgap = ((FlowLayout) imagePanes.getLayout()).getVgap();
        Component[] components = imagePanes.getComponents();
        int width = 0;
        int height = 0;
        for (int i = 0; i < components.length; i++) {
            if (model.isHorizontal()) {
                width += (components[i].getWidth() + hgap);
            } else {
                height += (components[i].getHeight() + vgap);
            }

            if (components[i] instanceof OImagePanel) {
//                if (model.isSelected()) {
//                    ((OImagePanel) components[i]).setBackgroundColor(model.getNotSelectedColor());
//                } else {
//                ((OImagePanel) components[i]).setBackgroundColor(model.getBackgroundcolor());
//                }
                ((OImagePanel) components[i]).getModel().setSelectColor(model.getNotSelectedColor());
            }
        }
        if (model.isHorizontal()) {
            imagePanes.setPreferredSize(new Dimension(width + hgap, imagePanes.getHeight() + vgap));
        } else {
            if (height + vgap > model.getHeight()) {
                imagePanes.setPreferredSize(new Dimension(imagePanes.getWidth(), height + vgap));
            } else {
                imagePanes.setPreferredSize(new Dimension(imagePanes.getWidth(), model.getHeight() - 20));
            }
        }
        imagePanes.revalidate();
        imagePanes.repaint();
        this.revalidate();
        this.repaint();
    }

    /**
     * 
     * @return
     */
    public List<OImagePanel> getImagePanels() {
        ArrayList<OImagePanel> imagelist = new ArrayList<OImagePanel>();
        Component[] components = imagePanes.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof OImagePanel) {
                OImagePanel imageLabel = (OImagePanel) components[i];
                imagelist.add(imageLabel);
            }
        }
        return imagelist;
    }

    public void setHasSelected(boolean hasSelected) {
        model.setHasSelected(hasSelected);
    }

    /**
     * @param _isSelected 
     */
    public void setSelected(boolean _isSelected) {
        checkbox.setSelected(_isSelected);
        getModel().setSelected(_isSelected);
    }

    /**
     * @param _isSelected
     * @param loc : -1 all , >=0 one
     */
    public void setSelectedImagePanel(boolean _isSelected, int loc) {
        Component[] components = imagePanes.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof OImagePanel) {
                if (loc == -1 || loc == i) {
                    ((OImagePanel) components[i]).setSelected(_isSelected);
                } else {
                    ((OImagePanel) components[i]).setSelected(!_isSelected);
                }
            }
        }
    }

    /**
     * @param _isHorizontal
     */
    public void setHorizontal(boolean _isHorizontal) {
        getModel().setHorizontal(_isHorizontal);
    }

    /**
     * @param model the model to set
     */
    public void setModel(GroupPanelModel model) {
        this.model = model;
    }

    /**
     * @return the model
     */
    public GroupPanelModel getModel() {
        return model;
    }

    private void setImageBackgroup(Color color) {
        imagePanes.setBackground(color);
        Component[] components = imagePanes.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof OImagePanel) {
                ((OImagePanel) components[i]).setBackgroundColor(color);
            }
        }
    }

    @Override
    public void forceCustomDraw() {
//        if (!model.isImageSelected() && model.hasSelected()) {
//            if (model.isSelected()) {
//                oscrollbar.getModel().setTrackColor(model.getNotSelectedColor());
//                setImageBackgroup(model.getNotSelectedColor());
//            } else {
//                oscrollbar.getModel().setTrackColor(model.getBackgroundcolor());
//                setImageBackgroup(model.getBackgroundcolor());
//            }
//        } else {
        if (!model.isImageSelected()) {
            oscrollbar.getModel().setTrackColor(model.getBackgroundcolor());
//            setImageBackgroup(model.getBackgroundcolor());
        }
//        else {
//            oscrollbar.getModel().setTrackColor(model.getSelectedColor());
//            setImageBackgroup(model.getSelectedColor());
//        }

//        }
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        if ( model.isSelected()) {
            this.setBackground(model.getSelectedColor());
            if (!model.isImageSelected()) {
                path.setVisible(true);
            }
        } else {
            this.setBackground(model.getBackgroundcolor());
            if (!model.isImageSelected()) {
                path.setVisible(false);
            }
        }
        this.checkbox.setBackground(model.getBackgroundcolor());
        if (model.hasSelected()) {
            checkbox.setVisible(true);
        } else {
            checkbox.setVisible(false);
        }
        getUI().redraw();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("click in " + e.getSource().getClass().getName());
        if (e.getSource() instanceof JPanel) {
            boolean iss = model.isSelected();
            model.setSelected(!iss);

            if (e.getSource() instanceof OImagePanel && model.isImageSelected()) {
                setSelectedImageLabel((OImagePanel) e.getSource());
            }
        } else if (e.getSource() instanceof OCheckBox) {
            model.setSelected(((OCheckBox) e.getSource()).isSelected());
        }
    }

    public void setSelectedImageLabel(OImagePanel obj) {
        setSelectedImagePanel(false, -1);
        obj.setSelected(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void postExec(ActionEvent e) {
//        System.out.println(selector.getPath());
    }
}
