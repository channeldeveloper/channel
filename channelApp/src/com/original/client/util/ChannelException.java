package com.original.client.util;

import com.original.client.util.ChannelConstants.TYPE;
import com.original.service.channel.ChannelAccount;

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
	public TYPE exceptionType = null;
	
	public ChannelException(ChannelAccount ca, TYPE exceptionType, String msg) {
        super(msg);
        this.channelAccount = ca;
        this.exceptionType = exceptionType;
    }

    public ChannelException(ChannelAccount ca, TYPE exceptionType, Exception cause) {
        super(cause);
        this.channelAccount = ca;       
        this.exceptionType = exceptionType;
    }

	public ChannelAccount getChannelAccount()
	{
		return channelAccount;
	}
	public void setChannelAccount(ChannelAccount channelAccount)
	{
		this.channelAccount = channelAccount;
	}
	
	public TYPE getExceptionType()
	{
		return exceptionType;
	}
	public void setExceptionType(TYPE exceptionType)
	{
		this.exceptionType = exceptionType;
	}
}
