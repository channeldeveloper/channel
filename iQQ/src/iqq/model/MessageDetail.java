package iqq.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息明细，包含QQ消息的简短内容，完整内容，以及表情、图片等信息,
 * 是{@link Message}消息内容的补充。
 * @author WMS
 *
 */
public class MessageDetail implements Serializable{
	private static final long serialVersionUID = 3832689743832174114L;
	public static final String FACE_PREFIX = "face_", 
			OFFPIC_PREFIX = "offpic_",
			CFACE_PREFIX = "cface_";
	
	public static final String FACE_NAME = "表情",
			OFFPIC_NAME = "图片",
			CFACE_NAME = "自定义表情";

	private String shortMsg, //简短消息
	completeMsg;//完整消息
	
	private Map<String, String> faces = new HashMap<String, String>(), //表情组
			offpics = new HashMap<String, String>(),//图片组
			cfaces = new HashMap<String, String>();//自定义表情组

	public String getShortMsg() {
		return shortMsg;
	}
	public void setShortMsg(String shortMsg) {
		this.shortMsg = shortMsg;
	}
	
	public String getCompleteMsg() {
		return completeMsg;
	}
	public void setCompleteMsg(String completeMsg) {
		this.completeMsg = completeMsg;
	}
	
	public Map<String, String> getFaces() {
		return faces;
	}	
	public void setFaces(Map<String, String> faces) {
		this.faces = faces;
	}
	public void addFace(int index, String face) {
		this.faces.put(FACE_PREFIX + index, face);
	}
	
	public Map<String, String> getOffpics() {
		return offpics;
	}	
	public void setOffpics(Map<String, String> offpics) {
		this.offpics = offpics;
	}
	public void addOffpic(int index, String offpic) {
		this.offpics.put(OFFPIC_PREFIX + index, offpic);
	}
	
	public Map<String, String> getCfaces() {
		return cfaces;
	}
	public void setCfaces(Map<String, String> cfaces) {
		this.cfaces = cfaces;
	}
	public void addCface(int index, String cface) {
		this.cfaces.put(CFACE_PREFIX + index, cface);
	}
	
	public static String getPrefixOf(String name) {
		if(FACE_NAME.equals(name)) {
			return FACE_PREFIX;
		}
		else if(OFFPIC_NAME.equals(name)) {
			return OFFPIC_PREFIX;
		}
		else if(CFACE_NAME.equals(name)) {
			return CFACE_PREFIX;
		}
		return null;
	}
}
