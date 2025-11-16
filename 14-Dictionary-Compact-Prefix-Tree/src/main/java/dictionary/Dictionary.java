package dictionary;

/** Dictionary ADT with extra functionality: the ability to make spelling suggestions.
 *  You may not change anything in this interface. */
public interface Dictionary {

    /** Adds a given word to the dictionary
     * @param word The word to add to the dictionary
     */
    void add(String word);

    /**
     * Checks if a given word is in the dictionary
     * @param word The word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    boolean check(String word);

    /**
     * Checks if a given prefix is stored in the dictionary
     * @param prefix The prefix of a word
     * @return true if this prefix is a prefix of any word in the dictionary,
     * and false otherwise
     */
    boolean checkPrefix(String prefix);


    /**
     * Returns an array of "suggestions" - the closest entries in the dictionary to
     * the target word
     * @param word the target word
     * @param numSuggestions the number of suggestions to return
     * @return the array with suggestions
     */
     String [] suggest(String word, int numSuggestions);

}
