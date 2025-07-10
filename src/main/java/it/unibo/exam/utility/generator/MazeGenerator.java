package it.unibo.exam.utility.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Generates random mazes using recursive backtracking algorithm.
 * The maze consists of walls and paths, with guaranteed path from start to end.
 */
public class MazeGenerator {

    // --- public static constants (paths & markers) -------------------

    /** Constant representing a path cell in the maze. */
    public static final int PATH  = 0;
    /** Constant representing a wall cell in the maze. */
    public static final int WALL  = 1;
    /** Constant representing the start position in the maze. */
    public static final int START = 2;
    /** Constant representing the end position in the maze. */
    public static final int END   = 3;

    // --- private static constants (sizes & logger) -------------------

    /** Easy maze “internal” size (5×5 display). */
    private static final int EASY   = 7;
    /** Medium maze “internal” size (7×7 display). */
    private static final int MEDIUM = 11;
    /** Hard maze “internal” size (9×9 display). */
    private static final int HARD   = 15;

    /** Logger for debug messages. */
    private static final Logger LOGGER = Logger.getLogger(MazeGenerator.class.getName());

    // --- instance fields ----------------------------------------------

    /** Random‐number generator for carving paths. */
    private final Random random;

    /**
     * Creates a new MazeGenerator.
     */
    public MazeGenerator() {
        this.random = new Random();
    }

    /**
     * Generates a maze with predetermined difficulty levels.
     *
     * @param difficulty 1 = Easy (5x5), 2 = Medium (7x7), 3 = Hard (9x9)
     * @return a 2D array representing the maze, with WALL, PATH, START and END markers
     */
    public int[][] generateMaze(final int difficulty) {
        final int size = switch (difficulty) {
            case 1 -> EASY;
            case 2 -> MEDIUM;
            case 3 -> HARD;
            default -> throw new IllegalArgumentException("Difficulty must be 1, 2, or 3");
        };

        final int[][] maze = new int[size][size];
        initializeWalls(maze, size, size);
        generatePaths(maze, 1, 1, size, size);
        setStartAndEnd(maze, size, size);
        return maze;
    }

    /**
     * Logs the maze to the application's logger at INFO level for debugging purposes.
     *
     * @param maze the maze to log
     */
    public void printMaze(final int[][] maze) {
        for (final int[] row : maze) {
            final StringBuilder sb = new StringBuilder();
            for (final int cell : row) {
                switch (cell) {
                    case WALL  -> sb.append("██");
                    case PATH  -> sb.append("  ");
                    case START -> sb.append("S ");
                    case END   -> sb.append("E ");
                    default    -> sb.append("? ");
                }
            }
            LOGGER.info(sb.toString());
        }
    }

    /**
     * Gets the width of the maze.
     *
     * @param maze the maze array
     * @return the width (number of columns) of the maze
     */
    public static int getWidth(final int[][] maze) {
        return maze.length > 0
            ? maze[0].length
            : 0;
    }

    /**
     * Gets the height of the maze.
     *
     * @param maze the maze array
     * @return the height (number of rows) of the maze
     */
    public static int getHeight(final int[][] maze) {
        return maze.length;
    }

    /**
     * Finds the start position in the maze.
     *
     * @param maze the maze array
     * @return array of two ints [x, y] for the START cell, or [-1, -1] if not found
     */
    public static int[] findStart(final int[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == START) {
                    return new int[]{col, row};
                }
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * Finds the end position in the maze.
     *
     * @param maze the maze array
     * @return array of two ints [x, y] for the END cell, or [-1, -1] if not found
     */
    public static int[] findEnd(final int[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == END) {
                    return new int[]{col, row};
                }
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * Initializes the maze grid with walls.
     *
     * @param maze   the maze array to initialize
     * @param height the height of the maze
     * @param width  the width of the maze
     */
    private void initializeWalls(final int[][] maze, final int height, final int width) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                maze[row][col] = WALL;
            }
        }
    }

    /**
     * Generates paths in the maze using recursive backtracking algorithm.
     *
     * @param maze   the maze array
     * @param x      current x position
     * @param y      current y position
     * @param width  maze width
     * @param height maze height
     */
    private void generatePaths(final int[][] maze, final int x, final int y,
                               final int width, final int height) {
        maze[y][x] = PATH;
        final List<Direction> directions = getRandomDirections();
        for (final Direction direction : directions) {
            final int newX = x + direction.getDx() * 2;
            final int newY = y + direction.getDy() * 2;
            if (isValidPosition(newX, newY, width, height)
                && maze[newY][newX] == WALL) {
                maze[y + direction.getDy()][x + direction.getDx()] = PATH;
                generatePaths(maze, newX, newY, width, height);
            }
        }
    }

    /**
     * Checks if a position is valid within the maze bounds.
     *
     * @param x      x coordinate
     * @param y      y coordinate
     * @param width  maze width
     * @param height maze height
     * @return true if position is valid
     */
    private boolean isValidPosition(final int x, final int y,
                                    final int width, final int height) {
        return x > 0 && x < width - 1
            && y > 0 && y < height - 1;
    }

    /**
     * Gets all four directions in random order.
     *
     * @return list of directions shuffled randomly
     */
    private List<Direction> getRandomDirections() {
        final List<Direction> directions = new ArrayList<>();
        directions.add(new Direction(0, -1)); // Up
        directions.add(new Direction(1, 0));  // Right
        directions.add(new Direction(0, 1));  // Down
        directions.add(new Direction(-1, 0)); // Left
        Collections.shuffle(directions, random);
        return directions;
    }

    /**
     * Sets the start and end positions in the maze.
     * Start is typically at top-left area, end at bottom-right area.
     *
     * @param maze   the maze array
     * @param width  maze width
     * @param height maze height
     */
    private void setStartAndEnd(final int[][] maze, final int width,
                                final int height) {
        final List<int[]> paths = new ArrayList<>();
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                if (maze[row][col] == PATH) {
                    paths.add(new int[]{row, col});
                }
            }
        }
        if (paths.isEmpty()) {
            maze[1][1] = START;
            maze[height - 2][width - 2] = END;
        } else {
            final int[] start = paths.get(0);
            maze[start[0]][start[1]] = START;
            final int[] end = paths.get(paths.size() - 1);
            maze[end[0]][end[1]] = END;
        }
    }

    /**
     * Helper class to represent movement directions.
     */
    private static class Direction {
        private final int dx;
        private final int dy;

        /**
         * Constructor for Direction.
         *
         * @param dx horizontal movement
         * @param dy vertical movement
         */
        Direction(final int dx, final int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        /**
         * Gets the horizontal movement.
         *
         * @return the x-axis delta of this direction
         */
        public int getDx() {
            return dx;
        }

        /**
         * Gets the vertical movement.
         *
         * @return the y-axis delta of this direction
         */
        public int getDy() {
            return dy;
        }
    }
}
