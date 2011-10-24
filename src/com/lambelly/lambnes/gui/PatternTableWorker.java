package com.lambelly.lambnes.gui;

import javax.swing.SwingWorker;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.ppu.NesTileCache;

public class PatternTableWorker extends SwingWorker<Void, Void> 
{
	private Logger logger = Logger.getLogger(PatternTableWorker.class);
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
    		if (this.getPatternTableVisualization().getPatternTableVisualizationType() == PatternTableVisualization.PATTERN_TABLE_VISUALIZATION_SPRITE)
    		{
    			this.getPatternTableVisualization().getPatternTableIcon(x).setIcon(NesTileCache.getSpriteTile(x).getBufferedImage());
    		}
    		else
    		{
    			this.getPatternTableVisualization().getPatternTableIcon(x).setIcon(NesTileCache.getBackgroundTile(x).getBufferedImage());
    		}
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
