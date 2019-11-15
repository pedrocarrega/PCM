#include<stdio.h>
#include<stdlib.h>

#define N 1000

int main(int argc, char **argv) {

    int a[n], r[n];
    int *dev_a, *dev_r;

    cudaMalloc(&dev_a, sizeof(int) * N);
    cudaMalloc(&dev_r, sizeof(int) * N);

    cudaMemcpy(dev_a, a, sizeof(int) * N, cudaMemcpyHostToDevice);
    cudaMemcpy(dev_b, r, sizeof(int) * N, cudaMemcpyHostToDevice);

    cudaMemcpy(dev_a, a, sizeof(int) * N, cudaMemcpyDeviceToHost);
    cudaMemcpy(dev_r, r, sizeof(int) * N, cudaMemcpyDeviceToHost);

    

    

    
    return 0;
}

void fill_vector(int *a, int *r){
    int i;
    srand(time(NULL));

    for(i = 0; i < N; i++){
        r[i]=0;
        a[i]=rand();
    }

}

__global__ void minimum(int *a, int *r){

}
