package it.unibo.exam.model.Game;

import java.util.List;

import it.unibo.exam.model.Entity.Entity;
import it.unibo.exam.model.Entity.Player;
import it.unibo.exam.model.Entity.enviroments.Room;
import it.unibo.exam.utility.Generator.RoomGenerator;
import it.unibo.exam.utility.Geometry.Point2D;

/**
 * Represents the state of the game, including rooms, the player, and the current room.
 */
public class GameState {

    private final List<Room> rooms;
    private final Player player;
    private int currentRoomId;

    /**
     * Constructor for the GameState class.
     * Initializes the game state with a list of rooms and a player.
     *
     * @param enviromentSize the size of the environment
     */
    public GameState(final Point2D enviromentSize) {
        this.rooms = initRooms(enviromentSize);
        this.player = new Player(enviromentSize);
        this.currentRoomId = 0; // Main room ID
    }

    /**
     * Resizes the game elements to fit the new environment size.
     *
     * @param newSize the new size of the environment
     */
    public void resize(final Point2D newSize) {
        final List<Entity> entityList = List.of(getPlayer()); 
        final Room room = getCurrentRoom();
        entityList.addAll(room.getDoors());

        if (room.getRoomType() == RoomGenerator.PUZZLE_ROOM) { 
            entityList.add(room.getNpc());
        }

        entityList.forEach(e -> e.resize(newSize));
    }

    /**
     * Initializes the rooms for the game.
     *
     * @param enviromentSize the size of the environment
     * @return a list of rooms
     */
    private List<Room> initRooms(final Point2D enviromentSize) {
        return new RoomGenerator().generateRooms(enviromentSize);
    }

    /**
     * @return the current room
     */
    public Room getCurrentRoom() {
        return rooms.get(currentRoomId);
    }

    /**
     * Changes the current room to the specified room ID.
     *
     * @param newRoomId the ID of the new room
     */
    public void changeRoom(final int newRoomId) { // Made 'newRoomId' final
        // Check if the new room ID is valid
        if (newRoomId < 0 || newRoomId >= rooms.size()) {
            throw new IllegalArgumentException("Invalid room ID: " + newRoomId);
        }

        this.currentRoomId = newRoomId;
    }

    /**
     * @return the player instance
     */
    public Player getPlayer() {
        return player;
    }
}
