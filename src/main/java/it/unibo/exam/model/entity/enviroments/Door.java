package it.unibo.exam.model.entity.enviroments;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Simple door class.
 * Represents a passage between two rooms, which may also serve as the special endgame exit.
 */
public final class Door extends Entity {

    private boolean isOpen;
    private final int fromId;
    private final int toId;
    private boolean endgameDoor;

    /**
     * Constructor for Door.
     *
     * @param enviromentSize the dimensions of the environment
     * @param position       the initial position of the door
     * @param fromId         the ID of the room this door departs from
     * @param toId           the ID of the room this door leads to
     */
    public Door(final Point2D enviromentSize, final Point2D position, final int fromId, final int toId) {
        super(position, enviromentSize);
        this.fromId = fromId;
        this.toId = toId;
        this.isOpen = false;
        this.endgameDoor = false;
    }

    /**
     * Returns whether the door is currently open.
     *
     * @return {@code true} if the door is open; {@code false} otherwise
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Opens the door.
     */
    public void open() {
        this.isOpen = true;
    }

    /**
     * Closes the door.
     */
    public void close() {
        this.isOpen = false;
    }

    /**
     * Returns the ID of the room this door departs from.
     *
     * @return the fromId
     */
    public int getFromId() {
        return this.fromId;
    }

    /**
     * Returns the ID of the room this door leads to.
     *
     * @return the toId
     */
    public int getToId() {
        return this.toId;
    }

    /**
     * Indicates whether this door is the special endgame exit.
     *
     * @return {@code true} if this door triggers the endgame; {@code false} otherwise
     */
    public boolean isEndgameDoor() {
        return this.endgameDoor;
    }

    /**
     * Marks or unmarks this door as the special endgame exit.
     * Use only during game initialization.
     *
     * @param endgameDoor {@code true} to flag this door as the exit; {@code false} to clear the flag
     */
    public void setEndgameDoor(final boolean endgameDoor) {
        this.endgameDoor = endgameDoor;
    }
}
