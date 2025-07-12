package it.unibo.exam.utility.generator;


import it.unibo.exam.controller.minigame.lab.MazeMinigame;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.controller.minigame.bar.BarMinigame;
import it.unibo.exam.controller.minigame.garden.CatchBallMinigame;
import it.unibo.exam.controller.minigame.kahoot.KahootMinigame;
import it.unibo.exam.controller.minigame.gym.GymMinigame;

/**
 * Factory class for creating different types of minigames based on room ID.
 * Each room has its own specific minigame type.
 */
public final class MinigameFactory {

    /** Room ID for the Maze Runner minigame. */
    public static final int ROOM_MAZE = 2;

    /** Room ID for the Garden minigame. */
    public static final int ROOM_GARDEN = 1;

    /** Room ID for the Garden minigame. */
    public static final int ROOM_LAB = 5;

    /** Room ID for the Gym minigame. */
    public static final int ROOM_GYM = 3;

    /** Room ID for the Bar minigame. */
    public static final int ROOM_BAR = 4;

    /** First room ID with a minigame (inclusive). */
    public static final int FIRST_ROOM = ROOM_GARDEN;

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
     * - Room 1: Catch the Ball (Garden)
     * - Room 2: Kahoot Quiz (Lab)
     * - Room 3: Maze Runner
     * - Room 4: Bubble Shooter (Gym)
     * - Room 5: Sort & Serve (Bar)
     *
     * @param roomId the ID of the room (1–5)
     * @return the corresponding minigame instance
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static Minigame createMinigame(final int roomId) {
        switch (roomId) {
            case ROOM_MAZE:
                return new MazeMinigame();
            case ROOM_GARDEN:
                return new CatchBallMinigame();
            case ROOM_LAB:
                return new KahootMinigame();
            case ROOM_GYM:
                return new GymMinigame();
            case ROOM_BAR:
                return new BarMinigame();
            default:
                throw new IllegalArgumentException(
                    "Invalid room ID for minigame: " + roomId
                    + ". Valid room IDs are " + FIRST_ROOM + "–" + LAST_ROOM + "."
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
            case ROOM_MAZE:
                return "Maze Runner";
            case ROOM_GARDEN:
                return "Catch the Ball";
            case ROOM_LAB:
                return "Kahoot";
            case ROOM_GYM:
                return "Bubble shooter";
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
            case ROOM_MAZE:
                return "Run fast, run furious! Use WASD and go to the red square";
            case ROOM_GARDEN:
                return "Catch the balls as they fall from the sky.";
            case ROOM_LAB:
                return "Answer quiz questions correctly";
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
