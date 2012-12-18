/*
 *  com.original.widget.OInnerToolbar.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;
import com.original.widget.model.PagePanelModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * (Class Annotation.)
 * 页面内用的Toolbar，非系统级别的
 * 
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-26 23:54:36
 */
public class OAppToolbar extends OPagePanel implements ActionListener {
    private JLabel titleBar;
    private OButtonContainer btnbar;
    private String[] buttons = null;
    private static final Font fntLabel = new Font("微软雅黑", Font.PLAIN, 16);
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    public synchronized void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }
    protected void fireEventInvoked(ActionEvent e) {
        for(ActionListener invoker: listeners){
            invoker.actionPerformed(e);
        }
    }
    public OAppToolbar(Dimension size, String title, String[] buttons, String[] commands){
        this.buttons = buttons;
        
        titleBar = new JLabel(title, SwingConstants.LEFT);
        titleBar.setBorder(BorderFactory.createEmptyBorder(0,5,0,2));
        titleBar.setFont(fntLabel);
        btnbar = new OButtonContainer(this, buttons, commands);

        installModel();
        this.setSize(size);
        this.setPreferredSize(size);
        
        this.setLayout(null);
        //customLayout();
    }
    //初始化内容
    private void installModel(){
        PagePanelModel model = getModel();
        model.setBackFillPattern(new Color[]{new Color(247, 251, 251),
        new Color(202,229,238)}, new Float[]{1.0f});
        model.addBorder(3, false, false, 1.0, Color.WHITE);
    }
    public OAppToolbar(String title, String[] buttons, String[] commands){
       this(new Dimension(400,40), title, buttons, commands);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        customLayout();
    }

    private void customLayout(){
        if(buttons==null) return;
        int count = buttons.length; //按钮个数
        int left = count*(btnbar.getBtnSize().width+10);
        this.removeAll();
        titleBar.setBounds(0, 0, getWidth()-left, getHeight());
        this.add(titleBar);
        btnbar.setHeight(getHeight());
        btnbar.setBounds(getWidth()-left, 0, left, getHeight());
        this.add(btnbar);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        fireEventInvoked(e);
    }

    public void setTitle(String title){
        this.titleBar.setText(title);
        this.updateUI();
    }


    class OButtonContainer extends JPanel{
        String[] buttons;
        String[] commands;
        OAppToolbar parent;
        int height = -1;
        Dimension btnsize = new Dimension(80,32);
        public OButtonContainer(OAppToolbar parent, String[] buttons, String[] commands){
            if(buttons!=null){
                this.buttons = buttons;
                this.commands = commands;
            }
            else{
                this.buttons  = new String[]{"确定","取消"};
                this.commands = new String[]{"ok", "cancel"};
            }
            this.parent = parent;
            this.setOpaque(false);
            this.setLayout(null);
            initButtons();
            
        }
        public void setHeight(int height){
            this.height = height;
            initButtons();
        }
        public Dimension getBtnSize(){
            return btnsize;
        }
        private void initButtons(){
            if(this.height==-1) return;

            int len = buttons.length;
            int offsetx = 0;
            int offsety = (this.height-btnsize.height)/2;
            this.removeAll();
            for(int i=0;i<len;i++){
                OButton btn = new OButton(buttons[i]);
                btn.setLevel(BUTTONLEVEL.SYSTEM);
                btn.setActionCommand(commands[i]);
                btn.addActionListener(parent);
                btn.setBounds(offsetx, offsety, btnsize.width, btnsize.height);
                this.add(btn);
                offsetx+=(btnsize.width+10);
            }

        }
    }
}
