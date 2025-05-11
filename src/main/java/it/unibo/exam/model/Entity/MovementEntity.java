package it.unibo.exam.model.Entity;

import it.unibo.exam.utility.Point2D;

public class MovementEntity extends Entity{

    public MovementEntity(Point2D position, Point2D dimension) {
        super(position, dimension);
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param d the distance to move
     */
    public void move(Point2D d) {
        this.getPosition().move(d.getX(), d.getY());
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param dx the distance to move in the x direction
     * @param dy the distance to move in the y direction
     */
    public void move(int dx, int dy) {
        this.getPosition().move(dx, dy);
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param dx the distance to move in the x direction
     */
    public void setPosition(Point2D position) {
        this.getPosition().setXY(position.getX(), position.getY());
    }

    /**
     * sets the position of the entity.
     * @param x the x coordinate
     * @param y the y coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPosition(int x, int y) {
        this.getPosition().setXY(x, y);
    }

    /**
     * sets the position X of the entity.
     * @param x the x coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPositionX(int x) {
        this.getPosition().setXY(x, this.getPosition().getY());
    }

    /**
     * sets the position Y of the entity.
     * @param y the x coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPositionY(int y) {
        this.getPosition().setXY(this.getPosition().getX(), y);
    }
    
}
