package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class Repost {

	public static void main(String[] args) {
		String access_token = "2.00RbcE5DRTD23C75d6f211b1KcDpvC";
		String id =  "3511962405600802";
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			Status status = tm.Repost(id);
//			Log.logInfo(status.toString());
			System.out.println(status);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
