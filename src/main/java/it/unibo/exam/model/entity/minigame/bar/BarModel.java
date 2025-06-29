package it.unibo.exam.model.entity.minigame.bar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The model for the Sort-&-Serve bar puzzle.
 * Manages a fixed number of glasses, each holding a fixed capacity of colored layers.
 */
public final class BarModel {
    private final List<Glass> glasses;
    private final int numGlasses;
    private final int capacity;

    /**
     * Creates a BarModel with the given configuration.
     *
     * @param numGlasses how many glasses to create
     * @param capacity   how many layers each glass holds
     * @param colors     exactly numGlasses distinct colors, each to be repeated capacity times
     */
    public BarModel(final int numGlasses,
                    final int capacity,
                    final List<Color> colors) {
        this.numGlasses = numGlasses;
        this.capacity   = capacity;

        this.glasses = new ArrayList<>(this.numGlasses);
        for (int i = 0; i < this.numGlasses; i++) {
            this.glasses.add(new Glass());
        }

        final List<Color> pool = new ArrayList<>(this.numGlasses * this.capacity);
        for (final Color c : colors) {
            for (int i = 0; i < this.capacity; i++) {
                pool.add(c);
            }
        }

        Collections.shuffle(pool);
        final Iterator<Color> it = pool.iterator();
        for (final Glass g : this.glasses) {
            for (int layer = 0; layer < this.capacity; layer++) {
                g.addLayer(it.next());
            }
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
        final Glass a = this.glasses.get(from);
        final Glass b = this.glasses.get(to);
        if (!a.canPourInto(b)) {
            return false;
        }
        a.pourInto(b);
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
}
