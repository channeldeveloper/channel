package com.original.serive.channel.comp;

import javax.swing.JToggleButton;

import com.seaglasslookandfeel.ui.SeaGlassToggleButtonUI;

public class CToggleButton extends JToggleButton {

	public CToggleButton() {
		super();
		setUI(SeaGlassToggleButtonUI.createUI(this));
	}

}
