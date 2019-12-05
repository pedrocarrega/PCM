package library;


public abstract class Message {

	private int number;
	private Actor sender;
	
	public Message(int num, Actor actor) {
		number=num;
		sender = actor;
	}
	
	public int getNumber() {
		return this.number;
	}

	public Actor getSender() {
		return sender;
	}
	
}
