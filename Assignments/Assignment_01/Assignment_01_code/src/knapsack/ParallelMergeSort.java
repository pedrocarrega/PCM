package knapsack;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class ParallelMergeSort extends RecursiveAction{

	// Decides when to fork or compute directly:
    private static final int SORT_THRESHOLD = 256;
 
    private final Individual[] values;
    private final int from;
    private final int to;
 
    public ParallelMergeSort(Individual[] values) {
        this(values, 0, values.length-1);
    }
 
    public ParallelMergeSort(Individual[] values, int from, int to) {
        this.values = values;
        this.from = from;
        this.to = to;
    }
 
    public void sort() {
        compute();
    }
 
    @Override
    protected void compute() {
        if (from < to) {
            int size = to - from;
            if (size < SORT_THRESHOLD) {
            	for (int i = from+1; i <= to; ++i) {
                    int current = values[i].fitness;
                    int j = i-1;
                    while (from <= j && current > values[j].fitness) {
                        values[j+1] = values[j--];
                    }
                    values[j+1].fitness = current;
                }
            	/*Arrays.sort(values, new Comparator<Individual>() {
    				@Override
    				public int compare(Individual o1, Individual o2) {
    					if (o1.fitness > o2.fitness) return -1;
    					if (o1.fitness < o2.fitness) return 1;
    					return 0;
    				}
    			});*/
            } else {
                int mid = from + Math.floorDiv(size, 2);
                invokeAll(
                        new ParallelMergeSort(values, from, mid),
                        new ParallelMergeSort(values, mid + 1, to));
                merge(mid);
            }
        }
    }
 
    private void merge(int mid) {
        Individual[] left = Arrays.copyOfRange(values, from, mid+1);
        Individual[] right = Arrays.copyOfRange(values, mid+1, to+1);
        int f = from;
 
        int li = 0, ri = 0;
        while (li < left.length && ri < right.length) {
            if (left[li].fitness >= right[ri].fitness) {
                values[f++] = left[li++];
            } else {
                values[f++] = right[ri++];
            }
        }
 
        while (li < left.length) {
            values[f++] = left[li++];
        }
 
        while (ri < right.length) {
            values[f++] = right[ri++];
        }
    }

}
