package com.original.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.plaf.basic.BasicMenuItemUI;

import com.original.channel.ChannelNativeCache;
import com.original.client.EventConstants;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.ui.data.MenuItem;
import com.original.client.ui.setting.ChannelSettingPane;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.client.util.ChannelUtil;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.IconFactory;
import com.original.client.util.LocationIcon;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGLabel;
import com.seaglasslookandfeel.widget.SGMenuItem;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGPopupMenu;
import com.seaglasslookandfeel.widget.SGTextField;

/**
 * 消息Channel工具栏面板，由用户头像，查询组合面板，和工具面板3部分组成。
 * 目前用户头像固定，所以需要自己设计其余2个部分。
 * @author WMS
 *
 */
public class ChannelToolBar extends SGPanel implements ActionListener, EventConstants
{
	/** 工具栏的固定大小 */
	public static Dimension SIZE = new Dimension(
			ChannelConfig.getIntValue("width"),
			ChannelConfig.getIntValue("toolbarHeight"));
	
//	private SGButton btnNew = new SGButton(IconFactory.loadIconByConfig("newIcon")), //新建联系人发送信息
//			btnSetting = new SGButton(IconFactory.loadIconByConfig("settingIcon"));//设置
	
	private SGButton btnNew = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, NEW, 	IconFactory.loadIconByConfig("newIcon"))),
					btnSetting = ChannelUtil.createAbstractButton(
							new AbstractButtonItem(null, SETTING, IconFactory.loadIconByConfig("settingIcon")));
	
	private PropertyChangeSupport changeSupport =
			new SwingPropertyChangeSupport(this);
	
	private ChannelButton typeButton = new ChannelButton("类型", IconFactory.loadIconByConfig("viewdownIcon")),
			 statusButton = new ChannelButton("状态", IconFactory.loadIconByConfig("viewdownIcon"));
	private String selectedType = VIEW_ALL_TYPE, 
			selectedStatus = VIEW_ALL_STATUS; //当前类型状态和类型
	
	public ChannelToolBar() {
				constructToolBar();
	}
	
	/**
	 * 构建工具栏
	 */
	private void constructToolBar() {
		setLayout(null);//这里使用绝对定位，因而没有布局方式，但是必须setPreferredSize()
		setPreferredSize(SIZE); 
		
		//添加类型按钮
		typeButton.setBounds(73+7, 10, 90,25);
		typeButton.initPopupMenu(new MenuItem[]{
				new MenuItem("全部", VIEW_ALL_TYPE, true),
				new MenuItem("邮件", VIEW_MAIL),
				new MenuItem("QQ", VIEW_QQ),
				new MenuItem("微博", VIEW_WEIBO),
		});
		add(typeButton);
		
		//添加状态按钮
		statusButton.setBounds(73+102, 10, 90,25);
		statusButton.initPopupMenu(new MenuItem[]{
				new MenuItem("全部", VIEW_ALL_STATUS, true),
				new MenuItem("未处理", VIEW_UNDO),
				new MenuItem("草稿箱", VIEW_DRAFT),
				new MenuItem("垃圾箱", VIEW_TRASH),
		});
		add(statusButton);
		
		//添加搜索框
		ChannelTextField searchTextField = new ChannelTextField(null, 20, 
				new LocationIcon(IconFactory.loadIconByConfig("searchIcon")));
		searchTextField.setBounds(73+197, 10, 205, 26);
		add(searchTextField);
		
		//添加一些控制按钮
		btnNew.addActionListener(this);
		btnNew.setBounds(73+855, 10,40, 26);
		add(btnNew);
		
		btnSetting.addActionListener(this);
		btnSetting.setBounds(73+900, 10, 40, 26);
		add(btnSetting);
		
	}
	
	/**
	 * 添加消息属性发生改变事件监听器
	 */
	public void addMessageChangeListener(PropertyChangeListener listener)
	{
		if(listener != null)
			changeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * 通知消息属性发生改变，给所有的事件监听器
	 * @param changePropertyName 消息改变属性名称，属性名称都在{@link EventConstants}常量中
	 * @param oldValue 旧值
	 * @param newValue 新值
	 */
	private void fireMessageChange(String changePropertyName, Object oldValue, Object newValue) {
		if(newValue != null && !newValue.equals(oldValue)) {
			if (changePropertyName == TYPE_CHANGE_PROPERTY) {
				selectedType = (String) newValue;
			} else if (changePropertyName == STATUS_CHANGE_PROPERTY) {
				selectedStatus = (String) newValue;
			}
			
			PropertyChangeListener[] listeners = changeSupport.getPropertyChangeListeners();
			for(int i=0; i<listeners.length; i++)
			{
				listeners[i].propertyChange(
						new PropertyChangeEvent(this, changePropertyName, oldValue, newValue));
			}
		}
	}
	
	//一些控制按钮的触发事件
	public void actionPerformed(ActionEvent e)
	{
		ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
		if (e.getActionCommand() == NEW) {
			ChannelMessagePane cmp = new ChannelMessagePane(new NewMessageTopBar(true));
			cmp.newMessage(null);
			desktop.addOtherShowComp(PREFIX_NEW, cmp);
			
		} else if (e.getActionCommand() == SETTING) {
			ChannelSettingPane csp = new ChannelSettingPane();
			csp.initProfileAccount();
			desktop.addSettingShowComp(PREFIX_SETTING, csp);
		}
	}

	/**
	 * 绘制背景效果，如背景色(带有一点透明)，阴影效果等。
	 * 所有的效果都是客户给定好，这里只需要绘制出大概的效果即可。
	 */
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		
		//绘制区域
		Area area = new Area(new Rectangle(0, 0, SIZE.width, SIZE.height));
		g2d.setClip(area);
		
		//绘制渐变效果
		Paint paint = new GradientPaint(new Point(SIZE.width/2, 0), new Color(247,251,251), 
				new Point(SIZE.width/2,SIZE.height), new Color(202,229,238));//这些颜色值客户给定，暂时固定
		g2d.setPaint(paint);
		g2d.fill(area);
		
		//清除选区
		g2d.setClip(null);
		GraphicsHandler.suspendRendering(g2d);
	}	
	
	//获取选中的类型
	public String getSelectedType() {
		return selectedType;
	}
	public void setSelectedType(String selectedType) {
		typeButton.setItemSelected(selectedType, true);
	}

	//获取选中的状态
	public String getSelectedStatus() {
		return selectedStatus;
	}
	public void setSelectedStatus(String selectedStatus) {
		statusButton.setItemSelected(selectedStatus, true);
	}

	/**
	 * Channel按钮，可以通用。注意按钮的高度固定，宽度随文字的长度和大小而定。
	 */
	public class ChannelButton extends SGButton
	{
		ChannelPopupMenu popmenu = new ChannelPopupMenu(this) ;
		public ChannelButton(String text)
		{
			this(text, null);
		}
		public ChannelButton(String text, Icon icon)
		{
			super(text,icon);
			setMargin(new Insets(2, 2, 2, 0));
			setIconTextGap(20);
			setHorizontalAlignment(LEFT);
			setHorizontalTextPosition(LEFT);
			setFont(ChannelConstants.DEFAULT_FONT.deriveFont(15F));
			
			addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
//					if(SwingUtilities.isLeftMouseButton(e))
					{
						popmenu.show(popmenu.getOwner(), 	0,  25);
					}
				}
			});
		}
		
		public void setItemSelected(String actionCommand, boolean isSelected) 
		{
			for (int i = 0; i < popmenu.getComponentCount(); i++) {
				Component comp = popmenu.getComponent(i);
				if (comp instanceof SGMenuItem
						&& ((SGMenuItem) comp).getActionCommand() == actionCommand
						&& ((SGMenuItem) comp).isSelected() != isSelected) {
					((SGMenuItem) comp).setSelected(isSelected);
					break;
				}
			}
		}
		
		public void initPopupMenu(MenuItem[] items)
		{
			if(items != null && items.length > 0)
			{
				for(int i= 0; i< items.length; i++)
				{
					SGMenuItem mi = popmenu.createMenuItem(items[i]);
					popmenu.add(mi);
					if(items[i].isAddSeparator()) {
						popmenu.addSeparator();
					}
					
					if(i == 0) {//设置默认第一个选中
						mi.setSelected(true);
					}
				}
			}
		}
		
		/**
		 * 绘制通用按钮的样式
		 */
		protected void paintComponent(Graphics g)
		{			
			RenderingHints hints = GraphicsHandler.DEFAULT_RENDERING_HINT_ON;
			hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g, hints);
			
			int width = getMargin().left + 
					g.getFontMetrics(getFont()).stringWidth(getText())+
					getIconTextGap()+
					(getIcon() == null ? 0 : getIcon().getIconWidth())+
					getMargin().right
					+2*12;
			int height = 24;
			
