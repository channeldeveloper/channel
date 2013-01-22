package com.original.serive.channel.comp;

import javax.swing.JTextArea;

import com.seaglasslookandfeel.ui.SeaGlassTextAreaUI;

public class CTextArea extends JTextArea {
	public CTextArea() {
		this(null);
	}
	
	public CTextArea(String text) {
		super(text);
        setUI(SeaGlassTextAreaUI.createUI(this));
    }
}
