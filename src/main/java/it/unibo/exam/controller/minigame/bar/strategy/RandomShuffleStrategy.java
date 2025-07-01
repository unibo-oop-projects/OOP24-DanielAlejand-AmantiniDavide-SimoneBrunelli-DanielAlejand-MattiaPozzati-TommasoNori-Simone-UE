package it.unibo.exam.controller.minigame.bar.strategy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Shuffles layers using Collections.shuffle with a seeded Random.
 */
public final class RandomShuffleStrategy implements ShuffleStrategy {

    @Override
    public List<Color> shuffle(final List<Color> layers, final long seed) {
        final List<Color> copy = new ArrayList<>(layers);
        Collections.shuffle(copy, new Random(seed));
        return copy;
    }
}
