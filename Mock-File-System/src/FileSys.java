/* filename: FileSys.java
 * author: Leisha Figuerres
 * Contains a Tree class which has class TreeNode as a member along with FileSys class that runs the commands from user
 * TreeNode class takes the value of StorgageUnit which is the medium type of Directory and File (both classes extend Storageunit)
 * All together structures mimic a simple computer file system 
 * Project 03
*/
import java.util.ArrayList;
import java.util.Scanner;
/* classename: Tree
 * class that represents the hierarchy of files and directories in the form of a tree
 * contains class TreeNode as a member that represents actual directories and files
 * keeps track of root, currentDir (current directory, only changes when cd is called), directoryWorkingWith (whatever directory node we're trying to perform on, and currentFile (file we want to execute command on)
 * contains methods that help execute commands. methods work closely with methods called in FileSys.java that test out the tree
*/ 
class Tree{
	/* classename: TreeNode
 	* stores type StorageUnit to represent files and directories
 	* contains arraylist of children 
	*/ 
	protected class TreeNode{
		protected StorageUnit unit;
		private TreeNode parent;
		protected ArrayList<TreeNode> children = new ArrayList<>();
		
		TreeNode(StorageUnit input){
			unit = input;
		}
	}
	
	protected TreeNode root;
	protected TreeNode currentDir; // the current directory is at first the root until cd is called
	protected TreeNode directoryWorkingWith; 
	protected TreeNode currentFile; // current file user wants to work with if existent 
	
	public Tree() {
		root = new TreeNode(new Directory("/")); // the parent root of file system is / 
		currentDir = root;
	}
	 // method helperCD() that finds the node of the directory user wants to cd into and changes the current dir to it
	// if not found then we return false, if found we update and return true;
	private boolean helperCD (TreeNode node, String name) {
		if(name.equals("..")){
			currentDir = node.parent; // change to the parent of directory if they want to cd into previous directory
			return true;
		}
		if(node == null) {
			return false;
		}
		for(TreeNode child: node.children) {
			if(child.unit.getName().equals(name) && child.unit instanceof Directory) {
				currentDir = child; // directory is found so update the currentDir class variable
				return true;
			}
			if(helperCD(child, name)) { return true; }	// checks the subnodes of children if not found in set of parents
		}
		return false; // if no matches
	}

	// method changeDirectory() that calls recursive method helperCD() which helps account for multiple path scenarios
	// breaks up the command from user by / and calls helperCD for each of the separate commands
	public boolean changeDirectory(String name){
		boolean ableToCD = true;
		if(name.equals("/")){ //change the cd to root to be itself
			currentDir = root;
			return true;
		}else{
			if(name.startsWith("/")){ 
				currentDir = root;
				ableToCD = helperCD(currentDir, "/");
			}
			String[] arr = name.split("/"); // splits the command 
			for(int i = 0; i < arr.length; i++){
				if(arr[i].isEmpty()){ continue; } // ignores where the split happens and replaces / with " "
				ableToCD = helperCD(currentDir, arr[i]);
				if(!ableToCD) { 
					System.out.println("ERROR invalid path");
					return false; 
				}
			}
		}
		return false;
	}
	
	// method add() that properly adds a new treenode to tree based on if the unit of its value is a File type or Class type
	// checks if those values already exist within current directory by calling their corresponding hel[per methods
	public void add(StorageUnit value) {
		TreeNode newNode = new TreeNode(value);
		if(newNode.unit instanceof File) {
			if(fileNameExists(value.getName())) { return; } 
		}
		if(newNode.unit instanceof Directory) {
			if(directoryExists(currentDir, value.getName())) {
			 return;
			}
		}
		newNode.parent = currentDir; // whatever new file or directory is added in, it must have a parent which is the currentDir. if no cd's has happened then parent is root directory
		newNode.parent.children.add(newNode); 
	}
	
