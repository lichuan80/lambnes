package com.lambelly.lambnes.platform.interrupts;

public class InterruptRequest
{
	public static final int interruptTypeNMI = 0;
	public static final int interruptTypeIRQ = 1;
	public static final int interruptTypeReset = 2;
	private int interruptType = 0;
	
	public InterruptRequest(int requestType)
	{
		this.setInterruptType(requestType);
	}
	
	public int getInterruptType()
	{
		return interruptType;
	}
	public void setInterruptType(int interruptType)
	{
		this.interruptType = interruptType;
	}
}
