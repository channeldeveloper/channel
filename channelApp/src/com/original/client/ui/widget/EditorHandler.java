package com.original.client.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.Box.Filler;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.lang.StringEscapeUtils;

import com.original.client.border.DottedLineBorder;
import com.original.client.border.SingleLineBorder;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;

/**
 * 文本编辑器通用处理类，如插入图片、表情等等。
 * @author WMS
 *
 */
public class EditorHandler implements ChannelConstants{
	
	public static final String IMAGE_PATTERN = 
			"<img src=\"%s\" width=\"%d\" height=\"%d\" border=\"0\">",
			
			IMAGE_HYBERLINK_PATTERN = 
			"<a href=\"%s\"><img src=\"%s\" width=\"%d\" height=\"%d\" border=\"0\"></a>";
	
	private JEditorPane editor = null;
	private boolean supportMultiImages = true; //是否支持多张图片
	
	public EditorHandler(JEditorPane editor)
	{
		this.editor = editor;
	}
	
	public boolean isSupportMultiImages() {
		return supportMultiImages;
	}
	public void setSupportMultiImages(boolean supportMultiImages) {
		this.supportMultiImages = supportMultiImages;
	}

	/**
	 * 插入图片，图片大小为本身的大小不变
	 * @param icon
	 */
	public void insertImage(URL iconURL)
	{
		insertImage(iconURL, 0, 0);
	}
	
	/**
	 * 插入图片，图片按设定大小显示
	 * @param icon
	 * @param width
	 * @param height
	 */
	public void insertImage(URL iconURL, int width, int height)
	{
		insertImage(iconURL, width, height, editor.getSelectionEnd()); //在光标处插入
	}
	public void insertImage(File iconFile, int width, int height)
	{
		if(iconFile != null && iconFile.exists()) {
			URL iconURL = null;
			try {
				iconURL = iconFile.toURI().toURL();//标准的文件地址："file:/"
			} catch (MalformedURLException ex) {

			}
			insertImage(iconURL, width, height, editor.getSelectionEnd()); //在光标处插入
		}
	}
	
