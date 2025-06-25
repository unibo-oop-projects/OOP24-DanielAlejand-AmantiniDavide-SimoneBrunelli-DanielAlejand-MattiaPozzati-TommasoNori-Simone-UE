package it.unibo.exam.model.scoring;

/**
 * Ensures the final score never exceeds a specified maximum.
 */
public class CapDecorator extends ScoringDecorator {
    private final int maxPoints;

    public CapDecorator(ScoringStrategy inner, int maxPoints) {
        super(inner);
        this.maxPoints = maxPoints;
    }

    @Override
    public int calculate(int timeTaken, int roomId) {
        int scored = super.calculate(timeTaken, roomId);
        return Math.min(maxPoints, scored);
    }
}
