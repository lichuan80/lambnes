package com.lambelly.lambnes.gui;

import java.awt.event.*; 
import org.apache.log4j.*;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.platform.controllers.NesControllerPorts;

public class LambNesKeyListener implements KeyListener
{
	private NesControllerPorts controllerPorts = LambNes.getPlatform().getControllerPorts();
	private Logger logger = Logger.getLogger(LambNesKeyListener.class);
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			this.getControllerPorts().getPortA().setUp(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_A)
		{
			this.getControllerPorts().getPortA().setLeft(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			this.getControllerPorts().getPortA().setRight(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_X)
		{
			this.getControllerPorts().getPortA().setDown(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_J)
		{
			this.getControllerPorts().getPortA().setA(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_K)
		{
			this.getControllerPorts().getPortA().setB(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.getControllerPorts().getPortA().setSelect(true);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			this.getControllerPorts().getPortA().setStart(true);					
		}
	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			this.getControllerPorts().getPortA().setUp(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_A)
		{
			this.getControllerPorts().getPortA().setLeft(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			this.getControllerPorts().getPortA().setRight(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_X)
		{
			this.getControllerPorts().getPortA().setDown(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_J)
		{
			this.getControllerPorts().getPortA().setA(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_K)
		{
			this.getControllerPorts().getPortA().setB(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.getControllerPorts().getPortA().setSelect(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			this.getControllerPorts().getPortA().setStart(false);
		}
	}

	public void keyTyped(KeyEvent e)
	{
		
	}

	public NesControllerPorts getControllerPorts()
    {
    	return controllerPorts;
    }

	public void setControllerPorts(NesControllerPorts controllerPorts)
    {
    	this.controllerPorts = controllerPorts;
    }
}
