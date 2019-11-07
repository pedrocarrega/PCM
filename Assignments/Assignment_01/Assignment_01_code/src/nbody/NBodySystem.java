package nbody;

import java.util.Random;
import java.util.stream.IntStream;

public class NBodySystem {

	public static final int DEFAULT_ITERATIONS = 25;
	public static final int DEFAULT_SIZE = 50000;

	public static final int ADVANCE_THRESHOLD = 1000;
	public static final int APPLY_THRESHOLD = 100;

	static final double PI = 3.141592653589793;
	static final double SOLAR_MASS = 4 * PI * PI;

	private final int NTHREADS = Runtime.getRuntime().availableProcessors();

	//public long time = 0;

	protected NBody[] bodies;


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

		//long startTime = System.currentTimeMillis();
		Thread[] threads = new Thread[NTHREADS];


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

		for (int tid=0; tid < NTHREADS; tid++ ) {
			final int tidInside = tid;
			Runnable r = () -> {

				int startIndex = tidInside * bodies.length / NTHREADS;
				int endIndex = (tidInside + 1) * bodies.length / NTHREADS;

				for (int i = startIndex; i < endIndex; ++i) {
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
						
						synchronized (body) {
							body.vx += dx * iBody.mass * mag;
							body.vy += dy * iBody.mass * mag;
							body.vz += dz * iBody.mass * mag;
						}
					}
				}
			};
			threads[tidInside] = new Thread(r);
			threads[tidInside].start();
		}

		for (Thread t :threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		/*Seq is faster because of overhead (tested for 1M)
		for (NBody body : bodies) {
			body.x += dt * body.vx;
			body.y += dt * body.vy;
			body.z += dt * body.vz;
		}*/
		
		IntStream.range(0, bodies.length)
				 .parallel()
				 .forEach((int i) -> {
					 bodies[i].x += dt * bodies[i].vx;
					 bodies[i].y += dt * bodies[i].vy;
					 bodies[i].z += dt * bodies[i].vz;
		});

		//time += (System.currentTimeMillis() - startTime);
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
}