/*
    * author: @leishaaf
    * filename: project02.c
    * description: Program that simulates a game called "Foxes and Squirrels" that mimics a realistic ecosystem
    * of prey and predators interacting. Essentially an animated sequence showing population change and demographics
    * My special rules:
    * - Each foxes count of eaten squirrels is counted, and they start of with a certain amount eaten
    * - If foxes don't eat they lose a squirrel
    * - They can starve and die if they don't eat enough squirrels after certain amount of rounds
    * - Neither animal reproduces if their population is signifcantly larger than the other half
    * - I create cool down periods and prevent foxes from completely over-eating squirrels by restricting eating if their population
    *   is signifcantly larger than the squirrels
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#define UP 1
#define DOWN 2
#define LEFT 3
#define RIGHT 4
#define VISITED 1
#define SQUIRRELS_EATEN 10
#define MAX_STARVING_ROUNDS (-6)
#define NUM_NEIGHBORS 8
#define INDICES 2

typedef struct { // game board
    int rows;
    int cols;
    int update;
    int squirrels;
    int foxes;
    int **eatenBoard;
    char **boardArr;
} Board;

/*
 * method: printBoard()
 * prints the board at every update along with the size of each population
 */
void printBoard(Board* board){
    printf("\033[2J\033[H"); // ascii codes so that board stays in the same place when being printed
    int squirrels = 0;
    int foxes = 0;
    for(int i = 0; i < board->rows; i++){
        for(int j  = 0; j < board->cols; j++){
            if (board->boardArr[i][j] == 's'){ squirrels++; } // count how many foxes and squirrels are currently on the board
            if (board->boardArr[i][j] == 'F'){ foxes++; }
            printf("%c", board->boardArr[i][j]);
        }
        printf("\n");
    }
    board->foxes = foxes;
    board->squirrels = squirrels;
    board->update++; // iterate the round/update number
    printf("Update #%d: %d Foxes, %d Squirrels\n", board->update, board->foxes, board->squirrels); // display the results after getting the current data after eating and mating occurs
    fflush(stdout);
}
/*
 * method: isValidMove()
 * checks all the possible directions an animal can move and changes the value of their current position through pointers if valid
 */
int isValidMove(Board* board, int *i, int *j, int direction){
    int tempI = *i; // dereference i and j to get their values, use tempI and tempJ to store them to see if safe before changing actual values of i and j
    int tempJ = *j;
    if (direction == UP){  // see if movements are safe first before actually changing the real values of i and j
        tempI--;
    }else if (direction == DOWN){
        tempI++;
    }else if (direction == LEFT){
        tempJ--;
    }else if (direction == RIGHT){ // right and down are special cases
        tempJ++;
    }
    if (((tempI < board->rows) && (tempI >= 0)) && (tempJ < board->cols) && (tempJ >= 0)){ // if we're out of bounds
        if (board->boardArr[tempI][tempJ] == '-'){ // the only spot we can move to is an empty space
            *i = tempI;
            *j = tempJ; // change the real values of i and j because movements are safe and valid
            return 1;
        }else{ return 0; }
    }
    return 0; // if we reach here, the movement is invalid
}
// creates the board used to keep track for every iteration if we've previously been at a position and we're iterating over new position
void createVisited(Board* board, char visited[board->rows][board->cols]){
    for (int i = 0; i < board->rows; i++){ // this allows us to make sure when we move down and right that we don't move that animal twice
        for (int j = 0; j < board->cols; j++){
            visited[i][j] = 0;
        }
    }
}
/*
 * method: moveAnimals()
 * function that moves all animals on the board one time per round
 * for each animal, we check all neighboring positions and randomly chose from the valid positions (after finding them) where to go
 * we get a random num that's 1-4 which determines if the animal is moving up, down, left, or right, and then move them
 * handles prevention of moving twice on moving right or down by having a visited array
 */
