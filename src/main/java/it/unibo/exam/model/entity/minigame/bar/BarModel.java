package it.unibo.exam.model.entity.minigame.bar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import it.unibo.exam.controller.minigame.bar.strategy.RandomShuffleStrategy;
import it.unibo.exam.controller.minigame.bar.strategy.ShuffleStrategy;

/**
 * The model for the Sort-&-Serve bar puzzle.
 * Manages a fixed number of glasses, each holding a fixed capacity of colored layers.
 */
public final class BarModel {
    private final List<Glass> glasses;
    private final int numGlasses;
    private final int capacity;
    private final List<PuzzleListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Creates a BarModel with the given configuration.
     *
     * @param numGlasses      how many glasses to create
     * @param capacity        how many layers each glass holds
     * @param colors          exactly numGlasses distinct colors
     * @param shuffleSeed     the RNG seed to use when shuffling layers
     * @param shuffleStrategy the algorithm to apply when shuffling the layers
     */
        public BarModel(final int numGlasses,
                    final int capacity,
                    final List<Color> colors,
                    final long shuffleSeed,
                    final ShuffleStrategy shuffleStrategy) {
        this.numGlasses = numGlasses;
        this.capacity   = capacity;

        this.glasses = new ArrayList<>(this.numGlasses);
        for (int i = 0; i < this.numGlasses; i++) {
            this.glasses.add(new Glass(this.capacity));
        }
        final int coloredGlasses = this.numGlasses - 2; // one extra empty glass
        final List<Color> pool = new ArrayList<>(coloredGlasses * this.capacity);
        for (final Color c : colors) {
            for (int i = 0; i < this.capacity; i++) {
                pool.add(c);
            }
        }

        final List<Color> shuffled = shuffleStrategy.shuffle(pool, shuffleSeed);
        final Iterator<Color> it = shuffled.iterator();
        for (int i = 0; i < this.numGlasses; i++) {
            final Glass g = this.glasses.get(i);
            // Fill only the first (numGlasses - 1) glasses
            if (i < coloredGlasses) {
                for (int layer = 0; layer < this.capacity; layer++) {
                    g.addLayer(it.next());
                }
            }
            // else: leave last glass empty
        }
    }

    /**
     * Attempts to pour the top layer of one glass into another.
     *
     * @param from the index of the source glass (0-based)
     * @param to   the index of the target glass (0-based)
     * @return true if the move was valid and performed; false otherwise
     */
    public boolean attemptPour(final int from, final int to) {
        final Glass a = glasses.get(from);
        final Glass b = glasses.get(to);
        if (!a.canPourInto(b)) {
            return false;
        }
        a.pourInto(b);

        // fire pour event
        for (final PuzzleListener lst : listeners) {
            lst.onPoured(from, to);
        }

        // check for completion
        final boolean nowCompleted = glasses.stream()
                                            .allMatch(g -> g.isUniform(capacity));
        if (nowCompleted) {
            for (final PuzzleListener lst : listeners) {
                lst.onCompleted();
            }
        }
        return true;
    }

    /**
     * @return an unmodifiable view of the glass list
     */
    public List<Glass> getGlasses() {
        return Collections.unmodifiableList(this.glasses);
    }

    /**
     * @return the number of glasses in this model
     */
    public int getNumGlasses() {
        return this.numGlasses;
    }

    /**
     * @return the capacity (layers per glass) in this model
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Registers a listener to receive pour/completion events.
     *
     * @param listener the listener to add
     */
    public void addListener(final PuzzleListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregisters a previously-added listener.
     *
     * @param listener the listener to remove
     */
    public void removeListener(final PuzzleListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fluent builder for BarModel.
     */
    public static final class Builder {
        private int numGlasses      = 4;
        private int capacity        = 4;
        private List<Color> colors  = List.of(
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
        );
        private long shuffleSeed    = System.currentTimeMillis();
        private ShuffleStrategy shuffleStrategy = new RandomShuffleStrategy();

        /**
         * Sets how many glasses the puzzle will have.
         *
         * @param n how many glasses
         * @return this Builder instance
         */
        public Builder numGlasses(final int n) {
            this.numGlasses = n;
            return this;
        }

        /**
         * Sets how many layers each glass holds.
         *
         * @param c how many layers per glass
         * @return this Builder instance
         */
        public Builder capacity(final int c) {
            this.capacity = c;
            return this;
        }

        /**
         * Specifies the distinct colors to use.
         *
         * @param cols the distinct colors (size should match numGlasses)
         * @return this Builder instance
         */
        public Builder colors(final Color... cols) {
            this.colors = List.of(cols);
            return this;
        }

        /**
         * Specifies the seed for the shuffle RNG.
         *
         * @param seed the RNG seed for shuffling layers
         * @return this Builder instance
         */
        public Builder shuffleSeed(final long seed) {
            this.shuffleSeed = seed;
            return this;
        }

        /**
         * @param strategy the shuffle algorithm to use
         * @return this Builder
         */
        public Builder shuffleStrategy(final ShuffleStrategy strategy) {
            this.shuffleStrategy = strategy;
            return this;
        }

        /**
         * Builds a new BarModel using the configured parameters.
         *
         * @return a new BarModel instance with the current settings
         */
        public BarModel build() {
            return new BarModel(
                this.numGlasses,
                this.capacity,
                this.colors,
                this.shuffleSeed,
                this.shuffleStrategy
            );
        }
    }
}
