package com.original.serive.channel.comp;

import javax.swing.JViewport;

import com.seaglasslookandfeel.ui.SeaGlassViewportUI;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CViewport extends JViewport{

	public CViewport()
	{
		super();
		setUI(new SeaGlassViewportUI());
	}  
}
