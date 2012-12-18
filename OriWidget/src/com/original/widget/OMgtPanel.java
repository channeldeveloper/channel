/*
 *  com.original.widget.OMgtPanel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.TableHeaderFilterListener;
import com.original.widget.event.TableRowMouseListener;
import com.original.widget.model.OComplexTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-4 15:53:55
 */
public class OMgtPanel extends JPanel {

    private OComplexTable table;
    private OTableHeader tableheader;
    private OComplexTableModel model;
    private String[] headers = null;
    private String[] columnTypes = null;
    private String[] columnKeys = null;
    private int[] columnWidths = null;
    private boolean isChecked = true;
    private boolean isSearch = true;

    public void setSearch(boolean _isSearch){
        isSearch = _isSearch;
    }

    public boolean isSearch(){
        return isSearch;
    }
    
    public void setChecked(boolean _isChecked) {
        this.isChecked = _isChecked;
    }

    public boolean isChecked() {
        return this.isChecked();
    }

    public void setColumnTypes(String[] _columnTypes) {
        columnTypes = _columnTypes;
    }

    public void setColumnKeys(String[] _columnKeys) {
        columnKeys = _columnKeys;
    }

    public void setColumnWiths(int[] _columnWiths) {
        columnWidths = _columnWiths;
    }

    public void setHeaders(String[] _headers) {
        headers = _headers;
    }

    public void setSelectedRow(int row){
        if (row < model.getRowCount())
            table.setRowSelectionInterval(row, row);
    }
    
    public void setDataToTable(Vector[] datas) {
        model.removeAllData();
        model.addRows(datas);
        changeModelData();
    }

    public void changeModelData(){
        model.initModelData();
        table.resetCellEditor();
    }

    public void setDataToTable(List datas) {
        model.removeAllData();
        model.addRows(datas);
        changeModelData();
    }

    public Vector getAllValue(){
        return model.getAllValue();
    }

    public Vector getAllInitValue(){
        return model.getAllInitValue();
    }

    public Object getRowValue(int _rowIndex) {
        return model.getRowValue(_rowIndex);
    }

    public int getSelectedRow(){
        return table.getSelectedRow();
    }

    public Object getSelectedObject(){
        return model.getRowValue(table.getSelectedRow());
    }
    
    public Vector getCheckedRowsValue(){
        return model.getCheckedRowsValue();
    }

    public Vector getRowsValue(int[] _indexes) {
        return model.getRowsValue(_indexes);
    }

    public void Filter_TextChanged(int column) {
        String val = ((JTextField)tableheader.getTableHeaderFilter(column)).getText();
        model.setFilterColumn(column, val);
        if ( this.isSearch ){
            model.initModelData();
            table.resetCellEditor();
        }
    }

    public String getFilterText(int column){
        return model.getFilterColumnData(column);
    }

    public void clearAllFilterData(){
        this.isSearch = false;
        Set<Integer> columns = model.getFilterColumns().keySet();
        for ( Integer column : columns ){
            ((JTextField)tableheader.getTableHeaderFilter(column)).setText("");
        }
        this.isSearch = true;
    }
    
    public void rowClicked(int clickcount) {
    }

    public OComplexTableModel getTableModel() {
        return this.model;
    }

    public OComplexTable getTable() {
        return this.table;
    }

    public void setTableSize(int width, int height, int head_height) {
        table.getTableHeader().setPreferredSize(new Dimension(width, head_height));
        table.setPreferredScrollableViewportSize(new Dimension(width, height));// 表格的显示尺寸
    }

    public void initTable() {
        this.removeAll();
        model = new OComplexTableModel();
        model.setChecked(isChecked);

        if (headers != null) {
            //创建 columns
            Vector columnsVector = new Vector();
            for (int i = 0; i < headers.length; i++) {
                columnsVector.add(headers[i]);
            }
            model.setTableColumn(columnsVector);
        }
        if (columnTypes != null) {
            Vector columnsVector = new Vector();
            for (int i = 0; columnTypes != null && i < columnTypes.length; i++) {
                columnsVector.add(columnTypes[i]);
            }
            model.setColumnTypes(columnsVector);
        }
        if (columnKeys != null) {
            Vector columnsVector = new Vector();
            for (int i = 0; columnKeys != null && i < columnKeys.length; i++) {
                columnsVector.add(columnKeys[i]);
            }
            model.setColumnKeys(columnsVector);
        }
        model.setColumnWiths(columnWidths);
        
        // 创建table,并将tablemodel添加到table中
        table = new OComplexTable(model);
        table.getTableHeader().setOpaque(false);
        // 创建一个tableheader
        tableheader = new OTableHeader(table.getColumnModel(), this.isChecked,model.getColumnTypes(),table.getFont());
        //tableheader.setBackground(this.getBackground());
        tableheader.setOpaque(false);

        // 将此tableheader添加到table中
        table.setTableHeader(tableheader);
        
        // 产生一个带滚动条的面板
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, Color.white));
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        //scrollPane.setBackground(this.getBackground());
        //scrollPane.getViewport().setUI(new OViewPortUI(scrollPane.getViewport()));
        scrollPane.getViewport().setBackground(this.getBackground());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        scrollPane.setColumnHeader(new JViewport());
        scrollPane.getColumnHeader().setOpaque(false);
                
        // 将带滚动条的面板添加入窗口中
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.setBackground(new Color(237,237,237));
        this.setOpaque(false);
        for (int i = 0; headers != null && i < headers.length ; i++) {
            Object obj = tableheader.getTableHeaderFilter(i);
            if (obj instanceof JTextField) {
                model.addFilterColumn(i);
                
                // 给第i个 header过滤框 添加事件
                ((JTextField)tableheader.getTableHeaderFilter(i)).getDocument().addDocumentListener(
                        new TableHeaderFilterListener(i) {
                            //过滤框文本发生变化，触发此事件
                            @Override
                            public void FilterTextChanged(javax.swing.event.DocumentEvent e) {
                                Filter_TextChanged(getColumn());
                            }
                        });
            }
        }
        //给table的row添加单击事件
        table.addMouseListener(new TableRowMouseListener(table) {

            @Override
            public void rowMouseClicked(MouseEvent e) {
                rowClicked(e.getClickCount());
            }
        });
        table.resetCellEditor();
    }
}
