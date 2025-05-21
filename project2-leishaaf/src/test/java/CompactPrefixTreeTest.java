import dictionary.CompactPrefixTree;
import dictionary.Dictionary;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Test file for CompactPrefixTree class.
 *  Note that this class provides only minimal testing.
 *  You are responsible for thoroughly testing the project on your own.
 *  S20 */
public class CompactPrefixTreeTest {
    static final int NUM_SUGGESTIONS = 4;

    @Test
    public void testSimpleTree1() {
        CompactPrefixTree tree = new CompactPrefixTree();
        // create the tree shown in the project description
        tree.add("cat");
        tree.add("ape");
        tree.add("apple");
        tree.add("cart");
        tree.add("cats");
        tree.add("cat");
        tree.add("demon");
        tree.add("dog");
        tree.add("demons");

        String output = "src/test/java" + File.separator + "simpleTree1";
        tree.printTree(output);

        Path actual = Paths.get(output);  // your output
        Path expected = Paths.get("src/test/java" + File.separator + "expectedSimpleTree1"); // instructor's

        int count = 0;
        try {
            count = TestUtils.checkFiles(expected, actual);
        } catch (IOException e) {
            fail("File check failed: " + e.getMessage());
        }
        if (count <= 0)
            fail("File check failed, files are different");
    }

    @Test
    public void testSimpleTree2() {
        CompactPrefixTree tree = new CompactPrefixTree();
        // create a simple tree #2
        tree.add("liberty");
        tree.add("life");
        tree.add("lavender");
        tree.add("list");
        tree.add("youtube");
        tree.add("your");
        tree.add("you");
        tree.add("wrist");
        tree.add("wrath");
        tree.add("wristle");

        String output = "src/test/java" + File.separator + "simpleTree2";
        tree.printTree(output);

        Path actual = Paths.get(output);  // your output
        Path expected = Paths.get("src/test/java" + File.separator + "expectedSimpleTree2"); // instructor's

        int count = 0;
        try {
            count = TestUtils.checkFiles(expected, actual);
        } catch (IOException e) {
            fail("File check failed: " + e.getMessage());
        }
        if (count <= 0)
            fail("File check failed, files are different");
    }

    @Test
    public void testComplexTree() {
        // Create a tree from words in the file "words_ospd.txt"
        CompactPrefixTree tree = new CompactPrefixTree("src/main/resources/words_ospd.txt");

        String output = "src/test/java" + File.separator + "wordsTree";
        tree.printTree(output);

        Path actual = Paths.get(output);  // your output
        Path expected = Paths.get("src/test/java" + File.separator + "expectedWordsTree"); // instructor's

        int count = 0;
        try {
            count = TestUtils.checkFiles(expected, actual);
        } catch (IOException e) {
            fail("File check failed: " + e.getMessage());
        }
        if (count <= 0)
            fail("File check failed, files are different");
    }

    @Test
    public void testCheckSmallDictionary() {
        Dictionary d = new CompactPrefixTree();
        String[] words = {"cat", "cart", "dog", "apple", "ape", "breakfast", "breakneck", "queasy", "quash",
                "quail", "quick", "reason", "rickshaw", "reality"};

        String[] nonWords = {"carts", "ap", "break", "qu", "reasons", "buttercup", "under"};

        for (String word : words)  {
            d.add(word);
        }
        for (String nonWord : nonWords) {
            if (d.check(nonWord)) {
                fail("Found word: " + nonWord + ", shouldn't have");
            }
        }

        for (String word : words) {
            if (!d.check(word)) {
                fail("Failed to find word: " + word);
            }
        }
    }

    @Test
    public void testCheckLargeDictionary() {
        String filename = "src/main/resources/words_ospd.txt";
        Dictionary d = new CompactPrefixTree(filename); // added words from file to the dictionary
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // read each word from the file and check if it is in the dictionary
                if (!line.isEmpty()) {
                    if (!d.check(line)) {
                        fail("Failed to find word: " + line);
                    }
                    for (int i = 0; i <= line.length(); i++) {
                        String prefix = line.substring(0, i);
                        if (!d.checkPrefix(prefix)) {
                            fail("Failed to find prefix: " + prefix + " of " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            fail("Could not read from the file: " + e);
        }
    }

    @Test
    /** Note: this method does not check whether your suggestions are reasonable.
     * It just checks the number of suggestions and whether each suggestion is a
     * valid word in the dictionary.
     */
    public void testSuggestions() {
        System.out.println("In test Suggestions ---");
        String filename = "src/main/resources/words_ospd.txt";
        Dictionary d = new CompactPrefixTree(filename); // adds words from file to the dictionary

        String[] goodWords = {"cat", "baseball", "original"};
        for (String goodWord : goodWords)  {
            String[] result = d.suggest(goodWord, NUM_SUGGESTIONS);
            if (result == null) {
                fail("Your suggest method returned a null.");
            }
            if (result.length != 1) {
                System.out.println("Word " + goodWord + " is in the dictionary -- suggest should return 1 item");
                System.out.println("   " + result.length + " items returned instead");
                fail("Incorrect number of suggestions for " + goodWord);
            }
        }

        String[] badWords = {"accer", "fatte", "flox", "forg", "forsoom"};
        for (String badWord : badWords) {
            System.out.println("Trying " + badWord);
            String[] result = d.suggest(badWord, NUM_SUGGESTIONS);
            if (result.length != NUM_SUGGESTIONS) {
                String feedback = "Didn't get correct number of suggestions for " + badWord +
                        System.lineSeparator() + "  Expected " + NUM_SUGGESTIONS +
                        ", got " + result.length + System.lineSeparator();
                fail(feedback);
            }
            for (String suggestion : result) {
                System.out.println("  " + suggestion);
                if (!d.check(suggestion)) {
                    fail("Suggestion " + suggestion + " not in dictionary.");
                }
            }
        }
    }
}