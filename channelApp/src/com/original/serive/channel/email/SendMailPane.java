package com.original.serive.channel.email;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.text.DefaultEditorKit;

import org.apache.log4j.Logger;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.services.EMailSaver;
import com.original.service.channel.protocols.email.services.EmailSender;
import com.original.service.channel.protocols.email.services.MailAuthentication;
import com.original.service.channel.protocols.email.services.MailParseUtil;
import com.original.service.storage.FileExchange;
import com.original.service.storage.FileManager;
import com.original.service.storage.StreamData;
import com.original.util.log.OriLog;
import com.original.widget.OButton;
import com.original.widget.OHtmlEditor;
import com.original.widget.OObjEditor;
import com.original.widget.OPanel;
import com.original.widget.OTextField;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-23 13:07:52
 */
public class SendMailPane extends OPanel {

    Logger log = OriLog.getLogger(SendMailPane.class);
    private String workdir = System.getProperty("user.dir") + "/temp/";
    private Font font1 = new Font("微软雅黑", Font.BOLD, 14);
    private OTextField titleField = new OTextField();
    private OObjEditor receiverField = new OObjEditor();
    private OObjEditor copyField = new OObjEditor();
    private OObjEditor BCCField = new OObjEditor();
    private JLabel deleteAttachmentLabel = new JLabel("删除");
    private JList showAttachmentList = new JList();
    private String mailtype = "outbox";
    private EMail email = null;
    private List<String> attachList = new ArrayList<String>();
    private List<String> attachNameList = new ArrayList<String>();
    private JLabel addresserLabel = new JLabel();
    /**
     *
     */
    public MailAuthentication curMailAccount;
    private OHtmlEditor htmlEditor;
    private OButton addAttachLabel = new OButton("添加附件");
    private OButton addCopyLabel = new OButton("添加抄送");
    private Color lineColor = new Color(132, 187, 233);
    JLabel copyLabel = new JLabel("抄　送：");
    JLabel BCCLabel = new JLabel("密　送：");

    /**
     *
     * @param type
     */
    public void setMailType(String type) {
        this.mailtype = type;
    }

    /**
     *
     * @param account
     */
    public void setSelectedAccount(MailAuthentication account) {
        if (account != null) {
            this.curMailAccount = account;
            this.addresserLabel.setText(account.getUserName());
        }
    }

    /**
     *
     */
    public SendMailPane() {
        init();
    }

    /**
     * 调用工具集成方法来实现通讯录和该组件关联，
     * 这样当你输入拼音的时候，系统就会弹出一个对话框供选择，
     * 不同的人输入之间使用小写逗号
     * @param button
     */
    private void attach(Container button) {
//        OriFactory o = OriFactory.getInstance();
//        o.executeEx("ori.addressbook", new Class[]{Container.class},
//                new Object[]{button});
    }

    /**
     *
     * @param isSend
     */
    public void sendMessage(boolean isSend) {
//        MainFrame.getMainFrame().removeTab(this);

        if (curMailAccount == null) {
            return;
        }
        EmailSender themail = new EmailSender("");
        EMailSaver savemail = new EMailSaver("");
        String attachs = null;
        if (attachList.size() > 0) {
            attachs = "";
            for (int i = 0; i < attachList.size(); i++) {
                attachs += attachList.get(i);
                attachs += ";";
            }
        }

        String isCreated = themail.createMessage(receiverField.getInValue(), copyField.getInValue(), BCCField.getInValue(), titleField.getText(),
                htmlEditor.getTextPane().getText(), attachs, curMailAccount.getUserName());
        String emailid = (email == null ? null : email.get_id());
//        String isSended = null;
//        if (isSend && isCreated == null) {
//            isSended = themail.send(curMailAccount);
//            if ("reply".equalsIgnoreCase(mailtype) || "replyall".equalsIgnoreCase(mailtype)) {
//                if (email.getIsReplay() != 1) {
//                    EMail em = new EMail();
//                    em.setIsReplay(1);
//                    String emailid = email.get_id();
//                    savemail.updateMail(em, emailid);
//                }
//            }
//        }
        String type = isSend ? "outbox" : "draftbox";
        if (isCreated == null) {
            try {
                String returns = savemail.sendEMail(themail.getMimeMsg(), curMailAccount.getUserName(), type, emailid );
                if ("draftbox".equalsIgnoreCase(mailtype)) {
//                    String emailid = email.get_id();
//                    savemail.deleteMail(emailid);
//                    MainFrame.getMainFrame().updateMailList();
                }
                log.debug("return = "+returns);
            } catch (Exception ex) {
//                ex.printStackTrace();
                log.error(ex.getMessage(), ex);
            }
        } else {
            log.error(isCreated);
        }
    }

