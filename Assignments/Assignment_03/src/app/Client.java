package app;

import java.util.Random;

import app.messagetypes.AddMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.RemoveMessage;

public class Client {

	private static final int NUM_OPERATIONS = 1000;
	private static final int ACTOR_VAL_RANGE = 15;

	public static void main(String[] args) {
		
		Random r = new Random();
		
		RootActor root = new RootActor(0);
		root.start();
		
		
		addNode(5, root);
		addNode(1, root);
		addNode(3, root);
		
		for(int i = 0; i < NUM_OPERATIONS; i++) {
            int decision = r.nextInt(3);

            if(decision == 0) {
                addNode(r.nextInt(ACTOR_VAL_RANGE), root);
            }else if(decision == 1) {
                containsNode(r.nextInt(ACTOR_VAL_RANGE), root);
            }else {
                removeNode(r.nextInt(ACTOR_VAL_RANGE)+1, root);
            }
        }
		
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
