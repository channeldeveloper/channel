package com.original.serive.channel.email;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.widget.ODialog;
import com.original.widget.OGroupList;
import com.original.widget.OGroupPanel;
import com.original.widget.OImagePanel;
import com.original.widget.event.DragListener;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-4-24 14:15:57
 */
public class SaveInfoDialog extends ODialog {

    Logger log;// = OriLog.getLogger(SaveInfoDialog.class);
    private Font font = new Font("微软雅黑", Font.BOLD, 22);
    private Color color1 = new Color(255, 255, 255);
    private BufferedImage image;
    private EMail mail;
    private OGroupList centerPane;
    private boolean isAttach;
    private int location = -1;
    private JFrame owner = null;

    /**
     *
     * @param owner
     * @param size
     * @param _mail
     * @param title
     */
    public SaveInfoDialog(JFrame owner, Dimension size, EMail _mail, String title) {
        this(owner, size, _mail, -1, false, title);//只保存邮件内容
    }

    /**
     *
     * @param owner
     * @param size
     * @param _mail
     * @param _attach
     * @param title
     */
    public SaveInfoDialog(JFrame owner, Dimension size, EMail _mail, int _attach, String title) {
        this(owner, size, _mail, _attach, true, title);//保存邮件内容和附件
    }

    /**
     *
     * @param _owner
     * @param size
     * @param _mail
     * @param _attach
     * @param _isAttach
     * @param title
     */
    public SaveInfoDialog(JFrame _owner, Dimension size,
            EMail _mail, int _attach, boolean _isAttach, String title) {
        super(_owner, size, title, WINTYPE.CANCELONLY);
        this.getModel().setBackgroundColor(Color.white);
        color1 = this.getModel().getBackgroundColor();

        owner = _owner;
        this.mail = _mail;
        this.isAttach = _isAttach;
        location = _attach;
        cutScreen();

        centerPane = new OGroupList(632, 202, 198) {

            @Override
            public void saveInfo(String path) {
                if (isAttach == true) {//保存附件
                    saveAttachInfo(path);
                } else {//保存邮件
                    saveMailInfo(path);
                }
            }
        };
        centerPane.setSelectionModel(false);
        centerPane.getModel().setBackgroundcolor(color1);
        centerPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(212, 212, 212)));
        setDataToCenterPane();

        this.getContentPane().add(centerPane);
        this.pack();
        this.setVisible(true);
    }

    private void setDataToCenterPane() {
        List attachs = mail.getAttachments();
        if (!this.isAttach) {
            OGroupPanel singlePane = new OGroupPanel(620, 150, 0, true, false) {

                @Override
                public void postExec(ActionEvent e) {
                    centerPane.saveInfo(getPath());
                }
            };
            singlePane.setRightComponentVisible(false);
            singlePane.setVisibleCount(1);
            OImagePanel p = new OImagePanel(mail.getMailtitle(), -1, image);
            p.setOrientation(BorderLayout.EAST);
            singlePane.addImagePanel(p);
            singlePane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(212, 212, 212)));
            centerPane.addCellPanel(singlePane);
            centerPane.setSelected(0);
        } else if (attachs != null) {
            if (location == -1) {
                centerPane.setDropSource(new DragSource());
                centerPane.setDropListener(new DragListener());

                OGroupPanel singlePane = new OGroupPanel(620, 160, 32, true, true) {

                    @Override
                    public void postExec(ActionEvent e) {
                        centerPane.saveInfo(getPath());
                    }
                };
                singlePane.setVisibleCount(5);
                int count = 0;
                for (int i = 0; i < attachs.size(); i++) {
                    EMailAttachment att = (EMailAttachment) attachs.get(i);
                    if (att.getInfoId() != null && att.getInfoId().length() > 0) {
                        continue;
                    }
                    count++;
                    singlePane.addImagePanel(new OImagePanel(att.getFileName(), i, image));
                }
                if (count > 1) {
                    singlePane.setRightComponentVisible(true);
                    singlePane.setRightComponentText("打散");
                }
                singlePane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(212, 212, 212)));
                centerPane.addCellPanel(singlePane);
                if (count > 0) {
                    centerPane.setSelected(0);
                }
            } else {
                EMailAttachment att = (EMailAttachment) attachs.get(location);
                if (att.getInfoId() == null || att.getInfoId().length() == 0) {
                    OGroupPanel singlePane = new OGroupPanel(620, 150, 0, true, false) {

                        @Override
                        public void postExec(ActionEvent e) {
                            centerPane.saveInfo(getPath());
                        }
                    };
                    singlePane.setHasSelected(false);
                    singlePane.setVisibleCount(5);
                    singlePane.addImagePanel(new OImagePanel(att.getFileName(), location, image));
                    singlePane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(212, 212, 212)));
                    centerPane.addCellPanel(singlePane);
                    centerPane.setSelected(0);
                }
            }
        }
    }

    /**
     * 保存附件片段
     * @param path
     */
    public void saveAttachInfo(String path) {
        OGroupPanel cell = centerPane.getSelected();
        if (cell == null) {
            return;
        }
        saveMailAttachInfo(cell.getImagePanels(), path);
        if (centerPane.getListSize() > 1) {
            centerPane.removeCellPanel(cell);
            centerPane.setSelected(0);
        } else {
            this.closeDialog();
        }
    }

    /**
     * 保存邮件片段
     * @param path 
     */
    public void saveMailInfo(String path) {
        SaveMailPane saveMailPane = new SaveMailPane(path, mail);
        String message = mail.getMailtitle();
//        MainFrame.getMainFrame().addTab(message, saveMailPane);
        int height =300;
        if (mail.getAttachments() != null && mail.getAttachments().size() > 1) {
            height = 500;
        } 
        new SaveInfoDialog(this.owner, new Dimension(650, height), mail, -1, "保存附件");
        
        this.closeDialog();
    }

    /**
     * 保存邮件片段
     * @param selectedId
     * @param path 
     */
    public void saveMailAttachInfo(List selectedId, String path) {
        if (selectedId.size() == 0) {
            return;
        }
        SaveMailPane saveMailPane = new SaveMailPane(path, mail, true, selectedId);
//        MainFrame.getMainFrame().addTab(mail.getMailtitle(), saveMailPane);
    }

    /**
     *
     */
    public void cutScreen() {
        try {
            Robot robot = new Robot();
            image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }
}