package com.original.client.ui.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import com.original.client.EventConstants;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelUtil;
import com.original.client.util.IconFactory;
import com.original.service.channel.core.QueryItem;
import com.seaglasslookandfeel.ui.SeaGlassListUI;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGPopupMenu;
import com.seaglasslookandfeel.widget.SGScrollPane;
import com.seaglasslookandfeel.widget.SGTextField;

/**
 * 联系人搜索弹窗界面，便于用户查询联系人。分两种模型model，一种是自带搜索文本框；
 * 另外一种是外部文本框输入时，弹出该搜索窗。
 * @author WMS
 *
 */
public class PeopleSearchPopup extends SGPopupMenu implements ActionListener, EventConstants{
	
	public static final int SHOW_SEARCH_LIST = 0,//只显示搜索列表
			SHOW_SEARCH_TEXT_LIST = 1;//显示搜索文本框和搜索列表
	
	private SGButton btnSearch = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, SEARCH, IconFactory.loadIconByConfig("searchIcon")));
	private SGTextField txtSearch = new SGTextField(15);
	private JList searchList = new JList();
	
	private int model = SHOW_SEARCH_LIST;
	private QueryItem queryItem = null;//查询项
	
	public PeopleSearchPopup() {
		this(SHOW_SEARCH_LIST);
	}
	
	public PeopleSearchPopup(int model) {
		if(model != SHOW_SEARCH_LIST
				&& model != SHOW_SEARCH_TEXT_LIST)
			throw new IllegalArgumentException("model can only be set for \"SHOW_SEARCH_LIST(0)\" " +
					"or \"SHOW_SEARCH_TEXT_LIST(1)\"");
		
		this.model = model;
		createPopupMenu();
	}
	
	private void createPopupMenu() {
		SGPanel popup = new SGPanel();
		ChannelGridBagLayoutManager layout = new ChannelGridBagLayoutManager(popup);
		layout.setInsets(new Insets(2, 5, 2, 5));
		layout.setAnchor(GridBagConstraints.WEST);

		if (model == SHOW_SEARCH_TEXT_LIST) {
			btnSearch.addActionListener(this);
			
			layout.addComToModel(txtSearch);
			layout.addComToModel(btnSearch);
			layout.newLine();
		}

		SGScrollPane ssp = new SGScrollPane(searchList);
		ssp.setPreferredSize(new Dimension(200, 200));
		searchList.setUI(new SeaGlassListUI());
		searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		layout.addComToModel(ssp, 2, 1, GridBagConstraints.BOTH);
		layout.newLine();

		this.setLayout(new BorderLayout());
		this.add(popup, BorderLayout.CENTER);
	}
	
	public void setLocation(int x, int y) {
		// TODO 自动生成的方法存根
		Point point = ChannelUtil.checkComponentLocation(x, y, this);
		super.setLocation(point.x, point.y);
	}

	//搜索事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}

	public QueryItem getQueryItem() {
		return queryItem;
	}
	public void setQueryItem(QueryItem queryItem) {
		this.queryItem = queryItem;
	}
}
