package com.lambelly.lambnes.gui;

import javax.swing.JPanel;   
import javax.swing.SwingUtilities;   

public class PanelThread implements Runnable
{
	private JPanel panel;   
	  
	public PanelThread(JPanel p)
	{   
		panel = p;   
	}   
	    	  
	public void  run()
	{   
		try
		{   
			while(true)
			{   
				Thread.sleep(50);   
	  
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

}
