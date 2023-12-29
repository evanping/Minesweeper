package org.cis1200.minesweeper;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Minesweeper minesweeper;

    @BeforeEach
    public void setUp() {
        /*

        -1  0  0  0  0  0  0  0  0 -1
         0 -1  0  0  0  0  0  0 -1  0
         0  0 -1  0  0  0  0 -1  0  0
         0  0  0 -1  0  0 -1  0  0  0
         0  0  0  0 -1 -1  0  0  0  0
         0  0  0  0 -1 -1  0  0  0  0
         0  0  0 -1  0  0 -1  0  0  0
         0  0 -1  0  0  0  0 -1  0  0
         0 -1  0  0  0  0  0  0 -1  0
        -1  0  0  0  0  0  0  0  0 -1

         */
        int[][] testBoard = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == j || i == 9 - j) {
                    testBoard[i][j] = -1;
                }
            }
        }
        minesweeper = new Minesweeper();
        minesweeper.setBoard(testBoard);
        minesweeper.populateBoard();
        minesweeper.setFirstTurn(false);
    }

    @Test
    public void testPlayTurnRevealsAllEmptyPlots() {
        minesweeper.playTurn(0, 4, true);
        assertEquals(0, minesweeper.getCell(0, 3));
        assertEquals(0, minesweeper.getCell(0, 4));
        assertEquals(0, minesweeper.getCell(0, 5));
        assertEquals(0, minesweeper.getCell(0, 6));
        assertEquals(0, minesweeper.getCell(1, 4));
        assertEquals(0, minesweeper.getCell(1, 5));
        assertEquals(1, minesweeper.getStackSize());
        assertTrue(minesweeper.getActive());
    }

    @Test
    public void testPlayTurnRevealsSingleNumberedPlot() {
        minesweeper.playTurn(4, 3, true);
        assertEquals(3, minesweeper.getCell(4, 3));
        assertEquals(1, minesweeper.getStackSize());
        assertTrue(minesweeper.getActive());
    }

    @Test
    public void testPlayTurnRevealsMineAndEndsGame() {
        minesweeper.playTurn(4, 4, true);
        assertEquals(-1, minesweeper.getCell(4, 4));
        assertEquals(1, minesweeper.getStackSize());

        assertFalse(minesweeper.getActive());
        minesweeper.playTurn(4, 5, true);
        assertEquals(-2, minesweeper.getCell(4, 5));
    }

    @Test
    public void testPlayFlagAndPlayTurnRemovesFlag() {
        minesweeper.playFlag(0, 0);
        assertEquals(-3, minesweeper.getCell(0, 0));

        minesweeper.playTurn(0, 0, true);
        assertEquals(-2, minesweeper.getCell(0, 0));
        assertEquals(1, minesweeper.getStackSize());
        assertTrue(minesweeper.getActive());
    }

    @Test
    public void testUndoWhenGameActive() {
        minesweeper.playTurn(4, 3, true);
        assertEquals(3, minesweeper.getCell(4, 3));
        minesweeper.undo();
        assertEquals(-2, minesweeper.getCell(4, 3));
        assertEquals(0, minesweeper.getStackSize());
    }

    @Test
    public void testUndoWhenGameEnded() {
        minesweeper.playTurn(4, 4, true);
        assertEquals(-1, minesweeper.getCell(4, 4));
        assertFalse(minesweeper.getActive());
        minesweeper.undo();
        assertEquals(-2, minesweeper.getCell(4, 4));
        assertEquals(0, minesweeper.getStackSize());
        assertTrue(minesweeper.getActive());
    }

    @Test
    public void testReset() {
        minesweeper.playTurn(4, 4, true);

        minesweeper.reset();
        assertEquals(-2, minesweeper.getCell(4, 4));
        assertTrue(minesweeper.getActive());
        assertEquals(0, minesweeper.getStackSize());
    }

    @Test
    public void testGenerateMines() {
        minesweeper.reset();
        minesweeper.generateMines(1, 1);
        minesweeper.populateBoard();
        int[][] board = minesweeper.getBoard();
        int mineCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == -1) {
                    mineCount++;
                }
            }
        }
        assertEquals(20, mineCount);
        assertEquals(0, board[1][1]);
    }
}
