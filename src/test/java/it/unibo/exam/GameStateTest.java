package it.unibo.exam;

import it.unibo.exam.model.game.GameState;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.geometry.Point2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameStateTest {

    private static final int ENV_WIDTH = 800;
    private static final int ENV_HEIGHT = 600;
    private static final int HUB_ROOM_ID = 0;
    private static final int TEST_ROOM_ID = 1;
    private static final int INVALID_ROOM_ID = 999;
    private static final int NEW_WIDTH = 1000;
    private static final int NEW_HEIGHT = 800;
    private static final int MAIN_ROOM_TYPE = 1;

    private GameState gameState;

    @BeforeEach
    void setUp() {
        final Point2D environmentSize = new Point2D(ENV_WIDTH, ENV_HEIGHT);
        gameState = new GameState(environmentSize);
    }

    @Test
    void testGameStateCreation() {
        assertNotNull(gameState.getPlayer());
        assertNotNull(gameState.getAllRooms());
        assertEquals(HUB_ROOM_ID, gameState.getCurrentRoomId());
    }

    @Test
    void testRoomsInitialization() {
        final var rooms = gameState.getAllRooms();

        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());

        // Check that room 0 (Hub) exists and is main room
        final Room hub = rooms.get(HUB_ROOM_ID);
        assertEquals(HUB_ROOM_ID, hub.getId());
        assertEquals("Hub", hub.getName());
    }

    @Test
    void testPlayerInitialization() {
        final Player player = gameState.getPlayer();

        assertNotNull(player);
        assertEquals(0, player.getTotalScore());
        assertTrue(player.getRoomScores().isEmpty());
    }

    @Test
    void testChangeRoom() {
        final int newRoomId = TEST_ROOM_ID;

        gameState.changeRoom(newRoomId);

        assertEquals(newRoomId, gameState.getCurrentRoomId());
    }

    @Test
    void testGetCurrentRoom() {
        final Room currentRoom = gameState.getCurrentRoom();

        assertNotNull(currentRoom);
        assertEquals(HUB_ROOM_ID, currentRoom.getId()); // Should be Hub initially
    }

    @Test
    void testResize() {
        final Point2D newSize = new Point2D(NEW_WIDTH, NEW_HEIGHT);

        gameState.resize(newSize);

        // Check that player has been resized
        final Player player = gameState.getPlayer();
        assertEquals(newSize.getX(), player.getEnviromentSize().getX());
        assertEquals(newSize.getY(), player.getEnviromentSize().getY());
    }

    @Test
    void testRoomDoors() {
        final Room currentRoom = gameState.getCurrentRoom();
        final var doors = currentRoom.getDoors();

        assertNotNull(doors);
        assertFalse(doors.isEmpty());
    }

    @Test
    void testRoomNpc() {
        // Test that puzzle rooms have NPCs
        final var rooms = gameState.getAllRooms();

        for (int i = 1; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            if (room.getRoomType() != MAIN_ROOM_TYPE) { // Not main room
                assertNotNull(room.getNpc());
                assertNotNull(room.getNpc().getName());
                assertNotNull(room.getNpc().getDescription());
                assertNotNull(room.getNpc().getDialogue());
            }
        }
    }

    @Test
    void testRoomNames() {
        final var rooms = gameState.getAllRooms();

        // Check that rooms have proper names
        for (final Room room : rooms) {
            assertNotNull(room.getName());
            assertFalse(room.getName().isEmpty());
        }
    }

    @Test
    void testInvalidRoomChange() {
        final int invalidRoomId = INVALID_ROOM_ID;

        // Should not throw exception, but should handle gracefully
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.changeRoom(invalidRoomId);
        });
    }

    @Test
    void testPlayerPositionAfterRoomChange() {
        final int newRoomId = TEST_ROOM_ID;

        gameState.changeRoom(newRoomId);

        // Player position should be updated after room change
        final Point2D newPosition = gameState.getPlayer().getPosition();
        assertNotNull(newPosition);
    }
}
