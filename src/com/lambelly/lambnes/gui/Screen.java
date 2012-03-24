package com.lambelly.lambnes.gui;

import java.awt.*;   
import java.awt.image.*;  

import org.apache.log4j.Logger;

public class Screen extends javax.swing.JPanel 
{
    private BufferedImage image = new BufferedImage(Screen.SCREEN_HORIZONTAL_RESOLUTION, Screen.SCREEN_VERTICAL_RESOLUTION, BufferedImage.TYPE_INT_ARGB);
    private Logger logger = Logger.getLogger(Screen.class);
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
    public Screen() 
    {               	
    	if(logger.isDebugEnabled())
    	{
    		logger.debug("initializing");
    	}
    	this.setBounds(0, 0, Screen.SCREEN_HORIZONTAL_RESOLUTION, Screen.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(Screen.SCREEN_HORIZONTAL_RESOLUTION, Screen.SCREEN_VERTICAL_RESOLUTION));
    }            
    
    @Override          
    public void paint(Graphics g) 
    {              
        if (this.getImage() != null && isShowing()) 
        {
            g.drawImage(this.getImage(), 0, 0, this);
        }
    }

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}      
}
