package it.unibo.exam.view.hud;

import it.unibo.exam.model.game.GameState;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.data.RoomScoreData;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;

/**
 * Renders a non-interactive HUD showing per-room progress and scores aligned to the top‑right corner, with margin.
 */
public class ScoreHud {

    private static final int START_Y = 30;
    private static final int LINE_HEIGHT = 24;
    private static final int PADDING = 10;
    private static final int PANEL_WIDTH = 240;
    private static final int ARC_RADIUS = 15;
    private static final int RIGHT_MARGIN = 24; // <-- Add some right margin
    private static final Color BG_COLOR = new Color(0, 0, 0, 150);
    private static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 16);
    private static final Font TEXT_FONT = new Font("Dialog", Font.PLAIN, 14);

    private final GameState gameState;

    public ScoreHud(final GameState gameState) {
        this.gameState = gameState;
    }

    public void draw(final Graphics2D g) {
        final Player player = gameState.getPlayer();
        final Map<Integer, RoomScoreData> scores = player.getRoomScores();

        final int panelHeight = (scores.size() + 2) * LINE_HEIGHT + PADDING * 2;

        int screenW = g.getClipBounds().width;
        int startX  = screenW - PANEL_WIDTH - PADDING - RIGHT_MARGIN; // Apply margin

        g.setColor(BG_COLOR);
        g.fillRoundRect(startX - PADDING, START_Y - LINE_HEIGHT, PANEL_WIDTH + PADDING * 2, panelHeight, ARC_RADIUS, ARC_RADIUS);

        g.setFont(TITLE_FONT);
        g.setColor(Color.WHITE);
        g.drawString("Progress", startX, START_Y);

        g.setFont(TEXT_FONT);
        int y = START_Y + LINE_HEIGHT;
        for (RoomScoreData data : scores.values()) {
            String check = data.isCompleted() ? "[✓]" : "[ ]";
            String time  = data.isCompleted() ? data.getTimeTaken()  + "s" : "--";
            String pts   = data.isCompleted() ? data.getPointsGained() + " pts" : "--";
            String line  = String.format("%s %s | %s | %s", check, data.getRoomName(), time, pts);
            g.drawString(line, startX, y);
            y += LINE_HEIGHT;
        }

        g.setFont(TITLE_FONT);
        String total = "Total: " + player.getTotalScore() + " pts";
        g.drawString(total, startX, y + LINE_HEIGHT / 2);
    }
}
