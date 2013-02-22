package com.original.client;

import com.original.service.channel.ChannelMessage;

public interface ChannelEvent {
	
	/**
	 * 快速回复
	 * @param isON 打开或者关闭快速回复
	 */
	public void doQuickReply(boolean isON);
	
	/**
	 * 保存
	 */
	public void doSave();
	
	/**
	 * 编辑
	 */
	public void doEdit();
	
	/**
	 * 删除
	 */
	public void doDelete();
	
	/**
	 * 显示完整信息
	 */
	public void doShowComplete();
	
	/**
	 * 回复
	 */
	public void doReply(ChannelMessage msg);
	
	/**
	 * 当前面板显示的消息
	 * @return
	 */
	public ChannelMessage getBodyMessage();
	
}
