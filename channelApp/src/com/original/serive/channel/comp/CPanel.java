package com.original.serive.channel.comp;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.seaglasslookandfeel.ui.SeaGlassPanelUI;

public class CPanel extends JPanel {

	public CPanel() {
		this(true);
	}

	public CPanel(boolean isDoubleBuffered) {
		this(new FlowLayout(), true);
	}

	public CPanel(LayoutManager layout) {
		this(layout, true);
	}

	public CPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		setUI(SeaGlassPanelUI.createUI(this));
	}
}
