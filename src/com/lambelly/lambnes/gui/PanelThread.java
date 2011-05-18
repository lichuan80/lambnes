package com.lambelly.lambnes.gui;

import javax.swing.JPanel;   
import javax.swing.SwingUtilities;   

public class PanelThread implements Runnable
{
	private JPanel panel;   
	private int sleepTime = 50;
	
	public PanelThread(JPanel p, int sleepTime)
	{   
		panel = p;   
	}   
	    	  
	public void  run()
	{   
		try
		{   
			while(true)
			{   
				Thread.sleep(this.getSleepTime());   
	  
				SwingUtilities.invokeLater
				(   
						new Runnable()
						{   
							public void run()
							{   
								panel.repaint(); 
							}   
						}   
				);   
			}   
		}   
		catch(InterruptedException e)
		{   
			e.printStackTrace();   
		}      
	}

	public int getSleepTime()
	{
		return sleepTime;
	}

	public void setSleepTime(int sleepTime)
	{
		this.sleepTime = sleepTime;
	}  

}
