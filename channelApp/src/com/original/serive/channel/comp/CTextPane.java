package com.original.serive.channel.comp;

import javax.swing.JTextPane;

import com.seaglasslookandfeel.ui.SeaGlassTextAreaUI;

public class CTextPane extends JTextPane {

	public CTextPane() {
		super();
		setUI(SeaGlassTextAreaUI.createUI(this));
	}
}