	// method fileNameExists() finds if certain file name exists within a directory and sets the current file so that cat filename is better used
	// iterates through the children arraylist of current directory to see if file already exists
	public boolean fileNameExists(String nameToCreate) {
		ArrayList<TreeNode> childrenList = currentDir.children;
		for(int i = 0; i < childrenList.size(); i++) {
			if(childrenList.get(i).unit instanceof File && childrenList.get(i).unit.getName().equals(nameToCreate)) {
				currentFile = childrenList.get(i);
				return true;
			}
		}
		return false;
	}

	// method directoryExists() that uses the similar method to changeDirectory() to find if a directory with a certain name exists and returns true if yes
	// sets the class variable directoryWorkingWith just in case the command we're using needs another directory other than current or we can compare if it should or should not equal the currentDir
	public boolean directoryExists(TreeNode node, String name) {
		if(node == null) {
			return false;
		}
		for(TreeNode child: node.children) { // transverse through the children of the current directory and if the name matches the node we're searching for, set the class variable and return 
			if(child.unit.getName().equals(name) && child.unit instanceof Directory) {
				directoryWorkingWith = child;
				return true;
			}
			if(directoryExists(child, name)) { return true; }	// checks the subnodes of children if not found in first set of parents
		}
		return false; // if no matches
	}

	// method rmStorageUnit() that takes a TreeNode in to remove a certain node with a fileName called when rmdir and rm filename is called
	public void rmStorageUnit(TreeNode node) {
		if(node == root) { // prevent the deletion of the root node
			System.out.println("ERROR can't remove root directory");
			return;
		}
		if(node.unit instanceof File || (node.unit instanceof Directory && node.children.size() == 0)) { // if it's a file, then it's a leaf node which is in a child array list so we remove it
			node.parent.children.remove(node);
			return;
		}
		if(node.unit instanceof Directory && node.children.size() >= 1) { // handles if the node we want to remove has children and if those nodes have children by using recursion
			for(TreeNode child: node.children ) { // erases all its children 
				rmStorageUnit(child);
				
			}
			node.parent.children.remove(node);
		}
	}

	// method that finds the current path of the current directory by starting at the current directory and getting the parent until the parent is the root directory
	// each directory in the path while transversing to the parent is added to an array list
	// the path is then printed by printing the arraylist backwards because that is the correct order how the path to current dir
	public void pwd(TreeNode node){
		TreeNode pointer = node;
		ArrayList<TreeNode> directories = new ArrayList<>();
		while(pointer != null && pointer != root){
			directories.add(pointer);
			pointer = pointer.parent;
		}
		directories.add(root); // add in the root as the last element because that's where we start from and it's not populated in while loop since its condition checks for pointer to not equal root
		for(int i = directories.size() - 1; i >= 0; i--){
			System.out.print(directories.get(i).unit.getName());
			if(i != 0 && directories.get(i) != root){ // ensures not printing an extra slash for root node
				System.out.print("/");
			}
		}
	}

	// method findName() that uses recursion to go through the subdirectories of the subdirectories in the current directory to find the name user is searching for
	// in any case if name is found then we call pwd on that node and the current path is printed
	public void findName(TreeNode node, String name){
		for(TreeNode child : node.children){
			if(child.unit.getName().equals(name)){ 
				pwd(child);
				System.out.println();
			}
			if(child.unit instanceof Directory){
				findName(child,name);
			}
		}
		return; 
		
	}

	// method du() that is called within FileSys and calls the recursive method helperDu
	public void du(){
		System.out.println(helperDu(currentDir));

	}

	// recursive method to get the disk usage of the current directory. transverses through nodes starting from current directory.
	// if the node is a type of file, get the size by calling getSize() and add that to the counter for usage
	// if it's an instance of directory, then call the function with the child directory of the current instead and find the files (if any) in that directory to add it's disk usage
	public int helperDu(TreeNode node){
		if(node == null){
			return 0;
		}
		int usage = 0;
		if(node.unit instanceof File){
			usage += getSize((File)node.unit);
		}
		if(node.unit instanceof Directory){
			for(TreeNode child: node.children){
				usage += helperDu(child);
			}
		}
		return usage;
	}

