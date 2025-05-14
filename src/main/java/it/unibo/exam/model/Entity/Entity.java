package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.geometry.Rectangle;

/**
 * 
 * Simple Entity class.
 */
public class Entity {

    private Point2D position;
    private Point2D dimension;
    private Rectangle hitbox;
    private final Point2D enviromentSize;

    /**
     * Constructor for Entity.
     *
     * @param scaleFactor the scale factor for the entity
     * @param enviromentSize the size of the environment
     * @param position Inizial position of the entity
     */
    public Entity(final Point2D position, final int scaleFactor, final Point2D enviromentSize) {

        if (position == null) {
            this.position = new Point2D(enviromentSize.getX() / 2, enviromentSize.getY() / 2);
        } else if (new Rectangle(new Point2D(0, 0), enviromentSize).contains(position)) {
            throw new IllegalArgumentException("Initial position out of bounds");
        } else {
            this.position = position;
        }

        if (enviromentSize == null) {
            throw new IllegalArgumentException("Environment size cannot be null");
        }
        if (scaleFactor <= 0 || scaleFactor > enviromentSize.getX() || scaleFactor > enviromentSize.getY()) {
            throw new IllegalArgumentException("Scale factor out of bound");
        }

        this.dimension = new Point2D(enviromentSize.getX() / scaleFactor, enviromentSize.getY() / scaleFactor);
        this.enviromentSize = enviromentSize;
        this.hitbox = new Rectangle(position, dimension);
    }


    /**
     * Alternative constractor.
     * Place the entity in the center of the enviroment.
     * @param scaleFactor the scale factor for the entity
     * @param enviromentSize the size of the environment
     */
    public Entity(final int scaleFactor, final Point2D enviromentSize) {

        this(
            new Point2D(enviromentSize.getX() / 2, enviromentSize.getY() / 2), 
            scaleFactor, 
            enviromentSize
        );
    }

    /**
     * @return the position of the entity
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * @return enviromentSize
     */
    protected final Point2D getEnviromentSize() {
        return enviromentSize;
    }

    /**
     * @return the dimension of the entity
     */
    public Point2D getDimension() {
        return dimension;
    }

    /**
     * Sets the new dimension of the entity.
     * 
     * @param newDimension the new dimension of the entity
     */
    public void resize(final Point2D newDimension) {
        this.dimension = newDimension;
        this.hitbox = new Rectangle(this.position, dimension);
    }

    /**
     * @return the hitbox of the entity
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
}
