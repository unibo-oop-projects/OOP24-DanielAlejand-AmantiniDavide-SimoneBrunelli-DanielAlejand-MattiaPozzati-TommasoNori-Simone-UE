package it.unibo.exam.model.scoring;

import java.util.Objects;

/**
 * A base decorator for {@link ScoringStrategy} that delegates
 * point calculation to an inner strategy. Subclasses can override
 * {@link #calculate(int, int)} to add behavior before or after
 * calling {@link #getInner()}.
 */
public abstract class ScoringDecorator implements ScoringStrategy {

    /** The wrapped scoring strategy. */
    private final ScoringStrategy inner;

    /**
     * Creates a new ScoringDecorator wrapping the given strategy.
     *
     * @param inner the underlying strategy to delegate to; must not be null
     */
    public ScoringDecorator(final ScoringStrategy inner) {
        this.inner = Objects.requireNonNull(inner, "inner strategy must not be null");
    }

    /**
     * Returns the wrapped {@link ScoringStrategy}.
     *
     * @return the inner strategy
     */
    protected final ScoringStrategy getInner() {
        return this.inner;
    }

    /**
     * {@inheritDoc}
     * <p>
     * By default this method simply forwards to {@link #getInner()}.
     * Subclasses may add additional behavior.
     * </p>
     *
     * @param timeTaken the time taken to complete the room (seconds)
     * @return the points awarded by the inner strategy
     */
    @Override
    public int calculate(final int timeTaken) {
        return this.inner.calculate(timeTaken);
    }
}
