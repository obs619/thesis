package com.cardgame.screenapi;

public class Screen {
	private String name;
	private boolean isShared;
	private EventManager eventManager;//could remove this field, make EventManager a singleton, call EventManager.getInstance()

	
	public Screen()
	{
		eventManager=EventManager.getInstance();
	}
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
	
	public void triggerEvent(Event e)
	{
		applyEvent(e);//apply event to yourself (if it affects you?)
		eventManager.sendEvent(e);
	}
	public void applyEvent(Event e)
	{
		eventManager.applyEvent(e);
	}
	public void open()
	{
		//make screen visible to other screens (auto-connect)
	}
	public void close()
	{
		//hide screen fromother screens
	}
}
