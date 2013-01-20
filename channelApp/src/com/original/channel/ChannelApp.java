package com.original.channel;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

import org.jdesktop.application.IFrameView;
import org.jdesktop.application.SingleIFrameApplication;

public class ChannelApp extends SingleIFrameApplication {

    public static ChannelApp channelApp;

    /**
     * my internal frame.
     */
    @Override
    protected void initialize(String[] args) {
        channelApp = this;
        IFrameView frameView = getMainView();
        // Here to create your custom InternalFrame
        JInternalFrame myframe = new ChannelIFrame();
        frameView.setFrame(myframe);
        this.getMainFrame().setVisible(true);

    }

    @Override
    protected void startup() {
        this.getMainFrame().setPreferredSize(new Dimension(1024, 570));
        this.getMainFrame().pack();
    }
}
