package com.original.service.channel.protocols.email.oldimpl;

import java.util.Date;

public class Utils {
	
	  /**
     * 根据格式的时间字符串，获取时间
     *
     * @param time
     *            - 转换时间字符串，格式为yyyy-MM-dd HH:mm:ss。
     * @return Date
     */
    public static Date getDate(String time) {
        int year = Integer.parseInt(time.substring(0, 4)) - 1900;
        int month = Integer.parseInt(time.substring(5, 7)) - 1;
        int day = Integer.parseInt(time.substring(8, 10));
        int hh = 0;
        int mm = 0;
        int ss = 0;
        if (time.length() >= 19) {
            hh = Integer.parseInt(time.substring(11, 13));
            mm = Integer.parseInt(time.substring(14, 16));
            ss = Integer.parseInt(time.substring(17, 19));
        }
        return new Date(year, month, day, hh, mm, ss);
    }
}
