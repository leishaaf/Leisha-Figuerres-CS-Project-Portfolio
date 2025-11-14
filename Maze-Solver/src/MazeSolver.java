/* filename: MazeSolver.java
 * author: Leisha Figuerres
 * Contains Maze and MazeSolver class
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
/* classname: Maze
 * class that reads from file that contains a maze and parses it into a 2d array
 * contains methods that work together to mark up and solve a maze as well as checking if a maze/file is valid
*/
class Maze{
	private char[][] maze;
	private int width; // columns
	private int length; // rows
	private String file;
	private int widthIndex = 0;
	private int lengthIndex = 0;
	private boolean endFound = false;
	private int posTaken = 0; // number of positions taken
	private int totalPositions = 0; // total valid positions you can take 
	private final char RIGHT = '3'; // global markers for positions 
	private final char UP = '1';
	private final char DOWN = '2';
	private final char LEFT = '4';
	/* 
	 * constructor that uses scanner and command line argument from class MazeSolver to read from a file that contains a maze. 
	 * measures each line for its length which becomes the width of the rectangle, each length of line is stores in a string that will become an array
	 * if all the elements of the width array is not the same , we print out an error for it not being rectangular and exit the program 
	*/
	public Maze(String file){
		this.file = file;
		try {
			Scanner scan = new Scanner(new File(file)); // makes scanner for reading file
			String line = ""; 
			String widthsStr = ""; 
			while(scan.hasNextLine()) { // makes the length and width for 2d array
				line = scan.nextLine();
				this.length ++; 
				this.width = line.length();
				widthsStr += width + " ";
				}
			//checks for non-rectangular mazes
			String[] widths = widthsStr.split(" ");
			int baseWidth = Integer.valueOf(widths[0]);
			for(int i = 0; i < widths.length; i++) {
				if(Integer.valueOf(widths[i]) != baseWidth) {
					System.err.println("ERROR ! Not rectangular.");
					System.exit(0);
				}
			}
			scan = new Scanner(new File(file)); // refreshes scanner so that we can read from file again and populate maze array correctly
			this.maze = new char[length][width];
			int lIndex = 0; // index for the rows
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				for(int i = 0; i < line.length(); i++) { // adds chars into maze array based on the row number and reading all the chars in that row
					maze[lIndex][i] = line.charAt(i);
				}
				lIndex ++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERROR ! File is not found.");
			System.exit(0);
		}
	}
	/* 
	 * finds the starting index which we start solving at
	 * iterates through the entire maze 2d array to check for characters that aren't allowed 
	 * counts the start and end characters to check for more invalid mazes that contain multiple S's, E's, missing one of either or missing both
	 * counts the total dashes in the maze and finds the total valid positions you can possibly take by subtracting the dashCounter from the total characters in the entire maze
	*/
	public void findStart() {
		int startCount = 0;
		int endCount = 0;
		int dashCounter = 0;
		for(int i = 0; i < length; i++) { // gets the starting index
			for(int j = 0; j < width; j++) {
				if(maze[i][j] == 'S') {
					widthIndex = j;
					lengthIndex = i;
					startCount++;
				}
				if(maze[i][j] == 'E') {
					endCount ++;
				}
				if(maze[i][j] == '-') {
					dashCounter ++;
				}
				if(maze[i][j] == 'S' || maze[i][j] == 'E' || maze[i][j] == '-' || maze[i][j] == 'O' ) { // checks for characters that aren't valid
				}else {
					System.err.println("ERROR ! Invalid Characters.");
					System.exit(0);
				}
			}
		}
		if(startCount > 1 || endCount > 1) { // handles the errors for invalid amounts of starting and ending characters S and E
			System.err.println("ERROR ! Multiple start or end characters.");
			System.exit(0);
		}else if(startCount == 0 || endCount == 0) {
			System.err.println("ERROR ! Missing a start, or end, or both");
			System.exit(0);
		}

		totalPositions = (width * length) - dashCounter;  // total valid positions you can take 
	}
	/* 
	 * method to mark and solve the maze. numbers for valid paths, and b for when we need to backtrack
	 * we backtrack if there are no other valid paths to take except for one that's already been revisited (if char is a number)
	*/
	public void solve() { // 1 for forward 2 for down, 3 for right. 4 for left , b for back tracking
		findStart();
		while(posTaken < totalPositions || !endFound) { // iterates for as long as all the positions haven't been taken and E isn't found
			if(regMovement(0, 1, RIGHT)) {
				if(endFound) { break ;}
			}else if(regMovement(-1, 0, UP)) {
				if(endFound) { break ;}
			}else if(regMovement(1, 0, DOWN)) {
				if(endFound) { break ;}
			}else if(regMovement(0, -1, LEFT)) {
				if(endFound) { break ;}
			}else if(Character.isDigit(maze[lengthIndex][widthIndex])){ // finds dead-ends
				maze[lengthIndex][widthIndex] = 'b';
			}else if(maze[lengthIndex][widthIndex] == 'b'){ // if we are at a place we can't go, we redirect if 'O' or 'E' is available or back track to a spot that we've visited before.
				if(backTrack(0, 1, RIGHT)) {
					if(endFound) { break ;}
				}else if(backTrack(-1, 0, UP)) {
					if(endFound) { break ;}
				}else if(backTrack(1, 0, DOWN)) {
					if(endFound) { break ;}
				}else if(backTrack(0, -1, LEFT)) {
					if(endFound) { break ;}
				}else {  // if none of the backtracking or redirecting paths work, there is no solution and we backtrack to start
					System.err.println("ERROR ! No solution found."); 
					System.exit(0);
				}
					
			}	else {
				if(!endFound) { // handles other cases where there is no solution like if S is not connected to an O, and more straightforward mazes
					System.err.println("ERROR ! No solution found.");
					System.exit(0);
				}else {
					System.out.println("End found.");
					break;
				}
			}
		}
	}
	/* 
	 * both regMovement() and backTrack() are very similar and structure and repeat code BUT it is for good reason!
	 * I needed them to both do the same essential thing ( move in a certain direction ), however the way I call regular movements in the beginning
	 * of solve() only looks at O's or E's. Meanwhile backTrakc() also includes if the character we're at is a digit because backtracking means going into a revisited position if no other paths are available
	 * We don't want the initial movements (regMovements) of solving to go into digits, because it would backTrack unnecessarily
	 * If I had them in the same method I wouldn't be able to change the conditional based on parameters, so I made them separate methods
	 * While they look the same, they do different things and are meant for two completely different scenarios.
	*/
	public boolean regMovement(int lengthChange, int widthChange, char marker) { // returns true if we're able to move in a certain direction and executes the movement
		int changeLengthBy = lengthChange;
		int changeWidthBy = widthChange;
		if ((lengthIndex + changeLengthBy < length &&  lengthIndex + changeLengthBy >= 0 && widthIndex + changeWidthBy < width && widthIndex + changeWidthBy >= 0) && (maze[lengthIndex + changeLengthBy][widthIndex + changeWidthBy] == 'O' || maze[lengthIndex + changeLengthBy][widthIndex + changeWidthBy] == 'E')){ // down
			if(changeLengthBy != 0) {
				lengthIndex += changeLengthBy;
			}
			if(changeWidthBy != 0) { // updates the width and length indexes based on directions
				widthIndex += changeWidthBy;
			}
			endFound = endReached(endFound, maze[lengthIndex][widthIndex]); // checks for if the end is found after every position
			if(!endFound) {
				maze[lengthIndex][widthIndex] = marker;
			}
			posTaken ++;
			return true;
		}else {
			return false;
		}
	}
	
