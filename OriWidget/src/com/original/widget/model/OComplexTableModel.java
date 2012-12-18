/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget.model;

import com.original.widget.draw.OriImgProcer;
import java.awt.Color;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author liummb
 */
public class OComplexTableModel extends DefaultTableModel {

    private Color arrowclr = new Color(48, 160, 205);
    private Vector columnsVector = new Vector();
    private Vector rowsVector = new Vector();// 转换后的数据
    private ArrayList<OTableRowGroupModel> groupList = new ArrayList<OTableRowGroupModel>();
    private boolean isChecked = true;
    private Vector columnTypes = new Vector();
    private Vector columnKeys = new Vector();
    private int[] columnWiths = null;
    private Vector rowsdata = new Vector(); // 转换前的数据
    private Vector initdatas = new Vector(); // 初始数据
    private int groupcolumn = -1; // 组选显示列
    private HashMap<Integer, String> filterColumns = new HashMap<Integer, String>(); // 需要过滤的列信息
    private final ImageIcon uparrow = new ImageIcon(OriImgProcer.createUpArrow(arrowclr));
    private final ImageIcon downarrow = new ImageIcon(OriImgProcer.createDownArrow(arrowclr));

    public OComplexTableModel() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean _isChecked) {
        this.isChecked = _isChecked;
    }

    public void setColumnTypes(Vector _columntypes) {
        this.columnTypes = _columntypes;
    }

    public void addFilterColumn(Integer column) {
        this.setFilterColumn(column, "");
    }

    public void setFilterColumn(Integer column, String value) {
        if (filterColumns.containsKey(column)) {
            filterColumns.remove(column);
        }
        filterColumns.put(column, value);
    }

    public String getFilterColumnData(Integer column) {
        return filterColumns.get(column);
    }

    public HashMap getFilterColumns() {
        return this.filterColumns;
    }

    public Vector getColumnTypes() {
        return this.columnTypes;
    }

    public void setColumnWiths(int[] _columnWiths) {
        this.columnWiths = _columnWiths;
    }

    public int[] getColumnWiths() {
        return this.columnWiths;
    }

    public Vector getColumnKeys() {
        return this.columnKeys;
    }

    public void setColumnKeys(Vector _columnkeys) {
        this.columnKeys = _columnkeys;
    }

    public ArrayList<OTableRowGroupModel> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<OTableRowGroupModel> groupList) {
        this.groupList = groupList;
    }

    // 添加row分组
    public void addTableRowGroup(OTableRowGroupModel tableRowGroupModel) {
        groupList.add(tableRowGroupModel);
    }

    //获取row分组
    public OTableRowGroupModel getTableRowGroup(int index) {
        if (index > groupList.size()) {
            return null;
        }
        return groupList.get(index);
    }

    public void setRowGroupStatus(int index) {
        OTableRowGroupModel group = this.getTableRowGroup(index);
        if (group != null) {
            boolean fold = group.isFold();
            group.setFold(!fold);
        }
    }

    // 添加column
    public void setTableColumn(Vector columnsV) {
        if (this.isChecked) {
            this.columnsVector.add("");
        }
        for (int i = 0; i < columnsV.size(); i++) {
            if (columnsV.get(i) != null
                    && columnsV.get(i).toString().length() > 0
                    && groupcolumn == -1) {
                this.groupcolumn = i;
                if (this.isChecked) {
                    this.groupcolumn++;
                }
            }
            this.columnsVector.add(columnsV.get(i));
        }
        this.setColumnIdentifiers(columnsVector);
    }

    public Object getValueByKey(Object valobj, String key) {
        String mname = "get" + key;
        Method m = null;
        try {
            m = valobj.getClass().getMethod(mname, new Class[]{});
            return m.invoke(valobj, new Object[0]);
        } catch (Exception ex) {
            try {
                m = valobj.getClass().getMethod(key, new Class[]{});
                return m.invoke(valobj, new Object[0]);
            } catch (Exception ex1) {
            }
        }
        return "";
    }

    // 增加行
    public void addRow(Object row) {
        this.initdatas.add(row);
    }

    // 增加多行
    public void addRows(Vector[] row) {
        for (int i = 0; row != null && i < row.length; i++) {
            this.initdatas.add(row[i]);
        }
    }

    // 增加多行
    public void addRows(List row) {
        for (int i = 0; row != null && i < row.size(); i++) {
            this.initdatas.add(row.get(i));
        }
    }

    boolean isFilterData(String filter, Object value) {
        if (value == null || filter == null || filter.length() == 0) {
            return false;
        }

        String val = value.toString();
        if (value instanceof Date) {
            val = this.formateDate((Date) value);
        }
        if (val.indexOf(filter) == -1) {
            return true;
        }
        return false;
    }

    private boolean isRowToTable(Object row) {
        if (row instanceof Vector) {
            for (int i = 0; i < ((Vector) row).size(); i++) {
                Object o = ((Vector) row).get(i);
                if (this.filterColumns.containsKey(i)
                        && this.isFilterData(this.filterColumns.get(i), o)) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < columnKeys.size(); i++) {
                Object o = getValueByKey(row, (String) columnKeys.get(i));
                if (o != null) {
                    if (this.filterColumns.containsKey(i)
                            && this.isFilterData(this.filterColumns.get(i), o)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void insertRowToTable(Object row) {
        Vector tempVector = new Vector();
        if (this.isChecked) {
            tempVector.add(false);
        }
        if (row instanceof Vector) {
            for (int i = 0; i < ((Vector) row).size(); i++) {
                Object o = ((Vector) row).get(i);
                if (this.filterColumns.containsKey(i)
                        && this.isFilterData(this.filterColumns.get(i), o)) {
                    return;
                }
                if (o instanceof Date) {
                    tempVector.add(this.formateDate((Date) o));
                } else {
                    tempVector.add(o);
                }
            }
        } else {
            for (int i = 0; i < columnKeys.size(); i++) {
                Object o = getValueByKey(row, (String) columnKeys.get(i));
                if (o != null) {
                    if (this.filterColumns.containsKey(i)
                            && this.isFilterData(this.filterColumns.get(i), o)) {
                        return;
                    }
                    if (o instanceof Date) {
                        tempVector.add(this.formateDate((Date) o));
                    } else {
                        tempVector.add(o);
                    }
                } else {
                    tempVector.add("");
                }
            }
        }
        rowsdata.add(row);
        this.rowsVector.add(tempVector);
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
        this.rowsdata.remove(row);
    }

    public void removeAllData() {
        initdatas.clear();
        int count = rowsdata.size();
        for (int i = count - 1; i >= 0; i--) {
            this.removeRow(i);
        }
        if (groupList != null) {
            groupList.clear();
        }
    }

    public Vector getAllInitValue() {
        return this.initdatas;
    }

    public Vector getAllValue() {
        return this.rowsdata;
    }

    public Object getRowValue(int _rowIndex) {
        if (rowsdata == null) {
            return null;
        }
        if ((_rowIndex < rowsdata.size()) && (_rowIndex > -1)) {
            return rowsdata.get(_rowIndex);
        }
        return null;
    }

    public Vector getCheckedRowsValue() {
        Vector datas = new Vector();
        if (this.isChecked) {
            int count = rowsdata.size();
            for (int i = 0; i < count; i++) {
                Object obj = this.getValueAt(i, 0);
                if (obj instanceof Boolean) {
                    if ((Boolean) obj) {
                        datas.add(rowsdata.get(i));
                    }
                }
            }
        }
        return datas;
    }

    public Vector getRowsValue(int[] _indexes) {
        if (_indexes != null) {
            int iCount = _indexes.length;
            Vector vTemp = new Vector(iCount);
            for (int i = 0; i < iCount; i++) {
                int index = _indexes[i];
                vTemp.addElement(rowsdata.get(index));
            }
            return vTemp;
        }
        return null;
    }

    // 创建model数据
    public void initModelData() {
        rowsdata.clear();
        rowsVector.clear();
        //分组模式
        if (groupList.size() > 0) {
            for (int i = 0; i < groupList.size(); i++) {
                Vector v = groupList.get(i).getGroupVector();
                int count = 0;
                for (int j = 0; j < v.size(); j++) {
                    if (this.isRowToTable(v.get(j))) {
                        count++;
                    }
                }
                if (count > 0) {
                    String name = groupList.get(i).getName();
                    Vector groupHeaderVector = new Vector();
                    for (int j = 0; j < this.groupcolumn; j++) {
                        groupHeaderVector.add(new TableGroup("", null, i));
                    }
                    groupHeaderVector.add(new TableGroup(name + "(" + count + ")", null, i));
                    ((TableGroup) groupHeaderVector.get(0)).setIcon((!groupList.get(i).isFold() ? uparrow : downarrow));

                    rowsdata.add(groupHeaderVector);
                    this.rowsVector.add(groupHeaderVector);
                    if (!groupList.get(i).isFold()) {
                        for (int j = 0; j < v.size(); j++) {
                            insertRowToTable(v.get(j));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < initdatas.size(); i++) {
                insertRowToTable(initdatas.get(i));
            }
        }
        setDataVector(rowsVector, columnsVector);
        this.fireTableDataChanged();
    }

    // 将表格声明为可编辑的
    @Override
    public boolean isCellEditable(int row, int col) {

        if (!(this.getValueAt(row, 0).getClass().equals(Boolean.class))) {
            return false;
        } else if (col < 1 && this.isChecked) {
            return true;
        } else {
            return false;
        }
    }

    public int getColumnWidth(int idx) {
        if (this.isChecked && idx == 0) {
            return 36;
        }

        if (this.columnWiths == null || idx >= columnWiths.length) {
            String ColumnName = this.getColumnName(idx);
            int i = 100;
            if (ColumnName != null && i < 12 * ColumnName.getBytes().length) {
                i = 12 * ColumnName.getBytes().length;
            }
            return i;
        }
        if (this.isChecked) {
            return columnWiths[idx - 1];
        }
        return columnWiths[idx];
    }

    private String formateDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stime = formater.format(date).substring(0, 16);
        return stime;
    }
}
