import java.util.concurrent.LinkedBlockingQueue;

public abstract class Actor implements Runnable{

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

	private void add(Message m) {
		if(left == null && value > m.getNumber())
			left = new ActorNode(m);
		else if(right == null && value < m.getNumber())
			right = new ActorNode(m);
		else
			foward(m);
	}

	public void foward(Message m) {
		if(left.getValue() > m.getNumber())
			left.receiveMessage(m);
		else
			right.receiveMessage(m);
	}
	
	protected void die() {
		if(left != null)
			left.receiveMessage(new KillMessage());
		if(right != null)
			right.receiveMessage(new KillMessage());
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
