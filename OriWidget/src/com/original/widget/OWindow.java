/*
 *  com.original.widget.OWindow.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.draw.AWTUtilWrapper;
import com.original.widget.draw.OriImgProcer;
import com.original.widget.model.DialogModel;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

/**
 * (Class Annotation.)
 * 非模式对话框
 * 修改记录：
 * 0. 增加拖拽 hucj 06-12/2012
 * 1. 修改一个对话框标准形式 hucj 06-12/2012
 * 2. 增加关闭按钮 hucj 06-12/2012
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 22:24:01
 */
public class OWindow extends JWindow implements ActionListener, MouseListener,
        MouseMotionListener, javax.swing.RootPaneContainer{

    private static final long serialVersionUID = -6497888280847098839L;


    public static enum WINTYPE {CANCELONLY, OKCANCEL,BLANK};
    protected DialogModel model;
    protected boolean isapplyeffect = false;
    private JPanel container;
    private String wintitle;
    private WINTYPE winstyle;
    private JLabel titleBar;
    private OButtonContainer btnbar;
    private OWindowDragger windragger = null;
    private final int HEADERHEIGHT = 42;
    private final int FOOTERHEIGHT = 48;
    private boolean resized = true;

    private Rectangle rectIcon = null;
    private boolean mouseRolloverCloseBtn = false;

    public OWindow(JFrame owner, Dimension size){
        this(owner, size, null, WINTYPE.BLANK);
    }
    public OWindow(JFrame owner, Dimension size, String wintitle){
        this(owner, size, wintitle, WINTYPE.CANCELONLY);
    }

    //初始化对象
    public OWindow(JFrame owner, Dimension size, String wintitle, WINTYPE wintype) {
        super(owner);

        //属性设置
        this.wintitle = wintitle;
        this.winstyle = wintype;
        this.setMinimumSize(size);
        this.setSize(size);

        //模型设置
        model = new DialogModel();
        super.getContentPane().setLayout(null);
        if(winstyle!=WINTYPE.BLANK){
            //内部Pane创建
            
            container = new JPanel();
            container.setLayout(new BorderLayout());
            container.setOpaque(false);
            //container.setBackground(Color.gray);
            super.getContentPane().add(container);
        }
        //设置透明
        initWindowStyle();
        if(winstyle!=WINTYPE.BLANK)
            //设置标题/ButtonPanel
            initTitlebar();
        this.setFocusableWindowState(true);
    }

    //设置窗口透明和相关形状
    private void initWindowStyle(){
        if(this.getWidth()>0 && !this.isapplyeffect){
            AWTUtilWrapper.applySpecialEffect(this, model.getCornerradius(), 100);
            isapplyeffect = true;
        }

    }

    //重写窗口的ContentPane以保证用户添加正常
    @Override
    public Container getContentPane(){
        if(winstyle!=WINTYPE.BLANK)
            return container;
        else
            return super.getContentPane();
    }

    private void initTitlebar(){
        titleBar = new JLabel(wintitle, SwingConstants.CENTER);
        titleBar.setFont(model.getDialogTitleFont());
		super.getContentPane().add(titleBar);
        titleBar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        //titleBar.setOpaque(false);//设置透明
        titleBar.setBackground(Color.red);
        btnbar = new OButtonContainer(this, this.winstyle);
        super.getContentPane().add(btnbar);
        //btnbar.setBackground(Color.red);
        windragger = new OWindowDragger(this, titleBar);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    //模型设置部分
    public void setModel(DialogModel model) {
        this.model = model;
    }

    public DialogModel getModel() {
        return model;
    }


    @Override
    public void paint(Graphics g){
        drawShadow(g);
        drawFrame(g);
        super.paintComponents(g);
    }


    protected void drawFrame(Graphics g){
        int height = this.getHeight();
        int width = this.getWidth();
        Graphics2D g2x = (Graphics2D)g;
        g2x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2x.setPaint(model.getBackgroundColor());
        //g2x.setStroke(new BasicStroke(1));
        int offsetx = model.getShadowsize();
        int shrink = model.getCornerradius();
        //绘制背景
        RoundRectangle2D r2d = new RoundRectangle2D.Double(offsetx-1, offsetx-1, width-offsetx*2+2, height-offsetx*2+2,
                model.getCornerradius(), model.getCornerradius());

        g2x.fill(r2d);
        if(winstyle==WINTYPE.BLANK) return;
        //draw the titlebar
        Area title = new Area(r2d);
        title.subtract(new Area(new Rectangle(0, HEADERHEIGHT, width, height)));

        g2x.setPaint(model.getTitleBackColor());
        g2x.fill(title);
        g2x.setColor(model.getSepartorColor());
        //the title line.
        g2x.drawLine(offsetx, HEADERHEIGHT, width-offsetx, HEADERHEIGHT);

        //the Button line.
        g2x.drawLine(offsetx, height-offsetx-FOOTERHEIGHT,
                width-offsetx, height-offsetx-FOOTERHEIGHT);

        int margin = 0;
        if(resized && this.getWidth()>0 && this.getHeight()>0){
            titleBar.setBounds(shrink, offsetx,
                    width-shrink*2-12, HEADERHEIGHT-offsetx);
            container.setBounds(offsetx+margin, HEADERHEIGHT+margin,
                    width-offsetx*2-margin*2,
                    height-offsetx-HEADERHEIGHT-FOOTERHEIGHT-margin*2);
            btnbar.setBounds(shrink, height-offsetx-FOOTERHEIGHT,
                    width-shrink*2, FOOTERHEIGHT);
            resized = false;
        }
        //draw close icon.
        drawCloseIcon(g);
    }

    //draw close icon
    private void drawCloseIcon(Graphics g){
         Rectangle rect = this.getBounds();

         //计算绘制叉的区域的左定点
         int radius = 12;
         int x = rect.width-model.getCornerradius()-radius-2;
         int offset = 2;
         int y = model.getShadowsize()
                 -model.getShadowdistance()
                 +model.getCornerradius()-6;

         rectIcon = new Rectangle(x, y, radius, radius );
         GeneralPath path = new GeneralPath();
         path.moveTo(x+offset, y+offset);
         path.lineTo(x+radius-offset, y+radius-offset);
         path.moveTo(x+radius-offset, y+offset);
         path.lineTo(x+offset, y+radius-offset);

         Graphics2D g2d = (Graphics2D)  g;
         if(mouseRolloverCloseBtn)
            g2d.setPaint(new Color(69,160,249));
         else
            g2d.setPaint(new Color(144,144,144));
         g2d.setStroke(new BasicStroke(3.2f,                     // Line width
                            BasicStroke.CAP_ROUND,    // End-cap style
                            BasicStroke.JOIN_ROUND));
         g2d.draw(path);
     }

    /**
     * draw the shadow
     * @param g
     */
    protected void drawShadow(Graphics g){
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2d = (Graphics2D)g;
        //g2d.setColor(new Color(255, 255, 255, 0));
        //g2d.fillRect(0, 0, width, height);
        //if(shadow==null) {
        BufferedImage shadow = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2x = (Graphics2D)shadow.getGraphics();
        g2x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2x.setColor(new Color(255,255,255,0));
        g2x.fillRect(0, 0, width, height);
        g2x.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        g2x.fillRoundRect(model.getShadowsize(), model.getShadowsize(), width-2*model.getShadowsize(),
                height-2*model.getShadowsize(), model.getCornerradius(), model.getCornerradius());

        g2d.drawImage(OriImgProcer.blurImage(shadow,
                12.5f, 2.0f, (float)Math.PI*1.5f, 0.4f), 0, 0, null);
        g2x.dispose();
        shadow.flush();
        shadow = null;
    }


    protected void beforeDispose(){

    }

    //跟踪和处理窗口拖拽
    class OWindowDragger{
        private Window fWindow;
        private Component fComponent;
        private int dX;
        private int dY;

        /**
         * 让指定的Component通过鼠标拖动来移动Window
         * @param window
         * @param component
         */
        public OWindowDragger(Window window, Component component) {
            fWindow = window;
            fComponent = component;
            fComponent.addMouseListener(createMouseListener());
            fComponent.addMouseMotionListener(createMouseMotionListener());
        }

        private MouseListener createMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point clickPoint = new Point(e.getPoint());
                    SwingUtilities.convertPointToScreen(clickPoint, fComponent);
                    dX = clickPoint.x - fWindow.getX();
                    dY = clickPoint.y - fWindow.getY();
                }
            };
        }
        private MouseMotionAdapter createMouseMotionListener() {
            return new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point dragPoint = new Point(e.getPoint());
                    SwingUtilities.convertPointToScreen(dragPoint, fComponent);
                    fWindow.setLocation(dragPoint.x - dX, dragPoint.y - dY);
                }
            };
        }
    }
    //内部Button Toolbar
    class OButtonContainer extends JPanel{
        WINTYPE type = WINTYPE.CANCELONLY;
        OWindow parent;
        public OButtonContainer(OWindow parent, WINTYPE type){
            this.type = type;
            this.parent = parent;
            this.setOpaque(false);
            initButtons();
        }

        private void initButtons(){
            if(type==WINTYPE.CANCELONLY){
                this.setLayout(new MigLayout("fillx", "[center]", "[top][14!]"));
                OButton btn = new OButton("取消");
                //btn.setSize(100, 36);
                btn.setLevel(BUTTONLEVEL.APPLICATION);
                this.add(btn, "width 100!, height 36!, wrap");
                btn.setActionCommand("cancel");
                btn.addActionListener(parent);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else{
                this.setLayout(new MigLayout("fillx", "[right]10[left]", "[top][14!]"));
                OButton btn = new OButton("确定");
                btn.setLevel(BUTTONLEVEL.APPLICATION);
                btn.setActionCommand("ok");
                btn.addActionListener(parent);
                this.add(btn, "width 100!, height 36!");
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btn = new OButton("取消");
                btn.setLevel(BUTTONLEVEL.APPLICATION);
                btn.setActionCommand("cancel");
                btn.addActionListener(parent);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                this.add(btn, "width 100!, height 36!, wrap");

            }
        }
    }

     protected void closeWindow(){
        beforeDispose();
        dispose();
        setVisible(false);
    }

    public void actionOKPerformed(ActionEvent e){
        closeWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("cancel")){
            closeWindow();
        }else if(e.getActionCommand().equals("ok")){
            actionOKPerformed(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        procMouseClick(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        procMouseExit();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        procMouseMoved(e);
    }
     public void procMouseExit(){
        if(rectIcon==null) return;
        mouseRolloverCloseBtn = false;
        this.repaint(rectIcon.x, rectIcon.y, rectIcon.width, rectIcon.height);
    }
    public void procMouseClick(MouseEvent e){
        if(rectIcon==null) return;
        Point pt = e.getPoint();
        if(rectIcon.contains(pt)){
            closeWindow();
        }
    }
    public void procMouseMoved(MouseEvent e){
        if(rectIcon==null) return;
        Point pt = e.getPoint();
        if(rectIcon.contains(pt)){
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            mouseRolloverCloseBtn = true;
        }else{
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            mouseRolloverCloseBtn = false;
        }
        this.repaint(rectIcon.x, rectIcon.y, rectIcon.width, rectIcon.height);
    }
}
