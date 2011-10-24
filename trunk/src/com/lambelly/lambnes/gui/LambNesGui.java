package com.lambelly.lambnes.gui;

import java.awt.*;  
import java.awt.event.*;   
import javax.swing.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;

public class LambNesGui extends JFrame implements Runnable 
{
	private static Screen screen = new Screen();
	private static Container content = null;
	private static boolean run = true;
	private PatternTableVisualization sptv = new PatternTableVisualization(LambNesGui.SPRITE_PATTERN_TABLE_TOOL_TIP_TEXT, PatternTableVisualization.PATTERN_TABLE_VISUALIZATION_SPRITE);
	private PatternTableVisualization bptv = new PatternTableVisualization(LambNesGui.BACKGROUND_PATTERN_TABLE_TOOL_TIP_TEXT, PatternTableVisualization.PATTERN_TABLE_VISUALIZATION_IMAGE);
	private PaletteVisualization bpv = new PaletteVisualization(0x3F00, 16);
	private PaletteVisualization spv = new PaletteVisualization(0x3F10, 16);
	private Timer workertimer = null;
	private Logger logger = Logger.getLogger(LambNesGui.class);
	public static final int SLEEP_TIME = 125;
	public static final int WORKER_INTIAL_DELAY = 10000;
	public static final int WORKER_PAUSE = 100000;
	public static final String SCREEN_TITLE = "LambNes"; 
	public static final String SPRITE_PATTERN_TABLE_TOOL_TIP_TEXT = "Sprite Pattern Table";
	public static final String BACKGROUND_PATTERN_TABLE_TOOL_TIP_TEXT = "Background Pattern Table";
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
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					logger.debug("key: esc released");
					Platform.getControllerPorts().getPortA().setSelect(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					logger.debug("key: K released");
					Platform.getControllerPorts().getPortA().setStart(false);
				}
			}

			public void keyTyped(KeyEvent e)
			{
				
			}
        });
        
        // add panels to content pane
        setContent(getContentPane());
        getContent().setLayout(new GridLayout(2,3));
        getContent().add(getSpv());
        MasterPaletteVisualization mpv = new MasterPaletteVisualization();
        getContent().add(mpv);
        getContent().add(getBpv());
        getContent().add(getSptv());
        getContent().add(LambNesGui.getScreen());
        getContent().add(getBptv());
        this.pack();    
        
        // set up worker threads that update the visualizations
        ActionListener workerActionListener = new ActionListener() 
        {
            public void actionPerformed(ActionEvent actionEvent) 
            {
                PatternTableWorker sptw = new PatternTableWorker(sptv);
                sptw.execute();
                PatternTableWorker bptw = new PatternTableWorker(bptv);
                bptw.execute();
				getBpv().refreshPalette();
				getSpv().refreshPalette();
				getWorkertimer().restart();
            }
        };
        
        setWorkertimer(new Timer(LambNesGui.WORKER_PAUSE, workerActionListener));
        getWorkertimer().setInitialDelay(LambNesGui.WORKER_INTIAL_DELAY);
        getWorkertimer().start(); 
	}
	
	public void  run()
	{   
		try
		{   
			while(LambNesGui.isRun())
			{   
				Thread.sleep(LambNesGui.SLEEP_TIME);   
				content.repaint();    
			}   
		}   
		catch(InterruptedException e)
		{   
			e.printStackTrace();   
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

	public PaletteVisualization getBpv()
	{
		return bpv;
	}

	public void setBpv(PaletteVisualization bpv)
	{
		this.bpv = bpv;
	}

	public PaletteVisualization getSpv()
	{
		return spv;
	}

	public void setSpv(PaletteVisualization spv)
	{
		this.spv = spv;
	}

	public Timer getWorkertimer()
	{
		return workertimer;
	}

	public void setWorkertimer(Timer workertimer)
	{
		this.workertimer = workertimer;
	}

	public PatternTableVisualization getSptv()
	{
		return sptv;
	}

	public void setSptv(PatternTableVisualization sptv)
	{
		this.sptv = sptv;
	}

	public PatternTableVisualization getBptv()
	{
		return bptv;
	}

	public void setBptv(PatternTableVisualization bptv)
	{
		this.bptv = bptv;
	}   
}
