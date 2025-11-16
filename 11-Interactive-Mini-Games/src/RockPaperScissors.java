package for_proj3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors extends Game {
	
	private int roundCount;
	private Random random;
	private int randNum;
	private String userGuess; 
	private ArrayList<String> artList;

	public RockPaperScissors() {
		super();
		random = new Random();
		artList = new ArrayList<>();
		try {
			File file = new File("rps.txt");
			Scanner scan = new Scanner(file);
			scan.useDelimiter(",");
			while(scan.hasNext()) {
				artList.add(scan.next());
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("Missing the file!");
		}
		
	}

	public String explainRules() {
		return "Welcome to Rock Paper Scissors. You will play 3 rounds against the computer by entering three choices.\nWhoever wins best out of three wins the game overall. You may play again if you wish.\nYou will chose your option by entering a number corresponding to rock, paper, or scissors.";
	}
	
	public String setup() {
	 return "Enter 1 for Rock, 2 for Paper, and 3 for Scissors.";
	}
	
	public boolean goodPlayerInput(String guess) {
		
		try {
			int numGuess = Integer.valueOf(guess);
			if(numGuess == 1 || numGuess == 2 || numGuess == 3) {
				if(numGuess == 1) {
					userGuess = "Rock";
					return true;
				}else if(numGuess == 2) {
					userGuess = "Paper";
					return true;
				}else if(numGuess == 3) {
					userGuess = "Scissors";
					return true;
				}
			}else {
				return false;
			}
		}catch (NumberFormatException e){
			return false; 
		}
		return false;
	}
	

	public String checkWinOrLose() {
		String toReturn = "";
		roundCount ++;
		toReturn = "Round " + (roundCount)+"\n";
		randNum = random.nextInt(2)+1;
		
		if(userGuess.equals("Rock") && randNum == 2) {
			toReturn += "Computer:\n";
			toReturn += artList.get(1) + "\n";
			toReturn += "You:\n";
			toReturn += artList.get(0) +"\n";
			toReturn += "You chose Rock and the computer chose paper.\n";
			toReturn += "You lost this round.\n";
		}else if (userGuess.equals("Paper") && randNum == 3) {
			toReturn += "Computer:\n";
			toReturn += artList.get(2)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(1) + "\n";
			toReturn += "You chose paper and the computer chose scissors.\n";
			toReturn += "You lost this round.\n";
		}else if(userGuess.equals("Scissors") && randNum == 1) {
			toReturn += "Computer:\n";
			toReturn += artList.get(0)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(2) + "\n";
			toReturn += "You chose scissors and the computer chose rock.\n";
			toReturn += "You lost this round.\n";
		}else if(userGuess.equals("Scissors") && randNum == 2) {
			toReturn += "Computer:\n";
			toReturn += artList.get(2)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(1) + "\n";
			toReturn += "You chose scissors and the computer chose paper.\n";
			toReturn += "You won this round!\n";
		}else if(userGuess.equals("Rock") && randNum == 3) {
			toReturn += "Computer:\n";
			toReturn += artList.get(2)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(0) + "\n";
			toReturn += "You chose rock and the computer chose scissors.\n";
			toReturn += "You won this round!\n";
		}else if(userGuess.equals("Paper") && randNum == 1) {
			toReturn += "Computer:\n";
			toReturn += artList.get(0)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(1) + "\n";
			toReturn += "You chose paper and the computer chose rock.\n";
			toReturn += "You won this round!";
		}else if(userGuess.equals("Paper") && randNum == 2) {
			toReturn += "Computer:\n";
			toReturn += artList.get(1)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(1) + "\n";
			toReturn += "You and the computer both chose paper.\n";
			toReturn += "It's a tie this round!\n";
		}else if(userGuess.equals("Scissors") && randNum == 3) {
			toReturn += "Computer:\n";
			toReturn += artList.get(2)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(2) + "\n";
			toReturn += "You and the computer both chose scissors.\n";
			toReturn += "It's a tie this round!\n";
		}else if(userGuess.equals("Rock") && randNum == 1){
			toReturn += "Computer:\n";
			toReturn += artList.get(0)+ "\n";
			toReturn += "You:\n";
			toReturn += artList.get(0) + "\n";
			toReturn += "You and the computer both chose rock.\n";
			toReturn += "It's a tie this round!\n";
		}
	return toReturn;
}
	public boolean canPlayAgain() {
		return roundCount < 3;
		
	}

}
