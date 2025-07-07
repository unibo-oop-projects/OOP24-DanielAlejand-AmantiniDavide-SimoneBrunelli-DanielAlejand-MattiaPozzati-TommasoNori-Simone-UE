package it.unibo.exam.model.entity.minigame.garden;

import java.awt.Color;
import java.awt.Graphics2D;

public class BallEntity {

    private int x, y;
    private static final int RADIUS = 10;
    private static final int FALL_SPEED = 4;

    public BallEntity(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += FALL_SPEED;
    }

    public boolean isOffScreen(final int height) {
        return y - RADIUS > height;
    }

    public void draw(final Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return RADIUS;
    }
}
