package connectfour;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 * NOW AVAILABLE WITH AI as ENEMY with 3 Various Difficulty NOOB,SMART and TRICKSTER
 * AI Characteristics
 * NOOB = Play with no strategy, just a noob AI
 * SMART = Focus To Make You Never WON, but instead his Step is Creepy
 * TRICKSTER = Focus On win, this one is really though opponent
 */
public class ConnectFour extends JPanel {
    private static final long serialVersionUID = 1L;

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private AIPlayer aiPlayer;  // Tambahkan AI
    private boolean aiEnabled;  // boolean untuk player ingin bertarung dengan orang atau AI

    public ConnectFour() {
        // Pilih mode permainan: Single Player atau Multiplayer
        Object[] options = {"Human vs AI", "Human vs Human"};
        int mode = JOptionPane.showOptionDialog(null, "Select Game Mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        aiEnabled = (mode == 0); // Aktifkan AI jika dipilih Human vs AI

        if (aiEnabled) {
            String[] difficulties = {"Noob", "Smart", "Trickster"};
            String chosenDifficulty = (String) JOptionPane.showInputDialog(null, "Choose AI Difficulty:",
                    "AI Difficulty", JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
            aiPlayer = new AIPlayer(Seed.NOUGHT, chosenDifficulty);
        }
        /** Constructor to setup the UI and game components */

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SoundEffect.CLICK.play();
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        repaint();

                        if (currentState == State.PLAYING && aiEnabled) {
                            aiMove();
                        } else {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        statusBar = new JLabel();
        setupUI();
        initGame();
        newGame();
    }

    private void aiMove() {
        int[] move = aiPlayer.move(board);
        if (move != null) {
            currentState = board.stepGame(Seed.NOUGHT, move[0], move[1]);
            currentPlayer = Seed.CROSS;
            repaint();
        }
    }

    private void setupUI() {
        statusBar.setFont(new Font("OCR A Extended", Font.PLAIN, 14));
        statusBar.setPreferredSize(new Dimension(300, 30));
        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
    }
    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();
    }
    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }
    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);
        statusBar.setText((currentState == State.PLAYING)
                ? (currentPlayer == Seed.CROSS ? "Maxwell's Turn" : "Oiia Cat's Turn")
                : currentState.toString());
    }
    /** The entry "main" method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Connect Four");
            frame.setContentPane(new ConnectFour());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
