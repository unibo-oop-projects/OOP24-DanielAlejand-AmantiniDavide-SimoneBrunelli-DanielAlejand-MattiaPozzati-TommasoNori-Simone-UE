package it.unibo.exam.model.entity.minigame.bar;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents a single glass in the Sort-&-Serve puzzle,
 * holding a stack of colored layers.
 */
public final class Glass {

    private final Deque<Color> layers;
    private final int capacity;

    /**
     * Constructs a glass with a fixed capacity.
     * @param capacity maximum number of layers this glass can hold
     */
    public Glass(final int capacity) {
        this.capacity = capacity;
        this.layers = new ArrayDeque<>();
    }

    /**
     * Adds one layer of the given color on top of this glass.
     *
     * @param c the color to add as the top layer
     */
    public void addLayer(final Color c) {
        layers.push(c);
    }

    /**
     * Checks whether the top layer of this glass can be poured into another.
     * It is valid if this glass is non-empty, the target is not full,
     * and either the target glass is empty or its top layer matches this top layer.
     *
     * @param other the target glass
     * @return {@code true} if a pour from this to {@code other} is allowed
     */
    public boolean canPourInto(final Glass other) {
        // Cannot pour if this glass is empty or if target is full!
        if (layers.isEmpty() || other.layers.size() >= other.capacity) {
            return false;
        }
        return other.layers.isEmpty() || other.layers.peek().equals(layers.peek());
    }

    /**
     * Moves the top layer from this glass into the target glass.
     * Call {@link #canPourInto(Glass)} first to ensure the move is valid.
     *
     * @param other the glass to pour into
     * @throws IllegalStateException if {@code canPourInto(other)} is {@code false}
     */
    public void pourInto(final Glass other) {
        if (!canPourInto(other)) {
            throw new IllegalStateException("Cannot pour from this glass into the target");
        }
        other.layers.push(layers.pop());
    }

    /**
     * Exposes a snapshot of the current layers (top first).
     *
     * @return a new deque containing the same elements as this glass’s stack,
     *         so callers can’t modify the internal state
     */
    public Deque<Color> getLayers() {
        return new ArrayDeque<>(layers);
    }

    /**
     * @param capacity the expected size of the stack
     * @return true if this glass has exactly 'capacity' layers
     *         and they’re all the same color
     */
    public boolean isUniform(final int capacity) {
        if (layers.size() != capacity) {
            return false;
        }
        final Color top = layers.peek();
        // allMatch requires a stream, so:
        return layers.stream().allMatch(c -> c.equals(top));
    }
}
