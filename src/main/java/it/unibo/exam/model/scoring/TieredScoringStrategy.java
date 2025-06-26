package it.unibo.exam.model.scoring;

/**
 * Default scoring strategy awarding fixed points based on completion time tiers.
 * <ul>
 *   <li>Fast: &lt; FAST_THRESHOLD seconds → POINTS_FAST</li>
 *   <li>Medium: &lt; MEDIUM_THRESHOLD seconds → POINTS_MEDIUM</li>
 *   <li>Slow: otherwise → POINTS_SLOW</li>
 * </ul>
 */
public final class TieredScoringStrategy implements ScoringStrategy {

    private static final int FAST_THRESHOLD   = 30;
    private static final int MEDIUM_THRESHOLD = 60;
    private static final int POINTS_FAST      = 100;
    private static final int POINTS_MEDIUM    =  70;
    private static final int POINTS_SLOW      =  40;

    /**
     * Calculates points based on how quickly a room was cleared.
     *
     * @param timeTaken the time taken to complete the room (in seconds)
     * @param roomId    the identifier of the room (unused in this strategy)
     * @return the points awarded for the given time
     */
    @Override
    public int calculate(final int timeTaken, final int roomId) {
        if (timeTaken < FAST_THRESHOLD) {
            return POINTS_FAST;
        } else if (timeTaken < MEDIUM_THRESHOLD) {
            return POINTS_MEDIUM;
        } else {
            return POINTS_SLOW;
        }
    }
}
