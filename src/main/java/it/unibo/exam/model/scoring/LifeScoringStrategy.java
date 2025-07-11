package it.unibo.exam.model.scoring;

/**
 * Default scoring strategy awarding fixed points based on completion time tiers.
 * <ul>
 *   <li>Fast: &lt; FAST_THRESHOLD seconds → POINTS_FAST</li>
 *   <li>Medium: &lt; MEDIUM_THRESHOLD seconds → POINTS_MEDIUM</li>
 *   <li>Slow: otherwise → POINTS_SLOW</li>
 * </ul>
 */
public final class LifeScoringStrategy implements ScoringStrategy {

    private static final int FULL_LIVES = 3; // Assuming full lives is 3
    private static final int TWO_LIVES = 2; // Assuming two lives remaining
    private static final int ONE_LIFE = 1; // Assuming one life remaining
    private static final int POINTS_MAX = 100;
    private static final int POINTS_NORMAL = 60;
    private static final int POINTS_BAD = 30;


    /**
     * Calculates points based on how quickly a room was cleared.
     *
     * @param lives the number of lives remaining
     * @return the points awarded for the given time
     */
    @Override
    public int calculate(final int lives) {
        switch (lives) {
            case FULL_LIVES:
                return POINTS_MAX;
            case TWO_LIVES:
                return POINTS_NORMAL;
            case ONE_LIFE:
                return POINTS_BAD;
            default:
                return 0; // No points if no lives are left
        }

    }
}
