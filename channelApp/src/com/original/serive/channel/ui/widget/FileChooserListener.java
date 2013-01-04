package com.original.serive.channel.ui.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 * 文件选择监听器，用于监听文件的选择(打开)和取消操作
 * @author WMS
 *
 */
public class FileChooserListener implements ActionListener {
	JFileChooser chooser = null;
	JDialog dialog = null;
	File chooseFile = null;

	public FileChooserListener(JFileChooser chooser) {
		this.chooser = chooser;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		dialog = (JDialog)SwingUtilities.getWindowAncestor(chooser);
		if (e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
			chooseFile = chooser.getSelectedFile();
		} else if (e.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
			chooseFile = null;
		}
		dialog.dispose();//关闭对话框
	}

	public File getChooseFile() {
		return chooseFile;
	}
}