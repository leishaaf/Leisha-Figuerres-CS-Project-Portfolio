package sorting.heapsort;

public class MaxHeap {
    private Comparable[] heap;  // does NOT store the sentinel value at index 0
    private int maxsize;
    private int size; // the current number of elements in the array

    /**
     * Constructor for the max heap - builds a max heap from the bottom up
     *
     * @param array of keys
     */
    public MaxHeap(Comparable[] array) {
        maxsize = array.length;
        heap = new Comparable[maxsize];
        for (int i = 0; i < array.length; i++)
            heap[i] = array[i];
        size = array.length;
        fixFromBottomUp();  // also can be called heapify
    }

    /**
     * Fixes the max heap from the bottom up
     */
    public void fixFromBottomUp() {
        // FILL IN CODE:
        // find first node that isn't a leaf and then calling pushdown
        for(int i = size/2 -1; i >= 0; i--){
            pushdown(i);
        }
    }

    /**
     * Return the index of the left child of the element at index pos
     *
     * @param pos the index of the element in the heap array
     * @return the index of the left child
     */
    private int leftChild(int pos) {
        return 2 * pos + 1;
    }

    /**
     * Return the index of the right child
     *
     * @param pos the index of the element in the heap array
     * @return the index of the right child
     */
    private int rightChild(int pos) {
        return 2 * pos + 2;
    }

    /**
     * Return the index of the parent
     *
     * @param pos the index of the element in the heap array
     * @return the index of the parent
     */
    private int parent(int pos) {
        return (pos - 1) / 2;
    }

    /**
     * Returns true if the node in a given position is a leaf
     *
     * @param pos the index of the element in the heap array
     * @return true if the node is a leaf, false otherwise
     */
    public boolean isLeaf(int pos) {
         return ((pos > (size - 2) / 2) && (pos < size));
    }

    /**
     * Swap elements at indices pos1 and pos2
     *
     * @param pos1 the index of the first element in the heap
     * @param pos2 the index of the second element in the heap
     */
    private void swap(int pos1, int pos2) {
        Comparable tmp = heap[pos1];
        heap[pos1] = heap[pos2];
        heap[pos2] = tmp;
    }

    /**
     * Print the array that stores the heap
     */
    public void print() {
        for (int i = 0; i < size; i++)
            System.out.print(heap[i] + " ");
        System.out.println();
    }

    /**
     * Remove the largest element (the element at top of the maxHeap)
     *
     * @return the largest element in the max heap
     */
    public Comparable removeMax() {
        swap(0, size - 1);
        size--;
        if (size != 0)
            pushdown(0);
        return heap[size];
    }

    /**
     * Push the value down the heap if it does not satisfy the heap property
     *
     * @param position the index of the element in the heap
     */
    private void pushdown(int position) {
        int largestChild;
        while (!isLeaf(position)) {
            largestChild = leftChild(position);
            if (largestChild >= size) // leaf
                return;
            if (largestChild + 1 <  size) { // right child exists
                if (heap[largestChild + 1].compareTo(heap[largestChild])>0) {
                    largestChild = largestChild + 1; // right child was larger
                }
            }
            if (heap[position].compareTo(heap[largestChild]) > 0)
                return;
            swap(position, largestChild);
            position = largestChild;
        }
    }

    public Comparable[] getArray() {
        return heap; // would be better to return a copy
    }

    public static void main(String[] args) {
        Comparable[] arr = {1,6,7,3,6,2,7,9,20};
        MaxHeap heap = new MaxHeap(arr);
        heap.fixFromBottomUp();
        heap.print();
    }
}
