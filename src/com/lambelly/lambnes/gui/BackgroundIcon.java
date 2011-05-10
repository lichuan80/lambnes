package com.lambelly.lambnes.gui;

import javax.swing.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.*;
import java.awt.*;
import org.apache.log4j.*;

public class BackgroundIcon implements Icon
{
	public int tileNumber = 0;
	public int getIconWidth() { return 8; }
    public int getIconHeight() { return 8; }
    private Logger logger = Logger.getLogger(BackgroundIcon.class);    
    
    public BackgroundIcon(int tileNumber)
    {
    	this.setTileNumber(tileNumber);
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y) 
    {
    	if (Platform.getPpu() != null)
    	{
    		if (Platform.getPpu().getPpuMaskRegister().isBackgroundVisibility())
    		{    			
    			BackgroundTile b = NesTileCache.getBackgroundTile(this.getTileNumber());
    			g.drawImage(b.getBufferedImage(),0,0,null);
    			
    			if (logger.isDebugEnabled())
    			{
    				logger.debug("drawing icon " + this.getTileNumber());
    				logger.debug(b);
    			}
    		}
    	}
    }
	public int getTileNumber()
	{
		return tileNumber;
	}
	public void setTileNumber(int spriteNumber)
	{
		this.tileNumber = spriteNumber;
	}
}
