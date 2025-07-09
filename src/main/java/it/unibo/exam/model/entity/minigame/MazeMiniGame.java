package it.unibo.exam.model.entity.minigame;

import it.unibo.exam.controller.minigame.lab.MazeController;
import it.unibo.exam.utility.generator.MazeGenerator;
import it.unibo.exam.view.lab.MazePanel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * Maze minigame that challenges players to navigate through 3 progressively harder mazes.
 * Players must complete a 5x5, then 7x7, then 9x9 maze within a time limit.
 */
public final class MazeMiniGame implements Minigame {

    private static final Logger LOGGER = Logger.getLogger(MazeMiniGame.class.getName());

    // Window settings
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 800;

    // Game settings
    private static final int TOTAL_MAZES = 3;
    private static final String[] DIFFICULTY_NAMES = {"Easy (5x5)", "Medium (7x7)", "Hard (9x9)"};

    // Game state
    private JFrame gameFrame;
    private MinigameCallback callback;
    private final MazeGenerator generator = new MazeGenerator();
    private MazeController currentController;
    private int currentMazeIndex;
    private boolean gameCompleted;
    private long gameStartTime;

    /**
     * Starts the maze minigame with the given parent frame and completion callback.
     *
     * @param parentFrame the parent frame for centering the game window
     * @param onComplete  callback when the game finishes
     */
    @Override
    public void start(final JFrame parentFrame, final MinigameCallback onComplete) {
        this.callback = onComplete;
        this.gameStartTime = System.currentTimeMillis();
        this.currentMazeIndex = 0;
        this.gameCompleted = false;

        createGameWindow(parentFrame);
        startNextMaze();

        LOGGER.info("MazeMinigame started");
    }

    /**
     * Stops the minigame by disposing of the game window.
     */
    @Override
    public void stop() {
        if (gameFrame != null) {
            gameFrame.dispose();
            gameFrame = null;
        }
    }

    /**
     * Returns the display name of this minigame.
     *
     * @return the name of the minigame
     */
    @Override
    public String getName() {
        return "Maze Runner";
    }

    /**
     * Returns a description of the minigame.
     *
     * @return the game description
     */
    @Override
    public String getDescription() {
        return "Complete 3 mazes of increasing difficulty as fast as possible!";
    }

    /**
     * Creates the main game window.
     *
     * @param parentFrame the parent frame for centering
     */
    private void createGameWindow(final JFrame parentFrame) {
        gameFrame = new JFrame(getName());
        gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameFrame.setLocationRelativeTo(parentFrame);
        gameFrame.setResizable(false);

        // Add window close listener to handle incomplete games
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                endGame(false);
            }
        });
    }

    /**
     * Starts the next maze if currentMazeIndex < TOTAL_MAZES.
     */
    private void startNextMaze() {
        if (currentMazeIndex >= TOTAL_MAZES) {
            // All mazes completed
            endGame(true);
            return;
        }

        // Generate maze for difficulty
        final int difficulty = currentMazeIndex + 1; // {1 (E), 2 (M), 3 (H)}
        final int[][] maze = generator.generateMaze(difficulty);

        // Create and display the new maze panel
        final MazePanel panel = new MazePanel(maze);
        
        // Create controller and link it with the panel
        currentController = new MazeController(maze, panel);
        currentController.setCompletionListener(this::handleMazeCompletion);
        panel.setController(currentController);
        
        gameFrame.getContentPane().removeAll();
        gameFrame.add(panel);
        gameFrame.revalidate();
        gameFrame.repaint();
        gameFrame.setVisible(true);

        // Focus for keyboard input
        panel.requestFocusInWindow();

        LOGGER.info("Started maze " + (currentMazeIndex + 1) + "/3: " + DIFFICULTY_NAMES[currentMazeIndex]);
    }

    /**
     * Handles the completion of a single maze.
     *
     * @param success     true if this maze was solved
     * @param timeSeconds the time taken to complete the maze
     */
    private void handleMazeCompletion(final boolean success, final int timeSeconds) {
        if (!success) {
            endGame(false);
            return;
        }

        currentMazeIndex++;
        LOGGER.info("Maze " + currentMazeIndex + " completed in " + timeSeconds + " seconds");

        if (currentMazeIndex < TOTAL_MAZES) {
            showTransitionDialog(timeSeconds);
        } else {
            // All mazes completed
            endGame(true);
        }
    }

    /**
     * Shows transition dialog between mazes.
     *
     * @param timeSeconds the time taken to complete the maze
     */
    private void showTransitionDialog(final int timeSeconds) {
        final String message = String.format(
            "Maze %d/3 completed in %d seconds!%n%nReady for the next maze?",
            currentMazeIndex, timeSeconds
        );

        final int choice = JOptionPane.showConfirmDialog(
            gameFrame, message, "Maze Completed!",
            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            startNextMaze();
        } else {
            endGame(false);
        }
    }

    /**
     * Handles game exit.
     *
     * @param success true if all 3 mazes were completed
     */
    private void endGame(final boolean success) {
        if (gameCompleted) {
            return;
        }

        gameCompleted = true;
        final int totalTime = getTotalTime();

        if (success) {
            showCompletionDialog(totalTime);
        } else {
            showGameOverDialog();
        }

        // Notify callback
        if (callback != null) {
            callback.onComplete(success, totalTime);
        }
        stop();
    }

    /**
     * Shows completion dialog when all mazes are completed.
     *
     * @param totalTime the total time taken to complete all mazes
     */
    private void showCompletionDialog(final int totalTime) {
        final String message = String.format(
            "🎉 ALL MAZES COMPLETED! 🎉%n%nTotal Time: %d seconds%nWell done, Runner!",
            totalTime
        );
        JOptionPane.showMessageDialog(gameFrame, message, "Congratulations!",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows game over dialog if the game ends early.
     */
    private void showGameOverDialog() {
        final String message = String.format(
            "Game ended at maze %d/3.%nBetter luck next time!",
            currentMazeIndex + 1
        );
        JOptionPane.showMessageDialog(gameFrame, message, "Game Over",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    private int getTotalTime() {
        return (int) ((System.currentTimeMillis() - this.gameStartTime) / 1000);
    }

    /**
     * Gets the current maze index (0-2).
     *
     * @return the current maze index
     */
    public int getCurrentMazeIndex() {
        return this.currentMazeIndex;
    }

    /**
     * Checks if the entire game is completed.
     *
     * @return true if all mazes are completed
     */
    public boolean isGameCompleted() {
        return this.gameCompleted && this.currentMazeIndex >= TOTAL_MAZES;
    }
}