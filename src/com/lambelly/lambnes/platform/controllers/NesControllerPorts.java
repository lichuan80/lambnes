package com.lambelly.lambnes.platform.controllers;

import org.apache.log4j.Logger;

public class NesControllerPorts
{
	private ControlRegister1 controlRegister1;
	private ControlRegister2 controlRegister2;
	private NesController portA = null;
	private NesController portB = null;
	private Logger logger = Logger.getLogger(NesControllerPorts.class);
	
	public NesControllerPorts()
	{
		this.setPortA(new NesJoypad(1));
	}
	
	public void cycle()
	{
		this.getControlRegister1().cycle();
		this.getControlRegister2().cycle();
	}
	
	public ControlRegister1 getControlRegister1()
	{
		return controlRegister1;
	}
	public void setControlRegister1(ControlRegister1 controlRegister1)
	{
		this.controlRegister1 = controlRegister1;
	}
	public ControlRegister2 getControlRegister2()
	{
		return controlRegister2;
	}
	public void setControlRegister2(ControlRegister2 controlRegister2)
	{
		this.controlRegister2 = controlRegister2;
	}

	public NesController getPortA()
	{
		return portA;
	}

	public void setPortA(NesController portA)
	{
		this.portA = portA;
	}

	public NesController getPortB()
	{
		return portB;
	}

	public void setPortB(NesController portB)
	{
		this.portB = portB;
	}
}
