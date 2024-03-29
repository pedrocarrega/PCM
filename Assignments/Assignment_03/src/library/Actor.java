package library;
import java.util.concurrent.LinkedBlockingQueue;

import app.ActorNode;
import app.messagetypes.AddMessage;
import app.messagetypes.AddResponseMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.ContainsResponseMessage;
import app.messagetypes.RemoveMessage;
import app.messagetypes.RemoveResponseMessage;
import app.messagetypes.ReorganizeMessage;
import library.Message;

public abstract class Actor extends Thread implements Runnable{

	private LinkedBlockingQueue<Message> mailbox = new LinkedBlockingQueue<>();
	private Actor left;
	private Actor right;
	private int value;
	private boolean run;


	public Actor(int i) {
		left = null;
		right = null;
		value = i;
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
				left = new ActorNode(m.getNumber());
				left.start();
				AddResponseMessage reply = new AddResponseMessage(1, this);
				m.getSender().receiveMessage(reply);
			}else {
				left.receiveMessage(m);
			}
		}else{
			if(right == null) {
				right = new ActorNode(m.getNumber());
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
			if(left != null && right != null) {
				ReorganizeMessage organize = new ReorganizeMessage(0, m.getSupervisor(), m.getSide());
				right.receiveMessage(organize);
			}else if(left != null) {
				ReorganizeMessage organize = new ReorganizeMessage(1, left, 0);
				m.getSender().receiveMessage(organize);
			}else if(right != null) {
				ReorganizeMessage organize = new ReorganizeMessage(1, right, 1);
				m.getSender().receiveMessage(organize);
			}
			this.run = false;
			this.right = null;
			this.left = null;
			while(!mailbox.isEmpty()) {
				mailbox.remove();
			}
			RemoveResponseMessage reply = new RemoveResponseMessage(1, this);
			m.getSender().receiveMessage(reply);
		}else if(value > m.getNumber()) {
			if(left != null) {
				left.receiveMessage(new RemoveMessage(m.getNumber(), m.getSender(), this, 0));
			}else {
				RemoveResponseMessage reply = new RemoveResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}else{
			if(right != null) {
				right.receiveMessage(new RemoveMessage(m.getNumber(), m.getSender(), this, 1));
			}else {
				RemoveResponseMessage reply = new RemoveResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}
	}

	public void setLeft(Actor left) {
		this.left = left;
	}

	public void setRight(Actor right) {
		this.right = right;
	}

	public Actor getLeft() {
		return left;
	}

	public Actor getRight() {
		return right;
	}

	public int getValue() {
		return this.value;
	}

	public void run() {
		int sleepCounter = 10;
		while(run || !mailbox.isEmpty()) {
			if(!mailbox.isEmpty()) {
				Message current = mailbox.remove();
				processMessage(current);
				sleepCounter = 10;
			}else {
				sleepCounter *= 2;
				if(sleepCounter > 10000) {
					sleepCounter = 10000;
					//run = false;
				}
				try {
					Thread.sleep(sleepCounter);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
