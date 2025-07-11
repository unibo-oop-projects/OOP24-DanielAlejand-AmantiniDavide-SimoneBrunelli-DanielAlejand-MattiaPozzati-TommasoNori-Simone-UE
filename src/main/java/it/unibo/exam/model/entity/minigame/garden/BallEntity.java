package it.unibo.exam.model.entity.minigame.garden;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import it.unibo.exam.utility.medialoader.AssetLoader;

/**
 * Represents a falling ball in the CatchBall minigame.
 */
public final class BallEntity {

    /** Ball radius in pixels. */
    private static final int RADIUS = 15;
    /** Falling speed in pixels per update. */
    private static final int FALL_SPEED = 4;
    private static final Image DROP_IMAGE;

    static {
        DROP_IMAGE = AssetLoader.loadImage("Garden/water.png");
    }

    @SuppressWarnings("PMD.ImmutableField")
    private int x;
    @SuppressWarnings("PMD.ImmutableField")
    private int y;

    /**
     * Constructs a BallEntity at the given position.
     *
     * @param x the x coordinate of the ball's center
     * @param y the y coordinate of the ball's center
     */
    public BallEntity(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the ball's position by moving it down.
     */
    public void update() {
        y += FALL_SPEED;
    }

     /**
     * Checks if the ball is off the screen (below the given height).
     *
     * @param height the height of the screen/panel
     * @return true if the ball is below the screen, false otherwise
     */
    public boolean isOffScreen(final int height) {
        return y - RADIUS > height;
    }

     /**
     * Draws the ball on the provided graphics context.
     *
     * @param g2 the Graphics2D context to draw on
     */
    public void draw(final Graphics2D g2) {
        if (DROP_IMAGE != null) {
            g2.drawImage(DROP_IMAGE, x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2, null);
        } else {
            g2.setColor(Color.YELLOW);
            g2.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        }
    }

    /**
     * Returns the x coordinate of the ball's center.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the ball's center.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the radius of the ball.
     *
     * @return the radius
     */
    public int getRadius() {
        return RADIUS;
    }
}
