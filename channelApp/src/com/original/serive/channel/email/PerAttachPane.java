package com.original.serive.channel.email;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.storage.FileManager;
import com.original.service.storage.StreamData;
import com.original.widget.OImagePanel;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-3-12 11:34:17
 */
public class PerAttachPane extends JPanel {

    private EMail email = null;
    private int location;
    private Image image;
    private EMailAttachment eatt = null;

    /**
     *
     * @param _obj
     * @param _location
     * @param image
     */
    public PerAttachPane(EMail _obj, int _location, Image image) {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.email = _obj;
        this.image = image;
        this.location = _location;
        init();
    }

    /**
     *
     */
    public void init() {
        String title = "";
        if (location == -1) {
            title = email.getMailtitle();
        } else {
            List attachs = email.getAttachments();
            if (attachs != null && location >= 0 && location < attachs.size()) {
                this.eatt = (EMailAttachment) attachs.get(location);
                title = eatt.getFileName();
            }
        }
        OImagePanel imageLabel = new OImagePanel(title, location, 100, 70, image);
//        imageLabel.getModel().setLeftOffset(10);
//        imageLabel.getModel().setRightOffset(10);
        imageLabel.getModel().setBackgroundcolor(Color.white);
        JPanel actionPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
        actionPane.setOpaque(false);
        JLabel openlabel = new JLabel("打开");
        openlabel.setForeground(new Color(145, 190, 221));
        openlabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                saveAttachment();
            }
        });
        JLabel storelabel = new JLabel("保存");
        storelabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                new SaveInfoDialog(getFatherFrame(), new Dimension(650, 300), email, location, "保存附件");
            }
        });
        storelabel.setForeground(new Color(145, 190, 221));
        actionPane.add(storelabel);
        actionPane.add(openlabel);

        if (eatt != null && eatt.getInfoId() != null && eatt.getInfoId().length() > 0) {
            storelabel.setVisible(false);
        }
        this.add(imageLabel, BorderLayout.NORTH);
        this.add(actionPane);
    }

    /**
     *
     */
    public void saveAttachment() {
        if (eatt == null) {
            return;
        }
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = jfc.getSelectedFile().getPath();
            String fileName = eatt.getFileName();
            StreamData sd = FileManager.fetchBinaryFile(new String(eatt.getData()));
            File file = new File(filePath + "\\" + fileName);
            if (file.isFile()) {
                int number = (int) Math.random() * 100;
                String newFileName = null;
                if (fileName.contains(".") == true) {
                    newFileName = fileName.substring(0, fileName.indexOf("."));
                }
                file = new File(filePath + "\\" + newFileName + number + "." + eatt.getType());
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte b[] = sd.getData();
                if (b != null) {
                    bos.write(b);
                    fos.flush();
                    bos.flush();
                }
                fos.close();
                bos.close();
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
        }
    }

    JFrame getFatherFrame() {
        Container father = this.getParent();
        while (!(father instanceof JFrame)) {
            father = father.getParent();
        }
        return (JFrame) father;
    }
}