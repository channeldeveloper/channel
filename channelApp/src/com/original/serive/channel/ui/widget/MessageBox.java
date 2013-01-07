package com.original.serive.channel.ui.widget;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.original.widget.OScrollBar;

/**
 * 消息框，用于弹窗显示。如确认消息、提示消息等等。
 * @author WMS
 *
 */
public class MessageBox extends JTextArea
{
	Dimension defaultSize = new Dimension(500, 200);
	
	public MessageBox(Object content) {
		super(content == null ? null : content.toString());
		setOpaque(false);
		setLineWrap(true);
		setWrapStyleWord(true);
		setEditable(false);
		
		adjustBoxSize();
	}
	
	/**
	 * 根据文本内容自动调整宽度和高度
	 */
	private void adjustBoxSize() {
		FontMetrics fm = getFontMetrics(getFont());
		Dimension boxSize = new Dimension(Math.min(defaultSize.width, fm.stringWidth(getText())), getFont().getSize());
		setSize(boxSize);
	}
	
	public Container getMessageContainer() 
	{
		Container container = this;
		if (this.getUI().getPreferredSize(this).height > defaultSize.height) {
			JScrollPane jsp = new JScrollPane(this);
			
			jsp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			jsp.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, Color.gray));
			jsp.setViewportBorder(null);
			jsp.setOpaque(false);
			jsp.getViewport().setOpaque(false);

			Dimension size = (Dimension)this.getSize().clone();
			size.height = defaultSize.height;
			jsp.setPreferredSize(size);
			
			container = jsp;
		}
		return container;
	}
	
}
