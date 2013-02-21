package com.original.client.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box.Filler;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang.StringEscapeUtils;

import com.original.client.border.DottedLineBorder;
import com.original.client.util.ChannelUtil;

/**
 * 文本编辑器通用处理类，如插入图片、表情等等。
 * @author WMS
 *
 */
public class EditorHandler {
	
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
		
		HTMLDocument doc = ensureHTMLDocument();
		HTMLEditorKit kit = ensureHTMLEditorKit();

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
	
	/**
	 * 插入文本，支持css样式
	 * @param text 文本内容
	 * @param styleCss 文本样式
	 */
	public void insertText(int paragraphIndex, String text, String textCss) {
insertText(-1, paragraphIndex, text, "background-color:#ffffff;", textCss);
	}
	public void insertText(int offset, int paragraphIndex, String text, String backgroundCss, String textCss) {
		HTMLDocument doc = ensureHTMLDocument();
		HTMLEditorKit kit = ensureHTMLEditorKit();

		StringBuffer sb = new StringBuffer("<div style=\"").append(backgroundCss).append("\"><div id=\"text_" + paragraphIndex + "\" style=\"")
				.append(textCss).append("\">");
		sb.append(text).append("</div></div>");

		StringReader sr = new StringReader(sb.toString());
		try {
			kit.read(sr, doc, offset == -1 ? doc.getLength() : offset);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			sr.close();//关闭是一种好习惯
		}
	}
	
	public void updateText(int paragraphIndex, String text, String textCss) {
updateText(paragraphIndex, textCss, "background-color:#ffffff;", textCss);		
	}
	
	public void updateText(int paragraphIndex, String text, String backgroundCss, String textCss) {
        
        String id = "text_" + paragraphIndex;
        int find = clearParagraph(id);
        
        if(find != -1) {
        	
insertText(find, paragraphIndex, text, backgroundCss, textCss);
editor.setCaretPosition(find);

        }
		
		
	}

	public void removeText(int paragraphIndex) {
       int find =  clearParagraph("text_" + paragraphIndex);
       if(find != -1) {
//    	   editor.setCaretPosition(find);
       }
	}
	
	/**
	 * 插入swing控件至文本面板中，该控件占一个段的位置。
	 * @param comp
	 */
	public void insertCompParagraph(int paragraphIndex, Component comp) 
	{
		insertCompParagraph(-1, paragraphIndex, comp, "background-color:#ffffff;");
	}
	public void insertCompParagraph(int offset, int paragraphIndex, Component comp, String styleCss) 
	{
		HTMLDocument doc = ensureHTMLDocument();
		HTMLEditorKit kit = ensureHTMLEditorKit();
		
		StringReader sr = new StringReader("<div style=\"" + styleCss + 
				"\"><div id=\"" + comp.getName() + "_" + paragraphIndex + "\" ></div></div>");
		try {
			if(offset == -1) offset = doc.getLength();
			kit.read(sr, doc, offset);

			MutableAttributeSet attribute = new SimpleAttributeSet();
			StyleConstants.setComponent(attribute, comp);
			doc.insertString(offset == 0 ? 1 : offset, " ", attribute);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			sr.close();//关闭是一种好习惯
		}
	}
	
	public void removeCompParagraph(int paragraphIndex, Component comp)
	{
		String id = comp.getName() + "_" + paragraphIndex;
		int find = clearParagraph(id);

		if(find != -1) {
			comp = null; //垃圾回收：Let gc collect it!!!
//			editor.setCaretPosition(find);
		}
	}
	
	/**
	 * 插入水平分割线，这里目前都是虚线样式。
	 * 注意，不要使用css样式，swing支持不好
	 * @throws Exception
	 */
	public void insertHorizontalLine(int paragraphIndex, int width, int height)
	{
insertHorizontalLine(-1, paragraphIndex, width, height);
	}
	public void insertHorizontalLine(int offset, int paragraphIndex, int width, int height)
	{
		Filler filler = ChannelUtil.createBlankFillArea(width, height);
		filler.setBorder(BorderFactory.createCompoundBorder(
				new EmptyBorder(0, 10, 0, 20), 
				new DottedLineBorder(DottedLineBorder.BOTTOM, new Color(213, 213, 213), new float[]{3f,4f})));
		filler.setName("hr");
		insertCompParagraph(offset, paragraphIndex, filler, "background-color:#ffffff;");
	}
	public void removeHorizontalLine(int paragraphIndex)
	{
		int find = clearParagraph("hr_"+paragraphIndex);
		if(find != -1) {
//		editor.setCaretPosition(find);
		}
	}
	
