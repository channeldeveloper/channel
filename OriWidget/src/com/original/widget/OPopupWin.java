/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.Timer;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author A
 */
public class OPopupWin extends OPopupMenu  {

    protected JScrollPane container;
    private Handler handler;
    protected MouseMotionListener mouseMotionListener;
    protected MouseListener mouseListener;
    protected KeyListener keyListener;
    protected MouseListener listMouseListener;
    protected MouseMotionListener listMouseMotionListener;
    protected PropertyChangeListener propertyChangeListener;
    
    protected Timer autoscrollTimer;
    protected boolean hasEntered = false;
    protected boolean isAutoScrolling = false;
    protected int scrollDirection = SCROLL_UP;
    protected static final int SCROLL_UP = 0;
    protected static final int SCROLL_DOWN = 1;

   
    /**
     * Implementation of ComboPopup.hide().
     */
    public void hide() {
        MenuSelectionManager manager = MenuSelectionManager.defaultManager();
        MenuElement[] selection = manager.getSelectedPath();
        for (int i = 0; i < selection.length; i++) {
            if (selection[i] == this) {
                manager.clearSelectedPath();
                break;
            }
        }
        
    }

    /**
     * Implementation of ComboPopup.getMouseListener().
     *
     * @return a <code>MouseListener</code> or null
     * @see ComboPopup#getMouseListener
     */
    public MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = createMouseListener();
        }
        return mouseListener;
    }

    /**
     * Implementation of ComboPopup.getMouseMotionListener().
     *
     * @return a <code>MouseMotionListener</code> or null
     * @see ComboPopup#getMouseMotionListener
     */
    public MouseMotionListener getMouseMotionListener() {
        if (mouseMotionListener == null) {
            mouseMotionListener = createMouseMotionListener();
        }
        return mouseMotionListener;
    }

    /**
     * Implementation of ComboPopup.getKeyListener().
     *
     * @return a <code>KeyListener</code> or null
     * @see ComboPopup#getKeyListener
     */
    public KeyListener getKeyListener() {
        if (keyListener == null) {
            keyListener = createKeyListener();
        }
        return keyListener;
    }

    /**
     * Called when the UI is uninstalling.  Since this popup isn't in the component
     * tree, it won't get it's uninstallUI() called.  It removes the listeners that
     * were added in addComboBoxListeners().
     */
    public void uninstallingUI() {
        uninstallKeyboardActions();
    }

    

    protected void uninstallKeyboardActions() {
        // XXX - shouldn't call this method
//        comboBox.unregisterKeyboardAction( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) );
    }

    //===================================================================
    // begin Initialization routines
    //
    public OPopupWin() {
        super();
        init();
    }

    private void init(){
        container = createContainer();
        configureContainer();
        configurePopup();
    }

    public Container getContentPane(){
        return container;
    }
    protected MouseListener createMouseListener() {
        return getHandler();
    }

    protected MouseMotionListener createMouseMotionListener() {
        return getHandler();
    }

    protected KeyListener createKeyListener() {
        return null;
    }

    protected ListSelectionListener createListSelectionListener() {
        return null;
    }

   
    protected ListDataListener createListDataListener() {
        return null;
    }

    protected MouseListener createListMouseListener() {
        return getHandler();
    }

    
    protected MouseMotionListener createListMouseMotionListener() {
        return getHandler();
    }

    

    protected ItemListener createItemListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    
    /**
     * Creates the scroll pane which houses the scrollable list.
     */
    protected JScrollPane createContainer() {
        return new JScrollPane();
    }
    

    /**
     * Configures the scrollable portion which holds the list within
     * the combo box popup. This method is called when the UI class
     * is created.
     */
    protected void configureContainer() {
        container.setBorder(null);
        container.setLayout(null);
        //container.getViewport().setOpaque(false);
        container.setOpaque(false);
        container.setFocusable(false);

    }

    @Override
    public void addMouseWheelListener(MouseWheelListener listener){
        container.addMouseWheelListener(listener);
    }

    
    /**
     * Configures the popup portion of the combo box. This method is called
     * when the UI class is created.
     */
    protected void configurePopup() {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(container, BorderLayout.CENTER);
        setDoubleBuffered(true);
        setFocusable(true);
    }

    
    protected class InvocationMouseHandler extends MouseAdapter {

        /**
         * Responds to mouse-pressed events on the combo box.
         *
         * @param e the mouse-press event to be handled
         */
        public void mousePressed(MouseEvent e) {
            getHandler().mousePressed(e);
        }

        /**
         * Responds to the user terminating
         * a click or drag that began on the combo box.
         *
         * @param e the mouse-release event to be handled
         */
        public void mouseReleased(MouseEvent e) {
            getHandler().mouseReleased(e);
        }
    }

    /**
     * This listener watches for dragging and updates the current selection in the
     * list if it is dragging over the list.
     */
    protected class InvocationMouseMotionHandler extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            getHandler().mouseDragged(e);
        }
    }

    /**
     * As of Java 2 platform v 1.4, this class is now obsolete and is only included for
     * backwards API compatibility. Do not instantiate or subclass.
     * <p>
     * All the functionality of this class has been included in
     * BasicComboBoxUI ActionMap/InputMap methods.
     */
    public class InvocationKeyHandler extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
        }
    }

    /**
     * As of Java 2 platform v 1.4, this class is now obsolete, doesn't do anything, and
     * is only included for backwards API compatibility. Do not call or
     * override.
     */
    protected class ListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
        }
    }

    /**
     * As of 1.4, this class is now obsolete, doesn't do anything, and
     * is only included for backwards API compatibility. Do not call or
     * override.
     * <p>
     * The functionality has been migrated into <code>ItemHandler</code>.
     *
     * @see #createItemListener
     */
    public class ListDataHandler implements ListDataListener {

        public void contentsChanged(ListDataEvent e) {
        }

        public void intervalAdded(ListDataEvent e) {
        }

        public void intervalRemoved(ListDataEvent e) {
        }
    }

    /**
     * This listener hides the popup when the mouse is released in the list.
     */
    protected class ListMouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent anEvent) {
            getHandler().mouseReleased(anEvent);
        }
    }

    /**
     * This listener changes the selected item as you move the mouse over the list.
     * The selection change is not committed to the model, this is for user feedback only.
     */
    protected class ListMouseMotionHandler extends MouseMotionAdapter {

        public void mouseMoved(MouseEvent anEvent) {
            getHandler().mouseMoved(anEvent);
        }
    }

    /**
     * This listener watches for changes to the selection in the
     * combo box.
     */
    protected class ItemHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            getHandler().itemStateChanged(e);
        }
    }

   

    private class AutoScrollActionHandler implements ActionListener {

        private int direction;

        AutoScrollActionHandler(int direction) {
            this.direction = direction;
        }

        public void actionPerformed(ActionEvent e) {
            if (direction == SCROLL_UP) {
                autoScrollUp();
            } else {
                autoScrollDown();
            }
        }
    }

    private class Handler implements ItemListener, MouseListener,
            MouseMotionListener,  Serializable,MouseWheelListener {
        //
        // MouseListener
        // NOTE: this is added to both the JList and JComboBox
        //

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
       
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        //
        // MouseMotionListener:
        // NOTE: this is added to both the List and ComboBox
        //
        public void mouseMoved(MouseEvent anEvent) {
          
        }

        public void mouseDragged(MouseEvent e) {

        }

       

        //
        // ItemListener
        //
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println(e);
                //JComboBox comboBox = (JComboBox) e.getSource();
                //setListSelection(comboBox.getSelectedIndex());
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            
        }
    }

    //
    // end Event Listeners
    //=================================================================
    /**
     * Overridden to unconditionally return false.
     */
    public boolean isFocusTraversable() {
        return true;
    }

    //===================================================================
    // begin Autoscroll methods
    //
    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void startAutoScrolling(int direction) {
       
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void stopAutoScrolling() {
       
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void autoScrollUp() {
       
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void autoScrollDown() {
       
    }

    

    /**
     * Makes the popup visible if it is hidden and makes it hidden if it is
     * visible.
     */
    protected void togglePopup() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    class OFocusPanel extends JPanel  {

        @Override
        public boolean isFocusTraversable(){
            return true;
        }
    }
}
