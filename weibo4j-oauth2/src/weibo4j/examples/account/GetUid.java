package weibo4j.examples.account;

import weibo4j.Account;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class GetUid {

	public static void main(String[] args) {		
		String access_token = "2.00ejXVBCRTD23Cb43644219dvKl6rD";
		Account am = new Account();
		am.client.setToken(access_token);
		try {
			JSONObject uid = am.getUid();
			System.out.println(uid);
//			Log.logInfo(uid.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
