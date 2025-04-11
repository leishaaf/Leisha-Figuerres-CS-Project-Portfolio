/* filename: FileSys.java
 * author: Leisha Figuerres
 * Contains a Tree class which has class TreeNode as a private member along with FileSys class that runs the commands from user
 * All together structures mimic a simple computer file system 
 * Project 03
*/
import java.util.ArrayList;
import java.util.Scanner;
/* classename: Tree
 * class that represents the hierarchy of files and directories in the form of a tree
 * contains class TreeNode as a member that represents actual directories and files. each tree is a directory and each child node is a sub directory
*/ 

// NOV 30TH 8:13PM BEFORE MAKING CHANGES AND LOOKING AT TAXONOMY TREE
class Tree{
	protected class TreeNode{
		StorageUnit unit;
		TreeNode parent;
		ArrayList<TreeNode> children = new ArrayList<>();
		
		TreeNode(StorageUnit input){
			unit = input;
		}
		
		public TreeNode getParent() {
			return parent;
		}
		public StorageUnit getUnit() {
			return unit;
		}
		
		public ArrayList<TreeNode> getChildren() {
			return children;
		}
	}
	
	TreeNode root;
	TreeNode currentDir; // the current directory is at first the root until cd is called
	TreeNode directoryWorkingWith;
	TreeNode currentFile; // current file user wants to work with if existent 
	
	public Tree() {
		root = new TreeNode(new Directory("/")); // the parent root of file system is /
		currentDir = root;
	}
	 // method that finds the node of the directory user wants to cd into and changes the current dir to it
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
				currentDir = child; // directory is found
				return true;
			}
			if(helperCD(child, name)) { return true; }	// checks the subnodes of children if not found in first set of parents
		}
		return false; // if no matches
	}

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
			String[] arr = name.split("/");
			for(int i = 0; i < arr.length; i++){
				if(arr[i].isEmpty()){ continue; }
				ableToCD = helperCD(currentDir, arr[i]);
				if(!ableToCD) { 
					System.out.println("ERROR invalid path");
					return false; 
				}

			}

		}
		return false;
	}
	
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
		newNode.parent = currentDir;
		newNode.parent.children.add(newNode);
	}
	
// finds if certain file name exists within a directory and sets the current file so that cat filename is better used
// iterates through the children arraylist of current directory to see if file already exists
	public boolean fileNameExists(String nameToCreate) {
		ArrayList<TreeNode> childrenList = currentDir.getChildren();
		for(int i = 0; i < childrenList.size(); i++) {
			if(childrenList.get(i).unit instanceof File && childrenList.get(i).unit.getName().equals(nameToCreate)) {
				currentFile = childrenList.get(i);
				return true;
			}
		}
		return false;
	}
	// method that uses the same method as changeDirectory() to find if a directory with a certain name exists and returns true if yes
	public boolean directoryExists(TreeNode node, String name) {
		if(node == null) {
			return false;
		}
		for(TreeNode child: node.children) {
			if(child.unit.getName().equals(name) && child.unit instanceof Directory) {
				directoryWorkingWith = child;
				return true;
			}
			if(directoryExists(child, name)) { return true; }	// checks the subnodes of children if not found in first set of parents
		}
		return false; // if no matches
	}
	// method that takes a TreeNode in to remove a certain node with a fileName called when rmdir and rm filename is called
	public void rmStorageUnit(TreeNode node) {
		ArrayList<TreeNode> nodesToRemove = new ArrayList<>();
		if(node == root) { // prevent the deletion of the root node
			System.out.println("ERROR can't remove root directory");
			return;
		}
		if(node.unit instanceof File || (node.unit instanceof Directory && node.children.size() == 0)) { // if it's a file, then it's a leaf node which is in a child array list so we remove it
			System.out.println(node.unit.getName() + " removed!");
			node.parent.children.remove(node);
			return;
		}
		if(node.unit instanceof Directory && node.children.size() >= 1) { // handles if the node we want to remove has children and if those nodes have children by using recursion
			for(TreeNode child: node.children ) {
				rmStorageUnit(child);
				
			}
			node.parent.children.remove(node);
		}

		System.out.println(node.unit.getName() + " removed!");

	}


	public boolean pwd(TreeNode node, String name){
		if(node == null) {
			return false;
		}
		for(TreeNode child: node.children) {
			System.out.print(child.unit.getName() + "/");
			if(child.unit.getName().equals(name) && child.unit instanceof Directory) {
				
				return true;
			}
			if(pwd(child, name)) { return true; }	// checks the subnodes of children if not found in first set of parents
		}
		return false; // if no matches
	}
	// private String helperPwd(TreeNode dir){
	// 	if(dir.parent == dir){
	// 		return dir.unit.getName();
	// 	}
	// 	return helperPwd(dir.parent) + dir.unit.getName() + "/";

	// }

	// public void pwd(){
	// 	String path = helperPwd(currentDir);
	// 	System.out.println(path);

	// }

	public void ls(){
		ArrayList<TreeNode> children = currentDir.children;
		// alphabetically bubble sort
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
	
		for(TreeNode node : children){
			if(node.unit instanceof Directory){
				System.out.println(node.unit.getName() + " (*)");
			}else{
				System.out.println(node.unit.getName());
			}
		}
	}
	

	
	public TreeNode getCurrentDir() {
		return currentDir;
	}
}
// class File that represents the value of each node in the class Tree and represents the behaviors and contents of what an actual file would have
class StorageUnit{
	String name;
	
