package knapsack;

import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class ParallelMergeSort extends RecursiveAction{
	
	private static final int MAX = 1 << 13;
	private final Individual[] array;
	private final Individual[] helper;
	private final int low;
	private final int high;

	public ParallelMergeSort(final Individual[] array, final int low, final int high) {
		this.array = array;
		helper = new Individual[array.length];
		this.low = low;
		this.high = high;
	}

	@Override
	protected void compute() {
		if (low < high) {
			if (high - low <= MAX) {
				mergesort(array, helper, low, high);
			} else {
				final int middle = (low + high) / 2;
				final ParallelMergeSort left = new ParallelMergeSort(array, low, middle);
				final ParallelMergeSort right = new ParallelMergeSort(array, middle + 1, high);
				invokeAll(left, right);
				merge(array, helper, low, middle, high);
			}
		}
	}
	
	private static void mergesort(final Individual[] array, final Individual[] helper, final int low, final int high) {
		if (low < high) {
			final int middle = (low + high) / 2;
			mergesort(array, helper, low, middle);
			mergesort(array, helper, middle + 1, high);
			merge(array, helper, low, middle, high);
		}
	}
	
	private static void merge(final Individual[] array, final Individual[] helper, final int low, final int middle, final int high) {
		for (int i = low; i <= high; i++) {
			helper[i] = array[i];
		}

		int helperLeft = low;
		int helperRight = middle + 1;
		int current = low;

		while (helperLeft <= middle && helperRight <= high) {
			if (helper[helperLeft].fitness >= helper[helperRight].fitness) {
				array[current] = helper[helperLeft++];
			} else {
				array[current] = helper[helperRight++];
			}
			current++;
		}

		while (helperLeft <= middle) {
			array[current++] = helper[helperLeft++];
		}
	}

}
