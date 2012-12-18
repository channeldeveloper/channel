package com.original.widget.event;

import com.original.widget.model.TableGroup;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OTableGroupRenderer extends DefaultTableCellRenderer {

    private Color bgColor = new Color(255, 255, 255);

    public OTableGroupRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

//        if ( column > 1 ) hasFocus = false;
//        System.out.println("Groupname="+column+":"+(value!=null?value.toString():""));
        if ( value instanceof TableGroup){
            this.setIcon(((TableGroup)value).getIcon());
            this.setText(((TableGroup)value).toString());
        }         
        Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        this.setColor(cell, table, isSelected, hasFocus, row, column);
        this.setFont(table.getFont());
        this.setOpaque(false);
        return this;

    }

    /**
     *
     * 设置颜色
     *
     */
    private void setColor(Component component, JTable table,
            boolean isSelected, boolean hasFocus, int row, int column) {
        component.setBackground(bgColor);
        setBorder(null);// 去掉边
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Stroke drawingStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        Line2D line = new Line2D.Double(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(new Color(170,198,237));
        g2d.setStroke(drawingStroke);
        g2d.draw(line);

        /*OriPainter.drawLine(g, new Point(0, this.getHeight() - 3),
                new Point(this.getWidth(), this.getHeight() - 3), new Color(203, 202, 201), 1);
        OriPainter.drawLine(g, new Point(0, this.getHeight() - 2),
                new Point(this.getWidth(), this.getHeight() - 2), new Color(203, 202, 201), 1);
        OriPainter.drawLine(g, new Point(0, this.getHeight() - 1),
                new Point(this.getWidth(), this.getHeight() - 1), new Color(255, 255, 255), 1);*/
    }
}
