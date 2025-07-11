package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;
import java.util.Random;

public class RandomWalkStrategy implements MovementStrategy {
    private static final int THRESHOLD = 5;  // “close enough” to pick a new target

    private final int maxX;
    private final int maxY;
    private final Random random = new Random();
    private Point2D target = null;

    /**
     * @param environmentSize the full play‐area size (from MainController)
     */
    public RandomWalkStrategy(final Point2D environmentSize) {
        this.maxX = environmentSize.getX();
        this.maxY = environmentSize.getY();
    }

    @Override
    public Point2D getNextMove(final MovementEntity entity,
                               final Room room,
                               final double deltaTime) {
        Point2D pos = entity.getPosition();

        // 1) Pick a brand‐new random target if we have none or we’re “there”
        if (target == null || pos.distance(target) < THRESHOLD) {
            int tx = random.nextInt(maxX);
            int ty = random.nextInt(maxY);
            target = new Point2D(tx, ty);
        }

        // 2) Compute the vector toward target
        int dx = target.getX() - pos.getX();
        int dy = target.getY() - pos.getY();
        double dist = Math.hypot(dx, dy);
        if (dist < 1) {
            return new Point2D(0, 0);
        }

        // 3) Scale by speed × deltaTime (clamped not to overshoot)
        double moveDist = entity.getSpeed() * deltaTime;
        if (moveDist > dist) moveDist = dist;
        int moveX = (int) Math.round(dx / dist * moveDist);
        int moveY = (int) Math.round(dy / dist * moveDist);

        return new Point2D(moveX, moveY);
    }
}
