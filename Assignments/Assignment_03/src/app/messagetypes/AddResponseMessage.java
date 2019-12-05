package app.messagetypes;

import library.Actor;
import library.Message;

public class AddResponseMessage extends Message{

	public AddResponseMessage(int reply, Actor actor) {
		super(reply, actor);
	}

}
