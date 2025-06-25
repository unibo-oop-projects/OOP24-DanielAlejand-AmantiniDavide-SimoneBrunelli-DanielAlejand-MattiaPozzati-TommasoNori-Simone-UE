package it.unibo.exam.model.scoring;

public abstract class ScoringDecorator implements ScoringStrategy {
    protected final ScoringStrategy inner;
    protected ScoringDecorator(ScoringStrategy inner) {
        this.inner = inner;
    }
    @Override
    public int calculate(int timeTaken, int roomId) {
        return inner.calculate(timeTaken, roomId);
    }
}
