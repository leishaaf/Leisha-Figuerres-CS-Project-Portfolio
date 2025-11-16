/* filename: Poker.java
 * author: Leisha Figuerres
 * Contains Poker class that simulates a player
*/
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;
import java.util.ArrayList; 
/* Class: Poker
 * Simulates player and makes decisions based on commands
 * Contains a testing and socket mode
 * Uses methods to make decisions based on command program receives 
 * My strategy is fairly conservative but has a balance
 */
public class Poker {
	private static boolean socketMode = false; // class variable level boolean that determines if socket gets created or not
	private static Scanner scan = new Scanner(System.in);
	// prepares for communications with the dealers and calls methods based on commands  and has socket and testing mode
	public static void main(String[] args){
		String ipAddress = "";
		String ipPort = "";
		if(args.length >= 1) { // if there are command line arguments, set socketMode to true
			socketMode = true;
			ipAddress = args[0];
			ipPort = args[1];
			System.out.println("Going into socket mode...");
		}else {
			socketMode = false;
			System.out.println("Going into test mode..."); // if there's no command line arguments go into test mode (which reads from scanner) and mimics dealer
		}
		callCommands(ipAddress, ipPort); // calls method that parses all the different commands from dealer
	}
	// method that parses and categorizes what player should do based on command from dealer
	public static void callCommands(String ipAddress, String ipPort) {
		try {
			String command = ""; // string to hold what is read from the dealer
			String response = ""; // string that will be written to dealer and or printed in test mode 
			Socket socket = null; // sets socket, data input stream and data output streams to null so they're not activated yet until socketMode is true
			DataInputStream dis = null;
			DataOutputStream dos = null;
	
			if(socketMode) { // if socket mode is true create socket, dis, and dos
				socket = new Socket(ipAddress, Integer.parseInt(ipPort));
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			}
		
			while(true) { // while true, read the commands from the dealer or scanner (if test mode). this is where command is deciphered and response is constructed accordingly
				command = read(dis);
				String[] commandArr = command.split(":"); // whatever is read from dealer or scanner is split into an array called commandArr
				if(command.contains("login")) { // responds with github id and avatar name
					response = "leishaaf:topdawg";
					write(response, dos);
				}
				if(command.contains("bet1")){ 
					System.out.println("bet1 being called");
					String[] bet1 = command.split(":"); // splits the bet one command into items in an array for easier use when calculating it
					System.out.println(command);
					response = calcBet1(bet1); // calls bet one method that implements my logic and assigns what is returned to "response" which will be written to dealer 
					write(response, dos);
				}
				if(command.contains("bet2")){
					System.out.println("bet2 being called");
					String[] bet2 = command.split(":"); // splits the bet two command into items in an array for easier use when calculating it
					System.out.println(command);
					response = calcBet2(bet2); // calls the calculating bet2 method and assigns the return value of it to "response" which will be written to dealer
					write(response, dos);
				}
				if(command.contains("status")) { // doesn't reply to dealer, just parses message and prints
					System.out.println("MESSAGE: " + command);
				}
				if(command.contains("done")) { // doesn't reply to dealer also just parses message and prints
					if(socketMode) {
						socket.close();
					}
					 // ends game and communication with dealer
					System.out.println("MESSAGE: " + command);
					System.out.println("Recieved done command, program terminating...");
					return;
				}
			}
		}catch (NumberFormatException e){
			return;
		}catch(IOException e) {
			return;
		}
	}
	//write method that writes to socket if socketMode boolean is true and else (test mode), simply print the message instead of writing it to anyone
	private static void write(String s, DataOutputStream dos) throws IOException {
		if(socketMode) {
			dos.writeUTF(s);
			dos.flush();
		}else {
			System.out.println(s);
		}
	}
	//read method that reads from the dealers data input stream or from scanner based on if socketMode is true or not
	private static String read(DataInputStream dis) throws IOException {
		String result = "";
		if(socketMode) {
			result = dis.readUTF();
		}else {
			result = scan.nextLine();
		}
		return result; // either way a command is returned to CallCommands() and used in betting logic 
	}
	
