package it.unibo.exam.view.panel;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.data.RoomScoreData;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * End game menu panel that displays the final score and completion statistics.
 * Shown when the player completes all puzzle rooms.
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "SE_BAD_FIELD_STORE"}, 
                   justification = "EndGameMenu is not intended to be serialized,"
                   + " contains game-specific non-serializable components")
public class EndGameMenu extends JPanel {

    private static final long serialVersionUID = 1L;

    // UI Constants
    private static final int TITLE_FONT_SIZE = 48;
    private static final int SUBTITLE_FONT_SIZE = 24;
    private static final int STATS_FONT_SIZE = 18;
    private static final int BUTTON_FONT_SIZE = 20;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int PANEL_PADDING = 30;

    // Layout Constants
    private static final int INSETS_SMALL = 5;
    private static final int INSETS_MEDIUM = 10;
    private static final int INSETS_LARGE = 15;
    private static final int INSETS_XL = 20;
    private static final int INSETS_XXL = 30;

    // Performance Thresholds
    private static final int EXCELLENT_SCORE = 400;
    private static final int GOOD_SCORE = 300;
    private static final int FAIR_SCORE = 200;

    // Button Colors
    private static final int BUTTON_RED = 70;
    private static final int BUTTON_GREEN = 130;
    private static final int BUTTON_BLUE = 180;

    // Colors
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    private static final Color TITLE_COLOR = new Color(255, 215, 0); // Gold
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color STATS_COLOR = new Color(200, 200, 200);
    private static final Color PANEL_BORDER_COLOR = new Color(70, 70, 90);

    private final JFrame parentWindow;
    private final Player player;

    /**
     * Creates the end game menu with player statistics.
     *
     * @param parentWindow the parent window
     * @param player the player object containing completion data
     */
    public EndGameMenu(final JFrame parentWindow, final Player player) {
        this.parentWindow = parentWindow;
        this.player = player;
        initializeUI();
    }

