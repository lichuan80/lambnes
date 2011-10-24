package com.lambelly.lambnes.gui;

import javax.swing.*;
import java.awt.image.BufferedImage;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.*;
import java.awt.*;
import org.apache.log4j.*;

public class PatternTableIcon implements Icon
{
	public int tileNumber = 0;
	public int getIconWidth() { return 8; }
    public int getIconHeight() { return 8; }
    private BufferedImage icon = new BufferedImage(this.getIconWidth(), this.getIconHeight(), BufferedImage.TYPE_INT_RGB);
    private Logger logger = Logger.getLogger(PatternTableIcon.class);    
    
    public PatternTableIcon(int spriteNumber)
    {
    	this.setTileNumber(spriteNumber);
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y) 
    {
    	if (Platform.getPpu() != null)
    	{
    		if (Platform.getPpu().getPpuMaskRegister().isSpriteVisibility())
    		{
    			if(logger.isDebugEnabled())
    			{
    				logger.debug("drawing icon");
    			}
    			g.drawImage(this.getIcon(),0,0,null);
    		}
    	}
    }
	public int getTileNumber()
	{
		return tileNumber;
	}
	public void setTileNumber(int tileNumber)
	{
		this.tileNumber = tileNumber;
	}
	public BufferedImage getIcon()
	{
		return icon;
	}
	public void setIcon(BufferedImage icon)
	{
		this.icon = icon;
	}
}
