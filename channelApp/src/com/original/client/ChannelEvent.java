package com.original.client;

import java.awt.event.ActionEvent;

import com.original.service.channel.ChannelMessage;

public interface ChannelEvent {
	
	/**
	 * 快速回复
	 * @param isON 打开或者关闭快速回复
	 */
	public void doQuickReply(ActionEvent ae, boolean isON);
	
	/**
	 * 保存
	 */
	public void doSave(ActionEvent ae);
	
	/**
	 * 编辑
	 */
	public void doEdit(ActionEvent ae);
	
	/**
	 * 删除
	 */
	public void doDelete(ActionEvent ae);
	
	/**
	 * 显示完整信息
	 */
	public void doShowComplete(ActionEvent ae);
	
	/**
	 * 当前面板显示的消息
	 * @return
	 */
	public ChannelMessage getBodyMessage();
	
}
