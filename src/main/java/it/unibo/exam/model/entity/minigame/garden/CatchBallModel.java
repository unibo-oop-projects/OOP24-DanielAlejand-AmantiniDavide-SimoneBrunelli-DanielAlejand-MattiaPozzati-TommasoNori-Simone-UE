package it.unibo.exam.model.entity.minigame.garden;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The model for the CatchBallMinigame.
 * Generate the balls that falls from up, and manage the bottle entity catching them.
 */
public final class CatchBallModel {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TARGET_SCORE = 5;
    private static final int BALL_INTERVAL = 60; // frames
    private static final int BOTTLE_WIDTH = 60;
    private static final int BOTTLE_HEIGHT = 20;
    private static final int BOTTLE_Y_OFFSET = 60;
    private static final int BALL_START_Y = 10;

    private final List<BallEntity> balls = new ArrayList<>();
    private final BottleEntity bottle;
    private final Random random = new Random();

    private int score;
    private int lives = 3; // 3 palline possono cadere
    private int ballSpawnTimer;

     /**
     * Creates a new CatchBallModel with default size and bottle position.
     */
    public CatchBallModel() {
        this.bottle = new BottleEntity(WIDTH / 2 - BOTTLE_WIDTH, HEIGHT - BOTTLE_Y_OFFSET, BOTTLE_WIDTH, BOTTLE_HEIGHT);
    }

    /**
     * Updates the game state, moves the bottle and the balls, and checks for catches or misses.
     *
     * @param leftPressed  true if the left key is pressed
     * @param rightPressed true if the right key is pressed
     */
    public void update(final boolean leftPressed, final boolean rightPressed) {
        if (leftPressed) {
            bottle.moveLeft();
        }
        if (rightPressed) {
            bottle.moveRight(WIDTH);
        }

        final Iterator<BallEntity> it = balls.iterator();
        while (it.hasNext()) {
            final BallEntity ball = it.next();
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
            final int x = random.nextInt(WIDTH - 10);
            balls.add(new BallEntity(x, BALL_START_Y));
        }
    }

     /**
     * Returns true if the player has won the game.
     *
     * @return true if the score is at least TARGET_SCORE
     */
    public boolean hasWon() {
        return score >= TARGET_SCORE;
    }

    /**
     * Returns the current score (balls caught).
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

     /**
     * Returns the list of active balls in the game.
     *
     * @return the list of BallEntity
     */
    public List<BallEntity> getBalls() {
        return balls;
    }

    /**
     * Returns the player's bottle entity.
     *
     * @return the BottleEntity
     */
    public BottleEntity getBottle() {
        return bottle;
    }

    /**
     * Returns the number of lives (missed balls remaining).
     *
     * @return the number of lives
     */
    public int getLives() {
        return lives;
    }

     /**
     * Returns true if the player has lost (all lives are gone).
     *
     * @return true if lives <= 0
     */
    public boolean hasLost() {
        return lives <= 0;
    }
}
