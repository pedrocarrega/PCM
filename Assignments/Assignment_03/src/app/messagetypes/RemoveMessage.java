package app.messagetypes;

import library.Actor;
import library.Message;

public class RemoveMessage extends Message{

	public RemoveMessage(int num, Actor actor) {
		super(num, actor);
	}

}
