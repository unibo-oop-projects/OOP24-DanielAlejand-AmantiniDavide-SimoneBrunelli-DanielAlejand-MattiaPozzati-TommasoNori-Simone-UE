package it.unibo.exam.model.scoring;

import java.util.Objects;

/**
 * Decorator that adds a flat bonus if the completion time is under a specified threshold.
 */
public final class TimeBonusDecorator extends ScoringDecorator {

    /** The time threshold (in seconds) under which the bonus applies. */
    private final int bonusThreshold;
    /** The number of bonus points to add. */
    private final int bonusPoints;

    /**
     * Constructs a TimeBonusDecorator.
     *
     * @param inner           the underlying strategy to wrap; must not be null
     * @param bonusThreshold  the time (in seconds) under which bonusPoints are awarded
     * @param bonusPoints     the number of points to add when timeTaken &lt; bonusThreshold
     */
    public TimeBonusDecorator(final ScoringStrategy inner,
                              final int bonusThreshold,
                              final int bonusPoints) {
        super(Objects.requireNonNull(inner, "inner strategy must not be null"));
        this.bonusThreshold = bonusThreshold;
        this.bonusPoints    = bonusPoints;
    }

    /**
     * Calculates points via the wrapped strategy, then adds a bonus if
     * the timeTaken is strictly less than bonusThreshold.
     *
     * @param timeTaken the time taken to complete the room (seconds)
     * @return the points awarded, including any bonus
     */
    @Override
    public int calculate(final int timeTaken) {
        final int base = super.calculate(timeTaken);
        return (timeTaken < bonusThreshold)
             ? base + bonusPoints
             : base;
    }
}
