package library.messagetypes;

import library.Actor;
import library.Message;

public class KillMessage extends Message {

	public KillMessage(int num, Actor actor) {
		super(num, actor);
	}

}
