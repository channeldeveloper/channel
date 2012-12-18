import java.util.List;

import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;



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
		csc.initService();
		
		//加入自己的监听
		csc.addMessageListener(listner);
		
		csc.start();
		
		//2 获取All信息,已经按照时间排序
		
		MessageManager msm = csc.getMsgManager();
		
		List<ChannelMessage> msgs = msm.getMessages();
		
		System.out.println("message count:" + msgs.size());
		
		for (ChannelMessage m : msgs)
		{
//			System.out.println("message"+ m);
			String peopleAddr = m.getFromAddr();//
			System.out.println("peopleAddr:"+ peopleAddr);
			
		}
		//获取某个人脉的信息,已经按照时间排序

		String fromAddr = "franzsoong<franzsoong@gmail.com>";
		List<ChannelMessage> msgs1 = msm.getMessagesByFrom(fromAddr);
		System.out.println("message count:" + msgs1.size());
		for (ChannelMessage m : msgs1)
		{
			System.out.println("message1"+ m);			
			String peopleAddr = m.getFromAddr();//
			System.out.println("peopleAddr:"+ peopleAddr);
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
