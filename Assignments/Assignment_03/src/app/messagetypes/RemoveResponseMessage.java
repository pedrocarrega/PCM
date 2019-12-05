package app.messagetypes;

import library.Actor;
import library.Message;

public class RemoveResponseMessage extends Message{

	public RemoveResponseMessage(int num, Actor actor) {
		super(num, actor);
	}

}
