/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget;

import com.original.widget.event.ChoiceSelectionEvent;
import com.original.widget.event.ChoiceSelectionListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
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
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author A
 */
public class OScrollablePopupMenu extends OPopupMenu  {

    private static class EmptyListModelClass implements ListModel,
            Serializable {

        public int getSize() {
            return 0;
        }

        public Object getElementAt(int index) {
            return null;
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    };
    static final ListModel EmptyListModel = new EmptyListModelClass();
    private static Border LIST_BORDER = new LineBorder(Color.BLACK, 1);
  
    private DefaultListModel _model;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor methods instead.
     *
     * @see #getList
     * @see #createList
     */
    protected JList list;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createScroller
     */
    protected JScrollPane scroller;
    /**
     * As of Java 2 platform v1.4 this previously undocumented field is no
     * longer used.
     */
    protected boolean valueIsAdjusting = false;
    // Listeners that are required by the ComboPopup interface
    /**
     * Implementation of all the listener classes.
     */
    private Handler handler;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getMouseMotionListener
     * @see #createMouseMotionListener
     */
    protected MouseMotionListener mouseMotionListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getMouseListener
     * @see #createMouseListener
     */
    protected MouseListener mouseListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getKeyListener
     * @see #createKeyListener
     */
    protected KeyListener keyListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead.
     *
     * @see #createListSelectionListener
     */
    protected ListSelectionListener listSelectionListener;
    // Listeners that are attached to the list
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead.
     *
     * @see #createListMouseListener
     */
    protected MouseListener listMouseListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createListMouseMotionListener
     */
    protected MouseMotionListener listMouseMotionListener;
    // Added to the combo box for bound properties
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createPropertyChangeListener
     */
    protected PropertyChangeListener propertyChangeListener;
    // Added to the combo box model
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createListDataListener
     */
    protected ListDataListener listDataListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createItemListener
     */
    protected ItemListener itemListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override.
     */
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


    public void addMenuItem(Object item){
        _model.addElement(item);        
    }
    public void addMenuItem(Component item){
        _model.addElement(item);
    }

    /**
     * Implementation of ComboPopup.getList().
     */
    public JList getList() {
        return list;
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
        uninstallListListeners();
        list.setModel(EmptyListModel);
    }

    

    protected void uninstallKeyboardActions() {
        // XXX - shouldn't call this method
//        comboBox.unregisterKeyboardAction( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) );
    }

    //===================================================================
    // begin Initialization routines
    //
    public OScrollablePopupMenu() {
       init();        
    }

    private void init(){
        list = createList();
        configureList();
        scroller = createScroller();
        configureScroller();
        configurePopup();
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

    protected JList createList() {
        _model=new DefaultListModel();
        return new JList(_model) {

            public void processMouseEvent(MouseEvent e) {
                if (e.isControlDown()) {
                    // Fix for 4234053. Filter out the Control Key from the list.
                    // ie., don't allow CTRL key deselection.
                    e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                            e.getModifiers() ^ InputEvent.CTRL_MASK,
                            e.getX(), e.getY(),
                            e.getXOnScreen(), e.getYOnScreen(),
                            e.getClickCount(),
                            e.isPopupTrigger(),
                            MouseEvent.NOBUTTON);
                }
                super.processMouseEvent(e);
            }
        };
    }

    protected void configureList() {
        //list.setForeground(this.getBackground() );
        //list.setBackground(new Color(249,249,249));
        list.setOpaque(false);
        list.setFixedCellHeight(30); //
        list.setBorder(null);
        list.setCellRenderer(new ODefaultListCellRender());
        list.setFocusable(false);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _model.clear();
        installListListeners();
    }

    protected void installListListeners() {
        if ((listMouseListener = createListMouseListener()) != null) {
            list.addMouseListener(listMouseListener);
        }
        if ((listMouseMotionListener = createListMouseMotionListener()) != null) {
            list.addMouseMotionListener(listMouseMotionListener);
        }
        if ((listSelectionListener = createListSelectionListener()) != null) {
            list.addListSelectionListener(listSelectionListener);
        }
    }

    void uninstallListListeners() {
        if (listMouseListener != null) {
            list.removeMouseListener(listMouseListener);
            listMouseListener = null;
        }
        if (listMouseMotionListener != null) {
            list.removeMouseMotionListener(listMouseMotionListener);
            listMouseMotionListener = null;
        }
        if (listSelectionListener != null) {
            list.removeListSelectionListener(listSelectionListener);
            listSelectionListener = null;
        }
        handler = null;
    }

    /**
     * Creates the scroll pane which houses the scrollable list.
     */
    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBar(null);
        return sp;
    }

