/**
 * 
 */
package com.original.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * Learn from java swingx OBackgroundImagePanel
 * @author Song XueYong
 *
 */
public class OImageBgPanel extends JPanel {
	

	 /**
     * Creates a new <code>OImageBgPanel</code> with a double buffer
     * and a flow layout.
     */
    public OImageBgPanel() {
    }
    
    /**
     * Creates a new <code>OBackgroundImagePanel</code> with <code>FlowLayout</code>
     * and the specified buffering strategy.
     * If <code>isDoubleBuffered</code> is true, the <code>OBackgroundImagePanel</code>
     * will use a double buffer.
     *
     * @param isDoubleBuffered  a boolean, true for double-buffering, which
     *        uses additional memory space to achieve fast, flicker-free 
     *        updates
     */
    public OImageBgPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }
    
    /**
     * Create a new buffered OBackgroundImagePanel with the specified layout manager
     *
     * @param layout  the LayoutManager to use
     */
    public OImageBgPanel(LayoutManager layout) {
        super(layout);
    }
    
    /**
     * Creates a new OBackgroundImagePanel with the specified layout manager and buffering
     * strategy.
     *
     * @param layout  the LayoutManager to use
     * @param isDoubleBuffered  a boolean, true for double-buffering, which
     *        uses additional memory space to achieve fast, flicker-free 
     *        updates
     */
    public OImageBgPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }
    
    
    //background----------->
	/**
	 * 
	 * @param Path
	 */

	public void setWallPaper(String path) {
//		ImagePainter painter = null;
		try {
			BufferedImage img = ImageIO.read(OImageBgPanel.class
					.getResourceAsStream(path));
			this.backgroundImage = img;
//			painter = new ImagePainter(img);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
   
    
	/**
	 * Overridden to provide Painter support. It will call
	 * backgroundPainter.paint() if it is not null, else it will call
	 * super.paintComponent().
	 */
	@Override
	protected void paintComponent(Graphics g) {
		if (backgroundImage != null) {
			if (isOpaque()) {
				super.paintComponent(g);
			}

			Graphics2D g2 = (Graphics2D) g.create();

			try {
				if (this.backgroundImage != null) {					
					g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
				}
			} finally {
				g2.dispose();
			}
		} else {
			super.paintComponent(g);
		}
	}
    
    
    /**
     * Specifies the Painter to use for painting the background of this panel.
     * If no painter is specified, the normal painting routine for JPanel
     * is called. Old behavior is also honored for the time being if no
     * backgroundPainter is specified
     */
    private BufferedImage backgroundImage;
    //background-----------<
    

}
