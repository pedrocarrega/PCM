package library;
import java.util.concurrent.LinkedBlockingQueue;

import app.ActorNode;
import app.messagetypes.AddMessage;
import app.messagetypes.ContainsMessage;
import app.messagetypes.ResponseMessage;
import library.Message;
import library.messagetypes.KillMessage;
import library.messagetypes.SystemMessage;

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

	private void processAnyMessage(Message current) {
		if(current instanceof SystemMessage)
			processSystemMessage(current);
		else
			processMessage(current);
	}

	private void processSystemMessage(Message current) {
		if(current instanceof KillMessage)
			die();
	}

	protected void add(AddMessage m) {
		if(this.value == m.getNumber()) {
			//lancar erro?
		}else if(value > m.getNumber()) {
			if(left == null) {
				left = new ActorNode(m);
				left.start();
				ResponseMessage reply = new ResponseMessage(1, this);
				m.getSender().receiveMessage(reply);
			}else {
				left.receiveMessage(m);
			}
		}else{
			if(right == null) {
				right = new ActorNode(m);
				right.start();
				ResponseMessage reply = new ResponseMessage(1, this);
				m.getSender().receiveMessage(reply);
			}else {
				right.receiveMessage(m);
			}
		}
	}

	protected void contains(ContainsMessage m) {
		if(value == m.getNumber()) {
			ResponseMessage reply = new ResponseMessage(1, this);
			m.getSender().receiveMessage(reply);
		}else if(value > m.getNumber()) {
			if(left != null) {
				left.receiveMessage(m);
			}else {
				ResponseMessage reply = new ResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}else{
			if(right != null) {
				right.receiveMessage(m);
			}else {
				ResponseMessage reply = new ResponseMessage(0, this);
				m.getSender().receiveMessage(reply);
			}
		}
	}

	protected void die() {
		if(left != null)
			left.receiveMessage(new KillMessage(value, this));
		if(right != null)
			right.receiveMessage(new KillMessage(value, this));
		this.run = false;
	}

	public int getValue() {
		return this.value;
	}

	public void run() {
		while(run) {
			int sleepCounter = 1;
			if(!mailbox.isEmpty()) {
				Message current = mailbox.remove();
				processAnyMessage(current);
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
