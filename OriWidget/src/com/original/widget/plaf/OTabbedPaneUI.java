/*
 *  测试.OTabbedPaneUI.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.OTabbedPane;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.draw.OriShadow;
import com.original.widget.model.OTabbedModel;
import sun.swing.SwingUtilities2;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/**
 * (Class Annotation.)
 * 修改记录：
 * 1. 实现真色彩渲染（按照胡涛设计） - 胡长建 2012-06-12
 *
 * @author   Yangkj
 *
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-4-20 16:06:50
 */
public class OTabbedPaneUI extends BasicTabbedPaneUI {

    private OTabbedPane comp;
    private OTabbedModel model = null;

    public static ComponentUI createUI(JComponent c) {
        return new OTabbedPaneUI((OTabbedPane) c);
    }

    public OTabbedPaneUI(OTabbedPane c) {
        comp = c;
    }

    private OTabbedModel getModel() {
        if (model == null) {
            model = (OTabbedModel) comp.getModel();
        }
        return model;
    }

    @Override
    protected Insets getContentBorderInsets(int tabPlacement) {
        return getModel().getContentInsets();
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement,
            Rectangle[] rects, int tabIndex, Rectangle iconRect,
            Rectangle textRect, boolean isSelected) {
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        int width = tabPane.getWidth();
        int height = tabPane.getHeight();
        Insets insets = tabPane.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;
        switch (tabPlacement) {
            case LEFT:
                x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                w -= (x - insets.left);
                break;
            case RIGHT:
                w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                break;
            case BOTTOM:
                h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                break;
            case TOP:
            default:
                y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                h -= (y - insets.top);
        }

        if (tabPane.getTabCount() > 0) {
            // Fill region behind content area
            g.setColor(((OTabbedModel) comp.getModel()).getDividerColor());
            g.fillRect(x, y, w, h);
        }

        //由于我们自己绘制，这块进行了去除 -hucj 06-12/2012
        //paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        //paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        //paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        //paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }

    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
            int selectedIndex,
            int x, int y, int w, int h) {
        Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

        g.setColor(getModel().getDividerColor());

        if (tabPlacement != TOP || selectedIndex < 0 || (selRect.y + selRect.height + 1 < y)
                || (selRect.x < x || selRect.x > x + w)) {
            g.drawLine(x, y, x + w - 2, y);
        } else {
            // Break line to show visual connection to selected tab
            g.drawLine(x, y, selRect.x + 1, y);
            if (selRect.x + selRect.width < x + w - 2) {
                g.drawLine(selRect.x + selRect.width - 1, y,
                        x + w - 2, y);
            } else {
                g.setColor(shadow);
                g.drawLine(x + w - 2, y, x + w - 2, y);
            }
        }
    }

    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
            int selectedIndex,
            int x, int y, int w, int h) {
        Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

        g.setColor(getModel().getDividerColor());

        // Draw unbroken line if tabs are not on LEFT, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        //
        if (tabPlacement != LEFT || selectedIndex < 0 || (selRect.x + selRect.width + 1 < x)
                || (selRect.y < y || selRect.y > y + h)) {
            g.drawLine(x, y, x, y + h - 2);
        } else {
            // Break line to show visual connection to selected tab
            g.drawLine(x, y, x, selRect.y - 1);
            if (selRect.y + selRect.height < y + h - 2) {
                g.drawLine(x, selRect.y + selRect.height,
                        x, y + h - 2);
            }
        }
    }

    @Override
    protected void layoutLabel(int tabPlacement,
            FontMetrics metrics, int tabIndex,
            String title, Icon icon,
            Rectangle tabRect, Rectangle iconRect,
            Rectangle textRect, boolean isSelected) {
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            tabPane.putClientProperty("html", v);
        }

        SwingUtilities.layoutCompoundLabel((JComponent) tabPane,
                metrics, title + getSubTitle(tabIndex), icon,
                SwingUtilities.CENTER,
                SwingUtilities.LEFT,
                SwingUtilities.CENTER,
                SwingUtilities.LEFT,
                tabRect,
                iconRect,
                textRect,
                textIconGap);

        tabPane.putClientProperty("html", null);

        int xNudge = 10;
        int yNudge = 0;
        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement,
            Font font, FontMetrics metrics, int tabIndex,
            String title, Rectangle textRect,
            boolean isSelected) {

        g.setFont(font);

        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);

            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                Color fg = tabPane.getForegroundAt(tabIndex);
