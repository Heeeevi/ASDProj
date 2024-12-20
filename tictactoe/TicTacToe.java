/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231102 - Ahmed Miftah Ghifari
 * 2 - 5026231103 - Eric Vincentius Jaolis
 * 3 - 5026231156 - Hafiyyuddin Ahmad
 */

package tictactoe;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 * NOW AVAILABLE WITH AI as ENEMY with 3 Various Difficulty NOOB,SMART and TRICKSTER
 * AI Characteristics
 * NOOB = Play with no strategy, just a noob AI
 * SMART = Focus To Make You Never WON, but instead his Step is Creepy
 * TRICKSTER = Focus On win, this one is really though opponent
 */
public class TicTacToe extends JPanel {
    private static final long serialVersionUID = 1L;

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private JButton setVolume;

    private static FloatControl volumeSetting;

    private AIPlayer aiPlayer;  // Tambahkan AI
    private boolean aiEnabled;  // boolean untuk player ingin bertarung dengan orang atau AI

    public TicTacToe() {

//        Object[] boardSize = {"3x3", "5x5"};
//        String selectedSize = (String) JOptionPane.showInputDialog(
//                null, "Select Board Size:", "Board Size",
//                JOptionPane.QUESTION_MESSAGE, null, boardSizeOptions, boardSizeOptions[0]);
//
//        if (selectedSize != null && selectedSize.equals("5x5")) {
//            Board.ROWS = 5;
//            Board.COLS = 5;
//        } else {
//            Board.ROWS = 3;
//            Board.COLS = 3;
//        }
        // Pilih mode permainan: Single Player atau Multiplayer
        Object[] options = {"Human vs AI", "Human vs Human"};
        int mode = JOptionPane.showOptionDialog(null, "Select Game Mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        aiEnabled = (mode == 0); // Aktifkan AI jika dipilih Human vs AI

        if (aiEnabled) {
            String[] difficulties = {"Noob", "Smart", "Trickster", "Boss"};
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

    public static void playBGM(String filedir) {
        new Thread(() -> {
            try {
                File music = new File(filedir);
                if(music.exists()) {
                    AudioInputStream audio = AudioSystem.getAudioInputStream(music);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audio);

                    volumeSetting = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                } else {
                    System.out.println("Audio is not found");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void displayVolumeSlider(JFrame parentButton) {
        if(volumeSetting == null) {
            JOptionPane.showMessageDialog(parentButton, "Music doesn't play", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog volDialog = new JDialog(parentButton, "Control BGM Volume", true);
        volDialog.setSize(350, 150);
        volDialog.setLayout(new BoxLayout(volDialog.getContentPane(), BoxLayout.Y_AXIS));

        JSlider volSlider = new JSlider(
                (int) volumeSetting.getMinimum(),
                (int) volumeSetting.getMaximum(),
                (int) volumeSetting.getValue()
        );
        volSlider.setMajorTickSpacing(10);
        volSlider.setPaintTicks(true);
        volSlider.setPaintLabels(true);

        JButton exit = new JButton("OK");

        volSlider.addChangeListener(e -> volumeSetting.setValue(volSlider.getValue()));
        exit.addActionListener(e -> volDialog.dispose());

        volDialog.add(new JLabel("Set Volume: "));
        volDialog.add(volSlider);

        volDialog.setLocationRelativeTo(parentButton);
        volDialog.setVisible(true);
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

        JPanel navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.add(statusBar, BorderLayout.CENTER);
        navigationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        setVolume = new JButton("Set Volume");
        setVolume.setFont(new Font("OCR A Extended", Font.PLAIN, 12));
        setVolume.setMargin(new Insets(10,10,10,10));
        setVolume.addActionListener(e -> {
            JFrame parentButton = (JFrame) SwingUtilities.getWindowAncestor(this);
            displayVolumeSlider(parentButton);
        });
        setVolume.setPreferredSize(new Dimension(100,30));
        navigationPanel.add(setVolume, BorderLayout.EAST);

        super.setLayout(new BorderLayout());
        super.add(navigationPanel, BorderLayout.PAGE_END);
//        super.add(setVolume, BorderLayout.PAGE_END);
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
        playBGM("audio/retro-game-arcade-236133.wav");
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
            frame.setContentPane(new TicTacToe());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
