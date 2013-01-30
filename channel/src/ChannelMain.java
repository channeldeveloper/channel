import java.util.List;

import com.original.service.channel.Account;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants.CHANNEL;
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
		MessageManager msger = csc.getMsgManager();
		
		Account acc = new Account();
		acc.setChannelName("im_xx");
		acc.setPassword("syzb1234");
		acc.setUser("franzsoong@gmail.com");

		boolean exists  = csc.hasAccountInProfile(acc);
		System.out.println(csc.hasAccountInProfile(acc));
		if(!exists) {
		csc.pushAccountToProfile(acc);
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
