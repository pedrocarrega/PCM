public class Client {

	public static void main(String[] args) {
		
		RootActor root = new RootActor();
		root.receiveMessage(new StartMessage());
		root.start();
		
	}

}
