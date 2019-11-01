package nbody;

import java.util.Random;
import java.util.stream.IntStream;

public class NBodySystem {
	
	public static final int DEFAULT_ITERATIONS = 5;
	public static final int DEFAULT_SIZE = 50000;

	public static final int ADVANCE_THRESHOLD = 1000;
	public static final int APPLY_THRESHOLD = 100;

	static final double PI = 3.141592653589793;
	static final double SOLAR_MASS = 4 * PI * PI;
	
	public long time = 0;
	
	protected NBody[] bodies;
	//protected NBody[] currentBodies;
	
	//private final int NTHREADS = Runtime.getRuntime().availableProcessors();

	public NBodySystem(int n, long seed) {
		Random random = new Random(seed);
		bodies = new NBody[n];
		
		double px = 0.0;
		double py = 0.0;
		double pz = 0.0;
		for (int i = 0; i < n; i++) {
			bodies[i] = new NBody(random);
			px += bodies[i].vx * bodies[i].mass;
			py += bodies[i].vy * bodies[i].mass;
			pz += bodies[i].vz * bodies[i].mass;
		}
		bodies[0].offsetMomentum(px, py, pz);
	}
	
	public void advance(double dt) {
		
		long startTime = System.currentTimeMillis();
		
		/*
		for (int i = 0; i < bodies.length; ++i) {
			NBody iBody = bodies[i];
			for (int j = i + 1; j < bodies.length; ++j) {
				final NBody body = bodies[j];
				double dx = iBody.x - body.x;
				double dy = iBody.y - body.y;
				double dz = iBody.z - body.z;

				double dSquared = dx * dx + dy * dy + dz * dz;
				double distance = Math.sqrt(dSquared);
				double mag = dt / (dSquared * distance);

				iBody.vx -= dx * body.mass * mag;
				iBody.vy -= dy * body.mass * mag;
				iBody.vz -= dz * body.mass * mag;

				body.vx += dx * iBody.mass * mag;
				body.vy += dy * iBody.mass * mag;
				body.vz += dz * iBody.mass * mag;
			}
		}*/
		
		IntStream.range(0, bodies.length)
		 .parallel()
		 .forEach((int i) -> {
			 NBody iBody = bodies[i];
				for (int j = i + 1; j < bodies.length; ++j) {
					final NBody body = bodies[j];
					double dx = iBody.x - body.x;
					double dy = iBody.y - body.y;
					double dz = iBody.z - body.z;

					double dSquared = dx * dx + dy * dy + dz * dz;
					double distance = Math.sqrt(dSquared);
					double mag = dt / (dSquared * distance);

					iBody.vx -= dx * body.mass * mag;
					iBody.vy -= dy * body.mass * mag;
					iBody.vz -= dz * body.mass * mag;

					body.vx += dx * iBody.mass * mag;
					body.vy += dy * iBody.mass * mag;
					body.vz += dz * iBody.mass * mag;
				}
		 });
		
		//Seq is faster because of overhead
		for (NBody body : bodies) {
			body.x += dt * body.vx;
			body.y += dt * body.vy;
			body.z += dt * body.vz;
		}
		
		time += (System.currentTimeMillis() - startTime);
	}

	public double energy() {
		double dx, dy, dz, distance;
		double e = 0.0;

		for (int i = 0; i < bodies.length; ++i) {
			NBody iBody = bodies[i];
			e += 0.5 * iBody.mass * (iBody.vx * iBody.vx + iBody.vy * iBody.vy + iBody.vz * iBody.vz);

			for (int j = i + 1; j < bodies.length; ++j) {
				NBody jBody = bodies[j];
				dx = iBody.x - jBody.x;
				dy = iBody.y - jBody.y;
				dz = iBody.z - jBody.z;

				distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
				e -= (iBody.mass * jBody.mass) / distance;
			}
		}
		return e;
	}
	
	/*
	void startTasks(List<Runnable> tasks, final int iterations) {
		   final Phaser phaser = new Phaser() {
		     protected boolean onAdvance(int phase, int registeredParties) {
		    	 if(!(phase >= iterations || registeredParties == 0)) {
		    		 currentBodies = bodies.clone();		    		 		    		 
		    		 return false;
		    	 }else
		    		 return true;
		     }
		   };
		   phaser.register(); // register for the first barrier with parties = 1
		   int taskC = 0;
		   for (final Runnable task : tasks) {
			 final int count = taskC;
		     phaser.register();
		     new Thread() {
		       public void run() {
		         do {
		           int startIndex = count * bodies.length/NTHREADS;
		           int endIndex = (count + 1) * bodies.length/NTHREADS;
		           
		           for(int i = startIndex; i<endIndex; i++) {
		        	   
		           }
		           
		           phaser.arriveAndAwaitAdvance();
		         } while (!phaser.isTerminated());
		       }
		     }.start();
		     taskC++;
		   }
		   phaser.arriveAndDeregister(); // releases the first barrier
		 }*/
}