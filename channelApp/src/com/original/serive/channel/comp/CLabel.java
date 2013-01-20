package com.original.serive.channel.comp;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.original.serive.channel.util.ChannelConstants;
import com.seaglasslookandfeel.ui.SeaGlassLabelUI;

/**
 * @Deprecated
 * @author sxy 
 *
 */

public class CLabel extends JLabel{

	public CLabel()
	{
		super();
		setUI(new SeaGlassLabelUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}
	
	public CLabel(String text)
	{
		super(text);
		setUI(new SeaGlassLabelUI());
		this.setFont(ChannelConstants.DEFAULT_FONT);
	}

    public CLabel(Icon image) {
    	super(image);
    	setUI(new SeaGlassLabelUI());
    	this.setFont(ChannelConstants.DEFAULT_FONT);
    }
    
 
    public CLabel(String text, Icon icon, int horizontalAlignment) {
    	super(text, icon, horizontalAlignment);
    	setUI(new SeaGlassLabelUI());
    }
    
}
