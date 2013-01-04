package com.original.serive.channel.ui.data;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.ChannelUtil;

/**
 * 字体样式属性，分为全局和非全局样式设定。全局样式即整个文本都是该样式，非全局样式即用户选择后的文本所设定的样式。
 * 由<code>overall</code>来区分，也可以切换。
 * @author WMS
 *
 */
public class FontStyle implements Serializable, Cloneable, ChannelConstants
{
	private static final long serialVersionUID = -4413622380964683144L;
	
	private String fontFamily = DEFAULT_FONT_FAMILY;
	private int fontSize = DEFAULT_FONT_SIZE;
	private Color color = Color.black; //黑色
	
	private boolean isBold = false,
			isItalic = false,
			isUnderLine = false;
	
	/** 字体样式属性 */	
	public static final Object[] STYLES = {StyleConstants.FontFamily,
		StyleConstants.FontSize,
		StyleConstants.Foreground,
		StyleConstants.Bold,
		StyleConstants.Italic,
		StyleConstants.Underline};
	
	private transient JTextPane editor = null; //文本编辑器
	private boolean overall = false; //是否全局样式，如对于QQ，则是全局样式。即一旦添加某个StyleAttribute，则所有的文本都有该属性
	private SimpleAttributeSet overallAttribute = null;//全局样式，当overall==true时有效
	
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		if(fontFamily != null && !fontFamily.equals(this.fontFamily)) {
			this.fontFamily = fontFamily;
			updateFontStyle(StyleConstants.FontFamily);
		}
	}

	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		if(fontSize != this.fontSize) {
			this.fontSize = fontSize;
			updateFontStyle(StyleConstants.FontSize);
		}
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		if(color != null && !color.equals(this.color)) {
			this.color = color;
			updateFontStyle(StyleConstants.Foreground);
		}
	}

	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		if(isBold != this.isBold) {
			this.isBold = isBold;
			updateFontStyle(StyleConstants.Bold);
		}
	}

	public boolean isItalic() {
		return isItalic;
	}
	public void setItalic(boolean isItalic) {
		if(isItalic != this.isItalic) {
			this.isItalic = isItalic;
			updateFontStyle(StyleConstants.Italic);
		}
	}

	public boolean isUnderLine() {
		return isUnderLine;
	}
	public void setUnderLine(boolean isUnderLine) {
		if(isUnderLine != this.isUnderLine) {
			this.isUnderLine = isUnderLine;
			updateFontStyle(StyleConstants.Underline);
		}
	}
	
	public boolean isOverall() {
		return overall;
	}
	public void setOverall(boolean overall) {
		this.overall = overall;
		if(overall && overallAttribute == null) {
			overallAttribute = new SimpleAttributeSet();
		}
	}
	
	public JTextPane getEditor() {
		return editor;
	}
	public void setEditor(JTextPane editor) {
		this.editor = editor;
	}
	
	/**
	 * 更新编辑器字体样式
	 * @param fontStyleAttri 字体样式属性
	 */
	private void updateFontStyle(Object fontStyleAttri)
	{
		if(editor != null && fontStyleAttri != null) {
			MutableAttributeSet attri = null;
			
			if(overall) { //如果是全局样式，则必须全部选中
				editor.select(0, editor.getDocument().getLength());
				attri = overallAttribute;
			}
			
			if(attri == null) {
				attri = new SimpleAttributeSet();
			}
			
			if(StyleConstants.FontFamily == fontStyleAttri) {
				StyleConstants.setFontFamily(attri, fontFamily);
			}
			else if(StyleConstants.FontSize == fontStyleAttri) {
				StyleConstants.setFontSize(attri, fontSize);
			}
			else if(StyleConstants.Bold == fontStyleAttri) {
				StyleConstants.setBold(attri, isBold);
			}
			else if(StyleConstants.Italic == fontStyleAttri) {
				StyleConstants.setItalic(attri, isItalic);
			}
			else if(StyleConstants.Underline == fontStyleAttri) {
				StyleConstants.setUnderline(attri, isUnderLine);
			}
			else if(StyleConstants.Foreground == fontStyleAttri) {
				StyleConstants.setForeground(attri, color);
			}
			
//			editor.setCharacterAttributes(attri, overall);
			editor.setCharacterAttributes(attri, false);
		}
	}
	
	/**
	 * 获取属性名对应的属性值
	 * @param styleName
	 * @return
	 */
	public Object getFontStyleAttri(Object styleName)
	{
		if(StyleConstants.FontFamily == styleName)
			return fontFamily;
		else if(StyleConstants.FontSize == styleName)
			return fontSize;
		else if(StyleConstants.Foreground == styleName)
			return color;
		else if(StyleConstants.Bold == styleName)
			return isBold;
		else if(StyleConstants.Italic == styleName)
			return isItalic;
		else if(StyleConstants.Underline == styleName)
			return isUnderLine;
		
		return null;
	}
	
	public void setFontStyleAttri(Object styleName, Object styleValue)
	{
		if(StyleConstants.FontFamily == styleName)
			fontFamily =  styleValue == null ? DEFAULT_FONT_FAMILY : (String)styleValue;
		else if(StyleConstants.FontSize == styleName)
			fontSize = styleValue == null ? DEFAULT_FONT_SIZE : ((Integer)styleValue).intValue();
		else if(StyleConstants.Foreground == styleName)
			color = styleValue == null ? Color.black :  (Color)styleValue;
		else if(StyleConstants.Bold == styleName)
			isBold = styleValue == null ? false : ((Boolean)styleValue).booleanValue();
		else if(StyleConstants.Italic == styleName)
			isItalic = styleValue == null ? false : ((Boolean)styleValue).booleanValue();
		else if(StyleConstants.Underline == styleName)
			isUnderLine = styleValue == null ? false : ((Boolean)styleValue).booleanValue();
		
	}
	
	public SimpleAttributeSet convertToAttributeSet() 
	{
		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.FontFamily, fontFamily);
		sas.addAttribute(StyleConstants.FontSize, fontSize);
		sas.addAttribute(StyleConstants.Foreground, color);
		sas.addAttribute(StyleConstants.Bold, isBold);
		sas.addAttribute(StyleConstants.Italic, isItalic);
		sas.addAttribute(StyleConstants.Underline, isUnderLine);
		return sas;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		else if(obj instanceof FontStyle) {
			FontStyle fs = (FontStyle)obj;
			return ChannelUtil.isEqual(fontFamily, fs.getFontFamily())
					&& ChannelUtil.isEqual(fontSize, fs.getFontSize())
					&& ChannelUtil.isEqual(color, fs.getColor())
					&& ChannelUtil.isEqual(isBold, fs.isBold())
					&& ChannelUtil.isEqual(isItalic, fs.isItalic())
					&& ChannelUtil.isEqual(isUnderLine, fs.isUnderLine());
		}
		return false;
	}
	
	//下面是一些颜色转换方法：
	public static String convertColor(Color color)
	{
		return convertColor(null, color);
	}
	public static String convertColor(String prefix, Color color)
	{
		return convertColor(prefix, color.getRed(), 
				color.getGreen(), 
				color.getBlue());
	}
	public static String convertColor(String prefix, int r, int g, int b)//prefix通常为#
	{
		return (prefix == null ? "" : prefix) + 
				convert2HexValue(r) +
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
	
	@Override
	public FontStyle clone() {
		FontStyle fs = null;
		try {
			fs = (FontStyle)super.clone();
		}
		catch(CloneNotSupportedException ex) {
			fs = new FontStyle();
		}
		return fs;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		return json.put("family", fontFamily)
				.put("size", fontSize)
				.put("color", convertColor(color))
				.put("bold", isBold)
				.put("italic", isItalic)
				.put("underline", isUnderLine);
	}
	
	public String toJSONString() throws JSONException {
		return toJSON().toString();
	}
}
