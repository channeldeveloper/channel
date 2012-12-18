/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.model;

/**
 * 非规则化按钮的数据模型类
 * 主要记录：形状，色彩等，目前不做特别丰富的处理。
 * @author Changjian Hu
 */
public class IrregularButtonModel {
    private boolean rollover;
    private boolean pressed;

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isRollover() {
        return rollover;
    }

    public void setRollover(boolean rollover) {
        this.rollover = rollover;
    }

}
