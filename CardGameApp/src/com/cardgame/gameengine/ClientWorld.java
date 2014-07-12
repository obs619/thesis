package com.cardgame.gameengine;

import com.cardgame.gameengine.transport.CardGameEvent;

public class ClientWorld extends World{
	
	/*Client actions below
	 * 
	 * 
	 */
	public void playCard(String cardName)
	{
		
		triggerEvent(CardGameEvent.CARD_PLAYED,cardName);//this will already be applied on this device thanks to the contents of Screen.triggerEvent()
		
	}

}