	/**
	 * 删除文本中的某一段，同时返回该段的起始offset
	 * @param paragraphId
	 */
	public int clearParagraph(String paragraphId)
	{
		HTMLDocument doc = ensureHTMLDocument();
//		HTMLEditorKit kit = ensureHTMLEditorKit();
		
		ElementIterator it = new ElementIterator(doc);
        Element element;
        
		 int find = -1;
	        
	        while ((element = it.next()) != null) {
	            if (element.getName().equals(HTML.Tag.DIV.toString())) {
	            	
	            	 try {
	                     int start = element.getStartOffset();
	                     int end    = element.getEndOffset();
	                     
	                     boolean isEqual = 
	                    		 paragraphId.equals(element.getAttributes().getAttribute(HTML.Attribute.ID));
	                    		 if(isEqual) {
	                     
	                     doc.remove(start, end-start);

	                     find = start;
	                     break;
	                    		 }
	                 } catch (BadLocationException ex) {
	                 	
	                 }
	            }
	        }
	        
	        return find;
	}
	
	public int[] findParentParagraph(String paragraphId)
	{
		HTMLDocument doc = ensureHTMLDocument();
//		HTMLEditorKit kit = ensureHTMLEditorKit();
		
		ElementIterator it = new ElementIterator(doc);
        Element element;
        
		 int find = -1;
	        
	        while ((element = it.next()) != null) {
	            if (element.getName().equals(HTML.Tag.DIV.toString())) {
	            	
	            	boolean isEqual = 
                   		 paragraphId.equals(element.getAttributes().getAttribute(HTML.Attribute.ID));
                   		 if(isEqual) {
                    

                    int startOffset = element.getParentElement().getStartOffset();
                    int endOffset = element.getParentElement().getEndOffset();
                    return new int[]{startOffset, endOffset};
	            }
	        }
	        }
	        
	        return new int[]{find, find};
	}
	
	/**
	 * 删除文本中的图片标签
	 */
	public void clearAllImageTags() {
		HTMLDocument doc = ensureHTMLDocument();
		HTMLEditorKit kit = ensureHTMLEditorKit();
		
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
		HTMLDocument doc = ensureHTMLDocument();
		HTMLEditorKit kit = ensureHTMLEditorKit();
		
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
		HTMLDocument doc = ensureHTMLDocument(editor);
		HTMLEditorKit kit = ensureHTMLEditorKit(editor);
		
		if(pos < 0)
			pos = doc.getLength();
		
		StringReader reader = new StringReader(text);
		try {
			kit.read(reader, doc, pos);
		}
		catch (Exception ex) {
			// TODO: handle exception
		}
	}
	
	public static void updateHTML(JEditorPane editor, String text, int pos)
	{
		HTMLDocument doc = ensureHTMLDocument(editor);
		HTMLEditorKit kit = ensureHTMLEditorKit(editor);
		
		try {
//			htmlDoc.replace(pos, text.length(), text, htmlKit.getInputAttributes());
			doc.remove(pos, text.length());
			kit.read(new StringReader(text), doc, pos);
		} catch (Exception ex) {
			// TODO 自动生成的 catch 块
		}
	}
	
	private HTMLEditorKit ensureHTMLEditorKit() {
		return ensureHTMLEditorKit(editor);
	}

	private HTMLDocument ensureHTMLDocument() {
		return ensureHTMLDocument(editor);
	}
	
	private static HTMLEditorKit ensureHTMLEditorKit(JEditorPane editor) {
		EditorKit kit = editor.getEditorKit();
		if (!(kit instanceof HTMLEditorKit)) {
			kit = new HTMLEditorKit();
			editor.setEditorKit(kit);
		}
		return (HTMLEditorKit) kit;
	}

	private static HTMLDocument ensureHTMLDocument(JEditorPane editor) {
		Document doc = editor.getDocument();
		ensureHTMLEditorKit(editor);
		return (HTMLDocument) doc;
	}
}
