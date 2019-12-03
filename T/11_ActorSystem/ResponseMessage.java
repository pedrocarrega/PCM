package T10;

public class ResponseMessage extends Message{
	
	private int calculatedNumber;
	
	public ResponseMessage(int number) {
		this.calculatedNumber = number;
	}
	
	public int getNumber() { return this.calculatedNumber; }

}
