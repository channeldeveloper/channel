/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.DefaultButtonModel;

/**
 *
 * @author Changjian Hu
 */
public class LevelButtonModel extends DefaultButtonModel  {
    public static enum BUTTONLEVEL {SYSTEM, APPLICATION, PAGE};
    private BUTTONLEVEL level = BUTTONLEVEL.SYSTEM;

    private static Color clr1 = new Color(120, 182, 238);
    private static Color clr2 = new Color(41, 147, 220);
    private static Color clr3 = new Color(230,230,230);
    private static Color clr4 = new Color(237,237,237);
    private static Font font1 = new Font("微软雅黑", Font.PLAIN, 14);
    private static Font font2 = new Font("verdana", Font.PLAIN, 14);
    private ButtonEffect effect = null;
    public BUTTONLEVEL getLevel() {
        return level;
    }

    public void setLevel(BUTTONLEVEL level) {
        this.level = level;
    }

    
    //圆角半径
    public int getRoundCorner(){
        if(level==BUTTONLEVEL.SYSTEM)
            return 12;
        else
            return 4;
    }

    //过渡色填充开始颜色
    public Color getGradientStartColor(){
        if(level == BUTTONLEVEL.SYSTEM)
            return clr1;
        else
            return Color.WHITE;
    }
    //过渡色填充结束颜色
    public Color getGradientEndColor(){
        if(level == BUTTONLEVEL.SYSTEM)
            return clr2;
        else if(level==BUTTONLEVEL.APPLICATION)
            return clr3;
        else
            return clr4;
    }
    //按钮文字字体
    public Font getButtonTextFont(boolean isEnglish){
        if(!isEnglish)
            return font1;
        else
            return font2;
    }
    //字体颜色
    public Color getButtonTextColor(){
        if(level==BUTTONLEVEL.SYSTEM)
            return Color.WHITE;
        else
            return Color.BLACK;
    }
    //按钮特效
    public ButtonEffect getButtonEffect(){
        if(effect!=null) return effect;
        ButtonEffect o = new ButtonEffect();
        return (effect=o.createButtonEffect(level));
    }

    //获取Button的对外的Margin偏移
    public Rectangle getMargin(){
        ButtonEffect t = getButtonEffect();
        if(t.hasShadow){
            Rectangle rect = new Rectangle();
            rect.x = t.size + (int)(t.dist * Math.cos(t.direction));
            rect.y = t.size + (int)(t.dist * Math.sin(t.direction));
            rect.width = t.size - (int)(t.dist * Math.cos(t.direction));
            rect.height = t.size - (int)(t.dist * Math.sin(t.direction));
            return rect;
        }else
            return null;

    }

    public class ButtonEffect{
        private boolean hasShadow = true;
        private boolean drawBorder = false;
        
        private int dist = 1;
        private int size = 1;
        private double direction = Math.PI/2;
        private Color shadowColor = Color.BLACK;
        private int opacity = 65;

        private Float borderWidth = 1.0f;
        private Color borderColor;

        public ButtonEffect(){
            
        }
        public ButtonEffect createButtonEffect(BUTTONLEVEL level){
            if(level==BUTTONLEVEL.SYSTEM)
                return new ButtonEffect(true, 1, 1, Math.PI*3/2,
                    Color.BLACK, 65, false, null, null);
            else if(level==BUTTONLEVEL.APPLICATION)
                return new ButtonEffect(true, 1, 4, Math.PI*3/2,
                    Color.BLACK, 50, true, 1.0f, Color.WHITE);
            else
                return new ButtonEffect(false, 0, 0, 0, Color.BLACK,
                        10, true, 1.0f, new Color(188,188,188));
        }

        public ButtonEffect(boolean hasShadow, int dist, int size, double direction,
                Color shadowColor, int opacity, boolean drawBorder, Float borderWidth,
                Color borderColor){
                this.hasShadow = hasShadow;
                this.dist = dist;
                this.size = size;
                this.direction = direction;
                this.borderColor = borderColor;
                this.shadowColor = shadowColor;
                this.opacity = opacity;
                this.drawBorder = drawBorder;
                this.borderWidth = borderWidth;        
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
        }

        public float getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(float borderWidth) {
            this.borderWidth = borderWidth;
        }

        public double getDirection() {
            return direction;
        }

        public void setDirection(double direction) {
            this.direction = direction;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        public boolean isDrawBorder() {
            return drawBorder;
        }

        public void setDrawBorder(boolean drawBorder) {
            this.drawBorder = drawBorder;
        }

        public boolean isHasShadow() {
            return hasShadow;
        }

        public void setHasShadow(boolean hasShadow) {
            this.hasShadow = hasShadow;
        }

        public int getOpacity() {
            return opacity;
        }

        public void setOpacity(int opacity) {
            this.opacity = opacity;
        }

        public Color getShadowColor() {
            return shadowColor;
        }

        public void setShadowColor(Color shadowColor) {
            this.shadowColor = shadowColor;
        }

        public int getSize() {
            if(this.hasShadow)
                return size;
            else
                return 0;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
