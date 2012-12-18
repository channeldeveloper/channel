/*
 *  com.original.widget.ODialog.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 * (Class Annotation.)
 * 模式对话框，该模式对话框并不涵盖任何按钮，只是一个容器
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 22:56:28
 */
public class ODialog extends OWindow{
    private static final long serialVersionUID = -6497888280847098839L;
    private Dimension drawsize = null;
    private FixedGlassPane glass = null;


    //初始化对象
    public ODialog(JFrame owner, Dimension size, String wintitle, WINTYPE type) {
        super(owner, size, wintitle, type);
        initDialog(owner, size);
    }
    public ODialog(JFrame owner, Dimension size, String wintitle) {
        this(owner, size, wintitle, null);
    }

    private void initDialog(JFrame owner, Dimension size){
        //放置到屏幕中央
        drawsize = size;
        Rectangle rc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        int x = (rc.width - size.width)/2;
        int y = (rc.height-size.height)/2;
        this.setLocation(x, y);

        JRootPane rootpane = SwingUtilities.getRootPane(owner);
        glass = new FixedGlassPane( owner);
        glass.setLayout(new GridLayout(0, 1));
        glass.setOpaque(false);
        
        rootpane.setGlassPane(glass);
        glass.setNeedToRedispatch(false);
        glass.setVisible(true);
    }

    @Override
    protected void beforeDispose(){
        glass.setVisible(false);
    }

    /**
     * 关闭Dialog
     */
    public void closeDialog(){
        beforeDispose();
        dispose();
        setVisible(false);
    }

    class FixedGlassPane extends JPanel implements MouseListener,
            MouseMotionListener, FocusListener {
      // helpers for redispatch logic
      Toolkit toolkit;


      Container contentPane;

      boolean inDrag = false;

      // trigger for redispatching (allows external control)
      boolean needToRedispatch = false;

      @Override
           protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.1f));
              g.fillRect(0, 0, getWidth(), getHeight());
              //this.addMouseListener(null);
              //this.addMouseMotionListener(null);
           }
      public FixedGlassPane(Container cp) {
        toolkit = Toolkit.getDefaultToolkit();
        contentPane = cp;
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
      }

      public void setVisible(boolean v) {
        // Make sure we grab the focus so that key events don't go astray.
        if (v)
          requestFocus();
        super.setVisible(v);
      }

      // Once we have focus, keep it if we're visible
      public void focusLost(FocusEvent fe) {
        if (isVisible())
          requestFocus();
      }

      public void focusGained(FocusEvent fe) {
      }

      // We only need to redispatch if we're not visible, but having full control
      // over this might prove handy.
      public void setNeedToRedispatch(boolean need) {
        needToRedispatch = need;
      }

      /*
       * (Based on code from the Java Tutorial) We must forward at least the mouse
       * drags that started with mouse presses over the check box. Otherwise, when
       * the user presses the check box then drags off, the check box isn't
       * disarmed -- it keeps its dark gray background or whatever its L&F uses to
       * indicate that the button is currently being pressed.
       */
      public void mouseDragged(MouseEvent e) {
        if (needToRedispatch)
          redispatchMouseEvent(e);
      }

      public void mouseMoved(MouseEvent e) {
        if (needToRedispatch)
          redispatchMouseEvent(e);
      }

      public void mouseClicked(MouseEvent e) {
        if (needToRedispatch)
          redispatchMouseEvent(e);
      }

      public void mouseEntered(MouseEvent e) {
        if (needToRedispatch)
          redispatchMouseEvent(e);
      }

      public void mouseExited(MouseEvent e) {
        if (needToRedispatch)
          redispatchMouseEvent(e);
      }

      public void mousePressed(MouseEvent e) {
        if (needToRedispatch)
          redispatchMouseEvent(e);
      }

      public void mouseReleased(MouseEvent e) {
        if (needToRedispatch) {
          redispatchMouseEvent(e);
          inDrag = false;
        }
      }

      private void redispatchMouseEvent(MouseEvent e) {
        boolean inButton = false;
        boolean inMenuBar = false;
        Point glassPanePoint = e.getPoint();
        Component component = null;
        Container container = contentPane;
        Point containerPoint = SwingUtilities.convertPoint(this,
            glassPanePoint, contentPane);
        int eventID = e.getID();

        if (containerPoint.y < 0) {
          inMenuBar = true;
          testForDrag(eventID);
        }

        //XXX: If the event is from a component in a popped-up menu,
        //XXX: then the container should probably be the menu's
        //XXX: JPopupMenu, and containerPoint should be adjusted
        //XXX: accordingly.
        component = SwingUtilities.getDeepestComponentAt(container,
            containerPoint.x, containerPoint.y);

        if (component == null) {
          return;
        } else {
          inButton = true;
          testForDrag(eventID);
        }

        if (inMenuBar || inButton || inDrag) {
          Point componentPoint = SwingUtilities.convertPoint(this,
              glassPanePoint, component);
          component.dispatchEvent(new MouseEvent(component, eventID, e
              .getWhen(), e.getModifiers(), componentPoint.x,
              componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
        }
      }

      private void testForDrag(int eventID) {
        if (eventID == MouseEvent.MOUSE_PRESSED) {
          //inDrag = true;
        }
      }
    }
}
