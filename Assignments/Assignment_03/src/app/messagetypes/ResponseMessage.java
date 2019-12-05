package app.messagetypes;

import library.Actor;
import library.Message;

public class ResponseMessage extends Message{

	public ResponseMessage(int reply, Actor actor) {
		super(reply, actor);
	}

}
