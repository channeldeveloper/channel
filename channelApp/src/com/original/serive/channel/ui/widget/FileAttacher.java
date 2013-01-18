package com.original.serive.channel.ui.widget;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.original.serive.channel.EventConstants;
import com.original.serive.channel.layout.ChannelGridLayout;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.ui.data.ComboItem;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;
import com.original.service.channel.Attachment;
import com.original.service.channel.Utilies;

/**
 * 附件添加、删除控件
 * @author WMS
 *
 */
public class FileAttacher extends JPanel implements ActionListener, EventConstants
{
	public static Icon attacherDelIcon = IconFactory.loadIconByConfig("fileDelIcon"),
			attacherAddIcon = IconFactory.loadIconByConfig("fileAddIcon");
	
	private JButton btnDel = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, DEL_ATTACHMENT, attacherDelIcon)),
			btnAdd = ChannelUtil.createAbstractButton(
					new AbstractButtonItem(null, ADD_ATTACHMENT, attacherAddIcon));
	
	private JComboBox attacherBox = new JComboBox();
	private JFileChooser attachChooser = new JFileChooser();
	
	public FileAttacher() {
		setLayout(new ChannelGridLayout(10, 0, new Insets(5, 10, 5, 10)));
		
		add(new JLabel("附件："));
		attacherBox.setPreferredSize(new Dimension(200, 22));
		add(attacherBox);
		btnDel.setMargin(new Insets(0, 0, 0, 0));
		btnDel.addActionListener(this);
		add(btnDel);
		btnAdd.setMargin(new Insets(0, 0, 0, 0));
		btnAdd.addActionListener(this);
		add(btnAdd);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(DEL_ATTACHMENT == e.getActionCommand()) {
			Object selectedItem = attacherBox.getSelectedItem();			
			attacherBox.removeItem(selectedItem);
		}
		else if(ADD_ATTACHMENT == e.getActionCommand()) {
			File addFile = ChannelUtil.showFileChooserDialog(btnAdd, "添加附件", true, attachChooser, null);
			if(addFile != null) {
				ComboItem addItem = new ComboItem(addFile.toURI().toString(), addFile.getName());
				attacherBox.insertItemAt(addItem, 0);
				attacherBox.setSelectedItem(addItem);
			}
		}
	}
	
	public void clearAttachments() {
		attacherBox.removeAllItems();
	}
	
	public List<ComboItem> getAttachments() {
		ComboBoxModel model = attacherBox.getModel();
		int size = model.getSize();

		List<ComboItem> attachments = new ArrayList<ComboItem>(size);
		for (int i = 0; i < size; i++) {
			attachments.add((ComboItem) model.getElementAt(i));
		}
		return attachments;
	}
	
	public ComboItem getSelectedAttachment() {
		return (ComboItem)attacherBox.getSelectedItem();
	}
	
	public void setAttachments(List<Attachment> attachments) {
		if (attachments != null && !attachments.isEmpty()) {
//			ComboItem[] items = new ComboItem[attachments.size()];
			Vector<ComboItem> items = new Vector<ComboItem>();
			for (Attachment attach : attachments) {
				if (Attachment.ATTACHMEMNT.equals(attach.getContentType())) {
					items.add(new ComboItem(Utilies.getTempDir(
							attach.getFileId(), attach.getFileName()), attach.getFileName()));
				}
			}
			ComboBoxModel model = new DefaultComboBoxModel(items);
			attacherBox.setModel(model);
		}
	}
	
	public List<Attachment> convertToAttachments() {
		List<ComboItem> comboItems = getAttachments();
		if (comboItems.isEmpty())
			return null;

		List<Attachment> attachments = new ArrayList<Attachment>(comboItems.size());
		Attachment attachment = null;
		for (ComboItem item : comboItems) {
			attachment = new Attachment();
			attachment.setFileName((String) item.getName());
			attachment.setFilePath((String) item.getId());
			attachment.setSize(0);// 暂时不设置大小
			attachment.setContentType(Attachment.ATTACHMEMNT);

			int suffix = attachment.getFileName() == null ? -1 : attachment.getFileName().lastIndexOf(".");
			attachment.setType(suffix == -1 ? "未知文件" : attachment.getFileName().substring(suffix + 1) + "文件");

			attachments.add(attachment);
		}
		return attachments;
	}
}
