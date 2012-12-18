/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.model;

import com.original.widget.date.OriDate;
import java.util.Date;

/**
 * 状态栏对应数据结构
 * @author Changjian Hu
 */
public class StatusBarModel {
    private float battery = 0.6f;
    private float signal = 0.8f;

    public String getTime(){
        return OriDate.formatDate(new Date(), "HH:mm");
    }
    
    public float getBattery() {
        return battery;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public float getSignal() {
        return signal;
    }

    public void setSignal(float signal) {
        this.signal = signal;
    }


}
