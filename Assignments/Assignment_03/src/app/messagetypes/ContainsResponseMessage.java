package app.messagetypes;

import library.Actor;
import library.Message;

public class ContainsResponseMessage extends Message{

	public ContainsResponseMessage(int num, Actor actor) {
		super(num, actor);
	}

}
