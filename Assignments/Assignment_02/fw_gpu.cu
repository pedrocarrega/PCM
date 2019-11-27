#include <assert.h>
#include <cuda.h>
#include <cuda_runtime.h>
#include <device_launch_parameters.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define GRAPH_SIZE 32

#define EDGE_COST(graph, graph_size, a, b) graph[a * graph_size + b]
#define D(a, b) EDGE_COST(output, graph_size, a, b)

#define INF 0x1fffffff

#define HANDLE_ERROR(error) { \
    if (error != cudaSuccess) { \
        fprintf(stderr, "%s in %s at line %d\n", \
                cudaGetErrorString(error), __FILE__, __LINE__); \
        exit(EXIT_FAILURE); \
    } \
} \

void generate_random_graph(int *output, int graph_size) {
  int i, j;

  srand(0xdadadada);

  for (i = 0; i < graph_size; i++) {
    for (j = 0; j < graph_size; j++) {
      if (i == j) {
        D(i, j) = 0;
      } else {
        int r;
        r = rand() % 40;
        if (r > 20) {
          r = INF;
        }

        D(i, j) = r;
      }
    }
  }
}

//__device__ int min(int x, int y) { return x < y ? x : y; }

__global__ void floyd_warshall_gpu(int *output, int graph_size, int const k) {
    
    //__shared__ int best;
    int col = blockIdx.x * blockDim.x + threadIdx.x;
    if (col >= graph_size){
    //int idx = graph_size * blockIdx.y + col;
    int idx = blockIdx.y * blockDim.y + threadIdx.y;
    /*
    PLACE SHARED MEMORY
    __syncthreads();
    */
    if (D(col, k) + D(k, idx) < D(col, idx)) {
        D(col, idx) = D(col, k) + D(k, idx);
    }
    }
    

    //D(col, idx) = min(D(col, k), D(k, idx));
    

}

void floyd_warshall_cpu(const int *graph, int graph_size, int *output) {
  int i, j, k;

  memcpy(output, graph, sizeof(int) * graph_size * graph_size);

  for (k = 0; k < graph_size; k++) {
    for (i = 0; i < graph_size; i++) {
      for (j = 0; j < graph_size; j++) {
        if (D(i, k) + D(k, j) < D(i, j)) {
          D(i, j) = D(i, k) + D(k, j);
        }
      }
    }
  }
}

void printGraph(int* output, int graph_size) {
    int i, j;
        for (i = 0; i < graph_size; i++) {
            
            for (j = 0; j < graph_size; j++) {
                int k = D(i, j);
                fprintf(stderr, "%d ", k);
            }
            fprintf(stderr, "\n");
        
    }
}

int main(int argc, char **argv) {
/*#define TIMER_START() gettimeofday(&tv1, NULL)
#define TIMER_STOP()                                                           \
  gettimeofday(&tv2, NULL);                                                    \
  timersub(&tv2, &tv1, &tv);                                                   \
  time_delta = (float)tv.tv_sec + tv.tv_usec / 1000000.0

  
    
    
  struct timeval tv1, tv2, tv;
  float time_delta = 0;*/
    
  cudaDeviceProp prop;
  cudaGetDeviceProperties(&prop, 0);

  int *graph, *graph_gpu, *output_cpu, *output_gpu;
  int size;

  size = sizeof(int) * GRAPH_SIZE * GRAPH_SIZE;

  graph = (int *)malloc(size);
  assert(graph);

  output_cpu = (int *)malloc(size);
  assert(output_cpu);
  memset(output_cpu, 0, size);

  output_gpu = (int *)malloc(size);
  assert(output_gpu);

  generate_random_graph(graph, GRAPH_SIZE);

  fprintf(stderr, "running on cpu...\n");
  //TIMER_START();
  floyd_warshall_cpu(graph, GRAPH_SIZE, output_cpu);
  //TIMER_STOP();
  //fprintf(stderr, "%f secs\n", time_delta);

  fprintf(stderr, "running on gpu...\n");
  //TIMER_START();
  printGraph(output_cpu, GRAPH_SIZE);
  
  HANDLE_ERROR(cudaMalloc(&graph_gpu, size));
  HANDLE_ERROR(cudaMemcpy(graph_gpu, graph, size, cudaMemcpyHostToDevice));

  dim3 dimGrid((GRAPH_SIZE + prop.maxThreadsPerBlock - 1) / prop.maxThreadsPerBlock, GRAPH_SIZE);

  for (int k = 0; k < GRAPH_SIZE; k++)
  {
      floyd_warshall_gpu<<<1, dim3(GRAPH_SIZE,GRAPH_SIZE)>>>(graph_gpu, GRAPH_SIZE, k);
      cudaError_t err = cudaDeviceSynchronize();
      if (err != cudaSuccess) { printf("%s in %s at line %d\n", cudaGetErrorString(err), __FILE__, __LINE__); }
  }

  cudaMemcpy(output_gpu, graph_gpu, size, cudaMemcpyDeviceToHost);
  printGraph(output_gpu, GRAPH_SIZE);
  cudaFree(graph_gpu);

  
  //TIMER_STOP();
  //fprintf(stderr, "%f secs\n", time_delta);

  if (memcmp(output_cpu, output_gpu, size) != 0) {
    fprintf(stderr, "FAIL!\n");
  }

  return 0;
}
