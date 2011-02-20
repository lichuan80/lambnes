package com.lambelly.lambnes.gui;

import javax.swing.*;
import javax.swing.border.*;

public class BackgroundLabel extends JLabel
{
	private int tileNumber = 0;
	
	public BackgroundLabel(int tileNumber)
	{
		this.setTileNumber(tileNumber);
		//this.setHorizontalTextPosition(JLabel.CENTER);
		//this.setVerticalTextPosition(JLabel.BOTTOM);
		//this.setText(Integer.toString(tileNumber));
		this.setIcon(new BackgroundIcon(this.getTileNumber()));
		this.setBorder(LineBorder.createGrayLineBorder());
	}

	public int getTileNumber()
	{
		return tileNumber;
	}

	public void setTileNumber(int tileNumber)
	{
		this.tileNumber = tileNumber;
	}
}
