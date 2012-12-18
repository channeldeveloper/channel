/*
 *  com.original.widget.draw.OriCharacter.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.draw;

/**
 * (Class Annotation.)
 * 该类用于处理字符相关内容
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-26 22:30:33
 */
public class OriCharacter {
    //Chinese character
    public static boolean isChineseCharacter(char ch){
        return (ch>=0x4E00 && ch<=0x9FA5);
    }

    //判断是否含有中文
    public static boolean containChinese(String input){
        boolean ret = false;
        if(input==null) return ret;
        for(char ch: input.toCharArray()){
            if(isChineseCharacter(ch)){
                ret = true;
                break;
            }
        }
        return ret;
    }
}
