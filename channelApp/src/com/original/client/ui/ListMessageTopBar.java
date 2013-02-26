package com.original.client.ui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;

import com.original.client.EventConstants;
import com.original.client.ui.data.TitleItem;

public class ListMessageTopBar extends ChannelMessageTopBar
{
	TitleItem[] titleItems = new TitleItem[] {
			new TitleItem("未处理消息").setBold(true).setColor(Color.black),
			new TitleItem("邮件：0"),//1
			new TitleItem("QQ：0"),//2
			new TitleItem("微博：0"),//3
	};
			
	public ListMessageTopBar()
	{
		this.setTitleItems(titleItems); //设置标题内容
	}

	@Override
	public void doClose() {
		// TODO 自动生成的方法存根
//		ListMessageBodyPane mbp = (ListMessageBodyPane)this.getMessageBody();
//		System.out.println(mbp.body.getText());
		
		super.doClose();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO 自动生成的方法存根
		try {
			channelLock.lock();
			
			if (evt.getPropertyName() == EventConstants.MAIL_UNREAD_CHANGE_PROPERTY)
			{
				int newCount = evt.getNewValue() instanceof Integer ? 
						((Integer)evt.getNewValue()).intValue(): 0;
						if(newCount != 0) {//1
							titleItems[1].addValue(newCount);
							titleItems[1].setTitle("邮件：" + titleItems[1].getValue());
						}
				
			} else if (evt.getPropertyName() == EventConstants.QQ_UNREAD_CHANGE_PROPERTY)
			{
				int newCount = evt.getNewValue() instanceof Integer ? 
						((Integer)evt.getNewValue()).intValue(): 0;
						if(newCount != 0) {//2
							titleItems[2].addValue(newCount);
							titleItems[2].setTitle("QQ：" + titleItems[2].getValue());
						}

			} else if (evt.getPropertyName() == EventConstants.WEIBO_UNREAD_CHANGE_PROPERTY)
			{
				int newCount = evt.getNewValue() instanceof Integer ? 
						((Integer)evt.getNewValue()).intValue(): 0;
						if(newCount != 0) {//3
							titleItems[3].addValue(newCount);
							titleItems[3].setTitle("微博：" + titleItems[3].getValue());
						}
			}
			repaint();//重绘标题内容
		}
		finally {
			channelLock.unlock();
		}
	}

}
