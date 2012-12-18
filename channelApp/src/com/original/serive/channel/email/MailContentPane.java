package com.original.serive.channel.email;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;

import com.original.service.channel.ChannelMessage;
import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.oldimpl.Utils;
import com.original.service.channel.protocols.email.services.EmailReceiver;
import com.original.service.channel.protocols.email.services.MailAuthentication;
import com.original.service.channel.protocols.email.services.MailParseUtil;
import com.original.service.storage.FileManager;
import com.original.service.storage.StreamData;
import com.original.util.log.OriLog;
import com.original.widget.OButton;
import com.original.widget.OPanel;
import com.original.widget.OScrollBar;
import com.original.widget.border.LineMetteBorder;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;

/**
 * 邮件内容展示。
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-23 10:06:33
 */
public class MailContentPane extends OPanel implements ActionListener {

    Logger log = OriLog.getLogger(this.getClass());
    private EmailReceiver saveEmail = new EmailReceiver("");
    private EMail email = null;
    private String workdir = System.getProperty("user.dir") + "/temp/";
    private String mailtype = "inbox";
    private Font font1 = new Font("微软雅黑", Font.BOLD, 14);
    private OButton replyButton = new OButton("回复");
    private OButton replyAllButton = new OButton("回复全部");
    private OButton transmitButton = new OButton("转发");
    private OButton storeButton = new OButton("保存");
    private OButton deleteButton = new OButton("删除");
    private OButton signDustButton = new OButton("设置为垃圾邮件");
    private OButton stockButton = new OButton("全部保存");
    private JTextPane textPane;
    private JLabel topicLabel = new JLabel();
    private JLabel addresserLabel = new JLabel();
    private JLabel receiverLabel = new JLabel();
    private JLabel CCLabel = new JLabel();
    private JLabel timeLabel = new JLabel();
    private Color lineColor = new Color(132, 187, 233);

    private JPanel attachmentPane = new JPanel();
    private JPanel attachPaneBodys = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
    private JLabel attachMessageLabel = new JLabel("附件");
    private JLabel attachMessageShow = new JLabel("查看全部附件");
    private MailAuthentication mailAccount;
    JLabel attachlab = new JLabel("附   件:");

    /**
     *
     * @param type
     */
    public void setMailType(String type) {
        this.mailtype = type;
    }

    /**
     *
     * @param _mailAccount
     */
    public MailContentPane(MailAuthentication _mailAccount) {
        this.setBackground(Color.white);
        this.mailAccount = _mailAccount;
        init();
    }

