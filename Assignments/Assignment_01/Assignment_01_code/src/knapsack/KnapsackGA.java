package knapsack;

//import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class KnapsackGA {
	private static final int N_GENERATIONS = 500;
	private static final int POP_SIZE = 500000;
	private static final double PROB_MUTATION = 0.5;
	private Individual[] population = new Individual[POP_SIZE];

	//private Random r = new Random();

	public KnapsackGA() {
		populateInitialPopulationRandomly();
	}

	private void populateInitialPopulationRandomly() {

		/*for (int i=0; i<POP_SIZE; i++) {
			population[i] = Individual.createRandom();
		}*/

		IntStream.range(0, POP_SIZE)
				 .parallel()
				 .forEach((int i) -> {
					 	population[i] = Individual.createRandom(); 
				 });
	}

	public void run() {
		for (int generation=0; generation<N_GENERATIONS; generation++) {

			IntStream.range(0, POP_SIZE)
					 .parallel()
					 .forEach((int i) -> {
						 	population[i].measureFitness(); 
					 });

			/*
			// Step1 - Calculate Fitness
			for (int i=0; i<POP_SIZE; i++) {
				population[i].measureFitness();
			}*/

			// Step2 - Sort by Fitness descending
			//Done
			new ForkJoinPool(Runtime.getRuntime().availableProcessors()).invoke(new ParallelMergeSort(population, 0, population.length - 1));

			// Debug

			//System.out.println("Best fitness at " + generation + " is " + population[0].fitness);

			// Step3 - Find parents to mate (cross-over)
			Individual[] newPopulation = new Individual[POP_SIZE];
			newPopulation[0] = population[0]; // The best individual remains

			IntStream.range(1, POP_SIZE)
					 .parallel()
					 .forEach((int i) -> {

						 	ThreadLocalRandom r = ThreadLocalRandom.current();

						 	int pos1 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;
						 	int pos2 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;

						 	newPopulation[i] = population[pos1].crossoverWith(population[pos2]); 
					 });

			/*
			for (int i=1; i<POP_SIZE; i++) {
				// The first elements in the population have higher probability of being selected
				int pos1 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;
				int pos2 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;

				newPopulation[i] = population[pos1].crossoverWith(population[pos2]);
			}
			 */

			IntStream.range(1, POP_SIZE)
					 .parallel()
					 .forEach((int i) -> {

						 	ThreadLocalRandom r = ThreadLocalRandom.current();
						 	if (r.nextDouble() < PROB_MUTATION) {
						 		newPopulation[i].mutate();
						 	}
					 });

			/*
			// Step4 - Mutate
			for (int i=1; i<POP_SIZE; i++) {
				if (r.nextDouble() < PROB_MUTATION) {
					newPopulation[i].mutate();
				}
			}
			 */
			population = newPopulation;
		}
	}

}
