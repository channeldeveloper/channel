/**
 * com.original.widget.comp.arrowedpanel.model;
 *
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import com.original.widget.date.OriDate;
import com.original.widget.event.TimeAxisChangeEvent;
import com.original.widget.event.TimeAxisChangeListener;
import java.awt.*;
import java.util.Date;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.util.Calendar;

/**
 * The Model behind the TimeAxis Component
 * The model only contains necessary informations.
 *
 * @updator Changjian HU
 * @author Min NI, Xueyong SONG
 * @version 1.00 2012/6/14
 */
public class TimeAxisModel {
    //时间阶段开始时间
    private Date timePeriodStartDate;
    //时间阶段结束时间
    private Date timePeriodEndDate;
    //滑动区域开始时间
    private Date slideAreaStartDate;
    //滑动区域结束时间
    private Date slideAreaAEndDate;
    //时间窗口开始时间
    private Date timeviewStartDate;
    //时间窗口结束时间
    private Date timeviewEndDate;

    //滑动区域在屏幕中的宽度
    private float slideAreaPercent = 0.80f;
    //滑块高度
    private int slideBrickHeight = 37;
    
    //显示字体
    private Font labelFont = new Font("微软雅黑", Font.PLAIN, 12);
    private Font inputFont = new Font("verdana", Font.PLAIN, 12);
    private transient double scalefactor = 12.0;
    //左右Scale
    private transient float lpercent = 0.3f;
    private transient float rpercent = 1.0f;
    private transient long timegranular = 0;
    private transient int choosetimewintype = -1;

    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    //临时用
    
    private static Date now = new Date();

    //简便用的构造函数
    public TimeAxisModel() {
        this(OriDate.getDate("2010-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss"),
                null,
                OriDate.dateAdd(now, Calendar.DATE, -5),
                OriDate.dateAdd(now, Calendar.DATE, 7),
                now,
                OriDate.dateAdd(now, Calendar.DATE, 1)
        );
    }

