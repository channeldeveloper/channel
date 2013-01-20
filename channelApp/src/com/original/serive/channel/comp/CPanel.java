package com.original.serive.channel.comp;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.seaglasslookandfeel.ui.SeaGlassPanelUI;
import com.original.serive.channel.util.ChannelConstants;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CPanel extends JPanel{

	public CPanel()
	{
		super();
		setUI(new SeaGlassPanelUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
    public CPanel(LayoutManager layout)
	{
		super(layout);
		setUI(new SeaGlassPanelUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
}
