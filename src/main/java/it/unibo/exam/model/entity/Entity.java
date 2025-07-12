package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.geometry.Rectangle;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * 
 * Simple Entity class.
 */
public class Entity {

    private static final int SCALE_FACTOR = 20;

    private Point2D position;
    private Point2D dimension;
    private Rectangle hitbox;
    private Point2D enviromentSize;
    private int xFactor = 1;
    private int yFactor = 1;

    /**
     * Constructor for Entity.
     *
     * @param enviromentSize the size of the environment
     * @param position Inizial position of the entity
     * @param xFactor  scaling factor for the entity's width relative to the environment
     * @param yFactor scaling factor for the entity's height relative to the environment
     */
    public Entity(final Point2D position, final Point2D enviromentSize, final int xFactor, final int yFactor) {
        if (enviromentSize == null) {
            throw new IllegalArgumentException("Environment size cannot be null");
        }

        this.enviromentSize = new Point2D(enviromentSize);

        if (position == null) {
            this.position = new Point2D(this.enviromentSize.getX() / 2, this.enviromentSize.getY() / 2);
        } else if (!new Rectangle(new Point2D(0, 0), this.enviromentSize).contains(position)) {
            // Fixed the logic - was inverted before
            throw new IllegalArgumentException("Initial position out of bounds: "
            + "position(" + position.getX() + "," + position.getY() + ") " 
            + "environment(" + enviromentSize.getX() + "," + enviromentSize.getY() + ")");
        } else {
            this.position = new Point2D(position);
        }
        this.xFactor = xFactor;
        this.yFactor = yFactor;

        this.dimension = new Point2D(
            xFactor * enviromentSize.getX() / SCALE_FACTOR, 
            yFactor * enviromentSize.getY() / SCALE_FACTOR
        );
        this.hitbox = new Rectangle(this.position, dimension);
    }

    /**
     * 
     * @param position
     * @param enviromentSize
     */
    public Entity(final Point2D position, final Point2D enviromentSize) {
        this(position, enviromentSize, 1, 1);
    }


    /**
     * Alternative constructor.
     * Place the entity in the center of the enviroment.
     * @param enviromentSize the size of the environment
     */
    public Entity(final Point2D enviromentSize) {
        this(
            new Point2D(enviromentSize.getX() / 2, enviromentSize.getY() / 2), 
            enviromentSize
        );
    }

    /**
     * @return copy of entity's position
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", 
                       justification = "Position must be mutable for game performance,"
                                        + "returning direct reference is intentional")
    public Point2D getPosition() {
        return position;
    }

    /**
     * @return copy of entity's position
     */
    public final Point2D getEnviromentSize() {
        return new Point2D(enviromentSize);
    }

    /**
     * @return copy of entity's position
     */
    public Point2D getDimension() {
        return new Point2D(dimension);
    }

    /**
     * Sets the new dimension of the entity.
     * 
     * @param newEnviromentSize the new enviroment size
     */
    public void resize(final Point2D newEnviromentSize) {
        this.enviromentSize = new Point2D(newEnviromentSize);
        this.dimension = new Point2D(
            xFactor * enviromentSize.getX() / SCALE_FACTOR, 
            yFactor * enviromentSize.getY() / SCALE_FACTOR
        );
        updateHitbox();
    }

    /**
     * Updates the hitbox position based on current position.
     */
    private void updateHitbox() {
        this.hitbox = new Rectangle(this.position, dimension);
    }

    /**
     * 
     * @return SCALE_FACTOR
     */
    public int getScaleFactor() {
        return SCALE_FACTOR;
    }

    /**
     * Updates the hitbox when position changes.
     * Call this method after moving the entity.
     */
    public void updateHitboxPosition() {
        updateHitbox();
    }

    /**
     * @return the hitbox of the entity
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
}
