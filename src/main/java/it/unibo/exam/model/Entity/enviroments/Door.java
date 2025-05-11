package it.unibo.exam.model.Entity.enviroments;

import it.unibo.exam.model.Entity.Entity;
import it.unibo.exam.utility.Geometry.Point2D;

public class Door extends Entity {

    private boolean isOpen;
    private final int fromId;
    private final int toId;

    /**
     * Constructor for Door.
     *
     * @param position the inizial position of the door
     * @param dimension the dimension of the door
     * @param isOpen if the door is open or closed
     */
    public Door(final int scaleFactor, final Point2D enviromentSize, final Point2D position, final int fromId, final int toId) {
        super(position, scaleFactor, enviromentSize);
        this.fromId = fromId;
        this.toId = toId;
        this.isOpen = false;
    }

    /**
     * @return if the door is open or closed
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Sets the door to open.
     */
    public void open() {
        this.isOpen = true;
    }
    
    /**
     * Sets the door to closed.
     */
    public void close() {
        this.isOpen = false;
    }

    /**
     * @return the id of the door
     */
    public int getFromId() {
        return this.fromId;
    }

    /**
     * @return the id of the door
     */
    public int getToId() {
        return this.toId;
    }
    
}
