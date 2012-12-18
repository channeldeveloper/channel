package weibo4j.examples.oauth2;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;

public class OAuth4Code {
	static Oauth oauth = new Oauth();
	
	public static JDialog showAuthorizeDialog() throws Exception {
		final JDialog dialog = new JDialog() ;
		dialog.setTitle("授权");
		dialog.setResizable(false);
		dialog.setBounds(30, 30, 608, 522);
		dialog.add(getWebBrowser(), BorderLayout.CENTER);
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		return dialog;
	}
	
	public static JWebBrowser getWebBrowser() throws Exception
	{
		final String[] args = readOAuthClientIDAndSecretKey();
	   final String URL = oauth.authorize("code",args[0],args[1]);
		final JWebBrowser webBrowser = new JWebBrowser();
		webBrowser.setBarsVisible(false);
		webBrowser.setButtonBarVisible(false);
		webBrowser.setDefaultPopupMenuRegistered(false);
		try {
//			lastURL = oauth.authorize("code");
			webBrowser.navigate(URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
			@Override
			public void locationChanged(WebBrowserNavigationEvent arg0) {
				String site = arg0.getWebBrowser().getResourceLocation();
				if (site == null || URL.equals(site)
						|| site.indexOf("code=") == -1) {
					return;
				}
				String code = site.substring(site.lastIndexOf("code=") + 5);
				AccessToken accessToken = null;
				try {
					accessToken = oauth.getAccessTokenByCode(code);
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				if (accessToken == null) {
					return;
				}
				
				SwingUtilities.getWindowAncestor(webBrowser).dispose();
				System.out.println("授权成功！");
				System.out.println(accessToken.getAccessToken());
				
			}

		});
		
		return webBrowser;
	}
	
	public static void main(String [] args) throws Exception{		
		NativeInterface.open();
//		UIUtils.setPreferredLookAndFeel();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					showAuthorizeDialog();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		NativeInterface.runEventPump();
	
	}
	
	public static String[] readOAuthClientIDAndSecretKey() throws IOException
	{
		InputStream in = OAuth4Code.class.getResourceAsStream("/config.properties");
		Properties prop = new Properties();
		prop.load(in);
		
		String client_ID = prop.getProperty("client_ID");
		String client_SERCRET = prop.getProperty("client_SERCRET");
		System.out.println("client_ID=" + client_ID);
		System.out.println("client_SERCRET=" + client_SERCRET);
		
		in.close();
		
		return new String[]{client_ID, client_SERCRET};
	}

}
