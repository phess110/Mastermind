# Mastermind

Text based user interface to play the game Mastermind. 

The algorithm is an adaptation of Knuth's five-guess algorithm. However, my program enables the user to select a varying number of colors and code lengths to play with.

Once the code length and colors are selected, the algorithm constructs all possible codes which may be generated with those colors. Then, it guesses the first code from the list of possible codes, and gets user feedback on the correctness of that code. If the guess is completely correct, the program asks the user if he'd like to play again. Otherwise, it utilizes the user feedback to eliminate, from the guess set, all codes that would not give the same response if they were the user's code, against which the guess was played. Finally, we guess again, and repeat until correct.

The program enables the user to choose up to 7 colors to play with and use codes up to length 5.
