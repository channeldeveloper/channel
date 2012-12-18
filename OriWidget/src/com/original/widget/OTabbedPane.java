/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.model.OTabbedModel;
import com.original.widget.plaf.OTabbedPaneUI;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
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
public class OTabbedPane extends JTabbedPane implements MouseListener {

    private static final String uiClassID = "OTabbedPaneUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OTabbedPaneUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    private Vector subTitles = new Vector(1);
    Vector pages;

    public OTabbedPane() {
        this(TOP, SCROLL_TAB_LAYOUT);
    }

    public OTabbedPane(int tabPlacement) {
        this(tabPlacement, SCROLL_TAB_LAYOUT);
    }

    public OTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        this.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        setTabPlacement(tabPlacement);
        setTabLayoutPolicy(tabLayoutPolicy);
        pages = new Vector(1);
        setModel(new OTabbedModel());
        updateUI();
        addMouseListener(this);
    }

    /**
     * 设置页签高度
     * @param height
     */
    public void setDividerHight(int height) {
        getTabbedModel().setDividerHeight(height);
    }

    /**
     * 获取数据模型对象
     * @return
     */
    public OTabbedModel getTabbedModel(){
        return ((OTabbedModel)this.getModel());
    }
    /**
     * 插入页签
     * @param title
     * @param comp
     */
    @Override
    public void addTab(String title, Component comp) {
        this.addTab(title, comp, null, true);
    }

    public void addTab(String title, Component comp, boolean hasClose) {
        this.addTab(title, comp, null, hasClose);
    }

    public String getSubTitle(int tabIndex) {
        if (tabIndex < subTitles.size()) {
            return (String) subTitles.get(tabIndex);
        }
        return null;
    }
    /**
     * 设置页签标题
     * @param tabIndex
     * @param _subTitle
     */
    public void setSubTitle(int tabIndex, String _subTitle) {
        if (tabIndex < subTitles.size()) {
            this.subTitles.set(tabIndex, _subTitle);
        } else {
            this.subTitles.add(tabIndex, _subTitle);
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void removeTabAt(int tabIndex) {
        super.removeTabAt(tabIndex);
        if (tabIndex < subTitles.size()) {
            subTitles.remove(tabIndex);
        }
    }

    public void addTab(String title, Component comp, Icon extraIcon, boolean hasClose) {
        if (hasClose) {
            super.addTab(title, new CloseTabIcon(extraIcon), comp);
        } else {
            super.addTab(title, comp);
        }
        this.setSelectedComponent(comp);
    }

    public void setUI(OTabbedPaneUI ui) {
        super.setUI(ui);
    }

    @Override
    public OTabbedPaneUI getUI() {
        return (OTabbedPaneUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    @Override
    public void updateUI() {
        setUI((OTabbedPaneUI)UIManager.getUI(this));
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.getTabCount() > 1) {
            int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
            if (tabNumber < 0) {
                return;
            }
            Object o = getIconAt(tabNumber);
            if (o == null) {
                return;
            }
            int tabPlacement1 = this.getTabPlacement();
            int x = e.getX(),y = e.getY();
            if ( tabPlacement1 == BOTTOM )
                y = this.getHeight() - y;
            else if ( tabPlacement1 == RIGHT)
                x = x - this.getSelectedComponent().getWidth();
            Rectangle rect = ((CloseTabIcon) getIconAt(tabNumber)).getBounds();
            if (rect.contains(x,y)) {
                //the tab is being closed
                this.removeTabAt(tabNumber);
            }
        }
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

    class CloseTabIcon implements Icon {

        private int x_pos;
        private int y_pos;
        private int width;
        private int height;
        private Icon fileIcon;

        public CloseTabIcon(Icon fileIcon) {
            this.fileIcon = fileIcon;
            width = 16;
            height = 16;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {

            this.x_pos = x;
            this.y_pos = y;
            int y_p = y + 2;
//            g.drawLine(x + 1, y_p, x + 12, y_p);
//            g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
//            g.drawLine(x, y_p + 1, x, y_p + 12);
//            g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
            g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
            g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
            g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
            g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
            g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
            g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
            if (fileIcon != null) {
                fileIcon.paintIcon(c, g, x + width, y_p);
            }
        }

        @Override
        public int getIconWidth() {
            return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, width, height);
        }
    }
}
