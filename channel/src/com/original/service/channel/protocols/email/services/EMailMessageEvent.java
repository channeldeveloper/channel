package com.original.service.channel.protocols.email.services;

import com.original.service.channel.Channel;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Service;
import com.original.service.channel.event.MessageEvent;

public class EMailMessageEvent extends MessageEvent{

	public EMailMessageEvent(Service service, Channel channel, int type,
			ChannelMessage[] added, ChannelMessage[] removed, ChannelMessage[] updated) {
		super(service, channel, type, added, removed, updated);
		// TODO Auto-generated constructor stub
	}

}
