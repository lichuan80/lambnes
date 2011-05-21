package com.lambelly.lambnes.gui;

import javax.swing.SwingWorker;
import com.lambelly.lambnes.platform.ppu.NesTileCache;

public class PatternTableWorker extends SwingWorker<Void, Void> 
{
	private PatternTableVisualization patternTableVisualization = null;
	
    public PatternTableWorker(PatternTableVisualization ptv) 
    {
    	this.setPatternTableVisualization(ptv);
    }


    @Override
    protected Void doInBackground() throws Exception 
    {
    	for (int x = 0; x <  this.getPatternTableVisualization().getPatternTableIcons().length; x++)
    	{
    		this.getPatternTableVisualization().getPatternTableIcon(x).setIcon(NesTileCache.getSpriteTile(x).getBufferedImage());
    	}
    	
    	return null;
    }

    @Override
    protected void done() 
    {
    }


	public PatternTableVisualization getPatternTableVisualization()
	{
		return patternTableVisualization;
	}


	public void setPatternTableVisualization(
			PatternTableVisualization patternTableVisualization)
	{
		this.patternTableVisualization = patternTableVisualization;
	}
}
