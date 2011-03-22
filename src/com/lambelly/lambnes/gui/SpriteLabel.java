package com.lambelly.lambnes.gui;

import javax.swing.*;
import javax.swing.border.*;

public class SpriteLabel extends JLabel
{
	private int spriteNumber = 0;
	
	public SpriteLabel(int spriteNumber)
	{
		this.setSpriteNumber(spriteNumber);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setIcon(new SpriteIcon(this.getSpriteNumber()));
		this.setBorder(LineBorder.createGrayLineBorder());
	}

	public int getSpriteNumber()
	{
		return spriteNumber;
	}

	public void setSpriteNumber(int spriteNumber)
	{
		this.spriteNumber = spriteNumber;
	}
}
