public class Client {

	public static void main(String[] args) {
		
		RootActor root = new RootActor();
		root.recieveMessage(new StartMessage());
		root.start();
		
	}

}
