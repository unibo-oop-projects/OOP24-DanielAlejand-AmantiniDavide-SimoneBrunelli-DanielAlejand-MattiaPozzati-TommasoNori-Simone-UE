package it.unibo.exam.view.panel;

import it.unibo.exam.utility.geometry.Point2D;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Main menu panel for the game, displays play, options and exit buttons.
 * Updated to properly support minigame integration by passing parent frame reference.
 * This class is final as it's not designed for extension.
 */
public final class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(MainMenuPanel.class.getName());

    // Button size constants
    private static final int WIDTHBUTTON    = 800;
    private static final int HEIGHTBUTTON   = 80;
    private static final int BUTTONFONTSIZE = 30;
    private static final int BUTTONSPACING  = 20;

    // Color constants for magic numbers
    private static final int BUTTON_TEXT_RED      = 255;
    private static final int BUTTON_TEXT_GREEN    = 255;
    private static final int BUTTON_TEXT_BLUE     = 255;
    private static final int BUTTON_TEXT_ALPHA    = 220;
    private static final int BUTTON_BORDER_RADIUS = 30;

    /** The background image drawn behind the menu. */
    private transient Image backgroundImage;

    private GamePanel gamePanel; // Reference to track the game panel

    /**
     * Creates the main menu panel with buttons.
     *
     * @param window the parent JFrame window
     */
    public MainMenuPanel(final JFrame window) {
        initializeUI(window);
    }

    /**
     * Initialize the UI components.
     * This method is separated from the constructor to avoid calling overridable methods.
     *
     * @param window the parent JFrame window
     */
    private void initializeUI(final JFrame window) {
        setLayout(new GridBagLayout());
        setPreferredSize(window.getSize());

        // --- LOAD BACKGROUND IMAGE ---
        try {
            final var resource = getClass().getClassLoader().getResource("MainMenu/MainMenuBackGround.png");
            if (resource == null) {
                LOGGER.warning("Background image not found: MainMenu/MainMenuBackGround.png");
                backgroundImage = null;
                return;
            }
            backgroundImage = ImageIO.read(resource);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load background image: MainMenu/MainMenuBackGround.png", e);
            backgroundImage = null;
        }

        // Creates the panel where the buttons will stay
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(BUTTONSPACING, 0, BUTTONSPACING, 0); // Space between buttons
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; // Move the buttons in the centre

        // Buttons creation
        final JButton playButton = createStyledButton("Gioca");
        final JButton optionsButton = createStyledButton("Opzioni");
        final JButton exitButton    = createStyledButton("Esci");

        final Dimension buttonSize = new Dimension(WIDTHBUTTON, HEIGHTBUTTON);
        playButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        final Font buttonFont = new Font("Arial", Font.BOLD, BUTTONFONTSIZE);
        playButton.setFont(buttonFont);
        optionsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        gbc.gridx  = 0;
        gbc.insets = new Insets(BUTTONSPACING, 0, BUTTONSPACING, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        add(playButton, gbc);
        gbc.gridy = 1;
        add(optionsButton, gbc);
        gbc.gridy = 2;
        add(exitButton, gbc);

        playButton.addActionListener(e -> startGame(window));
        optionsButton.addActionListener(e ->
            JOptionPane.showMessageDialog(window, "Options not implemented yet.")
        );
        exitButton.addActionListener(e -> {
            final int confirmed = JOptionPane.showConfirmDialog(
                window,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmed == JOptionPane.YES_OPTION) {
                if (gamePanel != null) {
                    gamePanel.stopGame();
                }
                window.dispose();
            }
        });
    }

    private void startGame(final JFrame window) {
        window.getContentPane().removeAll();

        final Dimension size = window.getSize();
        final Point2D gameSize = new Point2D(size.width, size.height);

        gamePanel = new GamePanel(gameSize, window);
        window.add(gamePanel);
        gamePanel.requestFocusInWindow();
        window.revalidate();
        window.repaint();
        addReturnToMenuListener(window);
    }

    private void addReturnToMenuListener(final JFrame window) {
        if (gamePanel != null) {
            gamePanel.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                     .put(javax.swing.KeyStroke.getKeyStroke("ESCAPE"), "returnToMenu");
            gamePanel.getActionMap().put("returnToMenu",
                new javax.swing.AbstractAction() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void actionPerformed(final java.awt.event.ActionEvent e) {
                        returnToMenu(window);
                    }
                }
            );
        }
    }

    private void returnToMenu(final JFrame window) {
        final int confirmed = JOptionPane.showConfirmDialog(
            window,
            "Return to main menu?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );
        if (confirmed == JOptionPane.YES_OPTION) {
            if (gamePanel != null) {
                gamePanel.stopGame();
                gamePanel = null;
            }
            window.getContentPane().removeAll();
            window.add(this);
            window.revalidate();
            window.repaint();
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage,
                        0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton createStyledButton(final String text) {
        final JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(new java.awt.Color(
            BUTTON_TEXT_RED, BUTTON_TEXT_GREEN,
            BUTTON_TEXT_BLUE, BUTTON_TEXT_ALPHA
        ));
        button.setFont(new Font("Arial", Font.BOLD, BUTTONFONTSIZE));
        button.setPreferredSize(new Dimension(WIDTHBUTTON, HEIGHTBUTTON));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        final java.awt.Color baseColor  = new java.awt.Color(60, 120, 200, 150);
        final java.awt.Color hoverColor = new java.awt.Color(80, 140, 220, 180);
        final java.awt.Color clickColor = new java.awt.Color(30, 90, 180, 200);

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(final Graphics g, final javax.swing.JComponent c) {
                final Graphics g2 = g.create();
                if (button.getModel().isPressed()) {
                    g2.setColor(clickColor);
                } else if (button.getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(baseColor);
                }
                g2.fillRoundRect(
                    0, 0,
                    button.getWidth(), button.getHeight(),
                    BUTTON_BORDER_RADIUS, BUTTON_BORDER_RADIUS
                );
                g2.dispose();
                super.paint(g, c);
            }
        });

        return button;
    }
}
