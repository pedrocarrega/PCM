package app;

import app.messagetypes.AddMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.RemoveMessage;
import library.Actor;
import library.Message;

public class ActorNode extends Actor {


	public ActorNode(int i) {
		super(i);
	}

	@Override
	protected void processMessage(Message m) {
		if(m instanceof AddMessage) {
			AddMessage m2 = (AddMessage) m;
			add(m2);
		}else if(m instanceof ContainsMessage) {
			ContainsMessage m2 = (ContainsMessage) m;
			contains(m2);
		}else if(m instanceof RemoveMessage) {
			RemoveMessage m2 = (RemoveMessage) m;
			remove(m2);
		}/*else if(m instanceof ReorganizeMessage) {
			ReorganizeMessage m2 = (ReorganizeMessage) m;
			reorganize(m2);
		}*/
		
	}

}
