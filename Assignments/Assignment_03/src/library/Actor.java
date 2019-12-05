package library;
import java.util.concurrent.LinkedBlockingQueue;

import app.ActorNode;
import app.messagetypes.AddMessage;
import app.messagetypes.AddResponseMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.ContainsResponseMessage;
import app.messagetypes.RemoveMessage;
import app.messagetypes.RemoveResponseMessage;
import library.Message;

public abstract class Actor extends Thread implements Runnable{

	private LinkedBlockingQueue<Message> mailbox = new LinkedBlockingQueue<>();
	private Actor left;
	private Actor right;
	private int value;
	private boolean run;


	public Actor(Message m) {
		left = null;
		right = null;
		value = m.getNumber();
		run = true;
	}

	public void receiveMessage(Message m) {
		mailbox.add(m);
	}

	protected abstract void processMessage(Message m);

	protected void add(AddMessage m) {
		if(this.value == m.getNumber()) {
			AddResponseMessage reply = new AddResponseMessage(0, this);
			m.getSender().receiveMessage(reply);
		}else if(value > m.getNumber()) {
			if(left == null) {
				left = new ActorNode(m);
				left.start();
				AddResponseMessage reply = new AddResponseMessage(1, this);
				m.getSender().receiveMessage(reply);
			}else {
				left.receiveMessage(m);
			}
		}else{
			if(right == null) {
				right = new ActorNode(m);
				right.start();
				AddResponseMessage reply = new AddResponseMessage(1, this);
				m.getSender().receiveMessage(reply);
			}else {
				right.receiveMessage(m);
			}
		}
	}

	protected void contains(ContainsMessage m) {
		if(value == m.getNumber()) {
			ContainsResponseMessage reply = new ContainsResponseMessage(1, this);
			m.getSender().receiveMessage(reply);
		}else if(value > m.getNumber()) {
			if(left != null) {
				left.receiveMessage(m);
			}else {
				ContainsResponseMessage reply = new ContainsResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}else{
			if(right != null) {
				right.receiveMessage(m);
			}else {
				ContainsResponseMessage reply = new ContainsResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}
	}

	protected void remove(RemoveMessage m) {
		if(value == m.getNumber()) {
			this.run = false;
		}else if(value > m.getNumber()) {
			if(left != null) {
				left.receiveMessage(m);
			}else {
				RemoveResponseMessage reply = new RemoveResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}else{
			if(right != null) {
				right.receiveMessage(m);
			}else {
				RemoveResponseMessage reply = new RemoveResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}
	}

	public int getValue() {
		return this.value;
	}

	public void run() {
		while(run) {
			int sleepCounter = 1;
			if(!mailbox.isEmpty()) {
				Message current = mailbox.remove();
				processMessage(current);
				sleepCounter = 1;
			}else {
				sleepCounter *= 2;
				if(sleepCounter > 10000)
					sleepCounter = 10000;
				try {
					Thread.sleep(sleepCounter);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
