package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class GetUserTimeline {

	public static void main(String[] args) {
		String access_token = "2.00RbcE5DRTD23C75d6f211b1KcDpvC";
		Timeline tm = new Timeline();
//		tm.client.setToken(access_token);
		tm.client.setProxyAuthUser("WMSSouvi");
		tm.client.setProxyAuthPassword("ntdxj062");
		try {
//			StatusWapper status = tm.getUserTimeline();
			StatusWapper status = tm.getUserTimelineByName("姚晨");
//			StatusWapper status = tm.getUserTimelineByUid("yaochen");
			int i=0;
			for(Status s : status.getStatuses()){
				Log.logInfo(s.toString());
				if(++i > 0)
					break;
				
////				System.out.println(s.getText());
//				System.out.println(s.getOriginalPic());
//				System.out.println(s.getBmiddlePic());
			}
//			System.out.println(status.getNextCursor());
//			System.out.println(status.getPreviousCursor());
//			System.out.println(status.getTotalNumber());
//			System.out.println(status.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
