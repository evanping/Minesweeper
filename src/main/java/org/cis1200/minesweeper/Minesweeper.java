package org.cis1200.minesweeper;

import java.util.Random;
import java.util.Stack;

/**
 * This class is a model for Minesweeper.
 * This game adheres to a Model-View-Controller design framework.
 */
public class Minesweeper {

    /*
    Board states:
    0+ -> number of mines (visible)
    -1 -> mine
    -2 -> empty (non-visible)
    -3 -> flagged (visible)
     */
    private int[][] board;
    private int[][] visibleBoard;
    private int numMines;
    private boolean gameActive;
    private Stack<int[][]> boardStack;
    private boolean firstTurn; // whether the grid has been "broken" yet

    /**
     * Constructor sets up game state.
     */
    public Minesweeper() {
        reset();
    }

    /**
     * playTurn allows player to clear a tile. If the tile has value 0
     * the method recurses through nearby tiles, uncovering all 0 tiles
     *
     * @param c column to play in
     * @param r row to play in
     * @param newBoard whether to save the current board to the stack
     */
    public void playTurn(int r, int c, boolean newBoard) {
        // check valid coords and game status
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
            return;
        } else if (!gameActive) {
            return;
        }

        // create board layout if first turn
        if (firstTurn) {
            generateMines(r, c);
            populateBoard();
            firstTurn = false;
        }

        int currTile = board[r][c];

        // check if already revealed or if flagged remove flag
        if (visibleBoard[r][c] == currTile) {
            return;
        } else if (visibleBoard[r][c] == -3) {
            visibleBoard[r][c] = -2;
            return;
        }

        // add current board onto board history stack
        if (newBoard) {
            saveBoardToStack();
        }

        visibleBoard[r][c] = currTile;
        if (currTile == -1) {
            gameActive = false;
        // base case: currTile != 0
        } else if (currTile == 0) { // DFS and uncover other 0 numbered tiles
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        playTurn(r + i, c + j, false);
                    }

                }
            }
        }

        if (checkWinner()) {
            gameActive = false;
        }
    }


    /**
     * takes the current visibleBoard and saves a copy to the stack
     */
    public void saveBoardToStack() {
        int[][] boardCopy = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boardCopy[i][j] = visibleBoard[i][j];
            }
        }
        boardStack.push(boardCopy);
    }

    /**
     * places a flag on the visibleBoard if the clicked tile is uncovered
     *
     * @param c column to play in
     * @param r row to play in
     */
    public void playFlag(int r, int c) {
        // check valid coords and game status
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
            return;
        } else if (!gameActive) {
            return;
        }

        if (visibleBoard[r][c] == -2) {
            saveBoardToStack();
            visibleBoard[r][c] = -3;
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * Checks to see if tile does not contain bomb, it must be uncovered.
     *
     * @return true if game is won, false if game is still going
     */
    public boolean checkWinner() {
        int curr;
        int hidden;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                curr = visibleBoard[r][c];
                hidden = board[r][c];
                if ((curr == -2 || curr == -3) && hidden != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * undos move by calling back board from stack
     */
    public void undo() {
        if (!boardStack.empty()) {
            visibleBoard = boardStack.pop();
            if (!gameActive) {
                gameActive = true;
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[10][10];
        visibleBoard = new int[10][10];
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                visibleBoard[r][c] = -2; // -2 denotes non-visible
            }
        }
        numMines = 20;
        gameActive = true;
        boardStack = new Stack<>();
        firstTurn = true;
    }

    /**
     * generates and adds 20 mines to game board.
     */
    public void generateMines(int clickedR, int clickedC) {
        Random rand = new Random();
        int[][] mineCoords = new int[numMines][2];
        int generatedR;
        int generatedC;
        boolean loop;

        for (int i = 0; i < numMines; i++) {
            loop = true;
            while (loop) { // find new coord pair
                loop = false;
                generatedR = rand.nextInt(10);
                generatedC = rand.nextInt(10);
                if (Math.abs(clickedR - generatedR) <= 1 && Math.abs(clickedC - generatedC) <= 1) {
                    loop = true;
                    continue;
                }
                for (int c = 0; c < i; c++) {
                    if (mineCoords[c][0] == generatedR && mineCoords[c][1] == generatedC) {
                        loop = true; // must keep trying if coords already exist
                    }
                }

                if (!loop) { // loop is false if found no coords previously exist (new pair)
                    mineCoords[i][0] = generatedR;
                    mineCoords[i][1] = generatedC;
                    // add mine to board
                    board[generatedR][generatedC] = -1; // -1 is symbol for mine
                }
            }
        }
    }

    /**
     * generates tile numbers by iterating through board and counting mines
     */
    public void populateBoard() {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == -1) {
                    continue;
                }
                board[r][c] = countSurroundingMines(r, c);
            }
        }
    }

    /**
     * calculates how many mines in a tile's vicinity
     *
     * @param r row of tile
     * @param c column of tile
     * @return number of mines in 1-tile radius
     */
    public int countSurroundingMines(int r, int c) {
        int count = 0;
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
                    continue;
                }
                if (board[i][j] == -1) {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, -1 = mine, -2 = non-visible
     */
    public int getCell(int r, int c) {
        return visibleBoard[r][c];
    }

    /**
     * getActive is a getter for the status of the game, if false the user cannot play
     *
     * @return boolean stating whether game is currently running
     */
    public boolean getActive() {
        return gameActive;
    }

    /**
     * setter to manually change the firstTurn setting
     *
     * @param ft whether it is first turn or not
     */
    public void setFirstTurn(boolean ft) {
        firstTurn = ft;
    }

    /**
     * setter to manually set the board for testing
     *
     * @param b 2D array board to be set
     */
    public void setBoard(int[][] b) {
        board = b;
    }

    /**
     * allows testing to see the size of stack;
     *
     * @return size of stack
     */
    public int getStackSize() {
        return boardStack.size();
    }

    /**
     * getter that returns a deep copy of the game's internal board
     *
     * @return 2D array of copied board
     */
    public int[][] getBoard() {
        int[][] boardCopy = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }


    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Minesweeper t = new Minesweeper();
        Random random = new Random();
        int clickedR = random.nextInt(10);
        int clickedC = random.nextInt(10);
        t.playTurn(clickedR, clickedC, true);

        System.out.println("Clicked: [" + clickedR + "] [" + clickedC + "]");
        for (int[] row : t.board) {
            for (int tile : row) {
                if (tile != -1) {
                    System.out.print(" ");
                }
                System.out.print(tile + " ");
            }
            System.out.println();
        }
    }
}
