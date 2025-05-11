package it.unibo.exam.model.Entity.enviroments;

import java.util.List;

import it.unibo.exam.model.Entity.Npc;
import it.unibo.exam.model.Entity.Minigame.Minigame;

/**
 * A simple Room class rappresenting a room.
 */
public class Room {

    private final int id;
    private final Minigame minigame;
    private final int roomType;
    private final Npc npc;
    private final List<Door> doors;

    /**
     * Constructor for Room.
     *
     * @param id the id of the room
     * @param doors the doors of the room
     * @param minigame the minigame of the room
     * @param roomType the type of the room
     * @param npc the npc of the room
     */
    public Room(final int id, final List<Door> doors, final Minigame minigame, final int roomType, final Npc npc) {
        this.id = id;
        this.doors = doors;
        this.minigame = minigame;
        this.roomType = roomType;
        this.npc = npc;
    }

    /**
     * Alternative Contractor.
     * @param id the id of the room
     * @param doors the doors of the room
     * @param roomType the type of the room
     */
    public Room(final int id, final List<Door> doors, final int roomType) {
        this(id, doors, null, roomType, null);
    }

    /**
     * @return the id of the room
     */
    public int getId() {
        return id;
    }

    /**
     * @return the minigame of the room
     * @throws IllegalStateException if the room has no minigame
     */
    public Minigame getMinigame() {
        if (roomType == 1) {
            throw new IllegalStateException("This room has no minigame");
        }
        return minigame;
    }

    /**
     * @return the doors of the room
     */
    public List<Door> getDoors() {
        return doors;
    }

    /**
     * @return the type of the room
     */
    public int getRoomType() {
        return roomType;
    }

    /**
     * @return the npc of the room
     * @throws IllegalStateException if the room has no npc
     */
    public Npc getNpc() {
        if (roomType == 1) {
            throw new IllegalStateException("This room has no npc");
        }
        return npc;
    }

}
