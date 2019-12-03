

public class TreeNode {
	
	private Actor left;
	private Actor right;
	private int value;
	
	public TreeNode(Message m) {
		left = null;
		right = null;
		value = m.getNumber();
	}
	
	public void handleMessage(Message m) {}
	
	public void foward(Message mes) {}
	
	

}
