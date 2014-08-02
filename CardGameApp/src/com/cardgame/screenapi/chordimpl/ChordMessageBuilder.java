package com.cardgame.screenapi.chordimpl;

import android.util.Log;

import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.MessageBuilder;

public class ChordMessageBuilder implements MessageBuilder {

	@Override
	public Message buildMessage(Event e) {
		// TODO pass whether is API event
		Log.e("return new chord message",""+e.getPayload() + "" + e.getSource());
		return new ChordMessage(e.getPayload(),e.getRecipient(),e.getSource(),e.getType(),e.isAPIEvent());
	}

	@Override
	public Event unpackEvent(Message m) {
		// TODO Auto-generated method stub
		Log.i("Is received message an API EVENT:",""+m.isAPIEvent());
		return new Event(m.getSource(),m.getRecipient(),m.getType(),m.getContents(),m.isAPIEvent());
	}
	

}
