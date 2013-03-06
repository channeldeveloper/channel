package com.original.client.util;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.event.MouseInputListener;

/**
 * 对话框鼠标拖动事件。目前设定鼠标拖动不能超出界面显示区域。
 * @author WMS
 *
 */
public class ChannelDialogDragListener implements MouseInputListener, ChannelConstants
{
	JDialog dialog = null;
	Point point = new Point(); // 记录dialog的坐标点
	
	public ChannelDialogDragListener(JDialog dialog) {
		this.dialog = dialog;
	}
	
	public void mouseDragged(MouseEvent e) {
		Point p = dialog.getLocation();
		int newX = p.x + e.getX() - point.x, newY = p.y + e.getY() - point.y;

		//检查是否超出边界
		ChannelUtil.checkWindowLocation(newX, newY, dialog);
	}

	public void mousePressed(MouseEvent e) {		
		point.x = e.getX();
		point.y = e.getY();
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
