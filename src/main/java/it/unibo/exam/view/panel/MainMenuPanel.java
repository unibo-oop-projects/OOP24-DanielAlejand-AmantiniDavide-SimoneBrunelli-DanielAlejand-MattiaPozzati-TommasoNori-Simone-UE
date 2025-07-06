package it.unibo.exam.view.panel;

import it.unibo.exam.utility.geometry.Point2D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main menu panel for the game, displays play, options and exit buttons.
 * Updated to properly support minigame integration by passing parent frame reference.
 */
public class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int WIDTHBUTTON = 800;
    private static final int HEIGHTBUTTON = 80;
    private static final int BUTTONFONTSIZE = 30;
    private static final int BUTTONSPACING = 20;

    /**
     * The background image drawn behind the bottles.
     */
    private transient Image backgroundImage;

    private GamePanel gamePanel; // Reference to track the game panel

    /**
     * Creates the main menu panel with buttons.
     *
     * @param window the parent JFrame window
     */
    public MainMenuPanel(final JFrame window) {
        createUI(window);
    }

    /**
     * Initialize the UI components.
     * This method is separated from the constructor to avoid calling overridable methods.
     *
     * @param window the parent JFrame window
     */
    private void createUI(final JFrame window) {
        // Panel layout for align the buttons
        super.setLayout(new BorderLayout());
        super.setPreferredSize(window.getSize());

        // --- LOAD BACKGROUND IMAGE ---
        try {
            var resource = getClass().getClassLoader().getResource("MainMenu/MainMenuBackGround.png");
            if (resource == null) {
                throw new IllegalArgumentException("Immagine non trovata!");
            }
            backgroundImage = ImageIO.read(resource);
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }

        // Creates the panel where the buttons will stay
        final JPanel buttonPanel = createButtonPanel();
        buttonPanel.setOpaque(false);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(BUTTONSPACING, 0, BUTTONSPACING, 0); // Space between buttons
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; // Move the buttons in the centre

        // Buttons creation
        final JButton playButton = createStyledButton("Gioca");
        final JButton optionsButton = createStyledButton("Opzioni");
        final JButton exitButton = createStyledButton("Esci");

        final Dimension buttonSize = new Dimension(WIDTHBUTTON, HEIGHTBUTTON);
        playButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // Button's font
        final Font buttonFont = new Font("Arial", Font.BOLD, BUTTONFONTSIZE);
        playButton.setFont(buttonFont);
        optionsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        // Adding the buttons to the panel
        buttonPanel.add(playButton, gbc);
        gbc.gridy++; // Move the next button lower
        buttonPanel.add(optionsButton, gbc);
        gbc.gridy++; // Move the next button lower
        buttonPanel.add(exitButton, gbc);

        // Adding the panel to the window
        super.add(buttonPanel, BorderLayout.CENTER);

        // Action listener for the button 'Play'
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Once you press 'Play', menu panel gets removed and the game starts 
                startGame(window);
            }
        });

        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Options not implemented yet
                JOptionPane.showMessageDialog(window, "Options not implemented yet.");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Show confirmation dialog
                final int confirmed = JOptionPane.showConfirmDialog(window,
                        "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION); 

                if (confirmed == JOptionPane.YES_OPTION) {
                    // Stop game if running
                    if (gamePanel != null) {
                        gamePanel.stopGame();
                    }
                    // Close the window instead of terminating the JVM
                    window.dispose();
                }
            }
        });
    }

    /**
     * Starts the game by removing the menu and adding the game panel.
     * Updated to pass the parent frame reference for minigame support.
     * 
     * @param window the parent window
     */
    private void startGame(final JFrame window) {
        // Remove the menu panel
        window.getContentPane().removeAll();

        // Create and add the game panel WITH parent frame reference
        final Dimension windowSize = window.getSize();
        final Point2D gameSize = new Point2D(windowSize.width, windowSize.height);

        // IMPORTANT: Pass the window reference to enable minigames
        gamePanel = new GamePanel(gameSize, window);

        window.add(gamePanel);

        // Ensure the game panel gets focus for key input
        gamePanel.requestFocusInWindow();

        // Refresh the window
        window.revalidate();
        window.repaint();

        // Add ESC key listener to return to menu
        addReturnToMenuListener(window);
    }

    /**
     * Adds a listener to return to the main menu (ESC key).
     * 
     * @param window the parent window
     */
    private void addReturnToMenuListener(final JFrame window) {
        if (gamePanel != null) {
            gamePanel.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(javax.swing.KeyStroke.getKeyStroke("ESCAPE"), "returnToMenu");

            gamePanel.getActionMap().put("returnToMenu", new javax.swing.AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(final ActionEvent e) {
                    returnToMenu(window);
                }
            });
        }
    }

    /**
     * Returns to the main menu from the game.
     * 
     * @param window the parent window
     */
    private void returnToMenu(final JFrame window) {
        final int confirmed = JOptionPane.showConfirmDialog(window,
                "Return to main menu?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            // Stop the game and any running minigames
            if (gamePanel != null) {
                gamePanel.stopGame();
                gamePanel = null;
            }

            // Remove game panel and restore menu
            window.getContentPane().removeAll();
            window.add(this);
            window.revalidate();
            window.repaint();
        }
    }

    /**
     * Creates a panel for the buttons with custom background painting.
     *
     * @return JPanel with GridBagLayout
     */
    private JPanel createButtonPanel() {
        return new JPanel(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton createStyledButton(final String text) {
    final JButton button = new JButton(text);

    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false); // non riempie il background in modo "standard"
    button.setOpaque(false); // importantissimo per la trasparenza
    button.setForeground(new java.awt.Color(255, 255, 255, 220));
    button.setFont(new Font("Arial", Font.BOLD, BUTTONFONTSIZE));
    button.setPreferredSize(new Dimension(WIDTHBUTTON, HEIGHTBUTTON));
    button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

     // Background semitrasparente iniziale (con alpha)
    java.awt.Color baseColor = new java.awt.Color(60, 120, 200, 150);
    java.awt.Color hoverColor = new java.awt.Color(80, 140, 220, 180);
    java.awt.Color clickColor = new java.awt.Color(30, 90, 180, 200);

    // Custom painting per sfondo trasparente
    button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void paint(Graphics g, javax.swing.JComponent c) {
            Graphics g2 = g.create();
            if (button.getModel().isPressed()) {
                g2.setColor(clickColor);
            } else if (button.getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(baseColor);
            }
            g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 30, 30);
            g2.dispose();
            super.paint(g, c);
        }
    });

    return button;
}
}
