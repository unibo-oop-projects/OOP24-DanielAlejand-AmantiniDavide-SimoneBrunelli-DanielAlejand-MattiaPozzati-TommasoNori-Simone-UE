package it.unibo.exam.model.entity.minigame.gym;

import java.awt.Color;

import it.unibo.exam.utility.geometry.Point2D;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents the projectile fired by the cannon in the Gym minigame.
 */
public class Projectile extends Disk {

    private final double angle;
    private boolean active;

    /**
     * Constructs a new projectile.
     * @param startPosition initial position
     * @param color projectile color
     * @param radius projectile radius
     * @param angle firing angle (radians)
     * @param env environment size
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
        justification = "Projectile is immutable after construction and safe to broadcast.")
    public Projectile(final Point2D startPosition, final Color color, final int radius, final double angle, final Point2D env) {
        super(startPosition, color, radius, env);
        this.angle = angle;
        this.active = true;
    }
    /**
     * Constructs a new projectile.
     * @param p
     */
    public Projectile(final Projectile p) {
        this(p.getPosition(), p.getColor(), p.getRadius(), p.getAngle(), p.getEnviromentSize());
    }

    /**
     * @return true if the projectile is active
     */
    public boolean isActive() { 
        return active; 
    }

    /**
     * Sets the projectile active state.
     * @param active true if active
     */
    public void setActive(final boolean active) { 
        this.active = active; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPopped() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pop() {
        throw new IllegalStateException("Projectille can't be popped");
    }

    /**
     * @return firing angle
     */
    public double getAngle() {
        return angle;
    }
    /**
     * Updates the projectile position based on angle and speed.
     */
    public void update() {
        if (active) {
            final int dx = (int) (super.getSpeed() * Math.cos(angle));
            final int dy = (int) (-super.getSpeed() * Math.sin(angle)); // NOTE THE MINUS SIGN!
            this.move(new Point2D(dx, dy));
        }
    }
}
