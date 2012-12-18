package com.original.widget.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.original.widget.OComplexTable;
import com.original.widget.model.OComplexTableModel;
import com.original.widget.model.TableGroup;

public class TableRowMouseListener implements MouseListener {

    private OComplexTable table;
    private OComplexTableModel tableModel;

    public TableRowMouseListener(OComplexTable table) {
        this.table = table;
        this.tableModel = this.table.getModel();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int column = table.getSelectedColumn();
        if (column >= 0) {
            Object o = tableModel.getValueAt(table.getSelectedRow(), 0);
            if (o instanceof TableGroup) {
                tableModel.setRowGroupStatus(((TableGroup) o).getIndex());
                tableModel.initModelData();
                this.table.resetCellEditor();
                return;
            }
        }
        rowMouseClicked(e);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void rowMouseClicked(MouseEvent arg0) {
    }
}
