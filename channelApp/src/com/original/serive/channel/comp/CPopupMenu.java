package com.original.serive.channel.comp;

import javax.swing.JPopupMenu;

import com.seaglasslookandfeel.ui.SeaGlassPopupMenuUI;
import com.original.serive.channel.util.ChannelConstants;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CPopupMenu extends JPopupMenu{

	public CPopupMenu()
	{
		super();
		setUI(new SeaGlassPopupMenuUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
   
}
