import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.original.service.channel.Attachment;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageFilter;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;
import com.original.service.storage.GridFSUtil;



public class ChannelMain {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		//监听变化
		MyListener listner = new MyListener();
		
		//1 启动服务
		ChannelService csc = new ChannelService();
		
		//加入自己的监听
		csc.addMessageListener(listner);
		
		csc.start();
		
		//2 获取All信息,已经按照时间排序
		
		/*MessageManager msm = csc.getMsgManager();
		
		List<ChannelMessage> msgs = msm.getMessages();
		
		System.out.println("message count:" + msgs.size());
		
		for (ChannelMessage m : msgs)
		{
//			System.out.println("message"+ m);
			String peopleAddr = m.getFromAddr();//
//			System.out.println("peopleAddr:"+ peopleAddr);
			
		}
		//获取某个人脉的信息,已经按照时间排序

		String fromAddr = "franzsoong<franzsoong@gmail.com>";
		List<ChannelMessage> msgs1 = msm.getMessagesByFrom(fromAddr);
		System.out.println("message count:" + msgs1.size());
		for (ChannelMessage m : msgs1)
		{
//			System.out.println("message1"+ m);			
			String peopleAddr = m.getFromAddr();//
//			System.out.println("peopleAddr:"+ peopleAddr);
		}
		//test quick reply
		//方式1，原来的msg + 内容
		ChannelMessage first = msgs1.get(0);
		ChannelMessage replyMsg = first.clone();
		replyMsg.setBody("Replay this email from franzsong to franzsong, Hi!");
		
		csc.put(Constants.ACTION_QUICK_REPLY, replyMsg);
		
		//方式2，new msg
		first = msgs1.get(0);
		replyMsg = first.clone();
		replyMsg.setId(null);
		replyMsg.setFromAddr(first.getToAddr());
		replyMsg.setFromAddr(first.getFromAddr());
		replyMsg.setSubject("Re: " + first.getSubject());
		replyMsg.setBody("Replay this email from franzsong to franzsong, Hi!");
		
		csc.put(Constants.ACTION_QUICK_REPLY, replyMsg);
		//Pending filter finder(full text search) sort delete.s
		*/
		////////////////////////////查找 过滤和删除
		//1 全文搜索
		MessageManager msgMg = csc.getMsgManager();
		Iterator<ChannelMessage> result = msgMg.search("Twitter");
		while(result.hasNext())
		{
			System.out.println("search:"+ result.next());			
		}


		//2 按照类型过滤消息channel clazz
		result = msgMg.getByChannelType(ChannelMessage.MAIL);
		while(result.hasNext())
		{
			System.out.println("getByChannelType:"+ result.next());			
		}
		//3 按照类型过滤 消息ID 获取消息
		
//		
//		//4. 按照过滤器来过滤by filter(Pending).
		MessageFilter filter = new MessageFilter("fromAddr", "franzsoong<franzsoong@gmail.com>", "recievedDate");
		result = msgMg.getMessage(filter);
		while(result.hasNext())
		{
			System.out.println("getByChannelType:"+ result.next());			
		}
		
		////////删除////////
		//1. 删除消息按照 消息ID（消息ID这里有个bug，发送的消息ID都是

		System.out.println("Curent Message Count:" +msgMg.getMessages().size());
		String msgId = msgMg.getByChannelType(ChannelMessage.MAIL).next().getMessageID();
		csc.deleteMessages(msgId);
		System.out.println("Curent Message Count afer delete by message id:" + msgId + " : " +msgMg.getMessages().size());
		
		//2 按照类型过滤 消息 数据库ID 获取消息
		ObjectId id = msgMg.getByChannelType(ChannelMessage.MAIL).next().getId();
		csc.deleteMessage(id);
		
		System.out.println("Curent Message Count afer delete by mail:" +msgMg.getMessages().size());
		

		//attachments
		List<ChannelMessage> msgs = msgMg.getMessages();
		
		System.out.println("message count:" + msgs.size());
		
		for (ChannelMessage m : msgs)
		{
			List<Attachment> atts = m.getAttachments();
			if (atts != null && atts.size() > 0)
			{
				for (Attachment a : atts)
				{
					//信息
					System.out.println("Attachment:" + a);
					System.out.println(a.getFileName());
					System.out.println(a.getFileId());
					System.out.println(a.getSize());
					System.out.println(a.getType());
					//写入本地文件
					GridFSUtil.getGridFSUtil().writeFile( a.getFileId(), "c:/"+ a.getFileName());
			
				}
				
				//测试发邮件（带附件和内容)
//				 * 	Subject: Packt Publishing: You are now unsubscribed
//				 * 	To: franzsoong@gmail.com
				 
				ChannelMessage m0 = m.clone();
				m0.setFromAddr(m0.getToAddr());m0.setToAddr("franzsong@163.com");
				csc.put(Constants.ACTION_SEND, m0);
//				csc.put(Constants.ACTION_QUICK_REPLY, m);
//				csc.put(Constants.ACTION_REPLY, m);
//				csc.put(Constants.ACTION_FORWARD, m);
				break;
				
			}

		}


//	

		while (true) {
			Thread.sleep(1000);
		}
	}

	static class MyListener implements MessageListner {

		@Override
		public void change(MessageEvent evnt) {
			// TODO Auto-generated method stub
			System.out.println("get a event from service:" + evnt.getAdded());

		}

	}
}
