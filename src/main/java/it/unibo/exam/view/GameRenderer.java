package it.unibo.exam.view;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.game.GameState;

/**
 * Handles rendering of the game elements such as rooms and players.
 */
public class GameRenderer {
    private final GameState gs;

    /**
     * Constructor for GameRenderer.
     *
     * @param gs the game state to render
     */
    public GameRenderer(final GameState gs) {
        this.gs = gs;
    }

    /**
     * Renders the game by rendering the current room and player.
     */
    public void renderGame() {
        renderRoom(gs.getCurrentRoom());
        renderPlayer(gs.getPlayer());
    }

    /** 
     * Renders the current room background, doors, and NPCs.
     *
     * @param currentRoom the room to render
     */
    private void renderRoom(final Room currentRoom) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderRoom'");
    }
    /**
     * Renders the player.
     *
     * @param player the player to render
     */
    private void renderPlayer(final Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderPlayer'");
    }
}
