/*
 *  可移动Label的JPanel.DragListener.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.event;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-4-24 19:59:43
 */
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

public class DragListener implements DragGestureListener, DragSourceListener,
        DropTargetListener, Transferable {

    static final DataFlavor[] supportedFlavors = {null};

    static {
        try {
            supportedFlavors[0] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    Object object;

// Transferable methods.
    @Override
    public Object getTransferData(DataFlavor flavor) {

        if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
            return object;
        } else {
            return null;
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {

        return flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType);

    }
// DragGestureListener method.

    @Override
    public void dragGestureRecognized(DragGestureEvent ev) {
        ev.startDrag(null, this, this);
    }

// DragSourceListener methods.
    public void dragDropEnd(DragSourceDropEvent ev) {
    }

    public void dragEnter(DragSourceDragEvent ev) {
    }

    public void dragExit(DragSourceEvent ev) {
    }

    public void dragOver(DragSourceDragEvent ev) {
        object = ev.getSource();
    }

    public void dropActionChanged(DragSourceDragEvent ev) {
    }

// DropTargetListener methods.
    public void dragEnter(DropTargetDragEvent ev) {
    }

    public void dragExit(DropTargetEvent ev) {
    }

    public void dragOver(DropTargetDragEvent ev) {
        dropTargetDrag(ev);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent ev) {
        dropTargetDrag(ev);
    }

    void dropTargetDrag(DropTargetDragEvent ev) {
        ev.acceptDrag(ev.getDropAction());
    }

    @Override
    public void drop(DropTargetDropEvent ev) {
        ev.acceptDrop(ev.getDropAction());
        try {
            Object target = ev.getSource();
            Object source = ev.getTransferable().getTransferData(supportedFlavors[0]);
            if (source == null) {
                return;
            }
            Component component = ((DragSourceContext) source).getComponent();
            Container oldContainer = component.getParent();
            Container container = (Container) ((DropTarget) target).getComponent();
            container.add(component);

            oldContainer.validate();
            oldContainer.repaint();
            container.validate();
            container.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ev.dropComplete(true);

    }
}


