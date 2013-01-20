package com.original.serive.channel.comp;

import javax.swing.JTextField;

import com.seaglasslookandfeel.ui.SeaGlassTextFieldUI;
import com.original.serive.channel.util.ChannelConstants;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CTextField extends JTextField{

	public CTextField()
	{
		super();
		setUI(new SeaGlassTextFieldUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
   
}
