package com.original.widget.plaf;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 修改记录
 *   1. 修正了老杨提出的静态对象应用的Bug，就是多个组件互相干涉现象 2012-05-27
 * @author ChangjianHu
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;

import com.original.widget.OScrollBar;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.ScrollBarModel;

public class OScrollBarUI extends MetalScrollBarUI
{
    //private static OScrollBarUI cui;
    private OScrollBar comp;
    private ScrollBarModel model;
    
    private Color _clrBackground = new Color(235, 235, 235);
    //private Color _clrTrack = new Color(202,202,202);
    //private Color _clrThumb = new Color(9,9,9);
    
    public OScrollBarUI(JComponent c ){
        comp = (OScrollBar)c;
        //this.model = model;
        //scrollBarWidth = 10;
    }

    // Create our own scrollbar UI!
    public static ComponentUI createUI( JComponent c ) {
        return new OScrollBarUI(c);
    }
    @Override
    protected JButton createDecreaseButton( int orientation )
    {
        return new OriScrollButton("");
    }
    @Override
    protected JButton createIncreaseButton( int orientation )
    {
        return new OriScrollButton("");
    }
    @Override
    public Dimension getPreferredSize( JComponent c ){
        model = comp.getModel();
        scrollBarWidth = model.getBarwidth()*2;
        if ( scrollbar.getOrientation() == JScrollBar.VERTICAL ){
            return new Dimension( scrollBarWidth, scrollBarWidth *3 );
        }
        else{  // Horizontal
	        return new Dimension( scrollBarWidth *3, scrollBarWidth );
        }
    }
    /*
    public void redraw() {
        if (model == null){
			model = comp.getModel();
		}
		model.setSize(comp.getHeight(), comp.getWidth());
        
    }*/
    @Override
    protected void paintTrack( Graphics g, JComponent c, Rectangle trackBounds){
        if (!c.isEnabled()) { return; }

        model = comp.getModel();
        _clrBackground = model.getTrackColor();
        if(!comp.isOpaque()) return;
        //_clrBackground = comp.getParent().getBackground();
        if (scrollbar.getOrientation()==JScrollBar.VERTICAL){
            g.setColor(_clrBackground);
            g.fillRect(0, 0, trackBounds.width,
                    trackBounds.height+scrollBarWidth*3);
        }
        else{
            g.setColor(_clrBackground);
            g.fillRect(0, 0, trackBounds.width+scrollBarWidth*3,
                    trackBounds.height);
        }
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
        if (!c.isEnabled()) { return; }
        //if(!scrollbar.isDisplayable()) return;
        model = comp.getModel();
        RoundRectangle2D r2d = null;
        g.translate( thumbBounds.x, thumbBounds.y );

        if(scrollbar.getOrientation()==JScrollBar.VERTICAL){
            r2d = new RoundRectangle2D.Double(
                thumbBounds.x+thumbBounds.width/2, model.getBarwidth()+2,
                model.getBarwidth(), thumbBounds.height - 1,
                model.getBarwidth(),model.getBarwidth());
        }
        else  { // HORIZONTAL
            r2d = new RoundRectangle2D.Double(
                thumbBounds.x,
                thumbBounds.y + thumbBounds.height/2-2,
                thumbBounds.width-1,
                model.getBarwidth(),
                model.getBarwidth(),model.getBarwidth());
            //OriGraphics.fillRoundRectangle(g, thumbBounds.x, thumbBounds.height/2-2, thumbBounds.width-1, 5,
            //6,6,this._clrThumb);
        }
        OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                model.getBarcolor());
        g.translate( -thumbBounds.x, -thumbBounds.y );
    }

    //这是一个内嵌类，没有特别的意义，所以不抽取出去作为独立组件使用
    private class OriScrollButton extends JButton {
        private static final long serialVersionUID = 1L;
        private Color circleColor = Color.WHITE;

        public OriScrollButton(String label) {
            super(label);
            //his.setEnabled(false);
            setRolloverEnabled(false);
            setBorder(null);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(circleColor);
            g.fillRect(-1, -1, getWidth()+2, getHeight()+2);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.width += size.height;
            //System.out.println(size.width);
            return size;
        }
    }
}