	StorageUnit(String input){
		name = input;
	}
	
	public String getName() {
		return name;
	}
	
}

class File extends StorageUnit{
	ArrayList<String> contents;
	File(String input){
		super(input);
	}
	// draft of adding contents to file ?
	public void createContent(){
		ArrayList<String> fileList = new ArrayList<>();
		System.out.println("Please enter contents");
		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String lineToAdd = "";
			for(int i = 0; i < line.length(); i++) {
				for(int j = 0; j < i; j++) {
					if(line.charAt(j) ==  '~') {
						break;
					}
					lineToAdd += (line.charAt(j));
				}
			}
			fileList.add(lineToAdd);
		}
		this.contents = fileList;
	}
	public ArrayList<String> getContents(){
		return contents;
	}
}
// class Directory that contains files. still working on this, consider it as a draft
class Directory extends StorageUnit{
	Directory(String input){
		super(input);
	}	
	
}
// class FileSys where command prompt is asked and executed based on user response. for part one validation is tested.
public class FileSys {
	private static Tree system = new Tree();
	// makes a String for the user input and assign it to variable "command" and check if command contains one of the distinct commands for this project
	// validate each command, for ones that need a file name, check if there is a file name user inputs and if not throw an error
	// validate if the input by user is a command that can be executed
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String command = "";
		while(scan.hasNextLine()) {
			System.out.print("prompt> "); // asks for prompt
			command = scan.nextLine();
			String[] commandArr = command.split(" ");
			if(command.contains("create")) { // checks for distinct commands and calls method to validate commands that need a file or directory name, if errors, continue to prompting user 
				if(!validName(command, commandArr)) {continue; } else {
					if(!createFile(commandArr[1].trim())) { continue; } }
			}else if(command.contains("cat")) { // needs special handling
				if(!validName(command, commandArr)) {continue; } else {
					if(!catFile(commandArr[1].trim())) { continue; } }	
			}else if(command.contains("rm") && !command.contains("rmdir")) { // needs special handling
				if(!validName(command, commandArr)) {continue; } else {
					if(!removeFile(commandArr[1].trim())) { continue; } }
			}else if(command.contains("mkdir")) { // needs special handling
				if(!validName(command, commandArr)) {continue; } else {
					if(!makeDirectory(commandArr[1].trim())) { continue; } }
			}else if(command.contains("rmdir")) { // needs special handling
				if(!validName(command, commandArr)) {continue; } else {
					if(!removeDirectory(commandArr[1].trim())) {continue; } }
			}else if(command.contains("cd")) { // needs special handling
				if(!validName(command, commandArr)) {continue; }
				system.changeDirectory(commandArr[1].trim());
				System.out.println("AFTER CURRENT DIR " + system.currentDir.unit.getName());
			// commands that don't need file name
			}else if(command.contains("ls")) { System.out.println("Command ls received"); system.ls();// print statements so user input is on same line as prompt> when shown
			}else if(command.contains("du")) { System.out.println("Command du received");
			}else if(command.contains("pwd")) { System.out.println("Command pwd received"); system.pwd(system.root, system.currentDir.unit.getName()); System.out.println();;
			}else if(command.contains("find")) {
				if(!validName(command, commandArr)) {continue; }
			}else if(command.contains("exit")) { System.out.println(); 
				System.exit(0);
			}else if(command.isEmpty()){ System.out.println();
			 continue;
			}else { System.out.println("ERROR invalid command name");
			}
		}
	}
		

	public static boolean catFile(String name) {
		if(!system.fileNameExists(name)) {
			System.out.println("ERROR file doesn't exist");
			return false;
		}else {
			if(system.currentFile.unit instanceof File) {
				System.out.println("Contents are being printed:");
				File file = (File) system.currentFile.unit;
				ArrayList<String> contents = file.getContents();
				for(String content : contents) { 
					System.out.println(content);
				}
				return true;
			}
				
		}
		return false;
	}		
	
	public static boolean createFile(String name) {
		if(!system.fileNameExists(name)) { // check if the filename exists within the current directory before creating
			File newFile = new File(name);
			system.add(newFile);
			newFile.createContent();
			return true;
		}else {
			System.out.println("ERROR file already exists within the current directory");
			return false;
		}	
	}
	
	public static boolean removeFile(String name) {
		if(system.fileNameExists(name)) { // check if the filename exists within the current directory before creating
			system.rmStorageUnit(system.currentFile); // remove the file
			return true;
		}else {
			System.out.println("ERROR file doesn't exist");
			return false;
		}	
	}

	public static boolean removeDirectory(String name) {
		if(system.directoryExists(system.root, name)) { // check if the filename exists within the current directory before creating
			system.rmStorageUnit(system.directoryWorkingWith); // remove the file
			return true;
		}else {
			System.out.println("ERROR directory doesn't exist");
			return false;
		}	
	}

	public static boolean makeDirectory(String name) {
		if(!system.directoryExists(system.root, name)) {
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
			System.out.println("Command " + command + " received");
			return true;
		}	
	}
	
}