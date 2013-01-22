package com.original.serive.channel.comp;

import java.awt.Color;

import com.original.widget.OScrollBar;

public class CScrollBar extends OScrollBar {
	private boolean isScrollBarVisible = false; // 滚动条是否可见
	private int scrollBarValue = 0, scrollBarAmount = 0;// 滚动条当前值和当前可见长度

	public CScrollBar(int orientation, Color trackColor) {
		super(orientation, trackColor);
	}

	public boolean isScrollBarVisible() {
		return isScrollBarVisible;
	}

	public void setScrollBarVisible(boolean isScrollBarVisible) {
		this.isScrollBarVisible = isScrollBarVisible;
	}

	public int getScrollBarValue() {
		return scrollBarValue;
	}

	public void setScrollBarValue(int scrollBarValue) {
		this.scrollBarValue = scrollBarValue;
	}

	public int getScrollBarAmount() {
		return scrollBarAmount;
	}

	public void setScrollBarAmount(int scrollBarAmount) {
		this.scrollBarAmount = scrollBarAmount;
	}

}
