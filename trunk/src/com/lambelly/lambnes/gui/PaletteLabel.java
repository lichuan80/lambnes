package com.lambelly.lambnes.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.lambelly.lambnes.platform.MasterColor;

public class PaletteLabel extends JLabel
{
	private MasterColor backgroundColor = null;
	
	public PaletteLabel(MasterColor color)
	{
		this.setBackgroundColor(color);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		//this.setText(Integer.toString(spritePaletteColorNumber)); 
		this.setOpaque(true);
		this.setBorder(LineBorder.createGrayLineBorder());
		this.setToolTipText("r" + this.getBackgroundColor().getRed() + "b" + this.getBackgroundColor().getBlue() + "g" + this.getBackgroundColor().getGreen());
		this.setBackground(new Color(this.getBackgroundColor().getColorInt()));
	}
	
	public void refreshBackground(MasterColor color)
	{
		this.setBackgroundColor(color);
		this.setToolTipText("r" + this.getBackgroundColor().getRed() + "b" + this.getBackgroundColor().getBlue() + "g" + this.getBackgroundColor().getGreen());
		this.setBackground(new Color(this.getBackgroundColor().getColorInt()));
	}

	public MasterColor getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(MasterColor color)
	{
		backgroundColor = color;
	}
}
