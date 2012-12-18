package com.original.widget.event;

import com.original.widget.OCheckBox;
import com.original.widget.plaf.OLineBorder;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class OCheckboxRenderer extends OCheckBox implements TableCellRenderer{

	private Color selectionColor = new Color(207, 228, 249);// 行选择颜色

	private Color evenRowColor = new Color(233, 242, 241);// 奇数行颜色

	private Color oddRowColor = new Color(255, 255, 255);// 偶数行颜色
	public OCheckboxRenderer()
	{
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
	    
		Boolean b = (Boolean) value;    
        this.setSelected(b.booleanValue());    
        this.setColor(this, table, isSelected, hasFocus, row, column);
        //this.setSize(36, 30);
         if(isSelected)
            this.setOpaque(true);
        else
            this.setOpaque(false);
        this.setBorderPainted(true);
        this.setBorder(new OLineBorder(1, new Color(209,209,209)));
        return this;    

	}
	
	/*
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
