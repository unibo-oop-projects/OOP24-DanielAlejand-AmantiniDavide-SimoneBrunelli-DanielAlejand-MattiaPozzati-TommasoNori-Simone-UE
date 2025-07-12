package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;

/**
 * Interface representing the movement strategy for an entity.
 * 
 * This interface defines how an entity's movement is calculated. Implementing 
 * classes will provide specific strategies for how an entity moves within a 
 * room, taking into account the time elapsed since the last update.
 * 
 * @see MovementEntity
 * @see Room
 * @see Point2D
 */
public interface MovementStrategy {

    /**
     * Computes the movement for the entity during the current tick.
     * 
     * This method calculates how far the entity should move based on the 
     * elapsed time since the last update. The resulting vector is returned 
     * as a {@link Point2D} object representing the change in position 
     * (delta x and delta y in pixels).
     * 
     * @param entity    the entity being moved. This is the object whose position 
     *                  is being updated.
     * @param room      the room in which the entity is moving. This parameter is 
     *                  not used in this method but could be relevant in 
     *                  subclasses with more complex movement logic.
     * @param deltaTime the time in seconds that has passed since the last update.
     *                  This value can be used to scale the movement based on time.
     * @return a {@link Point2D} representing the vector (dx, dy) to apply to 
     *         the entity's position.
     */
    Point2D getNextMove(MovementEntity entity, Room room, double deltaTime);
}
