package com.cardgame.screenapi;

public class Screen {
	private String name;
	private boolean isShared;

	public boolean isShared() {
		return isShared;
	}

	public void setAsShared()
	{
		this.isShared=true;
	}
	public void setAsPersonal()
	{
		this.isShared=false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
