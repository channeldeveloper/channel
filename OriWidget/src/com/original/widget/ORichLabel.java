/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.RichLabelModel;
import com.original.widget.plaf.ORichLabelUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.BadLocationException;
import javax.swing.text.View;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 14:01:07
 */
public class ORichLabel extends JLabel implements CustomDrawable {

    private static final String uiClassID = "ORichLabelUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.ORichLabelUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private RichLabelModel model;
    private DrawAreaChangeListener listener;

    public ORichLabel() {
        this(60, 20, "");
    }

    public ORichLabel(String text) {
        this(60, 20, text);
    }

    //初始化对象
    public ORichLabel(int width, int height, String text) {
        super(text);
        //model
        this.setPreferredSize(new Dimension(width, height));
        setModel(new RichLabelModel(width, height));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        //mode property listner
        listener = new DrawAreaChangeListener(this);
        this.model.addChangeListener(listener);
        this.setVerticalAlignment(SwingConstants.TOP);

        initComp();
        this.setText(text);
    }

    private void initComp() {
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
    }

    //模型设置部分
    public void setModel(RichLabelModel model) {
        this.model = model;
    }

    public RichLabelModel getModel() {
        return model;
    }

//    public void resize() {
//        int width = this.getWidth();
//        this.model.setWidth(width);
//        int height = this.getHeight();
//        this.model.setHeight(height);
//    }

//    @Override
//    public void setBounds(int x, int y, int width, int height) {
//        super.setBounds(x, y, width, height);
//        resize();
//    }
    //设置UI

    public void setUI(ORichLabelUI ui) {
        super.setUI(ui);
    }

    @Override
    public ORichLabelUI getUI() {
        return (ORichLabelUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    @Override
    public void setText(String _text) {
        super.setText(_text);
        if (model != null) {
            model.setValue(_text);
            autoSize();
        }
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (model != null) {
            model.setFont(font);
            autoSize();
        }
    }

    private void autoSize() {
        if (model.isAutoWidthSize()) {
            try {
                View view = BasicHTML.createHTMLView(this, model.getValue());
                int w1 = view.getDocument().getLength();
                String text = view.getDocument().getText(1, w1 - 1);
                FontMetrics fm = this.getFontMetrics(model.getFont());
                int w = fm.stringWidth(text);
                this.setPreferredSize(new Dimension(w + 16, model.getHeight()));
//                resize();
            } catch (BadLocationException ex) {
            }
        } else if (model.isAutoHeightSize()) {
            String text = model.getValue();
            if ( !BasicHTML.isHTMLString(text)){
                this.setText("<HTML>"+text+"</HTML>");
                return;
            }
            View view = BasicHTML.createHTMLView(this, text);
            view.setSize(model.getWidth() - 16 , Integer.MAX_VALUE);
            int h = (int) view.getMinimumSpan(View.Y_AXIS);
            this.setPreferredSize(new Dimension(model.getWidth(), h + 16));
//            resize();
        }
    }

    public void setAutoWidthSize(boolean _autosize) {
        model.setAutoWidthSize(_autosize);
        autoSize();
    }

    public void setAutoHeightSize(boolean _autosize) {
        model.setAutoHeightSize(_autosize);
        autoSize();
    }

    //强制绘制-当变化的时候
    @Override
    public void forceCustomDraw() {
        getUI().redraw();
        repaint();
    }
}
