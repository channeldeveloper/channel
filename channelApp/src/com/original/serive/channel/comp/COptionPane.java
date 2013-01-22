package com.original.serive.channel.comp;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.seaglasslookandfeel.ui.SeaGlassOptionPaneUI;

public class COptionPane extends JOptionPane {
	public COptionPane() {
		this("JOptionPane message");
	}

	public COptionPane(Object message) {
		this(message, PLAIN_MESSAGE);
	}

	public COptionPane(Object message, int messageType) {
		this(message, messageType, DEFAULT_OPTION);
	}

	public COptionPane(Object message, int messageType, int optionType) {
		this(message, messageType, optionType, null);
	}

	public COptionPane(Object message, int messageType, int optionType,
			Icon icon) {
		this(message, messageType, optionType, icon, null);
	}

	public COptionPane(Object message, int messageType, int optionType,
			Icon icon, Object[] options) {
		this(message, messageType, optionType, icon, options, null);
	}

	public COptionPane(Object message, int messageType, int optionType,
			Icon icon, Object[] options, Object initialValue) {

		super(message, messageType, optionType, icon, options, initialValue);
		setUI(SeaGlassOptionPaneUI.createUI(this));

	}

	@Override
	public void updateUI() {
		// TODO 自动生成的方法存根
		setUI(SeaGlassOptionPaneUI.createUI(this));
	}

}
