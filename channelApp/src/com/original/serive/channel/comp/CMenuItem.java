package com.original.serive.channel.comp;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import com.original.serive.channel.util.ChannelConstants;
import com.seaglasslookandfeel.ui.SeaGlassMenuItemUI;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CMenuItem extends JMenuItem{

	public CMenuItem()
	{
		super();
		setUI(new SeaGlassMenuItemUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
	
	public CMenuItem(String text, Icon icon) 
	{
		super(text, icon);
		setUI(new SeaGlassMenuItemUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}

}
