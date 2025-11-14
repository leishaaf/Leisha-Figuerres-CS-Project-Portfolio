/*
Author: @leishaaf 
Date: Sept 19th, 2025
Desc: Program that simulates the capture style of mancala where two people can play against eachother
using keyboard input
*/

#include <stdio.h>
#include <stdlib.h>
#define NUM_ROWS 2 
#define NUM_COLS 6
#define A_SIDE 1
#define B_SIDE 0

struct Mancala {
	int scoreA; 
	int scoreB;
	int board[NUM_ROWS][NUM_COLS]; // to represent the bins
	int gameOver;
};

void init(struct Mancala* board);
int calculateTurn(struct Mancala* board, int playerBTurn, int move);
void printScore(struct Mancala* board);
void printBoard(struct Mancala* board);
int switchTurns(struct Mancala* board, int playerBTurn);
void gameOver(struct Mancala* board);
void handleCapture(struct Mancala* board, int* playerBTurn, int* seedsToDrop, int* row, int* col);

void init(struct Mancala* board){
	board->scoreA = 0;
	board->scoreB = 0;
	board->gameOver = 0;
	for(int i = 0; i < NUM_ROWS; i++){ // initialize bins and scoring basins
		for(int j = 0; j < NUM_COLS; j++){
			board->board[i][j] = 5; 
		}
	}
}

// method: gameOver() - handles checking for if a game ends (either players row is entirely empty) and is called after every turn. If so, we compute the scores 
void gameOver(struct Mancala* board){
	int emptyA = 1; // assume they're empty
	int emptyB = 1;
	int scoreATemp = 0;
	int scoreBTemp = 0;
	for(int j = 0; j < NUM_COLS; j++){
		if(board->board[B_SIDE][j] != 0){ // B 
			emptyB = 0; // not empty
			break;
		}
	}
	for(int j = 0; j < NUM_COLS; j++){
		if(board->board[A_SIDE][j] != 0){ // A
			emptyA = 0;
			break;
		}
	}
	if(emptyA || emptyB){ // if there's still seeds in one of the basins 
		if(emptyA){ // if player A basins are empty 
			for(int j = 0; j < NUM_COLS; j++){
				board->scoreB += board->board[B_SIDE][j];
				board->board[B_SIDE][j] = 0; // clear out bin
			}
		}
		if(emptyB){ // if player B basins are empty 
			for(int j = 0; j < NUM_COLS; j++){
				board->scoreA += board->board[A_SIDE][j];
				board->board[A_SIDE][j] = 0; // clear out bin
			}
		}
		board->gameOver = 1; // the game is over so set flag to true
	}
}

// method: calculateTurn() handles the distribution of seeds, wrap arounds, and manages turns. calls helper methods handleCapture() and noSeeds()
int calculateTurn(struct Mancala* board, int playerBTurn, int move){ // calc score, edge case wrap ard, print score, double digits
	int row, col;
	if(!playerBTurn){ // player A
		row = A_SIDE;
		col = move -1;
	}else{ // player B
		row = B_SIDE;
		col = NUM_COLS - move; // get correct corresponding index of 2D array from users basin. reversed so you - move
	}
	int seedsToDrop = board->board[row][col];
	if(seedsToDrop == 0){
		printf("You chose a non-empty bin. Please chose again.\n");
		return playerBTurn;
	}
	printf("Seeds to drop: %d\n", seedsToDrop);
	board->board[row][col] = 0;
	while(seedsToDrop > 0){
		if(row == A_SIDE){ // wraparound potentially
			col++; 
			if(col > 5){ // if player a side is over
				if(playerBTurn == 0){ // skip over player A's basin if it's player B's turn
					board->scoreA++;
					seedsToDrop--;
				}
				if(seedsToDrop == 0){ 
					printf("You get an extra turn!!!\n");
					return playerBTurn;
				}
				row = B_SIDE; // wraparound and start on player B's side on the end
				col = 5;
			}
		}else{ // B_SIDE
			col--;
			if(col < 0){ // if we go past end of B's side it's neg bc we subtract from cols to move 
				if(playerBTurn == 1){ // skip over player B's basin if it's player A's turn
					board->scoreB++;
					seedsToDrop--;
				}
				if(seedsToDrop == 0){ 
					printf("You get an extra turn!!!\n");
					return playerBTurn;
				}
				row = A_SIDE; // wrap around and start at end of A's side 
				col = 0;
			}
		}
		if(seedsToDrop > 0){
			board->board[row][col]++; // drop seed
			seedsToDrop--;
			if(seedsToDrop == 0 && ((!playerBTurn && row == A_SIDE) || (playerBTurn && row == B_SIDE)) && board->board[row][col] == 1){ // capture
				handleCapture(board, &playerBTurn, &seedsToDrop, &row, &col);
			}
		}
	}

	return playerBTurn == 0 ? 1 : 0; // if playerB == 0 (player A's turn), return 1 to switch and vice versa	
}

