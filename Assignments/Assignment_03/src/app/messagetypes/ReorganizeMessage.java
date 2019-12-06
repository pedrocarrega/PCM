package app.messagetypes;

import library.Actor;
import library.Message;

public class ReorganizeMessage extends Message{
	
	private int side;

	public ReorganizeMessage(int num, Actor actor, int side) {
		super(num, actor);
		this.side = side;
	}

	public int getSide() {
		return side;
	}

}
