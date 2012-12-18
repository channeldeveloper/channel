/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.original.widget.date.OriDate;
import com.original.widget.event.ChoiceSelectionEvent;
import com.original.widget.event.ChoiceSelectionListener;
import com.original.widget.model.MultiSelectorModel;
import com.original.widget.model.StatusBarModel;
import com.original.widget.plaf.OStatusBarUI;

/**
 * 状态栏,包含了时间阶段切换器
 * @author Changjian HU
 */
public class OStatusBar extends JPanel implements ActionListener, MouseListener,
        MouseMotionListener,ChoiceSelectionListener{
    private static final String uiClassID = "OStatusBarUI";

    private int timeperoidindex = -1;
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OStatusBarUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
    private StatusBarModel model;
    Timer t = new Timer(1000,this);


    public OStatusBar(){
        initComp();
    }

    private void initComp(){
    	
//    	System.out.println("initComp");
    	if (actionMap  == null)
    	{
    		actionMap = new ActionMap();
    	}
    	
        setModel(new StatusBarModel());
        Dimension size = new Dimension(800, 30);
        setMinimumSize(size);
        setPreferredSize(size);
        setSize(size);
        setOpaque(false);
        t.start();

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
     //模型设置部分
    public void setModel(StatusBarModel model) {
        this.model = model;
    }
     public StatusBarModel getModel() {
        return model;
    }

    //UI Handeling Mechanism
    
    @Override
    public OStatusBarUI getUI() {
		return (OStatusBarUI) ui;
	}
	public void setUI(OStatusBarUI ui) {
		super.setUI(ui);
	}
    @Override
	public String getUIClassID() {
		return uiClassID;
	}


    public void setBattery(float battery){
        model.setBattery(battery);
        repaint();
    }
    public void setSignal(float signal){
        model.setSignal(signal);
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == t) { //
//            repaint(getUI().getRectTime().getBounds());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int ind = getUI().getMouseLocArea(e.getPoint());
        if(ind==2){ // TimePeriod
            configTimePeriod(e.getPoint());
        }else if(ind==1){ //HOME

            Action action = actionMap.get("home");
            if (action != null)
            {
            	action.actionPerformed(null);
            }
            //other pending...
            action = actionMap.get("lockscreen");
         
    		action = actionMap.get("shutdown");
    		
//            action.actionPerformed(e);
        }
    }
    //设置时间阶段，弹出Menu来进行设置
    protected void configTimePeriod(Point pt){
        OPopupWin popupMenu = new OPopupWin();
        popupMenu.setPopupSize(476, 70);
        OMultiSelector selector =
                new OMultiSelector(new MultiSelectorModel(MultiSelectorModel.SELECTORTYPE.COMPLEX));
        updateComplexSelector(selector);
        selector.setBounds(0, 0, 450, 53);
        popupMenu.getContentPane().setLayout(null);
        popupMenu.getContentPane().add(selector);
        popupMenu.show(this, pt.x,pt.y-70);
    }

    protected void updateComplexSelector(OMultiSelector selector){
        MultiSelectorModel tmodel = selector.getModel();
        tmodel.addComplexChoiceDataItem("硕士",
                OriDate.getDate("2000-09-08", "yyyy-MM-dd"),
                OriDate.getDate("2002-06-20", "yyyy-MM-dd"));
        tmodel.addComplexChoiceDataItem("留学",
                OriDate.getDate("2000-06-20", "yyyy-MM-dd"),
                OriDate.getDate("2005-10-23", "yyyy-MM-dd"));
        tmodel.addComplexChoiceDataItem("慧影科技",
                OriDate.getDate("2005-10-23", "yyyy-MM-dd"),
                OriDate.getDate("2008-03-25", "yyyy-MM-dd"));
        tmodel.addComplexChoiceDataItem("当前阶段",
                OriDate.getDate("2008-03-25", "yyyy-MM-dd"), null);
        //
        selector.addSelectionListener(this);

        if(timeperoidindex!=-1)
            tmodel.setSelectedIndex(timeperoidindex);
        else
            tmodel.setSelectedIndex(3);

    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {
        int ind = getUI().getMouseLocArea(e.getPoint());
        if(ind!=-1)
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
    }

    @Override
    public void choiceSelectChange(ChoiceSelectionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        //切换时间阶段
        OMultiSelector selector = (OMultiSelector)e.getSource();
        OPopupWin win = (OPopupWin)(selector.getParent().getParent());
        win.setVisible(false);
        timeperoidindex = e.getSelectedIndex();
        System.out.println(e.getData().toString());
    }
    
    /**
     * 
     * @param action
     */
    public void addAction(Action action)
    {
    	
    	if (actionMap != null)
    	{
    		actionMap.put(action.getValue(Action.NAME), action);
    	}
    }
    
    private ActionMap actionMap;
}
