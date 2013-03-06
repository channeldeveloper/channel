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
 * Channel访问服务，用于UI界面访问远程服务，操作数据库等。目前是CS结构，以后如需改成BS结构的话，只需要改动此类即可。
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
	
	/**
	 * 按账户名存储授权Token，目前保存在本地文件中，以后可能会保存至数据库中。待优化
	 * @param account 指定账户名
	 * @param accessToken 授权Token
	 */
	public static void storeAccessTokenKey(String account, String accessToken) {
		WeiboService.storeToken(account, accessToken);
	}
	
	/**
	 * 获取消息管理服务对象，即MongoDB数据管理服务对象，用于获取消息
	 * @return
	 */
	public static MessageManager getMsgManager() {
		return cs.getMsgManager();
	}
	
	/**
	 * 获取联系人服务对象，即MongoDB联系人管理服务对象，用于获取联系人
	 * @return
	 */
	public static PeopleManager getPeopleManager() {
		return cs.getPeopleManager();
	}
	
	/**
	 * 获取Channel服务类对象，单例对象，即只能有一个Channel服务，其余一些子服务，如<code>MessageManager</code>
	 * 和<code>PeopleManager</code>等都是该服务创建并获取得到。
	 * @return
	 */
	public static ChannelService getChannelService() {
		if(cs == null) {
			cs = ChannelService.getInstance();
		}
		return cs;
	}
	
	/**
	 * 获取自己的账户，如微博、QQ、邮件等等。
	 * @return
	 */
	public static List<Account> getAccounts() {
		return cs.getAccounts();
	}
	
	/**
	 * 获取消息对应的联系人账户头像。目前邮件不支持头像
	 * @param acc   联系人账户
	 * @param msg 消息对象，由消息来判断类型，如QQ、Weibo还是邮件。
	 * @return
	 */
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
	 * 将联系人列表转换成联系人Id(ObjectId)列表。特殊需要，以减少存储空间和方便查询联系人
	 * @param pL 联系人列表
	 * @return
	 */
	public static List<ObjectId> convertToPeopleIdList(List<People> pL) {
		List<ObjectId> pidL = null;
		if (pL != null) {
			pidL = new ArrayList<ObjectId>();
			for (People p : pL) {
				pidL.add(p.getId());
			}
		}
		return pidL;
	}
	
	/**
	 * 获取联系人Id列表。根据需求，是{@link #getPeopleList()}，还是{@link #getPeopleIdList()}
	 * @return
	 */
	public static List<ObjectId> getPeopleIdList() {
return convertToPeopleIdList(getPeopleList());
	}
	
	/**
	 * 由联系人Id获取联系人，需要查询一次数据库。
	 * @param id
	 * @return
	 */
	public static People getPeopleById(ObjectId id) {
		PeopleManager pm = getPeopleManager();
		if (pm != null) {
			return pm.getPeopleById(id);
		}
		return null;
	}
	
	/**
	 * 获取联系人消息列表。默认获取每个联系人20条最新消息，即按联系人分组
	 * @param peopleGrp 联系人组
	 * @param item 自定义查询条件（用于过滤消息）
	 * @param offset 查询起始位置(>=0)，即在查询结果中从该处开始，默认为0，即从第一个结果开始。
	 * @param count 查询需求数，即从offset开始选择count个消息保存至消息列表中
	 * @return 消息列表
	 */
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
	
	/**
	 * 获取一个联系人的消息列表，默认从0开始遍历。
	 * @param peopleId 联系人Id
	 * @param item 自定义查询条件（用于过滤消息）
	 * @param count 查询需求数，即从0开始选择count个消息保存至消息列表中
	 * @return 消息列表
	 */
	public static List<ChannelMessage> getMessageByPeople(ObjectId peopleId, QueryItem item, int count) {
		return getMessageByPeople(peopleId, item, 0, count);
	}
	
	/**
	 * 获取一个联系人的消息列表。
	 * @param peopleId 联系人Id
	 * @param item 自定义查询条件（用于过滤消息）
	 * @param offset 查询起始位置(>=0)，即在查询结果中从该处开始，默认为0，即从第一个结果开始。
	 * @param count 查询需求数，即从offset开始选择count个消息保存至消息列表中
	 * @return 消息列表
	 */
	public static List<ChannelMessage> getMessageByPeople(ObjectId peopleId, QueryItem item, int offset, int count) {
		List<ChannelMessage> messageList = null;
		MessageManager mm = null;
		if(peopleId != null && (mm = cs.getMsgManager()) != null) {
			return mm.getMessageByPeople(peopleId, offset, count, item);
		}
		return messageList;
	}
}
