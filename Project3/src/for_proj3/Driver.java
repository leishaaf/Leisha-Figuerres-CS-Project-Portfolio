package for_proj3;


import java.util.Scanner;

public class Driver {
	
	public static void checkQuit(String response) {
		String response1 = response.toUpperCase();
		if(response1.equals("QUIT")) {
			System.out.println("Goodbye!");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		
		boolean playing = true;
		while(playing) {
			System.out.println("Welcome to the CS 186 Gaming System.\n"
					+ "Enter 1 to play Rock, Paper, Scissors,\n" // You CAN change this menu item, if you're not doing Spaceship
					+ "Enter 2 to play TicTacToe,\n" // You CAN change this menu item, if you're not doing Cho-Han
					+ "Or enter QUIT at any time to end the game.");
			try {
				Scanner in = new Scanner(System.in);
				String input = in.nextLine();
				checkQuit(input.toUpperCase());
				int choice = Integer.parseInt(input);
				Game game;
				if (choice == 1) {
					game = new RockPaperScissors(); // You CAN change this line, if you're not doing Spaceship
				}else if (choice == 2) {
					game = new TicTacToe(); // You CAN change this line, if you're not doing ChoHan
				}else {
					System.out.println("Please only enter 1 or 2.");
					continue;
				}
				System.out.println(game.explainRules());
				boolean playingThisGame = true;
				while(playingThisGame) {
					System.out.println(game.setup());
					String response1 = in.nextLine();
					checkQuit(response1);
					boolean goodInput = game.goodPlayerInput(response1);
					while(!goodInput) {
						System.out.println("Sorry, bad input. Please try again.");
						String response2 = in.nextLine();
						checkQuit(response2);
						goodInput = game.goodPlayerInput(response2);
					}
					String output = game.checkWinOrLose();
					System.out.println(output);
					boolean playAgain = game.canPlayAgain();
					if(!playAgain) {
						playingThisGame = false;
						System.out.println("Your game is done. Goodbye!");
					}
				}
				in.close();
				playing = false;
			}catch(NumberFormatException ne) {
				System.out.println("You must enter a number.");
			}
		}
	}

}