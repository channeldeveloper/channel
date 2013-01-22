package com.original.serive.channel.comp;

import javax.swing.JComboBox;

import com.seaglasslookandfeel.ui.SeaGlassComboBoxUI;

public class CCombobox extends JComboBox{

	public CCombobox() {
		super();
		setUI(SeaGlassComboBoxUI.createUI(this));
	}
}
