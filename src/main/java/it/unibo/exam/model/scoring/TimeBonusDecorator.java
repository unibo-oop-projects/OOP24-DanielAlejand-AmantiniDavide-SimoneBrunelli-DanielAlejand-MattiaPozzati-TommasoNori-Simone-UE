package it.unibo.exam.model.scoring;

/**
 * Adds a flat bonus if clearing the room under a threshold.
 */
public class TimeBonusDecorator extends ScoringDecorator {
    private final int bonusThreshold;
    private final int bonusPoints;

    public TimeBonusDecorator(ScoringStrategy inner,
                              int bonusThreshold,
                              int bonusPoints) {
        super(inner);
        this.bonusThreshold = bonusThreshold;
        this.bonusPoints    = bonusPoints;
    }

    @Override
    public int calculate(int timeTaken, int roomId) {
        int base = super.calculate(timeTaken, roomId);
        return timeTaken < bonusThreshold
             ? base + bonusPoints
             : base;
    }
}