    /**
     *
     */
    private void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JPanel functionButtonPane = new JPanel(new GridBagLayout());
        functionButtonPane.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, lineColor));
        functionButtonPane.setOpaque(false);

        OButton sendMessageButton = new OButton("发  送");
        sendMessageButton.setLevel(BUTTONLEVEL.APPLICATION);
        sendMessageButton.setPreferredSize(new Dimension(80, 36));
        sendMessageButton.addMouseListener(new MouseAdapter() {

            @Override
            @SuppressWarnings("static-access")
            public void mouseClicked(MouseEvent e) {
                sendMessage(true);
            }
        });

        OButton saveMessageButton = new OButton("存草稿");
        saveMessageButton.setLevel(BUTTONLEVEL.APPLICATION);
        saveMessageButton.setPreferredSize(new Dimension(80, 36));

        saveMessageButton.addMouseListener(new MouseAdapter() {

            @Override
            @SuppressWarnings("static-access")
            public void mouseClicked(MouseEvent e) {
                sendMessage(false);
            }
        });

        functionButtonPane.add(sendMessageButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        functionButtonPane.add(saveMessageButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        functionButtonPane.add(new JLabel(" "), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));

        JPanel textpanel = new JPanel(new GridBagLayout());
        textpanel.setOpaque(false);
        JLabel label = new JLabel("发件人：");
        label.setFont(font1);
        textpanel.add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 0, 0), 0, 0));
        textpanel.add(addresserLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(10, 20, 0, 0), 0, 0));
        addresserLabel.setFont(font1);

        JLabel receiverLabel = new JLabel("收件人：");
        receiverField.setPreferredSize(new Dimension(30, 40));
        receiverLabel.setFont(font1);
        textpanel.add(receiverLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));
        textpanel.add(receiverField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));

        copyLabel.setFont(font1);
        textpanel.add(copyLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));
        textpanel.add(copyField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));

        JLabel titleLabel = new JLabel("主　题：");
        titleLabel.setFont(font1);

        textpanel.add(titleLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));
        textpanel.add(titleField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));

        BCCLabel.setFont(font1);
        textpanel.add(BCCLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));
        textpanel.add(BCCField, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 0), 0, 0));
        copyField.setVisible(false);
        copyField.setPreferredSize(new Dimension(30, 40));
        BCCField.setPreferredSize(new Dimension(30, 40));

        deleteAttachmentLabel.setVisible(false);
        JPanel actionPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        actionPane.setOpaque(false);
        addCopyLabel.setPreferredSize(new Dimension(90, 30));
        addCopyLabel.setLevel(BUTTONLEVEL.PAGE);
        addCopyLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                setStatus(!copyField.isVisible());
            }
        });
        addAttachLabel.setPreferredSize(new Dimension(90, 30));
        addAttachLabel.setLevel(BUTTONLEVEL.PAGE);
