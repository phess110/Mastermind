import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public class MMGame implements mm {

	static String [] palette = {"Red", "Blue", "Green", "Yellow", "Pink", "Purple", "Orange"}; //General set of possible colors (7).
	static Scanner s;
	private String [] colors; 						//Subset of palette, chosen by user for pin colors.
	private ArrayList<String []> guessSet;			//Set of all possible guesses.		
	private boolean replay, keepGuessing;			//replay: decides whether to start a new game. keepGuessing: decides whether to print the next guess, or end game.
	private int numGuesses, whitepins, redpins, length;
	
	public static void main(String[] args){
		//Print instructions.
		System.out.println("Welcome to Mastermind.\n\nYour job is do create a secret code of colored pegs.\nYou can choose up to 7 colors and have a code up to 5 pins long.\nThe AI will try to guess your code. You will critique its guesses until it's correct. Enjoy!\n");
		MMGame g = new MMGame(Arrays.copyOfRange(palette, 0, getColors()), getLength());	
		String [] currGuess;
		ArrayList<String []> temp = new ArrayList<>();
		do{
			System.out.print("Make a code of length "  + g.length + " using the colors: "); //Prints available colors for user.
			printArr(g.colors);
			g.comboGen();																	//Generates an arraylist of all possible guesses.
			try {
			    Thread.sleep(4000);
			}catch(InterruptedException ex) {Thread.currentThread().interrupt();}			//Pause to let user create a code.
			
			while(g.keepGuessing){
				System.out.print("Computer's guess: ");
				currGuess = g.nextMove();
				printArr(currGuess);									//Output current guess for user.
				g.response();											//Get user response to guess.
				for(String[] i: g.guessSet){ 							//Remove incorrect guesses from guessSet, based on user input.
					if(g.compareGuess(currGuess, i))					
						temp.add(i);
				}
				g.guessSet = new ArrayList<>(temp);
				temp = new ArrayList<>();
			}
			g.playAgain();												//Ask user to play again.
		}while(g.replay);
		System.out.println("Thanks for playing.");
	}
	
	/*
	 * Mastermind game constuctor.
	 */
	public MMGame(String [] col, int codeLen){
		length = codeLen;		
		colors = col;
		replay = true;
		keepGuessing = true;
		numGuesses = 0;
		guessSet = new ArrayList<>();
	}
	
	/*
	 * Get user input for the number of colors.
	 */
	public static int getColors(){
		s = new Scanner(System.in);
		System.out.println("Please enter the number of colors you wish to use (2-7): ");
		int i = s.nextInt();
		while(i < 2 || i > 7){
			System.out.println("Invalid input. Please enter a number 2-7:");
			i = s.nextInt();
		}
		return i;
	}
	
	/*
	 * Get user input for length of code.
	 */
	public static int getLength(){
		s = new Scanner(System.in);
		System.out.println("Please enter the length of code you wish to use (2-5): ");
		int j = s.nextInt();
		while(j < 2 || j > 5){
			System.out.println("Invalid input. Please enter a number 2-5:");
			j = s.nextInt();
		}
		return j;
	}
	
	/*
	 * Gets user input on computer's guess.
	 */
	public void response() {
		s = new Scanner(System.in);
		System.out.println("Enter the number of correct colors which are in the right position: ");
		whitepins = s.nextInt();
		System.out.println("Enter the number of correct colors which are in the wrong position: ");
		redpins = s.nextInt();
		if(whitepins == length){		//Test if guess was winning guess.
				keepGuessing = false;	
				System.out.println("The computer has guessed correctly in " + this.numGuesses + " guesses. Good game.");
		}
	}
	
	/*
	 * Generates next guess.
	 * @Return the next guess.
	 */
	public String[] nextMove() {
		this.numGuesses++;
		return guessSet.get(0);	//Returns first valid move from the guessSet.
	}
	
	/*
	 * Asks user to play again.
	 */
	public void playAgain(){
		s = new Scanner(System.in);
		System.out.println("Would you like to play again? Enter Y or N:");
		String i = s.nextLine();
		if(i.substring(0, 1).equalsIgnoreCase("Y")){
			newGame();									//Call to newGame
			return;
		}
		replay = false;									//Ends the game if reached.
	}
	
	/*
	 * Starts new game, getting new input for colors and length.
	 */
	public void newGame() {
		this.colors = Arrays.copyOfRange(palette, 0, getColors());
		this.length = getLength();
		keepGuessing = true;
		numGuesses = 0;
		guessSet = new ArrayList<>();
	}
	
	
	/*
	 * Prints contents of @param array
	 */
	public static <T> void printArr(T [] arr){
		for(T i: arr)
			System.out.print(i + " ");
		System.out.println();
	}
	
	/*
	 * Generates all possible guess combinations of the array colors with code length.
	 */
	public void comboGen(){
		for(int j = 0; j < Math.pow(colors.length, length);  j++){			//Generates set of empty String arrays
			guessSet.add(new String[length]);
		}
		String [] temp;
		int index = 0;
		int k = length - 1;
		while(k > -1){
			while(index < guessSet.size()){
				for(int i = 0; i < colors.length; i++){						//Iterate through colors
					for(int j = 0; j < Math.pow(colors.length, k);  j++){	//Add to (colors.length)^k arrays, color[i], until index = (colors.length)^length, then increment k.
						temp = guessSet.get(index);							
						temp[length - 1 - k] = colors[i];
						guessSet.set(index, temp);
						index++;
					}
				}
			}
			index = 0;
			k--;
		}
	}
	
	/*
	 * Returns true if when the array, potential, is asssumed to be the correct code, it yields the same combination of white and red pins, as the user input.
	 * Returns false if they differ.
	 */
	public boolean compareGuess(String [] guess, String [] potential){
		int white = 0, red = 0;
		for(int i = 0; i < length; i++){
			for(int j = 0; j < length; j++){
				if(potential[i].equals(guess[i])){					//Iterates through counting "white pins"
					white++;
					break;
				}
				else if(i != j && potential[i].equals(guess[j])){	//Counts "red pins"
					red++;
					break;
				}
			}
		}
		return white == whitepins && red == redpins;
	}
}
