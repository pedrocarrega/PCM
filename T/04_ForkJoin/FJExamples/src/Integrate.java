import java.util.List;

public class Integrate {
	
	static final double errorTolerance = 1.0e-4;
	static double start = 0;
    static double end = 100;
    
	// the function to integrate
    static double computeFunction(double x)  {
        return (x * x + 1.0) * x;
    }
	public static void main(String[] args) throws Exception {

		int ITERATIONS = (int) ((end - start) / errorTolerance);
		
		List<Double> parts = SuperDuperParallelLibrary.doInParallel(ITERATIONS, (startIndex, endIndex) -> {
			double accumulator = 0.0;
			for (double d = start + (startIndex/ITERATIONS) ; d<=start + (endIndex/ITERATIONS); d += errorTolerance) {
				double h = errorTolerance;
				double b = computeFunction(d);
				double B = computeFunction(d + errorTolerance);
				accumulator += (b + B) * h / 2;
			}
			return accumulator;
			
		});

		double integrate = 0.0;
		for (Double d : parts) {
			integrate += d;
		}
		
		System.out.println("Integrate: " + integrate);
		
		/*
		for (double d=Integrate.start; d<=end; d += errorTolerance) {
			double h = errorTolerance;
			double b = computeFunction(d);
			double B = computeFunction(d + errorTolerance);
			integrate += (b + B) * h / 2;
		}
		
		System.out.println("Integrate: " + integrate);
		// Integrate: 2.500500000140081E7
		
		*/
	}
}
