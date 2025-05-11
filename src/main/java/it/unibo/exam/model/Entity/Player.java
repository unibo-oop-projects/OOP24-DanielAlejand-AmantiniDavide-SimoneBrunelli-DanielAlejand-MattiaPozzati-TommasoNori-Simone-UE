package it.unibo.exam.model.Entity;

import it.unibo.exam.utility.Geometry.Point2D;

public class Player extends MovementEntity {

    public static final int SCALE_FACTOR = 20; // Reordered static variable
    private final int score; // Made 'score' final

    /**
     * Constructor for Player.
     *
     * @param enviromentSize the size of the environment
     */
    public Player(final Point2D enviromentSize) {
        super(SCALE_FACTOR, enviromentSize);
        this.score = 0;
    }

    /**
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }
}
