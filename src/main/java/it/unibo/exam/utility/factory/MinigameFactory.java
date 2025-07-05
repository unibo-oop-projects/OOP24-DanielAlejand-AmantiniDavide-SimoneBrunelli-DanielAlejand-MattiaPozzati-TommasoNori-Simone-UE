package it.unibo.exam.utility.factory;

import it.unibo.exam.model.entity.minigame.KahootMinigame;
import it.unibo.exam.model.entity.minigame.Minigame;

/**
 * Factory class for creating different types of minigames based on room ID.
 * Each room has its own specific minigame type.
 */
public final class MinigameFactory {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private MinigameFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Creates the appropriate minigame for the specified room.
     * 
     * Room-Minigame mapping:
     * - Room 1 (Bar): Kahoot Quiz
     * 
     * @param roomId the ID of the room (1-5)
     * @return the corresponding minigame instance
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static Minigame createMinigame(final int roomId) {
        switch (roomId) {
            //TODO Scrivete con le vostre stanze, marrani
            // Bar
            case 2: return new KahootMinigame();
            // case 2: return "return new MAZE()";
            default:
                throw new IllegalArgumentException("Invalid room ID for minigame: " + roomId
                                                 + ". Valid room IDs are 1-5.");
        }
    }

    /**
     * Gets the name of the minigame for a specific room without creating an instance.
     * 
     * @param roomId the ID of the room
     * @return the name of the minigame
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static String getMinigameName(final int roomId) {
        switch (roomId) {
            //TODO Scrivete con le vostre stanze, marrani
            case 2: return "Quiz Kahoot";
            // case 2: return "aMAZEing";
            default:
                throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    /**
     * Gets the description of the minigame for a specific room.
     * 
     * @param roomId the ID of the room
     * @return the description of the minigame
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static String getMinigameDescription(final int roomId) {
        switch (roomId) {
            //TODO Scrivete con le vostre stanze, marrani
            case 2: return "Answer all questions";
            // case 2: return "Solve 10 math problems!";
            // case 3: return "Click when it turns green!";
            // case 4: return "Find all matching pairs of cards!";
            default:
                throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    /**
     * Checks if a room has a minigame available.
     * 
     * @param roomId the ID of the room
     * @return true if the room has a minigame, false otherwise
     */
    public static boolean hasMinigame(final int roomId) {
        final int lastRoom = 5;
        return roomId >= 1 && roomId <= lastRoom;
    }
}
