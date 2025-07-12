package it.unibo.exam;

import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.utility.medialoader.AudioManager;
import it.unibo.exam.view.panel.MainMenuPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

/**
 * Main class for the UniversityEscape game.
 * Initializes the game window and starts the main menu.
 */
public final class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    }

    /**
     * Main method that starts the game.
     * @param args command line arguments (not used)
     */
    public static void main(final String[] args) {
        LOGGER.info("Starting UniversityEscape game...");

        // Initialize audio system
        initializeAudio();

        // Execute UI code in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            final KeyHandler keyHandler = new KeyHandler();
            final JFrame window = new JFrame();

            // Get screen dimensions
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int screenWidth = (int) screenSize.getWidth();
            final int screenHeight = (int) screenSize.getHeight();

            // Get graphics device for fullscreen support
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();

            // Create main menu panel
            final MainMenuPanel mainMenu = new MainMenuPanel(window);

            // Configure window
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("UniversityEscape");
            window.addKeyListener(keyHandler);

            // Add main menu panel
            window.getContentPane().add(mainMenu);

            // Set fullscreen mode
            final int initialWidth = (int) (screenWidth * 0.8);
            final int initialHeight = (int) (screenHeight * 0.8);
            window.setSize(initialWidth, initialHeight);
            window.setLocationRelativeTo(null);

            // Add window listeners
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(final WindowEvent e) {
                    LOGGER.info("Game window closing, cleaning up audio...");
                    AudioManager.cleanup();
                }
            });

            // Add global key listener for fullscreen toggle (F11) and exit (Alt+F4, Escape)
            window.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(final KeyEvent e) {
                    // Not used
                }

                @Override
                public void keyPressed(final KeyEvent e) {
                    // F11 to toggle fullscreen
                    if (e.getKeyCode() == KeyEvent.VK_F11) {
                        toggleFullscreen(window, gd);
                    } else if ((e.getKeyCode() == KeyEvent.VK_F4 && e.isAltDown()) 
                    || e.getKeyCode() == KeyEvent.VK_ESCAPE
                    && window.getContentPane().getComponent(0) instanceof MainMenuPanel) {
                        // Only exit from main menu, not during game
                        window.dispose();
                    }
                }

                @Override
                public void keyReleased(final KeyEvent e) {
                    // Not used
                }
            });

            // Make window visible
            window.setVisible(true);

            // Ensure we start in fullscreen
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            LOGGER.info("Game window displayed successfully");
        });
    }

    /**
     * Initializes the audio system with background music.
     */
    private static void initializeAudio() {
        // Try to start background music (will fail gracefully if no music file is found)
        final boolean musicStarted = AudioManager.playBackgroundMusic("audio/backgroundmusic.wav");
        if (musicStarted) {
            LOGGER.info("Background music started successfully");
        } else {
            LOGGER.info("No background music file found, continuing without music");
        }
    }

    /**
     * Toggles between fullscreen and windowed mode.
     * @param window the main window
     * @param graphicsDevice the graphics device
     */
    private static void toggleFullscreen(final JFrame window, final GraphicsDevice graphicsDevice) {

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();

        if (graphicsDevice.getFullScreenWindow().equals(window)) {
            // Exit fullscreen
            graphicsDevice.setFullScreenWindow(null);
            window.setUndecorated(false);
            window.setExtendedState(JFrame.NORMAL);
            final int windowWidth = (int) (screenWidth * 0.8);
            final int windowHeight = (int) (screenHeight * 0.8);
            window.setSize(windowWidth, windowHeight);
            window.setLocationRelativeTo(null);
        } else {
            // Enter fullscreen
            window.setUndecorated(false);
            if (graphicsDevice.isFullScreenSupported()) {
                graphicsDevice.setFullScreenWindow(window);
            } else {
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }
}
