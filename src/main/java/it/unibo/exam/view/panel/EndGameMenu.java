package it.unibo.exam.view.panel;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.leaderboard.LeaderboardEntry;
import it.unibo.exam.model.leaderboard.LeaderboardManage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

    // UI Constants - Font sizes for different text elements
    private static final int TITLE_FONT_SIZE = 48;         // Main congratulations title
    private static final int SUBTITLE_FONT_SIZE = 24;      // Secondary text
    private static final int LEADERBOARD_FONT_SIZE = 16;   // Score entries
    private static final int BUTTON_FONT_SIZE = 22;        // Button text (adjusted for smaller buttons)
    
    // Layout Constants - Spacing and component dimensions
    private static final int PADDING = 30;                 // Panel borders
    private static final int SPACING = 20;                 // Element spacing
    private static final int BUTTON_WIDTH = 320;           // Button width (slightly smaller than MainMenu)
    private static final int BUTTON_HEIGHT = 70;           // Button height (slightly smaller than MainMenu)

    // Colors - Theme palette
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);     // Dark blue background
    private static final Color TITLE_COLOR = new Color(255, 215, 0);         // Gold for titles
    private static final Color TEXT_COLOR = new Color(255, 255, 255);        // White for text
    private static final Color STATS_COLOR = new Color(200, 200, 200);       // Gray for scores
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);       // Blue for buttons

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

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    /**
     * Creates the title panel with congratulations message.
     */
    private JPanel createTitlePanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, 0, PADDING));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        final JLabel titleLabel = createStyledLabel("★ CONGRATULAZIONI! ★", 
                                                   TITLE_FONT_SIZE, Font.BOLD, TITLE_COLOR);
        panel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        final JLabel subtitleLabel = createStyledLabel("Hai completato tutti i puzzle!", 
                                                     SUBTITLE_FONT_SIZE, Font.PLAIN, TEXT_COLOR);
        panel.add(subtitleLabel, gbc);

        return panel;
    }

    /**
     * Creates the main content panel with leaderboard and buttons.
     */
    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, PADDING, PADDING));

        // Add player to leaderboard first
        addPlayerToLeaderboard();

        // Left: Leaderboard
        panel.add(createLeaderboardPanel(), BorderLayout.WEST);
        
        // Center: Buttons (moved from east for better positioning)
        panel.add(createButtonPanel(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the leaderboard panel.
     */
    private JPanel createLeaderboardPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(500, 400));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TITLE_COLOR, 2),
            BorderFactory.createEmptyBorder(SPACING, PADDING, SPACING, PADDING)
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, SPACING, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Leaderboard title
        final JLabel titleLabel = createStyledLabel("*** TOP 10 CLASSIFICA ***", 
                                                   SUBTITLE_FONT_SIZE, Font.BOLD, TITLE_COLOR);
        panel.add(titleLabel, gbc);

        // Add entries
        addLeaderboardEntries(panel, gbc);

        return panel;
    }

    /**
     * Adds leaderboard entries to the panel.
     */
    private void addLeaderboardEntries(final JPanel panel, final GridBagConstraints gbc) {
        final List<LeaderboardEntry> entries = leaderboard.getTop10();
        gbc.anchor = GridBagConstraints.WEST;

        if (entries.isEmpty()) {
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.CENTER;
            final JLabel emptyLabel = createStyledLabel("Nessun punteggio registrato", 
                                                       LEADERBOARD_FONT_SIZE, Font.ITALIC, STATS_COLOR);
            panel.add(emptyLabel, gbc);
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            gbc.gridy++;
            final LeaderboardEntry entry = entries.get(i);
            final boolean isCurrentPlayer = entry.getScore() == player.getTotalScore();
            
            final String entryText = String.format("%-3s %-15s %4d pts",
                (i + 1) + ".",
                truncateName(entry.getPlayerName(), 15),
                entry.getScore()
            );

            final JLabel entryLabel = new JLabel(isCurrentPlayer ? "♔ " + entryText : entryText);
            entryLabel.setFont(new Font("Monospaced", 
                                      isCurrentPlayer ? Font.BOLD : Font.PLAIN, 
                                      LEADERBOARD_FONT_SIZE));
            entryLabel.setForeground(isCurrentPlayer ? TITLE_COLOR : STATS_COLOR);

            panel.add(entryLabel, gbc);
        }
    }

    /**
     * Creates the button panel.
     */
    private JPanel createButtonPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 50)); // More centered positioning
        panel.setPreferredSize(new Dimension(380, 400)); // Adjusted width

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, SPACING * 2, 0); // Good spacing between buttons

        // Add buttons
        panel.add(createStyledButton("Menu Principale", this::returnToMainMenu), gbc);
        
        gbc.gridy++;
        panel.add(createStyledButton("Gioca Ancora", this::startNewGame), gbc);
        
        gbc.gridy++;
        panel.add(createStyledButton("Esci", this::exitGame), gbc);

        return panel;
    }

    /**
     * Creates a styled label with specified properties.
     */
    private JLabel createStyledLabel(final String text, final int fontSize, 
                                   final int fontStyle, final Color color) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", fontStyle, fontSize));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Creates a styled button with action listener.
     */
    private JButton createStyledButton(final String text, final Runnable action) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addActionListener(e -> action.run());
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
     * Adds the player to the leaderboard with name prompt.
     */
    private void addPlayerToLeaderboard() {
        final int totalScore = player.getTotalScore();
        final String playerName = JOptionPane.showInputDialog(
            parentWindow,
            "Inserisci il tuo nome per la classifica:",
            "Nome Giocatore",
            JOptionPane.QUESTION_MESSAGE
        );

        if (playerName != null && !playerName.trim().isEmpty()) {
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
     */
    private String truncateName(final String name, final int maxLength) {
        return name.length() <= maxLength ? name : name.substring(0, maxLength - 3) + "...";
    }
}