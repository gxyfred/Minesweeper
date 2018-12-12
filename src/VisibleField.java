// Name: Xiaoyu Gao
// USC NetID: xiaoyuga
// CS 455 PA3
// Fall 2018


/**
 VisibleField class
 This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
 user can see about the minefield), Client can call getStatus(row, col) for any square.
 It actually has data about the whole current state of the game, including
 the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
 It also has mutators related to moves the player could do (resetGameDisplay(), cycleGuess(), uncover()),
 and changes the game state accordingly.

 It, along with the MineField (accessible in mineField instance variable), forms
 the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
 It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from
 outside this class via the getMineField accessor.
 */
public class VisibleField {
    // ----------------------------------------------------------
    // The following public constants (plus numbers mentioned in comments below) are the possible states of one
    // location (a "square") in the visible field (all are values that can be returned by public method
    // getStatus(row, col)).

    // Covered states (all negative values):
    public static final int COVERED = -1;   // initial value of all squares
    public static final int MINE_GUESS = -2;
    public static final int QUESTION = -3;

    // Uncovered states (all non-negative values):

    // values in the range [0,8] corresponds to number of mines adjacent to this square

    public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
    public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
    public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
    // ----------------------------------------------------------

    // <put instance variables here>
    private int[][] currentState;       // currentState store the states of each square.
    private MineField myMineField;      // myMineField point to the MineField object that we want to modify and get info from.
    private int rowNum;                 // stores number of rows
    private int colNum;                 // stores number of columns
    private int guessNum;               // stores number of guessed square
    private int coveredNum;             // stores number of covered square
    private boolean explosionFlag;      // stores if a mine explode or not

    /**
     Create a visible field that has the given underlying mineField.
     The initial state will have all the mines covered up, no mines guessed, and the game
     not over.
     @param mineField  the minefield to use for for this VisibleField
     */
    public VisibleField(MineField mineField) {

        // Initialization
        explosionFlag = false;
        rowNum = mineField.numRows();
        colNum = mineField.numCols();
        currentState = new int[rowNum][colNum];
        myMineField = mineField;
        coveredNum = 0;
        // Use for for loop to set all square to covered state at the beginning.
        for(int i = 0; i < rowNum; i++) {
            for(int j = 0; j < colNum; j++) {
                currentState[i][j] = COVERED;
                coveredNum++;        // Keep recording corresponding number of covered squares.
            }
        }
        guessNum = 0;

    }


    /**
     Reset the object to its initial state (see constructor comments), using the same underlying MineField.
     */
    public void resetGameDisplay() {

        coveredNum = 0;
        explosionFlag = false;
        // Use for for loop to reset all square to covered state.
        for(int i = 0; i < rowNum; i++) {
            for(int j = 0; j < colNum; j++) {
                currentState[i][j] = COVERED;
                coveredNum++;
            }
        }
        guessNum = 0;
        myMineField.resetEmpty();

    }


    /**
     Returns a reference to the mineField that this VisibleField "covers"
     @return the minefield
     */
    public MineField getMineField() {

        return myMineField;

    }


    /**
     get the visible status of the square indicated.
     @param row  row of the square
     @param col  col of the square
     @return the status of the square at location (row, col).  See the public constants at the beginning of the class
     for the possible values that may be returned, and their meanings.
     PRE: getMineField().inRange(row, col)
     */
    public int getStatus(int row, int col) {

        return currentState[row][col];

    }


    /**
     Return the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
     or not.  Just gives the user an indication of how many more mines the user might want to guess. But the value can
     not be negative, if they have guessed more than the number of mines in the minefield.
     @return the number of mines left to guess.
     */
    public int numMinesLeft() {

        int targetMineNum = myMineField.numMines();
        // Since the value can not be negative, we need to judge if number of guessed square larger than number of mine.
        if(guessNum < targetMineNum) {
            return targetMineNum - guessNum;
        } else {
            return 0;
        }

    }


    /**
     Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
     changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
     changes it to COVERED again; call on an uncovered square has no effect.
     @param row  row of the square
     @param col  col of the square
     PRE: getMineField().inRange(row, col)
     */
    public void cycleGuess(int row, int col) {

        // Chenge the state of this square, and modify corresponding guessNum and coveredNum.
        if(currentState[row][col] == COVERED) {
            currentState[row][col] = MINE_GUESS;
            guessNum++;
            coveredNum--;
        } else if(currentState[row][col] == MINE_GUESS) {
            currentState[row][col] = QUESTION;
            guessNum--;
        } else if(currentState[row][col] == QUESTION) {
            currentState[row][col] = COVERED;
            coveredNum++;
        } else {
            return;
        }

    }


