package sorting.partition;

public class ABCSorting {

    /**
     * Suppose a city has n people, and these people need to vote to select a mayor of the city. There are three candidates for a mayor: "A",  "B" and "C".
     * We are given an array of n Strings where each element represents a vote for either candidate "A" or candidate "B", or candidate "C". For the purpose of this problem, let's assume there is a clear winner (so one candidate has more votes than the other two),
     * and each candidate has at least one vote for them.
     * Design and implement (in Java) an in-place algorithm for (1) sorting this array and (2) determining who wins the election, "A", "B" or "C".
     *  The algorithm must satisfy the following requirements:
     - SHOULD NOT simply count A,B,C elements -
     - Use the variation of the partition method of quicksort
     - Should run in linear time
     - Use no extra array or any other data structure
     - Run in two passes
     * Important: Do NOT just iterate over the array and count the number of "A"s, "B"s and "C"s  - such solutions will get 0 points.
     * Do NOT use counting sort.
     * Example: if we are given the following array that represents votes of 11 people:
     *      *             ["A", "B", "A", "C", "A", "A", "A", "B", "C", "A", "B"],
     *      *     your method should return "A" and change the array so that it is sorted while satisfying other requirements listed above:
     *      *             ["A", "A", "A", "A", "A", "A", "B", "B", "B",  "C", "C"]
     *
     * @param votes input array of votes
     * @return winner
     */

    public String sortAndFindWinner(String[] votes) {
        int n = votes.length;
        int low = 0, high = n - 1;
        // FILL IN CODE:
        // Do NOT simply count As, Bs, Cs  - no credit for such solutions
        // Instead, look at the exercise where we sorted ("partitioned") 0s and 1s in class

        // Partition A vs {B,C}
        while(low < high){
            while(low < high && votes[low].equals("A")){
                low++;
            }
            while(low < high && (votes[high].equals("B") || votes[high].equals("C"))){
                high--;
            }
            if(low < high){
                String temp = votes[low];
                votes[low] = votes[high];
                votes[high] = temp;

            }
        }
        // Partition B vs C
        high = n - 1; // reset the high pointer to correctly go through the second pass
        while(low < high){
            while(low < high && votes[low].equals("B")){
                low++;
            }
            while(low < high && votes[high].equals("C")){
                high--;
            }
            if(low < high){
                String temp = votes[low];
                votes[low] = votes[high];
                votes[high] = temp;
            }
        }

        // Pick the winner

        for(int i = 0; i < votes.length; i++){
            System.out.print(votes[i] + " ");
        }
        return votes[votes.length/2]; // change
    }


}