    /**
     *
     */
    private void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JPanel buttonPane = new JPanel(new GridBagLayout());
        buttonPane.setOpaque(false);
        buttonPane.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, lineColor));
        buttonPane.add(replyButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        buttonPane.add(replyAllButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        buttonPane.add(transmitButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        buttonPane.add(storeButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        buttonPane.add(deleteButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        buttonPane.add(signDustButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        buttonPane.add(new JLabel(" "), new GridBagConstraints(6, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));

        replyButton.setLevel(BUTTONLEVEL.APPLICATION);
        replyAllButton.setLevel(BUTTONLEVEL.APPLICATION);
        transmitButton.setLevel(BUTTONLEVEL.APPLICATION);
        storeButton.setLevel(BUTTONLEVEL.APPLICATION);
        deleteButton.setLevel(BUTTONLEVEL.APPLICATION);
        signDustButton.setLevel(BUTTONLEVEL.APPLICATION);

        replyButton.setPreferredSize(new Dimension(80, 36));
        replyAllButton.setPreferredSize(new Dimension(100, 36));
        transmitButton.setPreferredSize(new Dimension(80, 36));
        storeButton.setPreferredSize(new Dimension(80, 36));
        deleteButton.setPreferredSize(new Dimension(80, 36));
        signDustButton.setPreferredSize(new Dimension(140, 36));

        JPanel topicPane = new JPanel(new GridBagLayout());
        topicPane.setOpaque(false);
        topicPane.add(topicLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel label2 = new JLabel("发件人:");
        label2.setFont(font1);
        addresserLabel.setFont(font1);
        topicPane.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(addresserLabel, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(new JLabel(""), new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));

        JLabel label5 = new JLabel("时   间:");
        label5.setFont(font1);
        timeLabel.setFont(font1);
        topicPane.add(label5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(timeLabel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));

        JLabel label3 = new JLabel("收件人:");
        label3.setFont(font1);
        receiverLabel.setFont(font1);
        topicPane.add(label3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(receiverLabel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));

        JLabel label4 = new JLabel("抄   送:");
        label4.setFont(font1);
        CCLabel.setFont(font1);
        topicPane.add(label4, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(CCLabel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        
        attachlab.setFont(font1);
        topicPane.add(attachlab, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(attachMessageLabel, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        topicPane.add(attachMessageShow, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        
        attachMessageLabel.setFont(font1);
        attachMessageShow.setFont(font1);
        attachMessageShow.setForeground(new Color(145, 190, 221));
        
        attachMessageShow.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (attachmentPane.isVisible()) {
                    attachmentPane.setVisible(false);
                } else {
                    attachmentPane.setVisible(true);
                }
            }
        });

        topicPane.setBorder(new LineMetteBorder(0, 0, 2, 0, lineColor));

        textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(880, 200));
        textPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, this.getBackground()));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        textPane.setOpaque(false);
        
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        textPane.setEditorKit(htmlKit);

        stockButton.setActionCommand("stock");
        stockButton.addActionListener(this);
        stockButton.setLevel(BUTTONLEVEL.PAGE);
        stockButton.setPreferredSize(new Dimension(100, 30));

        attachmentPane.setLayout(new GridBagLayout());
        attachmentPane.setBorder(new LineMetteBorder(2, 0, 0, 0, lineColor));
        attachmentPane.setVisible(false);
        attachmentPane.setOpaque(false);
        attachmentPane.add(stockButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        attachmentPane.add(new JLabel(" "), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));

        JScrollPane scrollPane1 = new JScrollPane(attachPaneBodys);
        scrollPane1.setHorizontalScrollBar(new OScrollBar(Adjustable.HORIZONTAL, this.getBackground()));
        scrollPane1.setPreferredSize(new Dimension(100, 106));
        scrollPane1.setBorder(null);
        attachmentPane.add(scrollPane1, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 5, 5, 5), 0, 0));

        attachPaneBodys.setOpaque(false);
        scrollPane1.setOpaque(false);
        scrollPane1.getViewport().setOpaque(false);
        
        JPanel mailPane = new JPanel(new BorderLayout());
        mailPane.setPreferredSize(new Dimension(900, 440));
        mailPane.setOpaque(false);
        mailPane.add(topicPane, BorderLayout.NORTH);
        mailPane.add(scrollPane);
        scrollPane.setOpaque(false);
        mailPane.add(attachmentPane, BorderLayout.SOUTH);

        replyButton.setActionCommand("reply");
        replyButton.addActionListener(this);
        replyAllButton.setActionCommand("replyall");
        replyAllButton.addActionListener(this);
        transmitButton.setActionCommand("transmit");
        transmitButton.addActionListener(this);
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this);
        signDustButton.setActionCommand("signDust");
        signDustButton.addActionListener(this);

        storeButton.setActionCommand("store");
        storeButton.addActionListener(this);

        this.add(buttonPane,BorderLayout.NORTH);
        this.add(mailPane);
    }

    /**
     * 将邮件内容中的图片等附件下载到系统缓存
     */
    private String saveAttachmentToTemp(String content, String emailid, EMailAttachment eatt) {
        StreamData sd = FileManager.fetchBinaryFile(new String(eatt.getData()));
        if (eatt.getCId() != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(workdir).append(emailid).append("/").append(eatt.getFileName());
            String filename = buffer.toString();
            File file = new File(filename);
            content = content.replace("cid:" + eatt.getCId(), filename);
            try {
                if (!file.isFile()) {
                    sd.writeToFile(filename);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return content;
    }

    /**
     *
     */
    public void clear() {
        storeButton.setVisible(true);
        this.topicLabel.setText("");
        this.addresserLabel.setText("");
        this.receiverLabel.setText("");
        this.CCLabel.setText("");
        this.timeLabel.setText("");
        this.textPane.setText("");
        attachPaneBodys.removeAll();
        attachmentPane.setVisible(false);
        attachMessageLabel.setText("");
        this.updateUI();
    }

    /**
     *
     */
    public void checkMailContentPaneComponent() {
        if (mailtype.equals("sendedbox")) {
            replyButton.setVisible(false);
            replyAllButton.setVisible(false);
            transmitButton.setVisible(false);
            signDustButton.setVisible(false);
        } else if (mailtype.equals("dustbox")) {
            replyButton.setVisible(false);
            replyAllButton.setVisible(false);
            transmitButton.setVisible(false);
            storeButton.setVisible(false);
            signDustButton.setVisible(false);
        } else if (mailtype.equals("outbox")) {
            replyButton.setVisible(false);
            replyAllButton.setVisible(false);
            transmitButton.setVisible(false);
            storeButton.setVisible(false);
            signDustButton.setVisible(false);
        } else if (mailtype.equals("depbox")) {
            replyButton.setVisible(false);
            replyAllButton.setVisible(false);
            transmitButton.setVisible(false);
            storeButton.setVisible(false);
            signDustButton.setVisible(false);
        }
    }

    public void setDataToGUI(ChannelMessage msg) {
    	setDataToGUI(Utils.channel2Email(msg));
    }
    /**
     *
     * @param _email
     */
    public void setDataToGUI(EMail _email) {
        this.clear();
        email = _email;
        checkMailContentPaneComponent();
        if (email != null) {
            this.topicLabel.setText(email.getMailtitle());
            this.addresserLabel.setText(email.getAddresser());
            this.receiverLabel.setText(email.getReceiver());
            this.CCLabel.setText(email.getCc());
            this.timeLabel.setText(MailParseUtil.parseDate(email.getSendtime()));
            String content = email.getContent();
            if (content.startsWith("<![CDDATA[")) {
                content = content.substring(10, content.length() - 2);
            }
            String emailid = email.get_id();
            StringBuilder buffer = new StringBuilder();
            List attachList = email.getAttachments();
            if (attachList != null) {
                Image image = Parse.getAttachImage();//截屏
                int infocount = 0;
                for (int l = 0; l < attachList.size(); l++) {
                    EMailAttachment eatt = (EMailAttachment) attachList.get(l);
                    if (eatt.getInfoId() == null || eatt.getInfoId().length() == 0) {
                        infocount++;
                    }
                    buffer.append(eatt.getFileName()).append(";");
                    if (eatt.getCId() == null) {
                        PerAttachPane perAttach = new PerAttachPane(email, l, image);
                        attachPaneBodys.add(perAttach);
                    }
                    content = this.saveAttachmentToTemp(content, emailid, eatt);
                }
                if (infocount == 0) {
                    stockButton.setVisible(false);
                }
                String attachNames = null;
//                if (buffer.length() > 30) {
//                    attachNames = buffer.toString().substring(0, 30);
//                } else {
                attachNames = buffer.toString();
//                }
                String attachMessage = String.valueOf(email.getAttachments().size()) + "个附件 ";
                attachMessageLabel.setText(attachMessage + "(" + attachNames + ")");
                Dimension size = attachMessageLabel.getPreferredSize();
                if (attachMessageLabel.getPreferredSize().getWidth() > 380) {
                    size.setSize(380, size.getHeight());
                    attachMessageLabel.setPreferredSize(size);
                }
            }
            else{
                attachlab.setVisible(false);
                attachMessageLabel.setVisible(false);
                attachMessageShow.setVisible(false);
            }
            textPane.setText(MailParseUtil.parseContent(content));
            if (email.getInfoId() != null && email.getInfoId().length() > 0) {
                storeButton.setVisible(false);
            }
        }
        this.updateUI();
    }

    /**
     *
     */
    public void refreshMailList() {
//        MailAuthentication curAccount = MainFrame.getMainFrame().getCurSelectAccount();
//        if (curAccount != null && curAccount.getUserName().equals(this.mailAccount.getUserName())) {
//            MainFrame.getMainFrame().updateMailList();
//        }
    }

    JFrame getFatherFrame() {
        Container father = this.getParent();
        while (!(father instanceof JFrame)) {
            father = father.getParent();
        }
        return (JFrame) father;
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JButton) {
            String command = ((JButton) obj).getActionCommand();
            if ("reply".equals(command) || "replyall".equals(command)) {
                String message = "Re:" + email.getMailtitle();
                sendMailPane(command, message);
            } else if ("stock".equals(command)) {
                if (email.getAttachment() != null) {
                    int height = 300;
                    if (email.getAttachments() != null && email.getAttachments().size() > 1) {
                        height = 500;
                    } 
                    SaveInfoDialog saveInfoDialog = new SaveInfoDialog(getFatherFrame(), new Dimension(650, height), email, -1, "保存附件");
                }
            } else if ("transmit".equals(command)) {
                String message = "转发:" + email.getMailtitle();
                sendMailPane(command, message);
            } else if ("delete".equals(command)) {
                delete();
            } else if ("signDust".equals(command)) {
                signDust();
            } else if ("store".equals(command)) {
                SaveInfoDialog saveInfoDialog = new SaveInfoDialog(getFatherFrame(), new Dimension(650, 300), email, "保存邮件");
            }
        }
    }

    private void sendMailPane(String type, String message) {
        SendMailPane sendMailPane = new SendMailPane();
        sendMailPane.setMailType(type);
        sendMailPane.setSelectedAccount(mailAccount);
        sendMailPane.setDataToGUI(email);
//        MainFrame.getMainFrame().addTab(message, sendMailPane);
    }

    private void delete() {
//        EmailSender deleteMail = new EmailSender("");
//        if ("depbox".equalsIgnoreCase(mailtype)) {
//            deleteMail.deleteMail(email.get_id());
//        } else {
//            EMail em = new EMail();
//            em.setIsDelete(1);
//            deleteMail.updateMail(em, email.get_id());
//        }
//        refreshMailList();
//        removePane();
    }

    private void signDust() {
//        EMail em = new EMail();
//        em.setIsTrash(1);
//        saveEmail.updateMail(em, email.get_id());
//        refreshMailList();
    }

    private void removePane() {
//        MainFrame.getMainFrame().removeTab(this);
    }
}