//                if (isSelected && (fg instanceof UIResource)) {
//                    Color selectedFG = UIManager.getColor( "TabbedPane.selectedForeground");
//                    if (selectedFG != null) {
//                        fg = selectedFG;
//                    }
//                }
                if (isSelected) {
                    fg = getModel().getForceColor();
                } else {
                    fg = getModel().getUnselectedColor();
                }
                g.setColor(fg);
                SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
                        title, mnemIndex,
                        textRect.x, textRect.y + metrics.getAscent());

                // Show SubTitle
                String subtitle = this.getSubTitle(tabIndex);
                if (subtitle.length() > 0) {
                    int w = SwingUtilities2.stringWidth(tabPane, metrics, title);
                    if (isSelected) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.YELLOW);
                    }
                    SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
                            subtitle, mnemIndex,
                            textRect.x + w + 2, textRect.y + metrics.getAscent());
                }

            } else { // tab disabled
                g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
                SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
                        title, mnemIndex,
                        textRect.x, textRect.y + metrics.getAscent());
                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
                        title, mnemIndex,
                        textRect.x - 1, textRect.y + metrics.getAscent() - 1);

            }
        }
    }

    @Override
    protected void paintIcon(Graphics g, int tabPlacement,
            int tabIndex, Icon icon, Rectangle iconRect,
            boolean isSelected) {
        if (icon != null && comp.getTabCount() > 1) {
            Color color = g.getColor();
            if (isSelected) {
                g.setColor(getModel().getForceColor());
            } else {
                g.setColor(getModel().getUnselectedColor());
            }
            icon.paintIcon(tabPane, g, iconRect.x, iconRect.y);
            g.setColor(color);
        }
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
            int x, int y, int w, int h, boolean isSelected) {
        Rectangle rect = getTabBounds(tabIndex, new Rectangle(x, y, w, h));
        Graphics2D g2 = (Graphics2D) g;
        Composite old = g2.getComposite();
        AlphaComposite comp1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f);
        g2.setComposite(comp1);
        Color color;
        if (isSelected) {
            color = getModel().getSelectColorSet1();
        } else {
            color = getModel().getDividerColor();
        }
        //g2.setStroke(new BasicStroke(3));
        g2.setColor(color);
        g2.drawLine(rect.x + rect.width, 0, rect.x + rect.width, 20);
        g2.setComposite(old);
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement,
            int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2d = (Graphics2D) g;
        Color color1;
        Color color2;
        Rectangle rect = rects[tabIndex];
        if (isSelected) {
            color1 = getModel().getSelectColorSet1();
            color2 = getModel().getSelectColorSet2();
        } else {
            color1 = getModel().getDefaultColorSet1();
            color2 = getModel().getDefaultColorSet2();
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int width = rect.width;
        int xpos = rect.x;
        int yPos = rect.y;
        if (tabIndex > -1) {
            width -= 1;
            xpos++;
            yPos += 2;
        }
        if (!isSelected) {
            yPos += 2;
        }
        Shape p = this.getArea(xpos, yPos, width, h);
        if (isSelected) {
            OriPainter.basicDrawDropShadow(g, new Area(p),
                    Color.BLACK, 1, 1, Math.PI * 3 / 2, 0.5f);
            g2d.setPaint(new GradientPaint(xpos, 0, color1, xpos, p.getBounds().height, color2));
            g2d.fill(p);
            g2d.setColor(Color.WHITE);
            g2d.draw(GeomOperator.centerFixShrinkCopy(new Area(p), 1.1));
        } else {
            Area area = new Area(p);

            //draw shadow
            Rectangle r = area.getBounds();
            GeneralPath path = new GeneralPath();
            path.moveTo(r.x, r.y);
            path.lineTo(r.x + 3, r.y + r.height);
            path.lineTo(r.x + r.width - 6, r.y + r.height);
            path.lineTo(r.x + r.width, r.y);
            path.closePath();
            OriPainter.basicDrawDropShadow(g, new Area(path), Color.BLACK, 2, 0, Math.PI * 3 / 2, 0.5f);

            //绘制分段过度
            Area top = (Area) area.clone();
            top.subtract(new Area(new Rectangle(r.x, r.y + r.height / 2, r.width, r.height / 2)));
            g2d.setPaint(new GradientPaint(r.x, r.y, new Color(160, 189, 230), r.x, r.y + r.height / 2,
                    new Color(135, 179, 227)));
            g2d.fill(top);
            Area bottom = (Area) area.clone();
            bottom.subtract(new Area(new Rectangle(r.x, r.y, r.width, r.height / 2)));
            g2d.setPaint(new GradientPaint(r.x, r.y + r.height / 2, new Color(123, 172, 225), r.x, r.y + r.height,
                    new Color(95, 166, 213)));
            g2d.fill(bottom);
            //绘制内框
            Area inner = GeomOperator.centerFixShrinkCopy((Area) area.clone(), 1.0);
            Area lines = (Area) area.clone();
            lines.subtract(inner);
            g2d.setPaint(new GradientPaint(r.x, r.y, new Color(1.0f, 1.0f, 1.0f, 0.5f), r.x, r.y + r.height,
                    new Color(1.0f, 1.0f, 1.0f, 0.0f)));
            g2d.fill(lines);

        }
    }

    private Shape getArea(int x, int y, int w, int h) {
        RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, w, h + 2, 6, 6);
        Area b = new Area(rect);
        b.subtract(new Area(new Rectangle(x, y + h, w, 3)));
        return b;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return getModel().getDividerHeight();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        //draw the background of the Tabbed Pane.
        Graphics2D g2 = (Graphics2D) g;
        Rectangle rect = new Rectangle(-4, 0, c.getWidth() + 4, c.getHeight());
        BufferedImage img = OriShadow.INSTANCE.createInnerShadow(rect,
                new GradientPaint(0, 0, new Color(123, 178, 224), 0,
                c.getHeight(), new Color(95, 166, 213)),
                -1, 0.3f, Color.BLACK, 10, 270);
        g2.drawImage(img, 0, 0, null);
        super.paint(g, c);
    }

    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        if (comp.getSelectedIndex() == tabIndex) {
            return new Insets(7, 16, 10, 6);
        }
        return new Insets(5, 16, 9, 6);
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        Insets tabInsets1 = getTabInsets(tabPlacement, tabIndex);
        int width = tabInsets1.left + tabInsets1.right + 3;
        Component tabComponent = tabPane.getTabComponentAt(tabIndex);
        if (tabComponent != null) {
            width += tabComponent.getPreferredSize().width;
        } else {
            Icon icon = getIconForTab(tabIndex);
            if (icon != null) {
                width += icon.getIconWidth() + textIconGap;
            }
            View v = getTextViewForTab(tabIndex);
            if (v != null) {
                // html
                width += (int) v.getPreferredSpan(View.X_AXIS);
            } else {
                // plain text
                String title = tabPane.getTitleAt(tabIndex) + this.getSubTitle(tabIndex);
                width += SwingUtilities2.stringWidth(tabPane, metrics, title);
            }
        }
        return width;
    }

    private String getSubTitle(int tabIndex) {
        String subTitle = comp.getSubTitle(tabIndex);
        if (subTitle != null && subTitle.trim().length() > 0) {
            return subTitle;
        }
        return "";
    }
}
