package com.cardgame.screenapi;

public interface Screen {
	
	
	
	public boolean isShared();

	public void setAsShared();
	public void setAsPersonal();

	public String getName();

	public void setName(String name);
	//public void onEventReceived(Event e);
	//deprecated: methods for opening/closing

}
