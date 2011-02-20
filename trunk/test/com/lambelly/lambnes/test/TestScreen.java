package com.lambelly.lambnes.test;

import java.awt.*;   
import java.awt.image.*;  

import org.apache.log4j.Logger;

public class TestScreen extends javax.swing.JPanel 
{
    private BufferedImage image = null;           
    private Logger logger = Logger.getLogger(TestScreen.class);
	
    public TestScreen(BufferedImage bi) 
    {               	
    	if(logger.isDebugEnabled())
    	{
    		logger.debug("initializing");
    	}
    	
    	this.setImage(bi);
    	this.setBounds(0, 0, bi.getWidth(),bi.getHeight());
    	this.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
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
		logger.debug("setting image");
		if (image == null)
		{
			logger.debug("danger -- image is null");
		}
		this.image = image;
	}      
}
