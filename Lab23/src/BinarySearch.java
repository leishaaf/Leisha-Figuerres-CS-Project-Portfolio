
/* filename: BinarySearch.java
 * author: Leisha Figuerres
 * has class BinarySearch that uses binary search method on a list of strings
*/
import java.util.Scanner;
import java.util.ArrayList;
/* classname: BinarySearch
 * class that implements binary searching with a list of names 
 * finds if user input search word is in the list or not
*/
public class BinarySearch {
	// main method: gets command line argument search word from user as well as list of names through scanner
	// turns input into list and search name into a string 
	public static void main(String[] args) {
		if(args.length == 1) {
			String word = args[0];
			ArrayList<String> strList = new ArrayList<>();
			Scanner scan = new Scanner(System.in);
			while(scan.hasNextLine()) { // user input, needs ctrl d for mac to stop
				String line = scan.nextLine();
				strList.add(line);
				
			}
			if(binarySearch(strList, word)) {
				System.out.println("MATCH");
			}else {
				System.out.println("NOT FOUND");
			}
		}else {
			System.err.println("ERROR inapproriate amount of command line args.");
		}	
		
	}
	
	// binarySearch(): method that uses binary search by using a while loop to exit if there is no match or if the word is found
	// we find the lowest and highest index which starts at 0 and list length - 1
	// create the middle index in loop (because it's constantly updating) to account for which half we take by adding low and high index and dividing by two
	// use compareTo() method to see if our searched name is before or after the string of our middleIndex 
	public static boolean binarySearch(ArrayList<String> strList, String word) {
		int lowIndex = 0;
		int highIndex = strList.size() -1;
		while(true) {
			if(lowIndex > highIndex) { // if there is no more indices left to search and the search name is not in list, break and return false
				break;
			}
			int middleIndex = (lowIndex + highIndex) / 2;
			String word2 = strList.get(middleIndex);
			if(word.equals(word2)) { // when search name is found return true
				System.out.println(word2);
				return true;
			}
			int result = word.compareTo(word2); // compareTo() from https://www.cogentuniversity.com/post/string-comparison-in-java#:~:text=The%20compareTo()%20method%20in,their%20Unicode%20values%20is%20necessary.
			if(result < 0) { // before
				highIndex = middleIndex - 1; // the highest index becomes the last of the first half
			}else if(result > 0) { // after
				lowIndex = middleIndex + 1; // the lowest index becomes the first of the second half
    	}
	}
		return false;
	}
	
}
