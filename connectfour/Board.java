/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231102 - Ahmed Miftah Ghifari
 * 2 - 5026231103 - Eric Vincentius Jaolis
 * 3 - 5026231156 - Hafiyyuddin Ahmad
 */

package connectfour;

import javax.swing.*;
import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 6;  // ROWS x COLS cells
    public static final int COLS = 7;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        if(hasWon(player, selectedRow, selectedCol)) {
            SoundEffect.EXPLODE.play();
            Object[] choice = {"Play Again", "Exit"};
            int option = JOptionPane.showOptionDialog(null, player + " Wins", "Do you want to play again?", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choice, choice[0]);
            if(option == 0) newGame();
            else System.exit(0);
            return (player == Seed.RED) ? State.RED_WON : State.BLUE_WON;
        }

//        // Compute and return the new game state
//        if (cells[selectedRow][0].content == player  // 3-in-the-row
//                && cells[selectedRow][1].content == player
//                && cells[selectedRow][2].content == player
//                || cells[0][selectedCol].content == player // 3-in-the-column
//                && cells[1][selectedCol].content == player
//                && cells[2][selectedCol].content == player
//                || selectedRow == selectedCol     // 3-in-the-diagonal
//                && cells[0][0].content == player
//                && cells[1][1].content == player
//                && cells[2][2].content == player
//                || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
//                && cells[0][2].content == player
//                && cells[1][1].content == player
//                && cells[2][0].content == player) {
//            SoundEffect.EXPLODE.play();
//            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
//        } else {
            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING; // still have empty cells
                    }
                }
            }
            return State.DRAW; // no empty cell, it's a draw
//        }
    }

    public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
        // Check for 4-in-a-line on the rowSelected
        int count = 0;
        for (int col = 0; col < COLS; ++col) {
            if (cells[rowSelected][col].content == theSeed) {
                ++count;
                if (count == 4) return true;  // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
        }
        // Check column and diagonals
        count = 0;
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][colSelected].content == theSeed) {
                ++count;
                if (count == 4) return true;  // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
        }

        count = 0;
        for (int dig = -Math.min(rowSelected, colSelected); rowSelected + dig < ROWS && colSelected + dig < COLS; ++dig) {
            int row = rowSelected + dig;
            int col = colSelected + dig;
            if (row >= 0 && col >= 0 && cells[row][col].content == theSeed) {
                ++count;
                if (count == 4) return true;  // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
        }

        count = 0;
        for (int dig = -Math.min(rowSelected, COLS - colSelected -1); rowSelected + dig < ROWS && colSelected - dig >= 0; ++dig) {
            int row = rowSelected + dig;
            int col = colSelected + dig;
            if (row >= 0 && col >= 0 && cells[row][col].content == theSeed) {
                ++count;
                if (count == 4) return true;  // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
        }
        return false;  // no 4-in-a-line found
    }


    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}
