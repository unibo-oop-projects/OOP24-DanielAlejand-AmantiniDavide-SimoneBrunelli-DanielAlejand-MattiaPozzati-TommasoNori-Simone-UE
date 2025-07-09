package it.unibo.exam.view.lab;

import it.unibo.exam.controller.minigame.lab.MazeMinigame;
import it.unibo.exam.utility.generator.MazeGenerator;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public final class MazePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // Visual constants
    private static final int CELL_SIZE = 30;
    private static final int UI_HEIGHT = 60;
    private static final int STATUS_FONT_SIZE = 14;
    private static final int TIME_FONT_SIZE = 12;
    private static final int PLAYER_BORDER_SIZE = 6;
    private static final int PADDING = 80;
    private static final int LABEL_MARGIN = 10;
    private static final int LABEL_HEIGHT = 25;
    private static final int TIME_LABEL_Y_OFFSET = 35;

    // Colors
    private static final Color WALL_COLOR = new Color(45, 70, 45);
    private static final Color PATH_COLOR = new Color(245, 240, 235);
    private static final Color PLAYER_COLOR  = new Color(180, 140, 70);
    private static final Color START_COLOR = new Color(85, 140, 85);
    private static final Color END_COLOR = new Color(160, 82, 45);
    private static final Color BACKGROUND_COLOR = new Color(95, 125, 95);
    private static final Color UI_BACKGROUND = new Color(40, 60, 40);
    private static final Color TEXT_COLOR = new Color(240, 235, 220);

    // Game state
    private final int[][] maze;
    private int playerX, playerY;
    private boolean completed;

    // UI components
    private final JLabel statusLabel;
    private final JLabel timeLabel;

    // Controller reference
    private transient MazeMinigame controller;

    /**
     * Constructs a new MazePanel with the given maze.
     *
     * @param maze the 2D array representing the maze
     */
    public MazePanel(final int[][] maze) {
        // Defensive copy of user-supplied array
        this.maze = deepCopy(maze);

        // Initialize UI components
        this.statusLabel = createStatusLabel();
        this.timeLabel   = createTimeLabel();
        this.completed   = false;

        setupPanel();
    }

    /**
     * Creates a deep copy of the given 2D int array.
     *
     * @param original the original maze array
     * @return a new array with the same contents
     */
    private static int[][] deepCopy(final int[][] original) {
        final int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    /**
     * Sets up the panel configuration and listeners.
     */
    private void setupPanel() {
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);

        add(statusLabel);
        add(timeLabel);

        // Setting up the key listener to delegate input to the controller
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (controller != null) {
                    int dx = 0, dy = 0;

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W, KeyEvent.VK_UP    -> dy = -1;
                        case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> dy = 1;
                        case KeyEvent.VK_A, KeyEvent.VK_LEFT  -> dx = -1;
                        case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> dx = 1;
                        default -> {
                            return; // If no valid key is pressed, do nothing
                        }
                    }
                    controller.handleKeyPress(dx, dy);
                }
            }
        });

        requestFocusInWindow();
    }

    /**
     * Creates the status label.
     *
     * @return the configured status label
     */
    private JLabel createStatusLabel() {
        final JLabel label = new JLabel(
            "Use WASD or Arrow Keys to move - Reach the red square!",
            SwingConstants.CENTER
        );
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", Font.BOLD, STATUS_FONT_SIZE));
        label.setOpaque(true);
        label.setBackground(UI_BACKGROUND);
        return label;
    }

    /**
     * Creates the time label.
     *
     * @return the configured time label
     */
    private JLabel createTimeLabel() {
        final JLabel label = new JLabel("Time: 0s", SwingConstants.CENTER);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Arial", Font.BOLD, TIME_FONT_SIZE));
        label.setOpaque(true);
        label.setBackground(UI_BACKGROUND);
        return label;
    }

    /**
     * Updates the player position (called by controller).
     *
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    public void updatePlayerPosition(final int newX, final int newY) {
        this.playerX = newX;
        this.playerY = newY;
    }

    /**
     * Sets the maze completion status (called by controller).
     *
     * @param completed true if the maze is completed
     */
    public void setMazeCompleted(final boolean completed) {
        this.completed = completed;
        if (completed) {
            statusLabel.setText("Maze Completed! Well done!");
            statusLabel.setForeground(Color.GREEN);
        }
    }

    /**
     * Sets the controller reference.
     *
     * @param controller the maze controller
     */
    public void setController(final MazeMinigame controller) {
        this.controller = controller;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            final int[] offsets = calculateOffsets();
            drawMaze(g2d, offsets[0], offsets[1]);
            drawPlayer(g2d, offsets[0], offsets[1]);
            updateTimeLabel();
        } finally {
            g2d.dispose();
        }
    }

    /**
     * Calculates the maze rendering offsets for centering.
     *
     * @return array with [offsetX, offsetY]
     */
    private int[] calculateOffsets() {
        final int mazeWidth  = maze[0].length * CELL_SIZE;
        final int mazeHeight = maze.length * CELL_SIZE;
        final int offsetX    = (getWidth() - mazeWidth) / 2;
        final int offsetY    = (getHeight() - mazeHeight - UI_HEIGHT) / 2 + UI_HEIGHT;
        return new int[]{offsetX, offsetY};
    }

    /**
     * Draws the maze.
     *
     * @param g2d    the graphics context
     * @param offsetX horizontal offset for centering
     * @param offsetY vertical offset for centering
     */
    private void drawMaze(final Graphics2D g2d, final int offsetX, final int offsetY) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                final int x = offsetX + col * CELL_SIZE;
                final int y = offsetY + row * CELL_SIZE;
                drawMazeCell(g2d, x, y, maze[row][col]);
            }
        }
    }

    /**
     * Draws a single maze cell.
     *
     * @param g2d     graphics context
     * @param x       cell x position
     * @param y       cell y position
     * @param cellType type of the cell
     */
    private void drawMazeCell(final Graphics2D g2d, final int x, final int y, final int cellType) {
        final Color cellColor = switch (cellType) {
            case MazeGenerator.WALL  -> WALL_COLOR;
            case MazeGenerator.START -> START_COLOR;
            case MazeGenerator.END   -> END_COLOR;
            default                  -> PATH_COLOR;
        };
        g2d.setColor(cellColor);
        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        if (cellType != MazeGenerator.WALL) {
            g2d.setColor(Color.GRAY);
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    /**
     * Draws the player on the maze.
     *
     * @param g2d    the graphics context
     * @param offsetX horizontal offset for centering
     * @param offsetY vertical offset for centering
     */
    private void drawPlayer(final Graphics2D g2d, final int offsetX, final int offsetY) {
        final int x = offsetX + playerX * CELL_SIZE;
        final int y = offsetY + playerY * CELL_SIZE;
        final int playerSize = CELL_SIZE - PLAYER_BORDER_SIZE;
        g2d.setColor(PLAYER_COLOR);
        g2d.fillOval(x + 3, y + 3, playerSize, playerSize);
        g2d.setColor(Color.WHITE);
        g2d.drawOval(x + 3, y + 3, playerSize, playerSize);
    }

    /**
     * Updates the time label with current elapsed time.
     */
    private void updateTimeLabel() {
        if (!completed && controller != null) {
            timeLabel.setText("Time: " + controller.getElapsedTimeSeconds() + "s");
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        final int labelWidth  = getWidth() - 20;
        final int labelHeight = LABEL_HEIGHT;
        statusLabel.setBounds(LABEL_MARGIN, LABEL_MARGIN, labelWidth, labelHeight);
        timeLabel.setBounds(LABEL_MARGIN, TIME_LABEL_Y_OFFSET, labelWidth / 2, labelHeight);
    }

    @Override
    public Dimension getPreferredSize() {
        final int mazeWidth  = maze[0].length * CELL_SIZE;
        final int mazeHeight = maze.length * CELL_SIZE;
        return new Dimension(
            mazeWidth + PADDING,
            mazeHeight + PADDING + UI_HEIGHT
        );
    }

    /**
     * Sets the completion listener for maze events.
     * This method is kept for backward compatibility.
     *
     * @param listener the listener to set
     */
    public void setCompletionListener(final MazeCompletionListener listener) {
        // This method is now handled by the controller
        // but kept for backward compatibility
    }

    /**
     * Gets the current elapsed time in seconds.
     * This method delegates to the controller.
     *
     * @return the elapsed time since maze start
     */
    public int getElapsedTime() {
        return controller != null ? controller.getElapsedTimeSeconds() : 0;
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
     * Interface for handling maze completion events.
     * Kept for backward compatibility.
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
