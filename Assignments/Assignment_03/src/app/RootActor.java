package app;


import app.messagetypes.AddMessage;
import app.messagetypes.AddResponseMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.ContainsResponseMessage;
import app.messagetypes.RemoveMessage;
import app.messagetypes.RemoveResponseMessage;
import library.Actor;
import library.Message;

public class RootActor extends Actor {
	
	public RootActor() {
		super(null);
	}

	@Override
	protected void processMessage(Message m) {
		if(m instanceof AddMessage) {
			add((AddMessage) m);
		}else if(m instanceof ContainsMessage) {
			contains((ContainsMessage) m);
		}else if(m instanceof RemoveMessage) {
			remove((RemoveMessage) m);
		}else if(m instanceof AddResponseMessage) {
			if(m.getNumber() == 1) {
				System.out.println("Add successful");
			}else {
				System.out.println("Add failed");
			}
		}else if(m instanceof ContainsResponseMessage) {
			if(m.getNumber() == 1) {
				System.out.println("Node exists");
			}else {
				System.out.println("Node doesnt exist");
			}
		}else if(m instanceof RemoveResponseMessage) {
			if(m.getNumber() == 1) {
				System.out.println("Remove successful");
			}else {
				System.out.println("Remove failed");
			}
		}
	}
}
