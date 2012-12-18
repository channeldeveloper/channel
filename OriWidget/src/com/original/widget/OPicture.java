/*
 *  com.original.widget.OPicture.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.ByteBlockModel;
import com.original.widget.plaf.OPictureUI;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.IOUtils;

/**
 * (Class Annotation.)
 * 头像输入组件，目前支持各种类型的图片
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 5, 2012 10:49:33 PM
 */
public class OPicture extends JPanel implements MouseListener,
        MouseMotionListener {

    private static final String uiClassID = "OPictureUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OPictureUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private ByteBlockModel model;
    private DrawAreaChangeListener listener;
    
    boolean _mouseOver = false;
    boolean _mouseOverBtn = false;
    
    /**
     * constructor.
     */
    public OPicture(){
       init();
    }
    /**
     * initialize some event handlers.
     */
    private void init(){
        setModel(new ByteBlockModel());
		setOpaque(false);
		this.setSize(204, 216);
        this.setMinimumSize(new Dimension(204, 216));
        setBorder(null);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.model.setBrwbtntext("选择头像");
    }

     //模型设置部分
    public void setModel(ByteBlockModel model) {
        this.model = model;
    }
     public ByteBlockModel getModel() {
        return model;
    }

    
    //设置UI
    public void setUI(OPictureUI ui) {
		super.setUI(ui);
	}

    @Override
    public OPictureUI getUI() {
		return (OPictureUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}
   
    
    
    /** mouse event processing **/
    public void mouseClicked(MouseEvent e) {
        Point pt = e.getPoint();
        Rectangle btnRect = getUI().getBtnRect();
        if(btnRect.contains(pt)){
            //here, you can make a dialog to select a image file.
            File fil = chooseImgFile();
            if(fil!=null){
                try {
                    int ind = fil.getAbsolutePath().lastIndexOf(".");
                    if(ind!=-1){
                        this.model.setDataformat(
                                fil.getAbsolutePath().substring(ind+1));
                        this.model.setDatablock(
                                IOUtils.toByteArray(new FileInputStream(fil)));
                        //_hasImg = true;
                        this.model.setHasdata(true);
                        this.model.setBrwbtntext("更改头像");
                        repaint();
                    }
                } catch (IOException ex) {

                }
                
            }

        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
        //_mouseOver = true;
        this.model.setMouseOver(true);
        Rectangle btnRect = getUI().getBtnRect();
        this.repaint();
    }


    public void mouseExited(MouseEvent e) {
        _mouseOver = false;
        _mouseOverBtn = false;
        this.model.setMouseOver(false);
        this.model.setMouseOverBtn(false);
        Rectangle btnRect = getUI().getBtnRect();
        this.repaint();

    }

    /** mouse move event processing **/
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        Point pt = e.getPoint();
        Rectangle btnRect = getUI().getBtnRect();
        if(btnRect.contains(pt))
            this.model.setMouseOverBtn(true);
            //_mouseOverBtn = true;
        else
            //_mouseOverBtn = false;
            this.model.setMouseOverBtn(false);
        this.repaint();
    }


    /**
     * very simple method to select an image file.
     * Please be advised that we will create our own file chooser in the
     * near future. Since in our system, people will be not encouraged to
     * access the physical file.
     *
     * Of course, we need to discuss how to handle this issue.
     * @return
     */

    private File chooseImgFile(){
        JFileChooser chooser = new JFileChooser(".");
        FileFilter imgType = new OriExtFileFilter("Image files",
          new String[] { ".jpg", ".gif", ".jpeg", ".png" });
        chooser.addChoosableFileFilter(imgType);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(imgType);
        int status = chooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            return f;
        }
        return null;
    }

    //inner Class
    class OriExtFileFilter extends FileFilter{
        private String extensions[];
        private String description;
        public OriExtFileFilter(String description, String extension) {
            this(description, new String[] { extension });
        }
        public OriExtFileFilter(String description, String extensions[]) {
            this.description = description;
            this.extensions = (String[]) extensions.clone();
        }
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            int count = extensions.length;
            String path = file.getAbsolutePath();
            for (int i = 0; i <count; i++) {
                String ext = extensions[i];
                if (path.endsWith(ext) &&
                        (path.charAt(path.length() - ext.length()) == '.')) {
                    return true;
                }
            }
            return false;
        }
        public String getDescription() {
            return (description == null ? extensions[0] : description);
        }
    }
}