	//calculates bet 1 and returned whose result will be the response to dealer, considers other players upCards as well as your hole and 1st face card, and the value of your hand 
	public static String calcBet1(String[] betVals) {
		// uses the array of the bet 1 command to distinguish my program/players hole card, and 2 face cards based on index
		boolean yesBet = false; // boolean used to determine if player should raise or not
		int holeCard = readCard(betVals[4]); // prints the card and their value - obtained by going through the readCard method (returns value)
		System.out.println("bet 1 hole card is " + holeCard);
		int faceCard = readCard(betVals[5]);
		System.out.println("bet 1 face card is " + faceCard);
		int totalHandVal = faceCard + holeCard;
		System.out.println("total bet 1 hand val is " + totalHandVal);
		String result = "bet:"; // template of what will be returned as a response to dealer
		int betMatched = 0; // the integer value of the bet we plan to return 
		int yourMoney = Integer.parseInt(betVals[1]);
		System.out.println("chips in stack " + yourMoney);
		int betVal = Integer.parseInt(betVals[3]);
		int difference = yourMoney - betVal; // is used to determine if player has enough money to make bet or not
		int highCard = 0;
		if(holeCard > faceCard) { // get the higher card out of the two dealer has given you so it can be printed
			highCard = holeCard;
		}else {
			highCard = faceCard;
		}
		yesBet = read1stUpCards(yesBet, betVals, highCard); // decides if you should raise bet or not by reading other players up cards
		if(totalHandVal < 15) { // if hand is not as high as a 10-5 (15), we fold
			result = "fold";
			return result;
		}else {
			if(yesBet) { 
				betMatched = Integer.parseInt(betVals[3]) + 1;
				if(yourMoney - betMatched <= 0) { // if we don't have enough to raise bet by one, fold
					result = "fold";
					return result;
				}else {
					result += betMatched; // if we have the higher hand, raise the bet by one
					return result;
				}
			}
		}
		// statement is reached if total hand value of your face card and hole card is 14 + and it was determined you don't have the highest hand. so we don't raise but still bet and instead match the current value 
		if(Integer.parseInt(betVals[3]) != 0){ // if person raised bet, match their bet
			System.out.println("Money - bet = " + difference);
			if(difference <= 0) {
				System.out.println("You dont have enough"); // checks if you have enough to bet
				result = "fold";
				return result;
			}else {
				betMatched = Integer.parseInt(betVals[3]);
			}
			System.out.println("Bet matched = " + betVal);
				
		}else {
			betMatched = 0; // bet zero if no other person raised bet and it was determined we didn't have the highest hand
			result += Integer.toString(betMatched);
			return result;
		}
			result += betVal;
			System.out.println(result);
			return result;
	}
	
	// method that is called in calcbet1() to read all the other players up cards and see if their cards are higher than our highest card and returns a boolean based on it 
	public static boolean read1stUpCards(boolean yesBet, String[] betVals, int highCard) {
		ArrayList<Integer> firstUpCards = new ArrayList<>(); // uses an array list for the players up cards because we can't determine the amount of players every game
		int maxCard = 0;
		for(int i = 7; i < betVals.length; i++) { // adds values in the up cards array by going through bet vals and starting at index 7 (index of first players up card)
			firstUpCards.add(readCard(betVals[i]));
			System.out.println("This player's up card " + (readCard(betVals[i]))); // uses read card method to return the value of other plays card and prints it
		}
		for(int i = 0; i < firstUpCards.size(); i++) { // gets the max card in the players up card array 
			if(firstUpCards.get(i) > maxCard) {
				maxCard = firstUpCards.get(i);
			}
		}
		if(highCard >= maxCard) { // if our highest card is higher than or equal to the highest up card of all the other players, then yesBet is returned as true and in calcbet1() our player will raise the bet by 1
			yesBet = true;
		}else {
			yesBet = false; // if false, in calcbet1() if our total hand value is 14 or more, our player will match the current bet if it's not 0
		}
		return yesBet;
	}
	
