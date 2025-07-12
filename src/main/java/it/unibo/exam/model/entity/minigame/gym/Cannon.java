package it.unibo.exam.model.entity.minigame.gym;

import java.awt.Color;

import it.unibo.exam.model.entity.MovementEntity;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Represents the player-controlled cannon in the Gym minigame.
 */
public class Cannon extends MovementEntity {

    private static final int XFACTOR = 1;
    private static final int YFACTOR = 2;

    private final Point2D size;
    private final Color color;
    private double angle;


    /**
     * Constructs a new cannon.
     * @param position initial position
     * @param color cannon color
     * @param env environment size
     */
    public Cannon(final Point2D position, final Color color, final Point2D env) {
        super(position, env, XFACTOR, YFACTOR);
        this.color = color;
        this.angle = Math.PI / 2;
        this.size = new Point2D(XFACTOR * env.getX() / super.getScaleFactor(), YFACTOR * env.getY() / super.getScaleFactor());
    }

    /**
     * Copy constructor. Creates a new Cannon with the same position, color, and environment size as the given Cannon.
     *
     * @param c the Cannon to copy
     */
    public Cannon(final Cannon c) {
        this(c.getPosition(), c.color, c.getEnviromentSize());
    }

    // Getters e setters
    /**
     * @return the cannon width
     */
    public int getWidth() { 
        return size.getX(); 
    }
    /**
     * @return the cannon height
     */
    public int getHeight() { 
        return size.getY(); 
    }
    /**
     * @return the cannon color
     */
    public Color getColor() { 
        return color; 
    }
    /**
     * @return the cannon rotation angle (radians)
     */
    public double getAngle() { 
        return angle; 
    } 
    /**
     * Sets the cannon rotation angle.
     * @param angle new angle (radians)
     */
    public void setAngle(final double angle) { 
        this.angle = angle; 
    }

    /**
     * Calculates the tip position of the cannon.
     * @return 2D point of the tip
     */
    public Point2D getCannonTip() {
        final int x = super.getPosition().getX() + size.getX() / 2 + (int) (size.getX() * Math.cos(angle));
        final int y = super.getPosition().getY() - (int) (size.getX() * Math.sin(angle));
        return new Point2D(x, y);
    }
}
