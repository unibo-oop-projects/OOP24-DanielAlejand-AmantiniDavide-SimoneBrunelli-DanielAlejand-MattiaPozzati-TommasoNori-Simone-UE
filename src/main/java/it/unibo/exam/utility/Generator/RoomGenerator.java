package it.unibo.exam.utility.Generator;

import java.util.ArrayList;
import java.util.List;

import it.unibo.exam.model.Entity.enviroments.Room;
import it.unibo.exam.utility.Geometry.Point2D;

/**
 * Room Generator.
 * @see Room
 */
public class RoomGenerator {

    /**
     * RoomType = 2 = PuzzleRoom.
     */
    public static final int PUZZLE_ROOM = 2;

    private static final int SCALE_FACTOR = 20;

    /**
     * Generates a list of rooms for the game.
     * @param enviromentSize 
     * @return a list of rooms
     */
    public List<Room> generateRooms(final Point2D enviromentSize) {
        final List<Room> rooms = new ArrayList<>();
        // Add logic to generate rooms here
        // TODO:
        return rooms;
    }

    /**
     * Generates a room with the specified ID.
     * 
     * @param id the ID of the room
     * @return the generated room
     */
    public Room generateRoom(final int id) {
        // Add logic to generate a room with the specified ID here
        // TODO:
        return new Room(id, null, id);
    }
}
