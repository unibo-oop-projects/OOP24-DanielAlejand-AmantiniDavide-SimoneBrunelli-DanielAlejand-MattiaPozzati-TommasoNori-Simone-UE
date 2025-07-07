package it.unibo.exam.model.entity.minigame.garden;

import java.awt.Color;
import java.awt.Graphics2D;

public class BottleEntity {

    private int x, y, width, height;
    private static final int MOVE_SPEED = 6;

    public BottleEntity(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void moveLeft() {
        x = Math.max(0, x - MOVE_SPEED);
    }

    public void moveRight(final int boundary) {
        x = Math.min(boundary - width, x + MOVE_SPEED);
    }

    public boolean catchBall(final BallEntity ball) {
        return ball.getX() + ball.getRadius() > x &&
               ball.getX() - ball.getRadius() < x + width &&
               ball.getY() + ball.getRadius() >= y &&
               ball.getY() - ball.getRadius() <= y + height;
    }

    public void draw(final Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, width, height);
    }
} 
