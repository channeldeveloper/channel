package com.original.service.channel.core;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.Constants.CHANNEL;

/**
 * ChanelService启动或者运行过程中产生的错误。
 * @author WMS
 *
 */
public class ChannelException extends Exception
{
	private static final long serialVersionUID = -5429176779569508997L;
	
	/** 异常Channel账户 */
	private ChannelAccount channelAccount = null;
	
	/** 异常类型 */
	public CHANNEL channel = null;
	
	public ChannelException(ChannelAccount ca, CHANNEL channel, String msg) {
        super(msg);
        this.channelAccount = ca;
        this.channel = channel;
    }

    public ChannelException(ChannelAccount ca, CHANNEL channel, Exception cause) {
        super(cause);
        this.channelAccount = ca;       
        this.channel = channel;
    }

	public ChannelAccount getChannelAccount()
	{
		return channelAccount;
	}
	public void setChannelAccount(ChannelAccount channelAccount)
	{
		this.channelAccount = channelAccount;
	}

	public CHANNEL getChannel() {
		return channel;
	}
	public void setChannel(CHANNEL exceptionChannel) {
		this.channel = exceptionChannel;
	}
}
