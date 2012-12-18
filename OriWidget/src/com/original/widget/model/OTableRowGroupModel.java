package com.original.widget.model;

import java.util.Vector;

public class OTableRowGroupModel {

    private Vector groupVector = new Vector();
    private String name = "";
    private boolean isFold = false;

    public boolean isFold() {
        return isFold;
    }

    public String getName(){
        return name;
    }
    
    public void setFold(boolean isFold) {
        this.isFold = isFold;
    }

    public OTableRowGroupModel() {
    }

    //为分组添加分组名称
    public void setTableRowGroupName(String name) {
        this.name = name;
    }

//    private void initGroupHeader() {
//        if (groupVector.size() > 0 && groupVector.get(0) instanceof TableGroup) {
//            groupVector.remove(0);
//        }
//        Vector groupHeaderVector = new Vector();
//        groupHeaderVector.add(new TableGroup(this.name, null));
//        groupHeaderVector.add(this.name);
//        groupVector.insertElementAt(groupHeaderVector,0);
//    }

    //添加row
    public void addGroupRow(Vector rowVector) {

//        //转换row
//        Vector tempVector = new Vector();
//        if (model.isChecked()) {
//            tempVector.add(false);
//        }
//
//        for (int i = 0; i < rowVector.size(); i++) {
//            tempVector.add(rowVector.get(i));
//        }
        groupVector.add(rowVector);
    }

    //添加row
    public void addGroupRow(Object row) {
//        //转换row
//        Vector tempVector = new Vector();
//        if (model.isChecked()) {
//            tempVector.add(false);
//        }
//        Vector keys = model.getColumnKeys();
//        for (int i = 0; i < keys.size(); i++) {
//            Object o = model.getValueByKey(row, (String) keys.get(i));
//            if (o != null) {
//                if (o instanceof Date) {
//                    tempVector.add(this.formateDate((Date) o));
//                } else {
//                    tempVector.add(o);
//                }
//            } else {
//                tempVector.add("");
//            }
//        }
        groupVector.add(row);
    }

    public Vector getGroupVector() {
        return groupVector;
    }

    public void setGroupVector(Vector groupVector) {
        this.groupVector = groupVector;
    }
}
