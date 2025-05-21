//student author: Leisha Figuerres
package songlist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * A custom linked list class that stores song nodes.
 * Each node has a reference to the "next by title" node and the "next by score" node.
 * Allows to iterate over the list by title (in increasing alphabetical order)
 * or by score (in decreasing order of the score).
 */
public class SongList {
    private SongNode headByScore; // Head of the list if we want to iterate in the decreasing order of scores.
    private SongNode headByTitle; // Head of the list if we want to iterate in the increasing alphabetical order of the titles.

    /**
     * Read a give csv file and insert songs into the SongList.
     * @param filename name of csv file with songs; the file stores each song as following:
     * Title;Artist;Score
     *
     */
    public void loadSongs(String filename) {
        // FILL IN CODE:
        String csvFile = filename;
        String line;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            while((line = br.readLine()) != null){
                count++;
                if(count == 1) { continue; }
                String[] lineArr = line.split(";");
                insert(lineArr[0], lineArr[1], Integer.parseInt(lineArr[2]));
            }
        }catch (IOException e){
            System.err.println("Exception found ! " + e);
        }

    }
    /*
     method name: insertByTitle() -  a helper method that inserts SongNodes into list by title (ascending)
     uses the compareTo method to sort alphabetically on each node to insert
     a node is inserted when it is lesser than currents next node and more than the current node (alphabetically)
     a node can also be inserted before the head and replace it when the node to insert comes before it (new head edge case)
    */
    private void insertByTitle(SongNode newNode) {
        SongNode currentTitle = headByTitle;
        String title = newNode.getSong().getTitle();
        if(headByTitle == null){ // empty list case, new node is the head
            headByTitle = newNode;
            return;
        }
        // edge case inserting before head
        int result = title.compareTo(currentTitle.getSong().getTitle());
        if(result < 0){
            newNode.setNextByTitle(headByTitle);
            headByTitle = newNode;
            return;
        }
        // insert alphabetically
        // look at current and current.next & check conditions to see if you should insert
        while(currentTitle.getNextByTitle() != null){
            String currTitle = currentTitle.getSong().getTitle();
            String nextTitle = currentTitle.getNextByTitle().getSong().getTitle();
            int result1 = title.compareTo(currTitle); // get the results of node to insert and comparisons with current.next and current
            int result2 = title.compareTo(nextTitle);
            if(result1 >= 0 && result2 < 0) { // title to insert is after current and title is less than current.next title
                // INSERT NEW NODE
                newNode.setNextByTitle(currentTitle.getNextByTitle());
                currentTitle.setNextByTitle(newNode);
                return;
            }
            currentTitle = currentTitle.getNextByTitle(); // if the inserting conditions are not met, continue traversing until there is a place to insert
        }
        currentTitle.setNextByTitle(newNode); // if there was no place for the node to be inserted between other nodes (determined in while loop), it is alphabetically dominated and therefore inserted at very end
    }
    /*
       method name: insertByScore() -  a helper method that inserts SongNodes into list by score (descending)
       compares the scores on each song node and inserts when a node's score is greater than the current.next's score that way it is inserted between current and next
       if the scores are equal when we're looking to insert between nodes, we compare alphabetically and see which title comes before and insert based on that
       a node can also be inserted as the new head when we find a node's score to be higher than the current head and or when it is = to the heads score and
       alphabetically comes before the heads title
   */
    private void insertByScore(SongNode newNode) {
        SongNode currentScore = headByScore;
        int score = newNode.getSong().getScore();
        Song song = newNode.getSong();
        if(headByScore == null){ // empty list case, new node is the head
            headByScore = newNode;
            return;
        }
        int currScore = currentScore.getSong().getScore();
        int headScore = headByScore.getSong().getScore();
        // edge case new head if score =  score of head, compare alphabetically and if curr title comes before, make it the new head
        // or if score from new node is simply larger than headScore, we make it the new head
       if(score >= headScore || (score == headScore && song.getTitle().compareTo(currentScore.getSong().getTitle()) < 0)){
           newNode.setNextByScore(headByScore);
           headByScore = newNode;
           return;
       }
       // inserting between nodes
       while(currentScore.getNextByScore() != null){
           // only insert a node when it is higher than nextScore
           int nextScore = currentScore.getNextByScore().getSong().getScore();
           if(score > nextScore){ // insert between current and next
               newNode.setNextByScore(currentScore.getNextByScore());
               currentScore.setNextByScore(newNode);
               return;
           }
           if(score == nextScore) {
               // alphabetically edge case, compare current title with the next score node's title and if it comes first, insert it
               if(song.getTitle().compareTo(currentScore.getNextByScore().getSong().getTitle()) < 0){
                   newNode.setNextByScore(currentScore.getNextByScore());
                   currentScore.setNextByScore(newNode);
                   return;
               }
           }
           currentScore = currentScore.getNextByScore();

       }
       //insert at end of list ( the least score )
        currentScore.setNextByScore(newNode);
    }
    /** Insert a song node with the given song into this linked list,
     * preserving the correct order, and updating both references (the ones connecting nodes according to the title, and the ones connecting nodes according to the score).
     * Before and after the insertion, the nodes should be ordered by title using nextByTitle references
     * and by score, using nextbyScore references.
     * @param title title
     * @param artist artist
     * @param score score (rating)
     */
    public void insert(String title, String artist, int score) {
        // Feel free to write helper methods like insertByTitle, insertByScore
        // FILL IN CODE:
        //** HANDLE EGDE CASES : inserting a new node before one of the heads

        // creating a new node and sending to helper methods
        Song song = new Song(title,artist,score);
        SongNode newNode = new SongNode(song);
        insertByTitle(newNode); // helper methods
        insertByScore(newNode);
    }
    /** Checks if there's a song with given title/artist in the SongList.
     * @param title title of the song
     * @param artist artist of the song
     * @return true if the song is present, and false otherwise
     */
    public boolean containsSong(String title, String artist) {
        // FILL IN CODE:
        SongNode current = headByTitle;
        if(headByTitle == null){ // if list is empty return false because there are no songs
            System.out.println("List is empty");
            return false;
        }
        while(current != null){ // iterate through song list by title and check if the current nodes title == title to search
            if(current.getSong().getTitle().equals(title) && current.getSong().getArtist().equals(artist)){
                return true; // if found
            }
            current = current.getNextByTitle();
        }
        return false; // song isn't found so return false
    }
    /*
      method name: addSongsByScore()
      helper method for mergeByScore() that adds a newNode to the very end of the list maintaining the descending order of the score chain
      the sorting is taken care of prior to this method so nodes maintain order when being added through this method
      method is only called when new nodes score is greater than the other node it is being compared (if == compared alphabetically and then added)
     */
    private void addSongByScore(SongNode nodeToInsert, SongList list) {
        SongNode current = list.headByScore;
        if (current == null) { // iterates through entire list to get the tail and then new node is inserted at the end
            list.headByScore = nodeToInsert;
        } else {
            while (current.getNextByScore() != null) {
                current = current.getNextByScore();
            }
            //current is now the end of the list so we insert
            current.setNextByScore(nodeToInsert);
        }
    }
    /*
     method name: addSongsByTitle()
     helper method for mergeByTitle() that adds a newNode to the very end of the list maintaining the ascending alphabetical order of the title chain
     list is already sorted so order is maintained when this method is used
     method is only called when new nodes title comes before the other nodes title it's compared to
   */
    private void addSongByTitle(SongNode nodeToInsert, SongList list) {
        SongNode current = list.headByTitle;
        if (current == null) {
            list.headByTitle = nodeToInsert;
        } else {
            while (current.getNextByTitle() != null) { // iterates through list to get the tail then inserts after it
                current = current.getNextByTitle();
            }
            //current is now the end of the list so we insert
            current.setNextByTitle(nodeToInsert);
        }
    }
    /** Return a SongList where each song's score falls in [min, max] range.
     * Songs should be sorted in decreasing order of the score.
     *
     * @param min
     * @param max
     * @return SongList that contains songs whose score is >= min, <= max.
     */
    public SongList findSongsWithinScoreRange(int min, int max) {
        if(min < 0 || max < 0){ // checks for invalid numbers as max or min
            System.out.println("Invalid number or numbers");
        }
        SongList result = new SongList(); // create list that the nodes with scores in range will be added to
        SongNode current = headByScore;
        if(current == null){ // edge case, empty list
            System.out.println("List is empty");
            return result;
        }
        // FILL IN CODE:
        while(current != null){ // iterate through the list by score and check if the score is within range, if so insert into the result list
            int score = current.getSong().getScore();
            if(score <= max && score >= min){
               result.insert(current.getSong().getTitle(), current.getSong().getArtist(), score);
            }
            current = current.getNextByScore();
        }
        return result;
    }
    /*
        method name: mergeByScore()
        helper method for mergeWith() that merges two lists and maintains the descending score chain
        keeps track of a "currentScore" and "otherScore", compares them, and adds them to the merged list if one is greater than the other - if equal compares alphabetically
        new nodes are added by making a copy of which node should be appended and is sent to the addBySong() method (along with the merged list as a parameter) that adds nodes to the end of that list
        handles if one of the lists are empty and appends the entire list of the one that isn't to the merged list by calling addFromOneListBySong()
        if there are extras after the initial sort of two lists that aren't empty, they are handled in while loops that add the remaining part of the longer list by calling addFromOneListBySong()
     */
    private SongList mergeByScore(SongList other, SongList merged) {
        SongNode currentScore = headByScore;
        SongNode otherScore = other.headByScore;
        // cases where either one of the lists is empty, then append the whole thing to merged list using helper method
        if(headByScore == null && other.headByScore != null) { // if SongList is empty but other isn't
            addFromOneListByScore(merged,otherScore);
            return merged;
        }
        // if other SongList is empty and current isnt
        if(other.headByScore == null && headByScore != null) {
            addFromOneListByScore(merged,currentScore);
            return merged;
        }
        // merge two lists that aren't empty by comparing their scores
        while(currentScore != null && otherScore != null) {
            int currScore = currentScore.getSong().getScore();
            int othScore = otherScore.getSong().getScore();
            int result = currentScore.getSong().getTitle().compareTo(otherScore.getSong().getTitle()); // result of comparing alphabetically if we need it (if two scores are equal)
            // if the current score is greater than the other score or if it is equal but its title comes before other, it gets inserted
            if(currScore > othScore || (currScore == othScore && result < 0)) {
                Song song = new Song(currentScore.getSong().getTitle(), currentScore.getSong().getArtist(), currentScore.getSong().getScore());
                SongNode newNode = new SongNode(song);
                addSongByScore(newNode, merged);
                // only update pointers when an insertion happens
                currentScore = currentScore.getNextByScore();
            }else{
                // if other comes first, insert it instead
                Song song = new Song(otherScore.getSong().getTitle(), otherScore.getSong().getArtist(), otherScore.getSong().getScore());
                SongNode newNode = new SongNode(song);
                addSongByScore(newNode, merged);
               otherScore = otherScore.getNextByScore();
            }
        }
        // dealing with extras by checking the longer list out the two and adding the remaining nodes into merged using helper method
        if(currentScore != null){ addFromOneListByScore(merged,currentScore);}
        if(otherScore != null){ addFromOneListByScore(merged,otherScore);}
        return merged;
    }
    /*
        method name: mergeByTitle()
        helper method for mergeWith() that merges two lists and maintains the chain title (alphabetical)
        first checks the cases if one of the lists being compared is empty, if so then append that entire list to merge using addFromOneListByTitle()
        if not, it compares both lists alphabetically and the node that comes before the other will get inserted into merge using addByTitle()
        if there's a longer list, extras get added at the end using addFromOneListByTitle()
     */
    private SongList mergeByTitle(SongList other, SongList merged) {
        SongNode currentTitle = headByTitle;
        SongNode otherTitle = other.headByTitle;
        // cases where one of the lists are empty
        if(headByTitle == null && other.headByTitle != null) { // if SongList is empty but other isn't
            addFromOneListByTitle(merged, otherTitle); // helper method that appends remaining nodes into a given list
            return merged;
        }
        // if other SongList is empty and current isnt
        if(other.headByTitle == null && headByTitle != null) {
            addFromOneListByTitle(merged, currentTitle);
            return merged;
        }
        // merge two lists
        while(currentTitle != null && otherTitle != null) {
            int result = currentTitle.getSong().getTitle().compareTo(otherTitle.getSong().getTitle());
            if(result < 0) { // if current is less than other, it gets inserted
                Song song = new Song(currentTitle.getSong().getTitle(), currentTitle.getSong().getArtist(), currentTitle.getSong().getScore());
                SongNode newNode = new SongNode(song);
                addSongByTitle(newNode, merged);
               // only update pointers when inserted
                currentTitle = currentTitle.getNextByTitle();
            }else { // if other comes first
                Song song = new Song(otherTitle.getSong().getTitle(), otherTitle.getSong().getArtist(), otherTitle.getSong().getScore());
                SongNode newNode = new SongNode(song);
                addSongByTitle(newNode, merged);
                otherTitle = otherTitle.getNextByTitle();
            }
        }
        // dealing with extras by calling helper method that adds nodes to a given list
        if(currentTitle != null){ addFromOneListByTitle(merged, currentTitle); }
        if(otherTitle != null){ addFromOneListByTitle(merged, otherTitle); }
        return merged;
    }
    /*
       method name: addFromOneListByTitle()
       helper method for mergeByTitle that is called when we need to append nodes from only one by title list i.e one of the two lists being compared is empty
       or there is a longer list and extras need to be added at the end
     */
    private SongList addFromOneListByTitle(SongList merged, SongNode current){
        while(current != null){
            Song song = new Song(current.getSong().getTitle(), current.getSong().getArtist(), current.getSong().getScore());
            SongNode newNode = new SongNode(song);
            addSongByTitle(newNode, merged);
            current = current.getNextByTitle();
        }
        return merged;
    }
    /*
     method name: addFromOneListByScore()
     helper method for mergeByScore() that has the sane logic as addFromOneListByTitle : difference is that it iterates and appends by score
   */
    private SongList addFromOneListByScore(SongList merged, SongNode current){
        while(current != null){
            Song song = new Song(current.getSong().getTitle(), current.getSong().getArtist(), current.getSong().getScore());
            SongNode newNode = new SongNode(song);
            addSongByScore(newNode, merged);
            current = current.getNextByScore();
        }
        return merged;
    }

    /** Merge this song list with the "other" sorted song list and return a new list.
     *  The resulting list should be sorted both by score in decreasing order and by title in increasing alphabetical order.
     * @param other another SongList
     * @return a new SongList that contains songs from both this and other lists.
     */
     public SongList mergeWith(SongList other) {
         // FILL IN CODE:
//          Feel free to add helper methods like mergeByScore, mergeByTitle
        SongList merged = new SongList();
         // if both lists are empty
         if(headByScore == null && other.headByScore == null){
             return null;
         }
         mergeByScore(other, merged);
         mergeByTitle(other, merged);
        return merged;
    }

    /**
     * Return a new SongList containing the top k highest-scoring songs.
     * If k >= the total number of songs, all songs are returned.
     * @param k number of highest scoring songs to return
     * @return song list containing the top k highest-scoring songs
     */
    public SongList findBestKSongs(int k) {
        SongList result = new SongList();
        if(k <= 0){ // checks for invalid input
            System.out.println("Invalid. Please enter a positive non zero number");
            return result;
        }
        SongNode current = headByScore;
        // FILL IN CODE:
        if(headByScore == null){ return result; }
            int iterationCount = 0;
            // returns k best songs
            while(current != null && iterationCount < k){ // gets the first k iterations which returns k amount of best songs because the best songs are at the beg of list
                int score = current.getSong().getScore();
                result.insert(current.getSong().getTitle(), current.getSong().getArtist(), score);
                current = current.getNextByScore();
                iterationCount++;
            }
        return result;
    }


    /**
     * Return a new SongList containing the k lowest-scoring songs.
     * Must use the slow/fast pointer approach to find the start of the last k nodes.
     * Not allowed to count nodes or keep track of the size of the list.
     * If k >= total size, return a list with all songs.
     * @param k number of lowest scoring songs to return.
     * @return song list with k lowest-scoring songs
     */
    public SongList findWorstKSongs(int k) {
        SongList result = new SongList();
        if(k <= 0) { // checks for invalid input
            System.out.println("Invalid number, please enter a positive nonzero number");
            return result;
        }
        if(headByScore == null){
            System.out.println("Empty List");
            return result;
        }
        // FILL IN CODE:
        SongNode slow = headByScore;
        SongNode fast = headByScore;
        int kIteration = 0;
        // first move fast pointer k amount of times
        while(fast != null && kIteration < k){
            fast = fast.getNextByScore();
            kIteration++;
        }
        // then incremement fast and slow pointers by one, fast moves first
        while(fast != null && slow != null) {
            fast = fast.getNextByScore();
            slow = slow.getNextByScore();
        }
        // adds the remaining of the list (k worst songs) by adding what the slow pointer is at until the end of the list
        while(slow != null){
            result.insert(slow.getSong().getTitle(), slow.getSong().getArtist(), slow.getSong().getScore());
            slow = slow.getNextByScore();
        }
        return result;
    }
    /**
     * An iterator for iterating "by title"
     * @return iterator by title
     */
    public Iterator<Song> iteratorByTitle() {
        return new IteratorByTitle();
    }

    /** An iterator for iterating "by score"
     *
     * @return iterator by score
     */
    public Iterator<Song> iteratorByScore() {
        return new IteratorByScore();
    }

    /* Iterator by Title */
    class IteratorByTitle implements Iterator<Song> {
        private SongNode current = headByTitle;

        @Override
        public boolean hasNext() {
            // FILL IN CODE:
            if(current != null){
                return true;
            }
            return false;
        }

        /** Return the current song, move the iterator to the "nextByTitle" song node.
         *
         * @return current song
         */
        @Override
        public Song next() {
            // FILL IN CODE:
            if(!hasNext()){
                System.out.println("No next");
                return null;
            }
            Song currSong = current.getSong(); // get the current song
            current = current.getNextByTitle(); // increment current (iterate)
            return currSong; // return the current song
        }
    };

    /* Iterator by score. */
    class IteratorByScore implements Iterator<Song> {
        private SongNode current = headByScore;

        @Override
        public boolean hasNext() {
            // FILL IN CODE:
            if(current != null){
                return true;
            }
            return false;
        }

        /** Return the current Song, and move the iterator to the "nextByScore" node
         *
         * @return current song
         */
        @Override
        public Song next() {
            // FILL IN CODE:
            if(!hasNext()){
                System.out.println("No next");
                return null;
            }
            Song currSong = current.getSong();
            current = current.getNextByScore();
            return currSong; // change
        }
    };
}
