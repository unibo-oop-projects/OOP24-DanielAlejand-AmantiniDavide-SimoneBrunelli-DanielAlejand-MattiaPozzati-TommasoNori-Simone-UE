package it.unibo.exam.utility.factory;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.controller.minigame.bar.BarMinigame;

public final class MinigameFactory {
    private MinigameFactory() { throw new UnsupportedOperationException(); }

    public static Minigame createMinigame(final int roomId) {
        switch (roomId) {
            case 1:
                return new BarMinigame();                // ← hook in Sort & Serve
            // case 2: return new MazeMinigame();
            // case 3: return new QuizMinigame();
            // case 4: return new MemoryMatchMinigame();
            default:
                throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    public static String getMinigameName(final int roomId) {
        switch (roomId) {
            case 1: return "Sort & Serve";             // ← human-readable name
            // …
            default: throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    public static String getMinigameDescription(final int roomId) {
        switch (roomId) {
            case 1: return "Pour colored layers until each glass is uniform.";
            // …
            default: throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    public static boolean hasMinigame(final int roomId) {
        return roomId >= 1 && roomId <= 4;
    }
}
