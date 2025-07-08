package it.unibo.exam.utility.generator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates random mazes using recursive backtracking algorithm.
 * The maze consists of walls and paths, with guaranteed path from start to end.
 */
public class MazeGenerator {

    // Maze path
    public static final int PATH = 0; 
    // Maze wall
    public static final int WALL = 1; 
    // Maze start
    public static final int START = 2;
    // Maze end
    public static final int END = 3;

    // Costants for the maze level
    private static final int EASY = 7; // Easy maze size
    private static final int MEDIUM = 11; // Medium maze size
    private static final int HARD = 15; // Hard maze size

    // Number generator 
    private final Random random;

    /**
     * Creates a new MazeGenerator.
     */
    public MazeGenerator() {
        this.random = new Random();
    }

    /**
     * Generates a maze with predetermined difficulty levels for your game.
     * 
     * @param difficulty 1 = Easy (5x5), 2 = Medium (7x7), 3 = Hard (9x9)
     * @return a 2D array representing the maze
     */
    public int[][] generateMaze(final int difficulty) {
        // Determina dimensioni in base alla difficoltà
        final int size = switch (difficulty) {
            case 1 -> EASY; 
            case 2 -> MEDIUM;
            case 3 -> HARD; 
            default -> throw new IllegalArgumentException("Difficulty must be 1, 2, or 3");
        };

        // Generate the maze
        final int[][] maze = new int[size][size];
        initializeWalls(maze, size, size);
        generatePaths(maze, 1, 1, size, size);
        setStartAndEnd(maze, size, size);

        return maze;
    }

    /**
     * Prints the maze to console for debugging purposes.
     * 
     * @param maze the maze to print
     */
    public void printMaze(final int[][] maze) {
        for (final int[] row : maze) {
            for (final int cell : row) {
                switch (cell) {
                    case WALL -> System.out.print("██");
                    case PATH -> System.out.print("  ");
                    case START -> System.out.print("S ");
                    case END -> System.out.print("E ");
                    default -> System.out.print("? ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Gets the width of the maze.
     * 
     * @param maze the maze array
     * @return the width of the maze
     */
    public static int getWidth(final int[][] maze) {
        return maze.length > 0 ? maze[0].length : 0;
    }

    /**
     * Gets the height of the maze.
     * 
     * @param maze the maze array
     * @return the height of the maze
     */
    public static int getHeight(final int[][] maze) {
        return maze.length;
    }

    /**
     * Finds the start position in the maze.
     * 
     * @param maze the maze array
     * @return array with [x, y] coordinates of start, or [-1, -1] if not found
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
     * @return array with [x, y] coordinates of end, or [-1, -1] if not found
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
     * @param maze the maze array to initialize
     * @param height the height of the maze
     * @param width the width of the maze
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
     * @param maze the maze array
     * @param x current x position
     * @param y current y position
     * @param width maze width
     * @param height maze height
     */
    private void generatePaths(final int[][] maze, final int x, final int y,
                              final int width, final int height) {
        // Mark current cell as path
        maze[y][x] = PATH;

        // Get all possible directions in random order
        final List<Direction> directions = getRandomDirections();

        for (final Direction direction : directions) {
            final int newX = x + direction.getDx() * 2;
            final int newY = y + direction.getDy() * 2;

            // Check if the new position is valid and unvisited
            if (isValidPosition(newX, newY, width, height) && maze[newY][newX] == WALL) {
                // Remove wall between current and new position
                maze[y + direction.getDy()][x + direction.getDx()] = PATH;
                // Recursively generate from new position
                generatePaths(maze, newX, newY, width, height);
            }
        }
    }

    /**
     * Checks if a position is valid within the maze bounds.
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param width maze width
     * @param height maze height
     * @return true if position is valid
     */
    private boolean isValidPosition(final int x, final int y, final int width, final int height) {
        return x > 0 && x < width - 1 && y > 0 && y < height - 1;
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
     * @param maze the maze array
     * @param width maze width
     * @param height maze height
     */
    private void setStartAndEnd(final int[][] maze, final int width, final int height) {
        // Trova tutti i percorsi
        final List<int[]> paths = new ArrayList<>();
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                if (maze[row][col] == PATH) {
                    paths.add(new int[]{row, col});
                }
            }
        }

        if (paths.isEmpty()) {
            // Crea manualmente START e END se non ci sono percorsi
            maze[1][1] = START;
            maze[height - 2][width - 2] = END;
            return;
        }

        final int[] start = paths.get(0);
        maze[start[0]][start[1]] = START;

        final int[] end = paths.get(paths.size() - 1);
        maze[end[0]][end[1]] = END;
    }

    /**
     * Helper class to represent movement directions.
     */
    private static class Direction {
        private final int dx;
        private final int dy;

        /**
         * Constructor for Direction.
         * @param dx horizontal movement
         * @param dy vertical movement
         */
        Direction(final int dx, final int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        /**
         * Gets the horizontal movement.
         * @return horizontal delta
         */
        public int getDx() {
            return this.dx;
        }

        /**
         * Gets the vertical movement.
         * @return vertical delta
         */
        public int getDy() {
            return this.dy;
        }
    }
}
