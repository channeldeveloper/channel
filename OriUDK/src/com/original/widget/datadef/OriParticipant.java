/*
 *  com.original.xui.component.OriParticipant.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.datadef;

import com.original.widget.datadef.OriObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (Class Annotation.)
 *  地址簿等对应内容
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-7-14 0:15:04
 */
public class OriParticipant extends OriObject {
    String display;
    Object obj;
    boolean flag;

    public OriParticipant(String display, Object obj){
        this.display = display;
        this.obj = obj;
        this.flag = true;
    }
    public OriParticipant(String display){
        this.display = display;
        this.flag = false;
    }

    @Override
    public boolean isComplex() {
        return flag;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public String toString(){
        return String.format("<%s>%s", display, obj.toString());
    }

    //将一个字符串解析成为一组Participant对象
    public static List<OriObject> parseParticipant(String sInput){
        List<OriObject> ret = new ArrayList<OriObject>();
        String arr[] = sInput.split(",");
        for(String single: arr){
            Map<String, Object> itm = parseSingle(single);
            if(itm.size()==1){ //simple
                ret.add(new OriParticipant((String)itm.get("title")));
            }else{
                 ret.add(new OriParticipant((String)itm.get("title"),
                        itm.get("address") )
                       );
            }
        }
        return ret;
    }

    public static OriObject parseSingleParticipant(String sInput){
        Map<String, Object> itm = parseSingle(sInput);
        if(itm.size()==1){ //simple
             return new OriParticipant((String)itm.get("title"));
        }else{
             return new OriParticipant((String)itm.get("title"),
                    itm.get("address") )
                   ;
        }       
    }

    //将一个特定的字符串分解对应内容
    private static Map<String, Object> parseSingle(String input){
        Map<String, Object> ret = new HashMap<String, Object> ();
        Pattern pattern = Pattern.compile("<([^>]*)>(.*)");
		Matcher matcher = pattern.matcher(input);
		// Check all occurance
		if (matcher.find()) {
            ret.put("title", matcher.group(1));
            ret.put("address", matcher.group(2));
			
		}else{
            ret.put("title", input);
        }

        return ret;
    }  

}
