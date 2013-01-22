package com.original.serive.channel.ui.widget;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import com.original.serive.channel.comp.CScrollPanel;
import com.original.serive.channel.comp.CTextArea;
import com.original.serive.channel.util.ChannelUtil;

/**
 * 消息框，用于弹窗显示。如确认消息、提示消息等等。
 * @author WMS
 *
 */
public class MessageBox
{
	private CTextArea boxArea = null;
	private Dimension boxMaximumSize = new Dimension(500, 150);  //消息框的最大显示大小
	
	public MessageBox(Object content) {
		boxArea = new CTextArea(content == null ? null : content.toString());
		boxArea.setBorder(null);
		//设置背景透明
		boxArea.setOpaque(false);
		boxArea.setBackground(new Color(255, 255, 255, 0));
		//设置自动换行
		boxArea.setLineWrap(true);
		boxArea.setWrapStyleWord(true);
		//设置不可编辑
		boxArea.setEditable(false);
		
		//设置大小
		Font font = boxArea.getFont();
		FontMetrics fontMetrics = boxArea.getFontMetrics(font);
		Dimension boxSize = new Dimension(Math.min(boxMaximumSize.width,
				fontMetrics.stringWidth(boxArea.getText())), font.getSize());
		boxArea.setSize(boxSize);
	}
	
	
	public Container getMessageBox() {
		Container container = boxArea;
		
		if (boxArea.getUI().getPreferredSize(boxArea).height > boxMaximumSize.height) {
			CScrollPanel boxPane = ChannelUtil.createScrollPane(boxArea, Color.gray);

			//设置相对大小
			Dimension preferedSize = (Dimension) boxArea.getSize().clone();
			preferedSize.height = boxMaximumSize.height;
			boxPane.setPreferredSize(preferedSize);

			container = boxPane;
		}
		return container;
	}	
}
