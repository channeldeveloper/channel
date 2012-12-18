package weibo4j.examples.user;

import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class ShowUser {

	public static void main(String[] args) {
		//{"access_token":"2.00RbcE5DRTD23C75d6f211b1KcDpvC","remind_in":"157679999","expires_in":157679999,"uid":"3104132255","scope":"4d8

//7f51b3bfe00fbd9126995fefbd1b2"}
		
		
		String access_token = "2.00RbcE5DRTD23C75d6f211b1KcDpvC";
		String uid ="3104132255";
		Users um = new Users();
		um.client.setToken(access_token);
		try {
			User user = um.showUserById(uid);
//			Log.logInfo(user.toString());
			System.out.println(user);
			System.out.println(user.getProfileImageURL());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
