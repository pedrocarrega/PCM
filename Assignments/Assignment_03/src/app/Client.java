package app;

import java.util.Random;

import app.messagetypes.AddMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.RemoveMessage;

public class Client {

	public static void main(String[] args) {
		
		RootActor root = new RootActor(0);
		addNode(5, root);
		addNode(1, root);
		addNode(3, root);
		containsNode(1, root);
		containsNode(2, root);
		removeNode(1, root);
		containsNode(3, root);
		containsNode(1, root);
		addNode(2, root);
		containsNode(2, root);
		root.start();
		
	}
	
	private static void addNode(int number, RootActor root) {
		root.receiveMessage(new AddMessage(number, root));
	}
	
	private static void containsNode(int number, RootActor root) {
		root.receiveMessage(new ContainsMessage(number, root));
	}
	
	private static void removeNode(int number, RootActor root) {
		root.receiveMessage(new RemoveMessage(number, root, root, -1));
	}

}
