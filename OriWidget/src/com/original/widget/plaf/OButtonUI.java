/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.plaf;

import com.original.widget.OButton;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.LevelButtonModel;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;
import com.original.widget.model.LevelButtonModel.ButtonEffect;
import java.awt.Color;
import sun.swing.SwingUtilities2;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/**
 *
 * @author A
 */
public class OButtonUI extends BasicButtonUI{
    private int CORNERRADIUS = 10;
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private Color backgroundcolor = new Color(249,249,249);
    public static Color BORDERCOLOR = new Color(152,152,152);

    public static ComponentUI createUI(JComponent c) {
        return new OButtonUI(c);
    }

    public OButtonUI(JComponent com) {

    }

    //here, I will override all things
    //paint, paintText, paintIcon, paintFocus,paintButtonPressed
    @Override
    public void paint(Graphics g, JComponent c)
    {
        OButton b = (OButton) c;
        LevelButtonModel model = b.getModel();
        
        String text = layout(b, SwingUtilities2.getFontMetrics(b, g),
               b.getWidth(), b.getHeight());

        clearTextShiftOffset();
        //here, we can draw the basic button.
        paintBackground(g, b);
        //Draw the background frame.
        if(model.isRollover()){
            paintButtonRollover(g, b);
        }
        else{
            paintButtonNormal(g, b);
        }
        // perform UI specific press action, e.g. Windows L&F shifts text
        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(g,b);
        }
        
        // Paint the Icon
        if(b.getIcon() != null) {
            paintIcon(g,c,iconRect);
        }

        if (text != null && !text.equals("")){
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }

        if (b.isFocusPainted() && b.hasFocus()) {
            // paint UI specific focus
            paintFocus(g,b,viewRect,textRect,iconRect);
        }
    }
    

    protected void paintBackground(Graphics g, AbstractButton b){
        
    }
    protected void paintButtonRollover(Graphics g, AbstractButton b){
        drawButton(g, b, 1);
    }
    protected void paintButtonNormal(Graphics g, AbstractButton b){
       drawButton(g, b, 0);
    }
    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b){
        drawButton(g, b, 2);
    }

    private void drawButton(Graphics g, AbstractButton b, int type){
         OButton o = (OButton) b;
        LevelButtonModel model = o.getModel();
        ButtonEffect effect = model.getButtonEffect();
        int shadowsize = 1;
        if(effect.isHasShadow()){
            shadowsize = effect.getSize();
        }
        int width = b.getWidth()-shadowsize*2;
        int height = b.getHeight()-shadowsize*2;

        int corner = height/2;
        if(model.getLevel()!=BUTTONLEVEL.SYSTEM)
            corner = model.getRoundCorner()*2;
         RoundRectangle2D r2d= new RoundRectangle2D.Double(
                shadowsize,
                shadowsize,
                width,
                height,
                corner*2,
                corner*2);
        Area fillArea = new Area(r2d);
        if(effect.isHasShadow()){
        OriPainter.drawDropShadow(g, fillArea, effect.getShadowColor(),
                effect.getSize(), effect.getDist(),effect.getDirection(), 1.0f*effect.getOpacity()/100);
            GeomOperator.shrinkCopy(fillArea, shadowsize/2);
        }
        if(type==0)
            OriPainter.gradientFillArea(g, fillArea,
                model.getGradientStartColor(), model.getGradientEndColor(), true);
        else if(type==1)
            OriPainter.gradientFillArea(g, fillArea,
                model.getGradientEndColor(), model.getGradientStartColor(), true);
        else
            OriPainter.gradientFillArea(g, fillArea,
                model.getGradientEndColor(), model.getGradientEndColor(), true);
        if(effect.isDrawBorder()){
            if(model.getLevel()==BUTTONLEVEL.APPLICATION){
                Area borderArea = GeomOperator.centerFixShrinkCopy(fillArea, 1.0);
                OriPainter.drawAreaBorderWithSingleColor(g, borderArea,
                        effect.getBorderColor(), effect.getBorderWidth());
            }else{
                OriPainter.drawAreaBorderWithSingleColor(g, fillArea,
                        effect.getBorderColor(), effect.getBorderWidth());
                Area borderArea = GeomOperator.centerFixShrinkCopy(fillArea, 1.0);
                OriPainter.drawAreaBorderWithSingleColor(g, borderArea,
                        Color.WHITE, effect.getBorderWidth());
            }
        }
    }

     private String layout(AbstractButton b, FontMetrics fm,
                          int width, int height) {
        Insets i = b.getInsets();
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = width - (i.right + viewRect.x);
        viewRect.height = height - (i.bottom + viewRect.y);

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        // layout the text and icon
        return SwingUtilities.layoutCompoundLabel(
            b, fm, b.getText(), b.getIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect,
            b.getText() == null ? 0 : b.getIconTextGap());
    }
}
