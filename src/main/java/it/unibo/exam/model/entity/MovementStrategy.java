package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;

public interface MovementStrategy {
    /**
     * Compute how far to move this tick.
     *
     * @param entity    the entity being moved
     * @param room      the room (unused here)
     * @param deltaTime seconds elapsed since last update
     * @return a dx/dy vector (in pixels) to apply
     */
    Point2D getNextMove(MovementEntity entity, Room room, double deltaTime);
}
