package app;

import java.util.Random;

import app.messagetypes.AddMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.RemoveMessage;

public class Client {

	public static void main(String[] args) {
		
		RootActor root = new RootActor();
		root.start();
		
	}
	
	private void addNode(int number, RootActor root) {
		root.receiveMessage(new AddMessage(number, root));
	}
	
	private void containsNode(int number, RootActor root) {
		root.receiveMessage(new ContainsMessage(number, root));
	}
	
	private void removeNode(int number, RootActor root) {
		root.receiveMessage(new RemoveMessage(number, root));
	}

}
