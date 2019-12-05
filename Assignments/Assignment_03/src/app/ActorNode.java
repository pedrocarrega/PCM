package app;

import library.Actor;
import library.Message;
import library.messagetypes.AddMessage;

public class ActorNode extends Actor {

	public ActorNode(Message m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processMessage(Message m) {
		if(m instanceof AddMessage) {
			AddMessage m2 = (AddMessage) m;
			add(m2);
		}
		
	}

}
