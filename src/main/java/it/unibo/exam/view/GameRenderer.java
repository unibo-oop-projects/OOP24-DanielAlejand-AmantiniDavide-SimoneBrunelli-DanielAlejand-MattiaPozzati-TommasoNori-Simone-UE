package it.unibo.exam.view;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.game.GameState;
import it.unibo.exam.view.hud.ScoreHud;
import it.unibo.exam.view.renderer.PlayerRenderer;
import it.unibo.exam.view.renderer.DoorRenderer;
import it.unibo.exam.view.renderer.NpcRenderer;
import it.unibo.exam.utility.generator.RoomGenerator;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Handles rendering of the game elements such as rooms and players.
 * Updated with entity-specific renderers.
 */
public class GameRenderer {

    private static final Color MAIN_ROOM_COLOR    = new Color(70, 70, 90);
    private static final Color PUZZLE_ROOM_COLOR  = new Color(60, 80, 60);
    private static final Color DEFAULT_ROOM_COLOR = new Color(50, 50, 50);

    private final GameState      gs;
    private final ScoreHud       scoreHud;

    // Entity renderers
    private final PlayerRenderer playerRenderer;
    private final DoorRenderer   doorRenderer;
    private final NpcRenderer    npcRenderer;

    /**
     * Constructor for GameRenderer.
     *
     * @param gs the game state to render
     */
    public GameRenderer(final GameState gs) {
        this.gs             = gs;
        this.scoreHud       = new ScoreHud(gs);

        // Initialize renderers
        this.playerRenderer = new PlayerRenderer();
        this.doorRenderer   = new DoorRenderer();
        this.npcRenderer    = new NpcRenderer();
    }

    /**
     * Renders the game by rendering the current room and player.
     *
     * @param g the graphics context to render on
     */
    public void renderGame(final Graphics2D g) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics context cannot be null");
        }
        renderRoom(g, gs.getCurrentRoom());
        renderPlayer(g, gs.getPlayer());
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
     * Renders the current room background, doors, NPCs, and roaming NPCs.
     *
     * @param g the graphics context
     * @param currentRoom the room to render
     */
    private void renderRoom(final Graphics2D g, final Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        clearBackground(g);
        drawRoomBackground(g, currentRoom);

        // Draw doors
        currentRoom.getDoors().forEach(door -> doorRenderer.render(g, door));

        // Draw the puzzle NPC if present
        if (currentRoom.getRoomType() == RoomGenerator.PUZZLE_ROOM
            && currentRoom.getNpc() != null) {
            npcRenderer.render(g, currentRoom.getNpc());
        }

        // ADDED: Draw all roaming NPCs (non-interactable)
        currentRoom.getRoamingNpcs()
                   .forEach(rn -> npcRenderer.render(g, rn));
    }

    /**
     * Renders the player.
     *
     * @param g the graphics context
     * @param player the player to render
     */
    private void renderPlayer(final Graphics2D g, final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        playerRenderer.render(g, player);
    }

    /**
     * Clears the background with a default color.
     * 
     * @param g the graphics context
     */
    private void clearBackground(final Graphics2D g) {
        final java.awt.Rectangle bounds = g.getClipBounds();
        if (bounds != null) {
            g.setColor(DEFAULT_ROOM_COLOR);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    /**
     * Draws the room-specific background and visual elements.
     * 
     * @param g the graphics context
     * @param room the room to draw background for
     */
    private void drawRoomBackground(final Graphics2D g, final Room room) {
        final java.awt.Rectangle bounds = g.getClipBounds();
        if (bounds == null) return;

        Color roomColor = switch (room.getRoomType()) {
            case RoomGenerator.MAIN_ROOM   -> MAIN_ROOM_COLOR;
            case RoomGenerator.PUZZLE_ROOM -> PUZZLE_ROOM_COLOR;
            default                         -> DEFAULT_ROOM_COLOR;
        };

        g.setColor(roomColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw room border and title
        g.setColor(Color.WHITE);
        g.drawRect(5, 5, bounds.width - 10, bounds.height - 10);
        g.drawString(room.getId() == 0 ? "Hub" : room.getName(), 10, 25);
    }

    /**
     * Accessor for the ScoreHud.
     *
     * @return the ScoreHud used for rendering
     */
    public ScoreHud getScoreHud() {
        return scoreHud;
    }
}