// method: handleCapture() - called when a player's last seed drop is in an empty array. Takes the seeds of other players corresponding basin 
void handleCapture(struct Mancala* board, int* playerBTurn, int* seedsToDrop, int* row, int* col){
	if(*playerBTurn){ // playerB
		if(board->board[A_SIDE][*col] != 0){ // first check if opposite side corresponding bin is empty
			printf("Capture!!!\n");
			board->scoreB += board->board[*row][*col]; // add the one seed to score
 			board->scoreB += board->board[A_SIDE][*col];
 			board->board[A_SIDE][*col] = 0; // clear out corresponding opposite side & add to score
		}else{
			return;
		}
	}else{ // playerA
		if(board->board[B_SIDE][*col] != 0){ 
			printf("Capture!!!\n");
			board->scoreA += board->board[*row][*col]; 
 			board->scoreA += board->board[B_SIDE][*col];
 			board->board[B_SIDE][*col] = 0; 
		}else{
			return;
		}
	}
	board->board[*row][*col] = 0; // clear out bin of curr user
}

// method: printScore() - prints the final score after a game is finished
void printScore(struct Mancala* board){ 
	printf("Final Score:\n");
	printf("%s %d\n", "A:", board->scoreA);
	printf("%s %d\n", "B:", board->scoreB);
}

// method: printBoard() - prints the board using 2D member array variable of pits
void printBoard(struct Mancala* board){ // print after every turn // print the bins and numbers to rep spots in its own
	printf("%6d%4d%4d%4d%4d%4d\n",6,5,4,3,2,1); // print beginning indices that represent turns 
	printf("B:"); // 1st row of board 
	for(int j = 0; j < NUM_COLS; j++){
		printf("%4d", board->board[0][j]);
	}
	printf("\n");
	printf("%d%26c%4d\n", board->scoreB, ' ', board->scoreA); // 2nd row of board 
	for(int j = 0; j < NUM_COLS; j++){
		if(j == 0){
			printf("%6d", board->board[1][j]);
		}else{
			printf("%4d", board->board[1][j]);
		}
	}
	printf("%5s\n", ":A");
	printf("%6d%4d%4d%4d%4d%4d\n",1,2,3,4,5,6);  // print ending indices that represent turns 
}

// method: switchTurns() - handles input validation and the switching between the two players turns
int switchTurns(struct Mancala* board, int playerBTurn){ 
	int move;
	int result;
	do {
		if(!playerBTurn){
			printf("Player A, make a play! Please enter a digit from 1-6 representing a nonempty basin for your turn: ");
		}else{
			printf("Player B, make a play! Please enter a digit from 1-6 representing a nonempty basin for your turn: ");
		}
		result = scanf("%d", &move);
		if(result == EOF){
			printScore(board);
			_Exit(1); // exit program
		}else if(result != 1){ // if input is invalid
			printf("Please enter a digit from 1-6.\n");
			char ch;
			// while loop - https://stackoverflow.com/questions/7898215/how-can-i-clear-an-input-buffer-in-c
			while ((ch = getchar()) != '\n' && ch != EOF); // free buffer from bad input
		}else if(move >= 1 && move <= 6){ // makes sure all digital input is within range of basins dislpayed to user
			playerBTurn = calculateTurn(board, playerBTurn, move);
			printBoard(board);
		}else{
			printf("Invalid input. Please enter a positive digit from 1-6.\n");
		}
	} while (result != 1 || (move < 1 || move > 6)); // keep going unitl input is valid
	return playerBTurn;
}

int main(){ 
	struct Mancala board;
	struct Mancala* boardPtr = &board;
	init(boardPtr);
	int playerBTurn = 0; // starts on player A flag
	int inputNotValid = 0;
	printBoard(boardPtr);
	while(!boardPtr->gameOver){
		playerBTurn = switchTurns(boardPtr, playerBTurn); // input validation happens in switch turns
		gameOver(boardPtr);
	}
	printf("Final Board: \n");
	printBoard(boardPtr); // print the final board
	printScore(boardPtr); // print final score 
	return 0;
}