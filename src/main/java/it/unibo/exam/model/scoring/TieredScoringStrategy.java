package it.unibo.exam.model.scoring;

public class TieredScoringStrategy implements ScoringStrategy {
    private static final int FAST_THRESHOLD   = 30;
    private static final int MEDIUM_THRESHOLD = 60;
    private static final int POINTS_FAST      = 100;
    private static final int POINTS_MEDIUM    =  70;
    private static final int POINTS_SLOW      =  40;

    @Override
    public int calculate(int timeTaken, int roomId) {
        if (timeTaken < FAST_THRESHOLD)   return POINTS_FAST;
        if (timeTaken < MEDIUM_THRESHOLD) return POINTS_MEDIUM;
        return POINTS_SLOW;
    }
}

