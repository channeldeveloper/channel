package com.original.serive.channel.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;

import com.original.serive.channel.EventConstants;
import com.original.serive.channel.comp.CButton;
import com.original.serive.channel.comp.CPanel;
import com.original.serive.channel.comp.CTextPane;
import com.original.serive.channel.layout.ChannelGridLayout;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.ui.data.FontStyle;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;

/**
 * 字体选择插件
 * @author WMS
 * @version 1.1
 *
 */
public class FontChooser extends CPanel implements ActionListener, ItemListener,  CaretListener, EventConstants
{
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static DefaultComboBoxModel familiesModel = null,
			sizesModel = null;
	private JComboBox families = new JComboBox(),
			sizes = new JComboBox();
	
	private JToggleButton bold = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, BOLD_STYLE, 
					IconFactory.loadIconByConfig("boldFontIcon"),
					IconFactory.loadIconByConfig("boldFontSelectedIcon"), null), JToggleButton.class),
			
			italic = ChannelUtil.createAbstractButton(
					new AbstractButtonItem(null, ITALIC_STYLE, 
							IconFactory.loadIconByConfig("italicFontIcon"),
							IconFactory.loadIconByConfig("italicFontSelectedIcon"), null), JToggleButton.class),
					
			underline = ChannelUtil.createAbstractButton(
					new AbstractButtonItem(null, UNDERLINE_STYLE, 
							IconFactory.loadIconByConfig("underLineFontIcon"),
							IconFactory.loadIconByConfig("underLineSelectedIcon"), null), JToggleButton.class);
	
	private JColorChooser colorChooser = new JColorChooser(Color.black);
			private CButton color =  ChannelUtil.createAbstractButton(
					new AbstractButtonItem(null, CHOOSE_COLOR, IconFactory.loadIconByConfig("fontColorIcon")));
	
			//用于记录加粗、斜体、下划线这些按钮的选中状态
	private Map<String, Icon> icons = new HashMap<String, Icon>(),
			selectedIcons = new HashMap<String, Icon>();
			
	private CTextPane editor = null;
	private FontStyle fs = new FontStyle();
	
	static {
		familiesModel = new DefaultComboBoxModel(ge.getAvailableFontFamilyNames());
		Vector<String> sizes = new Vector<String>(12);
		for(int i=12; i<=24; i++)
			sizes.add(""+i);
		sizesModel = new DefaultComboBoxModel(sizes);
	}
	
	public FontChooser(CTextPane editor) //overall是否是全局样式
	{
		this.editor = editor;
		fs.setEditor(editor);
		
		constructFontChooser();
	}
	
	/**
	 * 构建字体选择控件
	 */
	protected void constructFontChooser()
	{
		setLayout(new ChannelGridLayout(10, 0, new Insets(5, 10, 5, 10)));
		
		//设置控件属性
		families.setModel(familiesModel);
		families.setSelectedItem(ChannelConfig.getPropValue("channelFont"));//默认字体
		sizes.setModel(sizesModel);
		sizes.setPreferredSize(new Dimension(55, 22));
		sizes.setSelectedItem("14");//默认字体大小
		
		//添加控件
		add(families);
		add(sizes);
		
		add(bold);
		add(italic);
		add(underline);
		
		add(color);
		color.addActionListener(this);
	}

	@Override
	public Component add(Component comp) {
		// TODO Auto-generated method stub
		if(comp instanceof AbstractButton) {
			icons.put(((AbstractButton) comp).getActionCommand(), ((AbstractButton) comp).getIcon());
			selectedIcons.put(((AbstractButton) comp).getActionCommand(), ((AbstractButton) comp).getSelectedIcon());
			((AbstractButton) comp).addItemListener(this);
		}
		else if(comp instanceof JComboBox) {
			((JComboBox) comp).addItemListener(this);
		}
		return super.add(comp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == color) {
			Color c = ChannelUtil.showColorChooserDialog(color, "字体颜色", true, colorChooser);
			if(c != null) {
				fs.setColor(c);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source instanceof AbstractButton) {
			AbstractButton button =(AbstractButton) e.getSource();
			if(e.getStateChange() == ItemEvent.SELECTED) {
				button.setIcon(selectedIcons.get(button.getActionCommand()));
				if(button == bold) {
					fs.setBold(true);
				}
				else if(button == italic) {
					fs.setItalic(true);
				}
				else if(button == underline) {
					fs.setUnderLine(true);
				}
			}
			else {
				button.setIcon(icons.get(button.getActionCommand()));
				if(button == bold) {
					fs.setBold(false);
				}
				else if(button == italic) {
					fs.setItalic(false);
				}
				else if(button == underline) {
					fs.setUnderLine(false);
				}
			}
		}

		else if(source instanceof JComboBox) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(source == families) {
					fs.setFontFamily((String)families.getSelectedItem());
				}
				else if(source == sizes) {
					fs.setFontSize(Integer.parseInt((String)sizes.getSelectedItem()));
				}
			}
		}
	}
	
	/**
	 * 选中文本时，如果对于非全局样式，需要判断选中文本的样式与当前选择器中的样式是否一致。
	 * 注意，需要考虑效率。
	 */
	@Override
	public void caretUpdate(CaretEvent ce) {
		int p0 = Math.min(ce.getDot(), ce.getMark());
		int p1 = Math.max(ce.getDot(), ce.getMark());
		if(p0 != p1)
		{
			Map<Object, Object> styles = new HashMap<Object, Object>();
			StyledDocument doc = editor.getStyledDocument();
			
			for(int p = p0; p<p0+1; p++) //这里考虑效率，我们只看光标后第一个字符的属性
			{
				Element e = doc.getCharacterElement(p);
				MutableAttributeSet sas = null;
				if(e != null && (sas = (MutableAttributeSet)e.getAttributes()) != null) {
					
					for(Object key : FontStyle.STYLES) {
						styles.put(key, sas.getAttribute(key));
					}
				}
			}
			FontStyle clone = fs.clone();
			for(Entry<Object, Object> entry : styles.entrySet()) {
				clone.setFontStyleAttri(entry.getKey(), entry.getValue());
			}
			
			setFontStyle(clone);
		}
	}

	/**
	 * 查找editor中的文本的字体样式
	 * @return
	 */
	public FontStyle lookforFontStyle()
	{	
		return fs;
	}
	
	/**
	 * 设置editor中的文本样式是否是全局
	 * @param overall
	 */
	public void setOverallStyle(boolean overall) 
	{
//		if(fs.isOverall() != overall) {
			fs.setOverall(overall);
			if(editor != null) {
				if(overall) {
					editor.removeCaretListener(this);
				}
				else {
					editor.addCaretListener(this);
				}
			}
//		}
	}
	
	/**
	 * 设置当前字体选择控件的样式
	 * @param fs
	 */
	public void setFontStyle(FontStyle fs) {
		if(fs == null) fs = new FontStyle();
		
		if(!this.fs.equals(fs)) {
			this.fs = fs;
			
			families.setSelectedItem(fs.getFontFamily());
			sizes.setSelectedItem(""+fs.getFontSize()); //注意转换
			colorChooser.setColor(fs.getColor());
			
			bold.setSelected(fs.isBold());
			italic.setSelected(fs.isItalic());
			underline.setSelected(fs.isUnderLine());
		}
	}
}
