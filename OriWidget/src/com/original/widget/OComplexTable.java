/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget;

import com.original.widget.event.OCheckboxRenderer;
import com.original.widget.event.OTableCellRenderer;
import com.original.widget.event.OTableGroupRenderer;
import com.original.widget.model.OComplexTableModel;
import com.original.widget.model.TableGroup;
import com.original.widget.plaf.OComplexTableUI;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author liummb
 */
public class OComplexTable extends JTable {

    private static final String uiClassID = "OComplexTableUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OComplexTableUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    
    public OComplexTable(TableModel model) {
        super(model);
        init();
    }

    private void init() {
        this.setOpaque(false);
        this.setUI(new OComplexTableUI(this));
        this.setShowGrid(false);
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(false);
        this.setRowHeight(40);
        this.setColumnSelectionAllowed(false);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.getColumnModel().setColumnMargin(0);
        this.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (this.getModel().isChecked()) {
            if (this.getModel().getValueAt(row, 0) instanceof TableGroup) {
                return new OTableGroupRenderer();
            } else if (column != 0) {
                return new OTableCellRenderer();
            } else {
                return new OCheckboxRenderer();
            }
        }
        return new OTableCellRenderer();
    }
    //设置UI

    public void setUI(OComplexTableUI ui) {
        super.setUI(ui);
    }

    @Override
    public OComplexTableUI getUI() {
        return (OComplexTableUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    @Override
    public OComplexTableModel getModel() {
        return (OComplexTableModel) super.getModel();
    }

    //reset table的edit组件
    public void resetCellEditor() {
        if (this.getModel().isChecked()) {
            OCheckBox editor = new OCheckBox();
            editor.setOpaque(false);
            editor.setHorizontalAlignment(SwingConstants.CENTER);
            TableColumn oColumn = this.getColumnModel().getColumn(0);
            oColumn.setCellEditor(new DefaultCellEditor(editor));
        }
        initTableColumns();
    }

    private void initTableColumns() {
        int iColumnCount = this.getColumnCount();
        for (int i = 0; i < iColumnCount; i++) {
            TableColumn oColumn = this.getColumnModel().getColumn(i);
            int width = getModel().getColumnWidth(i);
            oColumn.setPreferredWidth(width);
            if (oColumn.getHeaderValue().toString().length() == 0  ||
                    (this.getModel().isChecked() && i == 0)) {
                oColumn.setResizable(false);
                oColumn.setMaxWidth(width);
                oColumn.setPreferredWidth(width);
                oColumn.setMinWidth(width);
            }
        }
    }
}
