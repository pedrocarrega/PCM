package T10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Actor implements Runnable{

	private ConcurrentLinkedQueue<Message> mailbox = new ConcurrentLinkedQueue<Message>();
	private Actor supervisor;
	private List<Actor> children = new ArrayList<>();
	private boolean run = true;

	protected Actor(Actor supervisor) {
		this.supervisor = supervisor;
	}

	public void receiveMessage(Message m) {
		mailbox.add(m);
	}

	public Actor getSupervisor() { return this.supervisor; }

	protected abstract void processMessage(Message m);

	public void registerChild(Actor c) {
		children.add(c);
	}
	
	private void processAnyMessage(Message current) {
		if(current instanceof SystemMessage) {
			processSystemMessage(current);
		}else {
			processMessage(current);
		}
	}
	
	private void processSystemMessage(Message current) {
		if(current instanceof KillMessage) {
			die();
		}
	}
	
	protected void die() {
		for(Actor child : children) {
			child.receiveMessage(new KillMessage());
		}
		this.run = false;
	}

	public void run() {
		while(run) {
			int sleepCounter = 1;
			if(!mailbox.isEmpty()) {
				Message current = mailbox.remove();
				processAnyMessage(current);
				sleepCounter  = 1;
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
