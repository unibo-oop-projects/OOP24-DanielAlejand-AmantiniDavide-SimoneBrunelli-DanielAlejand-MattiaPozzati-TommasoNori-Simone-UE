package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;

/**
 * A non-interactable NPC that just moves around.
 * 
 * This class represents a non-playable character (NPC) that roams around in 
 * the environment without any interaction. It moves according to the strategy 
 * provided at instantiation and updates its position every tick based on that 
 * strategy. This class is not designed for extension, as it does not provide 
 * functionality that can be safely extended. If you want to modify its behavior, 
 * consider subclassing {@link MovementEntity} instead.
 */
public final class RoamingNpc extends MovementEntity {

    private final MovementStrategy movementStrategy;

    /**
     * Constructs a RoamingNpc entity with a specified start position, environment size, 
     * and movement strategy.
     * 
     * @param start             the starting position of the NPC in the environment.
     * @param environmentSize   the size of the environment (used for speed and movement boundaries).
     * @param strategy          the movement strategy that defines how the NPC moves each tick 
     *                          (e.g., RandomWalkStrategy).
     */
    public RoamingNpc(final Point2D start,
                      final Point2D environmentSize,
                      final MovementStrategy strategy) {
        super(start, environmentSize);  // Calls the constructor of MovementEntity with the starting position and environment size
        this.movementStrategy = strategy; // Sets the movement strategy
    }

    /**
     * Updates the position of the NPC based on the movement strategy.
     * 
     * This method queries the movement strategy to determine the next movement 
     * vector, then applies that movement to the NPC's position.
     * 
     * @param deltaTime  the time in seconds since the last update, used to scale the movement.
     * @param room       the room in which the NPC is located (unused in this method but may be relevant in other strategies).
     */
    public void update(final double deltaTime, final Room room) {
        // Ask the strategy how much to move
        final Point2D delta = movementStrategy.getNextMove(this, room, deltaTime);
        // Apply it—and update the hitbox
        this.move(delta.getX(), delta.getY()); // Moves the NPC and updates its hitbox
    }
}
