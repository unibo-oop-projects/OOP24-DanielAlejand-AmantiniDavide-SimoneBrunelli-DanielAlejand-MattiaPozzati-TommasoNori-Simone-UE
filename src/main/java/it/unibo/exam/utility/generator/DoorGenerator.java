package it.unibo.exam.utility.generator;

import java.util.ArrayList;
import java.util.List;

import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Generates doors for each room in the game, including the special exit door
 * in the hub (room 0). Positions are calculated based on the environment size.
 */
public class DoorGenerator extends EntityGenerator<List<Door>> {

    private static final int HUB_ROOM_ID = 0;
    private static final int PUZZLE_ROOM_COUNT = 5;
    private static final int MIN_DOOR_DIMENSION = 40;
    private static final int DOOR_DIVIDER = 20;
    private static final int DOOR_MARGIN = 20;

    private List<Point2D> dir;
    private final Point2D environmentSize;

    /**
     * Constructs a DoorGenerator for the specified environment size.
     *
     * @param environmentSize the dimensions of the environment
     */
    public DoorGenerator(final Point2D environmentSize) {
        super(environmentSize);
        this.environmentSize = new Point2D(environmentSize);
        calculateDoorPositions(environmentSize);
    }

    /**
     * Calculates positions for the inter-room doors based on the current
     * environment size, storing them in {@code dir}.
     *
     * @param envSize the dimensions of the environment
     */
    private void calculateDoorPositions(final Point2D envSize) {
        final int doorWidth = Math.max(MIN_DOOR_DIMENSION, envSize.getX() / DOOR_DIVIDER);
        final int doorHeight = Math.max(MIN_DOOR_DIMENSION, envSize.getY() / DOOR_DIVIDER);
        final int margin = DOOR_MARGIN;

        dir = List.of(
            // To room 1 – right side
            new Point2D(
                Math.max(0, envSize.getX() - doorWidth - margin),
                Math.max(0, (envSize.getY() - doorHeight) / 2)
            ),
            // To room 2 – bottom side
            new Point2D(
                Math.max(0, envSize.getX() / 3 - doorWidth / 2),
                Math.max(0, envSize.getY() - doorHeight - margin)
            ),
            // To room 3 – left side
            new Point2D(
                margin,
                Math.max(0, (envSize.getY() - doorHeight) / 2)
            ),
            // To room 4 – top side
            new Point2D(
                Math.max(0, (envSize.getX() - doorWidth) / 2),
                margin
            ),
            // To room 5 – bottom side
            new Point2D(
                Math.max(0, 2 * envSize.getX() / 3 - doorWidth / 2),
                Math.max(0, envSize.getY() - doorHeight - margin)
            )
        );
    }

    /**
     * Updates door positions when the environment is resized.
     *
     * @param newSize the new dimensions of the environment
     */
    public void updateEnvironmentSize(final Point2D newSize) {
        this.environmentSize.setXY(newSize.getX(), newSize.getY());
        calculateDoorPositions(newSize);
    }

    /**
     * Generates all doors for the specified room ID.
     *
     * @param id the room ID (0 for hub, 1–5 for puzzle rooms)
     * @return the list of doors for that room
     * @throws IllegalArgumentException if {@code id} is not in [0,5]
     */
    @Override
    public final List<Door> generate(final int id) {
        switch (id) {
            case HUB_ROOM_ID: {
                final List<Door> doors = new ArrayList<>();
                for (int toId = 1; toId <= PUZZLE_ROOM_COUNT; toId++) {
                    doors.add(generateSingleDoor(HUB_ROOM_ID, toId));
                }
                doors.add(generateEndDoor());
                return doors;
            }
            case 1: case 2: case 3: case 4: case PUZZLE_ROOM_COUNT: {
                return List.of(generateSingleDoor(id, HUB_ROOM_ID));
            }
            default: {
                throw new IllegalArgumentException("Id must be in [0," + PUZZLE_ROOM_COUNT + "]");
            }
        }
    }

    /**
     * Generates a single door between two rooms, positioned according to
     * the precomputed {@code dir} list and clamped within bounds.
     *
     * @param fromId the source room ID
     * @param toId   the destination room ID
     * @return the created Door
     */
    private Door generateSingleDoor(final int fromId, final int toId) {
        final Point2D pos = getPosition(fromId, toId);
        final int doorWidth = Math.max(MIN_DOOR_DIMENSION, environmentSize.getX() / DOOR_DIVIDER);
        final int doorHeight = Math.max(MIN_DOOR_DIMENSION, environmentSize.getY() / DOOR_DIVIDER);

        final int validX = Math.max(0,
            Math.min(pos.getX(), environmentSize.getX() - doorWidth));
        final int validY = Math.max(0,
            Math.min(pos.getY(), environmentSize.getY() - doorHeight));

        final Point2D validPos = new Point2D(validX, validY);
        return new Door(super.getEnv(), validPos, fromId, toId);
    }

    /**
     * Computes the raw position for a door based on precomputed positions.
     *
     * @param fromId the source room ID
     * @param toId   the destination room ID
     * @return the computed raw position
     * @throws IllegalArgumentException if indices are out of range
     */
    private Point2D getPosition(final int fromId, final int toId) {
        if (fromId == HUB_ROOM_ID) {
            final int index = toId - 1;
            if (index < 0 || index >= dir.size()) {
                throw new IllegalArgumentException("Invalid toId: " + toId);
            }
            return new Point2D(dir.get(index));
        } else {
            final int index = fromId - 1;
            if (index < 0 || index >= dir.size()) {
                throw new IllegalArgumentException("Invalid fromId: " + fromId);
            }
            return new Point2D(dir.get(index));
        }
    }

    /**
     * Generates the special endgame exit door in the hub (room 0).
     *
     * @return the flagged endgame exit Door
     */
    private Door generateEndDoor() {
        final int doorWidth = Math.max(MIN_DOOR_DIMENSION, environmentSize.getX() / DOOR_DIVIDER);
        final int doorHeight = Math.max(MIN_DOOR_DIMENSION, environmentSize.getY() / DOOR_DIVIDER);
        final int margin = DOOR_MARGIN;

        final Point2D exitPos = new Point2D(
            environmentSize.getX() / 2 - doorWidth / 2,
            environmentSize.getY() - doorHeight - margin
        );
        final Door exitDoor = new Door(super.getEnv(), exitPos, HUB_ROOM_ID, HUB_ROOM_ID);
        exitDoor.setEndgameDoor(true);
        return exitDoor;
    }
}
