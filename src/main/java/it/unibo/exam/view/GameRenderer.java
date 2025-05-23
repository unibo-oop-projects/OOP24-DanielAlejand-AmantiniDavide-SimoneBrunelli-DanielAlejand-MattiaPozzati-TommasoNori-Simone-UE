package it.unibo.exam.view;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.game.GameState;
import it.unibo.exam.view.hud.ScoreHud;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

/**
 * Handles rendering of the game elements such as rooms and players.
 */
public class GameRenderer {
    private final GameState gs;
    private final Canvas canvas;
    private final ScoreHud scoreHud;

    /**
     * Constructor for GameRenderer.
     *
     * @param gs the game state to render
     */
    public GameRenderer(final GameState gs) {
        this.gs = gs;
        this.canvas = new Canvas();
        this.canvas.createBufferStrategy(2);
        this.scoreHud = new ScoreHud(gs);
    }

    /**
     * Renders the game by rendering the current room, player, and HUD.
     */
    public void renderGame() {
        final BufferStrategy bs = canvas.getBufferStrategy();
        final Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        try {
            renderRoom(gs.getCurrentRoom());
            renderPlayer(gs.getPlayer());
            renderHud(g);
        } finally {
            g.dispose();
            bs.show();
        }
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
     * Exposes the canvas so it can be added to a window.
     *
     * @return the drawing canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }
}
