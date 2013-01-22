package com.original.serive.channel.comp;

import javax.swing.JViewport;

import com.seaglasslookandfeel.ui.SeaGlassViewportUI;

public class CViewport extends JViewport {

	public CViewport() {
		super();
		setUI(SeaGlassViewportUI.createUI(this));
	}
}
