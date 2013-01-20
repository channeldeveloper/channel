package com.original.serive.channel.comp;

import javax.swing.JTextArea;

import com.seaglasslookandfeel.ui.SeaGlassTextAreaUI;
import com.original.serive.channel.util.ChannelConstants;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CTextArea extends JTextArea{

	public CTextArea()
	{
		super();
		setUI(new SeaGlassTextAreaUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}  
}
