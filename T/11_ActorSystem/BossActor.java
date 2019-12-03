package T10;

import java.util.stream.IntStream;

public class BossActor extends Actor{

	private static final int NUMBER_OF_EMPLOYESS = 4;
	
	private int value = 0;

	private int onGoingNumber;

	protected BossActor(Actor supervisor) {
		super(supervisor);
	}

	public BossActor() {
		super(null);
	}

	@Override
	protected void processMessage(Message m) {
		if(m instanceof StartMessage) {
			IntStream.rangeClosed(0, NUMBER_OF_EMPLOYESS).forEach((int i) -> {
				EmployeeActor e = new EmployeeActor(this);
				registerChild(e);
				e.receiveMessage(new RequestMessage(i));
				e.start();
				onGoingNumber = i+1;
			});
		}
		if(m instanceof ResponseMessage) {
			ResponseMessage m2 = (ResponseMessage) m;
			value += m2.getNumber();
			if(value > 15) {
				System.out.println("More than 5");
				this.die();
			}else {
				RequestMessage request = new RequestMessage(onGoingNumber++);
				m.getSender().recieveMessage(req);
			}
		}
		
	}
}
