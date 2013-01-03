package iqq.model;

import java.awt.Color;
import java.io.Serializable;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

/**
 * QQ消息的默认样式
 * @author WMS
 *
 */
public class MessageStyle implements Serializable
{
	private static final long serialVersionUID = -5460012742679157400L;
	
	private String fontName = "宋体",
			color = "000000"; //黑色，具体的颜色值需要转换下
	
	private int fontSize = 10;
	private boolean isBold = false,
			isItalic = false,
			isUnderline = false;
	
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	
	public boolean isItalic() {
		return isItalic;
	}
	public void setItalic(boolean isItalic) {
		this.isItalic = isItalic;
	}
	
	public boolean isUnderline() {
		return isUnderline;
	}
	public void setUnderline(boolean isUnderline) {
		this.isUnderline = isUnderline;
	}
	
	public static String convertColor(Color color)
	{
		return convertColor(color.getRed(), 
				color.getGreen(), 
				color.getBlue());
	}
	
	public static String convertColor(int r, int g, int b)
	{
		return convert2HexValue(r) +
				convert2HexValue(g) + 
				convert2HexValue(b);
	}
	
	private static String convert2HexValue(int decValue)
	{
		if(decValue > 255) decValue =255;
		if(decValue < 0) decValue = 0;
		
		String hexValue = Integer.toHexString(decValue);
		if(hexValue.length() < 2)
			hexValue = "0" + hexValue;
		return hexValue;
	}
	
	public static MessageStyle convert(JSONObject json)
	{
		MessageStyle ms = new MessageStyle();
		if(json == null) 
			return ms;
		
		try {
			ms.setFontName(json.getString("family"));
			ms.setFontSize(json.getInt("size"));
			ms.setColor(json.getString("color"));
			ms.setBold(json.getBoolean("bold"));
			ms.setUnderline(json.getBoolean("underline"));
			ms.setItalic(json.getBoolean("italic"));
		}
		catch(JSONException ex) {

		}
		return ms;
	}
}
