/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget;

import com.original.widget.model.IrregularButtonModel;
import com.original.widget.plaf.OIrregularButtonUI;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * 非规则形状的Button
 * 基本原理，采用透明处理，并将对应事件进行Dipatch到其父窗口。
 *
 * @author Changjian Hu
 */
public class OIrregularButton extends JPanel implements MouseListener,
            MouseMotionListener {
    private static final String uiClassID = "OIrregularButtonUI";

	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OIrregularButtonUI");
	}
    List<ActionListener> listeners = new ArrayList<ActionListener>();

    private String commandString = "channelswitcher";
    private IrregularButtonModel model;

    @Override
    public OIrregularButtonUI getUI() {
		return (OIrregularButtonUI) ui;
	}
    public void setUI(OIrregularButtonUI ui) {
		super.setUI(ui);
	}
    @Override
    public String getUIClassID() {
		return uiClassID;
	}

    public OIrregularButton(){
        this("channelswitcher");
    }
    public OIrregularButton(String sCommandTitle){
        this(sCommandTitle, new Dimension(45, 91));
    }
    public OIrregularButton(String sCommandTitle, Dimension size){
        setModel(new IrregularButtonModel());
        this.commandString = sCommandTitle;
        setOpaque(false);
		this.setSize(size);
        this.setMinimumSize(size);
        setBorder(null);
        initComp();
    }

    public IrregularButtonModel getModel(){
        return model;
    }
    public void setModel(IrregularButtonModel model){
        this.model = model;
    }

    private void initComp(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    //Event Processing - Begin
    public synchronized void addActionListener(ActionListener l){
        this.listeners.add(l);
    }
    public synchronized void removeActionListener(ActionListener l){
        this.listeners.remove(l);
    }
    public synchronized void fireClick(){
        ActionEvent e = new ActionEvent(this, 1, this.commandString);
        for(ActionListener l: listeners){
            l.actionPerformed(e);
        }
    }
    //Event Processing - End

    void redispatchMouseEvent(MouseEvent e) {
        int count = getParent().getComponentCount();
        for(int i=0;i<count;i++){
            Component comp = getParent().getComponent(i);
            if(comp==this) continue;

            if(!comp.getBounds().contains(e.getPoint())) continue;
            Point componentPoint = SwingUtilities.convertPoint(this,
              e.getPoint(), comp);
            comp.dispatchEvent(new MouseEvent(comp, e.getID(), e
              .getWhen(), e.getModifiers(), componentPoint.x,
              componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
        }
      }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!getUI().isInFunctionArea(e.getPoint()))
            redispatchMouseEvent(e);
        else
            this.fireClick();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!getUI().isInFunctionArea(e.getPoint()))
            redispatchMouseEvent(e);
        else{
            model.setPressed(true);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!getUI().isInFunctionArea(e.getPoint()))
            redispatchMouseEvent(e);
        else{
            model.setPressed(false);
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
         model.setRollover(true);
         repaint();
         if(!getUI().isInFunctionArea(e.getPoint()))
            redispatchMouseEvent(e);

    }

    @Override
    public void mouseExited(MouseEvent e) {
         model.setRollover(false);
         repaint();
         if(!getUI().isInFunctionArea(e.getPoint()))
        redispatchMouseEvent(e);
      
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!getUI().isInFunctionArea(e.getPoint()))
          redispatchMouseEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
         if(!getUI().isInFunctionArea(e.getPoint()))
          redispatchMouseEvent(e);
    }


}
