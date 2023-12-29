package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a Minesweeper object to serve as the game's model.
 */
public class RunMinesweeper implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);

        // Help frame
        final JFrame helpFrame = new JFrame("Minesweeper Guide");
        helpFrame.setLocation(800, 273);

        // JTextArea for displaying help text
        JTextArea helpTextArea = new JTextArea();
        helpTextArea.setEditable(false);
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);

        // Add the help text to the JTextArea
        helpTextArea.setText("Welcome to Minesweeper!\n" +
                "Uncover all empty plots without triggering any mines!\n\n" +
                "How to Play:\n" +
                "- Left-click to reveal a cell or remove flag.\n" +
                "- Right-click or Left-click + Ctrl to flag a cell as a mine.\n" +
                "- Use the 'Reset' button to start a new game.\n" +
                "- Use the 'Undo' button to undo the last move.");

        // Add the JTextArea to a JScrollPane for scrolling if needed
        JScrollPane scrollPane = new JScrollPane(helpTextArea);

        // Add the scroll pane to the helpFrame
        helpFrame.add(scrollPane);

        // Set the size and make the helpFrame visible
        helpFrame.setSize(new Dimension(300, 220));
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setVisible(true);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
        status_panel.setBackground(new Color(250, 200, 110));

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        control_panel.setBackground(new Color(250, 200, 110));

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}