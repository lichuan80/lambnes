package com.lambelly.lambnes.platform.controllers;

import org.apache.log4j.Logger;
import com.lambelly.lambnes.util.BitUtils;

public class NesJoypad implements NesController 
{
	public static final int CONTROLLER_1_SIGNATURE = 1;
	public static final int CONTROLLER_2_SIGNATURE = 2;
	
	private boolean a = false;
	private boolean b = false;
	private boolean start = false;
	private boolean select = false;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	private int controllerNumber = 0;
	private Integer rawControlByte = 0;
	private Logger logger = Logger.getLogger(NesJoypad.class);
	
	public NesJoypad(int controllerNumber) 
	{
		this.setControllerNumber(controllerNumber);
	}
	
	public int read(int bit)
	{
		/*
		 *  1 = A          9 = Ignored   17 = +--+
      	 *  2 = B         10 = Ignored   18 =    +-- Signature
         *  3 = SELECT    11 = Ignored   19 =    |
         *  4 = START     12 = Ignored   20 = +--+
         *  5 = UP        13 = Ignored   21 = 0
         *  6 = DOWN      14 = Ignored   22 = 0
         *  7 = LEFT      15 = Ignored   23 = 0
         *  8 = RIGHT     16 = Ignored   24 = 0
		 */

		if (logger.isDebugEnabled())
		{
			logger.debug("read " + bit);
			logger.debug("controller number: " + this.getControllerNumber());
		}
		
		if (bit == 0)
		{
			return this.isA()?1:0;
		}
		else if (bit == 1)
		{
			return this.isB()?1:0;
		}
		else if (bit == 2)
		{
			return this.isSelect()?1:0;
		}
		else if (bit == 3)
		{
			return this.isStart()?1:0;
		}
		else if (bit == 4)
		{
			return this.isUp()?1:0;
		}
		else if (bit == 5)
		{
			return this.isDown()?1:0;
		}
		else if (bit == 6)
		{
			return this.isLeft()?1:0;
		}
		else if (bit == 7)
		{
			return this.isRight()?1:0;
		}
		else if (bit >= 8 && bit <= 15)
		{
			return 0;
		}
		else if (bit >= 16 && bit <= 17)
		{
			return 0;
		}
		else if (bit == 18)
		{
			if (this.getControllerNumber() == 2)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else if (bit == 19)
		{
			if (this.getControllerNumber() == 1)
			{
				return 1;
			}
			else 
			{
				return 0;
			}
		}
		else if (bit >= 20 && bit <= 23)
		{
			return 0;
		}
		else
		{
			throw new IllegalStateException("tried to read bit " + bit + " from controller");
		}
		
	}

	public boolean isA()
	{
		return a;
	}
	public void setA(boolean a)
	{
		this.a = a;
	}
	public boolean isB()
	{
		return b;
	}
	public void setB(boolean b)
	{
		this.b = b;
	}
	public boolean isStart()
	{
		return start;
	}
	public void setStart(boolean start)
	{
		this.start = start;
	}
	public boolean isSelect()
	{
		return select;
	}
	public void setSelect(boolean select)
	{
		this.select = select;
	}
	public boolean isUp()
	{
		return up;
	}
	public void setUp(boolean up)
	{
		this.up = up;
	}
	public boolean isDown()
	{
		return down;
	}
	public void setDown(boolean down)
	{
		this.down = down;
	}
	public boolean isLeft()
	{
		return left;
	}
	public void setLeft(boolean left)
	{
		this.left = left;
	}
	public boolean isRight()
	{
		return right;
	}
	public void setRight(boolean right)
	{
		this.right = right;
	}
	public int getControllerNumber()
	{
		return controllerNumber;
	}
	public void setControllerNumber(int controllerNumber)
	{
		this.controllerNumber = controllerNumber;
	}

	public Integer getRawControlByte()
	{
		return rawControlByte;
	}

	public void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	
}
