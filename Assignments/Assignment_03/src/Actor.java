

public class Actor {

	private Actor left;
	private Actor right;
	private int value;

	public Actor(Message m) {
		left = null;
		right = null;
		value = m.getNumber();
	}

	public void handleMessage(Message m) {
		if(left == null && value > m.getNumber())
			left = new Actor(m);
		else if(right == null && value < m.getNumber())
			right = new Actor(m);
		else
			foward(m);
		
	}

	public void foward(Message m) {
		if(left.getValue() > m.getNumber())
			left.handleMessage(m);
		else
			right.handleMessage(m);
	}

	public int getValue() {
		return this.value;
	}
}
