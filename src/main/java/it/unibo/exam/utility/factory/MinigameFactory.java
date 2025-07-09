package it.unibo.exam.utility.factory;

import it.unibo.exam.model.entity.minigame.KahootMinigame;
import it.unibo.exam.model.entity.minigame.MazeMiniGame;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.controller.minigame.bar.BarMinigame;

/**
 * Factory class for creating different types of minigames based on room ID.
 * Each room has its own specific minigame type.
 */
public final class MinigameFactory {

    /** Room ID for the Kahoot minigame. */
    public static final int ROOM_KAHOOT = 2;

    /** Room ID for the Maze Runner minigame. */
    public static final int ROOM_MAZE = 3;

    /** Room ID for the Sort & Serve Bar minigame. */
    public static final int ROOM_BAR = 5;

    /** First room ID with a minigame (inclusive). */
    public static final int FIRST_ROOM = 1;

    /** Last room ID with a minigame (inclusive). */
    public static final int LAST_ROOM = 5;


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
     * - Room 2: Quiz Kahoot
     * - Room 3: Maze Runner
     * - Room 4: Sort & Serve (Bar puzzle)
     *
     * @param roomId the ID of the room (2–4)
     * @return the corresponding minigame instance
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static Minigame createMinigame(final int roomId) {
        switch (roomId) {
            case ROOM_KAHOOT:
                return new KahootMinigame();
            case ROOM_MAZE:
                return new MazeMiniGame();
            case ROOM_BAR:
                return new BarMinigame();
            default:
                throw new IllegalArgumentException(
                    "Invalid room ID for minigame: " + roomId
                  + ". Valid room IDs are " + ROOM_KAHOOT + "–" + ROOM_BAR + "."
                );
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
            case ROOM_KAHOOT:
                return "Quiz Kahoot";
            case ROOM_MAZE:
                return "Maze Runner";
            case ROOM_BAR:
                return "Sort & Serve";
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
            case ROOM_KAHOOT:
                return "Answer all questions";
            case ROOM_MAZE:
                return "Go fast, go furious, or you'll lose";
            case ROOM_BAR:
                return "Pour colored layers until each glass is uniform.";
            default:
                throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    /**
     * Checks if a room has a minigame available.
     *
     * @param roomId the ID of the room
     * @return {@code true} if the room has a minigame, {@code false} otherwise
     */
    public static boolean hasMinigame(final int roomId) {
        return roomId >= FIRST_ROOM && roomId <= LAST_ROOM;
    }
}
