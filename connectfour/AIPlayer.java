package connectfour;

import java.util.Random;

public class AIPlayer {
    private Seed aiSeed;
    private Seed opponentSeed;
    private Random rand;
    private String difficulty; // Tingkat Difficulty AI

    public AIPlayer(Seed aiSeed, String difficulty) {
        this.aiSeed = aiSeed;
        this.opponentSeed = (aiSeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
        this.difficulty = difficulty;
        this.rand = new Random();
    }

    /** Menghasilkan langkah AI berdasarkan tingkat kesulitan */
    public int[] move(Board board) {
        switch (difficulty) {
            case "Noob":
                return noobMove(board);
            case "Smart":
                return smartMove(board);
            case "Trickster":
                return tricksterMove(board);
            default:
                return noobMove(board);
        }
    }

    /** AI Noob: Langkah acak */
    private int[] noobMove(Board board) {
        int row, col;
        do {
            row = rand.nextInt(Board.ROWS);
            col = rand.nextInt(Board.COLS);
        } while (board.cells[row][col].content != Seed.NO_SEED);
        return new int[]{row, col};
    }

    /** AI Smart: Mencoba menang atau mencegah kemenangan lawan */
    private int[] smartMove(Board board) {
        // Coba menang dulu
        int[] winMove = findWinningMove(board, aiSeed);
        if (winMove != null) return winMove;

        // Cegah lawan menang
        int[] blockMove = findWinningMove(board, opponentSeed);
        if (blockMove != null) return blockMove;

        // Kalau tidak ada, langkah acak
        return noobMove(board);
    }

    /** AI Trickster: Suka Njebak lawan, cenderung ngincar kemenangan */
    private int[] tricksterMove(Board board) {
        // Strategi AI untuk memprioritaskan langkah tertentu untuk menjebak
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    return new int[]{row, col};
                }
            }
        }
        return noobMove(board);
    }

    /** Cari langkah untuk menang */
    private int[] findWinningMove(Board board, Seed seed) {
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = seed;
                    if (board.stepGame(seed, row, col) ==
                            (seed == Seed.CROSS ? State.CROSS_WON : State.NOUGHT_WON)) {
                        board.cells[row][col].content = Seed.NO_SEED;
                        return new int[]{row, col};
                    }
                    board.cells[row][col].content = Seed.NO_SEED;
                }
            }
        }
        return null;
    }
}
