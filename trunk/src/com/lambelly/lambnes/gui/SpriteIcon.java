package com.lambelly.lambnes.gui;

import javax.swing.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.*;
import java.awt.*;
import org.apache.log4j.*;

public class SpriteIcon implements Icon
{
	public int spriteNumber = 0;
	public int getIconWidth() { return 8; }
    public int getIconHeight() { return 8; }
    private Logger logger = Logger.getLogger(SpriteIcon.class);    
    
    public SpriteIcon(int spriteNumber)
    {
    	this.setSpriteNumber(spriteNumber);
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
    			g.drawImage(NesTileCache.getSpriteTile(this.getSpriteNumber()).getBufferedImage(),0,0,null);
    		}
    	}
    }
	public int getSpriteNumber()
	{
		return spriteNumber;
	}
	public void setSpriteNumber(int spriteNumber)
	{
		this.spriteNumber = spriteNumber;
	}
}
