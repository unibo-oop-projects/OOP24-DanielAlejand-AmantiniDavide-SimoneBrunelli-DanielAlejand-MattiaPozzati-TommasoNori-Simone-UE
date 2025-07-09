package it.unibo.exam.controller.minigame.lab;

import it.unibo.exam.utility.generator.MazeGenerator;
import it.unibo.exam.view.lab.MazePanel;

import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Controller for the maze game that handles player input and game logic.
 * Manages the interaction between the maze model and view components.
 */
public final class MazeController {

    private static final Logger LOGGER = Logger.getLogger(MazeController.class.getName());

    // Game state
    private final int[][] maze;
    private int playerX;
    private int playerY;
    private boolean completed;
    private final long startTime;
    
    // View component
    private final MazePanel mazePanel;
    
    // Listener for maze completion
    private transient MazeCompletionListener completionListener;

    /**
     * Constructs a new MazeController.
     *
     * @param maze the maze data
     * @param mazePanel the maze view panel
     */
    public MazeController(final int[][] maze, final MazePanel mazePanel) {
        this.maze = maze;
        this.mazePanel = mazePanel;
        this.startTime = System.currentTimeMillis();
        this.completed = false;
        
        initializePlayerPosition();
    }

    /**
     * Handles key press events for player movement.
     *
     * @param e the key event
     */
    public void handleKeyPress(final KeyEvent e) {
        if (completed) {
            return;
        }

        int newX = playerX;
        int newY = playerY;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP    -> newY--;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> newY++;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT  -> newX--;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> newX++;
            default                               -> {
                return; 
            }
        }

        movePlayer(newX, newY);
    }

    /**
     * Moves the player to the specified position if valid.
     *
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    private void movePlayer(final int newX, final int newY) {
        if (newX < 0
            || newX >= maze[0].length
            || newY < 0
            || newY >= maze.length
            || maze[newY][newX] == MazeGenerator.WALL) {
            return;
        }

        playerX = newX;
        playerY = newY;

        // Update view with new player position
        mazePanel.updatePlayerPosition(playerX, playerY);

        if (maze[newY][newX] == MazeGenerator.END) {
            completeMaze();
        }

        mazePanel.repaint();
    }

    /**
     * Handles maze completion.
     */
    private void completeMaze() {
        completed = true;
        final int timeSeconds = getElapsedTime();

        // Update view to show completion
        mazePanel.setMazeCompleted(true);

        if (completionListener != null) {
            completionListener.onMazeCompleted(true, timeSeconds);
        }

        LOGGER.info("Maze completed in " + timeSeconds + " seconds");
    }

    /**
     * Initializes the player position at the maze start.
     */
    private void initializePlayerPosition() {
        final int[] startPos = MazeGenerator.findStart(maze);
        if (startPos[0] != -1 && startPos[1] != -1) {
            playerX = startPos[0];
            playerY = startPos[1];
        } else {
            findFirstPathCell();
        }
        
        // Set initial player position in view
        mazePanel.updatePlayerPosition(playerX, playerY);
    }

    /**
     * Finds and sets the first available path cell as player position.
     */
    private void findFirstPathCell() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == MazeGenerator.PATH) {
                    playerX = col;
                    playerY = row;
                    return;
                }
            }
        }
        // Ultimate fallback
        playerX = 1;
        playerY = 1;
    }

    /**
     * Gets the current elapsed time in seconds.
     *
     * @return the elapsed time since maze start
     */
    public int getElapsedTime() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    /**
     * Checks if the maze has been completed.
     *
     * @return true if the maze is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Gets the current player X position.
     *
     * @return the player's x coordinate
     */
    public int getPlayerX() {
        return playerX;
    }

    /**
     * Gets the current player Y position.
     *
     * @return the player's y coordinate
     */
    public int getPlayerY() {
        return playerY;
    }

    /**
     * Sets the completion listener for maze events.
     *
     * @param listener the listener to set
     */
    public void setCompletionListener(final MazeCompletionListener listener) {
        this.completionListener = listener;
    }

    /**
     * Interface for handling maze completion events.
     */
    public interface MazeCompletionListener {
        /**
         * Called when the player reaches the end of the maze.
         *
         * @param success     true if completed successfully
         * @param timeSeconds time taken in seconds
         */
        void onMazeCompleted(boolean success, int timeSeconds);
    }
}