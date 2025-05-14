package it.unibo.exam.utility.Generator;

import it.unibo.exam.model.Entity.enviroments.Room;
import it.unibo.exam.utility.Geometry.Point2D;

/**
 * Room Generator.
 * @see Room
 */
public class RoomGenerator extends EntityGenerator<Room>{

    /**
     * RoomType = 2 = PuzzleRoom.
     */
    public static final int PUZZLE_ROOM = 2;
    public static final int MAIN_ROOM = 1;

    public RoomGenerator (final Point2D enviromentSize){
        super(enviromentSize);
    }

    /**
     * Generates a room with the specified ID.
     * 
     * @param id the ID of the room
     * @return the generated room
     */
    @Override
    public Room generate(int id) {
        return new Room(
            id, 
            new DoorGenerator(enviromentSize).generate(id), 
            id == 0 ? MAIN_ROOM : PUZZLE_ROOM
        );
    }
}
