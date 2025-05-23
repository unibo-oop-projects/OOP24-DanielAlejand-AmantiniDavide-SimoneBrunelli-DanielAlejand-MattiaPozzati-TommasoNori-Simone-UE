package it.unibo.exam.view;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.game.GameState;
import it.unibo.exam.view.hud.ScoreHud;

import java.awt.Graphics2D;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles rendering of the game elements such as rooms and players.
 */
public class GameRenderer {
    private final GameState gs;
    private final ScoreHud scoreHud;

    /**
     * Constructor for GameRenderer.
     *
     * @param gs the game state to render
     */
    public GameRenderer(final GameState gs) {
        this.gs = gs;
        this.scoreHud = new ScoreHud(gs);
    }

    /**
     * Renders the game by rendering the current room and player.
     */
    public void renderGame() {
        renderRoom(gs.getCurrentRoom());
        renderPlayer(gs.getPlayer());
    }

    /**
     * Renders non-interactive overlays such as the score HUD.
     *
     * @param g the graphics context to draw on
     */
    public void renderHud(final Graphics2D g) {
        scoreHud.draw(g);
    }

    /** 
     * Renders the current room background, doors, and NPCs.
     *
     * @param currentRoom the room to render
     */
    private void renderRoom(final Room currentRoom) {
        // Use the parameter to avoid SpotBugs warning about unused parameter
        if (currentRoom == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        // TODO: Implement rendering logic
        throw new UnsupportedOperationException("Unimplemented method 'renderRoom'");
    }

    /**
     * Renders the player.
     *
     * @param player the player to render
     */
    private void renderPlayer(final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        // TODO: Implement rendering logic
        throw new UnsupportedOperationException("Unimplemented method 'renderPlayer'");
    }

    /**
     * Temporary accessor for integration with future GamePanel.
     *
     * @return the ScoreHud used for rendering the HUD
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Temporary accessor; remove when GamePanel calls renderHud directly"
    )
    public ScoreHud getScoreHud() {
        return scoreHud;
    }
}
