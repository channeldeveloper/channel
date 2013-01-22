package com.original.serive.channel.comp;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import com.seaglasslookandfeel.ui.SeaGlassMenuItemUI;

public class CMenuItem extends JMenuItem {

	public CMenuItem() {
		this(null, (Icon) null);
	}

	public CMenuItem(String text, Icon icon) {
		super(text, icon);
		setUI(SeaGlassMenuItemUI.createUI(this));
	}

}
