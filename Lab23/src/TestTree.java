/*********************************************************
 * filename: TestTree.java
 * author Paul Haskell and Leisha Figuerres for student methods
 * A file that builds a binary tree, fills it with
 *  integers read from a file, and does some operations
 *  on the tree, printing the results.
 *********************************************************/

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Scanner;


/** class Tree
 *
 * A binary tree data structure that stores TreeNodes.
 * Can:
 *	add new TreeNodes,
 *	sum all values in the tree,
 *	find minimum value in the tree,
 *	find total number of entries in the tree,
 *	find maximum depth of the tree.
 */
class Tree {
	/** class TreeNode - a private member class of Tree
	 *
	 * A node in class Tree.
	 * Stores a value, and references to zero, one, or two children.
	 */
	private class TreeNode {
		int value;
		TreeNode left;
		TreeNode right;

		TreeNode(int v) {
			value = v;
			left = null;
			right = null;
		}
		int value() { return value; }
	}

	//////
	////// Class members of Tree
	//////
	TreeNode root;

	//////
	////// Class methods
	//////
	public Tree() {
		root = null;
	}

	/** Add an int value to the tree
	 * @param input the int value to be added to the tree
	 */
	public void add(int input) {
		TreeNode n = new TreeNode(input);

		if (root == null) {
			root = n;
			return;
		}
		add(root, input);
	}

	private void add(TreeNode top, int v) {
		// Put the new node into the tree, in "sorted order".
		// New nodes are added "to the left" of existing nodes
		// with greater values and "to the right" of nodes
		// with smaller values.
		//
		// 'top' should not be null
		if (v > top.value()) {
			if (top.right == null) {
				top.right = new TreeNode(v);
			} else {
				add(top.right, v);
			}
		} else {
			// STUDENTS FILL IN CODE HERE
			if(top.left == null) { // add new tree node to the left with the smaller values
				top.left = new TreeNode(v);
			}else {
				add(top.left, v);
			}
		}
	}

	public int sum() {
		return sum(root);
	}
	//method sum() that adds of all the integer values in the tree and returns it
	private static int sum(TreeNode n) {
		// STUDENT FILL IN CODE HERE
		if(n == null) {
			return 0; // there's no values left to add so return 0
		}
		return sum(n.left) + sum(n.right) + n.value(); // recursively call sum with left and right value, add those values, and return
		
	}
	public int size() {
		return size(root);
	}
	
	//method size() that returns the number of entries in the tree
	private static int size(TreeNode n) {
		// STUDENT FILL IN CODE HERE
		if(n == null) { // if we reach the last node, return 0 because there's no nodes left to add
			return 0;
		}
		return size(n.left) + size(n.right) + 1; // adds one for to count for every node 
	}

	public int depth() {
		return depth(root);
	}
	// method depth() that returns the "maximum depth" of the tree - the height or the number of nodes from the root down to farthest leaf node
	// finds the last leaf, longest side is either on left or right side and follows down the longer path
	private static int depth(TreeNode n) {
		// STUDENT FILL IN CODE HERE
		if(n == null) { // base case if we're at the end of tree
			return 0;
		}
		int leftNodes = depth(n.left); // make counters for the left and right side to see where the longest path could be
		int rightNodes = depth(n.right);
		if(leftNodes > rightNodes) { // if left nodes is longer continue searching down and add one for the node we're at
			return leftNodes + 1;
		}else {
			return rightNodes + 1; // same thing ^^ with right
		}
	}

	/** @return minimum value stored in the tree.
	 *
	 * If the tree is empty, throw an Exception.
	 */
	public int min() throws Exception {
		if (root == null) { throw new Exception("TreeMin() called with null arg"); }
		// STUDENT FILL IN CODE HERE
		TreeNode temp = root; 
		while(temp.left != null) { // a sorted tree has smallest values on left so get the last value of the left side for the smallest val
			temp = temp.left;
		}
		return temp.value; // return the smallest value
	}
}

public class TestTree {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("ERROR: need a filename");
			System.exit(0);
		}
		Tree myTree = new Tree();
		// STUDENTS FILL IN CODE TO READ INTS FROM A FILE WHOSE
		// FILENAME IS IN args[0] AND ADD THEM TO THE TREE.
		try {
			String input = args[0]; 
			File file = new File(input); // reading through file of integers
			Scanner scan = new Scanner(file);
			while(scan.hasNext()) {
				myTree.add(Integer.parseInt(scan.next()));
			}
			scan.close();
		}catch(FileNotFoundException e){ // error handling for non-existent files
			System.out.println("ERROR file not found");
		}
		System.out.println("Sum is " + myTree.sum());
		System.out.println("Min is " + myTree.min());
		System.out.println("Size is " + myTree.size());
		System.out.println("Depth is " + myTree.depth());
	}
}