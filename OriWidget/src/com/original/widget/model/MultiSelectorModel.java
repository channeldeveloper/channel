/*
 *  com.original.widget.model.MultiSelectorModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * (Class Annotation.)
 * 多项选择器的模型
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-2 23:38:09
 */
public class MultiSelectorModel {
    //选择器类型 SIMPLE-日月天，否则是时间阶段
     public static enum SELECTORTYPE {
        SIMPLE, COMPLEX
    };
    List<Object> choices = new ArrayList<Object>();
    private SELECTORTYPE type ;
    private int selectedIndex = -1;

    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    
    public MultiSelectorModel(SELECTORTYPE type){
        this.type = type;
    }

    public List<Object> getChoices() {
        return choices;
    }

    public void setChoices(List<Object> choices) {
        this.choices = choices;
    }
    

    public SELECTORTYPE getType() {
        return type;
    }

    public void setType(SELECTORTYPE type) {
        this.type = type;
    }

    public void addSimpleChoiceDataItem(String display, Object data){
        if(this.type!=SELECTORTYPE.SIMPLE) return;
        this.choices.add(new SimpleChoiceDataItem(display, data));
    }
    public void addComplexChoiceDataItem(String display, Date start, Date end){
        if(this.type!=SELECTORTYPE.COMPLEX) return;
        this.choices.add(new ComplexChoiceDataItem(display, start, end));
    }

    public int getChoiceSize(){
        return choices.size();
    }
    public int getSelectedIndex() {
        return selectedIndex;
    }
    //need to update.
    public void setSelectedIndex(int selectedIndex) {
        if(selectedIndex==-1){
            if(this.selectedIndex!=-1){
                SimpleChoiceDataItem item = (SimpleChoiceDataItem)choices.get(this.selectedIndex);
                item.setSelected(false);
             }
            this.selectedIndex = -1;
            return;

        }
        if(selectedIndex>=0 && selectedIndex<choices.size()){
            if(this.selectedIndex!=selectedIndex)
                makeSelectedChange(selectedIndex);
        }
    }

    protected void makeSelectedChange(int selectedIndex){
         if(this.type==SELECTORTYPE.SIMPLE){
             if(this.selectedIndex!=-1){
                SimpleChoiceDataItem item = (SimpleChoiceDataItem)choices.get(this.selectedIndex);
                item.setSelected(false);
             }
             SimpleChoiceDataItem item = (SimpleChoiceDataItem)choices.get(selectedIndex);
             item.setSelected(true);

         }else{
             if(this.selectedIndex!=-1){
                ComplexChoiceDataItem item = (ComplexChoiceDataItem)choices.get(this.selectedIndex);
                item.setSelected(false);
             }
             ComplexChoiceDataItem item = (ComplexChoiceDataItem)choices.get(selectedIndex);
             item.setSelected(true);
         }

         this.selectedIndex = selectedIndex;
         this.fireStateChanged();
    }


    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }


    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(
                ChangeListener.class);
    }

    
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    //定义了复杂数据类型选项
    public class ComplexChoiceDataItem extends BaseChoiceItem{
        String display;
        Date start;
        Date end;
        
        public ComplexChoiceDataItem(String display, Date start, Date end){
            this.display = display;
            this.start = start;
            this.end = end;
        }

        public ComplexChoiceDataItem(String display, Date start){
            this(display, start, null);
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        


    }
    //定义了简单数据对象
    public class SimpleChoiceDataItem extends BaseChoiceItem{
        String display;
        Object data;
        //boolean selected;


        public SimpleChoiceDataItem(String display){
            this(display, null);
        }
        public SimpleChoiceDataItem(String display, Object data){
            this.display = display;
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        
    }

    public class BaseChoiceItem {
        boolean selected;
        boolean rollover;

        public boolean isRollover() {
            return rollover;
        }

        public void setRollover(boolean rollover) {
            this.rollover = rollover;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        
    }
    
}
