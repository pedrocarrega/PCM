import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class Heat {

	public static final int NX = 2048;
	public static final int NY = 2048;
	public static final int ITERATIONS = 50;

	public static void main(String[] args) {
		double[][] oldm = new double[NX][NY];
		double[][] newm = new double[NX][NY];
		final int NTHREADS = Runtime.getRuntime().availableProcessors();

		// Everything else is 0
		oldm[NX/2][NY/2] = 100000;

		List<Runnable> tasks = new ArrayList<Runnable>();

		for(int i = 0; i <= NTHREADS; i++) {
			tasks.add(new Runnable() {
				@Override
				public void run() {
					
				}
			});
		}

		newm = startTasks(tasks);



		//for (int timestep = 0; timestep <= ITERATIONS; timestep++) {

			for (int i=1; i< NX-1; i++) {
				for (int j=1; j< NY-1; j++) {
					double current = newm[i][j];
					newm[i][j] = (current + oldm[i-1][j] + oldm[i+1][j] + oldm[i][j-1] + oldm[i][j+1]) / 5; 
				}
			}

			// Swap matrices
			double[][] tmp = newm;
			newm = oldm;
			oldm = tmp;
		//}

		System.out.println(newm[NX/2 - 10][NY/2 - 10]); // ~3.77

	}

	private static double[][] startTasks(List<Runnable> tasks) {
		final Phaser phaser = new Phaser(1) {
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase > ITERATIONS || registeredParties == 0;
			}
		};
		for(final Runnable task : tasks) {
			phaser.register();
			new Thread() {
				public void run() {
					do {
						task.run();
						phaser.arriveAndAwaitAdvance();
					} while(!phaser.isTerminated());
				}
			}.start();
		}
		phaser.arriveAndDeregister();

		return null;
	}
}