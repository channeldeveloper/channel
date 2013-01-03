package com.original.service.channel.protocols.im.iqq;

import iqq.model.MessageDetail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.original.service.channel.ChannelMessage;

/**
 * QQ消息处理类
 * @author WMS
 *
 */
public class QQParser
{
	/**
	 * 处理消息，即显示QQ消息的完整内容，包括表情、图片等。
	 * @param msg
	 * @return
	 */
	public static String parseMessage(ChannelMessage msg)
	{
		String shortMsg = null;
		if(msg != null && (shortMsg = msg.getShortMsg()) != null)
		{
			Map<String, LinkedList<String>> exts = sort(msg.getExtensions());
			if(exts != null) {
				LinkedList<String> faces = exts.get(MessageDetail.FACE_PREFIX), //表情
						offpics = exts.get(MessageDetail.OFFPIC_PREFIX),//图片
						cfaces = exts.get(MessageDetail.CFACE_PREFIX);//自定义表情
				
				Matcher matcher = Pattern.compile("\\[(" + MessageDetail.FACE_NAME + ")\\]|" +
						"\\[(" + MessageDetail.OFFPIC_NAME + ")\\]|" +
								"\\[(" + MessageDetail.CFACE_NAME + ")\\]").matcher(shortMsg);
				
				String name = null, uri = null; //图片、表情等对应的文件URI
				while(matcher.find()) {
					for(int i=1; i<=3; i++) {
						name = matcher.group(i);
						if(name == null) continue;
						
						if(MessageDetail.FACE_NAME.equals(name) && (uri = faces.poll()) != null) {
							shortMsg = shortMsg.replaceFirst("\\[" + name + "\\]", uri);
						}
						else if(MessageDetail.OFFPIC_NAME.equals(name) && (uri = offpics.poll()) != null) {
							shortMsg = shortMsg.replaceFirst("\\[" + name + "\\]", uri);
						}
						else if(MessageDetail.CFACE_NAME.equals(name) && (uri = cfaces.poll()) != null) {
							shortMsg = shortMsg.replaceFirst("\\[" + name + "\\]", uri);
						}
					}
				}
				shortMsg = shortMsg.replaceAll("\\r|\\n", "<br>");
			}
		}
		return shortMsg;
	}
	
	/**
	 * 将QQ表情、图片、自定义表情按类型进行汇总。
	 * @param exts
	 * @return
	 */
	public static Map<String, LinkedList<String>> sort(HashMap<String, String> exts)
	{
		if(exts != null && !exts.isEmpty()) {
			Map<String, LinkedList<String>> map = new HashMap<String, LinkedList<String>>();
			TreeMap<String, String> sortExts = new TreeMap<String, String>(exts); //先按Key进行顺序排序(因为表情是按顺序添加的，图片等也是如此)
			
			for(Entry<String, String> entry : sortExts.entrySet())
			{
				String key = entry.getKey();
				String prefix = null;
				if(key.contains(MessageDetail.FACE_PREFIX)) {
					prefix = MessageDetail.FACE_PREFIX;
				}
				else if(key.contains(MessageDetail.OFFPIC_PREFIX)) {
					prefix = MessageDetail.OFFPIC_PREFIX;
				}
				else if(key.contains(MessageDetail.CFACE_PREFIX)) {
					prefix = MessageDetail.CFACE_PREFIX;
				}
				
				if(prefix == null) continue;
				LinkedList<String> values = map.get(prefix);
				if(values == null) {
					values = new LinkedList<String>();
				}
				
				values.add(entry.getValue());
				map.put(prefix, values);
			}
			
			if(map.isEmpty()) {
				map = null;
			}
			return map;
		}
		return null;
	}
	
	
}
