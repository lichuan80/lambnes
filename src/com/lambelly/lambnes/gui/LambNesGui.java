package com.lambelly.lambnes.gui;

import java.awt.*;  
import java.awt.event.*;   
import javax.swing.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;

public class LambNesGui extends JFrame implements Runnable
{
	private static Screen screen = new Screen();
	private Logger logger = Logger.getLogger(LambNesGui.class);
	public static final String SCREEN_TITLE = "LambNes"; 
	private static final boolean SPRITE_PATTERN_TABLE_VISUALIZATION = true;
	private static final boolean BACKGROUND_PATTERN_TABLE_VISUALIZATION = true;
	private static final boolean SPRITE_PALETTE_VISUALIZATION = true;
	private static final boolean BACKGROUND_PALETTE_VISUALIZATION = true;
	private static final boolean MASTER_PALETTE_VISUALIZATION = true;
	
	public void run() 
	{
        this.setTitle(LambNesGui.SCREEN_TITLE);
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
            	Platform.setRun(false);
            	dispose();    					
            }
        });
        
        this.addKeyListener(new KeyListener()
        {
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_W)
				{
					logger.info("key: W pressed");
					Platform.getControllerPorts().getPortA().setUp(true);
				}
				if (e.getKeyCode() == KeyEvent.VK_A)
				{
					logger.info("key: A pressed");
					Platform.getControllerPorts().getPortA().setLeft(true);
				}
				if (e.getKeyCode() == KeyEvent.VK_D)
				{
					logger.info("key: D pressed");
					Platform.getControllerPorts().getPortA().setRight(true);
				}
				if (e.getKeyCode() == KeyEvent.VK_X)
				{
					logger.info("key: X pressed");
					Platform.getControllerPorts().getPortA().setDown(true);
				}
			}

			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_W)
				{
					logger.info("key: W released");
					Platform.getControllerPorts().getPortA().setUp(false);
				}
				if (e.getKeyCode() == KeyEvent.VK_A)
				{
					logger.info("key: A released");
					Platform.getControllerPorts().getPortA().setLeft(false);
				}
				if (e.getKeyCode() == KeyEvent.VK_D)
				{
					logger.info("key: D released");
					Platform.getControllerPorts().getPortA().setRight(false);
				}
				if (e.getKeyCode() == KeyEvent.VK_X)
				{
					logger.info("key: X released");
					Platform.getControllerPorts().getPortA().setDown(false);
				}
			}

			public void keyTyped(KeyEvent e)
			{
				
			}
        });
        
        Container content = getContentPane();
        content.setLayout(new GridLayout(2,3));
        SpritePaletteVisualization spv = new SpritePaletteVisualization();
        content.add(spv);
        MasterPaletteVisualization mpv = new MasterPaletteVisualization();
        content.add(mpv);
        BackgroundPaletteVisualization bpv = new BackgroundPaletteVisualization();
        content.add(bpv);
        SpritePatternTableVisualization sptv = new SpritePatternTableVisualization();
        content.add(sptv);
        content.add(LambNesGui.getScreen());
        BackgroundPatternTableVisualization bptv = new BackgroundPatternTableVisualization();
        content.add(bptv);
        
        this.pack();
        this.setVisible(true);        
        
        while (Platform.isRun())
        {
        	content.repaint();
        }
        
	}  

	public static Screen getScreen()
	{
		return screen;
	}

	public static void setScreen(Screen s)
	{
		screen = s;
	}   
}
