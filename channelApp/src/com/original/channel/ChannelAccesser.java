package com.original.channel;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import com.original.service.channel.Account;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.core.QueryItem;
import com.original.service.channel.protocols.im.iqq.QQService;
import com.original.service.channel.protocols.sns.weibo.WeiboService;
import com.original.service.people.People;
import com.original.service.people.PeopleManager;

/**
 * Channel访问服务，用于UI界面访问远程服务，操作数据库等。
 * @author WMS
 *
 */
public class ChannelAccesser
{
	static Oauth oauth = new Oauth();
	static ChannelService cs = null;
	
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
	
	public static PeopleManager getPeopleManager() {
		return cs.getPeopleManager();
	}
	
	public static ChannelService getChannelService() {
		if(cs == null) {
			cs = ChannelService.getInstance();
		}
		return cs;
	}
	
	public static List<Account> getAccounts() {
		return cs.getAccounts();
	}
	
	public static String getAvadarPath(Account acc, ChannelMessage msg) {
		String avadarPath = null;
		if(acc != null && msg != null) {
			
			if(msg.isQQ()) {
				avadarPath = QQService.getAvatarPath(acc);
			}
			else if(msg.isWeibo()) {
				avadarPath = WeiboService.getAvadarPath(acc);
			}
			
			//对于邮件，暂时不至于头像
		}
		return avadarPath;
	}
	
	/**
	 * 获取联系人列表
	 * @return
	 */
	public static List<People> getPeopleList() {
		PeopleManager pm = getPeopleManager();
		if (pm != null) {
			return pm.getPeople();
		}
		return null;
	}
	/**
	 * 获取联系人Id列表。根据需求，是{@link #getPeopleList()}，还是{@link #getPeopleIdList()}
	 * @return
	 */
	public static List<ObjectId> getPeopleIdList() {
		List<People> pL = getPeopleList();
		List<ObjectId> pidL = null;
		if (pL != null) {
			pidL = new ArrayList<ObjectId>();
			for (People p : pL) {
				pidL.add(p.getId());
			}
			return pidL;
		}
		return pidL;
	}
	
	/**
	 * 获取联系人消息。默认获取每个联系人20条最新消息，即按联系人分组
	 * @return
	 */
//	public static List<ChannelMessage> getMessageByPeopleGroup(int count) {
//		return getMessageByPeopleGroup(null, count);
//	}
//	
//	public static  List<ChannelMessage> getMessageByPeopleGroup(QueryItem item, int count) {
//		return getMessageByPeopleGroup(item, 0, count);
//	}
	public static  List<ChannelMessage> getMessageByPeopleGroup(List<ObjectId> peopleGrp, QueryItem item, int offset, int count) {
		if(peopleGrp == null || peopleGrp.isEmpty())
			return null;

		if(count < 1) count = 1;

		List<ChannelMessage> messageList = new ArrayList<ChannelMessage>();
		List<ChannelMessage> subList = null;

		for(ObjectId pid : peopleGrp) {
			subList = getMessageByPeople(pid, item, offset, count);
			if(subList != null) {
				messageList.addAll(subList);
			}
		}

		return messageList;
	}
	
	public static List<ChannelMessage> getMessageByPeople(ObjectId peopleId, QueryItem item, int count) {
		return getMessageByPeople(peopleId, item, 0, count);
	}
	
	public static List<ChannelMessage> getMessageByPeople(ObjectId peopleId, QueryItem item, int offset, int count) {
		List<ChannelMessage> messageList = null;
		MessageManager mm = null;
		if(peopleId != null && (mm = cs.getMsgManager()) != null) {
			return mm.getMessageByPeople(peopleId, offset, count, item);
		}
		return messageList;
	}
}
