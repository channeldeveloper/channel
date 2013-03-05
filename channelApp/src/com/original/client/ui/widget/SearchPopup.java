package com.original.client.ui.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

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
 *  另外一种是外部文本框输入时，弹出该搜索窗。
 * @author WMS
 * 
 */
public class SearchPopup extends SGPopupMenu implements ActionListener, EventConstants {

	public static final int SHOW_SEARCH_LIST = 0,// 只显示搜索列表
			SHOW_SEARCH_TEXT_LIST = 1;// 显示搜索文本框和搜索列表

	private SGButton btnSearch = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, SEARCH, IconFactory.loadIconByConfig("searchIcon")));
	private SGTextField txtSearch = new SGTextField(15);
	private JList searchList = new JList();

	private int model = SHOW_SEARCH_LIST;
	private QueryItem queryItem = null;// 查询项

	public SearchPopup() {
		this(SHOW_SEARCH_LIST);
	}

	public SearchPopup(int model) {
		this(model, null);
	}
	
	public SearchPopup(int model, Vector<?> modelData) {
		if (model != SHOW_SEARCH_LIST && model != SHOW_SEARCH_TEXT_LIST)
			throw new IllegalArgumentException(
					"model can only be set for \"SHOW_SEARCH_LIST(0)\" "
							+ "or \"SHOW_SEARCH_TEXT_LIST(1)\"");
		this.model = model;
		
		createPopupMenu();
		initModelData(modelData);
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
		searchList.setToolTipText("双击选择您要查找的结果项~");
		searchList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) //这个判断不知移植到平板上会不会无效
						&& e.getClickCount() == 2) {
					fireAction(searchList.getSelectedIndex());
				}
			}
		});
		layout.addComToModel(ssp, 2, 1, GridBagConstraints.BOTH);
		layout.newLine();

		this.setLayout(new BorderLayout());
		this.add(popup, BorderLayout.CENTER);
	}

	/**
	 * 初始化列表显示数据。
	 */
	protected void initModelData(Vector<?> modelData) {
		searchList.setListData(modelData);
	}

	protected List<Object> getModelData() {
		ListModel model = searchList.getModel();
		int size = model.getSize();
		if (size <= 0)
			return null;

		List<Object> items = new ArrayList<Object>(size);
		for (int i = 0; i < size; i++) {
			items.add(model.getElementAt(i));
		}

		return items;
	}
	
	protected Object getValueAt(int selectedIndex) {
		ListModel model = searchList.getModel();
		return model.getElementAt(selectedIndex);
	}

	/**
	 * 双击事件
	 * @param selectedIndex 选中行索引
	 */
	protected void fireAction(int selectedIndex) {

	}
	
	// 搜索事件
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public void setLocation(int x, int y) {
		// TODO 自动生成的方法存根
		Point point = ChannelUtil.checkComponentLocation(x, y, this);
		super.setLocation(point.x, point.y);
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
	
	/**
	 * 获取搜索文本内容，用于查询。
	 * @return
	 */
	public String getSearchText() {
		return txtSearch.getText();
	}
}
