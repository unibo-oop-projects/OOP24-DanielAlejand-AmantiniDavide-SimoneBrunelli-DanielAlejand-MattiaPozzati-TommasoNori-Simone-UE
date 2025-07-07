package it.unibo.exam.view.panel;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.Leaderboard.LeaderboardEntry;
import it.unibo.exam.model.Leaderboard.LeaderboardManage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
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
    private static final int LEADERBOARD_FONT_SIZE = 16;
    private static final int BUTTON_FONT_SIZE = 20;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int PANEL_PADDING = 30;

    // Panel Size Constants
    private static final int LEADERBOARD_PANEL_WIDTH = 500;
    private static final int LEADERBOARD_PANEL_HEIGHT = 400;
    private static final int BUTTON_PANEL_WIDTH = 250;
    private static final int BUTTON_PANEL_HEIGHT = 400;
    private static final int BUTTON_PANEL_TOP_MARGIN = 50;

    // Layout Constants
    private static final int INSETS_SMALL = 5;
    private static final int INSETS_MEDIUM = 10;
    private static final int INSETS_XL = 20;
    private static final int INSETS_XXL = 30;

    // Button Colors
    private static final int BUTTON_RED = 70;
    private static final int BUTTON_GREEN = 130;
    private static final int BUTTON_BLUE = 180;

    // Colors
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    private static final Color TITLE_COLOR = new Color(255, 215, 0); // Gold
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color STATS_COLOR = new Color(200, 200, 200); // Light gray for stats

    private final JFrame parentWindow;
    private final Player player;
    private final LeaderboardManage leaderboard;

    /**
     * Creates the end game menu with player statistics.
     *
     * @param parentWindow the parent window
     * @param player the player object containing completion data
     */
    public EndGameMenu(final JFrame parentWindow, final Player player) {
        this.parentWindow = parentWindow;
        this.player = player;
        this.leaderboard = new LeaderboardManage();
        initializeUI();
    }

    /**
     * Initializes the user interface components.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(parentWindow.getSize());

        // Title panel at the top
        final JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Main content with two columns
        final JPanel mainPanel = createMainContentPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the title panel with congratulations message.
     *
     * @return the title panel
     */
    private JPanel createTitlePanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, 
                                                       INSETS_MEDIUM, PANEL_PADDING));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(INSETS_MEDIUM, 0, INSETS_SMALL, 0);
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

        return panel;
    }

    /**
     * Creates the main content panel with leaderboard on left and buttons on right.
     *
     * @return the main content panel
     */
    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));

        // Add player to leaderboard first
        addToLeaderboardWithNamePrompt();

        // Left side: Leaderboard
        final JPanel leftPanel = createLeaderboardPanel();
        panel.add(leftPanel, BorderLayout.WEST);

        // Right side: Buttons
        final JPanel rightPanel = createButtonPanel();
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Creates the leaderboard panel.
     *
     * @return the leaderboard panel
     */
    private JPanel createLeaderboardPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(LEADERBOARD_PANEL_WIDTH, LEADERBOARD_PANEL_HEIGHT));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TITLE_COLOR, 2),
            BorderFactory.createEmptyBorder(INSETS_XL, INSETS_XXL, INSETS_XL, INSETS_XXL)
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(INSETS_SMALL, 0, INSETS_XL, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Leaderboard title
        final JLabel titleLabel = new JLabel("🏆 TOP 10 CLASSIFICA 🏆");
        titleLabel.setFont(new Font("Arial", Font.BOLD, SUBTITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        panel.add(titleLabel, gbc);

        // Leaderboard entries
        final List<LeaderboardEntry> entries = leaderboard.getTop10();
        gbc.anchor = GridBagConstraints.WEST;

        if (entries.isEmpty()) {
            gbc.gridy++;
            final JLabel emptyLabel = new JLabel("Nessun punteggio registrato");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, LEADERBOARD_FONT_SIZE));
            emptyLabel.setForeground(STATS_COLOR);
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(emptyLabel, gbc);
        } else {
            for (int i = 0; i < entries.size(); i++) {
                gbc.gridy++;
                final LeaderboardEntry entry = entries.get(i);
                final String position = (i + 1) + ".";

                // Check if this is the current player's entry (based on score only)
                final boolean isCurrentPlayer = entry.getScore() == player.getTotalScore();

                final String entryText = String.format("%-3s %-15s %4d pts",
                    position,
                    truncateName(entry.getPlayerName(), 15),
                    entry.getScore()
                );

                final JLabel entryLabel = new JLabel(entryText);
                entryLabel.setFont(new Font("Monospaced", Font.PLAIN, LEADERBOARD_FONT_SIZE));

                if (isCurrentPlayer) {
                    entryLabel.setForeground(TITLE_COLOR);
                    entryLabel.setFont(new Font("Monospaced", Font.BOLD, LEADERBOARD_FONT_SIZE));
                    entryLabel.setText("👑 " + entryText);
                } else {
                    entryLabel.setForeground(STATS_COLOR);
                }

                panel.add(entryLabel, gbc);
            }
        }

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
        panel.setBorder(BorderFactory.createEmptyBorder(BUTTON_PANEL_TOP_MARGIN, INSETS_XXL, 0, 0));
        panel.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, INSETS_XL, 0);

        // Menu button
        final JButton menuButton = createStyledButton("Menu Principale");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                returnToMainMenu();
            }
        });
        panel.add(menuButton, gbc);

        // Play again button
        gbc.gridy++;
        final JButton playAgainButton = createStyledButton("Gioca Ancora");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                startNewGame();
            }
        });
        panel.add(playAgainButton, gbc);

        // Exit button
        gbc.gridy++;
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

    /**
     * Adds the player to the leaderboard with a name prompt.
     * Uses the total score from player's completed rooms.
     */
    private void addToLeaderboardWithNamePrompt() {
        final int totalScore = player.getTotalScore(); // Usa il total score da Player
        final String playerName = JOptionPane.showInputDialog(
            parentWindow,
            "Inserisci il tuo nome per la classifica:",
            "Nome Giocatore",
            JOptionPane.QUESTION_MESSAGE
        );

        if (playerName != null && !playerName.trim().isEmpty()) {
            // Usa 0 come tempo fittizio, ora ci basiamo solo sul punteggio
            final boolean added = leaderboard.addScore(playerName.trim(), totalScore, 0);
            if (added) {
                JOptionPane.showMessageDialog(
                    parentWindow,
                    "Punteggio aggiunto alla classifica!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    /**
     * Truncates a name to the specified maximum length.
     *
     * @param name the name to truncate
     * @param maxLength the maximum length
     * @return the truncated name
     */
    private String truncateName(final String name, final int maxLength) {
        if (name.length() <= maxLength) {
            return name;
        }
        return name.substring(0, maxLength - 3) + "...";
    }
}
