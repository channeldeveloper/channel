/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.event;

import java.util.EventObject;

/**
 *
 * @author A
 */
public class GranularListSelectionEvent extends EventObject {
    private int index;
    private int subindex;
    private boolean isAdjusting;

    public GranularListSelectionEvent(Object source, int index, int subindex,
            boolean isAdjusting){
        super(source);
        this.index = index;
        this.subindex = subindex;
        this.isAdjusting =isAdjusting;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSubindex() {
        return subindex;
    }

    public void setSubindex(int subindex) {
        this.subindex = subindex;
    }

    public boolean isIsAdjusting() {
        return isAdjusting;
    }

    public void setIsAdjusting(boolean isAdjusting) {
        this.isAdjusting = isAdjusting;
    }
}
