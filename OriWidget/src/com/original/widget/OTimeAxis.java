/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget;

import com.original.widget.event.BlockSelectionEvent;
import com.original.widget.event.BlockSelectionListener;
import com.original.widget.event.ChoiceSelectionEvent;
import com.original.widget.event.ChoiceSelectionListener;
import com.original.widget.event.TimeAxisChangeEvent;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.original.widget.event.TimeAxisChangeListener;
import com.original.widget.event.TimeAxisEvent;
import com.original.widget.event.TimeAxisEventListener;
import com.original.widget.model.MultiSelectorModel;
import com.original.widget.model.TimeAxisModel;
import com.original.widget.plaf.OTimeAxisUI;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Refactor the TimeAxis Component
 * We need to make the component as simple as possible.
 * @updator Changjian HU
 * @author Min NI, Xueyong SONG
 */
public class OTimeAxis extends JPanel implements MouseListener, 
        MouseMotionListener,ChoiceSelectionListener,BlockSelectionListener {

    private static final String uiClassID = "OTimeAxisUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OTimeAxisUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    private TimeAxisModel model;
    private TimeAxisChangeListener listener;
    private List<TimeAxisEventListener> listeners = new ArrayList<TimeAxisEventListener>();
    private OMultiSelector viewswitcher;
    //基本构造函数
    public OTimeAxis(){
        this(1000,95);
    }
    public OTimeAxis(int width, int height) {
        initAxis(width, height);
    }
    //初始化时间轴
    private void initAxis(int width, int height){
        setLayout(null);
        setPanelSize(width, height);
        setModel(new TimeAxisModel());
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setOpaque(false);
        listener = new TimeAxisChangeListener(this);
        viewswitcher = createViewSwitcher(width, height);
        
    }
    //create the viewSwitcher;
    private OMultiSelector createViewSwitcher(int width, int height){
        OMultiSelector ret = new OMultiSelector();
        MultiSelectorModel swmodel = ret.getModel();
        swmodel.addSimpleChoiceDataItem("月", "month");
        swmodel.addSimpleChoiceDataItem("周", "week");
        swmodel.addSimpleChoiceDataItem("天", "day");
        swmodel.setSelectedIndex(1);

        ret.addSelectionListener(this);
        add(ret);
        int offsetx = (int)(width * (1-model.getSlideAreaPercent())/2);
        int itmheight = 26;
        ret.setBounds(offsetx+4, height-itmheight-2, 106,itmheight);

        ret.setVisible(false);
        return ret;
    }
    //设置组件尺寸
    protected void setPanelSize(int width, int height) {
        super.setSize(width, height);
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        this.repaint();
    }

    @Override
    public OTimeAxisUI getUI() {
        return (OTimeAxisUI) ui;
    }
    public void setUI(OTimeAxisUI ui) {
        super.setUI(ui);
    }
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    //Setter/Getter 组件数据模型
    public void setModel(TimeAxisModel axisModel) {
        this.model = axisModel;
        this.model.addChangeListener(listener);
    }
    public TimeAxisModel getModel() {
		return model;
	}


    //事件处理机制
    @Override
    public void mouseClicked(MouseEvent e) {
        getUI().procMouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        getUI().procMousePressed(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        getUI().procMouseMoved(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getUI().procMouseReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        getUI().procMouseDragged(e);    
    }

    //Event Handler.
    public void fireChange(TimeAxisChangeEvent.CHANGETYPE type){
        fireEventInvoked(type);
    }

    //Event Processing.
    public synchronized void addTimeAxisEventListener(TimeAxisEventListener l) {
        listeners.add(l);
    }

    public synchronized void removeTimeAxisEventListener(TimeAxisEventListener l) {
        listeners.remove(l);
    }
    protected void fireEventInvoked(TimeAxisChangeEvent.CHANGETYPE type) {
        TimeAxisEvent e = new TimeAxisEvent(this, model, type);
        updateViewSwitcher();
        for(TimeAxisEventListener invoker: listeners){
            invoker.timeAxisChanged(e);
        }
    }
    private void updateViewSwitcher(){
        //System.out.println(model.getChoosetimewintype());
        this.viewswitcher.setSelectedIndex(model.getChoosetimewintype());
    }

    @Override
    public void choiceSelectChange(ChoiceSelectionEvent e) {
        MultiSelectorModel.SimpleChoiceDataItem info =
                (MultiSelectorModel.SimpleChoiceDataItem)e.getData();
        model.switchTimeView(info.getData().toString());
        repaint();
    }

    //这是一个临时处理，以后进行修订
    public void reset(){
        model.reset();
        repaint();
    }

    @Override
    public void blockSelectChange(BlockSelectionEvent e) {
        Date dt = e.getDt();
        //System.out.println(dt);
        model.midJumpTo(dt);
        getUI().hideInnerWin(this);
        repaint();
    }
}
