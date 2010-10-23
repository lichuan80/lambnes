package com.lambelly.lambnes.platform.ppu;

public class NesPpu implements PictureProcessingUnit
{
	private PPUControlRegister1 ppuControlRegister1 = new PPUControlRegister1();
	private PPUControlRegister2 ppuControlRegister2 = new PPUControlRegister2();
	private PPUStatusRegister ppuStatusRegister = new PPUStatusRegister();
	private PPUSpriteDMARegister ppuSpriteDMARegister = new PPUSpriteDMARegister();
	
	public void doRegisterReadsWrites()
	{
		this.getPpuControlRegister1().read();
		this.getPpuControlRegister2().read();
		this.getPpuStatusRegister().write();
		this.getPpuSpriteDMARegister().read();
	}

	public PPUControlRegister1 getPpuControlRegister1()
	{
		return ppuControlRegister1;
	}

	public void setPpuControlRegister1(PPUControlRegister1 ppuControlRegister1)
	{
		this.ppuControlRegister1 = ppuControlRegister1;
	}

	public PPUControlRegister2 getPpuControlRegister2()
	{
		return ppuControlRegister2;
	}

	public void setPpuControlRegister2(PPUControlRegister2 ppuControlRegister2)
	{
		this.ppuControlRegister2 = ppuControlRegister2;
	}

	public PPUStatusRegister getPpuStatusRegister()
	{
		return ppuStatusRegister;
	}

	public void setPpuStatusRegister(PPUStatusRegister ppuStatusRegister)
	{
		this.ppuStatusRegister = ppuStatusRegister;
	}

	public PPUSpriteDMARegister getPpuSpriteDMARegister()
	{
		return ppuSpriteDMARegister;
	}

	public void setPpuSpriteDMARegister(PPUSpriteDMARegister ppuSpriteDMARegister)
	{
		this.ppuSpriteDMARegister = ppuSpriteDMARegister;
	}
}
