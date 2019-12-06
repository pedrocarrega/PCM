package app;

import app.messagetypes.AddMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.RemoveMessage;
import app.messagetypes.ReorganizeMessage;
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
		}else if(m instanceof ReorganizeMessage) {
			ReorganizeMessage m2 = (ReorganizeMessage) m;
			if(m2.getNumber() == 1) {
				if(m2.getSide() == 1) {
					Actor right = this.getRight();
					right = m2.getSender();
				}else {
					Actor left = this.getLeft();
					left = m2.getSender();
				}
			}else {
				if(this.getLeft() == null) {
					ReorganizeMessage found = new ReorganizeMessage(1, this, m2.getSide());
					m2.getSender().receiveMessage(found);
				}else {
					ReorganizeMessage foward = new ReorganizeMessage(0, m2.getSender(), m2.getSide());
					this.getLeft().receiveMessage(foward);
				}
			}
		}
	}
}