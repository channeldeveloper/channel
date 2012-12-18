/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.plaf;

import com.original.widget.OPopupMenu;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.PopupMenuModel;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

/**
 *
 * @author Hu ChangJian
 */
public class OPopupMenuUI extends BasicPopupMenuUI {

    private static Robot robot = null;
    public static ComponentUI createUI(JComponent c) {
        return new OPopupMenuUI(c);
    }

    public OPopupMenuUI(JComponent com) {
    }

    @Override
    public void update(Graphics g, JComponent c){
        OPopupMenu comp = (OPopupMenu)c;
        PopupMenuModel model = comp.getModel();
        int width = comp.getWidth();
        int height = comp.getHeight();


        //fill the background to fake a transparent.
        try {
            robot = new Robot();
            Point pt = comp.getLocationOnScreen();
            Rectangle absRectangle = new Rectangle(pt.x, pt.y,
                    comp.getWidth(), comp.getHeight());
            BufferedImage popupBgImage  = robot.createScreenCapture(absRectangle);
            g.drawImage(popupBgImage, 0, 0, null);
        } catch (AWTException ex) {
            Logger.getLogger(OPopupMenuUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Graphics2D g2d = (Graphics2D)g;

        //g2d.setComposite(null)
        OriPainter.drawDropShadow(g, width, height, PopupMenuModel.CORNERRADIUS,
                model.getBackgroundColor(),
                model.getShadowColor(), model.getShadowSize(), model.getShadowDistance(),
                model.getShadowDirection(), model.getShadowOpacity());
        //comp.paintComponents(g);
        //super.update(g,c);
     }
}
