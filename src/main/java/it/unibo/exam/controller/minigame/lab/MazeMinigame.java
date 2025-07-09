package it.unibo.exam.controller.minigame.lab;

import it.unibo.exam.model.entity.minigame.lab.MazeModel;
import it.unibo.exam.view.lab.MazePanel;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.utility.generator.MazeGenerator;
import it.unibo.exam.model.scoring.TieredScoringStrategy;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class MazeMinigame implements Minigame {

    // Scoring parameters
    private static final int BONUS_TIME_THRESHOLD_SECONDS = 30;
    private static final int BONUS_POINTS = 10;
    private static final int MAX_POINTS_CAP = 100;
    private static final int ROOM_ID = 3;  // Example room ID for scoring

    private final ScoringStrategy scoringStrategy;

    private JFrame frame;
    private MinigameCallback callback;
    private MazeModel model;
    private MazePanel panel;
    private long startTimeMillis;
    private int level;  // Variable to track the current level

    public MazeMinigame() {
        this.scoringStrategy = new CapDecorator(
            new TimeBonusDecorator(
                new TieredScoringStrategy(),
                BONUS_TIME_THRESHOLD_SECONDS,
                BONUS_POINTS
            ),
            MAX_POINTS_CAP
        );
        this.level = 1;  // Start with level 1
    }

    @Override
    public void start(JFrame parent, MinigameCallback callback) {
        this.callback = Objects.requireNonNull(callback, "callback cannot be null");

        // Generate maze based on the current level
        MazeGenerator generator = new MazeGenerator();
        int[][] maze = generator.generateMaze(level);  // Pass the current level to generate the maze

        // Initialize model, panel, and set the view
        this.model = new MazeModel(maze);
        this.panel = new MazePanel(model.getMaze());
        panel.setController(this);

        // Set up frame and panel
        frame = new JFrame(getName());
        frame.add(panel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Initialize the game state
        panel.updatePlayerPosition(model.getPlayerX(), model.getPlayerY());

        // Start the timer and handle key presses
        startTimeMillis = System.currentTimeMillis();
    }

    // Handle player movement and check win condition
    public void handleKeyPress(int dx, int dy) {
        if (model.isCompleted()) return;

        if (model.movePlayer(dx, dy)) {
            panel.updatePlayerPosition(model.getPlayerX(), model.getPlayerY());
            panel.repaint();

            // Check if player reached the exit
            if (model.isCompleted()) {
                panel.setMazeCompleted(true);
                stop();  // End the game

                // Transition to the next level if applicable
                if (level < 3) {  // Assuming there are 3 levels
                    level++;  // Increment level
                    start(frame, callback);  // Start the next level
                } else {
                    // If all levels completed, notify callback
                    int elapsedSeconds = getElapsedTimeSeconds();
                    int score = scoringStrategy.calculate(ROOM_ID, elapsedSeconds);
                    callback.onComplete(true, elapsedSeconds, score);
                    JOptionPane.showMessageDialog(frame, "Congratulations! You completed all levels!");
                    stop();  // End the game
                }
            }
        }
    }

    @Override
    public void stop() {
        if (frame != null) {
            frame.dispose();  // Dispose of the window
        }
    }

    @Override
    public String getName() {
        return "Maze Game - Level " + level;  // Display current level in the name
    }

    @Override
    public String getDescription() {
        return "Navigate through the maze and reach the exit!";
    }

    // Gets the elapsed time in seconds since the game started
    public int getElapsedTimeSeconds() {
        return (int) ((System.currentTimeMillis() - startTimeMillis) / 1000);
    }
}
