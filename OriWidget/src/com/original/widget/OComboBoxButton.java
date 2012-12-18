/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.CustomComboBoxModel;
import com.original.widget.plaf.OComboBoxButtonUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author Changjian Hu
 */
public class OComboBoxButton extends JComboBox {
     private static final String uiClassID = "OComboBoxButtonUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OComboBoxButtonUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
    private CustomComboBoxModel model;
    
    public OComboBoxButton(){
        super();
        initComp();
     }

    public OComboBoxButton(Object[] itemArray){
        super(itemArray);
        initComp();
     }
    private void initComp(){
        setVizModel(new CustomComboBoxModel(CustomComboBoxModel.COMBOBOXTYPE.BUTTON));
        this.setEditable(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4,4));
        this.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
    }
    //模型设置部分
    public void setVizModel(CustomComboBoxModel model) {
        this.model = model;
    }
     public CustomComboBoxModel getVizModel() {
        return model;
    }

    
    //设置UI
    public void setUI(OComboBoxButtonUI ui) {
            super.setUI(ui);
    }

    @Override
    public OComboBoxButtonUI getUI() {
            return (OComboBoxButtonUI) ui;
    }

    @Override
    public String getUIClassID() {
            return uiClassID;
    }


    
    public void fireActionEvent(ActionEvent e){
        Object  [] listeners = listenerList.getListenerList();
         for (int i = listeners.length - 2; i >= 0; i -= 2) {
             if (listeners[i]==ActionListener.class) {
                ((ActionListener  )listeners[i+1]).actionPerformed(e);
             }
        }
    }
}
