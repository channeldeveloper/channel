package com.original.serive.channel.comp;

import javax.swing.JTextField;

import com.seaglasslookandfeel.ui.SeaGlassTextFieldUI;

public class CTextField extends JTextField {

	public CTextField() {
		this(null, 0);
	}

	public CTextField(String text, int columns) {
		super(text, columns);
		setUI(SeaGlassTextFieldUI.createUI(this));
	}

}
