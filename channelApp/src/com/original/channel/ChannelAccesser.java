package com.original.channel;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.protocols.sns.weibo.WeiboService;

/**
 * Channel访问服务，用于UI界面访问远程服务，操作数据库等。
 * @author WMS
 *
 */
public class ChannelAccesser
{
	static Oauth oauth = new Oauth();
	static ChannelService cs = ChannelService.getInstance();
	
	/**
	 * 获取激活 授权Token的远程地址
	 * @return
	 * @throws WeiboException
	 */
	public static String getOauthRemoteURL() throws WeiboException {
		return oauth.authorize("code");
	}
	
	/**
	 * 由激活码获取授权Token
	 * @param code 激活码
	 * @return
	 * @throws WeiboException
	 */
	public static AccessToken getAccessTokenByCode(String code) throws WeiboException {
		return oauth.getAccessTokenByCode(code);
	}
	
	/**
	 * 读取第一个账户的授权Token
	 * @return
	 */
	public static String readAccessTokenKey() {
		return WeiboService.readOneToken();
	}
	/**
	 * 读取指定账户的授权Token
	 * @param account 指定账户名
	 * @return
	 */
	public static String readAccessTokenKey(String account) {
		return WeiboService.readToken(account);
	}
	
	public static void storeAccessTokenKey(String account, String accessToken) {
		WeiboService.storeToken(account, accessToken);
	}
	
	/**
	 * 获取消息管理服务对象，即MongoDB数据管理服务对象，用于获取数据
	 * @return
	 */
	public static MessageManager getMsgManager() {
		return cs.getMsgManager();
	}
	
	public static ChannelService getChannelService() {
		return cs;
	}
}
