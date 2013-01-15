import java.util.List;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants.CHANNEL;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;



public class ChannelMain {


			
//	//控制 0,1
//		public static final String FLAG_REPLYED = "REPLYED";//是否回复了 0未，1是
//		public static final String FLAG_SIGNED  = "SIGNED";	//是否签名  0未，1是
//		public static final String FLAG_SEEN = "SEEN";//是否看过 0未，1是
//		public static final String FLAG_DELETED = "DELETED";//是否删除 0未，1是
//		public static final String FLAG_PROCESSED  = "PROCESSED";	//是否处理  0未，1是（邮件原来的）
//		public static final String FLAG_DONE = "DONE";//是否处理过 0未，1是 （程序的）
//		public static final String FLAG_TRASHED = "TRASHED";//是否垃圾 0未，1是 isTrash
//		
//		//下面这些目前没有使用到：
//		public static final String FLAG_DRAFT = "DRAFT";//是否草稿 0未，1是
//		public static final String FLAG_FLAGGED = "FLAGGED";//是否旗标 0未，1是
//		public static final String FLAG_RECENT = "RECENT";//是否最新 0未，1是
//		public static final String FLAG_SAVED = "SAVED";//是否存库了 0未，1是
//		

	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		
//		//监听变化
//		MyListener listner = new MyListener();
//		
//		//1 启动服务
		ChannelService csc = ChannelService.getInstance();
//		
//		//加入自己的监听
//		csc.addMessageListener(listner);
//		
//		csc.start();
//		//2 获取All信息,已经按照时间排序
//		
//		/*MessageManager msm = csc.getMsgManager();
//		
//		List<ChannelMessage> msgs = msm.getMessages();
//		
//		System.out.println("message count:" + msgs.size());
//		
//		for (ChannelMessage m : msgs)
//		{
////			System.out.println("message"+ m);
//			String peopleAddr = m.getFromAddr();//
////			System.out.println("peopleAddr:"+ peopleAddr);
//			
//		}
//		//获取某个人脉的信息,已经按照时间排序
//
//		String fromAddr = "franzsoong<franzsoong@gmail.com>";
//		List<ChannelMessage> msgs1 = msm.getMessagesByFrom(fromAddr);
//		System.out.println("message count:" + msgs1.size());
//		for (ChannelMessage m : msgs1)
//		{
////			System.out.println("message1"+ m);			
//			String peopleAddr = m.getFromAddr();//
////			System.out.println("peopleAddr:"+ peopleAddr);
//		}
//		//test quick reply
//		//方式1，原来的msg + 内容
//		ChannelMessage first = msgs1.get(0);
//		ChannelMessage replyMsg = first.clone();
//		replyMsg.setBody("Replay this email from franzsong to franzsong, Hi!");
//		
//		csc.put(Constants.ACTION_QUICK_REPLY, replyMsg);
//		
//		//方式2，new msg
//		first = msgs1.get(0);
//		replyMsg = first.clone();
//		replyMsg.setId(null);
//		replyMsg.setFromAddr(first.getToAddr());
//		replyMsg.setFromAddr(first.getFromAddr());
//		replyMsg.setSubject("Re: " + first.getSubject());
//		replyMsg.setBody("Replay this email from franzsong to franzsong, Hi!");
//		
//		csc.put(Constants.ACTION_QUICK_REPLY, replyMsg);
//		//Pending filter finder(full text search) sort delete.s
//		*/
//		////////////////////////////查找 过滤和删除
//		// 全文搜索
//		MessageManager msgMg = csc.getMsgManager();
//		Iterator<ChannelMessage> result = msgMg.search("Twitter");
//		while(result.hasNext())
//		{
//			System.out.println("search:"+ result.next());			
//		}
//
//
//		// 按照类型过滤消息channel clazz
//		result = msgMg.getByChannelType(ChannelMessage.MAIL);
//		while(result.hasNext())
//		{
//			System.out.println("getByChannelType:"+ result.next());			
//		}
//
////		//3. 按照过滤器来过滤by filter(Pending).
//		try
//		{
//			MessageFilter filter = new MessageFilter("fromAddr", "franzsoong<franzsoong@gmail.com>", "receivedDate");
//			result = msgMg.getMessage(filter);
//			while(result.hasNext())
//			{
//				System.out.println("getByChannelType:"+ result.next());			
//			}
//		}
//		catch(Exception exp)
//		{
//			exp.printStackTrace();
//		}
//		////////删除////////
//		//. 删除消息按照 消息ID（消息ID这里有个bug，发送的消息ID都是
//
//		try
//		{
//			System.out.println("Curent Message Count:" +msgMg.getMessages().size());
//			String msgId = msgMg.getByChannelType(ChannelMessage.MAIL).next().getMessageID();
//			csc.deleteMessage(msgId);
//			System.out.println("Curent Message Count afer delete by message id:" + msgId + " : " +msgMg.getMessages().size());
//		}
//		catch(Exception exp)
//		{
//			exp.printStackTrace();
//		}
//		
//		//按照clazz过滤获取消息
//		try
//		{
//			ObjectId id = msgMg.getByChannelType(ChannelMessage.MAIL).next().getId();
//			csc.deleteMessage(id);
//			
//			System.out.println("Curent Message Count afer delete by mail:" +msgMg.getMessages().size());
//		
//		}
//		catch(Exception exp)
//		{
//			exp.printStackTrace();
//		}
//		//处理附件attachments
//		List<ChannelMessage> msgs = msgMg.getMessages();
//		
//		System.out.println("message count:" + msgs.size());
//		
//		for (ChannelMessage m : msgs)
//		{
//			List<Attachment> atts = m.getAttachments();
//			if (atts != null && atts.size() > 0)
//			{
//				for (Attachment a : atts)
//				{
//					//信息
//					System.out.println("Attachment:" + a);
//					System.out.println(a.getFileName());
//					System.out.println(a.getFileId());
//					System.out.println(a.getSize());
//					System.out.println(a.getType());
//					//写入本地文件
//					String filePath = "c:/"+ a.getFileName();
//					GridFSUtil.getGridFSUtil().writeFile( a.getFileId(), filePath);
//					//用来测试发送邮件带附件（本地），也可以用从文档库中(注释下面的行）
//					a.setFilePath(filePath);
//			
//				}
//				
//				//测试发邮件（带附件和内容)
////				 * 	Subject: Packt Publishing: You are now unsubscribed
////				 * 	To: franzsoong@gmail.com
//				 
//				ChannelMessage m0 = m.clone();
//				m0.setFromAddr(m0.getToAddr());m0.setToAddr("franzsong@163.com");
//				csc.put(Constants.ACTION_SEND, m0);
////				csc.put(Constants.ACTION_QUICK_REPLY, m);
////				csc.put(Constants.ACTION_REPLY, m);
////				csc.put(Constants.ACTION_FORWARD, m);
//				break;
//				
//			}
//
//		}
//		
//		// 更新status
//		ChannelMessage msg = msgMg.getMessages().get(0);
//		csc.updateMessageStatus(msg, Constants.TYPE_SEND);//
//
//		// 更新flag
//		String mid = "<tencent_6E6A430D4A7A97AC404CDA7D@qq.com>";
//		Iterator<ChannelMessage> chm = msgMg.getByMessageID(mid);
//
//		while (chm.hasNext()) {
//			ChannelMessage m = chm.next();
//			
//			csc.updateMessageFlag(m, ChannelMessage.FLAG_TRASHED, 1);
//		}
//
//		//放入垃圾桶和删除
//		mid = "<tencent_2CA85865748F473536CE7435@qq.com>";
//		chm = msgMg.getByMessageID(mid);
//		while (chm.hasNext()) {
//			ChannelMessage m = chm.next();
//			ObjectId id = m.getId();
//			// 放入垃圾桶
//			csc.trashMessage(m);
//			// 删除
//			System.out.println(m);
//			csc.deleteMessage(id);
//			System.out.println(csc.getMsgManager().getMessage(id));
//		}
//
//		//按照flag查找
		MessageManager msger = csc.getMsgManager();
//		//不是垃圾
//		List<ChannelMessage> chmsgs = msger.getMessagesByFlag(ChannelMessage.FLAG_TRASHED, 0);
//		for (ChannelMessage ch : chmsgs)
//		{
//			System.out.println(ch);//
//			break;//测试，只取一条
//		}
//		
//
//		//设置为草稿
//		Iterator<ChannelMessage> result1 = csc.getMsgManager().getByChannelType(ChannelMessage.MAIL);
//		while(result1.hasNext())
//		{
//			csc.put(Constants.ACTION_PUT_DRAFT, result1.next().clone());
//			break;//测试，只取一条
//		}
//		
//
//		while (true) {
//			Thread.sleep(1000);
//		}
		
		
//		List<ChannelMessage> msgList = msger.getMessagesByFlags(new String[]{"DRAFT","TRASHED"}, new Integer[]{1,0});
//		for(ChannelMessage msg : msgList) {
//			System.out.println(msg.getFlags());
//		}
		
		
//		ChannelAccount ca = csc.getDefaultAccount(CHANNEL.MAIL);
//		System.out.println("channelAccount:" + ca.getAccount().getUser());
//		
//		ca = csc.getDefaultAccount(CHANNEL.QQ);
//		System.out.println("channelAccount:" + ca.getAccount().getUser());
//		
//		ca = csc.getDefaultAccount(CHANNEL.WEIBO);
//		System.out.println("channelAccount:" + ca.getAccount().getUser());
	}

	static class MyListener implements MessageListner {

		@Override
		public void change(MessageEvent evnt) {
			// TODO Auto-generated method stub
			System.out.println("get a event from service:" + evnt.getAdded());

		}

	}
}
