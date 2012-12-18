/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.comm;

import iqq.model.Member;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author chenzhihui
 */
public class Auth implements Serializable{
	private static AuthInfo singleAuthInfo = null; //用于单一用户登陆时
	
	private static Map<String, Auth.AuthInfo> accountInfos = 
			new LinkedHashMap<String, Auth.AuthInfo>(); //这里使用LinkedHashMap保证用户的添加顺序
    
    public static Map<String, Auth.AuthInfo> getAccountInfos()
	{
		return accountInfos;
	}
	public static void setAccountInfos(Map<String, Auth.AuthInfo> accountInfos)
	{
		Auth.accountInfos = accountInfos;
	}
	
	public static void addAccountInfo(String account, AuthInfo authInfo) {
		if(account != null && authInfo != null) {
			Auth.accountInfos.put(account, authInfo);
		}
	}
	
	public static AuthInfo getAccountInfo(String account) {
		return account == null ? null : Auth.accountInfos.get(account);
	}
	
	/**
	 * 用于单一用户登录时
	 * @return
	 */
	public static AuthInfo getSingleAccountInfo() {
		if(singleAuthInfo == null) {
			for(AuthInfo ai : Auth.accountInfos.values()) {
				singleAuthInfo = ai;
				break;
			}
		}
		return singleAuthInfo;
	}
     
	public static class AuthInfo implements Serializable {
		private int clientid = 73937875;
	    
        private String psessionid;
        private String ptwebqq;
        private String vfwebqq;
        private String skey;
        private Member member = new Member();

        public void setClientid(int clientid) {
			this.clientid = clientid;
	    }
		public int getClientid() {
	        return clientid;
	    }
		
        public Member getMember() {
            return member;
        }
        public void setMember(Member member) {
            this.member = member;
        }
        
        public String getPsessionid() {
            return psessionid;
        }
        public void setPsessionid(String psessionid) {
            this.psessionid = psessionid;
        }

        public String getPtwebqq() {
            return ptwebqq;
        }
        public void setPtwebqq(String ptwebqq) {
            this.ptwebqq = ptwebqq;
        }

        public String getSkey() {
            return skey;
        }
        public void setSkey(String skey) {
            this.skey = skey;
        }

        public String getVfwebqq() {
            return vfwebqq;
        }
        public void setVfwebqq(String vfwebqq) {
            this.vfwebqq = vfwebqq;
        }
    }
}
