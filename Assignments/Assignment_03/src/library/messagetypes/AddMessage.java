package library.messagetypes;

import library.Actor;
import library.Message;

public class AddMessage extends Message{

	public AddMessage(int num, Actor actor) {
		super(num, actor);
	}

}
