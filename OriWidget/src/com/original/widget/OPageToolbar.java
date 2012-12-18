/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget;

import com.original.widget.draw.OriPainter;
import com.original.widget.model.PagePanelModel;
import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Icon based Toolbars.
 * A title + a Button Container
 * all icon button will be arranged in the container.
 * @author Changjian Hu
 */
public class OPageToolbar extends OPagePanel implements ActionListener{
    ActionListener lst;
    private JLabel titleBar;
    private OButtonContainer btnbar;
    private String[] btntips;
    private String[] commands;
    //根据commands信息获取对应Icon的路径组织，string.format的第一个参数
    private String iconPathGenByCommand;
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private static final Font fntLabel = new Font("微软雅黑", Font.PLAIN, 14);


    public synchronized void addActionListener(ActionListener l) {
        listeners.add(l);
        btnbar.hookChildren(l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        listeners.remove(l);
        btnbar.unhookChildren(l);
    }
    protected void fireEventInvoked(ActionEvent e) {
        for(ActionListener invoker: listeners){
            invoker.actionPerformed(e);
        }
    }
    public OPageToolbar(Dimension size, String title, String[] btntips, 
            String commands[], String iconPathGenByCommand){
        this.setSize(size);
        this.setPreferredSize(size);
        
        this.btntips = btntips;
        this.commands = commands;
        this.iconPathGenByCommand = iconPathGenByCommand;
        titleBar = new JLabel(title, SwingConstants.LEFT);
        titleBar.setBorder(BorderFactory.createEmptyBorder(0,5,0,2));
        titleBar.setFont(fntLabel);
        btnbar = new OButtonContainer(this, btntips, commands, iconPathGenByCommand);
        installDefault();
        this.setLayout(null);
        customLayout();

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public OPageToolbar(String title, String[] btntips, String commands[], String iconPathGenByCommand){
        this(new Dimension(400,32), title, btntips, commands, iconPathGenByCommand);
    }
    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        customLayout();
    }
    //初始化内容
    private void installDefault(){
        PagePanelModel model = getModel();
        model.setBackFillPattern(new Color[]{new Color(184, 212, 216),
        new Color(184,217,222)}, new Float[]{1.0f});
        model.addBorder(3, false, false, 1.0, new Color(155,183,186));
        model.addShadow(2, 3, Color.BLACK, 0.3f, Math.PI*3/2);
        model.addShadow(0, 4, Color.BLACK, 0.3f, Math.PI);
    }

     private void customLayout(){
        if(btntips==null) return;
        int count = btntips.length; //按钮个数
        int left = count*(btnbar.getBtnSize().width+5);
        this.removeAll();
        titleBar.setBounds(0, 0, getWidth()-left, getHeight());
        this.add(titleBar);
        btnbar.setHeight(getHeight());
        btnbar.setBounds(getWidth()-left, 0, left, getHeight());
        this.add(btnbar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.fireEventInvoked(e);
    }

    
    class OButtonContainer extends JPanel{
        String[] buttons;
        String[] commands;
        String iconPathGenPattern;
        OPageToolbar parent;
        int height = -1;
        Dimension btnsize = new Dimension(24,24);
        public OButtonContainer(OPageToolbar parent, String[] buttons, String[] commands, String iconPathGenPattern){
            this.buttons = buttons;
            this.commands = commands;
            
            this.iconPathGenPattern = iconPathGenPattern;
            this.parent = parent;
            this.setOpaque(false);
            this.setLayout(null);
            initButtons();

        }
        public void hookChildren(ActionListener l){
            int count = this.getComponentCount();
            for(int i=0;i<count;i++){
                Component c = this.getComponent(i);
                if(c instanceof OriToggleBtn){
                    ((OriToggleBtn)c).addActionListener(parent);
                }
            }
        }
        public void unhookChildren(ActionListener l){
            int count = this.getComponentCount();
            for(int i=0;i<count;i++){
                Component c = this.getComponent(i);
                if(c instanceof OriToggleBtn){
                    ((OriToggleBtn)c).removeActionListener(parent);
                }
            }
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
                String sIcon = String.format(this.iconPathGenPattern, commands[i]);
                OriToggleBtn btn = new OriToggleBtn(OImageLoad.getImageIcon(sIcon), commands[i],
                        buttons[i]);
                btn.addActionListener(parent);
                btn.setBounds(offsetx, offsety, btnsize.width, btnsize.height);
                this.add(btn);
                offsetx+=(btnsize.width+5);
            }

        }
    }
    
    class OriToggleBtn extends JComponent{
        public final int XMARGIN=2,YMARGIN=2;
        public final int ACTIVE=1,IDLE=0,PRESSED=2;
        Color clrTranStart = null;
        Color clrTranEnd = null;
        Color clrSelBorder = null;
        Color clrHoverBorder = null;
        int roundCornerX = 6;
        int roundCornerY = 6;

        boolean selected = false;
        ActionListener lst;

        String txt,cmd;
        public int state;
        int align;
        private ImageIcon icon = null;

        // ctor
        public OriToggleBtn(String str,int alg){
            align=alg;
            cmd=txt=str;
            state=IDLE;
            enableEvents(AWTEvent.MOUSE_EVENT_MASK );
        }
        public OriToggleBtn(String str){
            this(str,Label.CENTER);
        }
        public OriToggleBtn(){
            this("",Label.LEFT);
        }
        public OriToggleBtn(ImageIcon icon, String cmd, String tip){
            this("",Label.LEFT);
            this.icon = icon;
            this.cmd = cmd;
            setToolTipText(tip);
            clrTranStart = new Color(246, 246, 246);
            clrTranEnd = new Color(220, 220, 220);
            clrSelBorder = new Color(84,84,84);
            clrHoverBorder = new Color(168,168,168);

        }
        // paint
        @Override
        public void paint(Graphics g){
            super.paintComponents(g);
            FontMetrics fm=getFontMetrics(getFont());
            int wt=fm.stringWidth(txt);
            int ht=fm.getHeight();
            Rectangle r=getBounds();
            int w=r.width;
            int h=r.height;
            int x=(w-wt) >> 1;
            int y=((h+ht) >> 1)-2;
            if(align == Label.LEFT) x=0;
            else if(align == Label.RIGHT) x=w-wt;
            else
                x = (w-wt)/2;
            RoundRectangle2D r2d = new RoundRectangle2D.Double (0, 0, w, h, roundCornerX,roundCornerY);
            RoundRectangle2D r2d2 = new RoundRectangle2D.Double (0, 0, w-1, h-1, roundCornerX,roundCornerY);
            if(state == PRESSED){
                OriPainter.gradientFillArea(g, new Area(r2d), clrTranEnd, clrTranStart,  true);
                OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d2), clrSelBorder, 1);
            }
            if(state==ACTIVE){
                OriPainter.gradientFillArea(g, new Area(r2d), clrTranStart, clrTranEnd,   true);
                OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d2), clrHoverBorder, 1);
            }
            if(this.icon!=null)
                g.drawImage(this.icon.getImage(), (w-icon.getIconWidth())/2, (h-icon.getIconHeight())/2, null);
            else
                g.drawString(txt,x,y);
        }
        // getMinimumSize
        @Override
        public Dimension getMinimumSize(){
            FontMetrics fm=getFontMetrics(getFont());
            int h=fm.getHeight() + YMARGIN + YMARGIN;
            int w=fm.stringWidth(txt) + XMARGIN + XMARGIN;
            return new Dimension(w,h);
        }
        // minimumSize
        @Override
        public Dimension minimumSize(){
            return getMinimumSize();
        }
        // preferredSize
        @Override
        public Dimension preferredSize(){
            return getMinimumSize();
        }
        // getPreferredSize
        @Override
        public Dimension getPreferredSize(){
            return getMinimumSize();
        }
        //
        public void released(){
            int a=state;
            state=ACTIVE;
            if(a != state) repaint();
            postAction();
        }
        //
        public void pressed(){
            int a=state;
            state=PRESSED;
            selected = !selected;
            if(a != state) repaint();
        }
        //
        public void entered(){
            int a=state;
            state=ACTIVE;
            if(a != state) repaint();
        }
        //
        public void exited(){
            int a=state;
            state=IDLE;
            if(a != state) repaint();
        }
        //
        @Override
        public void processEvent(AWTEvent e){
            if(e.getID() == MouseEvent.MOUSE_PRESSED) pressed();
            else if(e.getID() == MouseEvent.MOUSE_ENTERED) entered();
            else if(e.getID() == MouseEvent.MOUSE_EXITED) exited();
            else if(e.getID() == MouseEvent.MOUSE_RELEASED) released();
            super.processEvent(e);
        }
        //
        public void setActionCommand(String c){
            cmd=c;
        }
        //
        public void addActionListener(ActionListener l){
            lst=AWTEventMulticaster.add(lst,l);
        }
        //
        public void removeActionListener(ActionListener l){
            lst=AWTEventMulticaster.remove(lst,l);
        }
        //
        private void postAction(){
            if(lst != null){
                ActionEvent event = new
                ActionEvent(this,ActionEvent.ACTION_PERFORMED,cmd) ;
                lst.actionPerformed(event);
            }
        }
    }
}
