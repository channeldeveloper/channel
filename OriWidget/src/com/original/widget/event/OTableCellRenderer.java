package com.original.widget.event;

import com.original.widget.plaf.OLineBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OTableCellRenderer extends DefaultTableCellRenderer {

    private Color selectionColor = new Color(207, 228, 249);// 行选择颜色
    private Color evenRowColor = new Color(233, 242, 241);// 奇数行颜色
    private Color oddRowColor = new Color(255, 255, 255);// 偶数行颜色

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if ( value instanceof ImageIcon){
            this.setIcon((ImageIcon)value);
            this.setText("");
        }
        this.setColor(cell, table, isSelected, hasFocus, row, column);
        this.setFont(table.getFont());
        if(isSelected)
            this.setOpaque(true);
        else
            this.setOpaque(false);
        this.setBorder(new OLineBorder(1, new Color(209,209,209)));
        return this;
    }

    /**
     *
     * 设置颜色
     *
     */
    private void setColor(Component component, JTable table,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            component.setBackground(selectionColor);
            //setBorder(null);// 去掉边
        } else {
            if (row % 2 != 0) {
                component.setBackground(evenRowColor);
            } else {
                component.setBackground(oddRowColor);
            }
        }
    }
}