void moveAnimals(Board* board){
    char visited[board->rows][board->cols];
    createVisited(board, visited); // boolean that's the same length as game board and is used to keep track of if an animal already moved in the same round and we're just iterating over the new position
    for (int i = 0; i < board->rows; i++){
        for (int j = 0; j < board->cols; j++){
            if (visited[i][j] == VISITED){continue;} // check if we've moved to this position before
            if (board->boardArr[i][j] != 'F' && board->boardArr[i][j] != 's'){ continue; } // not an animal, so we don't move it
            int movedSuccessfully = 0;
            int directionsTried[] = {0,0,0,0}; // each index corresponds TO UP, DOWN, LEFT, RIGHT, array to keep track of each direction tried
            int triedCount = 0;
            while (!movedSuccessfully && triedCount < 4){
                int direction = (rand() % 4) + 1; // generate a number between 1-4 (corresponds to directions)
                if (directionsTried[direction-1] == 1){continue;} // skip if we've already tried the direction and it didn't work before
                directionsTried[direction-1] = 1;
                triedCount++; // after each direction is tried the count goes up, we keep finding new directions so long as we haven't moved successfully and we haven't gone through all four directions
                int newI = i;
                int newJ = j;
                if (isValidMove(board, &newI,&newJ, direction)){
                    char temp = board->boardArr[i][j]; // replace old animal spot with an empty space and move animal to new pos
                    board->boardArr[i][j] = '-';
                    board->boardArr[newI][newJ] = temp;
                    visited[newI][newJ] = VISITED; // mark as visited so we don't move the same animal twice during down or right movements
                    int foxTemp = board->eatenBoard[i][j]; // correlate the foxes to their new positions
                    board->eatenBoard[newI][newJ] = foxTemp;
                    board->eatenBoard[i][j] = SQUIRRELS_EATEN;
                    movedSuccessfully = 1;
                }else{
                    continue; // go to new try
                }
            }
        }
    }
}
/*
 * method: checkMatesNeighbors()
 * for every animal check all its neighbors, count if its another fox or squirrel
 * count and add the empty positions to an array of 8 positions corresponding to the spots in all neighboring directions (including digonal)
 * 0 -> UP, 1 -> DOWN, 2 -> LEFT, 3 -> RIGHT, 4 -> BOTTOM LEFT, 5 -> BOTTOM RIGHT, 6 -> UPPER LEFT, 7 -> UPPER RIGHT
 */
int checkMatesNeighbors(Board *board, char animal, int i, int j){
    int directions[NUM_NEIGHBORS][INDICES] = {{i-1, j}, {i+1,j}, {i,j-1}, {i, j+1},{i+1,j-1}, {i+1,j+1}, {i-1,j-1}, {i-1, j+1}}; // UP, DOWN, LEFT, RIGHT, BOTTOM LEFT, BOTTOM RIGHT, UPPER LEFT, UPPER RIGHT
    int sameAnimalCount = 0;
    for (int k = 0; k < NUM_NEIGHBORS; k++){ // check all the neighbors of the other mate to see if there's any extra animals of the same kind
        int tempI = directions[k][0];
        int tempJ = directions[k][1];
        if(((tempI < board->rows) && (tempI >= 0)) && (tempJ < board->cols) && (tempJ >= 0)){ // make sure it's safe to go to the neighbor position, prevent moving out of bounds
            if (board->boardArr[tempI][tempJ] == animal){
                sameAnimalCount++; // count if there's another animal of the same kind surrounding the other mate
            }
        }else{ continue;}
    }
    if (sameAnimalCount != 1){ return 0;} // if there's more than just the pair to mate around, then they're too shy to mate so return a 0
    return 1; // else make a baby
}

/* method: canMate()
 * gets the indices of the mate found in eatAndMateHelper() and calls checkMatesNeighbors() to see if there's no other animal
 * of the same kind around them
 * if so, we spawn a baby in an empty slot next to them
 */
int canMate(Board *board,int sameAnimals[],int directionEmpty[], int sameAnimal,char animal, int i, int j){ // this is where the baby spawns on the board
    int directions[NUM_NEIGHBORS][INDICES] = {{i-1, j}, {i+1,j}, {i,j-1}, {i, j+1},{i+1,j-1}, {i+1,j+1}, {i-1,j-1}, {i-1, j+1}}; // UP, DOWN, LEFT, RIGHT, BOTTOM LEFT, BOTTOM RIGHT, UPPER LEFT, UPPER RIGHT
    int neighborI = -1;
    int neighborJ = -1;
    for (int k = 0; k < NUM_NEIGHBORS; k++){ // find the indices of the neighbor that animal will mate with
        if (sameAnimals[k] == 1){
           neighborI = directions[k][0];
            neighborJ = directions[k][1];
            if (neighborI < 0 || neighborJ < 0 || neighborI >= board->rows || neighborJ >= board->cols){ continue; } // skip if invalid
            break;
        }
    }
    if (!checkMatesNeighbors(board, animal, neighborI, neighborJ)){ return 0;} // if no other animal of the same kind is around the other potential mate, don't make a baby
    for (int k = 0; k < NUM_NEIGHBORS; k++){ // we reach here if there's no animals around and this is where we actually spawn the baby or babies
        if (directionEmpty[k] == 1){ // put the baby in first empty position found
            int tempI = directions[k][0];
            int tempJ = directions[k][1];
            if(((tempI < board->rows) && (tempI >= 0)) && (tempJ < board->cols) && (tempJ >= 0)){ // safety check
                board->boardArr[tempI][tempJ] = animal;
                break;
            }
        }
    }
    return 1;
}
/* method: eatAndMateHelper()
 * helper method to eat() and share() - performs actions based on if we're in eat or mate mode
 * iterates through all the neighbors of a given animal, counts whenever there's another animal of their kind in that position, as well as the empty positions
 * if we're in eat mode, we go through each position and each time we encounter a fox, we eat it
 * if in mate mode, we use the sameAnimalCount data, the positions that were empty, and perform the actual mate execution in another helper method
 */