	/**
	 * 插入图片，图片按设定大小显示且可以自定义插入位置
	 * @param icon
	 * @param width
	 * @param height
	 * @param pos
	 */
	public void insertImage(URL iconURL, int width, int height, int pos)
	{
		if (iconURL == null || width < 0 || height < 0)
			return;

		Document doc = editor.getDocument();
		EditorKit kit = editor.getEditorKit();
		if(!(kit instanceof HTMLEditorKit)) //注意这里强制HTMLEditorKit
		{
			kit = new HTMLEditorKit();
			editor.setEditorKit(kit);
		}

		if (pos < 0)
			pos = 0;
		else if (pos > doc.getLength())
			pos = doc.getLength();

		// 设置图标的显示大小
		ImageIcon icon = new ImageIcon(iconURL);
		if (width == 0)
			width = icon.getIconWidth();
		if (height == 0)
			height = icon.getIconHeight();

		Dimension newSize = ChannelUtil.adjustImage(icon, width, height);
		String insertText = null;
		if (editor.isEditable()) { // 可编辑
			insertText = String.format(IMAGE_PATTERN, iconURL, newSize.width,
					newSize.height);
		} else { // 不可编辑
			insertText = String.format(IMAGE_HYBERLINK_PATTERN, iconURL,
					iconURL, newSize.width, newSize.height);
		}

		if(!supportMultiImages) { //不支持多张图片，需要删除原有的图片标签
			clearAllImageTags();
			pos = editor.getCaretPosition();//由于删除图片标签后，会影响原来的pos，所以我们重设pos为当前光标的位置。
		}
		
		StringReader sr = new StringReader(insertText);
		try {
			kit.read(sr, doc, pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sr.close();//关闭是一种好习惯
		}
	}
	
	public void insertText(String text) {
		Document doc = editor.getDocument();
		EditorKit kit = editor.getEditorKit();
		if(!(kit instanceof HTMLEditorKit)) //注意这里强制HTMLEditorKit
		{
			kit = new HTMLEditorKit();
			editor.setEditorKit(kit);
		}
		
		String style = String.format("style=\"font-family:%s;font-size:%s;margin-left:%s;\"",
				DEFAULT_FONT_FAMILY, "11px", "35px");
		
		StringBuffer sb = new StringBuffer("<div ").append(style).append(">");
		sb.append(text).append("</div>");
		
		try {
//			doc.insertString(doc.getLength(), sb.toString(), null);
			kit.read(new StringReader(sb.toString()), doc, doc.getLength());
		}
		 catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String style = String.format("style=\"font-family:%s;font-size:%s;margin-left:%s;\"",
				DEFAULT_FONT_FAMILY, "11px", "35px");
		System.out.println(style);
	}
	
	/**
	 * 插入swing控件至文本面板中，该控件占一个段的位置。
	 * @param comp
	 */
	public void insertCompParagraph(int paragraphIndex, Component comp) 
	{
		Document doc = editor.getDocument();
		EditorKit kit = editor.getEditorKit();
		if(!(kit instanceof HTMLEditorKit)) //注意这里强制HTMLEditorKit
		{
			kit = new HTMLEditorKit();
			editor.setEditorKit(kit);
		}
		
		StringReader sr = new StringReader("<div id=\"1\"></div>");
		try {
			int offset = doc.getLength();
			kit.read(sr, doc, offset);

			int length = doc.getLength();
			MutableAttributeSet attribute = new SimpleAttributeSet();
			StyleConstants.setComponent(attribute, comp);
			doc.insertString(offset == 0 ? length : length - 1, " ", attribute);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			sr.close();//关闭是一种好习惯
		}
	}
	
	/**
	 * 插入水平分割线，这里目前都是虚线样式。如果有其他样式，待以后优化！
	 * 注意，不要使用css样式，swing支持不好
	 * @throws Exception
	 */
	public void insertHorizontalLine(int width, int height)
	{
		Filler filler = ChannelUtil.createBlankFillArea(width, height);
		filler.setBorder(BorderFactory.createCompoundBorder(
				new EmptyBorder(0, 10, 0, 20), 
				new DottedLineBorder(DottedLineBorder.BOTTOM, new Color(213, 213, 213), new float[]{3f,4f})));
		insertCompParagraph(-1, filler);
	}
	
	/**
	 * 删除文本中的图片标签
	 */
	public void clearAllImageTags() {
		HTMLDocument doc = (HTMLDocument)editor.getDocument();
		HTMLEditorKit kit = (HTMLEditorKit)editor.getEditorKit();
		
		ElementIterator it = new ElementIterator(doc);
        Element element;
        while ((element = it.next()) != null) {
            if (element.getName().equals(HTML.Tag.IMG.toString())) {
                try {
                    int start = element.getStartOffset();
                    int end    = element.getEndOffset();
                    doc.replace(start, end-start, "", kit.getInputAttributes());

                } catch (BadLocationException ex) {
                	
                }
            }
        }
	}
	
	/**
	 * 删除光标后的第一个图片标签
	 */
	public void clearFirstImageTags() {
		HTMLDocument doc = (HTMLDocument)editor.getDocument();
		HTMLEditorKit kit = (HTMLEditorKit)editor.getEditorKit();
		
		ElementIterator it = new ElementIterator(doc);
        Element element;
        int pos = editor.getSelectionEnd();
        
        while ((element = it.next()) != null) {
            if (element.getName().equals(HTML.Tag.IMG.toString())) {
                try {
                    int start = element.getStartOffset();
                    if(start >= pos) {
                    	int end    = element.getEndOffset();
                    	doc.replace(start, end-start, "", kit.getInputAttributes());
                    	break;
                    }
                } catch (BadLocationException ex) {
                	
                }
            }
        }
	}
	
	/**
	 * 删除文本中的图片标签
	 * @deprecated 文本中图片标签的属性顺序不一定和{@link #IMAGE_HYBERLINK_PATTERN}
	 * 或{@link #IMAGE_PATTERN}一致，所以有时匹配不成功，删除失败
	 */
	public void removeAllImageTags() {
		String text = editor.getText();
		if(text.isEmpty()) return;
		
		String regex = (!editor.isEditable() ? IMAGE_HYBERLINK_PATTERN
				: IMAGE_PATTERN).replaceAll("%s|%d", "(.+?)");
		
		Matcher matcher = Pattern.compile(Matcher.quoteReplacement(regex)).matcher(text);
		while(matcher.find()) {
			text = text.replace(matcher.group(), "");
		}
		
		editor.setText(text);
	}
	
	/**
	 * 删除光标后的第一个图片标签
	 * @deprecated 文本中图片标签的属性顺序不一定和{@link #IMAGE_HYBERLINK_PATTERN}
	 * 或{@link #IMAGE_PATTERN}一致，所以有时匹配不成功，删除失败
	 */
	public void removeFirstImageTag() {
		
		Document doc = editor.getDocument();
		StyledEditorKit kit = (StyledEditorKit)editor.getEditorKit();
		
		String text = null;
		int offset = editor.getCaretPosition();
		int length = doc.getLength() - offset;
		try {
			text =  editor.getText(offset, length);
			if(text.isEmpty()) return;
		}
		catch(BadLocationException ex) {
			return; //出错了，就不做处理了
		}
		
		String regex = (!editor.isEditable() ? IMAGE_HYBERLINK_PATTERN
				: IMAGE_PATTERN).replaceAll("%s|%d", "(.+?)");
		
		Matcher matcher = Pattern.compile(Matcher.quoteReplacement(regex)).matcher(text);
		if(matcher.find()) {
			text = text.replace(matcher.group(), "");
		}
		
		try {
			if(doc instanceof AbstractDocument)
				((AbstractDocument) doc).replace(offset, length, text, kit.getInputAttributes());
			else {
				doc.remove(offset, length);
				doc.insertString(offset, text, kit.getInputAttributes());
			}
		}
		catch(BadLocationException ex) {
		}
	}
	
	/**
	 * 将HTML格式的内容转换成纯文本内容，注意只保留图片标签(img)不做处理
	 * @param text
	 * @return
	 */
	public String parseHTML(String text)
	{
		if (text.contains("<html>")) {
			text = text.replaceAll("\r?\n *", "\r\n");
            text = text.substring(text.indexOf("<body>")+8, text.indexOf("</body>"));
            String start = "<p style=\"margin-top: 0\">\r\n";
            String end = "\r\n</p>";
            text = text.replaceAll(start, "").replaceAll(end, "");
            
            //去掉字体属性
            start = "<font.*?>";
            end = "</font>";
            text = text.replaceAll(start, "").replaceAll(end, "");
            text = text.replaceAll("<b>|</b>|<u>|</u>|<i>|</i>", "");
            
            //取消换行符和div
            text = text.replaceAll("\r|\n|<div.*?>|</div>", "");
            text = text.replaceAll("<br>|<br/>", "\n");
            
            //最后转换一下中文编码：
            text = StringEscapeUtils.unescapeHtml(text);
        }
        return text;
	}
	
	public static void insertHTML(JEditorPane editor, String text, int pos)
	{
		EditorKit kit = editor.getEditorKit();
		if(!(kit instanceof HTMLEditorKit))
		{
			kit = new HTMLEditorKit();
			editor.setEditorKit(kit);
		}
		
		HTMLEditorKit htmlKit = (HTMLEditorKit)kit;
		HTMLDocument htmlDoc = (HTMLDocument)editor.getDocument();
		
		if(pos < 0)
			pos = htmlDoc.getLength();
		
		StringReader reader = new StringReader(text);
		try {
			htmlKit.read(reader, htmlDoc, pos);
		}
		catch (Exception ex) {
			// TODO: handle exception
		}
	}
	
	public static void updateHTML(JEditorPane editor, String text, int pos)
	{
		EditorKit kit = editor.getEditorKit();
		if(!(kit instanceof HTMLEditorKit))
		{
			kit = new HTMLEditorKit();
			editor.setEditorKit(kit);
		}
		
		HTMLEditorKit htmlKit = (HTMLEditorKit)kit;
		HTMLDocument htmlDoc = (HTMLDocument)editor.getDocument();
		
		try {
//			htmlDoc.replace(pos, text.length(), text, htmlKit.getInputAttributes());
			htmlDoc.remove(pos, text.length());
			htmlKit.read(new StringReader(text), htmlDoc, pos);
		} catch (Exception ex) {
			// TODO 自动生成的 catch 块
		}
	}
}
