package it.unibo.exam.utility.Generator;

import java.util.ArrayList;
import java.util.List;

import it.unibo.exam.model.Entity.enviroments.Room;
import it.unibo.exam.utility.Geometry.Point2D;

public class RoomGenerator {

    private static final int SCALE_FACTOR = 20;
    public static final int PUZZLE_ROOM = 2;

    /**
     * Generates a list of rooms for the game.
     * 
     * @return a list of rooms
     */
    public List<Room> generateRooms(final Point2D enviromentSize) {
        List<Room> rooms = new ArrayList<>();
        // Add logic to generate rooms here
        return rooms;
    }

    /**
     * Generates a room with the specified ID.
     * 
     * @param id the ID of the room
     * @return the generated room
     */
    public Room generateRoom(int id) {
        // Add logic to generate a room with the specified ID here
        return new Room(id, null, id);
    }
    
}