    /**
     * Initializes the user interface components.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(parentWindow.getSize());

        // Main content panel
        final JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        // Button panel
        final JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the main content panel with title and statistics.
     *
     * @return the content panel
     */
    private JPanel createContentPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, 
                                                       PANEL_PADDING, PANEL_PADDING));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(INSETS_MEDIUM, 0, INSETS_MEDIUM, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Congratulations title
        final JLabel titleLabel = new JLabel("🎉 CONGRATULAZIONI! 🎉");
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        final JLabel subtitleLabel = new JLabel("Hai completato tutti i puzzle!");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, SUBTITLE_FONT_SIZE));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(subtitleLabel, gbc);

        // Statistics panel
        gbc.gridy++;
        gbc.insets = new Insets(INSETS_XXL, 0, INSETS_XL, 0);
        final JPanel statsPanel = createStatisticsPanel();
        panel.add(statsPanel, gbc);

        return panel;
    }

    /**
     * Creates the statistics panel showing completion details.
     *
     * @return the statistics panel
     */
    private JPanel createStatisticsPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(INSETS_XL, INSETS_XXL, INSETS_XL, INSETS_XXL)
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(INSETS_SMALL, 0, INSETS_SMALL, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Total score
        final JLabel scoreLabel = new JLabel("Punteggio Finale: " + player.getTotalScore() + " punti");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, SUBTITLE_FONT_SIZE));
        scoreLabel.setForeground(TITLE_COLOR);
        panel.add(scoreLabel, gbc);

        // Detailed room statistics
        gbc.gridy++;
        gbc.insets = new Insets(INSETS_XL, 0, INSETS_MEDIUM, 0);
        final JLabel detailsLabel = new JLabel("Dettagli Completamento:");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, STATS_FONT_SIZE));
        detailsLabel.setForeground(TEXT_COLOR);
        panel.add(detailsLabel, gbc);

        // Room completion details
        final Map<Integer, RoomScoreData> roomScores = player.getRoomScores();
        int totalTime = 0;

        for (int roomId = 1; roomId <= 4; roomId++) {
            gbc.gridy++;
            gbc.insets = new Insets(INSETS_SMALL, 0, INSETS_SMALL, 0);

            final RoomScoreData roomData = roomScores.get(roomId);
            if (roomData != null) {
                totalTime += roomData.getTimeTaken();
                final String roomStats = String.format("%s: %ds - %d punti", 
                    roomData.getRoomName(), 
                    roomData.getTimeTaken(), 
                    roomData.getPointsGained());

                final JLabel roomLabel = new JLabel("✓ " + roomStats);
                roomLabel.setFont(new Font("Arial", Font.PLAIN, STATS_FONT_SIZE));
                roomLabel.setForeground(STATS_COLOR);
                panel.add(roomLabel, gbc);
            }
        }

        // Total time
        gbc.gridy++;
        gbc.insets = new Insets(INSETS_LARGE, 0, INSETS_SMALL, 0);
        final JLabel timeLabel = new JLabel("Tempo Totale: " + totalTime + " secondi");
        timeLabel.setFont(new Font("Arial", Font.BOLD, STATS_FONT_SIZE));
        timeLabel.setForeground(TEXT_COLOR);
        panel.add(timeLabel, gbc);

        // Performance evaluation
        gbc.gridy++;
        final JLabel performanceLabel = new JLabel(getPerformanceMessage(player.getTotalScore(), totalTime));
        performanceLabel.setFont(new Font("Arial", Font.ITALIC, STATS_FONT_SIZE));
        performanceLabel.setForeground(TITLE_COLOR);
        panel.add(performanceLabel, gbc);

        return panel;
    }

    /**
     * Creates the button panel with menu and exit options.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(INSETS_XL, 0, INSETS_XXL, 0));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, INSETS_LARGE, 0, INSETS_LARGE);

        // Menu button
        gbc.gridx = 0;
        final JButton menuButton = createStyledButton("Menu Principale");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                returnToMainMenu();
            }
        });
        panel.add(menuButton, gbc);

        // Play again button
        gbc.gridx = 1;
        final JButton playAgainButton = createStyledButton("Gioca Ancora");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                startNewGame();
            }
        });
        panel.add(playAgainButton, gbc);

        // Exit button
        gbc.gridx = 2;
        final JButton exitButton = createStyledButton("Esci");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                exitGame();
            }
        });
        panel.add(exitButton, gbc);

        return panel;
    }

    /**
     * Creates a styled button with consistent appearance.
     *
     * @param text the button text
     * @return the styled button
     */
    private JButton createStyledButton(final String text) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        button.setBackground(new Color(BUTTON_RED, BUTTON_GREEN, BUTTON_BLUE));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    /**
     * Gets a performance message based on score and time.
     *
     * @param totalScore the total score achieved
     * @param totalTime the total time taken
     * @return a performance message
     */
    private String getPerformanceMessage(final int totalScore, final int totalTime) {
        if (totalScore >= EXCELLENT_SCORE) {
            return "🏆 Prestazione Eccellente!";
        } else if (totalScore >= GOOD_SCORE) {
            return "🥈 Ottima Prestazione!";
        } else if (totalScore >= FAIR_SCORE) {
            return "🥉 Buona Prestazione!";
        } else {
            return "💪 Ben fatto! Prova a migliorare il tuo tempo!";
        }
    }

    /**
     * Returns to the main menu.
     */
    private void returnToMainMenu() {
        parentWindow.getContentPane().removeAll();
        final MainMenuPanel mainMenu = new MainMenuPanel(parentWindow);
        parentWindow.add(mainMenu);
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    /**
     * Starts a new game.
     */
    private void startNewGame() {
        parentWindow.getContentPane().removeAll();

        final Dimension windowSize = parentWindow.getSize();
        final it.unibo.exam.utility.geometry.Point2D gameSize = 
            new it.unibo.exam.utility.geometry.Point2D(windowSize.width, windowSize.height);

        final GamePanel gamePanel = new GamePanel(gameSize, parentWindow);
        parentWindow.add(gamePanel);
        gamePanel.requestFocusInWindow();
        
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    /**
     * Exits the game.
     */
    private void exitGame() {
        parentWindow.dispose();
    }
}
