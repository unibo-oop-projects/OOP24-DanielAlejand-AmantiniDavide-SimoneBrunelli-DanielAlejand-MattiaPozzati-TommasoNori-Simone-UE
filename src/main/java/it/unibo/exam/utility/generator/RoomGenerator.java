package it.unibo.exam.utility.generator;

import java.util.List;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Room Generator.
 * @see Room
 */
public class RoomGenerator extends EntityGenerator<Room> {

    /**
     * RoomType:
     *  2 = PuzzleRoom.
     */
    public static final int PUZZLE_ROOM = 2;

    /**
     * RoomType:
     *  1 = MainRoom.
     */
    public static final int MAIN_ROOM = 1;

    private DoorGenerator dg;

    /**
     * @param enviromentSize
     */
    public RoomGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
        this.dg = new DoorGenerator(enviromentSize);
    }

    /**
     * Updates the door generator when environment is resized.
     * @param newSize the new environment size
     */
    public void updateEnvironmentSize(final Point2D newSize) {
        this.dg.updateEnvironmentSize(newSize);
    }

    /**
     * Regenerates doors for a specific room with new environment size.
     * @param roomId the room ID
     * @param room the room to update
     */
    public void updateRoomDoors(final int roomId, final Room room) {
        // Usa raw type per compatibilità con Java vecchie
        @SuppressWarnings("unchecked")
        final List newDoors = dg.generate(roomId);
        room.updateDoors(newDoors);
    }

    /**
     * Generates a room with the specified ID.
     * 
     * @param id the ID of the room
     * @return the generated room
     */
    @Override
    public Room generate(final int id) {
        return new Room(
            id, 
            dg.generate(id), 
            id == 0 ? MAIN_ROOM : PUZZLE_ROOM
        );
    }
}
