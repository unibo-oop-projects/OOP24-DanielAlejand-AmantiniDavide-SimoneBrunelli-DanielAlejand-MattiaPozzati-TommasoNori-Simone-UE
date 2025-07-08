package it.unibo.exam.model.entity.minigame.gym;

import java.awt.Color;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.MovementEntity;
/**
 * Represents a disk (target) in the Gym minigame.
 */
public class Disk extends MovementEntity {
    private static final int DEFAULT_RADIUS = 20;
    private final Color color;
    private final int radius;
    private boolean popped;

    /**
     * Constructs a new disk.
     * @param position initial position
     * @param color disk color
     * @param radius disk radius
     * @param env environment size
     */
    public Disk(final Point2D position, final Color color, final int radius, final Point2D env) {
        super(position, env);
        this.color = color;
        this.radius = radius;
        this.popped = false;
    }
    // Getters e setters
    /**
     * @return the disk color
     */
    public Color getColor() { 
        return color; 
    }
    /**
     * @return the disk radius
     */
    public int getRadius() { 
        return radius; 
    }
    /**
     * @return true if the disk has been popped
     */
    public boolean isPopped() { 
        return popped; 
    }
    /**
     * Marks the disk as popped (eliminated).
     */
    public void pop() { 
        this.popped = true; 
    }

    /**
     * Checks if a point is contained within the disk.
     * @param p point to check
     * @return true if the point is inside the disk
     */
    public boolean contains(final Point2D p) {
        return this.getPosition().distance(p) <= radius;
    }

    /**
     * 
     * @return disk's default radius
     */
    public static int defaultRadius() {
        return DEFAULT_RADIUS;
    }
}
