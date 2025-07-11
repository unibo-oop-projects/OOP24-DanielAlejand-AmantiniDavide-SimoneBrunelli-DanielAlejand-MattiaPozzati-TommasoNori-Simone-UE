package it.unibo.exam.model.scoring;

import java.util.Objects;

/**
 * Decorator that enforces an upper limit on the points awarded by an underlying strategy.
 */
public final class CapDecorator extends ScoringDecorator {

    private final int maxPoints;

    /**
     * Constructs a CapDecorator.
     *
     * @param inner     the underlying strategy to wrap—must not be null
     * @param maxPoints the maximum points allowed after calculation
     */
    public CapDecorator(final ScoringStrategy inner, final int maxPoints) {
        super(Objects.requireNonNull(inner, "inner strategy must not be null"));
        this.maxPoints = maxPoints;
    }

    /**
     * Calculates points using the wrapped strategy and then caps the result.
     *
     * @param data the data used for scoring, such as time taken or room context
     * @return the capped points, never greater than {@code maxPoints}
     */
    @Override
    public int calculate(final int data) {
        final int scored = super.calculate(data);
        return Math.min(maxPoints, scored);
    }
}
