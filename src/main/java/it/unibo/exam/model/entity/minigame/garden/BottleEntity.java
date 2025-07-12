package it.unibo.exam.model.entity.minigame.garden;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import it.unibo.exam.utility.medialoader.AssetLoader;


/**
 * Represents the bottle controlled by the player in the CatchBall minigame.
 */
public final class BottleEntity {

    /** The horizontal move speed of the bottle (pixels per update). */
    private static final int MOVE_SPEED = 6;
    private static final Image BOTTLE_IMAGE;
    static {
        BOTTLE_IMAGE = AssetLoader.loadImage("Garden/bottle.png");
    }

    @SuppressWarnings("PMD.ImmutableField")
    private int x;
    @SuppressWarnings("PMD.ImmutableField")
    private int y;
    private final int width;
    private final int height;

    /**
     * Constructs a BottleEntity at the given position and size.
     *
     * @param x      the x coordinate (top-left corner)
     * @param y      the y coordinate (top-left corner)
     * @param width  the width of the bottle
     * @param height the height of the bottle
     */
    public BottleEntity(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves the bottle to the left by MOVE_SPEED pixels.
     */
    public void moveLeft() {
        x = Math.max(0, x - MOVE_SPEED);
    }

    /**
     * Moves the bottle to the right by MOVE_SPEED pixels, within the given boundary.
     *
     * @param boundary the right boundary (maximum x + width)
     */
    public void moveRight(final int boundary) {
        x = Math.min(boundary - width, x + MOVE_SPEED);
    }

    /**
     * Checks if the bottle catches a given ball.
     *
     * @param ball the BallEntity to check collision with
     * @return true if the ball is caught by the bottle, false otherwise
     */
    public boolean catchBall(final BallEntity ball) {
        return ball.getX() + ball.getRadius() > x
            && ball.getX() - ball.getRadius() < x + width
            && ball.getY() + ball.getRadius() >= y
            && ball.getY() - ball.getRadius() <= y + height;
    }

    /**
     * Draws the bottle on the provided graphics context.
     *
     * @param g2 the Graphics2D context to draw on
     */
    public void draw(final Graphics2D g2) {
        if (BOTTLE_IMAGE != null) {
            g2.drawImage(BOTTLE_IMAGE, x, y, width, height, null);
        } else {
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, width, height);
        }
    }
}
