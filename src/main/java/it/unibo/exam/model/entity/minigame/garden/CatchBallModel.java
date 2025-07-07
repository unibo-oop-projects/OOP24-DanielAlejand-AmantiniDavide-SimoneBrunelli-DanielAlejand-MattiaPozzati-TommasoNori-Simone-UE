package it.unibo.exam.model.entity.minigame.garden;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CatchBallModel {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TARGET_SCORE = 5;
    private static final int BALL_INTERVAL = 60; // frames

    private final List<BallEntity> balls = new ArrayList<>();
    private final BottleEntity bottle;
    private final Random random = new Random();

    private int score = 0;
    private int lives = 3; // 3 palline possono cadere
    private int ballSpawnTimer = 0;

    public CatchBallModel() {
        this.bottle = new BottleEntity(WIDTH / 2 - 20, HEIGHT - 60, 40, 20);
    }

    public void update(boolean leftPressed, boolean rightPressed) {
        if (leftPressed) {
            bottle.moveLeft();
        }
        if (rightPressed) {
            bottle.moveRight(WIDTH);
        }

        Iterator<BallEntity> it = balls.iterator();
        while (it.hasNext()) {
            BallEntity ball = it.next();
            ball.update();

            if (bottle.catchBall(ball)) {
                it.remove();
                score++;
            } else if (ball.isOffScreen(HEIGHT)) {
                it.remove();
                lives--;
            }
        }

        ballSpawnTimer++;
        if (ballSpawnTimer > BALL_INTERVAL) {
            ballSpawnTimer = 0;
            int x = random.nextInt(WIDTH - 10);
            balls.add(new BallEntity(x, 10));
        }
    }

    public boolean hasWon() {
        return score >= TARGET_SCORE;
    }

    public int getScore() {
        return score;
    }

    public List<BallEntity> getBalls() {
        return balls;
    }

    public BottleEntity getBottle() {
        return bottle;
    }

    public int getLives() {
        return lives;
    }
    public boolean hasLost() {
        return lives <= 0;
    }
}
