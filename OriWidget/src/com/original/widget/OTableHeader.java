/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget;

import com.original.widget.event.OTableGroupRenderer;
import com.original.widget.plaf.OTableHeaderUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * 
 * @author liummb
 */
public class OTableHeader extends JTableHeader {

    private static final String uiClassID = "OTableHeaderUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OTableHeaderUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    public boolean isClicked = false;
    public boolean isChecked = true;
    //header组件
    private List<JComponent> headerComs;
    private Vector columnsType = null;
    //全选checkbox
    private OCheckBox allCheckbox = new OCheckBox();
    private Font font = null;

    public OTableHeader() {
        
        this.setUI(new OTableHeaderUI());
        setOpaque(false);
        //this.setResizingAllowed(true);
    }

    public OTableHeader(TableColumnModel tableColumnModel, boolean _isChecked, Vector _columnsType, Font _font) {
        this.isChecked = _isChecked;
        this.font = _font;
        this.columnsType = _columnsType;
        
        this.setUI(new OTableHeaderUI());
        setOpaque(false);
        this.setResizingAllowed(true);
        this.setReorderingAllowed(false);
        this.setColumnModel(tableColumnModel);

        headerComs = new ArrayList<JComponent>();

        int j = 0;
        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            if (i == 0 && isChecked) {
                allCheckbox.setHorizontalAlignment(SwingConstants.CENTER);
                allCheckbox.setOpaque(false);
                
                allCheckbox.addActionListener(new java.awt.event.ActionListener() {
                
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        allCheckboxSelected(evt);
                    }
                });
                this.add(allCheckbox);
                this.headerComs.add(allCheckbox);
            } else {
                String value = tableColumnModel.getColumn(i).getHeaderValue().toString();
                if (value.length() == 0) {
                    JLabel label = new JLabel(value);
                    label.setFont(font);
                    this.add(label);
                    this.headerComs.add(label);
                } else if (columnsType != null && j < this.columnsType.size()
                        && columnsType.get(j).toString().equalsIgnoreCase("label")) {
                    ORichLabel label = new ORichLabel(100, 30, value);
                    label.setAutoWidthSize(true);
                    label.setFont(font);
                    this.add(label);
                    this.headerComs.add(label);
                } else {
                    OTextField text = new OTextField();
                    text.setPreferredSize(new Dimension(100, 30));
                    text.setCaption(value);
                    //text.setOpaque(false);
                    text.setFont(font);
                    this.add(text);
                    this.headerComs.add(text);
                }
                j++;
            }
        }
    }

    //全选事件
    private void allCheckboxSelected(ActionEvent evt) {

        for (int i = 0; i < this.table.getModel().getRowCount(); i++) {
            //判断是不是分组
            if (!(this.table.getCellRenderer(i, 1) instanceof OTableGroupRenderer)) {
                this.table.getModel().setValueAt(allCheckbox.isSelected(), i, 0);
            }
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    // 设置UI
    public void setUI(OTableHeaderUI ui) {
        super.setUI(ui);
    }

    @Override
    public OTableHeaderUI getUI() {
        return (OTableHeaderUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    //获取header组建
    public List<JComponent> getHeaderComs() {
        return headerComs;
    }

    public void setHeaderComs(List<JComponent> headerComs) {
        this.headerComs = headerComs;
    }

    //获取全选组建
    public OCheckBox getAllCheckbox() {
        return allCheckbox;
    }

    public void setAllCheckbox(OCheckBox allCheckbox) {
        this.allCheckbox = allCheckbox;
    }

    //获取过滤框组建
    public Object getTableHeaderFilter(int index) {
        return this.headerComs.get(index + (isChecked ? 1 : 0));
    }
}