	// method that is called within calcBet2() when we have a triple or a pair and makes sure bet response player gives is legal
	public static String validBet2(int yourMoney, int amountBet, int betVal) {
		String result = "bet:";
		if(yourMoney <= 0 || yourMoney - betVal <= 0) { // if our money - the minimum bet value is less than or equal to 0, fold immediately 
			result = "fold";
			return result;
		}else {
			if(yourMoney - amountBet > 0) { // if we have enough money to bet the amounts for pairs and triples determined in calcBet2(), then we return it to the method
				result += Integer.toString(amountBet);
				return result;
			}
			while(yourMoney - amountBet <= 0 && amountBet != 0 && yourMoney > 0){ // if we don't have enough for the amounts we want for pairs and triples, and the minimum betVal is not 0, bet the most we can without our money going to 0
				amountBet -= 1; // has to be atleast one
			}

			System.out.println("Didn't have enough to bet default so betting the most you can: amount you most can = " + amountBet);
			result += Integer.toString(amountBet);
			return result;
			
		}
	}
	// calculates bet value for bet 2 with my playing strategy 
	public static String calcBet2(String betVals[]) {
		String result = "bet:";
		int amountBet = 0; // int used to be the amount we want to bet
		boolean yesBet = true; // boolean used to determine different cases of where a player should bet, fold, or bet less depending on our hand and others up cards
		String yourHand = ""; // either a triple, double, or average (average hand I want to bet if its equal to or more than is queen-6-4 )
		int yourMoney = Integer.parseInt(betVals[1]); // index of chips in stack
		int betVal = Integer.parseInt(betVals[3]); // index of bet value 
		int holeCard = readCard(betVals[4]); // hole card val index 4 bc 5th in array
		System.out.println("hole card VALUE " + holeCard);
		int faceCard1 = readCard(betVals[5]); // face card val index 5 bc 6th in array (First up card)
		System.out.println("face card 1 VALUE " + faceCard1);
		int faceCard2 = readCard(betVals[6]); //face card 2 val index 6 bc 7th in array (Second up card)
		System.out.println("face card2 VALUE " + faceCard2);
		int totalHandVal = holeCard + faceCard1 + faceCard2; // adds the total hand values together and is used to compare later on 
		//highest hands will be handled first and returned as soon as they are found
		//highest hand, three of a kind 
		System.out.println();
		if(holeCard == faceCard1 && faceCard1 == faceCard2) { //triple , bets high either way and takes a chance, if it's guaranteed highest hand (using readUpCards) then bet all, if it's not then increase bet by 2 instead of by 6
			yourHand = "triple";
			yesBet = readUpCards(betVals, yesBet,totalHandVal,yourHand, holeCard);
			if(yesBet) {
				amountBet = betVal + 6; // raises bet by 6 if it's guaranteed we have the highest hand
			}else {
				amountBet = betVal + 2; // raises if it's not guaranteed we have the highest hand, but we can't read other players third card so we just raise by 2 because there's still a chance
			}
			result = validBet2(yourMoney, amountBet, betVal); // checks if bet value is an illegal amount
			return result;
		}
		// deals with pairs
		if(holeCard == faceCard1 || holeCard == faceCard2 || faceCard1 == faceCard2) {
			yourHand = "pair";
			if(holeCard == faceCard1) {
				yesBet = readUpCards(betVals, yesBet,totalHandVal,yourHand, holeCard);
			}else if(faceCard1 == faceCard2){
				yesBet = readUpCards(betVals, yesBet,totalHandVal,yourHand, faceCard1);
			}else if (holeCard == faceCard2){
				yesBet = readUpCards(betVals, yesBet,totalHandVal,yourHand, faceCard2);
			}
			// if our pair is higher than (or the only) the other pairs in read in readUpCards, yesBet is true and we raise the bet by 1
			if(yesBet) {
				amountBet = betVal + 1; // if i get a pair that's higher than 
				result = validBet2(yourMoney, amountBet, betVal); // checks if bet value is an illegal amount
				return result;
	
			}else {
				result = "fold"; // if it's not the highest pair, we fold
				return result;
			}
		}
		// if there are no pairs or three of a kind, bet or fold based on highest card scenario (my "highest card strategy" focuses on value of total hand and if it's higher enough to bet at all. takes care of the "highest card" scenario if there are no pairs or triples 
		// strategy : average hand I want to bet on is queen-6-4 and above (learned from watching videos and researching) queen val = 12, so 12 + 6 + 4 = 22
		if(totalHandVal < 22 && totalHandVal > 1) { // fold if total hand value is less than 22 queen-6-4)
			System.out.println("FOLDING - Total hand val " + totalHandVal + ". Lower than queen-6-4");
			result = "fold";
			return result;
		// if our hand is queen-6-4 or more, matches bet and doesn't raise in all circumstances. even if bet is zero and hand is higher than or equal 22 because I am playing it more safe and raise if I have pairs or triples
		}else if(totalHandVal >= 22){
			yourHand = "average"; 
			yesBet = readUpCards(betVals, yesBet,totalHandVal,yourHand, holeCard);
			if(yesBet) {
				amountBet = betVal; // matches bet
				System.out.println("Your money  - betVal = " + (yourMoney - betVal));
				if(yourMoney <= 0 || yourMoney - betVal <= 0) { // checks for illegal betting
					System.out.println("Don't have enough money to match. folding...");
					result = "fold";
					return result;
				}else {
					result += Integer.toString(amountBet); 
					System.out.println("Total value is " + totalHandVal);
					System.out.println("Total hand is 22 or more. Matching Bet Bet 2 value is " + result);
				}	
			}else {
				result = "fold";
			}
		}
		return result;
	}
	
