package com.original.serive.channel.comp;

import javax.swing.JToggleButton;

import com.original.serive.channel.util.ChannelConstants;
import com.seaglasslookandfeel.ui.SeaGlassToggleButtonUI;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CToggleButton extends JToggleButton{

	public CToggleButton()
	{
		super();
		setUI(new SeaGlassToggleButtonUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
   
}