//        addAttachLabel.setFont(font1);
//        addAttachLabel.setForeground(new Color(145, 190, 221));
        addAttachLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                clickAddAttachment();
            }
        });
        actionPane.add(addCopyLabel);
        actionPane.add(addAttachLabel);
        actionPane.add(showAttachmentList);
        actionPane.add(deleteAttachmentLabel);
        deleteAttachmentLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                deleteAttachmentList();
            }
        });
        deleteAttachmentLabel.setForeground(new Color(81, 165, 225));

        JPanel mailHeaderPane = new JPanel(new GridBagLayout());
        mailHeaderPane.setOpaque(false);
        mailHeaderPane.add(functionButtonPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        mailHeaderPane.add(textpanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 10), 0, 0));
        mailHeaderPane.add(actionPane, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 74, 0, 15), 0, 0));

        // 正文编辑区
        JPanel editorpane = new JPanel(new BorderLayout());
        htmlEditor = new OHtmlEditor();
        htmlEditor.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(217, 217, 217)));

        editorpane.add(htmlEditor);

        mailHeaderPane.add(editorpane, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(5, 80, 5, 15), 0, 0));

        showAttachmentList.setSelectionBackground(new Color(224, 224, 224));
        showAttachmentList.setBackground(Color.white);
        showAttachmentList.setSelectionForeground(new Color(81, 165, 225));
        showAttachmentList.setForeground(new Color(81, 165, 225));
        showAttachmentList.setFixedCellHeight(20);
        showAttachmentList.setFont(font1);
        deleteAttachmentLabel.setFont(font1);

        this.add(mailHeaderPane, BorderLayout.NORTH);
        setStatus(false);
        initAttach();
    }

    /**
     *
     */
    public void initAttach() {
        //使用工具集成关联通讯录。
        attach(receiverField);
        attach(copyField);
        attach(BCCField);
    }

    /*
     * 添加附件
     */
    /**
     *
     */
    public void clickAddAttachment() {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        jfc.setMultiSelectionEnabled(true);

        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] files = jfc.getSelectedFiles();
            for (File file : files) {
                String path = file.getAbsolutePath();
                String name = file.getName();
                attachNameList.add(name);
                attachList.add(path);
                FileExchange.getInstance().addContextVariable(path, new StreamData(null, path));
                showAttachmentList.setListData(attachNameList.toArray());
            }
            if (attachList.size() > 0) {
                deleteAttachmentLabel.setVisible(true);
            }
        }
    }

    private void setStatus(boolean flag) {
        if (flag) {
            addCopyLabel.setText("隐藏抄送");
            htmlEditor.setPreferredSize(new Dimension(860, 200));
            htmlEditor.updateUI();
        } else {
            addCopyLabel.setText("添加抄送");
            htmlEditor.setPreferredSize(new Dimension(860, 283));
            htmlEditor.updateUI();
        }
        copyField.setVisible(flag);
        copyLabel.setVisible(flag);
        BCCField.setVisible(flag);
        BCCLabel.setVisible(flag);
    }

    /*
     * 删除附件列表中的附件
     */
    private void deleteAttachmentList() {
        int items_to_delete[] = null;
        if (!showAttachmentList.isSelectionEmpty()) {
            items_to_delete = showAttachmentList.getSelectedIndices();
        } else {
            return;
        }
        if (items_to_delete != null);
        {
            for (int i = items_to_delete.length - 1; i >= 0; i--) {
                attachNameList.remove(items_to_delete[i]);
                String path = attachList.get(items_to_delete[i]);
                FileExchange.getInstance().getContextVariable(path, true);
                attachList.remove(items_to_delete[i]);
            }
            showAttachmentList.setListData(attachNameList.toArray());
            if (attachNameList.isEmpty()) {
                deleteAttachmentLabel.setVisible(false);
            }
        }
    }

    /**
     *
     */
    public void clear() {
        attachList.clear();
        attachNameList.clear();
        deleteAttachmentLabel.setVisible(false);
        this.titleField.setText("");
        this.receiverField.setText("");
        this.copyField.setText("");
        BCCField.setText("");
        this.showAttachmentList.setListData(attachNameList.toArray());
        htmlEditor.getTextPane().setText("");
        this.addresserLabel.setText("");
        this.updateUI();
    }

    /**
     *
     * @param _email
     */
    public void setDataToGUI(EMail _email) {
        this.clear();
        this.email = _email;
        if (email != null) {
            this.addresserLabel.setText(curMailAccount.getUserName());
            this.titleField.setText(email.getMailtitle());
            this.receiverField.setText(email.getReceiver().replace(";", ","));
            this.copyField.setText("");
            if ("reply".equalsIgnoreCase(mailtype)) {
                this.titleField.setText("Re:" + email.getMailtitle());
                this.receiverField.setText(email.getAddresser().replace(";", ","));
            } else if ("transmit".equalsIgnoreCase(mailtype)) {
                this.receiverField.setText("");
            } else if ("replyall".equals(mailtype)) {
                this.titleField.setText("Re:" + email.getMailtitle());
                String newaddress = email.getAddresser();
                if (email.getCc() != null && email.getCc().length() > 0) {
                    newaddress += "," + email.getCc().replace(";", ",");
                }
                this.receiverField.setText(newaddress);
            } else {
                this.copyField.setText(email.getCc().replace(";", ","));
                this.receiverField.setText(email.getReceiver().replace(";", ","));
            }
            String content = email.getContent();
            if (content.startsWith("<![CDDATA[")) {
                content = content.substring(10, content.length() - 2);
            }
            if (email.getAttachments() != null && email.getAttachments().size() > 0) {
                List<EMailAttachment> attachments = email.getAttachments();
                for (EMailAttachment attach : attachments) {
                    String emailid = email.get_id();
                    StringBuilder buffer = new StringBuilder();
                    buffer.append(workdir).append(emailid).append("/").append(attach.getFileName());
                    String filename = buffer.toString();
                    String content1 = saveAttachmentToTemp(content, attach, filename);
                    if (content1.equals(content) && !"reply".equalsIgnoreCase(mailtype) && !"replyall".equalsIgnoreCase(mailtype)) {
                        attachList.add(filename);
                        attachNameList.add(attach.getFileName());
                    } else {
                        content = content1;
                    }
                }
                if (attachList.size() > 0) {
                    this.showAttachmentList.setListData(attachNameList.toArray());
                    this.deleteAttachmentLabel.setVisible(true);
                }
            }
            if ("reply".equalsIgnoreCase(mailtype) || "replyall".equalsIgnoreCase(mailtype)) {
                this.htmlEditor.getTextPane().getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "<br/>\n");
                StringBuilder buffer = new StringBuilder();
                final String s = "<br/>";
                for (int j = 0; j < 4; j++) {
                    buffer.append(s);
                }
                String add = email.getAddresser();
                add = add.replace("<", "&lt;").replaceAll(">", "&gt;");
                buffer.append("在").append(MailParseUtil.parseDate(email.getSendtime())).append(",").
                        append(add).append("写道：" + "<br/>").append(content);
                this.htmlEditor.getTextPane().setText(buffer.toString());
                this.htmlEditor.getTextPane().setCaretPosition(0);
            } else if ("savecontent".equals(mailtype)) {
                this.htmlEditor.getTextPane().setText(email.getInfoId());
            } else {
                this.htmlEditor.getTextPane().setText(content);
            }
        }
        this.updateUI();
    }

    /**
     * save the image in the mail content to the system cache
     */
    private String saveAttachmentToTemp(String content, EMailAttachment eatt, String filename) {
        StreamData sd = FileManager.fetchBinaryFile(new String(eatt.getData()));
        String content1 = content;
        if (eatt.getCId() != null) {
            content1 = content.replace("cid:" + eatt.getCId(), filename);
            try {
                sd.writeToFile(filename);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            if (sd != null) {
                FileExchange.getInstance().addContextVariable(filename, sd);
            }
        }
        return content1;
    }
}
