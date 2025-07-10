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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * End game menu panel that displays the final score and completion statistics.
 * Shown when the player completes all puzzle rooms.
 */
@SuppressFBWarnings(
    value = {"SE_BAD_FIELD", "SE_BAD_FIELD_STORE"},
    justification = "EndGameMenu is not intended to be serialized,"
                + " contains game-specific non-serializable components"
)
public final class EndGameMenu extends JPanel {

    private static final long serialVersionUID = 1L;

    // UI Constants
    private static final int TITLE_FONT_SIZE = 48;
    private static final int SUBTITLE_FONT_SIZE = 24;
    private static final int LEADERBOARD_FONT_SIZE = 16;
    private static final int BUTTON_FONT_SIZE = 22;

    // Layout Constants
    private static final int PADDING = 30;                 // Panel borders
    private static final int SPACING = 20;                 // Element spacing
    private static final int BUTTON_WIDTH = 320;           // Button width
    private static final int BUTTON_HEIGHT = 70;           // Button height

    // Layout constants for panels
    private static final int LEADERBOARD_WIDTH = 500;      // Leaderboard panel width
    private static final int LEADERBOARD_HEIGHT = 400;     // Leaderboard panel height
    private static final int BUTTON_PANEL_TOP_PADDING = 30; // Top padding for button panel
    private static final int BUTTON_PANEL_RIGHT_PADDING = 50; // Right padding for button panel
    private static final int BUTTON_PANEL_LEFT_PADDING = 20; // Left padding for button panel
    private static final int BUTTON_PANEL_WIDTH = 380;     // Button panel width
    private static final int BUTTON_PANEL_HEIGHT = 400;    // Button panel height
    private static final int MAX_NAME_LENGTH = 15;         // Maximum length for player names
    private static final int NAME_TRUNCATION_SUFFIX = 3;   // Length of "..." suffix

    // Colors - Theme palette
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);     // Dark blue 
    private static final Color TITLE_COLOR = new Color(255, 215, 0);         // Gold
    private static final Color TEXT_COLOR = new Color(255, 255, 255);        // White
    private static final Color STATS_COLOR = new Color(200, 200, 200);       // Gray
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);       // Blue

    private final JFrame parentWindow;

    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final Player player;
    private final LeaderboardManage leaderboard;

    /**
     * Creates the end game menu with player statistics.
     *
     * @param parentWindow the parent window
     * @param player       the player object containing completion data
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
                   justification = "JFrame and Player references are needed for proper functionality. "
                   + "EndGameMenu is not intended to be serialized and these references are used internally.")
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public EndGameMenu(final JFrame parentWindow, final Player player) {
        this.parentWindow = parentWindow;
        this.player = player;
        this.leaderboard = new LeaderboardManage();

        // Setup layout
        super.setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(parentWindow.getSize());

        // Add components
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    /**
     * Creates the title panel with congratulations message.
     * 
     * @return the title panel containing congratulations text
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
        final JLabel titleLabel = createStyledLabel("=== CONGRATULAZIONI! ===", 
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
     * 
     * @return the main content panel containing leaderboard and buttons
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
     * 
     * @return the leaderboard panel displaying top 10 scores
     */
    private JPanel createLeaderboardPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(LEADERBOARD_WIDTH, LEADERBOARD_HEIGHT));
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
     * 
     * @param panel the panel to add entries to
     * @param gbc the grid bag constraints for layout
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
                truncateName(entry.getPlayerName(), MAX_NAME_LENGTH),
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
     * 
     * @return the button panel containing navigation buttons
     */
    private JPanel createButtonPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(BUTTON_PANEL_TOP_PADDING, 
                                                        BUTTON_PANEL_LEFT_PADDING, 
                                                        0, 
                                                        BUTTON_PANEL_RIGHT_PADDING)); // More centered positioning
        panel.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT)); // Adjusted width

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
     * 
     * @param text the text to display on the label
     * @param fontSize the font size for the label
     * @param fontStyle the font style
     * @param color the color of the label text
     * @return a styled JLabel with the specified properties
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
     * 
     * @param text the text to display on the button
     * @param action the action to execute when button is clicked
     * @return a styled JButton with the specified text and action
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

    private void returnToMainMenu() {
        parentWindow.getContentPane().removeAll();
        parentWindow.add(new MainMenuPanel(parentWindow));
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    private void startNewGame() {
        parentWindow.getContentPane().removeAll();
        final Dimension windowSize = parentWindow.getSize();
        final it.unibo.exam.utility.geometry.Point2D gameSize =
            new it.unibo.exam.utility.geometry.Point2D(
                windowSize.width, windowSize.height
            );
        final GamePanel gamePanel = new GamePanel(gameSize, parentWindow);
        parentWindow.add(gamePanel);
        gamePanel.requestFocusInWindow();
        parentWindow.revalidate();
        parentWindow.repaint();
    }

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

        if (playerName != null && !playerName.isBlank()) {
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
     * @param name the player name 
     * @param maxLength the maximum allowed length for the name
     * @return the truncated name with "..." suffix if needed, or the original name if within limit
     */
    private String truncateName(final String name, final int maxLength) {
        return name.length() <= maxLength ? name : name.substring(0, maxLength - NAME_TRUNCATION_SUFFIX) + "...";
    }
}
