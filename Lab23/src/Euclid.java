/* filename: Euclid.java
 * classname: Euclid
 * author: Leisha Figuerres
 * files that contains class Euclid that find the greatest common factor of two ints using recursion and Euclid's algorithm
*/
public class Euclid {
	// main that reads and parses two command line args as ints if the input is valid
	public static void main(String[] args) {
		if(args.length == 2) {
			try {
				int num1 = Integer.parseInt(args[0]);
				int num2 = Integer.parseInt(args[1]);
				System.out.println(GCF(num1, num2));
			}catch(NumberFormatException e) {
				System.err.println("ERROR argument must be an integer");
			}
		}else {
			System.err.println("ERROR invalid number of command line arguments, must be exactly 2 of them.");
		}
	}
	// recursive method that gets GCF of two numbers by returning at base case of the 2nd number == 0, return first number
	// otherwise, calls itself recursively
	//uses Euclid's algorithm to do this
	public static int GCF(int num1, int num2) {
		if(num2 == 0) {
			return num1;
		}
		return GCF(num2, num1 % num2);
	
	}
}
