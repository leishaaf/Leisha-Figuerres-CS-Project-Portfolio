package sorting.quicksort;

// imports added by leisha
import java.util.Random;

/** Implementation of randomized quick sort, where we pick the pivot as the median (not MEAN) of three elements of the subarray at random indices */
public class RandomizedQuicksort {
    /**
     * Sort the sublist of the given list (from lowindex to highindex, inclusive)
     * using the randomizedQuickSort
     * @param array array to sort
     * @param lowindex the beginning index of a sublist
     * @param highindex the end index of a sublist
     */
    public void sort(Comparable[] array, int lowindex, int highindex) {
        int pivotIndex; // index of the pivot
        if (lowindex >= highindex)
            return;
        // FILL IN CODE: Recursive case
        // partition the subarray from lowindex to high index
        // recursively sort the subarray from lowindex to pivotIndex - 1
        // recursively sort the subarray from pivotIndex + 1 to highindex (inclusive)
        pivotIndex = partition(array, lowindex, highindex); // partition the array first before calling quick sort recursive calls - gets the pivot index
        sort(array, lowindex, pivotIndex - 1); // sort the first half of the sub array left of pivot
        sort(array, pivotIndex + 1, highindex); // sort the second half of the sub array right of pivot

    }

    /** Partition helper method for randomized quick sort.
     *
     * @param array input array
     * @param lowindex start index of the subarray
     * @param highindex end index of the subarray
     * @return index of the pivot at the end of the parition
     */
    private int partition(Comparable array[], int lowindex, int highindex) {
        int median;
        // FILL IN CODE:
        // Select the pivot according to the following algorithm:
        //  - Generate three random indices in the range from lowindex to highindex
        //  - Access three array elements at these indices and pick the median one
        // The median of three elements is the one that isn’t the largest or the smallest;
        // median - the middle value when you put the three numbers in order.

        // Partition the subarray from lowindex to highindex using the pivot selected above
        Comparable temp; // for multiple swaps done later in method
        // get the pivot by first generating three random indices
        Random rand = new Random();
        int ind1 = rand.nextInt(highindex -  lowindex + 1) + lowindex;; // generate three random indices within range
        int ind2 = rand.nextInt(highindex -  lowindex + 1) + lowindex;;
        int ind3 = rand.nextInt(highindex -  lowindex + 1) + lowindex;;
        while(ind1 == ind2 && ind2 == ind3){ // makes sure there isn't a pick where all indices are the same
            ind1 = rand.nextInt(highindex -  lowindex + 1) + lowindex; // generates an int from range low to high indices
            ind2 = rand.nextInt(highindex -  lowindex + 1) + lowindex;
            ind3 = rand.nextInt(highindex -  lowindex + 1) + lowindex;
        }
        // get the median of arr at the three indices - will be used as pivot
        Comparable elem1 = array[ind1];
        Comparable elem2 = array[ind2];
        Comparable elem3 = array[ind3];
        int pivotInd;
        int ogPivotInd; // holds the original pivot index used to swap at the very end of method
        Comparable pivot;
        if((elem1.compareTo(elem2) >= 0 && elem1.compareTo(elem3) <= 0) ||  (elem1.compareTo(elem2) <= 0 && elem1.compareTo(elem3) >= 0)){ // elem1 is median
            pivotInd = ind1;
            pivot = elem1;
        }else if((elem2.compareTo(elem1) >= 0 && elem2.compareTo(elem3) <= 0) ||  (elem2.compareTo(elem1) <= 0 && elem2.compareTo(elem3) >= 0)){ // elem2 is median
            pivotInd = ind2;
            pivot = elem2;
        }else{ // elem 3 is median
            pivotInd = ind3;
            pivot = elem3;
        }
        // swap pivot (median)to end
        temp = array[highindex];
        array[highindex] = array[pivotInd];
        array[pivotInd] = temp;
        ogPivotInd = highindex;

        while(lowindex <= highindex){
            while((lowindex <= highindex) && array[lowindex].compareTo(pivot) < 0){ // keep the low pointer moving so long as array[low] is before pivot
                lowindex++;
            }
            while((lowindex <= highindex) && array[highindex].compareTo(pivot) >= 0){ // get decrementing the high pointer as long as arr[high] is after the pivot
                highindex--;
            }
            if(lowindex <= highindex){
                // swap the high and low indices once they can't move anymore
                temp = array[lowindex];
                array[lowindex] = array[highindex];
                array[highindex] = temp;
                lowindex++;
                highindex--;
            }
        }
        // swap pivot with low index
        temp = array[lowindex];
        array[lowindex] = array[ogPivotInd];
        array[ogPivotInd] = temp;
        return lowindex; // change, return the pivot index
    }

    private void swap(Comparable[] a, int i, int j) {
        Comparable tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    // added to test
    public static void main(String[] args) {
        RandomizedQuicksort sort = new RandomizedQuicksort();
//        Comparable[] arr = {5,4,12,2,1,8,0,3,17,12,9,4,-1};
        Comparable[] suggestedTest = {5,2,9,12,6,8,3,7};
        sort.sort(suggestedTest, 0, suggestedTest.length-1);
        for(int i = 0; i < suggestedTest.length; i++){
            System.out.print(suggestedTest[i] + " ");
        }
    }
}
