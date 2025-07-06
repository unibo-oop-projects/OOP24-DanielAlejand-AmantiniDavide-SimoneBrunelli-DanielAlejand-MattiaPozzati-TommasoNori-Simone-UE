package it.unibo.exam.view.hud;

import it.unibo.exam.model.game.GameState;
import it.unibo.exam.utility.generator.RoomGenerator;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.data.RoomScoreData;
import it.unibo.exam.model.score.ScoreListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/**
 * Displays the player's progress through all game rooms in a heads-up display.
 * Implements ScoreListener so it can be registered on Player and redraw if needed.
 */
public class ScoreHud implements ScoreListener {

    private static final int START_Y       = 30;
    private static final int LINE_HEIGHT   = 24;
    private static final int PADDING       = 10;
    private static final int PANEL_WIDTH   = 240;
    private static final int ARC_RADIUS    = 15;
    private static final int RIGHT_MARGIN  = 24;
    private static final Color BG_COLOR    = new Color(0, 0, 0, 150);
    private static final Font TITLE_FONT   = new Font("Dialog", Font.BOLD, 16);
    private static final Font TEXT_FONT    = new Font("Dialog", Font.PLAIN, 14);

    private final GameState gameState;

    /**
     * Constructs a ScoreHud tied to the given game state.
     *
     * @param gameState the current game state providing player and room data
     */
    public ScoreHud(final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Called by Player whenever the total score changes.
     * We don't need to cache anything here, since draw() always pulls fresh data,
     * but this method fulfills the ScoreListener contract.
     *
     * @param newTotal the updated total score
     */
    @Override
    public void onScoreChanged(final int newTotal) {
        // no-op: draw() uses up-to-date data from gameState.getPlayer()
    }

    /**
     * Draws the progress HUD on the provided graphics context.
     * All rooms are listed; unplayed rooms show placeholders,
     * completed rooms display actual time and points.
     *
     * @param g the Graphics2D context used for drawing the HUD
     */
    public void draw(final Graphics2D g) {
        final Player           player           = gameState.getPlayer();
        final List<Room>       rooms            = gameState.getAllRooms();
        final List<Room> puzzleRooms = rooms.stream()
            .filter(room -> room.getRoomType() == RoomGenerator.PUZZLE_ROOM) // Avoid non puzzle room
            .toList();
        final int              totalWidth       = g.getClipBounds().width;
        final int              x                = totalWidth - PANEL_WIDTH - PADDING - RIGHT_MARGIN;
        final int              backgroundHeight = (puzzleRooms.size() + 2) * LINE_HEIGHT + PADDING * 2;


        g.setColor(BG_COLOR);
        g.fillRoundRect(
            x - PADDING,
            START_Y - LINE_HEIGHT,
            PANEL_WIDTH + PADDING * 2,
            backgroundHeight,
            ARC_RADIUS,
            ARC_RADIUS
        );

        g.setFont(TITLE_FONT);
        g.setColor(Color.WHITE);
        g.drawString("Progress", x, START_Y);

        g.setFont(TEXT_FONT);
        int y = START_Y + LINE_HEIGHT;
        for (final Room room : puzzleRooms) {
            final RoomScoreData data = player.getRoomScore(room.getId());

            String check = "[ ]";
            String time  = "--";
            String pts   = "--";

            if (data != null && data.isCompleted()) {
                check = "[✓]";
                time  = data.getTimeTaken()  + "s";
                pts   = data.getPointsGained() + " pts";
            }

            final String line = String.format(
                "%s %s | %s | %s",
                check,
                room.getName(),
                time,
                pts
            );
            g.drawString(line, x, y);
            y += LINE_HEIGHT;
        }

        g.setFont(TITLE_FONT);
        final String total = "Total: " + player.getTotalScore() + " pts";
        g.drawString(total, x, y + LINE_HEIGHT / 2);
    }
}
