package dictionary;
/** The Driver class for CompactPrefixTree */
public class Driver {
    public static void main(String[] args) {
            Dictionary dict = new CompactPrefixTree();
            dict.add("cat");
            dict.add("cart");
            dict.add("carts");
            dict.add("case");
            dict.add("doge");
            dict.add("doghouse");
            dict.add("wrist");
            dict.add("wrath");
            dict.add("wristle");

            System.out.println("Testing out toString():");
            System.out.println(dict.toString());

//            // Add other "tests"
            System.out.println("Testing check():");
            System.out.println("Is 'cat' a valid word in dict? " + dict.check("cat"));
            System.out.println("Is 'ca' a valid word in dict? " + dict.check("ca"));
            System.out.println("Is 'wristle' a valid word in dict? " + dict.check("wristle"));
            System.out.println("Is '' a valid word in dict? " + dict.check(""));
            System.out.println("Testing checkPrefix():");
            System.out.println("Is 'ca' a prefix in dict? " + dict.checkPrefix("ca"));
            System.out.println("Is 'cat' a prefix in dict? " + dict.checkPrefix("cat"));
            System.out.println("Is 'dog' a prefix in dict? " + dict.checkPrefix("dog"));
            System.out.println("Is 'hey' a prefix in dict? " + dict.checkPrefix("hey"));
            System.out.println("Is '' a prefix in dict? " + dict.checkPrefix(""));
            System.out.println("Testing printTree() by writing to file called 'results.txt:'");
            // creating a compact tree to test printTree()
            CompactPrefixTree tree = new CompactPrefixTree();
            tree.add("hey");
            tree.add("he");
            tree.add("ol");
            tree.add("olive");
            tree.add("olivia");
            tree.add("lo");
            tree.add("love");
            tree.add("loved");
            tree.add("loves");
            tree.add("lover");
            tree.printTree("results.txt");
            // testing suggest()
            System.out.println("Testing Suggest():");
            System.out.println("word = 'ca' & numSuggestions input = 3");
            String result[] = dict.suggest("ca", 3);
            for(String str : result){
                    System.out.println(str);
            }
            System.out.println("word = 'ca' & numSuggestions input = 4");
            String result2[] = dict.suggest("ca", 4);
            for(String str : result2){
                    System.out.println(str);
            }
            System.out.println("word = 'ca' & numSuggestions input = 6, exceeds the amount of valid words in the best subtree");
            String result3[] = dict.suggest("ca", 9);
            for(String str : result3){
                    System.out.println(str);
            }
            System.out.println("word = 'dog' & numSuggestions input = 3");
            String result4[] = dict.suggest("dog", 3);
            for(String str : result4){
                    System.out.println(str);
            }
            System.out.println("word = 'dog' & numSuggestions input = 7, exceeds the amount of valid words in the best subtree");
            String result5[] = dict.suggest("dog", 7);
            for(String str : result5){
                    System.out.println(str);
            }
            System.out.println("word = 'wr' & numSuggestions input = 3");
            String result7[] = dict.suggest("wr", 3);
            for(String str : result7){
                    System.out.println(str);
            }
            System.out.println("word = 'wr' & numSuggestions input = 5, exceeds the amount of valid words in the best subtree");
            String result8[] = dict.suggest("wr", 5);
            for(String str : result8){
                    System.out.println(str);
            }
            System.out.println("word = 'hey' & numSuggestions input = 4, no common prefix with any of the nodes");
            String[] result9 = dict.suggest("hey", 4);
            for(String str : result9){
                    System.out.println(str);
            }
            System.out.println("word = 'cat' & numSuggestions input = 5, already in dictionary");
            String[] result10 = dict.suggest("cat", 5);
            for(String str : result10){
                    System.out.println(str);
            }

            // There is a file with words words_ospd.txt in src/main/resources
    }
}
