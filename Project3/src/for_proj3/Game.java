package for_proj3;

public abstract class Game {

	
	public abstract String explainRules();
	
	public abstract String setup();
	
	public abstract boolean goodPlayerInput(String guess);
	
	public abstract String checkWinOrLose();
	
	public abstract boolean canPlayAgain();

}
