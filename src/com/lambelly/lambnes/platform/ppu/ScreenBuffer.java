package com.lambelly.lambnes.platform.ppu;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import java.io.File;
import javax.imageio.ImageIO;
import java.util.Calendar;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.gui.LambNesGui;
import com.lambelly.lambnes.gui.Screen;
import org.apache.log4j.*;

public class ScreenBuffer
{
	private int[] screenBuffer = new int[Screen.SCREEN_HORIZONTAL_RESOLUTION * Screen.SCREEN_VERTICAL_RESOLUTION];
	private Logger logger = Logger.getLogger(ScreenBuffer.class);
	
	public ScreenBuffer()
	{
		
	}
	
	public void pushBufferToScreen()
	{
		LambNesGui.getScreen().setImage(this.getBufferedImageFromScreenBuffer());
	}
	
    private BufferedImage getBufferedImageFromScreenBuffer() 
    {
        BufferedImage image = new BufferedImage(Screen.SCREEN_HORIZONTAL_RESOLUTION, Screen.SCREEN_VERTICAL_RESOLUTION, BufferedImage.TYPE_INT_ARGB_PRE);
        WritableRaster raster = image.getRaster();
        int[] pixels = ((DataBufferInt) raster.getDataBuffer()).getData();
        System.arraycopy(this.getScreenBuffer(), 0, pixels, 0, this.getScreenBuffer().length);
        return image;
    }
    
    public void toFile()
    {
    	this.toFile(null);
    }
    
    public void toFile(String identifier)
    {
    	try 
    	{
    		Calendar cal = Calendar.getInstance();
    	    File outputfile = new File(Long.toString(cal.getTimeInMillis()) + ((identifier != null) ? identifier : "") + ".png");
    	    ImageIO.write(this.getBufferedImageFromScreenBuffer(), "png", outputfile);
    	} 
    	catch (Exception e) 
    	{
    	    logger.error("problem writing screen buffer to file");
    	}

    }
    
    public void setScreenBufferPixel(int arrayIndex, int color)
    {
    	this.screenBuffer[arrayIndex] = color;
    }
    
    public void setScreenBufferPixel(int horizontal, int vertical, int color)
	{
    	//logger.info("setting color to buffer from int: h: " + horizontal + " v: " + vertical + " color:" + color );
		this.screenBuffer[this.coordinatesToArrayIndex(horizontal, vertical)] = color;
	}
    
    public void setScreenBufferPixel(int horizontal, int vertical, PaletteColor color)
    {
    	//logger.info("setting color to buffer from palette: h: " + horizontal + " v: " + vertical + " color:" + color );
    	this.setScreenBufferPixel(horizontal, vertical, color.getMasterPaletteColor().getColorInt());
    }
    
    public void setScreenBufferTileRow(int horizontalCoord, int verticalCoord, int hFineScrollOffset, int vFineScrollOffset, PaletteColor[] tileRow)
    {
    	int bufferIndexStart = this.coordinatesToArrayIndex(horizontalCoord, verticalCoord);
    	//for (int i=0;i<tileRow.length;i++)
    	//{
    	//	logger.info("setting color to buffer: h: " + horizontalCoord + " v: " + verticalCoord + " hFileScrollOffset " + hFineScrollOffset +  "  vfineScrollOffet: " + vFineScrollOffset + " bufferIndexStart: " + bufferIndexStart + " color:" + tileRow[i]);
    	//}
    	
    	
    	for (int tileRowIndex = 0; tileRowIndex < tileRow.length; tileRowIndex++)
    	{
        	// deal with fine scroll -- if at start of line or end of line, truncate icon. If otherwise, offset icon.
    		if ((horizontalCoord - hFineScrollOffset >= 0 && horizontalCoord - hFineScrollOffset < Screen.SCREEN_HORIZONTAL_RESOLUTION) &&
    			(verticalCoord - vFineScrollOffset >= 0 && verticalCoord - vFineScrollOffset < Screen.SCREEN_VERTICAL_RESOLUTION))
    		{
    			this.setScreenBufferPixel((bufferIndexStart + tileRowIndex) - hFineScrollOffset, tileRow[tileRowIndex].getMasterPaletteColor().getColorInt());
    		}
    	}
    }
	
    private int coordinatesToArrayIndex(int horizontal, int vertical)
    {
    	return (vertical * LambNesGui.getScreen().SCREEN_HORIZONTAL_RESOLUTION) + horizontal;
    }
    
	public int[] getScreenBuffer()
	{
		return screenBuffer;
	}

	public void setScreenBuffer(int[] screenBuffer)
	{
		this.screenBuffer = screenBuffer;
	}	
}
