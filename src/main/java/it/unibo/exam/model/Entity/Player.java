package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;

/**
 * Simple player class.
 * @see MovementEntity
 */
public class Player extends MovementEntity {

    private static final int SCALE_FACTOR = 20; 
    private final int score;

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
