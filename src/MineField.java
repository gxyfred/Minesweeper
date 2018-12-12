// Name: Xiaoyu Gao
// USC NetID: xiaoyuga
// CS 455 PA3
// Fall 2018

import java.util.*;
/**
 MineField
 class with locations of mines for a game.
 This class is mutable, because we sometimes need to change it once it's created.
 mutators: populateMineField, resetEmpty
 includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {

    // <put instance variables here>
    private int rowNum;      // store number of rows
    private int colNum;      // store number of columns
    private int mineNum;     // store number of mines
    private boolean[][] dataPanel;        // dataPanel[][] stores the location of mines
    private Random rand = new Random();   // generate random numbers used in populateMineField function

    /**
     Create a minefield with same dimensions as the given array, and populate it with the mines in the array
     such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
     this minefield will corresponds to the number of 'true' values in mineData.
     * @param mineData  the data for the mines; must have at least one row and one col.
     */
    public MineField(boolean[][] mineData) {

        // Initialization
        rowNum = mineData.length;
        colNum = mineData[0].length;
        dataPanel = new boolean[rowNum][colNum];
        int tempMineNum = 0;

        // Use for for loop to count how many mines in the given 2D array.
        for(int i = 0; i < rowNum; i++) {
            for(int j = 0; j < colNum; j++) {
                dataPanel[i][j] = mineData[i][j];
                if(mineData[i][j] == true) {
                    tempMineNum++;
                }
            }
        }
        mineNum = tempMineNum;

    }


    /**
     Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once
     populateMineField is called on this object).  Until populateMineField is called on such a MineField,
     numMines() will not correspond to the number of mines currently in the MineField.
     @param numRows  number of rows this minefield will have, must be positive
     @param numCols  number of columns this minefield will have, must be positive
     @param numMines   number of mines this minefield will have,  once we populate it.
     PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations).
     */
    public MineField(int numRows, int numCols, int numMines) {

        // Initialization
        rowNum = numRows;
        colNum = numCols;
        mineNum = numMines;
        dataPanel = new boolean[rowNum][colNum];

    }


    /**
     Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
     ensuring that no mine is placed at (row, col).
     @param row the row of the location to avoid placing a mine
     @param col the column of the location to avoid placing a mine
     PRE: inRange(row, col)
     */
    public void populateMineField(int row, int col) {

        int mineRemained = mineNum;
        // Keep distribute mines in dataPanel[][] until no mine left.
        while(mineRemained > 0) {
            int targetRow = rand.nextInt(rowNum);
            int targetCol = rand.nextInt(colNum);
            // Do not put mine in where the first click from player,
            // and do not put mine in where there is already a mine.
            if(targetRow == row && targetCol == col) {
                continue;
            } else if(dataPanel[targetRow][targetCol] == true) {
                continue;
            } else {
                dataPanel[targetRow][targetCol] = true;
                mineRemained--;
            }
        }
    }


    /**
     Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
     Thus, after this call, the actual number of mines in the minefield does not match numMines().
     Note: This is the state the minefield is in at the beginning of a game.
     */
    public void resetEmpty() {

        for(int i = 0; i < rowNum; i++) {
            for(int j = 0; j < colNum; j++) {
                dataPanel[i][j] = false;   // Set all dataPanel slots to false to make it empty.
            }
        }

    }


    /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
     */
    public int numAdjacentMines(int row, int col) {

        int adjacentMineNum = 0;
        // Use for for loop to count the number of adjacent mines.
        for(int i = row - 1; i < row + 2; i++) {
            for(int j = col - 1; j < col + 2; j++) {
                // Move to next adjacent location if this location is out of range, or it is the center itself.
                if(!inRange(i, j)) {
                    continue;
                } else if(i == row && j == col) {
                    continue;
                } else {
                    if(dataPanel[i][j] == true) {
                        adjacentMineNum++;
                    }
                }
            }
        }
        return adjacentMineNum;
    }


    /**
     Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
     start from 0.
     @param row  row of the location to consider
     @param col  column of the location to consider
     @return whether (row, col) is a valid field location
     */
    public boolean inRange(int row, int col) {

        if(row >= 0 && col >= 0 && row < rowNum && col < colNum) {
            return true;
        } else {
            return false;
        }

    }


    /**
     Returns the number of rows in the field.
     @return number of rows in the field
     */
    public int numRows() {
        return rowNum;
    }


    /**
     Returns the number of rows in the field.
     @return number of rows in the field
     */
    public int numCols() {
        return colNum;
    }


    /**
     Returns whether there is a mine in this square
     @param row  row of the location to check
     @param col  column of the location to check
     @return whether there is a mine in this square
     PRE: inRange(row, col)
     */
    public boolean hasMine(int row, int col) {
        return dataPanel[row][col];
    }


    /**
     Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
     some of the time this value does not match the actual number of mines currently on the field.  See doc for that
     constructor, resetEmpty, and populateMineField for more details.
     * @return
     */
    public int numMines() {
        return mineNum;
    }


}

