package it.unibo.exam.model.Entity;

import it.unibo.exam.utility.Geometry.Point2D;

/**
 * A simple Entity who can move in the enviroment
 */
public class MovementEntity extends Entity{

    private static final int DEFAULT_SPEED = 20;
    private static final int DEFAULT_SIZE = 800;

    private int speed;
    
    /**
     * Constructor.
     * Create an entity and then update speed according to enviromentSize
     * @param scaleFactor 
     * @param enviromentSize
     * @see Entity
     */
    public MovementEntity(int scaleFactor, Point2D environmentSize) {
        super(scaleFactor, environmentSize);
        this.speed = updateSpeed();
    }

    /**
     * Update speed according to the enviroment size
     */
    private int updateSpeed() {
        return this.getEnviromentSize().getX() / DEFAULT_SIZE * DEFAULT_SPEED;
    }

    /**
     * @return the speed of the entity
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param d the distance to move
     */
    public void move(final Point2D d) {
        this.getPosition().move(d.getX(), d.getY());
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param dx the distance to move in the x direction
     * @param dy the distance to move in the y direction
     */
    public void move(final int dx, final int dy) {
        this.getPosition().move(dx, dy);
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param dx the distance to move in the x direction
     */
    public void setPosition(final Point2D position) {
        this.getPosition().setXY(position.getX(), position.getY());
    }

    /**
     * sets the position of the entity.
     * @param x the x coordinate
     * @param y the y coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPosition(final int x, final int y) {
        this.getPosition().setXY(x, y);
    }

    /**
     * sets the position X of the entity.
     * @param x the x coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPositionX(final int x) {
        this.getPosition().setXY(x, this.getPosition().getY());
    }

    /**
     * sets the position Y of the entity.
     * @param y the x coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPositionY(final int y) {
        this.getPosition().setXY(this.getPosition().getX(), y);
    }

    /**
     * Sets the new dimension of the entity.
     *
     * @param newDimension the new dimension of the entity
     */
    @Override
     public void resize(final Point2D newSize) {
        super.resize(newSize);
        this.speed = updateSpeed();
    }
    
}
