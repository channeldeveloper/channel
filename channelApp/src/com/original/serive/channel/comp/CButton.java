package com.original.serive.channel.comp;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.original.serive.channel.util.ChannelConstants;
import com.seaglasslookandfeel.ui.SeaGlassButtonUI;

/**

 * @Deprecated
 * @author sxy 
 *
 */

public class CButton extends JButton{

	public CButton()
	{
		super();
		
		setUI(new SeaGlassButtonUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}  
	
	public CButton(String text)
	{
		super(text);
		setUI(new SeaGlassButtonUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}  
	
	public CButton(String text, Icon icon)
	{
		super(text, icon);
		setUI(new SeaGlassButtonUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
}