	// back tracking method mentioned in previous bigger comment. looks for a new direction that isn't taken, but if there isn't also looks for previously visited positions. returns boolean and executes movements
	public boolean backTrack(int lengthChange, int widthChange, char marker) { 
		int changeLengthBy = lengthChange;
		int changeWidthBy = widthChange;
		if ((lengthIndex + changeLengthBy < length &&  lengthIndex + changeLengthBy >= 0 && widthIndex + changeWidthBy < width && widthIndex + changeWidthBy >= 0) && (maze[lengthIndex + changeLengthBy][widthIndex + changeWidthBy] == 'O' || maze[lengthIndex + changeLengthBy][widthIndex + changeWidthBy] == 'E' || Character.isDigit(maze[lengthIndex + changeLengthBy][widthIndex + changeWidthBy]))){ // down
			if(changeLengthBy != 0) {
				lengthIndex += changeLengthBy; // updates the width and length indexes based on directions
			}
			if(changeWidthBy != 0) {
				widthIndex += changeWidthBy;
			}
			endFound = endReached(endFound, maze[lengthIndex][widthIndex]); // checks for if the end is found after every position
			if(!endFound) {
				maze[lengthIndex][widthIndex] = marker;
			}
			posTaken ++;
			return true;
		}else {
			return false;
		}
		
	}
	
	// mini helper method called to check if the end is reached after every movement
	public boolean endReached(boolean endFound, char mark) {
		if(mark == 'E') {
			return endFound = true;
		}else {
			return endFound = false;
		}
	}
	//cleans up the maze markings to only show the successful path
	public void cleanUp() { 
		for(int i = 0; i < length; i++) { 
			for(int j = 0; j < width; j++) {
				if(Character.isDigit(maze[i][j])) {
					maze[i][j] = 'X'; // if it's a digit ( a successful visited position ), change to X
				}else if(maze[i][j] == 'b') {
					maze[i][j] = 'O'; // if backtracked position (unsuccessful), change back to O and don't include in path
				}
			}
		}
	}
	// prints the maze array using a nested for loop
	public void print() { 
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
	}
}
// class that contains main
public class MazeSolver { 
	public static void main(String[] args) { // creates Maze objects with command line arguments
		if(args.length < 1) {
			throw new IllegalArgumentException("ERROR ! No arguments.");
		}else {
			String file = args[0];
			Maze maze = new Maze(file); 
			maze.solve(); // solves and marks up the maze
			maze.cleanUp();
			maze.print(); // prints maze if there were no errors and it was solvable
		}
		
	}
}
