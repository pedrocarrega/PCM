package app;


import java.util.Random;

import app.messagetypes.StartMessage;
import library.Actor;
import library.Message;

public class RootActor extends Actor {
	
	public RootActor() {
		super(null);
	}

	@Override
	protected void processMessage(Message m) {
		if(m instanceof StartMessage) {
			
		}
		
	}
}
