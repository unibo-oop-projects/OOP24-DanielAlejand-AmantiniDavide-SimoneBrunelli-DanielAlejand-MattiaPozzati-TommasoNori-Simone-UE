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
     * @param timeTaken the time taken to complete the room (in seconds)
     * @param roomId    the identifier of the room
     * @return the number of points to award
     */
    int calculate(int timeTaken, int roomId);
}
