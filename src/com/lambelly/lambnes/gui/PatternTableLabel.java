package com.lambelly.lambnes.gui;

import javax.swing.*;
import javax.swing.border.*;

public class PatternTableLabel extends JLabel
{
	private int spriteNumber = 0;
	
	public PatternTableLabel(int spriteNumber, PatternTableIcon icon)
	{
		this.setSpriteNumber(spriteNumber);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setIcon(icon);
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
