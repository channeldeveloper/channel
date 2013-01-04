/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.service;

import iqq.comm.Auth;
import iqq.comm.Auth.AuthInfo;
import iqq.comm.MyDefaultTreeCellRenderer;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.model.MessageDetail;
import iqq.model.MessageStyle;
import iqq.ui.MainPanel;
import iqq.util.GroupUtil;
import iqq.util.Log;
import iqq.util.Method;
import iqq.util.QQImageUtil;
import iqq.util.ThreadUtil;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

/**
 *
 * @author chenzhihui
 */
public class MessageService extends Thread {

    private static MessageService messageService = null;
    private HttpService httpService = null;
    private MemberService memberService = MemberService.getInstance();
    private static boolean isRun = false;
    private Member member = null;
    private int errorCount = 0;
    
    private static AuthInfo loginAI = Auth.getSingleAccountInfo();

    private MessageService() {
    }

    public static MessageService getIntance() {
        if (messageService == null) {
            messageService = new MessageService();
        }
        return messageService;
    }
    
    public JSONObject openMessageChannel(AuthInfo ai) throws Exception
    {
    	String pollUrl = "http://d.web2.qq.com/channel/poll2?clientid=" + ai.getClientid()
    			+ "&psessionid=" + ai.getPsessionid();

    	httpService = new HttpService(pollUrl, Method.GET);
    	String ret = httpService.sendHttpMessage();
    	JSONObject retJ = new JSONObject(ret);
    	return retJ;
    }

    public static AuthInfo getLoginAI() {
		return loginAI;
	}
	public static void setLoginAI(AuthInfo ai) {
		loginAI = ai;
	}

