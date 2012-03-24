package com.lambelly.lambnes.test;

import java.awt.*;  
import java.awt.event.*;  
import java.awt.image.*;
import javax.swing.*;

import org.apache.log4j.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.BackgroundTile;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;
import com.lambelly.lambnes.util.ArrayUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class TileTestImageViewer extends JFrame implements ActionListener, KeyListener, Runnable
{
	private BufferedImage image = null;
	private Logger logger = Logger.getLogger(TileTestImageViewer.class);
	@Autowired
	private Platform platform;
	@Autowired
	private Ines cartridge;
	@Autowired
	private NesPpuMemory ppuMemory;
	
	public static final String SCREEN_TITLE = "LambNes"; 

	
	
	public TileTestImageViewer(int tileNumber) throws Exception
	{
        
		this.getPpuMemory().establishMirroring();
		//NesCpuMemory.setProgramInstructions(Ines.getInstance().getProgramInstructions());
		if(logger.isDebugEnabled())
		{
			logger.debug("pattern tiles head:");
		}
		ArrayUtils.head(this.getCartridge().getPatternTiles(), 16);
		this.getPpuMemory().setPatternTiles(this.getCartridge().getPatternTiles());
		this.getPpuMemory().setImagePalette(new int[]{0x0d, 0x07, 0x17, 0x27, 0x0d, 0x16, 0x26, 0x37, 0x0d, 0x12, 0x3c, 0x30, 0x0d, 0x06, 0x27, 0x38});
        
        
        BackgroundTile bg = new BackgroundTile(tileNumber,0);
        this.setImage(bg.getBufferedImage());
	}
	
	public void run() 
	{
        this.setTitle(TileTestImageViewer.SCREEN_TITLE);
        this.setResizable(false);
        
        this.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
            	if(logger.isDebugEnabled())
            	{
            		logger.debug("window closed.");
            	}
            	setVisible(false);
            	dispose();    					
            }
        });
        
        Container content = getContentPane();
        content.add(new TestScreen(this.getImage()));
        content.add(new PatternTableVisualization("test",1));
        
        this.pack();
        this.setVisible(true);        
        
        while (this.isVisible())
        {
        	content.repaint();
        }
	}  
	
    public void actionPerformed(ActionEvent arg0) 
    {
    	
    }

    public void keyPressed(KeyEvent arg0) 
    {
    	
    }
    
    public void keyReleased(KeyEvent arg0) 
    {	
    
    }
    
    public void keyTyped(KeyEvent arg0) 
    {
    
    }

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	public Platform getPlatform()
    {
    	return platform;
    }

	public void setPlatform(Platform platform)
    {
    	this.platform = platform;
    }

	public Ines getCartridge()
    {
    	return cartridge;
    }

	public void setCartridge(Ines cartridge)
    {
    	this.cartridge = cartridge;
    }

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }

}
