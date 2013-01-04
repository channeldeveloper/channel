package com.original.serive.channel.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class ChannelDialogDragListener implements MouseInputListener
{
	Point point = new Point(0, 0); // 坐标点
	JDialog dialog = null;
	
	public ChannelDialogDragListener(JDialog dialog) {
	this.dialog = dialog;	
	}
	
	public void mouseDragged(MouseEvent e) {
		Component cp = (Component)e.getSource();
		if(cp == dialog)
			throw new IllegalStateException("Add ChannelDialogDragListener only for dialog's content pane " +
					"or content pane's child component!");
		
		Point newPoint = SwingUtilities.convertPoint(dialog, e.getPoint(), dialog.getParent());
		dialog.setLocation(dialog.getX() + (newPoint.x - point.x), 
				dialog.getY() + (newPoint.y - point.y));
		point = newPoint; // 更改坐标点
	}

	public void mousePressed(MouseEvent e) {
		Component cp = (Component)e.getSource();
		if(cp == dialog)
			throw new IllegalStateException("Add ChannelDialogDragListener only for dialog's content pane " +
					"or content pane's child component!");
		
		point = SwingUtilities.convertPoint(dialog, e.getPoint(), dialog.getParent()); // 得到当前坐标点
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

}
