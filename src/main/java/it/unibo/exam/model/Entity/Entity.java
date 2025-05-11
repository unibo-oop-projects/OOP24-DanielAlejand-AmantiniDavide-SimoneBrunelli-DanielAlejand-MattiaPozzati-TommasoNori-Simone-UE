package it.unibo.exam.model.Entity;

import it.unibo.exam.utility.Point2D;
import it.unibo.exam.utility.Rectangle;

public class Entity {

    private Point2D position;
    private Point2D dimension;

    /**
     * Constructor for Entity.
     *
     * @param position the inizial position of the entity
     * @param dimension the dimension of the entity
     */
    public Entity(final Point2D position, final Point2D dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    /**
     * @return the position of the entity
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * @return the dimension of the entity
     */
    public Point2D getDimension() {
        return dimension;
    }

    /**
     * @return the hitbox of the entity
     */
    public Rectangle getHitbox() {
        return new Rectangle(
            this.position,
            this.dimension
        );
    }
}
