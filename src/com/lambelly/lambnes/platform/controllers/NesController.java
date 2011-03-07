package com.lambelly.lambnes.platform.controllers;

public interface NesController
{
	public boolean isA();
	public void setA(boolean a);
	public boolean isB();
	public void setB(boolean b);
	public boolean isUp();
	public void setUp(boolean up);
	public boolean isDown();
	public void setDown(boolean down);
	public boolean isLeft();
	public void setLeft(boolean left);
	public boolean isRight();
	public void setRight(boolean right);
	public boolean isSelect();
	public void setSelect(boolean select);
	public boolean isStart();
	public void setStart(boolean start);
	public int read(int bit);
}