int eatAndMateHelper(Board *board, int i, int j, int eatMode, char animal){
    int sameAnimalCount = 0;
    int eatenCount = 0;
    // 2D array of combinations of all possible iterations to the indexes i and j in the master board array so we can check all neighboring positions without repetitive if st
    int directions[NUM_NEIGHBORS][INDICES] = {{i-1, j}, {i+1,j}, {i,j-1}, {i, j+1},{i+1,j-1}, {i+1,j+1}, {i-1,j-1}, {i-1, j+1}}; // UP, DOWN, LEFT, RIGHT, BOTTOM LEFT, BOTTOM RIGHT, UPPER LEFT, UPPER RIGHT
    int directionEmpty[NUM_NEIGHBORS] = {0,0,0,0,0,0,0,0}; // only used for mating mode, place where baby can be placed
    int sameAnimals[NUM_NEIGHBORS] = {0,0,0,0,0,0,0,0};
    for (int k = 0; k < NUM_NEIGHBORS; k++){
        // iterate through all neighboring positions (8) checking if each direction moved is valid
        // if in eat mode we check if there's a squirrel for a fox to eat, if in mate we check if it's the same animal we're given
        int tempI = directions[k][0];
        int tempJ = directions[k][1];
        if(((tempI < board->rows) && (tempI >= 0)) && (tempJ < board->cols) && (tempJ >= 0)){
            if (board->boardArr[tempI][tempJ] == '-'){directionEmpty[k] = 1; } // add to empty positions
            if (eatMode){ // eat the squirrels
                if (board->boardArr[tempI][tempJ] == 's'){
                    if (board->squirrels == 1){continue;} // don't kill of last squirrel
                    board->boardArr[tempI][tempJ] = '-'; // kill off the squirrel and erase from board
                    board->eatenBoard[i][j] += 1; // increment counter
                    eatenCount++;
                }
            }else{ // mate mode - have two animals of the same kind mate
                if (board->boardArr[tempI][tempJ] == animal){
                    sameAnimals[k] = 1;
                    sameAnimalCount++;
                }
            }
        }
    }
    if (sameAnimalCount == 1 && !eatMode){ // if in mate mode and theres exactly 2 of the same animals next to eachother, have them make a baby
         return canMate(board, sameAnimals, directionEmpty, sameAnimalCount, animal, i, j);
    }
    if (eatMode){
        return eatenCount >= 1;
    }else{
        return 0; // if we couldn't mate
    }
}

/* method: mate()
 * the animals can mate if their population isn't 2x more than their counterpart population
 * calls helper method eatAndMateHelper() that shares similar logic to have to animals mate by checking and collecting neighbor data
 */
void mate(Board *board){
    // the F next to squirrels get more prioritiy than if there is a squirrel next to fox
    for (int i = 0; i < board->rows; i++){
        for (int j = 0; j < board->cols; j++){
            //
            if (board->boardArr[i][j] != 'F' && board->boardArr[i][j] != 's') { continue; }// logic only applies to animals
            if ((board->boardArr[i][j] == 'F' && board->squirrels <= board->foxes/2) || (board->boardArr[i][j] == 's' && board->foxes <= board->squirrels/2)){ // foxes can't reproduce if their population is significantly higher than squirrels
                continue;
            }
            eatAndMateHelper(board,i, j, 0, board->boardArr[i][j]);
        }
    }
}
/* method: eat()
 * eat is higher priority than mate before my special rules
 * foxes die after starving (being in negative numbers for the amount of squirrels they've eaten) for consecutive amount of rounds
 * foxes start off with 10 squirrels in their stomachs, healthy and normal
 */
