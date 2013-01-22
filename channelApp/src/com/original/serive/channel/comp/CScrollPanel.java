package com.original.serive.channel.comp;

import java.awt.Component;

import javax.swing.JScrollPane;

import com.seaglasslookandfeel.ui.SeaGlassScrollPaneUI;

public class CScrollPanel extends JScrollPane {

	public CScrollPanel() {
		this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public CScrollPanel(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		setUI(SeaGlassScrollPaneUI.createUI(this));
	}
}
