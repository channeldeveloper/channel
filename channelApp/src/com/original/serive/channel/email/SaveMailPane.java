package com.original.serive.channel.email;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.oldimpl.BaseObject;
import com.original.service.channel.protocols.email.services.EmailSender;
import com.original.service.storage.FileManager;
import com.original.service.storage.StreamData;
import com.original.util.log.OriLog;
import com.original.widget.OButton;
import com.original.widget.OGroupPanel;
import com.original.widget.OImagePanel;
import com.original.widget.OPanel;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-23 13:07:52
 */
public class SaveMailPane extends OPanel implements ActionListener, MouseListener {

    Logger log = OriLog.getLogger(SaveMailPane.class);
    private EMail email = null;
    private String selectedpath = "";
//    private ISProcHandler _handler = new ISProcHandler();
    private boolean isAttach = false;
    private List<OImagePanel> locationList = null;
    private int location = 0;
    private JPanel editorPane = new JPanel(new BorderLayout());
    private Component editor = null;
    private EmailSender savemail = new EmailSender("");
    private OGroupPanel cell = null;
    private Color lineColor = new Color(132, 187, 233);

    /**
     *
     * @param path
     * @param _email
     */
    public SaveMailPane(String path, EMail _email) {
        this(path, _email, false, null);
    }

    /**
     *
     * @param path
     * @param _email
     * @param _isattach
     * @param _loc
     */
    public SaveMailPane(String path, EMail _email, boolean _isattach, List<OImagePanel> _loc) {
        selectedpath = path;
        email = _email;
        isAttach = _isattach;
        locationList = _loc;
        if (locationList != null) {
            location = (Integer) locationList.get(0).getUserData();
        }
        init();
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
        OButton saveMessageButton = new OButton("保存");
        saveMessageButton.setLevel(BUTTONLEVEL.APPLICATION);
        saveMessageButton.setPreferredSize(new Dimension(70, 36));
        saveMessageButton.setActionCommand("saveMessageButton");
        saveMessageButton.addActionListener(this);
        functionButtonPane.add(saveMessageButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));
        functionButtonPane.add(new JLabel(" "), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 2, 5, 2), 0, 0));

        editorPane.setBackground(Color.WHITE);

        this.add(functionButtonPane, BorderLayout.NORTH);
        this.add(editorPane);
        initCellPanel();
        setSelectedImagePanel(location);
    }

    private void initCellPanel() {
        if (isAttach && locationList != null && locationList.size() > 1) {
            cell = new OGroupPanel(160, 400, 0, false, false);
            cell.setImageSelected(true);
//            cell.getModel().setBackgroundcolor(Color.white);
            cell.setMouseListener(this);
            cell.setImageSelected(true);
//            cell.setImageNotBoder();
            cell.setHasSelected(false);
            cell.setVisibleCount(5);
            cell.getModel().setNotSelectColor(new Color(219,219,219));
            for (OImagePanel panel : locationList) {
                panel.getModel().setRightOffset(15);
                cell.addImagePanel(panel, false);
            }
            cell.setSelectedImagePanel(true, 0);
            editorPane.add(cell, BorderLayout.WEST);
            cell.setOpaque(false);
            editorPane.setOpaque(false);
        }
    }

    private EMailAttachment getAttachment(int loc) {
        try {
            List attachs = email.getAttachments();
            if (attachs != null) {
                return (EMailAttachment) attachs.get(loc);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private String getType() {
        if (!isAttach) {
            return "normal";
        }
        EMailAttachment att = this.getAttachment(location);
        if (att == null) {
            return "normal";
        }
        if (att.getType().equals("eio")) {
            return "yozo";
        }
        return "normal";
    }

    private Object getContent() {
        if (!isAttach) {
            String content = email.getContent();
            if (content.startsWith("<![CDDATA[")) {
                content = content.substring(10, content.length() - 2);
            }
            return content;
        }
        EMailAttachment att = this.getAttachment(location);
        if (att == null) {
            return "";
        }
        StreamData sd = FileManager.fetchBinaryFile(new String(att.getData()));
        if (this.getType().equals("yozo")) {
//            return new OriBinaryPackage(sd.getData(), this.getType());
        	return "YOZO";
        } else {
            try {
                byte[] data = sd.getData();

                String encoding = Charset.defaultCharset().toString().toLowerCase();
                if (data[0] != -17) {
                    encoding = "gb2312";
                }
                return new String(data, encoding);

            } catch (UnsupportedEncodingException ex) {
                return "";
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        EMail em = new EMail();
        String emailid = email.get_id();
        long id =-1;//= _handler.store();
        if (!isAttach) {
            if (email.getInfoId() != null && email.getInfoId().length() > 0) {
                return;
            }
            email.setInfoId("" + id);
            em.setInfoId("" + id);
//            savemail.updateMail(em, emailid);
        } else {
            EMailAttachment att = this.getAttachment(location);
            if (att.getInfoId() != null && att.getInfoId().length() > 0) {
                return;
            }
            att.setInfoId("" + id);
            em.set_id(emailid);
            BaseObject attachment = new BaseObject();
            EMailAttachment att1 = new EMailAttachment();
            att1.set_id(att.get_id());
            att1.setInfoId("" + id);
//            attachment.addList(att1);
            em.setAttachment(attachment);
//            savemail.updateMail(em);
        }
//        MainFrame.getMainFrame().updateMailList();
    }

    private void setSelectedImagePanel(int _location) {
        if (editor != null) {
            editorPane.remove(editor);
        }
        this.location = _location;
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("topic", email.getMailtitle());
        String newaddress = email.getAddresser();
        if (email.getCc() != null && email.getCc().length() > 0) {
            newaddress += "," + email.getCc();
        }
        maps.put("participants", newaddress);
        maps.put("content", getContent());
        maps.put("_editortype", getType());
        editor = null;
//        editor = _handler.genEditorPanel(this, selectedpath, maps);

        editor.setPreferredSize(new Dimension(985, 445));
        if (isAttach && locationList != null && locationList.size() > 1) {
            editor.setPreferredSize(new Dimension(825, 420));
        }
        editorPane.add(editor, BorderLayout.EAST);
        editorPane.getLayout().layoutContainer(editorPane);
//        _handler.setVisible();
    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof OImagePanel) {
            int _loc = (Integer) ((OImagePanel) obj).getUserData();
            if (_loc == location) {
                return;
            }
            cell.setSelectedImageLabel((OImagePanel) obj);
            this.setSelectedImagePanel(_loc);
            editorPane.revalidate();
            editorPane.repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}