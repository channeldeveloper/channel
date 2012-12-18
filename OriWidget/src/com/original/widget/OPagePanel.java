/*
 *  com.original.widget.OPagePanel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.model.PagePanelModel;
import com.original.widget.plaf.OPagePanelUI;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 * 用于页面级的Panel，提供，过度色，边线（内外），上下左右阴影
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-26 22:17:19
 */
public class OPagePanel extends JPanel {
    private static final String uiClassID = "OPagePanelUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OPagePanelUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    private PagePanelModel  model;

    public OPagePanel(){
        initComp();
    }
    private void initComp(){
        setModel(new PagePanelModel());
    }
    public void setModel(PagePanelModel model) {
        this.model = model;
    }

    public PagePanelModel getModel() {
        return model;
    }

    public void setUI(OPagePanelUI ui) {
        super.setUI(ui);
    }

    @Override
    public OPagePanelUI getUI() {
        return (OPagePanelUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }
}