	@Override
    public void run() {
        while (isRun) {
            if (this.errorCount > 10) {
                isRun = false;

                throw new java.lang.IllegalStateException("网络连接异常，请检查网络后重新登录！");
            }

            try {
                JSONObject retJ = openMessageChannel(loginAI);
                int retcode = retJ.getInt("retcode");
                if (retcode == 0) {
                    JSONArray result = retJ.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        String poll_type = result.getJSONObject(i).getString(
                                "poll_type");
                        JSONObject value = result.getJSONObject(i).getJSONObject("value");
                        if ("message".equals(poll_type)) {// 好友消息
                            try {
                                receiveMsg(Auth.getSingleAccountInfo(), value);
                                this.errorCount = 0;
                            } catch (Exception e) {
                            }
                        } else if ("buddies_status_change".equals(poll_type)) {// 好友上下线
                            changeStatus(Auth.getSingleAccountInfo(), value);
                            this.errorCount = 0;
                        } else if ("group_message".equals(poll_type)) {// 群消息
                            receiveGroupMsg(Auth.getSingleAccountInfo(), value);
                            this.errorCount = 0;
                        } else if ("kick_message".equals(poll_type)) {
                            isRun = false; //线程中断
                            throw new java.lang.IllegalStateException("iQQ 已经在别处登录！");
                        }
                    }
                }
                //查询新信息，5秒后继续查询
                Thread.sleep(5000);
            } catch (Exception ex) {
                // TODO: handle exception
                this.errorCount++;
                System.err.println(ex);
            }
        }
    }
    
    public MessageDetail convertToHTML(AuthInfo ai, JSONObject value ) {
    	MessageDetail md = new MessageDetail();
    	try {
    	 int msg_id = value.getInt("msg_id");
         long from_uin = value.getLong("from_uin");
//         long reply_ip = value.getLong("reply_ip");
//         long time = value.getLong("time");

         JSONArray array = value.getJSONArray("content");
         int size = array.length();
         
         String item = null;//表情、图片、自定义表情等元素
         String shortMsg = "", completeMsg = ""; //简短消息、完整消息
         for (int i = 1; i < size; i++) {
             String valStr = array.get(i).toString();
             if (valStr.startsWith("[\"face\",")) {
            	 shortMsg += "[表情]";
            	 item = QQImageUtil.convertFlagToHTML(array.getJSONArray(i).getString(1));
            	 completeMsg += item;
            	 md.addFace(i, item);
             } else if (valStr.startsWith("[\"offpic\"")) {
            	 shortMsg += "[图片]";
                 JSONObject obj = new JSONObject(array.getJSONArray(i).getString(1));
                 if (obj.getInt("success") == 1) {
                	 item = QQImageUtil.getOffImage(ai, obj.getString("file_path"), from_uin);
                	 completeMsg += item;
                	 md.addOffpic(i, item);
                 }
             } else if (valStr.startsWith("[\"cface\"")) {
            	 shortMsg += "[自定义表情]";
            	 item = QQImageUtil.getCFaceImage(ai, array.getJSONArray(i).getString(1), msg_id, from_uin);
            	 completeMsg += item;
            	 md.addCface(i, item);
             } else {
            	 shortMsg += array.getString(i);
            	 completeMsg += array.getString(i);
             }
             
             md.setShortMsg(shortMsg);
             md.setCompleteMsg(completeMsg);
         }
         Log.println("新信息来了：" + shortMsg);

         completeMsg = QQImageUtil.parseHTML(completeMsg);
    	}
    	catch (JSONException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    	return md;
    }
    
    public Message receiveMsgOnly(final AuthInfo ai, JSONObject value) {
    	 try {
         	long from_uin = value.getLong("from_uin");
         	long time = value.getLong("time");
         	
         	MessageDetail md = convertToHTML(ai, value);
         	
             member = memberService.get(ai, from_uin);
             Message msg = new Message();
             msg.setMember(member);
             msg.setMsgDetail(md);
             msg.setCreateDate(new Date(time * 1000L));
             msg.setId(System.currentTimeMillis());
             return msg;
         } catch (JSONException ex) {
             Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
         } catch (Exception ex) {
             Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }
    
    public void receiveMsg(final AuthInfo ai, JSONObject value) {
        try {
        	long from_uin = value.getLong("from_uin");
        	long time = value.getLong("time");
        	
        	MessageDetail md = convertToHTML(ai, value);
        	HTMLDocument htmlDoc = new HTMLDocument();
        	htmlDoc.insertString(0, md.getCompleteMsg(), null);
        	
            member = memberService.get(ai, from_uin);
            Message msg = new Message();
            msg.setMember(member);
            msg.setMessage(htmlDoc);
            msg.setCreateDate(new Date(time * 1000L));
            
            	//添加新来信息到队列中
            	StackMessageService.getIntance().push(msg);

            	//下载头像和个人信息
            	Runnable r = new Runnable() {

            		@Override
            		public void run() {
            			try {
            				member.setFace(MemberService.getInstance().downloadFace(ai, member));
            				if (member.getStat() <= 0 && member.getClient_type() <= 0) {
            					try {
            						//下载个人信息
            						member = MemberService.getInstance().getMemberInfo(ai, member);
            					} catch (Exception ex) {
            						Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
            					}
            				}
            			} catch (Exception ex) {
            				Logger.getLogger(MyDefaultTreeCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
            			}
            		}
            	};

            	if (member != null) {
            		ThreadUtil.submit(r);
            	}
        } catch (JSONException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void changeStatus(AuthInfo ai, JSONObject value) throws Exception {
        long from_uin = value.getLong("uin");
        String status = value.getString("status");
        Member m = MemberService.getInstance().get(ai, from_uin);
        if (m == null) {
            return;
        }
        m.setStatus(status);
        Log.println("用户：" + m.getNickname() + "\t" + status);
        CategoryService.getInstance().changeStatus(ai, m);
    }
    
    public boolean sendMsg(AuthInfo ai, long toUin, String text) {
    	return sendMsg(ai, toUin, text, null);
    }
    
    public boolean sendMsg(AuthInfo ai, long toUin, String text, JSONObject styleJSON) {
    	if(text != null && !text.isEmpty()) {
    		HTMLEditorKit kit = new HTMLEditorKit();
    		HTMLDocument doc = (HTMLDocument)kit.createDefaultDocument();
    		StringReader sr = new StringReader(text);
			try {
				// doc.insertString(0, text, null);
				kit.read(sr, doc, 0);
			} catch (BadLocationException ex) {
			} catch (IOException e) {
			} finally {
				sr.close();
			}
    		return sendMsg(ai, toUin, doc, styleJSON);
    	}
    	return false;
    }
    
    public boolean sendMsg(AuthInfo ai, long toUin, HTMLDocument doc) {
    	return sendMsg(ai, toUin, doc, null);
    }

    public boolean sendMsg(AuthInfo ai, long toUin, HTMLDocument doc, JSONObject styleJSON) {
        JSONArray msg = QQImageUtil.convertHTMLToFlag(doc);
       
        try {
            JSONObject json = new JSONObject();
            json.put("to", toUin);// 要发送的人
            json.put("face", 0);
            
            //转换字体样式
            MessageStyle mstyle = MessageStyle.convert(styleJSON);
            JSONArray font = new JSONArray();
            font.add("font");

            JSONObject font1 = new JSONObject().put("name", mstyle.getFontName()).
            		put("size", ""+mstyle.getFontSize());

            JSONArray style = new JSONArray();
            style.add(mstyle.isBold() ? 1:0); //加粗
            style.add(mstyle.isItalic() ?1:0);//倾斜
            style.add(mstyle.isUnderline()?1:0);//下划线
            font1.put("style", style);
            font1.put("color", mstyle.getColor());

            font.add(font1);
            msg.add(font);
            
            json.put("content", msg.toString());
            json.put("msg_id", new Random().nextInt(10000000));
            json.put("clientid", ai.getClientid());
            json.put("psessionid", ai.getPsessionid());// 需要这个才能发送
            String sendMsgUrl = "http://d.web2.qq.com/channel/send_msg2";
            String content = json.toString();
            Log.println("****content: " + content);
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;

            httpService = new HttpService(sendMsgUrl, Method.POST, content);
            String res = httpService.sendHttpMessage();// 发送
            // 不出意外，这是返回结果：{"retcode":0,"result":"ok"}
            if (null == res || !res.contains("result")) {
                return false;
            }
            JSONObject rh = new JSONObject(res);
            if ("ok".equals(rh.getString("result"))) {
            	Log.println("send message to " + toUin + " successfully......\n");
                return true;
            }
        } catch (Exception e) {
            Log.println("send message to " + toUin + " failure......\n" + e.getMessage());
        }
        return false;
    }

    public void receiveGroupMsg(AuthInfo ai, JSONObject value) throws Exception {
        //Log.println(value);
        //String content = value.getJSONArray("content").getString(1);
        int msg_id = value.getInt("msg_id");
        long from_uin = value.getLong("from_uin");
        long send_uin = value.getLong("send_uin");
        long reply_ip = value.getLong("reply_ip");
        long group_code = value.getLong("group_code");
        long time = value.getLong("time");

        if (GroupUtil.getInstance().isDisabled(GroupService.getInstance().getNumberForCode(ai, group_code).getNumber())) {
            return;
        }

        JSONArray array = value.getJSONArray("content");
        int size = array.length();
        String content = "";
        for (int i = 1; i < size; i++) {
            String valStr = array.get(i).toString();
            if (valStr.startsWith("[\"face\",")) {
                content += QQImageUtil.convertFlagToHTML(array.getJSONArray(i).getString(1));
            } else if (valStr.startsWith("[\"offpic\"")) {
                JSONObject obj = new JSONObject(array.getJSONArray(i).getString(1));
                if (obj.getInt("success") == 1) {
                    content += QQImageUtil.getOffImage(ai, obj.getString("file_path"), from_uin);
                }
            } else if (valStr.startsWith("[\"cface\"")) {
                JSONObject obj = new JSONObject(array.getString(i).replaceFirst(",", ":").replace("[", "{").replace("]", "}"));
                //Log.println(obj);
                content += QQImageUtil.getGroupPic(ai, obj.getJSONObject("cface"), from_uin, send_uin);
            } else {
                content += array.getString(i);
            }
        }

        Log.println("新信息来了：" + content);

        List<Member> memberList = GroupService.getInstance().downloadMemberList(ai, group_code);
        for (Member m : memberList) {
            if (m.getUin() == send_uin) {
                member = m;
            }
        }
        HTMLDocument htmlDoc = new HTMLDocument();
        content = QQImageUtil.parseHTML(content);
        htmlDoc.insertString(0, content, null);

        Group group = GroupService.getInstance().get(from_uin);
        group.setMember(member);
        Message msg = new Message();
        msg.setGroup(group);
        msg.setMessage(htmlDoc);
        msg.setCreateDate(new Date(time * 1000L));
        //添加新来信息到队列中
        StackMessageService.getIntance().push(msg);

        //Log.println(content);
    }

    public boolean sendGroupMsg(AuthInfo ai, long toUin, HTMLDocument doc) {
        JSONArray msg = QQImageUtil.convertHTMLToFlag(doc);
        try {
            JSONObject json = new JSONObject();
            json.put("group_uin", toUin);// 要发送的群
            json.put("face", 330);

//            JSONArray msg = new JSONArray();
//            msg.add(message);
            JSONArray font = new JSONArray();
            font.add("font");

            JSONObject font1 = new JSONObject().put("name", "宋体").put("size",
                    "10");

            JSONArray style = new JSONArray();
            style.add(0);
            style.add(0);
            style.add(0);
            font1.put("style", style);
            font1.put("color", "000000");

            font.add(font1);
            msg.add(font);

            json.put("content", msg.toString());
            json.put("msg_id", new Random().nextInt(10000000));
            json.put("clientid", ai.getClientid());
            json.put("psessionid", ai.getPsessionid());// 需要这个才能发送
            String sendMsgUrl = "http://d.web2.qq.com/channel/send_qun_msg2";
            String content = json.toString();
            Log.println("****content: " + content);
            try {
                content = URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }// 他要需要编码
            content = "r=" + content;

            httpService = new HttpService(sendMsgUrl, Method.POST, content);
            String res = httpService.sendHttpMessage();// 发送
            // 不出意外，这是返回结果：{"retcode":0,"result":"ok"}
            if (null == res || !res.contains("result")) {
                return false;
            }
            JSONObject rh = new JSONObject(res);
            if ("ok".equals(rh.getString("result"))) {
                return true;
            }
        } catch (Exception e) {
            Log.println("send message to " + toUin + " failure......\n" + e.getMessage());
        }
        return false;
    }

    public static boolean isIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean isRun) {
        MessageService.isRun = isRun;
    }
}
