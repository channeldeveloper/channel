package com.original.serive.channel.ui.widget;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.GraphicsHandler;

/**
 * 文件预览控件，目前只用于图片预览
 * @author WMS
 * 
 */
public class FilePreviewer extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -1291104055731523653L;
	
	JFileChooser chooser = null;
	File chooseFile = null;

	public FilePreviewer(JFileChooser chooser) {
		this.chooser = chooser;
		if (chooser != null) {
			chooser.setAccessory(this);
			chooser.addPropertyChangeListener(this);
			
			chooseFile = chooser.getSelectedFile();
		}

		setPreferredSize(new Dimension(150, 0));
		setBorder(BorderFactory.createCompoundBorder(new EtchedBorder(),
				new EmptyBorder(5, 5, 5, 5)));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
			File f = (File) evt.getNewValue();
			if (f == null || !chooser.accept(f)) {
				chooseFile = null;
			} else {
				chooseFile = f;
			}
			repaint();
		}
	}

	@Override
	protected void paintChildren(Graphics g) {
		// TODO Auto-generated method stub
		if (chooseFile != null) {
			ImageIcon icon = null;
			try {
				icon = new ImageIcon(chooseFile.toURI().toURL());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
			}

			if (icon != null) {
				RenderingHints hints = new RenderingHints(
						RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				Graphics g2d = GraphicsHandler.optimizeGraphics(g, hints);
				
				int width = getWidth() - 10, height = getHeight() - 10;// 10为边距
				Dimension dim = ChannelUtil.adjustImage(icon, width, height);
				int iconWidth = dim.width, iconHeight = dim.height;
				
				g2d.drawImage(icon.getImage(), (width - iconWidth) / 2 + 5,
						(height - iconHeight) / 2 + 5, iconWidth, iconHeight, this);
			}
		}
		super.paintChildren(g);
	}
}
