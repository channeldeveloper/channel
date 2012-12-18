package com.original.service.channel.protocols.email.services;

import com.original.service.channel.event.MessageListner;


public interface EMailMessageListener extends MessageListner{
	
    /**
     * Gives notification that there was an insert into the document.  The 
     * range given by the EMailMessageEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    public void changeUpdate(EMailMessageEvent e);

}