//设置选区			
			Area rect = new Area(new Rectangle2D.Double(12, 0, width-2*12, height));
			Area leftHalfCircle = new Area(new Ellipse2D.Double(0, 0, 12*2, 12*2));
			Area rightHalfCircle = new Area(new Ellipse2D.Double(width-2*12, 0, 12*2, 12*2));
			rect.add(leftHalfCircle);
			rect.add(rightHalfCircle);
			
			//绘制阴影
			g2d.translate(0, 1);//坐标轴向下偏移1px，绘制阴影(阴影大小1px)
			
			Paint paint = new LinearGradientPaint(new Point(width/2,0), 
					new Point(width/2,height),
					new float[]{0.0f,1.0f}, 
					new Color[]{new Color(255, 255, 255, 0),new Color(0, 0, 0, (int)(255*0.65))});
			g2d.setPaint(paint);
			g2d.fill(rect);
			
			//绘制背景
			g2d.translate(0, -1);
		  paint = new GradientPaint(new Point(width/2, 0), new Color(120,182,238), 
					new Point(width/2,height), new Color(41,147,220));//这些颜色值客户给定，暂时固定
			g2d.setPaint(paint);
			g2d.fill(rect);
			
			//绘制文字
			g2d.setColor(Color.white);
			g2d.setFont(getFont());
			g2d.drawString(getText(),12+getMargin().left,
					(height-getFont().getSize()/2));
			
			//绘制图标
			if(getIcon() != null) {
			g2d.drawImage(((ImageIcon)getIcon()).getImage(), 
					width-12-getIcon().getIconWidth()-getMargin().right, 
					(height-getIcon().getIconHeight())/2, this);
			}			
		}
	}
	
	/**
	 * Channel文本框，可以通用。其中可以设置边角图标，支持鼠标事件。
	 */
	public class ChannelTextField extends SGTextField
	{		
		private LocationIcon cornerIcon = null;
		public ChannelTextField() {
			this(null);
		}

		public ChannelTextField(String text) {
			this(text, 20, null);
		}
		
		public ChannelTextField(String text, int columns, LocationIcon icon) {
			super(text, columns);
			if (icon != null) {
				setMargin(new Insets(0, 2, 0, icon.getIconWidth()));
				setOpaque(false);
				setBackground(new Color(0, 0, 0, 0));// 如果上面透明无效，这是唯一的解决方法
				initEventListener();

				this.cornerIcon = icon;
			}
		}
		
		//搜索文本框的一些响应事件
		private void initEventListener() {
			// 回车事件
//			addKeyListener(new KeyAdapter() {
//				public void keyReleased(KeyEvent e) {
//					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//						doClick();
//					}
//				}
//			});

			// 鼠标移动事件
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent e) {
					if (cornerIcon.getBounds().contains(e.getPoint())) {
						setCursor(ChannelConstants.HAND_CURSOR);
					} else {
						setCursor(ChannelConstants.TEXT_CURSOR);
					}
				}
			});

			// 鼠标点击事件
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (cornerIcon.getBounds().contains(e.getPoint())) {
						doClick();
					}
				}
			});
		}

		@Override
		protected void paintBorder(Graphics g)
		{
			RenderingHints hints = GraphicsHandler.DEFAULT_RENDERING_HINT_ON;
			hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g, hints);
			
			int width = 204, height = 24;
			Area rect = new Area(new Rectangle2D.Double(12, 0, width-2*12, height));
			Area leftHalfCircle = new Area(new Ellipse2D.Double(0, 0, 12*2, 12*2));
			Area rightHalfCircle = new Area(new Ellipse2D.Double(width-2*12, 0, 12*2, 12*2));
			rect.add(leftHalfCircle);
			rect.add(rightHalfCircle);
			
			//移动坐标轴，绘制内阴影效果
			Paint paint = new GradientPaint(new Point(width/2, 0), new Color(202,229,238),
						new Point(width/2,height),new Color(247,251,251) );//这些颜色值客户给定，暂时固定
				g2d.setPaint(paint);
					g2d.draw(rect);
			
					g2d.translate(0, 1);					
			 paint = new LinearGradientPaint(new Point(width/2,0), 
					new Point(width/2,height),
					new float[]{0.0f,1.0f}, 
					new Color[]{new Color(0, 0, 0, (int)(255*0.65)), new Color(255, 255, 255, 0)});
			g2d.setPaint(paint);
			g2d.draw(rect);
				
			g2d.translate(0, -1);
			
				ImageIcon rightCornerIcon = (ImageIcon)cornerIcon.getIcon();
				if (rightCornerIcon != null)
				{
					Rectangle bounds = new Rectangle(width - rightCornerIcon.getIconWidth()-4, 
							(height - rightCornerIcon.getIconHeight()) / 2, 
							rightCornerIcon.getIconWidth(), 
							rightCornerIcon.getIconHeight());

					g2d.drawImage(rightCornerIcon.getImage(), bounds.x, bounds.y, bounds.width, bounds.height, this);	        
					cornerIcon.setBounds(bounds);
				}
		}
		
		//搜索功能
		protected void doClick() {
			String text = this.getText();
			if (!ChannelUtil.isEmpty(text, true)) {
				this.setText(null); // 必须清空文本
				fireMessageChange(SEARCHTEXT_CHANGE_PROPERTY, null, text);
			}
		}
	}
	
	/**
	 * Channel弹出框，可以通用
	 */
	public class ChannelPopupMenu extends SGPopupMenu implements ActionListener
	{
		private SGButton owner;
		private Color selectedBackground = new Color(46,156,202),
				background = new Color(249, 249, 249);
		private Icon selectedIcon = IconFactory.loadIconByConfig("yesIcon"),
				disSelectedIcon = IconFactory.loadIconByConfig("emptyIcon");
		private ButtonGroup bgp = new ButtonGroup();
		
		public ChannelPopupMenu(SGButton owner) {
			this.owner = owner;
		}

		public SGMenuItem createMenuItem(MenuItem menuItem)
		{
			menuItem.setSize(owner.getBounds().width-2*2, 30);
			final SGMenuItem item = ChannelUtil.createMenuItem(menuItem);
			
			item.setUI(new ChannelMenuItemUI(selectedBackground, Color.white));
			item.setIcon(disSelectedIcon);
			item.setOpaque(false);
			item.setHorizontalAlignment(SGMenuItem.LEFT);
			item.addActionListener(this);
			
			final ButtonModel model = new JToggleButton.ToggleButtonModel() {
				public void setSelected(boolean b) {
					super.setSelected(b);
					if (b) {
						item.setIcon(selectedIcon);
					} else {
						item.setIcon(disSelectedIcon);
					}
				}
			};
			item.setModel(model);
			bgp.add(item);// why changes actionCommand to text?
			
			item.setActionCommand(menuItem.getActionCommand());
			return item;
		}
		
		public SGButton getOwner()
		{
			return owner;
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
			
//			int width = owner.getBounds().width, height = getHeight();
//			
//			//绘制3px阴影效果(要想绘制理想的阴影效果，就需要反复调试。目前阴影的厚度控制在3px以上较理想)
//			GraphicsHandler.fillShadow(g2d, 4, width, height, 7);
//			
//			//绘制背景
//			g2d.setColor(background);
//			g2d.fillRoundRect(3, 3, width-2*3, height-2*3, 5*2, 5*2);
//			
//			g2d.setRenderingHints(GraphicsHandler.DEFAULT_RENDERING_HINT_OFF);
			super.paintComponent(g2d);
		}

		//菜单项点击触发事件
		public void actionPerformed(ActionEvent e)
		{
			SGMenuItem mi = (SGMenuItem) e.getSource();
			ChannelPopupMenu menu = (ChannelPopupMenu)mi.getParent();
			if (mi.isSelected()) {
				SGButton owner = menu.getOwner();
				if (owner == typeButton) {
					fireMessageChange(TYPE_CHANGE_PROPERTY, selectedType, mi.getActionCommand());
				} else if (owner == statusButton) {
					fireMessageChange(STATUS_CHANGE_PROPERTY, selectedStatus, mi.getActionCommand());
				}
			}
		}
		
		class ChannelMenuItemUI extends BasicMenuItemUI
		{
			ChannelMenuItemUI(Color bg, Color fg)
			{
				super.selectionBackground = bg;
				super.selectionForeground = fg;
			}

//			@Override
//			protected void paintMenuItem(Graphics g, JComponent c,
//					Icon checkIcon, Icon arrowIcon, Color background,
//					Color foreground, int defaultTextIconGap)
//			{
//				Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
//				g2d.translate(2, 0);
//				super.paintMenuItem(g2d, c, checkIcon, arrowIcon, background, foreground,
//						defaultTextIconGap);
////				g2d.setRenderingHints(GraphicsHandler.DEFAULT_RENDERING_HINT_OFF);
//				g2d.translate(-2, 0);
//				GraphicsHandler.suspendRendering(g2d);
//			}
		}
	}
	
	/**
	 * Channel用户头像，唯一
	 */
	public static class ChannelUserHeadLabel extends SGLabel
	{
		public ChannelUserHeadLabel() {
			this(IconFactory.loadIconByConfig("userHeadIcon"));
		}
		public ChannelUserHeadLabel(Icon icon) {
			super(icon);
			setCursor(ChannelConstants.HAND_CURSOR);
			addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					doClick();
				}
			});
		}

		//点击用户头像触发事件
		public void doClick()
		{
			System.out.println("click");
		}
	}
}
