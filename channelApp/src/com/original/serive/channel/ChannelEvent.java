package com.original.serive.channel;

import com.original.service.channel.ChannelMessage;

/**
 * 消息CHannel事件接口类，该类提供的方法可以再扩展
 * @author WMS
 *
 */
public interface ChannelEvent extends EventConstants
{
	/** 保存 */
	public void doSave(ChannelMessage msg);
	
	/** 删除 */
	public void doDelete(ChannelMessage msg);
	
	/** 取消 */
	public void doCancel(ChannelMessage msg);
	
	/** 发送 */
	public void doPost(ChannelMessage msg);
}