void eat(Board *board){
    for (int i = 0; i < board->rows; i++){
        for (int j = 0; j < board->cols; j++){
            if (board->boardArr[i][j] != 'F'){ continue; }// ignore all logic below if not a fox because it doesn't apply
            // checks all neighboring positions (8 of them in the square), and eats if there's a squirrel in those positions
            if (!eatAndMateHelper(board,i,j,1, board->boardArr[i][j]) && board->foxes >=  board->squirrels/4){ // calls the helper method that does the checking and execution of a fox eating
                if (board->foxes == 1) {continue;} // don't kill off the last fox
                board->eatenBoard[i][j]--; // decrement each round a squirrel eaten if they didn't eat
            }
            if (board->eatenBoard[i][j] < MAX_STARVING_ROUNDS){ // fox can was in starving mode for 6 consecutive rounds then kill them
                    board->boardArr[i][j] = '-';
            }
        }
    }
}

// frees the 2D arrays
void freeBoards(Board *board){
   for (int i = 0; i < board->rows; i++){
       free(board->boardArr[i]); // free the rows in each 2d array
       free(board->eatenBoard[i]);
   }
    free(board->boardArr);
    free(board->eatenBoard); // free the actual arrays themselves
}

// initializes the board using the data from the readFile() method
void init(Board* board, int rows, int cols, char** lines){
    board->rows = rows;
    board->cols = cols;
    board->boardArr = (char**) malloc(sizeof(char*) * rows); // malloc the rows
    board->update = 0;
    board->eatenBoard = (int**) malloc(sizeof(int*) * rows); // the eaten board that allows us to keep track of hunger levels, a factor in if fox survives or not
    for (int i = 0; i < board->rows; i++){
        board->boardArr[i] = (char*) malloc(sizeof(char) * cols); // dynamically allocate for cols
        board->eatenBoard[i] = (int*) malloc(sizeof(int) * cols);
       for (int j = 0; j < board->cols; j++){
            board->boardArr[i][j] =  lines[i][j];
       }
    }
    for(int i = 0; i < board->rows; i++){
        for(int j = 0; j < strlen(lines[i]+1); j++){ // populate the master 2d array
            board->eatenBoard[i][j] = SQUIRRELS_EATEN;
            if (lines[i][j] != 'F' && lines[i][j] != 's' && lines[i][j] != '-' && lines[i][j] != 'X'){ // invalid char, invalid board
                printf("ERROR: INVALID BOARD, FILE CONTAINS INVALID CHARACTERS.\n");
                exit(0);
            }else{ board->boardArr[i][j] = lines[i][j]; }
        }
        free(lines[i]); // free the line at the end of use
    }
}

/* method: readFile()
 * reads in the input file, validates it, and gets the correct sizes for rows and cols needed for the board
 * read line by line. get the length of line (cols)
 * get count of line (rows)
 * if one col length is diff from the set one (first row read) then its invalid board
 * if invalid chars, board is invalid
 */
void readFile(Board* board, char* file){ // create board
    FILE *fp = fopen(file, "r");\
    if (fp == NULL){
        printf("ERROR: TROUBLE OPENING FILE\n");
        exit(0);
    }
    int cols = 0; // sizes of the board
    int rows = 0;
    char line[256]; // temporary arrays to hold the lines read from the file
    char *lines[100];
    while(fgets(line, sizeof(line), fp) != NULL){ // while there's still a line to get, get line and copy it into line str
        line[strcspn(line, "\n")] = '\0'; // remove new lines https://www.geeksforgeeks.org/dsa/removing-trailing-newline-character-from-fgets-input/
        if (strlen(line) == 0){continue;}
        if (cols == 0){ cols = strlen(line)+1; } // only set the target length using the first line
        if ((strlen(line)+1) != cols){
            printf("ERROR: INVALID BOARD, LINES ARE DIFFERENT LENGTHS.\n");
            exit(0);
        }
        lines[rows] = malloc(cols);
        strncpy(lines[rows], line, cols); // put the text read from file into lines array
        rows++;
    }
    if (rows == 0){
        printf("ERROR: INVALID BOARD, FILE IS EMPTY.\n");
        exit(0);
    }
    init(board, rows, cols, lines); // make the 2d array
}

int main(int argc, char **argv) {
    if(argc > 1){
        Board board;
        Board* boardPtr = &board;
        readFile(boardPtr, argv[1]); // calls init in this method
        if (board.boardArr == NULL){return -1;} // invalid board/problem reading file
        srand(time(NULL));
        while (1){ // runs until user forcibly quits
            printBoard(boardPtr);
            sleep(1); // for animation
            if (board.squirrels >= board.foxes / 3){ eat(boardPtr);} // the foxes don't eat if their population is 3x more than the squirrels to simulate a cooldown period
            mate(boardPtr);
            moveAnimals(boardPtr);
        }
        freeBoards(boardPtr); // for good measure
    }else{
        printf("ERROR: No arguments provided.\n");
    }
    return 0;
}