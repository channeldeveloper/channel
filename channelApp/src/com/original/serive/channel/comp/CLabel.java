package com.original.serive.channel.comp;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.seaglasslookandfeel.ui.SeaGlassLabelUI;

public class CLabel extends JLabel {

	public CLabel() {
		this("", null, LEADING);
	}

	public CLabel(String text) {
		this(text, null, LEADING);
	}

	public CLabel(Icon icon) {
		this(null, icon, CENTER);
	}

	public CLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		setUI(SeaGlassLabelUI.createUI(this));
	}

}
