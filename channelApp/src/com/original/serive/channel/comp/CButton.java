package com.original.serive.channel.comp;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

import com.seaglasslookandfeel.ui.SeaGlassButtonUI;

public class CButton extends JButton{
	
	public CButton() {
		this(null);
	}

	public CButton(String text) {
		this(text, null);
	}

	public CButton(String text, Icon icon) {
		super(text, icon);
		setUI(SeaGlassButtonUI.createUI(this));
	}

	@Override
	public void updateUI() {
		// TODO 自动生成的方法存根
		setUI(SeaGlassButtonUI.createUI(this));
	}
	
}
