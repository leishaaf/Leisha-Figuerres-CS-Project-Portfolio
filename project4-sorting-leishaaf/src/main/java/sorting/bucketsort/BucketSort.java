package sorting.bucketsort;

// imports added by leisha
import java.util.LinkedList;

/** Implementation of bucket sort */
public class BucketSort {
    /**
     * Sorts a sub-array of records using bucket sort.
     * Precondition: all keys must be ≥ 0.
     * @param array     array of records
     * @param lowindex  the beginning index of the sublist to sort
     * @param highindex the end index of the sublist to sort
     * @param reversed  if true, sort in descending (decreasing) order, otherwise ascending
     */
    public void sort(Elem[] array, int lowindex, int highindex, boolean reversed) {
        int bucketCount = Math.max(1, (highindex  - lowindex + 1) / 2);
        // FILL IN CODE:
        // Create buckets: array of linked lists
        LinkedList<Elem>[] buckets = new LinkedList[bucketCount];

        // Find the max key  in the subarray from lowindex to highindex
        int maxKey = 0;
        for(int i = lowindex; i <= highindex; i++){
            if(array[i].key() > maxKey){
                maxKey = array[i].key();
            }
        }
        // initialize buckets array
        for(int i = 0; i < buckets.length; i++){
            buckets[i] = new LinkedList<Elem>();
        }
        // Compute bucketRange given maxKey and bucketCount
        // Use the following formula: (maxKey / bucketCount)  + 1
        int bucketRange = (maxKey / bucketCount) + 1;

        // Iterate over array from lowindex to highindex and add each element into the corresponding bucket
        // in sorted order
        for(int i = lowindex; i <= highindex; i++){
            int index = array[i].key() / bucketRange;
            LinkedList<Elem> bucket = buckets[index]; // add to the linked list in the actual buckets array by creating reference to it
            int bucketInd = 0; //
            // fill the bucket in correct order
            while(bucketInd < bucket.size() && bucket.get(bucketInd).key() < array[i].key()){ // correctly add elements into bucket sorted
                bucketInd++; // keep finding a place for array[i] elem to be inserted into bucket list
            }
            bucket.add(bucketInd, array[i]); // add elem into bucket
        }
        // Place elements back into array
        // Handle reverse == true and reverse == false
        // Do NOT just reverse the resulting array, make changes to the algorithm
        int bucketsInd = lowindex;
        if (reversed) {
          // From last bucket down to first, and removeLast() each list
            for(int i = buckets.length - 1; i >= 0; i--){
                while(!buckets[i].isEmpty()){ // put the entire bucket into actual array
                    array[bucketsInd] = buckets[i].removeLast(); // add the last element
                    bucketsInd++;
                }
            }
        } else {
            //From first bucket up to last, and removeFirst() each list
            for(int i = 0; i < buckets.length; i++){
                while(!buckets[i].isEmpty()){
                    array[bucketsInd] = buckets[i].removeFirst(); // add the first element of bucket
                    bucketsInd++;
                }
            }

        }
    }
}
