package app.messagetypes;

import library.Actor;
import library.Message;

public class StartMessage extends Message{

	public StartMessage(int num, Actor actor) {
		super(num, actor);
	}

}
