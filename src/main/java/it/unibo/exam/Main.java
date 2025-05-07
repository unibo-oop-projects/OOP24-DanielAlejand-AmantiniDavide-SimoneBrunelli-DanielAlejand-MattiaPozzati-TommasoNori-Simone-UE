package it.unibo.exam;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import it.unibo.exam.controller.input.KeyHandler;

/**
 * Main application class that initializes the application window.
 */
public final class Main {
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    } 
    /**
     * Entry point of the application.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        // Execute UI code in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            final KeyHandler keyHandler = new KeyHandler();
            final JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("UNIBO");
            window.addKeyListener(keyHandler);
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int screenWidth = (int) screenSize.getWidth();
            final int screenHeight = (int) screenSize.getHeight();
            // Set default size for when window is not maximized (80% of screen size)
            final int defaultWidth = (int) (screenWidth * 0.8);
            final int defaultHeight = (int) (screenHeight * 0.8);
            window.setSize(defaultWidth, defaultHeight);
            // Add window listener to handle iconification (minimize) events
            window.addWindowListener(new WindowAdapter() {
                /***
                 * 
                 * @param e
                 * resize window
                 */
                @Override
                public void windowIconified(final WindowEvent e) {
                    // When minimized, restore to default size instead of minimizing
                    window.setExtendedState(JFrame.NORMAL);
                    window.setSize(defaultWidth, defaultHeight);
                    window.setVisible(true);
                }
            });
            window.setVisible(true);
            window.setLocationRelativeTo(null);
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            if (gd.isFullScreenSupported()) {
                window.setUndecorated(true);
                gd.setFullScreenWindow(window);
            }
        });
    }
}
