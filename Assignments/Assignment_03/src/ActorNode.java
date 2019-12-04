
public class ActorNode extends Actor {

	public ActorNode(Message m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	private void add(AddMessage m) {
		if(left == null && value > m.getNumber()) {
			left = new ActorNode(m);
			ResponseMessage reply = new ResponseMessage(1, m.getSender());
			m.getSender().receiveMessage(reply);
		}
			
		else if(right == null && value < m.getNumber()) {
			right = new ActorNode(m);
			ResponseMessage reply = new ResponseMessage(1, m.getSender());
			m.getSender().receiveMessage(reply);
		}
			
		else
			foward(m);
	}

	public void foward(Message m) {
		if(left.getValue() > m.getNumber())
			left.receiveMessage(m);
		else
			right.receiveMessage(m);
	}

	@Override
	protected void processMessage(Message m) {
		if(m instanceof AddMessage) {
			AddMessage m2 = (AddMessage) m;
			add(m2);
		}
		
	}

}