    /**
     Uncovers this square and returns false iff you uncover a mine here.
     If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in
     the neighboring area that are also not next to any mines, possibly uncovering a large region.
     Any mine-adjacent squares you reach will also be uncovered, and form
     (possibly along with parts of the edge of the whole field) the boundary of this region.
     Does not uncover, or keep searching through, squares that have the status MINE_GUESS.
     @param row  of the square
     @param col  of the square
     @return false   iff you uncover a mine at (row, col)
     PRE: getMineField().inRange(row, col)
     */
    public boolean uncover(int row, int col) {

        // If there is a mine in this square, call stateEditAfterLost() function and return false.
        if(myMineField.hasMine(row, col)) {
            stateEditAfterLost(row, col);
            return false;
        } else {
            // If there is not a mine in this square, call floodFillHelper() function and return true.
            floodFillHelper(row, col);
            return true;
        }

    }


    /**
     Returns whether the game is over.
     @return whether game over
     */
    public boolean isGameOver() {

        int targetMineNum = myMineField.numMines();
        // Game is over when player finds out all the mines, that is coveredNum + guessNum == targetMineNum,
        // or game is over when a mine explode.
        if((coveredNum + guessNum) == targetMineNum) {
            stateEditAfterWon();
            return true;
        } else if(explosionFlag) {
            return true;
        } else {
            return false;
        }

    }


    /**
     Return whether this square has been uncovered.  (i.e., is in any one of the uncovered states,
     vs. any one of the covered states).
     @param row of the square
     @param col of the square
     @return whether the square is uncovered
     PRE: getMineField().inRange(row, col)
     */
    public boolean isUncovered(int row, int col) {

        if(currentState[row][col] >= 0) {
            return true;
        } else {
            return false;
        }

    }

    // <put private methods here>

    /**
     Make some post processing for the state of each square after lost (a mine explode).
     @param row of the square
     @param col of the square
     PRE: getMineField().inRange(row, col)
     */
    private void stateEditAfterLost(int row, int col) {

        explosionFlag = true;      // Set the explosion flag to true, so I can detect the game is over.

        // Use for for loop to analysis every squares.
        for(int i = 0; i < rowNum; i++) {
            for(int j = 0; j < colNum; j++) {
                // If it is the mine that exploded, set it EXPLODED_MINE state.
                if(i == row && j == col) {
                    currentState[row][col] = EXPLODED_MINE;
                } else {
                    // If there is a mine in this square but player didn't make a guess, set it MINE state.
                    if(myMineField.hasMine(i, j) && currentState[i][j] != MINE_GUESS) {
                        currentState[i][j] = MINE;
                    }
                    // If there isn't a mine in this square but player make the mine guess, set it INCORRECT_GUESS state.
                    if((!myMineField.hasMine(i, j)) && currentState[i][j] == MINE_GUESS) {
                        currentState[i][j] = INCORRECT_GUESS;
                    }
                }
            }
        }

    }

    /**
     Recursively find which square need to be uncovered after click a non-mined square. Use flood fill algorithm.
     @param row of the square
     @param col of the square
     PRE: getMineField().inRange(row, col)
     */
    private void floodFillHelper(int row, int col) {

        currentState[row][col] = myMineField.numAdjacentMines(row, col);
        coveredNum--;      // Record corresponding number of covered squares.

        // If there are some adjacent mines, then we only need to uncover this square only,
        // but if there is no adjacent mine, then we need to uncover all adjacent squares, and judge again.
        if(currentState[row][col] != 0) {
            return;
        } else {
            // Use for for loop to go through all adjacent squares, but skip squares that 1). has already uncovered,
            // 2). is the center itself, 3). if it is out of range.
            for(int i = row - 1; i < row + 2; i++) {
                for(int j = col - 1; j < col + 2; j++) {
                    if(!myMineField.inRange(i, j)) {
                        continue;
                    } else if(i == row && j == col) {
                        continue;
                    } else if(currentState[i][j] >= 0) {
                        continue;
                    } else {
                        floodFillHelper(i, j);
                    }
                }
            }
        }

    }

    /**
     Make some post processing for the state of each square after won.
     @param row of the square
     @param col of the square
     PRE: getMineField().inRange(row, col)
     */
    private void stateEditAfterWon() {

        for(int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if(currentState[i][j] == COVERED) {
                    currentState[i][j] = MINE_GUESS;   // Set all remaining covered squares' state to MINE_GUESS.
                }
            }
        }

    }

}
