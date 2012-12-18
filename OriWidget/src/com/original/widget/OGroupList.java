/*
 *  com.original.widget.OImagePanel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DragListener;
import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.GroupPanelModel;
import com.original.widget.model.GroupListModel;
import com.original.widget.plaf.OGroupPanelUI;
import com.original.widget.plaf.OGroupListUI;
import java.awt.*;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class OGroupList extends JPanel implements MouseListener, CustomDrawable {

    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "OGroupListUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OGroupListUI");
    }
    /**
     *
     */
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private GroupListModel model;
    private DrawAreaChangeListener listener;
    private JPanel listPanes;
    private JScrollPane scrollPane;
    private DragListener dndListener = null;
    private DragSource dragSource = null;
    private OScrollBar oscrollbar = null;

    /**
     * 
     * @param width
     * @param height
     * @param rowHeight
     */
    public OGroupList(int width, int height, int rowHeight) {
        //model
        setModel(new GroupListModel(width, height, rowHeight));
        //size
        this.setPanelSize(width, height);
        //mode property listner
        listener = new DrawAreaChangeListener(this);
        this.model.addChangeListener(listener);
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        listPanes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(listPanes);
        listPanes.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        listPanes.setBackground(model.getBackgroundcolor());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        oscrollbar = new OScrollBar(Adjustable.VERTICAL, model.getBackgroundcolor());
        scrollPane.setVerticalScrollBar(oscrollbar);
        scrollPane.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
    }

    // //////////////////UI机制///////////////////////
    /**
     * Returns the look and feel (L&F) object that renders this component.
     *
     * @return the PanelUI object that renders this component
     * @since 1.4
     */
    @Override
    public OGroupListUI getUI() {
        return (OGroupListUI) ui;
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
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);

        model.setWidth(width);
        model.setHeight(height);

        this.repaint();
    }

    public void setDropSource(DragSource _dragSource) {
        dragSource = _dragSource;
    }

    public void setDropListener(DragListener _listener) {
        this.dndListener = _listener;
    }

    public void setSelected(int index) {
        OGroupPanel obj = model.get(index);
        if (obj != null) {
            model.setSelected(obj, true);
        }
    }

    public OGroupPanel getSelected() {
        return model.get(model.getSelectedIndex());
    }

    public List<OGroupPanel> getAllSelected() {
        return model.getSelectedList();
    }

    public int getListSize(){
        return model.size();
    }
    
    public void addCellPanel(OGroupPanel cell) {
        model.add(cell);
        cell.setDropSource(dragSource);
        cell.setDropListener(dndListener);
        cell.setPanelSize(model.getWidth() - 3, model.getRowHeight());
        cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(212, 212, 212)));
        cell.setMouseListener(this);
        listPanes.add(cell);
        int count = listPanes.getComponentCount();
        listPanes.setPreferredSize(new Dimension(model.getWidth(), model.getRowHeight() * count));
        listPanes.revalidate();
        listPanes.repaint();
    }

    public void removeCellPanel(OGroupPanel cell) {
        if (cell == null) {
            return;
        }
        model.remove(cell);
        listPanes.remove(cell);
        int count = listPanes.getComponentCount();
        cell.setMouseListener(null);
        cell.setDropSource(null);
        cell.setDropListener(null);
        listPanes.setPreferredSize(new Dimension(model.getWidth(), model.getRowHeight() * count));
        listPanes.revalidate();
        listPanes.repaint();
    }

    public void removeCellPanel(int index) {
        removeCellPanel((OGroupPanel) model.get(index));
    }

    /**
     * @param model the model to set
     */
    public void setModel(GroupListModel model) {
        this.model = model;
    }

    /**
     * @return the model
     */
    public GroupListModel getModel() {
        return model;
    }

    @Override
    public void forceCustomDraw() {
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
        listPanes.setBackground(model.getBackgroundcolor());
        listPanes.setForeground(model.getForecolor());
        oscrollbar.getModel().setTrackColor(model.getBackgroundcolor());
        getUI().redraw();
        repaint();
    }

    public void saveInfo(String path) {
        System.out.println("Select path = " + path);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("click in " + e.getSource().getClass().getName());
        OGroupPanel cell = this.getClickedCellPanel(e.getComponent());
        if (cell == null) {
            return;
        }
        if (e.getSource() instanceof JLabel) {
            splitCellPanel(cell);
            return;
        }
//        if (e.getSource() instanceof JButton) {
//            this.saveInfo(this.getSelected().getPath());
//            return;
//        }
        if (cell != null) {

            boolean isselect = !cell.getModel().isSelected();
            if (!model.isMultiple()) {
                isselect = true;
            } else if (cell.getModel().isSelected()) {
                isselect = false;
            } else {
                isselect = true;
            }
            model.setSelected(cell, isselect);
        }
    }

    private void splitCellPanel(OGroupPanel cell) {
        cell.setRightComponentVisible(false);
        List<OImagePanel> panels = cell.getImagePanels();
        GroupPanelModel model1 = cell.getModel();
        for (int i = 1; i < panels.size(); i++) {
            OGroupPanel cell1 = new OGroupPanel(model1.getWidth(), model1.getHeight() - 40,
                    model1.getRightOffset(), model1.isHorizontal(), model1.isDrop()){
                @Override
                public void postExec(ActionEvent e) {
                    saveInfo(getPath());
                }
            };
            cell1.getModel().copy(cell.getModel());
            cell1.addImagePanel(panels.get(i), false);
            this.addCellPanel(cell1);
        }
    }

    public void setSelectionModel(boolean isMutiple) {
        this.getModel().setMutilple(isMutiple);
    }

    private OGroupPanel getClickedCellPanel(Component c) {
        if (c instanceof OGroupPanel) {
            return (OGroupPanel) c;
        }
        while (true) {
            c = c.getParent();
            if (c == null) {
                break;
            }
            if (c instanceof OGroupPanel) {
                break;
            }
        }
        return (OGroupPanel) c;
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
}
