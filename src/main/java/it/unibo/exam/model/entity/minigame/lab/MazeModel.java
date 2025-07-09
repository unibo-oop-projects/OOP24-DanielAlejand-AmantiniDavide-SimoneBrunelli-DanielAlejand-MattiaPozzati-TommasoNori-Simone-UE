package it.unibo.exam.model.entity.minigame.lab;

import it.unibo.exam.utility.generator.MazeGenerator;

public class MazeModel {
    private final int[][] maze;   // 2D array representing the maze (walls, paths, start, end)
    private int playerX;          // Player's current X position
    private int playerY;          // Player's current Y position
    private boolean completed;    // Is the maze completed?

    public MazeModel(int[][] maze) {
        this.maze = maze;
        this.completed = false;
        initializePlayerPosition();
    }

    // Moves the player by dx, dy (if valid)
    public boolean movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

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

    // Checks if the maze is completed (player reached the end)
    public boolean isCompleted() {
        return completed;
    }

    // Returns the player's current X position
    public int getPlayerX() {
        return playerX;
    }

    // Returns the player's current Y position
    public int getPlayerY() {
        return playerY;
    }

    // Initializes the player's starting position (based on the maze)
    private void initializePlayerPosition() {
        int[] startPos = MazeGenerator.findStart(maze);
        if (startPos[0] != -1 && startPos[1] != -1) {
            playerX = startPos[0];
            playerY = startPos[1];
        } else {
            // Fallback if no start is found in the maze
            playerX = 1;
            playerY = 1;
        }
    }


    // Returns the current maze grid
    public int[][] getMaze() {
        return maze;
    }
}
