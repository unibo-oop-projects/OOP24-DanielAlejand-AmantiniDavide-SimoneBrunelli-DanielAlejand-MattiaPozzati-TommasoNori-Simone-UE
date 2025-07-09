package it.unibo.exam.utility.factory;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.controller.minigame.bar.BarMinigame;
import it.unibo.exam.controller.minigame.gym.GymMinigame;
/**
 * Factory class for creating different types of minigames based on room ID.
 * Each room has its own specific minigame type.
 */
public final class MinigameFactory {

    /** Room ID for the Gym minigame. */
    public static final int ROOM_GYM = 4;

    /** Room ID for the Sort & Serve Bar minigame. */
    public static final int ROOM_BAR = 5;

    /** First room ID with a minigame (inclusive). */
    public static final int FIRST_ROOM = 1;

    /** Last room ID with a minigame (inclusive). */
    public static final int LAST_ROOM = ROOM_BAR;


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
            case ROOM_GYM:
                return new GymMinigame();
            case ROOM_BAR:
                return new BarMinigame();
            default:
                throw new IllegalArgumentException(
                    "Invalid room ID for minigame: " + roomId
                  + ". Valid room IDs are " + ROOM_GYM + "–" + ROOM_BAR + "."
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
            case ROOM_GYM:
                return "Catch the Ball";
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
            case ROOM_GYM:
                return "Hit all disks with the cannon to win! Use mouse and keyboard.";
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
