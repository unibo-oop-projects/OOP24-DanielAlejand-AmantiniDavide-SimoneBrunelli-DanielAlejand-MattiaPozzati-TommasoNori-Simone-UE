package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;

/**
 * A non-interactable NPC that just moves around.
 */
public class RoamingNpc extends MovementEntity {
    private final MovementStrategy movementStrategy;

    /**
     * @param start         where to spawn
     * @param environmentSize the game’s environment size (for speed & bounds)
     * @param strategy      how to move each tick (e.g. RandomWalkStrategy)
     */
    public RoamingNpc(final Point2D start,
                      final Point2D environmentSize,
                      final MovementStrategy strategy) {
        super(start, environmentSize);          // ← matches MovementEntity(Point2D,Point2D) :contentReference[oaicite:1]{index=1}
        this.movementStrategy = strategy;
    }

    public void update(final double deltaTime, final Room room) {
        // Ask the strategy how much to move
        Point2D delta = movementStrategy.getNextMove(this, room, deltaTime);
        // Apply it—and update hitbox
        this.move(delta.getX(), delta.getY());
    }
}
