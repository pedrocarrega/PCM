package app.messagetypes;

import library.Actor;
import library.Message;

public class RemoveMessage extends Message{
	
	private Actor supervisor;
	private int side;

	public RemoveMessage(int num, Actor actor, Actor supervisor, int side) {
		super(num, actor);
		this.supervisor = supervisor;
		this.side = side;
	}

	public Actor getSupervisor() {
		return supervisor;
	}

	public int getSide() {
		return side;
	}

}
