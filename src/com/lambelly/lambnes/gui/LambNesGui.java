package com.lambelly.lambnes.gui;

import java.awt.*;  
import java.awt.event.*;   
import javax.swing.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;

public class LambNesGui extends JFrame //implements Runnable 
{
	private static Screen screen = new Screen();
	private static Container content = null;
	private static boolean run = true;
	private Logger logger = Logger.getLogger(LambNesGui.class);
	public static final String SCREEN_TITLE = "LambNes"; 
	private static final boolean SPRITE_PATTERN_TABLE_VISUALIZATION = true;
	private static final boolean BACKGROUND_PATTERN_TABLE_VISUALIZATION = true;
	private static final boolean SPRITE_PALETTE_VISUALIZATION = true;
	private static final boolean BACKGROUND_PALETTE_VISUALIZATION = true;
	private static final boolean MASTER_PALETTE_VISUALIZATION = true;
	
	public LambNesGui() 
	{
	    super();
        this.setTitle(LambNesGui.SCREEN_TITLE);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
            	setRun(false);
            	dispose();    					
            }
        });
        
        this.addKeyListener(new KeyListener()
        {
			public void keyPressed(KeyEvent e)
			{
				logger.debug("received KeyEvent " + e.getKeyCode());
				
				if (e.getKeyCode() == KeyEvent.VK_W)
				{
					logger.debug("key: W pressed");
					Platform.getControllerPorts().getPortA().setUp(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_A)
				{
					logger.debug("key: A pressed");
					Platform.getControllerPorts().getPortA().setLeft(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_D)
				{
					logger.debug("key: D pressed");
					Platform.getControllerPorts().getPortA().setRight(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_X)
				{
					logger.debug("key: X pressed");
					Platform.getControllerPorts().getPortA().setDown(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_J)
				{
					logger.debug("key: J pressed");
					Platform.getControllerPorts().getPortA().setA(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_K)
				{
					logger.debug("key: K pressed");
					Platform.getControllerPorts().getPortA().setB(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					logger.debug("key: esc pressed");
					Platform.getControllerPorts().getPortA().setSelect(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					logger.debug("key: enter pressed");
					Platform.getControllerPorts().getPortA().setStart(true);					
				}
			}

			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_W)
				{
					logger.info("key: W released");
					Platform.getControllerPorts().getPortA().setUp(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_A)
				{
					logger.info("key: A released");
					Platform.getControllerPorts().getPortA().setLeft(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_D)
				{
					logger.info("key: D released");
					Platform.getControllerPorts().getPortA().setRight(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_X)
				{
					logger.info("key: X released");
					Platform.getControllerPorts().getPortA().setDown(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_J)
				{
					logger.debug("key: J released");
					Platform.getControllerPorts().getPortA().setA(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_K)
				{
					logger.debug("key: K released");
					Platform.getControllerPorts().getPortA().setB(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_J)
				{
					logger.debug("key: J released");
					Platform.getControllerPorts().getPortA().setA(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_K)
				{
					logger.debug("key: K released");
					Platform.getControllerPorts().getPortA().setB(false);
				}
			}

			public void keyTyped(KeyEvent e)
			{
				
			}
        });
        
        setContent(getContentPane());
        getContent().setLayout(new GridLayout(2,3));
        PaletteVisualization spv = new PaletteVisualization(0x3F10);
        content.add(spv);
        MasterPaletteVisualization mpv = new MasterPaletteVisualization();
        content.add(mpv);
        PaletteVisualization bpv = new PaletteVisualization(0x3F00);
        content.add(bpv);
        SpritePatternTableVisualization sptv = new SpritePatternTableVisualization();
        content.add(sptv);
        getContent().add(LambNesGui.getScreen());
        BackgroundPatternTableVisualization bptv = new BackgroundPatternTableVisualization();
        content.add(bptv);
        this.pack();    
	}

	public static Screen getScreen()
	{
		return screen;
	}

	public static void setScreen(Screen s)
	{
		screen = s;
	}

	public static boolean isRun()
	{
		return run;
	}

	public static void setRun(boolean run)
	{
		LambNesGui.run = run;
	}

	public static boolean isSpritePatternTableVisualization()
	{
		return SPRITE_PATTERN_TABLE_VISUALIZATION;
	}

	public static boolean isBackgroundPatternTableVisualization()
	{
		return BACKGROUND_PATTERN_TABLE_VISUALIZATION;
	}

	public static boolean isSpritePaletteVisualization()
	{
		return SPRITE_PALETTE_VISUALIZATION;
	}

	public static boolean isBackgroundPaletteVisualization()
	{
		return BACKGROUND_PALETTE_VISUALIZATION;
	}

	public static boolean isMasterPaletteVisualization()
	{
		return MASTER_PALETTE_VISUALIZATION;
	}


	public static Container getContent()
	{
		return content;
	}


	public static void setContent(Container content)
	{
		LambNesGui.content = content;
	}   
}
