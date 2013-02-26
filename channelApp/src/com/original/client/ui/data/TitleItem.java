package com.original.client.ui.data;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

import com.original.client.util.ChannelConstants;

public class TitleItem implements Serializable, ChannelConstants
{
	private static final long serialVersionUID = -3667332471357682289L;
	
	private String title = null; //标题内容
	private String fontFamily = DEFAULT_FONT_FAMILY; //字体名称
	private int fontSize = DEFAULT_FONT_SIZE;//字体大小
	private boolean isBold = false;//是否加粗
	
	private Color color = LIGHT_TEXT_COLOR; //字体颜色
	private int value = 0;//用于统计
	
	public TitleItem(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public TitleItem setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getFontFamily() {
		return fontFamily;
	}
	public TitleItem setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		return this;
	}

	public int getFontSize() {
		return fontSize;
	}
	public TitleItem setFontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public boolean isBold() {
		return isBold;
	}
	public TitleItem setBold(boolean isBold) {
		this.isBold = isBold;
		return this;
	}

	public Color getColor() {
		return color;
	}
	public TitleItem setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void addValue(int value) {
		this.value += value;
	}

	public Font getFont() {
		Font font = new Font(fontFamily, isBold ? Font.BOLD : Font.PLAIN,
				fontSize);
		return font;
	}
}