	// derived from MyCardDeck.java but modified , only pays attention to getting value not suit and returns it for a single card
	public static int readCard(String betVal){
		int value = 0; // card value
		String c1 = "";
		String c2 = "";
		c1 = Character.toString(betVal.charAt(0)); //makes strings for 1st and 2nd char 
		c2 = Character.toString(betVal.charAt(1)); // second char is only created for the case that the number of the card is 10
		
		if(Character.isDigit(betVal.charAt(0))) { // if the char is digit it parses it into an int and sets values and suits accordingly
			int val = Integer.parseInt(c1);
			if(val >= 2 && val <= 9) {
				value = val;
				
			}else if(c1.equals("1") && c2.equals("0")) { // handles the special case if the value is 10 
				value = 10;
		
			}
		}else if(Character.isAlphabetic(betVal.charAt(0))) { // if the 1st char isn't numeric and instead alphabetic, then it must be a jack, queen, king, or ace.
			if(c1.equals("J")) {
				value = 11;
			}else if(c1.equals("Q")) {
				value = 12;
			}else if(c1.equals("K")) {
				value = 13;
			}else if(c1.equals("A")) {
				value = 14;
			}
		}
		return value;
	 }
	//betting based on the players up cards. checks if players total up card hand value is higher than yours
	public static boolean readUpCards (String[] betVals, boolean yesBet, int totalHandVal, String yourHand, int pairOrTripleCard) {
		ArrayList<Integer> players1stUpCards = new ArrayList<Integer>(); // array list of all the other players first up cards
		ArrayList<Integer> players2ndUpCards = new ArrayList<Integer>(); // array list of all the other players second up cards
		ArrayList<Integer> allPairValues = new ArrayList<Integer>(); // array list for if there is any pair values in other players upCards
		ArrayList<Integer> allHandVals = new ArrayList<Integer>(); // array list of the total hand value of the players up cards
		int yourPairVal = 0;
		for(int i = 8; i < betVals.length; i++) { // reads players up cards
			if(i % 2 == 0) { // if even, players first up card
				int cardVal = readCard(betVals[i]);
				players1stUpCards.add(cardVal);
			}else {
				int cardVal = readCard(betVals[i]); // if odd, its the players second up card
				players2ndUpCards.add(cardVal);
			}
		}
		//print up cards, finds pairs, and initializes array list values
		for(int i = 0; i < players1stUpCards.size(); i++) {
			yourPairVal = pairOrTripleCard * 2; // card from pair or triple in our hand 
			System.out.println("YOUR pair value is " + yourPairVal + " and hole card is  " + pairOrTripleCard);
			int pairVal = players1stUpCards.get(i) + players2ndUpCards.get(i); // total hand value of upCards
			System.out.print("Players 1st up card value is " + players1stUpCards.get(i) + " and Players 2nd up card is " + players2ndUpCards.get(i));
			if(players1stUpCards.get(i) == players2ndUpCards.get(i)) {
				System.out.print(". Pair found." + "\n");
				allHandVals.add(pairVal); // adds to pair value arraylist
				allPairValues.add(pairVal);
			}else {
				allHandVals.add(pairVal); // adds to all handsVals and not pair
			}
			System.out.println(" Pair val " + pairVal);
		}
		// if our hand in calcBet2() is a pair or triple, then we determine if there's any other pairs in the players up cards and see if ours is the highest
		if(yourHand.equals("pair") || yourHand.equals("triple")) { 
			int maxPair = 0; // gets the max value of other pairs , if there aren't any, value will remain 0
			for(int i = 0; i < allPairValues.size(); i++) {
				if(allPairValues.get(i) > maxPair){
					maxPair = allPairValues.get(i);
				}
			}
			System.out.println("Your pair val is " + yourPairVal + " Players max val is " + maxPair);
			if(yourPairVal >= maxPair) { // if we have the highest or only pair, we return yesBet to be true. if we have a triple in calcBet2(), we'll bet higher and if it's not the highest pair, we'll still bet just a little less
				yesBet = true; 			// if we have the highest pair than all the other players and our hand is determined to be pair in calcBet2(), then we bet 
				return yesBet;
			}else {
				yesBet = false; // if we have a pair and it's not the highest, we fold. if we have a triple and it's not the highest yesBet is set to false but we still take a chance and bet, as mentioned previously 
				return yesBet;
			}
		}else if(yourHand.equals("average")) { // if there's no pairs or triples in my hand, this clause handles the queen-6-4 hand or higher hand. it prevents user from betting if any of the the other players two up card values is greater than the total hand of the players three cards value
			int maxPair = 0;
			for(int i = 0; i < allHandVals.size(); i++) { // gets the highest value of the other players 2 up cards and compares to our whole hand (all three of our cards)
				if(allHandVals.get(i) > maxPair){
					maxPair = allHandVals.get(i);
				}	
			}
			System.out.println("YOUR TOTAL HAND VAL = " + totalHandVal);
			//decides when you should DEFINETLY FOLD by measuring if there's at least one another pair are higher than the total hand of your 3 cards
			if(totalHandVal < maxPair) {
				yesBet = false;
			}
		}
		return yesBet;
	}
}