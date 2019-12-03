package T10;

public class EmployeeActor extends Actor{

	protected EmployeeActor(Actor supervisor) {
		super(supervisor);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processMessage(Message m) {
		if (m instanceof RequestMessage) {
			RequestMessage m2 = (RequestMessage) m;
			int result = 2 * m2.getNumber();
			ResponseMessage reply = new ResponseMessage(result);
			m.getSender().receiveMessage(reply);
		}
	}
}
