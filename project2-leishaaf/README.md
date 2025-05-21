## Project 2
In this project, you will implement a dictionary using a compact prefix tree.
Please see the description on Canvas.
Design your own
algorithm and describe it in the Readme.

In my suggest(), I have an array that holds the worsd to be returned ( length = numSuggestions).
It first checks if the target word is a word in the dictionary, and if so, it creates a new array of length one with just the target word and returns it. 
If not, we get the similar words by first checking the subtree of the node with the longest common prefix. I have a variable for this node called "bestPrefixNode'.
This node is found by first checking if the target word starts with the root node's prefix. If not, it iterates through the children array, finding the node whose prefix the target word starts with.
Once that node is found, it is assigned to "bestPrefixNode" and sent as a parameter to my recursive helper method getWordsInSubtree(). I also have an array list in suggest() called "wordList" that holds the list of 
valid words found in the subtrees we will check. This list is sent to getWordsInSubtree() as a parameter, as well as an empty string for the word paths to be found. My helper method traverses through the tree starting 
with the node passed to it, concatenates the valid words, and adds them to the wordList. It does this by going through the children of the node passed, and recursively calling the getWordsInSubtree() method with
children that aren't null.
The words from the wordList are then added to the result array. If we don't have enough valid words to make up the numSuggestions, we search through the siblings of the longest common prefix node (bestPrefixNode)
and get the valid words from those subtrees. It then adds the remaining amount of suggestions needed to populate the result array.
If there is no longest common prefix, the same process for if there's not enough valid words in the longest common prefix subtree will take place,
populating the result array with valid words in the children of the parent from left to right.
