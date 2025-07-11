package it.unibo.exam.model.entity.enviroments;

import java.util.ArrayList;
import java.util.Collections; // ADDED
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.RoamingNpc; // ADDED
import it.unibo.exam.utility.generator.RoomGenerator;

/**
 * A simple Room class representing a room.
 */
public class Room {
    private final int id;
    private String name;
    private Minigame minigame;
    private final int roomType;
    private Npc npc;
    private List<Door> doors;
    private final List<RoamingNpc> roamingNpcs = new ArrayList<>(); // ADDED

    /**
     * Constructor.
     * @param id the id of the room
     * @param doors the doors of the room
     * @param roomType the type of the room
     */
    public Room(final int id, final List<Door> doors, final int roomType) {
        this.id = id;
        this.doors = new ArrayList<>(doors);
        this.roomType = roomType;
        this.name = "Room " + id;
    }

    /**
     * Updates the doors in this room.
     * @param newDoors the new list of doors
     */
    public final void updateDoors(final List<Door> newDoors) {
        this.doors = new ArrayList<>(newDoors);
    }

    /**
     * @return the id of the room
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of this room.
     * @param name the new display name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the minigame of the room
     * @throws IllegalStateException if the room has no minigame
     */
    public Minigame getMinigame() {
        if (roomType == RoomGenerator.MAIN_ROOM) {
            throw new IllegalStateException("This room has no minigame");
        }
        return minigame;
    }

    /**
     * @return the doors of the room
     */
    public List<Door> getDoors() {
        return new ArrayList<>(doors);
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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", 
                       justification = "NPC reference is needed for game interaction, defensive copy would break game logic")
    public Npc getNpc() {
        if (roomType == RoomGenerator.MAIN_ROOM) {
            throw new IllegalStateException("Main room has no npc");
        }
        return npc;
    }

    /**
     * Attaches an NPC to this room.
     * @param npc the NPC to attach
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
                       justification = "NPC is designed to be shared and modified by room, defensive copy not needed")
    public void attachNpc(final Npc npc) {
        if (roomType == RoomGenerator.MAIN_ROOM) {
            throw new IllegalStateException("Main room has no npc");
        }
        this.npc = npc;
    }

    /**
     * Attaches a minigame to this room.
     * @param mg the minigame to attach
     */
    public void attacMinigame(final Minigame mg) {
        if (roomType == RoomGenerator.MAIN_ROOM) {
            throw new IllegalStateException("Main room has no minigame");
        }
        this.minigame = mg;
    }

    // ------------------- ADDED: ROAMING NPC SUPPORT -------------------

    /**
     * Adds a roaming NPC to this room.
     * @param npc the roaming NPC to add
     */
    public void addRoamingNpc(final RoamingNpc npc) {
        this.roamingNpcs.add(npc);
    }

    /**
     * @return unmodifiable list of roaming NPCs in this room
     */
    public List<RoamingNpc> getRoamingNpcs() {
        return Collections.unmodifiableList(roamingNpcs);
    }
}
