/**
 * 
 */
package com.channel.test;

import org.junit.After;
import org.junit.Before;

import com.original.service.channel.core.ChannelService;

public abstract class TestBase
{

    protected ChannelService csc;

    protected TestBase() {
        try {
        	csc = ChannelService.getInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    @Before
    public void setUp()
    {
    
    }
	
    protected void cleanup() {
     
    }
    
	@After
	public void tearDown() {
		csc.close();
	}
}
