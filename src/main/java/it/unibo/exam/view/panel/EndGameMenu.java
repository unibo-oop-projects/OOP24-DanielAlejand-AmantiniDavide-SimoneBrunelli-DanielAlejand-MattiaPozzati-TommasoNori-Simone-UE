package it.unibo.exam.view.panel;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.leaderboard.LeaderboardEntry1;
import it.unibo.exam.model.leaderboard.LeaderboardManage1;

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

    // Font
    private static final String FONT_NAME = "Arial";

    // UI Constants
    private static final int TITLE_FONT_SIZE        = 48;
    private static final int SUBTITLE_FONT_SIZE     = 24;
    private static final int LEADERBOARD_FONT_SIZE  = 16;
    private static final int BUTTON_FONT_SIZE       = 20;
    private static final int BUTTON_WIDTH           = 200;
    private static final int BUTTON_HEIGHT          = 50;
    private static final int PANEL_PADDING          = 30;

    // Panel Size Constants
    private static final int LEADERBOARD_PANEL_WIDTH  = 500;
    private static final int LEADERBOARD_PANEL_HEIGHT = 400;
    private static final int BUTTON_PANEL_WIDTH       = 250;
    private static final int BUTTON_PANEL_HEIGHT      = 400;
    private static final int BUTTON_PANEL_TOP_MARGIN  = 50;

    // Layout Constants
    private static final int INSETS_SMALL  = 5;
    private static final int INSETS_MEDIUM = 10;
    private static final int INSETS_XL     = 20;
    private static final int INSETS_XXL    = 30;

    // Button Colors
    private static final int BUTTON_RED   = 70;
    private static final int BUTTON_GREEN = 130;
    private static final int BUTTON_BLUE  = 180;

    // Colors
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    private static final Color TITLE_COLOR      = new Color(255, 215, 0);
    private static final Color TEXT_COLOR       = new Color(255, 255, 255);
    private static final Color STATS_COLOR      = new Color(200, 200, 200);

    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final JFrame parentWindow;

    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final Player player;

    private final LeaderboardManage1 leaderboard;

    /**
     * Creates the end game menu with player statistics.
     *
     * @param parentWindow the parent window
     * @param player       the player object containing completion data
     */
    public EndGameMenu(final JFrame parentWindow, final Player player) {
        this.parentWindow = parentWindow;
        this.player       = player;
        this.leaderboard  = new LeaderboardManage1();
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

    private JPanel createTitlePanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(
            PANEL_PADDING, PANEL_PADDING, INSETS_MEDIUM, PANEL_PADDING
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 0;
        gbc.insets  = new Insets(INSETS_MEDIUM, 0, INSETS_SMALL, 0);
        gbc.anchor  = GridBagConstraints.CENTER;

        // Title
        final JLabel titleLabel = new JLabel("🎉 CONGRATULAZIONI! 🎉");
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        final JLabel subtitleLabel = new JLabel("Hai completato tutti i puzzle!");
        subtitleLabel.setFont(new Font(FONT_NAME, Font.PLAIN, SUBTITLE_FONT_SIZE));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(subtitleLabel, gbc);

        return panel;
    }

    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(
            0, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING
        ));

        addToLeaderboardWithNamePrompt();
        panel.add(createLeaderboardPanel(), BorderLayout.WEST);
        panel.add(createButtonPanel(),       BorderLayout.EAST);

        return panel;
    }

    private JPanel createLeaderboardPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(
            LEADERBOARD_PANEL_WIDTH, LEADERBOARD_PANEL_HEIGHT
        ));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TITLE_COLOR, 2),
            BorderFactory.createEmptyBorder(
                INSETS_XL, INSETS_XXL, INSETS_XL, INSETS_XXL
            )
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 0;
        gbc.insets  = new Insets(INSETS_SMALL, 0, INSETS_XL, 0);
        gbc.anchor  = GridBagConstraints.CENTER;

        // Title
        final JLabel titleLabel = new JLabel("🏆 TOP 10 CLASSIFICA 🏆");
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, SUBTITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        panel.add(titleLabel, gbc);

        final List<LeaderboardEntry1> entries = leaderboard.getTop10();
        gbc.anchor = GridBagConstraints.WEST;

        if (entries.isEmpty()) {
            gbc.gridy++;
            final JLabel emptyLabel = createLeaderboardLabel("Nessun punteggio registrato", false, true);
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(emptyLabel, gbc);
        } else {
            for (int i = 0; i < entries.size(); i++) {
                gbc.gridy++;
                final LeaderboardEntry1 entry = entries.get(i);
                final boolean isCurrent = entry.getScore() == player.getTotalScore();
                final String text = String.format(
                    "%-3s %-15s %4d pts",
                    (i + 1) + ".",
                    truncateName(entry.getPlayerName(), 15),
                    entry.getScore()
                );
                final JLabel entryLabel = createLeaderboardLabel(
                    isCurrent ? "👑 " + text : text,
                    isCurrent,
                    false
                );
                panel.add(entryLabel, gbc);
            }
        }
        return panel;
    }

    // Helper to remove duplicate JLabel code and fix PMD warnings
    private JLabel createLeaderboardLabel(final String text, final boolean highlight, final boolean italic) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(
            italic ? FONT_NAME : Font.MONOSPACED,
            highlight ? Font.BOLD : italic ? Font.ITALIC : Font.PLAIN,
            LEADERBOARD_FONT_SIZE
        ));
        label.setForeground(highlight ? TITLE_COLOR : STATS_COLOR);
        return label;
    }

    private JPanel createButtonPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(
            BUTTON_PANEL_TOP_MARGIN, INSETS_XXL, 0, 0
        ));
        panel.setPreferredSize(new Dimension(
            BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.insets = new Insets(0, 0, INSETS_XL, 0);

        final JButton menuButton = createStyledButton("Menu Principale");
        menuButton.addActionListener(e -> returnToMainMenu());
        panel.add(menuButton, gbc);

        gbc.gridy++;
        final JButton playAgainButton = createStyledButton("Gioca Ancora");
        playAgainButton.addActionListener(e -> startNewGame());
        panel.add(playAgainButton, gbc);

        gbc.gridy++;
        final JButton exitButton = createStyledButton("Esci");
        exitButton.addActionListener(e -> exitGame());
        panel.add(exitButton, gbc);

        return panel;
    }

    private JButton createStyledButton(final String text) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(new Font(FONT_NAME, Font.BOLD, BUTTON_FONT_SIZE));
        button.setBackground(new Color(BUTTON_RED, BUTTON_GREEN, BUTTON_BLUE));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
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

    private void addToLeaderboardWithNamePrompt() {
        final int totalScore = player.getTotalScore();
        final String name = JOptionPane.showInputDialog(
            parentWindow,
            "Inserisci il tuo nome per la classifica:",
            "Nome Giocatore",
            JOptionPane.QUESTION_MESSAGE
        );

        if (name != null && !name.isBlank()) {
            final boolean added = leaderboard.addScore(name.trim(), totalScore, 0);
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

    private String truncateName(final String name, final int maxLength) {
        return (name.length() <= maxLength)
            ? name
            : name.substring(0, maxLength - 3) + "...";
    }
}
