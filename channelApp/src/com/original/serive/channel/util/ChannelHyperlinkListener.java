package com.original.serive.channel.util;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.ChannelImageDialog;

/**
 * 处理微博、邮件客户端中的超链接等
 * @author WMS
 */
public class ChannelHyperlinkListener implements HyperlinkListener {
	public ChannelHyperlinkListener() {
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			String despURL = e.getDescription();//不要用e.getURL()
			if ("".equals(despURL)) {
				return;
			}
			if (despURL.substring(0, 1).equals("@")) {
				showUser(despURL);
			} else if (despURL.substring(0, 1).equals("#")) {
				openURL("http://huati.weibo.com/k/" + despURL.substring(1));
			} else if (despURL.startsWith("http://")	|| despURL.startsWith("https://") ||
					despURL.startsWith("file:/")) {
				openURL(despURL);
			}
		}
	}

	/**
	 * 使用浏览器打开URL
	 * @param desp
	 */
	private void openURL(String despURL) {
		String lastOfUrl = despURL.substring(despURL.lastIndexOf(".") + 1);
		if ("|gif|jpg|jpeg|png|bmp|".indexOf("|" + lastOfUrl + "|") > -1) {
			new ChannelImageDialog(despURL);
		} else if (despURL.startsWith("http://")
				|| despURL.startsWith("https://")) {
			ChannelUtil.showBrowser(null, despURL);
		} else if (despURL.startsWith("file:/")) {
			Desktop desktop = null;
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
				try {
					desktop.open(new File(new URI(despURL)));
				} catch (Exception ex) { // 打开本地文件出错
					
				}
			}
		}
	}

	/**
	 * 打开微博用户的URL
	 * @param descrip
	 */
	private void showUser(String despURL) {
		String username = despURL.substring(1, despURL.length());
		Users um = new Users();
		try {
			String accessToken = ChannelAccesser.readAccessTokenKey();
			if(accessToken != null) {
				um.setToken(accessToken);
				User user = um.showUserByScreenName(username);
				openURL("http://weibo.com/u/" + user.getId());
			}		
		} catch (WeiboException ex) {
			
		}
	}
}
