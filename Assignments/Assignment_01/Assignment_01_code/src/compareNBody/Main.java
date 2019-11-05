package compareNBody;


public class Main {
	public static void main(String[] args) {
		NBodySystem bodies = new NBodySystem(NBodySystem.DEFAULT_SIZE, 1L);
		NBodySystemSeq body = new NBodySystemSeq(NBodySystemSeq.DEFAULT_SIZE, 1L);
		//NBodySystemSeq body2 = new NBodySystemSeq(NBodySystemSeq.DEFAULT_SIZE, 1L);		
		//System.out.printf("%.9f\n", bodies.energy());
		for (int i = 0; i < NBodySystemSeq.DEFAULT_ITERATIONS; ++i) {
			bodies.advance(0.01);
			body.advance(0.01);
			//body2.advance(0.01);
			System.out.println("IT'S THE SAME RESULT??? " + compare(bodies.get(), body.get()));
			//System.out.printf("%.9f\n", bodies.energy());
		}
		//System.out.println("Time: " + bodies.time);
	}
	
	private static int compare(NBody[] body1, NBody[] body2) {
		
		int result = 0;
		
		if(body1.length != body2.length)
			return -1;
		
		for(int i = 0; i < body1.length; i++) {
			if(!body1[i].equals(body2[i]))
				result++;
		}

		return result;
	}
}
