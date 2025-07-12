package it.unibo.exam.model.entity.minigame.lab;

/**
 * The MazeModel class represents the state of the maze game.
 * It stores the maze grid, player position, and completion status.
 * The model handles player movement, maze completion check, 
 * and provides methods to interact with the maze.
 */
public final class MazeModel {  // Marking class as final to prevent extension

    private final int[][] maze;   // 2D array representing the maze (walls, paths, start, end)
    private int playerX;          // Player's current X position
    private int playerY;          // Player's current Y position
    private boolean completed;    // Is the maze completed?

    /**
     * Constructs a new MazeModel with the given maze grid.
     * Initializes the player's position and sets the maze as incomplete.
     * 
     * @param maze The 2D maze grid to initialize the model.
     */
    public MazeModel(final int[][] maze) {
        // Make a defensive copy of the maze array to prevent external modification
        this.maze = new int[maze.length][];
        for (int i = 0; i < maze.length; i++) {
            this.maze[i] = maze[i].clone();  // Clone each row of the 2D array
        }
        this.completed = false;
        initializePlayerPosition();
    }

    /**
     * Moves the player by dx, dy (if valid).
     * 
     * @param dx The change in the player's x-coordinate.
     * @param dy The change in the player's y-coordinate.
     * @return true if the move was valid, false if the move was blocked (e.g., by a wall).
     */
    public boolean movePlayer(final int dx, final int dy) {
        final int newX = playerX + dx;
        final int newY = playerY + dy;

        // Validate move (within bounds and not a wall)
        if (newX < 0 || newX >= maze[0].length || newY < 0 || newY >= maze.length || maze[newY][newX] == MazeGenerator.WALL) {
            return false;  // Invalid move
        }

        // Update player position
        playerX = newX;
        playerY = newY;

        // Check if the player has reached the exit
        if (maze[newY][newX] == MazeGenerator.END) {
            completed = true;
        }
        return true; // Valid move
    }

    /**
     * Checks if the maze is completed (player reached the end).
     * 
     * @return true if the maze is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Returns the player's current X position.
     * 
     * @return The player's current X position.
     */
    public int getPlayerX() {
        return playerX;
    }

    /**
     * Returns the player's current Y position.
     * 
     * @return The player's current Y position.
     */
    public int getPlayerY() {
        return playerY;
    }

    /**
     * Initializes the player's starting position based on the maze.
     * If the start position is not found, it falls back to the default position.
     */
    private void initializePlayerPosition() {
        final int[] startPos = MazeGenerator.findStart(maze);  // Find start position in the maze
        if (startPos[0] != -1 && startPos[1] != -1) {
            playerX = startPos[0];
            playerY = startPos[1];
        } else {
            // Fallback if no start is found in the maze
            playerX = 1;
            playerY = 1;
        }
    }

    /**
     * Returns a copy of the current maze grid to prevent external modification.
     * 
     * @return A 2D array representing the maze.
     */
    public int[][] getMaze() {
        // Create a defensive copy of the maze array before returning it
        final int[][] mazeCopy = new int[maze.length][];
        for (int i = 0; i < maze.length; i++) {
            mazeCopy[i] = maze[i].clone();  // Clone each row of the 2D array
        }
        return mazeCopy;
    }
}
