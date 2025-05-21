package sorting.heapsort;

/** Implementation of heap sort */
public class HeapSort {
    private MaxHeap heap;

    /**
     * Sorts a given array using heap sort
     * @param arr array
     */
    public void sort(Comparable[] arr) {
        MaxHeap heap = new MaxHeap(arr);
        // FILL IN CODE:
        heap.fixFromBottomUp();
        for(int i = 0; i < arr.length; i++){ // call remove min for each element in the arr
            heap.removeMax();
        }
        Comparable[] results = heap.getArray();
        for(int i = 0; i < results.length; i++){ // copy results into original arr
            arr[i] = results[i];
        }












    }
}