    //基本构造函数
    public TimeAxisModel(Date timePeriodStartDate, Date timePeriodEndDate,
            Date slideAreaStartDate, Date slideAreaEndDate,
            Date timeviewStartDate, Date timeviewEndDate ){
        this.timePeriodStartDate = timePeriodStartDate;
        this.timePeriodEndDate = timePeriodEndDate;
        this.slideAreaStartDate = slideAreaStartDate;
        this.slideAreaAEndDate = slideAreaEndDate;
        this.timeviewStartDate = timeviewStartDate;
        this.timeviewEndDate = timeviewEndDate;

        calcTimFrameFactors();
        calcTimeWindowScaleType();
    }
    public void reset(){
        Date cur = new Date();
        this.slideAreaStartDate = OriDate.dateAdd(cur, Calendar.MONTH, -1);
        this.slideAreaAEndDate =  OriDate.dateAdd(cur, Calendar.MONTH, 2);
        int hour = 7*24/4;
        this.timeviewStartDate = OriDate.dateAdd(cur, Calendar.HOUR, -hour);
        this.timeviewEndDate = OriDate.dateAdd(cur, Calendar.HOUR, hour*3);
        calcTimFrameFactors();
        calcTimeWindowScaleType();

        //readjustDateVars();
        this.fireStateChanged(TimeAxisChangeEvent.CHANGETYPE.ViewBlockChange);
        
    }
    //获取标尺的刻度标准
    public RulerMeterDef fetchRulerLabelUnit(int type){
        RulerMeterDef ret = null;
        Date start = type==0?slideAreaStartDate:timeviewStartDate;
        Date end = type==0?slideAreaAEndDate:timeviewEndDate;
        calcScaleFactor();
        long hour = OriDate.dateDiff(start,
                end, Calendar.HOUR);
        if(type==1) {
            hour /=this.scalefactor;
            //System.out.println(hour);
        } //; //放大12倍
        boolean oddControlFlag = false;
        if(hour<=9)
            ret = new RulerMeterDef("HH:mm",Calendar.MINUTE, 30, 60, true);
        else if(hour>9 && hour<=16)
            ret = new RulerMeterDef("HH:mm",Calendar.HOUR, 1, 3, true);
        else if(hour>16 && hour<36)
            ret = new RulerMeterDef("HH:mm",Calendar.HOUR, 1, 6, true);
        else if(hour>36 && hour<=5*24)
            ret = new RulerMeterDef("yyyy/MM/dd",Calendar.HOUR, 1, 24, false);
        else if(hour>5*24 && hour<=16*24){
            ret = new RulerMeterDef("yyyy/MM/dd",Calendar.DATE, 1, 1, false);
            oddControlFlag = true;
        }
        else if(hour>16*24 && hour<=35*24)
            ret = new RulerMeterDef("yyyy/MM/dd",Calendar.DATE, 1, 15, false);
        else if(hour>35*24 && hour<=60*24)
            ret = new RulerMeterDef("yyyy/MM/dd",Calendar.DATE, 1, 30, false);
        else if(hour>60*24 && hour<=4*35*24)
            ret = new RulerMeterDef("yyyy/MM",Calendar.DATE, 1, 30, false);
        else if(hour>4*35*24 && hour<=8*35*24)
            ret = new RulerMeterDef("yyyy/MM",Calendar.MONTH, 1, 3, false);
        else if(hour>8*35*24 && hour<=15*35*24)
            ret = new RulerMeterDef("yyyy/MM",Calendar.MONTH, 1, 6, false);
        else if(hour>15*30*24 && hour<=5*12*35*24)
            ret = new RulerMeterDef("yyyy/MM",Calendar.MONTH, 1, 12, false);
        else if(hour>5*12*35*24)
            ret = new RulerMeterDef("yyyy",Calendar.MONTH, 1, 12, false);

        ret.setHourdiff(hour);
        ret.setSblocknum(OriDate.calcBlockNumber(start,
                end, ret.increaseUnit, ret.increaseNumber));
        Date closeDate = OriDate.calcClosedNeatDate(start,
                ret.increaseUnit, ret.increaseNumber, oddControlFlag);
        ret.setBeginDate(closeDate);
        ret.setSblockdiff(OriDate.calcBlockNumber(closeDate, start,
                 ret.increaseUnit, ret.increaseNumber));

        if(type==1){
            if(ret.increaseUnit==Calendar.MINUTE)
                timegranular = 60*1000*ret.seperatorNumber;
            else if(ret.increaseUnit==Calendar.HOUR)
                timegranular = 60*60*1000*ret.seperatorNumber;
            else if(ret.increaseUnit==Calendar.DATE && ret.seperatorNumber==1)
                timegranular = 24*60*60*1000*2;
            else if(ret.increaseUnit==Calendar.DATE)
                timegranular = 24*60*60*1000*ret.seperatorNumber;
            else if(ret.increaseUnit==Calendar.MONTH)
                timegranular = 35*24*60*60*1000*ret.seperatorNumber;
        }
        return ret;
    }
    //根据用户的录入进行日期跳跃，争取保证滑块在滑动区间位置不变
    //
    public void midJumpTo(Date dt){
        Date oldMidDate = OriDate.midDate(this.timeviewStartDate, this.timeviewEndDate);
        int days = (int)OriDate.dateDiff(oldMidDate, dt, Calendar.DATE);
        //判断移动方向
        if(days>0){ //右移
            Date tDate = OriDate.dateAdd(this.slideAreaAEndDate, Calendar.DATE, days);

            if(this.timePeriodEndDate==null){ //未来模式
                moveSlideArea(days);
            }else{
                long diff = OriDate.dateDiff(tDate, this.timePeriodEndDate, Calendar.MINUTE);
                //判断是否越界
                if(diff>=0){ //移动区域仍然在时间阶段内
                    moveSlideArea(days);
                }else{ //移动区域不再时间区间内，只能移动时间区间
                    tDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.DATE, days);
                    long snddiff = OriDate.dateDiff(tDate, this.timePeriodEndDate, Calendar.MINUTE);
                    if(snddiff>=0){ //时间视图在时间阶段内
                        advancedMoveSlideArea(0, 0, days);
                    }else{ //时间视图不在时间阶段内
                        advancedMoveSlideArea(0, 1, days);
                    }                    
                }
            }

        }else if(days<0){ //左移
            Date tDate = OriDate.dateAdd(this.slideAreaStartDate, Calendar.DATE, days);

            long diff = OriDate.dateDiff(this.timePeriodStartDate, tDate,  Calendar.MINUTE);
            //判断是否越界
            if(diff>=0){ //移动区域仍然在时间阶段内
                moveSlideArea(days);
            }else{ //移动区域不再时间区间内，只能移动时间区间
                tDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.DATE, days);
                long snddiff = OriDate.dateDiff(this.timePeriodStartDate, tDate, Calendar.MINUTE);
                if(snddiff>=0){ //时间视图在时间阶段内
                    advancedMoveSlideArea(1, 0, days);
                }else{ //时间视图不在时间阶段内
                    advancedMoveSlideArea(1, 1, days);
                }
            }
        }

        this.fireStateChanged(TimeAxisChangeEvent.CHANGETYPE.ViewBlockChange);
    }
    //调整移动区间
    private void advancedMoveSlideArea(int direction, int type,int days){
        if(direction==0){ //向右靠向时间阶段
            int diff = (int)OriDate.dateDiff(this.slideAreaStartDate, this.slideAreaAEndDate, Calendar.MINUTE);
            this.slideAreaAEndDate = this.timePeriodEndDate;
            this.slideAreaStartDate = OriDate.dateAdd(this.slideAreaAEndDate, Calendar.MINUTE, -diff);

            if(type==0){ //移动后时间视图在时间阶段内
                this.timeviewStartDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.DATE, days);
                this.timeviewEndDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.DATE, days);
            }else if(type==1){ //移动后，时间视图超出时间阶段
                diff = (int)OriDate.dateDiff(this.timeviewStartDate, this.timeviewEndDate, Calendar.MINUTE);
                this.timeviewEndDate = this.timePeriodEndDate;
                this.timeviewStartDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.MINUTE, -diff);
            }
        }else if(direction==1){ //向左逼近时间阶段
            int diff = (int)OriDate.dateDiff(this.slideAreaStartDate, this.slideAreaAEndDate, Calendar.MINUTE);
            this.slideAreaStartDate = this.timePeriodStartDate;
            this.slideAreaAEndDate = OriDate.dateAdd(this.slideAreaStartDate, Calendar.MINUTE, diff);

            if(type==0){ //移动后时间视图依然在时间阶段内
                this.timeviewStartDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.DATE, days);
                this.timeviewEndDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.DATE, days);
            }else if(type==1){
                diff = (int)OriDate.dateDiff(this.timeviewStartDate, this.timeviewEndDate, Calendar.MINUTE);
                this.timeviewStartDate = this.timePeriodStartDate;
                this.timeviewEndDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.MINUTE, diff);
            }
            
        }
    }
    //同步移动时间大区间
    private void moveSlideArea(int days){
        this.timeviewStartDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.DATE, days);
        this.timeviewEndDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.DATE, days);
        this.slideAreaStartDate = OriDate.dateAdd(this.slideAreaStartDate, Calendar.DATE, days);
        this.slideAreaAEndDate = OriDate.dateAdd(this.slideAreaAEndDate, Calendar.DATE, days);
    }
    //移动
    //@changjian fixed the bug when drag the time line. at 12：36 06-15/2012
    public void moveTimeAxix(int quota, int mode){
        if(mode==0){ //移动滑块
            this.timeviewStartDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.MINUTE, quota);
            this.timeviewEndDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.MINUTE, quota);
        }else if(mode==1){ //移动时间轴
            if(quota>0){ //右移
                Date dt = OriDate.dateAdd(this.slideAreaStartDate, Calendar.MINUTE, -quota);
                if(OriDate.dateDiff(dt, this.timePeriodStartDate, Calendar.MINUTE)>0){ //移出时间阶段
                    return;
                }
            }else{ //左移
                Date dt = OriDate.dateAdd(this.slideAreaAEndDate, Calendar.MINUTE, -quota);
                if(timePeriodEndDate!=null && OriDate.dateDiff(this.timePeriodEndDate, dt, Calendar.MINUTE)>0){ //移出时间阶段
                    return;
                }
            }
            this.slideAreaStartDate = OriDate.dateAdd(this.slideAreaStartDate, Calendar.MINUTE, -quota);
            this.slideAreaAEndDate = OriDate.dateAdd(this.slideAreaAEndDate, Calendar.MINUTE, -quota);
            calcTimFrameFactors();
        }
        
        this.fireStateChanged(TimeAxisChangeEvent.CHANGETYPE.TimeViewChange);
    }
    //缩放时间轴
    public void scaleTimeAxis(int quota, int mode){
        if(mode==2){ //右边处理
            this.timeviewEndDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.MINUTE, quota);
            this.timeviewEndDate = OriDate.validTimeView(this.timeviewStartDate, this.timeviewEndDate);
            this.slideAreaAEndDate = OriDate.calcAreaEndDate(this.timeviewStartDate, this.timeviewEndDate,
                    this.slideAreaStartDate, 0);
            //System.out.println(this.slideAreaAEndDate +":" + this.slideAreaStartDate);
            //System.out.println(this.timeviewEndDate +":" + this.timeviewStartDate);
            
        }else if(mode==3){ //左边处理
            this.timeviewStartDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.MINUTE, quota);
            this.timeviewEndDate = OriDate.validTimeView(this.timeviewStartDate, this.timeviewEndDate);
            this.slideAreaStartDate = OriDate.calcAreaEndDate(this.timeviewStartDate, this.timeviewEndDate,
                    this.slideAreaAEndDate, 1);
        }
        //如果时间区间和时间滑块交叉，那么将时间滑块移动到时间区间中。
        calcTimeWindowScaleType();
        readjustDateVars();
        this.fireStateChanged(TimeAxisChangeEvent.CHANGETYPE.ViewBlockChange);
    }
    //readjust the relevant dates;
    protected void readjustDateVars(){
        if(OriDate.dateDiff(this.timeviewEndDate, this.slideAreaAEndDate, Calendar.MINUTE)<0){
            int hour = (int) OriDate.dateDiff(this.timeviewStartDate, this.timeviewEndDate, Calendar.HOUR);
            this.timeviewEndDate = this.slideAreaAEndDate;
            this.timeviewStartDate = OriDate.dateAdd(this.timeviewEndDate, Calendar.HOUR, -hour);
        }else if(OriDate.dateDiff(this.slideAreaStartDate, this.timeviewStartDate, Calendar.MINUTE)<0){
            int hour = (int) OriDate.dateDiff(this.timeviewStartDate, this.timeviewEndDate, Calendar.HOUR);
            this.timeviewStartDate = this.slideAreaStartDate;
            this.timeviewEndDate = OriDate.dateAdd(this.timeviewStartDate, Calendar.HOUR, hour);
        }
        guarenteeTimeFrame();
        calcTimFrameFactors();
    }
    //如果当前时间窗口超过了时间阶段，进行调整
    protected void guarenteeTimeFrame(){

        if(this.timePeriodEndDate!=null){ //向右缩放可能出现问题-出界情况
            if(OriDate.dateDiff(this.timePeriodEndDate, this.slideAreaAEndDate, Calendar.MINUTE)>0){ //右出界
                this.slideAreaAEndDate = this.timePeriodEndDate;
                this.timeviewEndDate = this.slideAreaAEndDate; //调整左边内容
                this.timeviewStartDate = OriDate.calcViewDate(this.slideAreaStartDate,
                        this.slideAreaAEndDate, this.timeviewEndDate, 1);
            }
         }
        //左出界可能 向左缩放
        if(OriDate.dateDiff(this.slideAreaStartDate, this.timePeriodStartDate,  Calendar.MINUTE)>0){ //左出界
                this.slideAreaStartDate = this.timePeriodStartDate;
                this.timeviewStartDate = this.slideAreaStartDate; //调整左边内容
                this.timeviewEndDate = OriDate.calcViewDate(this.slideAreaStartDate,
                        this.slideAreaAEndDate, this.timeviewStartDate, 0);
            }
    }
    

    //switch view window - month/week/day
    public void switchTimeView(String viewname){
        int days = -1;
        if(viewname.equals("month")){
            this.choosetimewintype = 0;
            days = 35;
        }else if(viewname.equals("week")){
            this.choosetimewintype = 1;
            days = 7;
        }else{
            this.choosetimewintype = 2;
            days = 1;
        }
        if(days!=-1)
        this.timeviewEndDate = OriDate.dateAdd(this.timeviewStartDate,
                    Calendar.DATE,days);
        this.slideAreaAEndDate = OriDate.calcAreaEndDate(this.timeviewStartDate,
                this.timeviewEndDate,
                    this.slideAreaStartDate, 0);
        readjustDateVars();
        //calcTimeWindowScaleType();
        this.fireStateChanged(TimeAxisChangeEvent.CHANGETYPE.ViewBlockChange);
    }
    //计算放大倍数
    private void calcScaleFactor(){
        long diff_block = OriDate.dateDiff(this.slideAreaStartDate,
                this.slideAreaAEndDate, Calendar.MINUTE);
        long diff_view = OriDate.dateDiff(this.timeviewStartDate,
                this.timeviewEndDate, Calendar.MINUTE);
        this.scalefactor = 1.0* diff_block/diff_view;
        this.scalefactor /=4;
        //System.out.println(scalefactor);
    }
    //计算比率
    private void calcTimFrameFactors(){
        if(this.timePeriodEndDate==null){
            this.rpercent = 1.0f;
            this.lpercent = OriDate.calcFrameFactors(this.timePeriodStartDate,
                    OriDate.dateAdd(this.timePeriodStartDate, Calendar.YEAR, 10),
                    this.slideAreaStartDate, 0);
        }else{
            this.lpercent = OriDate.calcFrameFactors(this.timePeriodStartDate,
                    this.timePeriodEndDate, this.slideAreaStartDate, 0);
            this.rpercent = OriDate.calcFrameFactors(this.timePeriodStartDate,
                    this.timePeriodEndDate, this.slideAreaAEndDate, 1);
            //if(this.lpercent==0.0f)
            //    this.rpercent = 1.0f;
            //if(this.rpercent==0.0f)
            //    this.lpercent = 1.0f;
        }
        
        //System.out.println(this.lpercent +":" + this.rpercent);
    }
    //计算时间窗口的比率
    private void calcTimeWindowScaleType(){
        this.choosetimewintype = -1;
        long hour = OriDate.dateDiff(this.timeviewStartDate, this.timeviewEndDate,
                Calendar.HOUR);
        if(hour==24){ //一天
            this.choosetimewintype = 2;
        }else if(hour==24*7){ //周
            this.choosetimewintype = 1;
        }else if(hour == 24*35){ //月
            this.choosetimewintype = 0;
        }
    }

    //-----------setter -- and getter ---------------------------------
    public Font getLabelFont() {
        return labelFont;
    }
    public Font getInputSFont(){
        return inputFont;
    }
    public void setLabelFont(Font labelFont) {
        this.labelFont = labelFont;
    }

    public Date getSlideAreaAEndDate() {
        return slideAreaAEndDate;
    }

    public int getChoosetimewintype() {
        return choosetimewintype;
    }

    public void setChoosetimewintype(int choosetimewintype) {
        this.choosetimewintype = choosetimewintype;
    }

    public long getTimegranular() {
        return timegranular;
    }

    public void setTimegranular(long timegranular) {
        this.timegranular = timegranular;
    }
    
    

    public void setSlideAreaAEndDate(Date slideAreaAEndDate) {
        this.slideAreaAEndDate = slideAreaAEndDate;
    }

    public float getSlideAreaPercent() {
        return slideAreaPercent;
    }

    public void setSlideAreaPercent(float slideAreaPercent) {
        this.slideAreaPercent = slideAreaPercent;
    }

    public Date getSlideAreaStartDate() {
        return slideAreaStartDate;
    }

    public void setSlideAreaStartDate(Date slideAreaStartDate) {
        this.slideAreaStartDate = slideAreaStartDate;
    }

    public int getSlideBrickHeight() {
        return slideBrickHeight;
    }

    public void setSlideBrickHeight(int slideBrickHeight) {
        this.slideBrickHeight = slideBrickHeight;
    }

    public Date getTimePeriodEndDate() {
        return timePeriodEndDate;
    }

    public void setTimePeriodEndDate(Date timePeriodEndDate) {
        this.timePeriodEndDate = timePeriodEndDate;
    }

    public Date getTimePeriodStartDate() {
        return timePeriodStartDate;
    }

    public void setTimePeriodStartDate(Date timePeriodStartDate) {
        this.timePeriodStartDate = timePeriodStartDate;
    }

    public Date getTimeviewEndDate() {
        return timeviewEndDate;
    }

    public void setTimeviewEndDate(Date timeviewEndDate) {
        this.timeviewEndDate = timeviewEndDate;
    }

    public Date getTimeviewStartDate() {
        return timeviewStartDate;
    }

    public void setTimeviewStartDate(Date timeviewStartDate) {
        this.timeviewStartDate = timeviewStartDate;
    }

    public float getLpercent() {
        return lpercent;
    }

    public void setLpercent(float lpercent) {
        this.lpercent = lpercent;
    }

    public float getRpercent() {
        return rpercent;
    }

    public void setRpercent(float rpercent) {
        this.rpercent = rpercent;
    }


    //-----------setter -- and getter ---------------------------------

    //Event Attachment and relevant processing
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }
    public void onDateChanged(Date startDate, Date endDate) {
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(
                ChangeListener.class);
    }
    //Changjian Hu
    protected void fireStateChanged(TimeAxisChangeEvent.CHANGETYPE type) {
         Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) { 
                TimeAxisChangeEvent e = new TimeAxisChangeEvent(this, type);
                ((ChangeListener)listeners[i+1]).stateChanged(e);
            }
        }
    }


    //inner classes
    //--一些逻辑内嵌到模型中
    //刻度绘制格式
    public class RulerMeterDef{
        public String formatPattern; //长刻度对应的日期处理格式
        public int increaseUnit; //短刻度对应的类型
        public int increaseNumber; //段刻度增加的数量
        public boolean drawZeroHour;
        public int seperatorNumber;

        private double sblocknum;
        private long hourdiff;
        private Date beginDate;
        private double sblockdiff;

        public Date getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(Date beginDate) {
            this.beginDate = beginDate;
        }

        public double getSblockdiff() {
            return sblockdiff;
        }

        public void setSblockdiff(double sblockdiff) {
            this.sblockdiff = sblockdiff;
        }
        
        public long getHourdiff() {
            return hourdiff;
        }
        public void setHourdiff(long hourdiff) {
            this.hourdiff = hourdiff;
        }
        public double getSblocknum() {
            return sblocknum;
        }
        public void setSblocknum(double sblocknum) {
            this.sblocknum = sblocknum;
        }

        public RulerMeterDef(String formatPattern, int increaseUnit,
                int increaseNumber, int seperatorNumber){
            this(formatPattern, increaseUnit, increaseNumber, seperatorNumber,
                    false);
        }
        public RulerMeterDef(String formatPattern, int increaseUnit,
                int increaseNumber, int seperatorNumber, boolean drawZeroHour){
            this.formatPattern = formatPattern;
            this.increaseUnit = increaseUnit;
            this.increaseNumber = increaseNumber;
            this.seperatorNumber = seperatorNumber;
            this.drawZeroHour = drawZeroHour;
        }
    }

}
