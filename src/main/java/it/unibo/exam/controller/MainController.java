package it.unibo.exam.controller;

import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.model.Entity.Player;
import it.unibo.exam.model.Entity.enviroments.Room;
import it.unibo.exam.model.Game.GameState;
import it.unibo.exam.utility.Geometry.Point2D;
import it.unibo.exam.view.GameRenderer;

/**
 * Main controller of the game.
 * It handles the game loop and the main logic of the game.
 * It is responsible for updating the game state of the game.
 * 
 */
public class MainController {

    private static final int FPS = 60;
    private static final double SECOND = 1_000_000_000.0;

    private final KeyHandler keyHandler;
    private final GameState gameState;
    private final GameRenderer gameRenderer;
    private double deltaTime;
    private boolean running;

    /**
     * @param enviromentSize size of the Game panel
     */
    public MainController(final Point2D enviromentSize) {
        this.keyHandler = new KeyHandler();
        this.gameState = new GameState(enviromentSize);
        this.gameRenderer = new GameRenderer(gameState);
    }

    /**
     * Resize all the entity.
     * @param newSize new size of the Game Panel
     */
    public void resize(final Point2D newSize) {
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
     * Stops the game loop.
     */
    public void stop() {
        running = false;
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        long lastTime = System.nanoTime();
        deltaTime = 0;
        final double nsPerUpdate =  SECOND / FPS;

        while (running) {
            final long now = System.nanoTime();
            deltaTime += (now - lastTime) / nsPerUpdate;
            lastTime = now;

            while (deltaTime >= 1) {
                update();
                deltaTime--;
            }

            render();

            try {
                Thread.sleep(1); // Sleep to limit the frame rate
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the game state.
     */
    private void update() {
        final Player player = gameState.getPlayer();
        final Room room = gameState.getCurrentRoom();

        movePlayer(player);
        checkInteraction(player, room);
        checkWin();
    }

    /**
     * 
     */
    private void checkWin() {
        return; // TODO: Implement win condition
    }

    /**
     * Check interaction with doors and npc's.
     * @param player Player
     * @param room Current Room
     */
    private void checkInteraction(final Player player, final Room room) {
        // Check for collisions with doors
        room.getDoors().forEach(door -> {
            if (player.getHitbox().intersects(door.getHitbox()) && keyHandler.isInteractPressed()) {
                gameState.changeRoom(door.getToId());
            }
        });

        // Check for collisions with NPCs
        if (room.getNpc() != null) {
            if (player.getHitbox().intersects(room.getNpc().getHitbox()) && keyHandler.isInteractPressed()) {
                // Interact with NPC
                room.getNpc().interact();
            }
        }

    }

    /**
     * Moves the player based on input.
     * This method should be called every frame to update the player's position.
     * It checks for input from the user and updates the player's position accordingly.
     * It also checks for collisions with walls and other entities.
     * @param player 
     */
    private void movePlayer(final Player player) {
        final Point2D position = player.getPosition();
        final int speed = player.getSpeed();

        if (keyHandler.isUpPressed()) {
            position.move(0, -speed);
        }
        if (keyHandler.isDownPressed()) {
            position.move(0, speed);
        }
        if (keyHandler.isLeftPressed()) {
            position.move(-speed, 0);
        }
        if (keyHandler.isRightPressed()) {
            position.move(speed, 0);
        }
    }

    /**
     * Renders the game.
     */
    private void render() {
        this.gameRenderer.renderGame();
    }
}
