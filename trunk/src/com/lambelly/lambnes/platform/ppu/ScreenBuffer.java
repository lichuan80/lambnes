package com.lambelly.lambnes.platform.ppu;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import com.lambelly.lambnes.gui.LambNesGui;
import com.lambelly.lambnes.gui.Screen;

public class ScreenBuffer
{
	private int[] screenBuffer = new int[Screen.SCREEN_HORIZONTAL_RESOLUTION * Screen.SCREEN_VERTICAL_RESOLUTION];
	
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
    
    public void setScreenBufferPixel(int arrayIndex, int color)
    {
    	this.screenBuffer[arrayIndex] = color;
    }
    
    public void setScreenBufferPixel(int horizontal, int vertical, int color)
	{
		this.screenBuffer[this.coordinatesToArrayIndex(horizontal, vertical)] = color;
	}
    
    public void setScreenBufferPixel(int horizontal, int vertical, PaletteColor color)
    {
    	this.setScreenBufferPixel(horizontal, vertical, color.getMasterPaletteColor().getColorInt());
    }
    
    public void setScreenBufferTileRow(int horizontal, int vertical, PaletteColor[] tileRow)
    {
    	int bufferIndexStart = this.coordinatesToArrayIndex(horizontal, vertical);
    	for (int tileRowOffset = 0; tileRowOffset < tileRow.length; tileRowOffset++)
    	{
    		this.setScreenBufferPixel(bufferIndexStart + tileRowOffset, tileRow[tileRowOffset].getMasterPaletteColor().getColorInt());
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
