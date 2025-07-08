package it.unibo.exam.view.lab;

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

/**
 * Panel that renders a maze and handles player movement.
 * Displays maze walls, paths, start/end positions, and player.
 */
public final class MazePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // Visual constants
    private static final int CELL_SIZE = 30;
    private static final int UI_HEIGHT = 60;
    
    // Colors
    private static final Color WALL_COLOR = new Color(45, 70, 45);
    private static final Color PATH_COLOR = new Color(245, 240, 235);
    private static final Color PLAYER_COLOR = new Color(180, 140, 70);
    private static final Color START_COLOR = new Color(85, 140, 85);
    private static final Color END_COLOR = new Color(160, 82, 45);
    private static final Color BACKGROUND_COLOR = new Color(95, 125, 95);
    private static final Color UI_BACKGROUND = new Color(40, 60, 40);
    private static final Color TEXT_COLOR = new Color(240, 235, 220);

    // Game state
    private final int[][] maze;
    private int playerX, playerY;
    private boolean completed = false;
    private final long startTime = System.currentTimeMillis();
    
    // UI components
    private final JLabel statusLabel;
    private final JLabel timeLabel;
    
    // Listener for game completion
    private MazeCompletionListener completionListener;

    /**
     * Interface for handling maze completion events.
     */
    public interface MazeCompletionListener {
        /**
         * Called when the player reaches the end of the maze.
         * 
         * @param success true if completed successfully
         * @param timeSeconds time taken in seconds
         */
        void onMazeCompleted(boolean success, int timeSeconds);
    }

    /**
     * Constructs a new MazePanel with the given maze.
     *
     * @param maze the 2D array representing the maze
     */
    public MazePanel(final int[][] maze) {
        this.maze = maze;
        
        // Initialize UI components
        this.statusLabel = createStatusLabel();
        this.timeLabel = createTimeLabel();
        
        initializePlayerPosition();
        setupPanel();
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
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                handleKeyPress(e);
            }
        });
        
        requestFocusInWindow();
    }

    /**
     * Creates the status label.
     */
    private JLabel createStatusLabel() {
        final JLabel label = new JLabel("Use WASD or Arrow Keys to move - Reach the red square!", SwingConstants.CENTER);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setOpaque(true);
        label.setBackground(UI_BACKGROUND);
        return label;
    }

    /**
     * Creates the time label.
     */
    private JLabel createTimeLabel() {
        final JLabel label = new JLabel("Time: 0s", SwingConstants.CENTER);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(UI_BACKGROUND);
        return label;
    }

    /**
     * Handles key press events for player movement.
     */
    private void handleKeyPress(final KeyEvent e) {
        if (completed) return;
        
        int newX = playerX;
        int newY = playerY;
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> newY--;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> newY++;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> newX--;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> newX++;
            default -> { return; }
        }
        
        movePlayer(newX, newY);
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
            // Fallback: find first path cell
            findFirstPathCell();
        }
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
     * Moves the player to the specified position if valid.
     * 
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    private void movePlayer(final int newX, final int newY) {
        // Check bounds and walls
        if (newX < 0 || newX >= maze[0].length || 
            newY < 0 || newY >= maze.length ||
            maze[newY][newX] == MazeGenerator.WALL) {
            return;
        }
        
        // Move player
        playerX = newX;
        playerY = newY;
        
        // Check if reached the end
        if (maze[newY][newX] == MazeGenerator.END) {
            completeMaze();
        }
        
        repaint();
    }

    /**
     * Handles maze completion.
     */
    private void completeMaze() {
        completed = true;
        final int timeSeconds = getElapsedTime();
        
        statusLabel.setText("Maze Completed! Well done!");
        statusLabel.setForeground(Color.GREEN);
        
        if (completionListener != null) {
            completionListener.onMazeCompleted(true, timeSeconds);
        }
        
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        final Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
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
        final int mazeWidth = maze[0].length * CELL_SIZE;
        final int mazeHeight = maze.length * CELL_SIZE;
        
        final int offsetX = (getWidth() - mazeWidth) / 2;
        final int offsetY = (getHeight() - mazeHeight - UI_HEIGHT) / 2 + UI_HEIGHT;
        
        return new int[]{offsetX, offsetY};
    }

    /**
     * Draws the maze.
     * 
     * @param g2d the graphics context
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
     * @param g2d graphics context
     * @param x cell x position
     * @param y cell y position
     * @param cellType type of the cell
     */
    private void drawMazeCell(final Graphics2D g2d, final int x, final int y, final int cellType) {
        final Color cellColor = switch (cellType) {
            case MazeGenerator.WALL -> WALL_COLOR;
            case MazeGenerator.START -> START_COLOR;
            case MazeGenerator.END -> END_COLOR;
            default -> PATH_COLOR;
        };
        
        g2d.setColor(cellColor);
        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        
        // Draw border for non-wall cells
        if (cellType != MazeGenerator.WALL) {
            g2d.setColor(Color.GRAY);
            g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    /**
     * Draws the player on the maze.
     * 
     * @param g2d the graphics context
     * @param offsetX horizontal offset for centering
     * @param offsetY vertical offset for centering
     */
    private void drawPlayer(final Graphics2D g2d, final int offsetX, final int offsetY) {
        final int x = offsetX + playerX * CELL_SIZE;
        final int y = offsetY + playerY * CELL_SIZE;
        final int playerSize = CELL_SIZE - 6;
        
        // Draw player circle
        g2d.setColor(PLAYER_COLOR);
        g2d.fillOval(x + 3, y + 3, playerSize, playerSize);
        
        // Draw player border
        g2d.setColor(Color.WHITE);
        g2d.drawOval(x + 3, y + 3, playerSize, playerSize);
    }

    /**
     * Updates the time label with current elapsed time.
     */
    private void updateTimeLabel() {
        if (!completed) {
            timeLabel.setText("Time: " + getElapsedTime() + "s");
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        
        // Position UI elements at the top
        final int labelWidth = getWidth() - 20;
        final int labelHeight = 25;
        
        statusLabel.setBounds(10, 10, labelWidth, labelHeight);
        timeLabel.setBounds(10, 35, labelWidth / 2, labelHeight);
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculate size based on maze dimensions plus padding
        final int mazeWidth = maze[0].length * CELL_SIZE;
        final int mazeHeight = maze.length * CELL_SIZE;
        final int padding = 80; // Simplified padding
        
        return new Dimension(
            mazeWidth + padding,
            mazeHeight + padding + UI_HEIGHT
        );
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
}