    /**
     * Configures the scrollable portion which holds the list within
     * the combo box popup. This method is called when the UI class
     * is created.
     */
    protected void configureScroller() {
        scroller.setFocusable(false);
        scroller.getVerticalScrollBar().setFocusable(false);
        scroller.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL,
                new Color(249, 249, 249)));
        scroller.getViewport().setOpaque(false);
        scroller.setBackground(Color.red);
        scroller.setBorder(null);
        scroller.setOpaque(false);
    }

    /**
     * Configures the popup portion of the combo box. This method is called
     * when the UI class is created.
     */
    protected void configurePopup() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        add(scroller);

        setDoubleBuffered(true);
        setFocusable(false);

        
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
            MouseMotionListener,  Serializable {
        //
        // MouseListener
        // NOTE: this is added to both the JList and JComboBox
        //

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (e.getSource() == list) {
                return;
            }

            togglePopup();
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == list) {
                if (list.getModel().getSize() > 0) {
                    fireSelectionChanged(list.getSelectedIndex(),
                            _model.get(list.getSelectedIndex()));
                    
                    hide();
                }
                return;
            }
           
            hasEntered = false;
            stopAutoScrolling();
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
            if (anEvent.getSource() == list) {
                Point location = anEvent.getPoint();
                Rectangle r = new Rectangle();
                list.computeVisibleRect(r);
                if (r.contains(location)) {
                    updateListBoxSelectionForEvent(anEvent, false);
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (e.getSource() == list) {
                return;
            }
            if (isVisible()) {
                MouseEvent newEvent = convertMouseEvent(e);
                Rectangle r = new Rectangle();
                list.computeVisibleRect(r);

                if (newEvent.getPoint().y >= r.y && newEvent.getPoint().y <= r.y + r.height - 1) {
                    hasEntered = true;
                    if (isAutoScrolling) {
                        stopAutoScrolling();
                    }
                    Point location = newEvent.getPoint();
                    if (r.contains(location)) {
                        updateListBoxSelectionForEvent(newEvent, false);
                    }
                } else {
                    if (hasEntered) {
                        int directionToScroll = newEvent.getPoint().y < r.y ? SCROLL_UP : SCROLL_DOWN;
                        if (isAutoScrolling && scrollDirection != directionToScroll) {
                            stopAutoScrolling();
                            startAutoScrolling(directionToScroll);
                        } else if (!isAutoScrolling) {
                            startAutoScrolling(directionToScroll);
                        }
                    } else {
                        if (e.getPoint().y < 0) {
                            hasEntered = true;
                            startAutoScrolling(SCROLL_UP);
                        }
                    }
                }
            }
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
        // XXX - should be a private method within InvocationMouseMotionHandler
        // if possible.
        if (isAutoScrolling) {
            autoscrollTimer.stop();
        }

        isAutoScrolling = true;

        if (direction == SCROLL_UP) {
            scrollDirection = SCROLL_UP;
            Point convertedPoint = SwingUtilities.convertPoint(scroller, new Point(1, 1), list);
            int top = list.locationToIndex(convertedPoint);
            list.setSelectedIndex(top);

            autoscrollTimer = new Timer(100, new AutoScrollActionHandler(
                    SCROLL_UP));
        } else if (direction == SCROLL_DOWN) {
            scrollDirection = SCROLL_DOWN;
            Dimension size = scroller.getSize();
            Point convertedPoint = SwingUtilities.convertPoint(scroller,
                    new Point(1, (size.height - 1) - 2),
                    list);
            int bottom = list.locationToIndex(convertedPoint);
            list.setSelectedIndex(bottom);

            autoscrollTimer = new Timer(100, new AutoScrollActionHandler(
                    SCROLL_DOWN));
        }
        autoscrollTimer.start();
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void stopAutoScrolling() {
        isAutoScrolling = false;

        if (autoscrollTimer != null) {
            autoscrollTimer.stop();
            autoscrollTimer = null;
        }
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void autoScrollUp() {
        int index = list.getSelectedIndex();
        if (index > 0) {
            list.setSelectedIndex(index - 1);
            list.ensureIndexIsVisible(index - 1);
        }
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void autoScrollDown() {
        int index = list.getSelectedIndex();
        int lastItem = list.getModel().getSize() - 1;
        if (index < lastItem) {
            list.setSelectedIndex(index + 1);
            list.ensureIndexIsVisible(index + 1);
        }
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
    
    /**
     * Sets the list selection index to the selectedIndex. This
     * method is used to synchronize the list selection with the
     * combo box selection.
     *
     * @param selectedIndex the index to set the list
     */
    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
        }
    }

    protected MouseEvent convertMouseEvent(MouseEvent e) {
        Point convertedPoint = SwingUtilities.convertPoint((Component) e.getSource(),
                e.getPoint(), list);
        MouseEvent newEvent = new MouseEvent((Component) e.getSource(),
                e.getID(),
                e.getWhen(),
                e.getModifiers(),
                convertedPoint.x,
                convertedPoint.y,
                e.getXOnScreen(),
                e.getYOnScreen(),
                e.getClickCount(),
                e.isPopupTrigger(),
                MouseEvent.NOBUTTON);
        return newEvent;
    }

    /**
     * Retrieves the height of the popup based on the current
     * ListCellRenderer and the maximum row count.
     */
    protected int getPopupHeightForRowCount(int maxRowCount) {
        // Set the cached value of the minimum row count
        int minRowCount = Math.min(maxRowCount, _model.getSize());
        int height = 0;
        ListCellRenderer renderer = list.getCellRenderer();
        Object value = null;
        int rowheight = list.getFixedCellHeight();
        for (int i = 0; i < minRowCount; ++i) {
            value = list.getModel().getElementAt(i);
            Component c = renderer.getListCellRendererComponent(list, value, i, false, false);
            if (c.getPreferredSize().height < rowheight) {
                height += rowheight;
            } else {
                height += c.getPreferredSize().height;
            }
        }

        if (height == 0) {
            height = 20;
        }

        Border border = scroller.getViewportBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            height += insets.top + insets.bottom;
        }

        border = scroller.getBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            height += insets.top + insets.bottom;
        }

        return height;
    }

   

    /**
     * A utility method used by the event listeners.  Given a mouse event, it changes
     * the list selection to the list item below the mouse.
     */
    protected void updateListBoxSelectionForEvent(MouseEvent anEvent, boolean shouldScroll) {
        // XXX - only seems to be called from this class. shouldScroll flag is
        // never true
        Point location = anEvent.getPoint();
        if (list == null) {
            return;
        }
        int index = list.locationToIndex(location);
        if (index == -1) {
            if (location.y < 0) {
                index = 0;
            } else {
                index = _model.getSize() - 1;
            }
        }
        if (list.getSelectedIndex() != index) {
            list.setSelectedIndex(index);
            if (shouldScroll) {
                list.ensureIndexIsVisible(index);
            }
        }
    }

    class ODefaultListCellRender extends DefaultListCellRenderer {
        Color bg = new Color(46, 156, 202);;
        Color fg = Color.WHITE;
        Color bg2 = new Color(218,218,218);
        public ODefaultListCellRender(){
        }
        @Override
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            setComponentOrientation(list.getComponentOrientation());


            if (isSelected) {
                setBackground(bg2);
                setForeground(Color.BLACK);
                
            }
            else {
                setBackground(new Color(249,249,249));
                setForeground(list.getForeground());
            }

            if (value instanceof Icon) {
                setIcon((Icon)value);
                setText("");
            }
            else {
                setIcon(null);
                setText((value == null) ? "" : value.toString());
            }

            setEnabled(list.isEnabled());
            setFont(list.getFont());
            return this;
        }
    }

    
    List<ChoiceSelectionListener> selectListenerList = new ArrayList<ChoiceSelectionListener>();

     public void addSelectionListener(ChoiceSelectionListener l) {
        selectListenerList.add(l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(ChoiceSelectionListener l) {
        selectListenerList.remove( l);
    }

     public void fireSelectionChanged(int selectIndex, Object data) {

        Iterator<ChoiceSelectionListener> iter = selectListenerList.iterator();
        while(iter.hasNext()){
            iter.next().choiceSelectChange(
                    new ChoiceSelectionEvent(this, selectIndex,data)
                    );
        }
    }
    //
    // end Utility methods
    //=================================================================
}
