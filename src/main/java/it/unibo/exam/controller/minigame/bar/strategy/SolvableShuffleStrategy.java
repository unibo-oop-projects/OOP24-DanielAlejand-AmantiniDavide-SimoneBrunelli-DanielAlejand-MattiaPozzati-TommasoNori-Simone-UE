package it.unibo.exam.controller.minigame.bar.strategy;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Shuffles the puzzle by simulating random legal moves from the solved state,
 * guaranteeing that the result is always solvable and not initially solved.
 */
public class SolvableShuffleStrategy implements ShuffleStrategy {

    private static final int NUM_MIX_MOVES = 400; // You can tune for more or less mixing

    @Override
    public List<Color> shuffle(List<Color> pool, long seed) {
        // Infer puzzle config
        int totalLayers = pool.size();
        List<Color> uniqueColors = pool.stream().distinct().toList();

        // Heuristically guess capacity
        int capacity = (int) pool.stream().filter(c -> c.equals(uniqueColors.get(0))).count();
        int numGlasses = totalLayers / capacity + 1; // assumes 1 empty

        // 1. Start from solved state: one glass for each color, one empty
        List<Deque<Color>> glasses = new ArrayList<>();
        for (Color c : uniqueColors) {
            Deque<Color> glass = new ArrayDeque<>();
            for (int i = 0; i < capacity; i++) glass.push(c);
            glasses.add(glass);
        }
        while (glasses.size() < numGlasses) {
            glasses.add(new ArrayDeque<>());
        }

        // 2. Perform random legal moves
        Random rnd = new Random(seed);
        int realMoves = 0; // count legal moves performed
        for (int i = 0; i < NUM_MIX_MOVES; i++) {
            int from, to, attempts = 0;
            do {
                from = rnd.nextInt(numGlasses);
                to = rnd.nextInt(numGlasses);
                attempts++;
                if (attempts > 100) break; // Prevent infinite loop
            } while (
                from == to ||
                glasses.get(from).isEmpty() ||
                glasses.get(to).size() >= capacity ||
                (!glasses.get(to).isEmpty() && !glasses.get(to).peek().equals(glasses.get(from).peek()))
            );
            if (
                from != to &&
                !glasses.get(from).isEmpty() &&
                glasses.get(to).size() < capacity &&
                (glasses.get(to).isEmpty() || glasses.get(to).peek().equals(glasses.get(from).peek()))
            ) {
                glasses.get(to).push(glasses.get(from).pop());
                realMoves++;
            }
        }

        // 3. Output as a single list (bottle by bottle, top-to-bottom, only filled glasses)
        List<Color> shuffled = new ArrayList<>();
        for (int i = 0; i < numGlasses - 1; i++) {
            Deque<Color> glass = glasses.get(i);
            List<Color> stack = new ArrayList<>(glass);
            for (Color c : stack) {
                shuffled.add(c);
            }
        }


        // 4. If still solved (or not enough real moves), re-shuffle with a new seed
        if (isSolved(glasses, capacity) || realMoves < capacity * 2) { // require at least some mixing!
            return shuffle(pool, seed + 1);
        }
        return shuffled;
    }

    /** Checks if the puzzle is in a solved state. */
    private boolean isSolved(List<Deque<Color>> glasses, int capacity) {
        for (Deque<Color> glass : glasses) {
            if (glass.isEmpty()) continue;
            if (glass.size() != capacity) return false;
            Color first = glass.peek();
            for (Color c : glass) {
                if (!c.equals(first)) return false;
            }
        }
        return true;
    }
}