	// method getSize() that returns the size of the contents arraylist in a file type which contains the number of characters in each file
	private int getSize(File file){
		return file.getContents().size();
	}

	// method ls() called in FileSys that alphetically prints the directories and files within the current directory 
	public void ls(){
		ArrayList<TreeNode> children = currentDir.children;
		// alphabetically bubble sort, (i wanted to practice)
		for(int i = children.size() - 1; i > 0; i--) {
			for(int j = 0; j < i; j++) {
				TreeNode temp = children.get(j+1);
				int result = children.get(j).unit.getName().compareTo(temp.unit.getName()); 
				if(result >= 1) {
					children.set(j+1, children.get(j));
					children.set(j, temp);
				}
			}
		}
		// prints the list of children
		for(TreeNode node : children){
			if(node.unit instanceof Directory){
				System.out.println(node.unit.getName() + " (*)");
			}else{
				System.out.println(node.unit.getName());
			}
		}
	}

}
// class StorageUnit - parent type for File and Directory so that TreeNode value can have one set type
class StorageUnit{
	private String name;
	
	StorageUnit(String input){
		name = input;
	}
	
	public String getName() {
		return name;
	}
	
}

// class File that represents the value of each node in the class Tree and represents the behaviors and contents of what an actual file would have
class File extends StorageUnit{
	private ArrayList<Character> contents;
	File(String input){
		super(input);
	}
	public void setContent(ArrayList<Character> inputs){
		this.contents = inputs;
	}
	public ArrayList<Character> getContents(){
		return contents;
	}
}
// class Directory 
class Directory extends StorageUnit{
	Directory(String input){
		super(input);
	}	
	
}
// classname : FileSys
// where user prompts are called and commands are processed. contains main and additional methods that help methods in Tree class to execute a full command 
public class FileSys {
	private static Tree system = new Tree();
	// makes a String for the user input and assign it to variable "command" and check if command contains one of the distinct commands for this project
	// validate each command, for ones that need a file name, check if there is a file name user inputs and if not throw an error
	// validate if the input by user is a command that can be executed
	// *** I know this method is long but with the way my functions were structured to be booleans, and we continue if they're false, I had to keep it the way it was
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String command = "";
		while(scan.hasNextLine()) {
			System.out.print("prompt> "); // asks for prompt
			command = scan.nextLine();
			String[] commandArr = command.split(" ");
			if(command.contains("create")) { // checks for distinct commands and calls method to validate commands that need a file or directory name, if errors, continue to prompting user 
				if(!validName(command, commandArr)) {continue; } else {
					if(!createFile(commandArr[1].trim(), scan)) { continue; } }
			}else if(command.contains("cat")) { 
				if(!validName(command, commandArr)) {continue; } else {
					if(!catFile(commandArr[1].trim())) { continue; } }	
			}else if(command.contains("rm") && !command.contains("rmdir")) { 
				if(!validName(command, commandArr)) {continue; } else {
					if(!removeFile(commandArr[1].trim())) { continue; } }
			}else if(command.contains("mkdir")) { 
				if(!validName(command, commandArr)) {continue; } else {
					if(!makeDirectory(commandArr[1].trim())) { continue; } }
			}else if(command.contains("rmdir")) { 
				if(!validName(command, commandArr)) {continue; } else {
					if(!removeDirectory(commandArr[1].trim())) {continue; } }
			}else if(command.contains("cd")) { 
				if(!validName(command, commandArr)) {continue; }
				system.changeDirectory(commandArr[1].trim());
			}else if(command.contains("ls")) { System.out.println(); 
				system.ls();// print statements so user input is on same line as prompt> when shown
			}else if(command.contains("du")) { System.out.println(); 
				system.du();
			}else if(command.contains("pwd")) { System.out.println(); 
			system.pwd(system.currentDir); 
			System.out.println();
			}else if(command.contains("find")) {
				if(!validName(command, commandArr)) {continue; } 
				else { system.findName(system.currentDir, commandArr[1].trim()); }
			}else if(command.contains("exit")) { System.out.println(); 
				System.exit(0);
			}else if(command.isEmpty()){ System.out.println();
			 	continue;
			}else { System.out.println("ERROR invalid command name"); // if user doesn't enter a valid command then throw an error
			}
		}
	}
	
	// method catFile() that prints all the contents in a File object
	public static boolean catFile(String name) {
		if(!system.fileNameExists(name)) {
			System.out.println("ERROR file doesn't exist");
			return false;
		}else {
			if(system.currentFile.unit instanceof File) { // prints out character by character
				File file = (File) system.currentFile.unit;
				ArrayList<Character> contents = file.getContents();
				if(contents != null && !contents.isEmpty()){
					for(char content : contents) { 
						System.out.print(content);
					}
					System.out.println();
					return true;
				}
			}
		}
		return false;
	}		
	
	// method createFile() that first checks if file already exists in current directory
	// if it doesn't then create the file and allow user to enter contents, then add it to the tree as well
	public static boolean createFile(String name, Scanner scan) {
		if(!system.fileNameExists(name)) { // check if the filename exists within the current directory before creating
			File newFile = new File(name);
			ArrayList<Character> contents = new ArrayList<>(); // array list to hold characters that user will input into file 
			boolean looping = true;
			while(looping && scan.hasNextLine()){ 
				 String word = scan.nextLine();
				 String[] arr = word.split("");
				 outloop: // idea to label outerloops to break from innerloop is from stack overflow 
				 for(int i = 0; i < arr.length; i++){
				 	for(int j = 0; j < arr[i].length(); j++){
				 		if(arr[i].charAt(j) == '~'){  // break if a tilda is detected
				 			looping = false;
				 			break outloop;
				 		}
				 		contents.add(arr[i].charAt(j));
				 	}
				 }
			}
			system.add(newFile); // add file to tree
			newFile.setContent(contents); // set the contents we collected to the newFile class variable of an arraylist of characters
			return true;
		}else {
			System.out.println("ERROR file already exists within the current directory");
			return false; // return false and move on to next prompt if file exists
		}	
	}
	
	// method removeFile() that checks if file exists or not and if not, call rmStorageUnit() which removes given treenode based on its units name
	public static boolean removeFile(String name) {
		if(system.fileNameExists(name)) { // check if the filename exists within the current directory before creating
			system.rmStorageUnit(system.currentFile); // remove the file
			return true;
		}else {
			System.out.println("ERROR file doesn't exist");
			return false;
		}	
	}

	// method removeDirectory() that checks if directory exists and if so, we can proceed to call rmStorageUnit() to remove directory and its children
	public static boolean removeDirectory(String name) {
		if(system.directoryExists(system.currentDir, name)) { // check if the filename exists within the current directory before creating
			boolean isInCurrent = false;
			if(system.directoryWorkingWith.unit.getName().equals(system.currentDir.unit.getName())){ // handles case where user tries to cd into the current directory 
				System.out.println("ERROR can't cd into current directory");
				return false;
			}
			system.rmStorageUnit(system.directoryWorkingWith); // remove the file
			return true;
		}else {
			System.out.println("ERROR directory doesn't exist");
			return false;
		}	
	}

	// method makeDirectory() that makes a Directory object and assigns it to the unit of a TreeNode (adds it to tree)
	public static boolean makeDirectory(String name) {
		if(!system.directoryExists(system.currentDir, name)) { // checks if directory already exists first
			Directory newDir = new Directory(name);
			system.add(newDir); 
			return true;
		}else {
			System.out.println("ERROR directory already exists within tree");
			return false;
		}
	}

	// method validName() that checks if user inputted a file or directory name for commands that need a file or directory name 
	public static boolean validName(String command, String[] commandArr) {
		if(commandArr.length < 2) {
			System.out.println("ERROR command " + command + " needs a filename or directory to perform command on!");
			return false;
		}else {
			System.out.println();
			return true;
		}	
	}
	
}