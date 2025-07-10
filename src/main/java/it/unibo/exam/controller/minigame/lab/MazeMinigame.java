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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.util.Objects;

/**
 * The MazeMinigame class manages the maze game, including the logic for generating 
 * the maze, tracking player progress, and transitioning between levels. It also handles
 * scoring and displays game-related information.
 */
public final class MazeMinigame implements Minigame {

    // Scoring parameters
    private static final int BONUS_TIME_THRESHOLD_SECONDS = 30;
    private static final int BONUS_POINTS = 10;
    private static final int MAX_POINTS_CAP = 100;
    private static final int ROOM_ID = 3;  // Example room ID for scoring
    private static final int WINDOW_WIDTH = 800;  // Window width constant
    private static final int WINDOW_HEIGHT = 600; // Window height constant
    private static final int MAX_LEVEL = 3;  // Maximum number of levels

    private final ScoringStrategy scoringStrategy;

    private JFrame frame;
    private MinigameCallback callback;  // Parameter should be final
    private MazeModel model;
    private MazePanel panel;
    private long startTimeMillis;
    private int level;  // Variable to track the current level

        /**
         * No‐arg constructor for factory instantiation (uses default scoring).
         */
        public MazeMinigame() {
            this(
                new CapDecorator(
                    new TimeBonusDecorator(
                        new TieredScoringStrategy(),
                        BONUS_TIME_THRESHOLD_SECONDS,
                        BONUS_POINTS
                    ),
                    MAX_POINTS_CAP
                )
            );
            this.level = 1;  // Initialize level to 1
        }

    /**
     * Full constructor allows custom scoring strategy.
     *
     * @param scoringStrategy the strategy used to compute final score
     */
    public MazeMinigame(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
            "scoringStrategy must not be null");
    }

    /**
     * Starts the MazeMinigame by generating the maze for the current level.
     * 
     * @param parent The parent JFrame for centering the minigame window.
     * @param callback The callback to handle game completion.
     */
    @Override
    public void start(final JFrame parent, final MinigameCallback callback) {
        // Initialize the callback field only if it is not already initialized
        if (this.callback == null) {
            this.callback = Objects.requireNonNull(callback, "callback cannot be null");
        }

        // Generate maze based on the current level
        final MazeGenerator generator = new MazeGenerator();
        final int[][] maze = generator.generateMaze(level);  // Pass the current level to generate the maze

        // Initialize model, panel, and set the view
        this.model = new MazeModel(maze);
        this.panel = new MazePanel(model.getMaze());
        panel.setController(this);

        // Set up frame and panel
        this.frame = new JFrame(getName());
        this.frame.add(panel);
        this.frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.frame.setResizable(false);
        this.frame.pack();
        this.frame.setLocationRelativeTo(parent);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);

        // Initialize the game state
        panel.updatePlayerPosition(model.getPlayerX(), model.getPlayerY());

        if (this.level < 2) {
            // Start the timer and handle key presses
            startTimeMillis = System.currentTimeMillis();
        }
    }

    /**
     * Handles player movement within the maze and checks if the player has reached the exit.
     * If the maze is completed, it transitions to the next level.
     * 
     * @param dx The change in the player's x-coordinate.
     * @param dy The change in the player's y-coordinate.
     */
    public void handleKeyPress(final int dx, final int dy) {
        if (model.isCompleted()) {
            return;
        }

        if (model.movePlayer(dx, dy)) {
            panel.updatePlayerPosition(model.getPlayerX(), model.getPlayerY());
            panel.repaint();

            // Check if player reached the exit
            if (model.isCompleted()) {
                panel.setMazeCompleted(true);
                stop();  // End the game

                // Transition to the next level if applicable
                if (level < MAX_LEVEL) {  // Assuming there are 3 levels
                    level++;  // Increment level
                    start(frame, callback);  // Start the next level
                } else {
                    // If all levels completed, notify callback
                    final int elapsedSeconds = getElapsedTimeSeconds();
                    final int score = scoringStrategy.calculate(ROOM_ID, elapsedSeconds);
                    callback.onComplete(true, elapsedSeconds, score);
                    JOptionPane.showMessageDialog(frame, "Congratulations! You completed all levels!");
                }
            }
        }
    }

    /**
     * Stops the MazeMinigame by disposing of the game window.
     */
    @Override
    public void stop() {
        if (frame != null) {
            frame.dispose();  // Dispose of the window
        }
    }

    /**
     * Returns the name of the MazeMinigame, including the current level.
     * 
     * @return The name of the game with the current level.
     */
    @Override
    public String getName() {
        return "Maze Runner - Level " + level;  // Display current level in the name
    }

    /**
     * Returns a description of the MazeMinigame.
     * 
     * @return A description of the game.
     */
    @Override
    public String getDescription() {
        return "Run fast, run furious. Use WASD and go to the red square!";
    }

    /**
     * Gets the elapsed time in seconds since the game started.
     * 
     * @return The elapsed time in seconds.
     */
    public int getElapsedTimeSeconds() {
        return (int) ((System.currentTimeMillis() - startTimeMillis) / 1000);
    }

    /**
     * Gets the current level.
     * 
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the maximum level.
     * 
     * @return the maximum level
     */
    public int getMaxLevel() {
        return MAX_LEVEL;
    }
}
