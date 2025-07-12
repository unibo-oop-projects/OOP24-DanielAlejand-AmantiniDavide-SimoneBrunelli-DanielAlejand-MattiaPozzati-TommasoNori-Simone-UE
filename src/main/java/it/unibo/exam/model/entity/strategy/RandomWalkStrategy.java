package it.unibo.exam.model.entity.strategy;

import it.unibo.exam.model.entity.MovementEntity;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.geometry.Point2D;
import java.util.Random;

/**
 * A random‐walk strategy that accumulates fractional movement
 * so you actually move at sub‐pixel speeds.
 * 
 * This class implements the {@link MovementStrategy} interface and provides 
 * a strategy for random movement within a specified environment. The entity 
 * will randomly choose targets within the environment and move towards them, 
 * with fractional movement accumulated over time for sub-pixel precision.
 * 
 * This class is not designed for extension. If you need to modify its behavior, 
 * consider creating a new strategy by implementing the {@link MovementStrategy} 
 * interface instead.
 */
public final class RandomWalkStrategy implements MovementStrategy {

    private static final int THRESHOLD = 5; // pixels “close enough” to pick a new target
    private static final double SPEED_MULTIPLIER = 20.0; // multiplier for speed

    private final Random random = new Random();
    private Point2D target;

    // carry‐over fractions from frame to frame
    private double residualX;
    private double residualY;

    // the play‐area dimensions, supplied by MainController
    private final int maxX;
    private final int maxY;

    /**
     * Constructs a RandomWalkStrategy with the given environment size.
     * 
     * @param environmentSize the size of the game panel in pixels, 
     *                        which defines the bounds for random movement.
     */
    public RandomWalkStrategy(final Point2D environmentSize) {
        this.maxX = environmentSize.getX();
        this.maxY = environmentSize.getY();
    }

    /**
     * Calculates the next movement of the entity based on the random walk strategy.
     * 
     * This method computes the movement towards a random target. If the entity 
     * is close enough to the target, a new target is chosen. The movement is 
     * calculated with sub-pixel precision, and the entity's speed is scaled by 
     * the elapsed time (deltaTime) and a speed multiplier.
     * 
     * If subclassing is intended, this method may be overridden to provide 
     * custom movement logic. However, ensure that the new behavior maintains 
     * the same method signature and logic flow for compatibility.
     * 
     * @param entity     the entity being moved, whose current position and speed 
     *                   are used to calculate the movement.
     * @param room       the current room (unused here but passed for compatibility).
     * @param deltaTime  the time in seconds that has passed since the last update, 
     *                   used to scale the movement.
     * @return           a {@link Point2D} representing the movement vector (dx, dy)
     *                   to be applied to the entity's position.
     */
    @Override
    public Point2D getNextMove(final MovementEntity entity,
                                final Room room, 
                                final double deltaTime) {
        final Point2D pos = entity.getPosition();

        // 1) Pick a new random target if needed
        if (target == null || pos.distance(target) < THRESHOLD) {
            final int tx = random.nextInt(maxX);
            final int ty = random.nextInt(maxY);
            target = new Point2D(tx, ty);
        }

        // 2) Compute direction toward target
        final double dx = target.getX() - pos.getX();
        final double dy = target.getY() - pos.getY();
        final double dist = Math.hypot(dx, dy);

        if (dist < 1) {
            return new Point2D(0, 0); // already "there"
        }

        final double dirX = dx / dist;
        final double dirY = dy / dist;

        // 3) Calculate movement distance, scaled by speed and deltaTime
        double moveDist = entity.getSpeed() * deltaTime;

        // Speed multiplier: to make NPCs move faster
        moveDist *= SPEED_MULTIPLIER;  // Increase by a factor of 3 or more to speed things up.

        // 4) Move in the chosen direction
        final double rawX = dirX * moveDist;
        final double rawY = dirY * moveDist;

        // 5) Add leftover fractions from last tick
        final double totalX = rawX + residualX;
        final double totalY = rawY + residualY;

        // 6) Extract integer pixels we can actually move
        final int stepX = (int) Math.floor(totalX);
        final int stepY = (int) Math.floor(totalY);

        // 7) Keep the new leftover fractions
        residualX = totalX - stepX;
        residualY = totalY - stepY;

        return new Point2D(stepX, stepY);
    }
}
