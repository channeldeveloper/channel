package com.original.service.channel.protocols.email.vendor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * 常用邮件服务商的配置服务。
 *
 * @author   Admin
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-3-17 9:17:05
 */
public final class EMailConfig {

	private static EMailConfig config;
    private HashMap<String, EMailServer> servers = null;
    
    /**
     * to create this singleton instance.
     * @return
     */
    public static EMailConfig getEMailConfig()//返回 单例
    {
	    if(config == null)
	    	config = new EMailConfig();
	    return config;
    }
	/**
	 * Only one instance.
	 */
	private EMailConfig()
	{
		init();
		config = this;
	}
	/**
	 * Call to Read configure file wholly.
	 */
	public void reload()
	{
		init();
	}
	/**
	 * 
	 */
	private void init() {
		try {	
			servers = new HashMap<String, EMailServer>();
			InputStream in = getConfig("email_config.xml");
			JAXBContext jc = JAXBContext.newInstance(EMailServers.class);
			Unmarshaller u = jc.createUnmarshaller();

			EMailServers _servers = (EMailServers) u.unmarshal(in);
			for (EMailServer conf : _servers.getEMailServer()) {
				String mailname = conf.getMailname();
				servers.put(mailname, conf);
			}
		} catch (Exception e) {
//			OriLog.getLogger(EMailConfig.class)
//					.error(OriLog.logStack(e));
		}
	}
	
	   /**
     * 获取指定配置文件的输入流
     * @param filename
     * @return
     */
    private static InputStream getConfig(String filename) {
        InputStream in = null;
        try {
            in = new FileInputStream("./config/" + filename);
        } catch (FileNotFoundException ex) {
            in = EMailServer.class.getClassLoader().getResourceAsStream(filename);
        }
        return in;
    }
	
    /**
     * 获取指定登录账户的服务器配置信息
     * @param user
     * @return 
     */
    public  EMailServer getEMailServerByUser(String user) {
//        OriLog.getLogger(EMailConfig.class).debug("getEMailServerByUser============>" + user);
        int idx = user.indexOf("@");
        String mailname = user;
        if (idx > 0) {
            mailname = user.substring(idx + 1);
        }
        if (servers.containsKey(mailname)) {
            return servers.get(mailname);
        }
        return null;
    }
    
    /**
     * 
     */
    public String toString()
    {
    	return this.servers.toString();
    }


}
