package it.unibo.exam.controller.minigame.bar.strategy;

import java.awt.Color;
import java.util.List;

/**
 * Encapsulates an algorithm for shuffling a list of layers.
 */
public interface ShuffleStrategy {

    /**
     * Returns a new list containing all elements of {@code layers},
     * shuffled according to this strategy and seed.
     *
     * @param layers the original flat list of layer‐colors
     * @param seed   the RNG seed to use
     * @return a shuffled copy of {@code layers}
     */
    List<Color> shuffle(List<Color> layers, long seed);
}
