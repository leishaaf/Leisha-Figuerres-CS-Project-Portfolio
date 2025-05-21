package for_proj3;

import java.util.Random;


public class TicTacToe extends Game {
	
	private Random random; 
	private int placeNum;
	private int randNum;
	private boolean someoneWon;
	private String posTaken = ""; //positions taken
	private int len;
	private boolean userTurn;
	private String board1;
	private String line1;
	private String board2;
	private String line2;
	private String board3;
	
	public TicTacToe() {
		super();
		random = new Random();
		
	}
	
	public String explainRules() {
		String toReturn = "";
		toReturn += "You will play TicTacToe against the computer. You are a ♡ (heart) and the computer is a ☆ (star)\n";
		toReturn += "You will start off the game by entering a number from 1-9 based on where you want your marker to go.\n";
		toReturn += "Each number corresponds to a spot for a marker. The turns change back and forth between you and the computer.\n";
		toReturn += "The first to get three of the same kind of marker in a row diagonally, horizontally, or vertically wins and the game ends.\n";
		this.board1 = " 1 | 2 | 3 ";
		this.line1 = "-----------";
		this.board2 = " 4 | 5 | 6 ";
		this.line2 = "-----------";
		this.board3 = " 7 | 8 | 9 ";
		toReturn += board(); 
		return toReturn;
	}
	public String board() {
		String toReturn = "";
		toReturn += board1 + "\n";
		toReturn += line1 + "\n";
		toReturn += board2 + "\n";
		toReturn += line2 + "\n";
		toReturn += board3 + "\n";
		return toReturn;
	}
	
	public String setup() {
		this.userTurn = true;
		this.someoneWon = false;
		return "Please enter a number from 1-9 (Note: You can't enter a number you've already entered).";
	
	}
	
	public boolean goodPlayerInput(String guess) {
		try {
		int numGuess = Integer.valueOf(guess);
		if(numGuess < 10 && numGuess > 0 && !posTaken.contains(guess)) {
			placeNum = numGuess;
			posTaken += guess;
			return true;
		}else {
			return false;
		}
		}catch(NumberFormatException e) {
			return false;
		}
	}	
	
	public boolean comboMade() {
			//horizontal wins
			if((board1.charAt(1) == '♡' && board1.charAt(5) == '♡' && board1.charAt(9) == '♡') || (board2.charAt(1) == '♡' && board2.charAt(5) == '♡' && board2.charAt(9) == '♡') || (board3.charAt(1) == '♡' && board3.charAt(5) == '♡' && board3.charAt(9) == '♡')) {
				this.someoneWon = true;
				System.out.println("YOU WON! Type of win - Horizontal win");
				return true;
			}else if((board1.charAt(1) == '☆' && board1.charAt(5) == '☆' && board1.charAt(9) == '☆') || (board2.charAt(1) == '☆' && board2.charAt(5) == '☆' && board2.charAt(9) == '☆') || (board3.charAt(1) == '☆' && board3.charAt(5) == '☆' && board3.charAt(9) == '☆')) {
				this.someoneWon = true;
				System.out.println("The computer won! Type of win - Horizontal.");
				return true;
				//vertical wins
			}else if((board1.charAt(1) == '♡' && board2.charAt(1) == '♡' && board3.charAt(1) == '♡') || (board1.charAt(5) == '♡' && board2.charAt(5) == '♡' && board3.charAt(5) == '♡') || (board1.charAt(9) == '♡' && board2.charAt(9) == '♡' && board3.charAt(9) == '♡')) {
				this.someoneWon = true;
				System.out.println("YOU WON! Type of win - Vertical.");
				return true;
			}else if((board1.charAt(1) == '☆' && board2.charAt(1) == '☆' && board3.charAt(1) == '☆') || (board1.charAt(5) == '☆' && board2.charAt(5) == '☆' && board3.charAt(5) == '☆') || (board1.charAt(9) == '☆' && board2.charAt(9) == '☆' && board3.charAt(9) == '☆')) {
				this.someoneWon = true;
				System.out.println("The computer won! Type of win - Vertical");
				return true;
				//diagonal wins
			}else if((board1.charAt(1) == '♡' && board2.charAt(5) == '♡' && board3.charAt(9) == '♡') || (board1.charAt(9) == '♡' && board2.charAt(5) == '♡' && board3.charAt(1) == '♡')) {
				this.someoneWon = true;
				System.out.println("YOU WON! Type of win - Diagonal.");
				return true;
			}else if((board1.charAt(1) == '☆' && board2.charAt(5) == '☆' && board3.charAt(9) == '☆') || (board1.charAt(9) == '☆' && board2.charAt(5) == '☆' && board3.charAt(1) == '☆')) {
				this.someoneWon = true;
				System.out.println("The computer won! Type of win - Diagonal.");
				return true;
			}else {
				return false;
			}
	}

	public String checkWinOrLose() {
		randNum = random.nextInt(8)+1;
		this.len = posTaken.length();
		while(posTaken.contains(String.valueOf(randNum)) && !someoneWon && len != 9) {
			randNum = random.nextInt(8)+1;
		}
		posTaken += String.valueOf(randNum);
		if(userTurn) {
			for(int i = 1; i < 10; i++) {
				if(i < 4 && i == placeNum) {
					board1 = board1.replace(String.valueOf(i), "♡");
				}else if(i > 3 && i < 7 && i == placeNum) {
					board2 = board2.replace(String.valueOf(i), "♡");
				}else if(i > 6 && i < 10 && i == placeNum) {
					board3 = board3.replace(String.valueOf(i), "♡");
				}
	
		}
		
			this.userTurn = false;
		}
		
			if(!userTurn) {
				for(int i = 1; i < 10; i++) {
					if(i < 4 && i == randNum) {
						board1 = board1.replace(String.valueOf(i), "☆");
					}else if(i > 3 && i < 7 && i == randNum) {
						board2 = board2.replace(String.valueOf(i), "☆");
					}else if(i > 6 && i < 10 && i == randNum) {
						board3 = board3.replace(String.valueOf(i), "☆");
					}
	
			}	
				this.userTurn = true;
			}
			comboMade();
			return board();
		}
		
		
	public boolean canPlayAgain() {
		return len < 9 && !someoneWon;
			
	}
}
