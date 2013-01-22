package com.original.serive.channel.comp;

import javax.swing.JPopupMenu;

import com.seaglasslookandfeel.ui.SeaGlassPopupMenuUI;

public class CPopupMenu extends JPopupMenu {

	public CPopupMenu() {
		super();
		setUI(SeaGlassPopupMenuUI.createUI(this));
	}

}
