package com.original.serive.channel.comp;

import java.awt.Component;

import javax.swing.JScrollPane;

import com.seaglasslookandfeel.ui.SeaGlassScrollPaneUI;


/**

 * @Deprecated
 * @author sxy 
 *
 */

public class CScrollPanel extends JScrollPane{

	public CScrollPanel()
	{
		super();
		setUI(new SeaGlassScrollPaneUI());
	}  
	
	public CScrollPanel(Component view, int vsbPolicy, int hsbPolicy) 
    {
		super(view,vsbPolicy,hsbPolicy);
		setUI(new SeaGlassScrollPaneUI());
    }
}
