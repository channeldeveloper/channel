package com.original.serive.channel.email;

/*
 *  com.original.app.email.ui.action.Parse.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;

import javax.mail.Message;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.original.util.log.OriLog;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-4-11 21:52:28
 */
public class Parse {

    /**
     *
     * @param msg
     */
    public static void outputMessage(Message msg) {
        try {
            String encoding = msg.getHeader("Content-Transfer-Encoding")[0];
            if ("8bit".equalsIgnoreCase(encoding)) {
                OriLog.getLogger(Parse.class).debug(new String(msg.getSubject().getBytes("iso-8859-1"), "gb2312"));
            }
        } catch (Exception ex) {
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            msg.writeTo(out);
        } catch (Exception ex) {
        }
        OriLog.getLogger(Parse.class).debug("EMail Message =================>\n " + new String(out.toByteArray()));
    }

    /**
     *
     * @param icon
     * @return
     */
    public static JButton changeButton(ImageIcon icon) {
        JButton button = new JButton();
        button.setIcon(icon);
        button.setBorder(null);
        button.setBorderPainted(false);
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setOpaque(false);
        button.setRolloverEnabled(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    /**
     *
     * @return
     */
    public static Image getAttachImage() {
        Image image = null;
        try {
            Robot robot = new Robot();
            image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
        return image;
    }
}