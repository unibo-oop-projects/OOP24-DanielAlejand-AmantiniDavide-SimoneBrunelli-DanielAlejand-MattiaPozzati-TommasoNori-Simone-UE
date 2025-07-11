package it.unibo.exam.controller;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.controller.position.PlayerPositionManager;
import it.unibo.exam.controller.minigame.MinigameManager;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.RoamingNpc;                    // ADDED
import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.game.GameState;
import it.unibo.exam.utility.generator.NpcGenerator;              // ADDED
import it.unibo.exam.utility.generator.RoomGenerator;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.view.GameRenderer;
import it.unibo.exam.view.hud.ScoreHud;
import it.unibo.exam.view.panel.EndGameMenu;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Main controller of the game.
 * Updated version with integrated minigame system.
 */
@SuppressFBWarnings(value = "IS2_INCONSISTENT_SYNC", 
                   justification = "Game loop thread safety is managed externally")
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private static final int FPS = 60;
    private static final double SECOND = 1_000_000_000.0;
    private static final int TOTALPUZZLEROOMS = 5; // Rooms 1–5

    private final KeyHandler      keyHandler;
    private final GameState       gameState;
    private final GameRenderer    gameRenderer;
    private MinigameManager       minigameManager;
    private boolean               running;
    private Point2D               environmentSize;
    private boolean               minigameActive;
    private int                   currentMinigameRoomId = -1;
    @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, 
                   justification = "JFrame reference is intentionally stored for UI operations and cannot be defensively copied")
    private JFrame                parentFrame;

    /**
     * Constructor with parent frame for minigame integration.
     *
     * @param environmentSize size of the Game panel
     * @param parentFrame    parent frame for minigame windows
     */
    public MainController(final Point2D environmentSize, final JFrame parentFrame) {
        // Core setup
        this.keyHandler      = new KeyHandler();
        this.gameState       = new GameState(environmentSize);
        this.environmentSize = new Point2D(environmentSize);
        this.parentFrame     = parentFrame;

        // ── ADDED: spawn both interactive and roaming NPCs via generator ──
        NpcGenerator npcGen = new NpcGenerator(environmentSize);
        for (Room r : gameState.getAllRooms()) {
            // 1) interactive puzzle NPC
            Npc interactive = npcGen.generate(r.getId());
            r.attachNpc(interactive);
            // 2) roaming background NPC
            RoamingNpc wanderer = npcGen.generateRoamingNpc(r);
            r.addRoamingNpc(wanderer);
        }
        // ─────────────────────────────────────────────────────────────────────

        this.gameRenderer    = new GameRenderer(gameState);

        // —— MinigameManager setup ——
        if (parentFrame != null) {
            this.minigameManager = new MinigameManager(this, parentFrame);
        } else {
            this.minigameManager = null;
        }

        // —— Observer wiring ——
        final Player player = gameState.getPlayer();
        final ScoreHud hud   = gameRenderer.getScoreHud();
        player.addScoreListener(hud);
    }

    /**
     * Constructor with default null parent frame (for backward compatibility).
     *
     * @param environmentSize size of the Game panel
     */
    public MainController(final Point2D environmentSize) {
        this(environmentSize, null);
    }

    /**
     * Sets (or resets) the parent frame used by the MinigameManager.
     * This is only needed if you couldn’t supply the frame at construction time.
     *
     * @param parentFrame the JFrame to use as parent for all minigame windows
     */
    public void setParentFrame(final JFrame parentFrame) {
        this.parentFrame = parentFrame;
        if (parentFrame != null && this.minigameManager == null) {
            this.minigameManager = new MinigameManager(this, parentFrame);
            LOGGER.info("MinigameManager initialized with parent frame");
        }
    }

    /**
     * Resize all the entity.
     * @param newSize new size of the Game Panel
     */
    public void resize(final Point2D newSize) {
        this.environmentSize = new Point2D(newSize);
        this.gameState.resize(newSize);
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        running = true;
        gameLoop();
    }

    /**
     * Stops the game loop and any running minigames.
     */
    public void stop() {
        running = false;
        if (minigameManager != null) {
            minigameManager.stopCurrentMinigame();
        }
    }

    /**
     * Gets the game renderer for external rendering calls.
     * @return the game renderer
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", 
                       justification = "GameRenderer reference is needed for rendering pipeline, defensive copy not appropriate")
    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    /**
     * Gets the key handler for external input management.
     * @return the key handler
     */
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    /**
     * Gets the minigame manager.
     * @return the minigame manager
     */
    public MinigameManager getMinigameManager() {
        return minigameManager;
    }

    /**
     * Main game loop.
     */
    @SuppressFBWarnings(value = "NN_NAKED_NOTIFY", 
                       justification = "Thread sleep is intentional for frame rate control")
    private void gameLoop() {
        long lastTime = System.nanoTime();
        final long nsPerUpdate = (long) (SECOND / FPS);
        long accumulatedTime = 0;

        while (running) {
            final long now = System.nanoTime();
            accumulatedTime += now - lastTime;
            lastTime = now;

            while (accumulatedTime >= nsPerUpdate) {
                update();
                accumulatedTime -= nsPerUpdate;
            }

            try {
                Thread.sleep(1);
            } catch (final InterruptedException e) {
                LOGGER.log(Level.WARNING, "Game loop interrupted", e);
            }
        }
    }

    /**
     * Updates the game state.
     */
    private void update() {
        final double deltaTime = 1.0 / FPS;                            // ADDED: compute timestep
        final Player player = gameState.getPlayer();
        final Room room     = gameState.getCurrentRoom();

        // ── ADDED: move all roaming NPCs each tick ──
        for (RoamingNpc rn : room.getRoamingNpcs()) {
            rn.update(deltaTime, room);
        }
        // ─────────────────────────────────────────────

        movePlayer(player);
        checkInteraction(player, room);
    }

    /**
     * Check win condition.
     */
    private void checkWin() {
        if (gameState.getPlayer().allRoomsCompleted(TOTALPUZZLEROOMS)) {
            running = false;
            SwingUtilities.invokeLater(this::showEndGameMenu);
        }
    }

    /**
     * Shows the EndGameMenu by replacing the current panel content.
     */
    @SuppressFBWarnings(value = "PZLA_PREFER_ZERO_LENGTH_ARRAYS", 
                       justification = "Exception handling pattern is intentional for UI operations")
    private void showEndGameMenu() {
        if (parentFrame == null) {
            LOGGER.severe("Cannot show EndGameMenu: parent frame is null");
            return;
        }
        if (gameState == null || gameState.getPlayer() == null) {
            LOGGER.severe("Cannot show EndGameMenu: game state or player is null");
            return;
        }

        try {
            if (minigameManager != null) {
                minigameManager.stopCurrentMinigame();
            }
            parentFrame.getContentPane().removeAll();
            final EndGameMenu endGameMenu = new EndGameMenu(parentFrame, gameState.getPlayer());
            parentFrame.add(endGameMenu);
            parentFrame.revalidate();
            parentFrame.repaint();
            LOGGER.info("EndGameMenu displayed successfully");
        } catch (final IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "UI state error when showing EndGameMenu", e);
        } catch (final SecurityException e) {
            LOGGER.log(Level.SEVERE, "Security error when showing EndGameMenu", e);
        }
    }

    /**
     * Check interaction with doors and NPCs.
     * @param player Player
     * @param room Current Room
     */
    private void checkInteraction(final Player player, final Room room) {
        if (!keyHandler.isInteractJustPressed()) {
            return;
        }
        // 1 Handle door interactions
        room.getDoors().forEach(door -> {
            if (!isNearDoor(player, door)) {
                return;
            }
            if (room.getId() == 0 && door.isEndgameDoor()) {
                checkWin();
                if (running) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Complete all minigames first!",
                        "Locked",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            } else {
                gameState.changeRoom(door.getToId());
                positionPlayerAfterRoomChange(door);
                LOGGER.info("Moved from room "
                    + door.getFromId() + " to room " + door.getToId());
            }
        });

        // 2) Handle NPC interactions (unchanged)
        if (room.getRoomType() == RoomGenerator.PUZZLE_ROOM
            && room.getNpc() != null
            && isNearNpc(player, room.getNpc())) {

            if (gameState.getPlayer().getRoomScore(room.getId()) != null) {
                LOGGER.info("Room " + room.getId() + " already completed!");
                return;
            }

            room.getNpc().interact();
            final int roomId = gameState.getCurrentRoom().getId();
            JOptionPane.showMessageDialog(
                null,
                room.getNpc().getDialogue(),
                room.getNpc().getName(),
                JOptionPane.INFORMATION_MESSAGE
            );

            if (minigameManager != null) {
                minigameManager.startMinigame(roomId);
            } else {
                LOGGER.warning("MinigameManager not initialized - cannot start minigame");
            }
        }
    }

    /**
     * Checks if player is near a door (with expanded detection area).
     * @param player the player
     * @param door the door
     * @return true if player is close enough to interact
     */
    private boolean isNearDoor(final Player player, final Door door) {
        final int proximityBuffer = 30;
        final Point2D playerPos = player.getPosition();
        final Point2D playerSize = player.getDimension();
        final Point2D doorPos = door.getPosition();
        final Point2D doorSize = door.getDimension();

        return playerPos.getX() + playerSize.getX() >= doorPos.getX() - proximityBuffer
            && playerPos.getX() <= doorPos.getX() + doorSize.getX() + proximityBuffer 
            && playerPos.getY() + playerSize.getY() >= doorPos.getY() - proximityBuffer 
            && playerPos.getY() <= doorPos.getY() + doorSize.getY() + proximityBuffer;
    }

    /**
     * Checks if player is near an NPC.
     * @param player the player
     * @param npc the NPC
     * @return true if player is close enough to interact
     */
    private boolean isNearNpc(final Player player, final Npc npc) {
        final int proximityBuffer = 30;
        final Point2D playerPos = player.getPosition();
        final Point2D playerSize = player.getDimension();
        final Point2D npcPos = npc.getPosition();
        final Point2D npcSize = npc.getDimension();

        return playerPos.getX() + playerSize.getX() >= npcPos.getX() - proximityBuffer
            && playerPos.getX() <= npcPos.getX() + npcSize.getX() + proximityBuffer 
            && playerPos.getY() + playerSize.getY() >= npcPos.getY() - proximityBuffer 
            && playerPos.getY() <= npcPos.getY() + npcSize.getY() + proximityBuffer;
    }

    /**
     * Positions the player after changing rooms based on the door used.
     * 
     * @param usedDoor the door that was used to transition
     */
    private void positionPlayerAfterRoomChange(final Door usedDoor) {
        final Player player = gameState.getPlayer();
        final Room newRoom = gameState.getCurrentRoom();

        Door correspondingDoor = null;
        for (final Door door : newRoom.getDoors()) {
            if (door.getToId() == usedDoor.getFromId()) {
                correspondingDoor = door;
                break;
            }
        }

        if (correspondingDoor != null) {
            PlayerPositionManager.positionPlayerAfterTransition(player, correspondingDoor, environmentSize);
        } else {
            player.setPosition(PlayerPositionManager.getDefaultSpawnPosition(environmentSize));
        }
    }

    /**
     * Moves the player based on input.
     * @param player the player to move
     */
    private void movePlayer(final Player player) {
        final int speed = player.getSpeed();
        final Point2D currentPos = player.getPosition();
        final Point2D playerSize = player.getDimension();

        if (keyHandler.isUpPressed()) {
            final int newY = currentPos.getY() - speed;
            if (newY >= 10) {
                player.move(0, -speed);
            }
        }
        if (keyHandler.isDownPressed()) {
            final int newY = currentPos.getY() + speed;
            if (newY + playerSize.getY() <= environmentSize.getY() - 10) {
                player.move(0, speed);
            }
        }
        if (keyHandler.isLeftPressed()) {
            final int newX = currentPos.getX() - speed;
            if (newX >= 10) {
                player.move(-speed, 0);
            }
        }
        if (keyHandler.isRightPressed()) {
            final int newX = currentPos.getX() + speed;
            if (newX + playerSize.getX() <= environmentSize.getX() - 10) {
                player.move(speed, 0);
                ensurePlayerInBounds(player);
            }
        }
    }

    /**
     * Ensures the player stays within bounds (safety check).
     * @param player the player to check
     */
    private void ensurePlayerInBounds(final Player player) {
        final Point2D currentPos = player.getPosition();
        final Point2D playerSize = player.getDimension();

        final int margin = 10;
        final int minX = margin;
        final int minY = margin;
        final int maxX = environmentSize.getX() - playerSize.getX() - margin;
        final int maxY = environmentSize.getY() - playerSize.getY() - margin;

        final int newX = Math.max(minX, Math.min(currentPos.getX(), maxX));
        final int newY = Math.max(minY, Math.min(currentPos.getY(), maxY));

        if (newX != currentPos.getX() || newY != currentPos.getY()) {
            player.setPosition(newX, newY);
        }
    }

    /**
     * Starts timing for a minigame in the specified room.
     * @param roomId the room ID for the minigame
     */
    public void startMinigame(final int roomId) {
        minigameActive = true;
        currentMinigameRoomId = roomId;
        LOGGER.info("Started minigame timing for room " + roomId);
    }

    /**
     * Ends the current minigame with a known duration and awards points.
     *
     * @param success   true if the minigame was completed successfully
     * @param timeTaken the time taken (in seconds) to complete the minigame
     * @param score     the score achieved in the minigame
     */
    public void endMinigame(final boolean success, final int timeTaken, final int score) {
        if (minigameActive && currentMinigameRoomId >= 0 && success) {
            gameState.getPlayer().addRoomScore(currentMinigameRoomId, timeTaken, score);
            LOGGER.info("Minigame completed successfully! Room "
                        + currentMinigameRoomId
                        + ", Time: " + timeTaken + "s, Points: " + score);
        } else if (minigameActive) {
            LOGGER.info("Minigame failed for room " + currentMinigameRoomId);
        }
        minigameActive        = false;
        currentMinigameRoomId = -1;
    }
}
