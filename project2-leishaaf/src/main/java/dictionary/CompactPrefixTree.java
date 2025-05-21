/*
    student author: Leisha Figuerres
    date: March 24th, 2025
    class: CS245
 */
package dictionary;
// added by Leisha
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;

/** CompactPrefixTree class, implements Dictionary ADT and
 *  several additional methods. Can be used as a spell checker.
 *  Fill in code and feel free to add additional methods as needed.
 *
 *  */
public class CompactPrefixTree implements Dictionary {
    private Node root; // the root of the tree

    /* --------- Private class Node ------------
      Represents a node in a compact prefix tree.
     */
    private class Node {
        String prefix; // prefix stored in the node
        Node children[]; // array of children (26 children)
        boolean isWord; // true if by concatenating all prefixes on the path from the root to this node, we get a valid word

        Node() {
            isWord = false;
            prefix = "";
            children = new Node[26]; // initialize the array of children
        }

        // FILL IN CODE: Feel free to add other methods to class Node as needed
    }

    /**
     * Default constructor
     */
    public CompactPrefixTree() {
        root = new Node();
    }

    /**
     * Creates a dictionary ("compact prefix tree")
     * using words from the given file.
     * See the format of the file in main/src/resources/words_ospd.txt
     *
     * @param filename the name of the file with words
     */
    public CompactPrefixTree(String filename) {
        root = new Node();
        // Read each word from the file, and call the add method
        // FILL IN CODE
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                add(line); // calls add method on each new word in file
            }
        } catch (IOException e) {
            System.err.println("Exception Found ! " + e);
        }
    }

    /**
     * Adds a given word to the dictionary.
     *
     * @param word the word to add to the dictionary
     */
    public void add(String word) {
        root = add(word.toLowerCase(), root); // Calling private add method
    }

    /**
     * Checks if a given word is in the dictionary
     *
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean check(String word) {
        return check(word.toLowerCase(), root); // Calling a aprivate check method
    }

    /**
     * Checks if a given prefix is stored in the dictionary
     *
     * @param prefix The prefix of a word
     * @return true if this prefix is a prefix of any word in the dictionary,
     * and false otherwise
     */
    public boolean checkPrefix(String prefix) {
        return checkPrefix(prefix.toLowerCase(), root); // Calling a private checkPrefix method
    }

    /**
     * Returns a human-readable string representation of the compact prefix tree;
     * contains nodes listed using pre-order traversal and uses indentations to show the level of the node.
     * An asterisk after the node means the node's boolean flag is set to true.
     * The root is at the current indentation level (followed by * if the node's valid bit is set to true),
     * then there are children of the node at a higher indentation level.
     */
    public String toString() {
        String s = "";
        s = toString(root, 0);
        return s;
    }

    /**
     * Print out the nodes of the tree to a file, using indentations to specify the level
     * of the node.
     *
     * @param filename the name of the file where to output the tree
     */
    public void printTree(String filename) {
        // FILL IN CODE
        // Uses toString() method; outputs info to a file
        // my notes: got help from code example given by professor on piazza
        String[] lines = toString().split("\n"); // creating the array of lines by splitting the toString by new line
        if (lines == null || lines.length == 0) {
            System.err.println("No lines.");
            return;
        }
        try (PrintWriter pw = new PrintWriter(filename)) { // write each line to a file using printwriter
            for (String line : lines) {
                pw.println(line);
            }
            pw.flush();
        } catch (IOException e) {
            System.err.println("ERROR FOUND " + e);
        }
    }

    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.  If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.  If the word is
     * not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use of the compact prefix tree.
     *
     * @param word           The word to check
     * @param numSuggestions The length of the array to return.  Note that if the word is
     *                       in the dictionary, this parameter will be ignored, and the array will contain a
     *                       single world.
     * @return An array of the closest entries in the dictionary to the target word
     */

    public String[] suggest(String word, int numSuggestions) {
        // FILL IN CODE
        // Note: you need to create a private suggest method in this class
        // (like we did for methods add, check, checkPrefix)
        String[] suggestions = new String[numSuggestions];
        String[] result = suggest(root, word, numSuggestions, suggestions);
        return result;
    }

    // ---------- Private helper methods ---------------

    // Moved the private suggest method to the top
    // Add a private suggest method. Decide which parameters it should have

    /*
        method name: suggest() - private helper method helps return a result array of similar words to the target word (user input)
        gets the node with the longest common prefix and sends it as an argument to recursive helper method that traverses down subtree, gets all the valid words, and returns them in a list
        if the number of valid words in longest common prefix subtree isn't enough, we go to the other children of the parent node and collect valid words from those subtrees
        if there is no common prefix then we get valid words from left to right

     */
    private String[] suggest(Node node, String word, int numSuggestions, String result[]) {
        // if word is in dictionary
        if (check(word)) {
            // create a new array with just one word and return it
            String[] result2 = new String[1];
            result2[0] = word;
            return result2;
        }
        // cases if there word is not in dictionary
        // first traverse through the subtree with the longest common prefix of word and add the children from that tree
        Node bestPrefixNode = null; // the node that has the longest common prefix with the word
        ArrayList<String> wordList = new ArrayList<>(); // arraylist that will hold the list of words being returned, ideally words in the longest common prefix subtree



        if(word.startsWith(node.prefix)){ // checks if the word starts with the node.prefix  ADDED 12:46pm
            bestPrefixNode = node;
        }

        // else
        for(Node child : node.children){ // find the node with the longest common prefix
            if(child != null){
                if(word.startsWith(child.prefix)){ // if the word starts with a nodes prefix, it has the longest common prefix
                    bestPrefixNode = child;
                }
            }
        }
        getWordsInSubtree(bestPrefixNode, "", wordList); // call recursive helper method that returns an arraylist of all the valid words within the "best prefix nodes" subtree
        int wordListIndex = 0;
        int resultIndex = 0;
        if(wordList.size() >= numSuggestions){ // case where we have enough suggestions from the longest common prefix tree, add normally to result array
            for(int j = 0; j < result.length; j++){
                result[j] = wordList.get(j);
            }
        } else if(wordList.size() < numSuggestions){ // case where the amount of words in the bestPrefixNode subtree is less than the amount of numSuggestions ( we don't have enough words filling the result array )
            int difference = numSuggestions - wordList.size(); // find the difference between amount of valid words found and the number of suggestions
            int limit = numSuggestions - difference; // get the amount of words left to add in the result array
            while(wordListIndex < limit){ // populate the array
                result[resultIndex] = wordList.get(wordListIndex);
                resultIndex++;
                wordListIndex++;
            }
            // handles the case where there's not enough valid word in the longest common prefix subtree AND if there is no common prefix
            for(Node child: node.children){ // check sibling subtree of bestPrefixNode and get the words from that
                if(child != null && child != bestPrefixNode && wordList.size() < numSuggestions){ // make sure the node being visited isn't one we've visited (bestPrefixNode)
                    getWordsInSubtree(child, "", wordList); // call recursive helper method that gets all the valid words in a subtree
                }
            }
            // fills the rest of the empty slots of result array if the # of valid words in longest common prefix subtree weren't enough and/or fills the entire result array up if there's no common prefix at all
            if(wordList.size() >= numSuggestions){
                while(wordListIndex < numSuggestions){
                    result[resultIndex] = wordList.get(wordListIndex);
                    resultIndex++;
                    wordListIndex++;
                }
            }
        }
        return result;
    }

    /*
       method name: getWordsInSubtree
       recursive helper method for suggest() that gets all the valid words within a subtree and returns them as an arraylist
       concatenates words in a String path, if a node has a word (flag == true), then we add it to the wordList
     */
    private void getWordsInSubtree(Node node, String word, ArrayList<String> words){ // gets a list of all the words in the matching prefix subtree
        if(node == null){ return; }
        word += node.prefix; // concatenates the words
        if(node.isWord){ // once a valid word is found, add it to the arrayList
            words.add(word);
        }
        for(Node child : node.children){ // traverse through the children
            if(child != null){
                getWordsInSubtree(child, word, words); // if the word is not null, recursively call method
            }
        }
    }

    /**
     *  A private add method that adds a given string to the tree
     * @param s the string to add
     * @param node the root of a tree where we want to add a new string
     * @return a reference to the root of the tree that contains s
     */
    private Node add(String s, Node node) {
        if (node == null)  { // base case: tree is null
            node = new Node();
            node.prefix = s;
            node.isWord = true;
            return node;
        }

        int indexDifference = findIndexDifference(s, node.prefix);
        if (indexDifference == node.prefix.length())  {
            if (s.length() > indexDifference) {
                int index = ((int) s.charAt(indexDifference)) - ((int) 'a');
                node.children[index] = add(s.substring(indexDifference), node.children[index]);
                return node;
            }
            node.isWord = true;
            return node;
        }

        String firstRemainder = node.prefix.substring(0, indexDifference);
        String secondRemainder = node.prefix.substring(indexDifference);

        Node newNode = new Node();
        newNode.prefix = firstRemainder;
        int index = ((int) node.prefix.charAt(indexDifference)) - ((int) 'a');
        newNode.children[index] = node;
        node.prefix = secondRemainder;

        if (indexDifference == s.length()) {
            newNode.isWord = true;
        }
        else {
            index = ((int) s.charAt(indexDifference)) - ((int) 'a');
            newNode.children[index] = add(s.substring(indexDifference), null);
        }
        return newNode ;
    }


    /** A private method to check whether a given string is stored in the tree.
     *
     * @param s the string to check
     * @param node the root of a tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean check(String s, Node node) {
        // FILL IN CODE
        // base cases
        if(node == null){ return false; } // tree is empty
        if(!s.startsWith(node.prefix)){ return false; } // prefix at root is not a prefix of word
        if(node.prefix.equals(s) && !node.isWord){ return false; } // if s is same as prefix at root but set to false, not in tree
        if(node.prefix.equals(s) && node.isWord){ return true; } // found word is in the tree
        //recursive cases
        String suffix = s.substring(findIndexDifference(node.prefix, s));
        if(!s.isEmpty()){
            int index = (int) suffix.charAt(0) - (int) 'a'; //get the proper index of the first letter of suffix in child array
            return check(suffix, node.children[index]); // check if the suffix is in the child subtree
        }else{
            return node.isWord; // if it's empty return the flag of the current node
        }
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param prefix the prefix
     * @param node the root of the tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean checkPrefix(String prefix, Node node) {
        // FILL IN CODE
        // my notes: valid if there is a leaf node with true that starts with the prefix
        // base cases
        if(node == null){ return false; }
        if(node.prefix.startsWith(prefix)){ return true; }
        //recursive case
        if(prefix.startsWith(node.prefix)){ // traverse deeper
            String suffix = prefix.substring(findIndexDifference(prefix, node.prefix));
            if(!suffix.isEmpty()){
                int index = (int) suffix.charAt(0) - (int)'a';
                return checkPrefix(suffix, node.children[index]);
            }
        }
        return false; // don't forget to change it : changed april 19th after project is pau lol used to be return node.isword
    }

    /**
     * Finds an index of the first character where strings s1 and s2 differ.
     * @param s1 the first string
     * @param s2 the second string
     * @return the index of the first character where the strings are different
     */
    private int findIndexDifference(String s1, String s2) {
        int index = 0;
        while (index < s1.length() && index < s2.length() &&  s1.charAt(index) == s2.charAt(index)) {
            index++;
        }
        return index;
    }

    // A private recursive helper method for toString
    // that takes the node and the number of indentations, and returns the tree  (printed with indentations) in a string.
    private String toString(Node node, int numIndentations) {
        StringBuilder s = new StringBuilder();
        // FILL IN CODE
        // my notes: an indentation for every new level
        // for each new child, a new level, a new indentation
        if(node == null){
            return "";
        }
        int count = 0; // loop to properly append the amount of indentations before printing a prefix
        while(count < numIndentations){
            s.append(" ");
            count++;
        }
        if(node.isWord){ // if boolean is true, there is a word so add an *
            s.append(node.prefix + "*");
        }else{
            s.append(node.prefix); // if not just append the prefix without an *
        }
        // want to append a new line after each new word is done processing
        s.append("\n");
        for(Node child : node.children){
            s.append(toString(child,numIndentations + 1)); // increment indentations by + 1 for every new level/new traversal
        }
        return s.toString();
    }

    // added the private suggest method to the top of the private methods
}
