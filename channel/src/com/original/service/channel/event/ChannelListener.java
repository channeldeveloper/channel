/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.event;

import java.util.EventListener;

public interface ChannelListener extends EventListener{

	public void change(ChannelEvent evnt);
}
