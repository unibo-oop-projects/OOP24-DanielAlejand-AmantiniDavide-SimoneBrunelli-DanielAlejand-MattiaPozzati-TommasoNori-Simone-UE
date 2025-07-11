package it.unibo.exam.model.scoring;

/**
 * Defines the contract for all scoring algorithms.
 * Implementations calculate how many points to award
 * based on the time taken and the room context.
 */
public interface ScoringStrategy {

    /**
     * Calculate the points to award for clearing a room.
     *
     * @param data the data used for scoring, such as time taken or room context
     * @return the number of points to award
     */
    int calculate(int data);
}
