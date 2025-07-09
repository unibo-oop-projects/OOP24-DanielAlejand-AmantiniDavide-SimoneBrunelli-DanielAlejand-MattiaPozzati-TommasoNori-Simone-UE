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

    public MazeMinigame() {
        this.scoringStrategy = new CapDecorator(
            new TimeBonusDecorator(
                new TieredScoringStrategy(),
                BONUS_TIME_THRESHOLD_SECONDS,
                BONUS_POINTS
            ),
            MAX_POINTS_CAP
        );
    }

    @Override
    public void start(JFrame parent, MinigameCallback callback) {
        this.callback = Objects.requireNonNull(callback, "callback cannot be null");

        MazeGenerator generator = new MazeGenerator();
        int[][] maze = generator.generateMaze(1);  // Generate a
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
                int elapsedSeconds = getElapsedTimeSeconds();
                int score = scoringStrategy.calculate(ROOM_ID, elapsedSeconds);
                panel.setMazeCompleted(true);
                callback.onComplete(true, elapsedSeconds, score);
                stop();  // End the game
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
        return "Maze Game";
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

