/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 做所有日期相关的运算
 * @author Changjian Hu
 */
public class OriDate {
    private static long HOURINMILLISECONDS = 1000*60*60;
    private static long MINUTEINMILLISECONDS = 1000*60;
    //指定日期内容，返回对应的日期
    public static Date getDate(int year, int month, int day, int hour, int minute, int second){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(year, month, day, hour, minute, second);
        return cal.getTime();
    }
    //根据对应的字符串和对应的匹配模式，获取对应的日期,如果不正常则返回空
    public static Date getDate(String sDateString, String sDatePattern){
        Date ret = null;
        SimpleDateFormat format = new SimpleDateFormat(sDatePattern, Locale.US);
        try {
            ret = format.parse(sDateString);
        } catch (ParseException ex) {
            
        }
        return ret;
    }

    //给定日期进行计算对应的日期
    //其中Type - Caledar.Date， Montth, Date, etc.
    public static Date dateAdd(Date dat, int type, int number){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(dat);
        cal.add(type, number);
        return cal.getTime();
    }

    //根据日期获得对应的字符串
    public static String formatDate(Date dat, String formatPattern){
        SimpleDateFormat format = new SimpleDateFormat(formatPattern, Locale.US);
        return format.format(dat);
    }

    //计算两个日期之间的之差
    //小时 分钟 （天以上不予计算)
    public static long dateDiff(Date dat1, Date dat2, int type){
        long diff = dat2.getTime() - dat1.getTime();
        long ret = 0;
        if(type==Calendar.HOUR)
            ret = diff/HOURINMILLISECONDS;
        else if(type==Calendar.MINUTE)
            ret = diff/MINUTEINMILLISECONDS;
        else if(type==Calendar.DATE){
            ret = diff/(HOURINMILLISECONDS*24);
        }
        else{
            ret = -1;
        }
        return ret;
    }
    //计算两个时间的中间时间
    public static Date midDate(Date dat1, Date dat2){
        long diff = dat2.getTime()-dat1.getTime();
        diff /= 2;

        return dateAdd(dat1, Calendar.MILLISECOND, (int)diff);
    }
    //计算两个日期按对应最小间隔划分的个数
    public static double calcBlockNumber(Date dat1, Date dat2, int type, int number){
        long diff = dat2.getTime() - dat1.getTime();
        double ret = 0;
        if(type==Calendar.MINUTE){
            ret = 1.0*diff/MINUTEINMILLISECONDS;
        }else if(type==Calendar.HOUR){
            ret = 1.0*diff/HOURINMILLISECONDS;
        }else if(type==Calendar.DATE)
            ret = 1.0*diff/(24*HOURINMILLISECONDS);
        else if(type==Calendar.MONTH)
            ret = 1.0*diff/(30*24*HOURINMILLISECONDS);
        ret /=number;
        return ret;
    }

    //获取对应的开始日期以便绘制
    public static Date calcClosedNeatDate(Date dat, int type, int number,
            boolean oddControlFlag){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(dat);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        if(type==Calendar.DATE){
            if(oddControlFlag){
                int day = cal.get(Calendar.DATE);
                if(day%2==0){
                    cal.add(Calendar.DATE, -1);
                }
            }
        }
        else if(type==Calendar.MONTH){
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DATE, 1);
        }
        return cal.getTime();
    }
    //给定增加量和相关数据获取日期显示内容
    public static String formatIncreaseDate(Date dt, int type, int number, String format){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(dt);
        cal.add(type, number);
        return formatDate(cal.getTime(), format);
    }

    //获取迁移日期
    public static Date getShiftDate(Date dt, int type, int number){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(dt);
        cal.add(type, number);
        return cal.getTime();
    }
    //判断当前时间是否是零点
    public static boolean isZeroHour(Date dt){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(dt);
        if(cal.get(Calendar.HOUR_OF_DAY)==0)
            return true;
        else
            return false;
    }
    //获取日期的天数
    public static int getDatePart(Date dt, int type){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(dt);
        return cal.get(type);
    }
    //测试是否是小时的整倍数
    public static Date validTimeView(Date dt1, Date dt2){
        long minutes = dateDiff(dt1, dt2, Calendar.MINUTE);
        long hours = minutes/60;
        if(hours<=0) hours = 2;
        if(hours>=24*35*12) hours = 24*35*12;
        return dateAdd(dt1, Calendar.HOUR, (int)hours);
    }
    //计算特定的终止时间
    public static Date calcAreaEndDate(Date dt1, Date dt2, Date dt, int mode){
        long diff = dateDiff(dt1, dt2, Calendar.HOUR);
        diff *=12;
        if(mode==0)
            return dateAdd(dt, Calendar.HOUR, (int) diff);
        else
            return dateAdd(dt, Calendar.HOUR, (int)(-diff));
    }
    //计算视图窗口对应的时间
    public static Date calcViewDate(Date dt1, Date dt2, Date dt, int mode){
        long diff = dateDiff(dt1, dt2, Calendar.HOUR);
        diff /=12;
        if(mode==0)
            return dateAdd(dt, Calendar.HOUR, (int) diff);
        else
            return dateAdd(dt, Calendar.HOUR, (int)(-diff));
    }

    //计算尺寸
    public static float calcFrameFactors(Date dt1, Date dt2, Date dt, int mode){
        long diff_all = dateDiff(dt1, dt2, Calendar.HOUR);
        long diff = 0;
        if(mode==0)
            diff = dateDiff(dt1, dt, Calendar.HOUR);
        else
            diff = dateDiff(dt, dt2, Calendar.HOUR);
        return diff*1.0f/diff_all;

    }
}
