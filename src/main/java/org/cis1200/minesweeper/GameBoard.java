package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * This class instantiates a Minesweeper object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper grid; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;

    boolean isCtrlPressed = false;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        grid = new Minesweeper(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (e.getButton() == MouseEvent.BUTTON1) { // left click
                    // updates the model given the coordinates of the mouseclick
                    if (isCtrlPressed) { // click + ctrl is flag
                        grid.playFlag(p.y / 50, p.x / 50);
                    } else { // uncover
                        grid.playTurn(p.y / 50, p.x / 50, true);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) { // right click
                    grid.playFlag(p.y / 50, p.x / 50);
                }


                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlPressed = false;
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        grid.reset();
        status.setText("Player 1's Turn"); // change to number of bombs left
        status.setText("Total Mines: 20");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * calls back previous board
     */
    public void undo() {
        grid.undo();
        repaint();
        updateStatus();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (grid.checkWinner()) {
            status.setText("You won!");
        } else if (!grid.getActive()) {
            status.setText("You lost.. Reset or Undo to continue");
        } else {
            status.setText("Total Mines: 20");
        }
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int i = 50; i <= 500; i += 50) {
            g.drawLine(i, 0, i, 500);
            g.drawLine(0, i, 500, i);
        }

        // draw numbers on grid
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int state = grid.getCell(j, i);
                if (state == -1) { // MINE
                    g.setColor(Color.RED);
                    g.fillRect(50 * i + 1, 50 * j + 1, 48, 48);
                    g.setColor(Color.BLACK);
                    g.drawString("(M)", 15 + 50 * i, 28 + 50 * j);
                } else if (state == -3) { // FLAG
                    g.setColor(Color.red);
                    g.drawOval(15 + 50 * i, 15 + 50 * j, 20, 20);
                    g.setColor(Color.BLACK);
                } else if (state != -2) { // NUMBERED TILE
                    g.setColor(Color.lightGray);
                    g.fillRect(50 * i + 1, 50 * j + 1, 48, 48);
                    g.setColor(Color.BLACK);
                    if (state == 0) { // leave 0 tiles blank
                        continue;
                    }
                    g.drawString(Integer.toString(state), 21 + 50 * i, 28 + 50 * j);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
