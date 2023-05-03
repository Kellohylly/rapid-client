package client.rapid.event.events.game;

import client.rapid.event.events.Event;

public class EventChat extends Event {
	private String message;
	
	public EventChat(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
