package T10;

public class RequestMessage extends Message{
	
	private int calculatedNumber;
	
	public RequestMessage(int number) { this.calculatedNumber = number; }
	
	public int getNumber() { return this.calculatedNumber; }

}
