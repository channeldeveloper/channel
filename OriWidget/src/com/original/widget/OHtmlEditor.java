/*
 *  com.original.widget.OHtmlEditor.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.draw.OriPainter;
import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.TextBlockModel;
import com.original.widget.plaf.OHtmlEditorUI;
import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 5, 2012 10:07:44 PM
 */
public class OHtmlEditor extends JPanel implements ActionListener,
        KeyListener {

    private static final String uiClassID = "OHtmlEditorUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OHtmlEditorUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private TextBlockModel model;
    private DrawAreaChangeListener listener;

    protected JTextPane jtpMain;
    private StyledEditorKit.BoldAction actionFontBold;
	private StyledEditorKit.ItalicAction actionFontItalic;
	private StyledEditorKit.UnderlineAction actionFontUnderline;
    private AlignAction actionAlignLeft;
	private AlignAction actionAlignCenter;
	private AlignAction actionAlignRight;
	private AlignAction actionAlignJustified;
    final private Font _font = new Font("微软雅黑", Font.PLAIN, 14);

    protected UndoManager undoMngr;
	protected UndoAction undoAction;
	protected RedoAction redoAction;

    private String _format = "normal";

    public OHtmlEditor(){
        init();
    }

    private void init(){
        setModel(new TextBlockModel(TextBlockModel.TextType.TextArea));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(300, 300));
		this.setMinimumSize(new Dimension(300, 300));
        this.setSize(new Dimension(300, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.anchor     = GridBagConstraints.NORTH;
        gbc.gridheight = 1;
        gbc.gridwidth  = 1;
        gbc.weightx    = 1.0;
        gbc.weighty    = 0.0;
        gbc.gridx      = 1;

        jtpMain = new JTextPane();
        jtpMain.setContentType("text/html");
        jtpMain.setFont(_font);
        jtpMain.setMargin(new Insets(4, 4, 4, 4));
        jtpMain.setBackground(model.getBackgroundcolor());
        /* Create the scroll area for the text pane */
		JScrollPane jspViewport = new JScrollPane(jtpMain,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//jspViewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        jspViewport.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL,
                model.getBackgroundcolor()));
        jspViewport.setBackground(model.getBackgroundcolor());
        
        jspViewport.setBorder(null);
        OriHtmlEditorFormatBar toolbar = new OriHtmlEditorFormatBar();
        toolbar.addActionListener(this);

        gbc.gridy      = 1;
        this.add(toolbar, gbc);
        gbc.anchor     = GridBagConstraints.SOUTH;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.weighty    = 1.0;
        gbc.gridy      = 2;
		this.add(jspViewport, gbc);

        actionFontBold        = new StyledEditorKit.BoldAction();
		actionFontItalic      = new StyledEditorKit.ItalicAction();
		actionFontUnderline   = new StyledEditorKit.UnderlineAction();

        actionAlignLeft       = new AlignAction(this, "AlignLeft", StyleConstants.ALIGN_LEFT);
		actionAlignCenter     = new AlignAction(this, "AlignCenter", StyleConstants.ALIGN_CENTER);
		actionAlignRight      = new AlignAction(this, "AlignRight", StyleConstants.ALIGN_RIGHT);
		actionAlignJustified  = new AlignAction(this, "AlignJustified", StyleConstants.ALIGN_JUSTIFIED);


        undoMngr = new UndoManager();
		undoAction = new UndoAction();
		redoAction = new RedoAction();

		jtpMain.getDocument().addUndoableEditListener(new CustomUndoableEditListener());
        jtpMain.addKeyListener(this);
        jtpMain.setBorder(BorderFactory.createEmptyBorder(4,4,0,4));


    }

     //模型设置部分
    public void setModel(TextBlockModel model) {
        this.model = model;
    }
     public TextBlockModel getModel() {
        return model;
    }

   
    //设置UI
    public void setUI(OHtmlEditorUI ui) {
		super.setUI(ui);
	}

    @Override
    public OHtmlEditorUI getUI() {
		return (OHtmlEditorUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}
   
    public JTextPane getTextPane(){
        return this.jtpMain;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals("font-bold"))
             actionFontBold.actionPerformed(e);
        else if(cmd.equals("font-italic"))
            actionFontItalic.actionPerformed(e);
        else if(cmd.equals("font-underline"))
            actionFontUnderline.actionPerformed(e);
        else if(cmd.equals("paragraph-left-align"))
            actionAlignLeft.actionPerformed(e);
        else if(cmd.equals("paragraph-center-align"))
            actionAlignCenter.actionPerformed(e);
        else if(cmd.equals("paragraph-right-align"))
            actionAlignRight.actionPerformed(e);
        else if(cmd.equals("paragraph-justify-align"))
            actionAlignJustified.actionPerformed(e);
    }

    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if(e.isControlDown() && e.getKeyCode()=='Z'){
            undoAction.actionPerformed(null);
        }else if(e.isControlDown() && e.getKeyCode()=='Y'){
            redoAction.actionPerformed(null);
        }
    }

    //Inner Classes
    class UndoAction extends AbstractAction
	{
		public UndoAction()
		{
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
                if(undoMngr.canUndo())
				undoMngr.undo();
			}
			catch(CannotUndoException ex)
			{
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		protected void updateUndoState()
		{
			setEnabled(undoMngr.canUndo());
		}
	}

	/** Class for implementing Redo as an autonomous action
	  */
	class RedoAction extends AbstractAction
	{
		public RedoAction()
		{
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
                if(undoMngr.canRedo())
				undoMngr.redo();
			}
			catch(CannotUndoException ex)
			{
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		protected void updateRedoState()
		{
			setEnabled(undoMngr.canRedo());
		}
	}

	/** Class for implementing the Undo listener to handle the Undo and Redo actions
	  */
	class CustomUndoableEditListener implements UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent uee)
		{
			undoMngr.addEdit(uee.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}
    class AlignAction extends StyledEditorKit.AlignmentAction
    {
        protected OHtmlEditor parent;

        public AlignAction(OHtmlEditor parent, String actionName, int actionType)
        {
            super(actionName, actionType);
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            int selStart = parent.getTextPane().getSelectionStart();
            int selEnd = parent.getTextPane().getSelectionEnd();

            parent.getTextPane().setText(parent.getTextPane().getText());
            parent.getTextPane().setSelectionStart(selStart);
            parent.getTextPane().setSelectionEnd(selEnd);

            super.actionPerformed(ae);
        }
    }

    //Editor Toolbar.
    class OriHtmlEditorFormatBar extends JPanel {
        ActionListener lst;
        int offset_x = 5;
        int offset_y = 5;
        int button_x_ext = 4;
        int button_y_ext = 4;
        Color clrTranStart = null;
        Color clrTranEnd = null;
        Color clrTopButton = null;
        public OriHtmlEditorFormatBar(){
            //this.setOpaque(false);
            this.setMinimumSize(new Dimension(190, 34));
            this.setPreferredSize(new Dimension(190, 34));
            init();
            enableEvents(AWTEvent.MOUSE_EVENT_MASK );
            this.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 1));
            clrTranStart = new Color(246, 246, 246);
            clrTranEnd = new Color(220, 220, 220);
            clrTopButton = new Color(211,211,211);
        }

        @Override
        public void paintComponent(Graphics g){
            g.clearRect(0, 0, getWidth(), getHeight());
            super.paintComponents(g);
            RoundRectangle2D r = new RoundRectangle2D.Double(0, 0, getWidth()-1,
                    getHeight()-1,5, 5);
            OriPainter.gradientFillArea(g, new Area(r), clrTranStart, clrTranEnd, true);
            r = new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-2, 5,5);
            OriPainter.drawAreaBorderWithSingleColor(g, new Area(r),
                    clrTopButton, 1);

        }

        public void addActionListener(ActionListener l){
            lst=AWTEventMulticaster.add(lst,l);
            hookChildren(lst);
        }
        //
        public void removeActionListener(ActionListener l){
            lst=AWTEventMulticaster.remove(lst,l);
            unhookChildren(l);
        }

        private ImageIcon getToolbarIcon(String iconName){
            URL imageURL = getClass().getResource("icons/" + iconName + ".png");
            if(imageURL != null)
            {
                return new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageURL));
            }
            return (ImageIcon)null;
        }
        private void init(){
            setLayout(null);
            String icons[] = new String[]{
              "bold", "italic", "underline", "sep",
              "leftalign", "centeralign", "rightalign","justifyalign"
            };
            String cmds[] = new String[]{
                "font-bold", "font-italic", "font-underline", "sep",
                "paragraph-left-align", "paragraph-center-align", "paragraph-right-align",
                "paragraph-justify-align"
            };
            int x = offset_x;
            int y = offset_y;

            for(int i=0;i<icons.length;i++){

                if(icons[i].compareTo("sep")==0) x+=10;
                else{
                    ImageIcon icon = getToolbarIcon(icons[i]);
                    OriToggleBtn btn = new OriToggleBtn(icon,
                            cmds[i]);
                    btn.setBounds(x, y, icon.getIconWidth()+button_x_ext,
                            icon.getIconHeight()+button_y_ext);
                    add(btn);
                    x+= (icon.getIconWidth()+button_x_ext);
                }
            }
        }
        private void hookChildren(ActionListener l){
            int count = this.getComponentCount();
            for(int i=0;i<count;i++){
                Component c = this.getComponent(i);
                if(c instanceof OriToggleBtn){
                    ((OriToggleBtn)c).addActionListener(l);
                }
            }
        }
        private void unhookChildren(ActionListener l){
            int count = this.getComponentCount();
            for(int i=0;i<count;i++){
                Component c = this.getComponent(i);
                if(c instanceof OriToggleBtn){
                    ((OriToggleBtn)c).removeActionListener(l);
                }
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

            lst=null;
        }
        public OriToggleBtn(String str){
            this(str,Label.CENTER);
        }
        public OriToggleBtn(){
            this("",Label.LEFT);
        }
        public OriToggleBtn(ImageIcon icon, String cmd){
            this("",Label.LEFT);
            this.icon = icon;
            this.cmd = cmd;
            setToolTipText(cmd);
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

