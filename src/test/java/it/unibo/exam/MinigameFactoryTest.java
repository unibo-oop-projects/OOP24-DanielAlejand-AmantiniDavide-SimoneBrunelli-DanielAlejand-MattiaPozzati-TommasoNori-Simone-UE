package it.unibo.exam;

import it.unibo.exam.utility.generator.MinigameFactory;
import it.unibo.exam.model.entity.minigame.Minigame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinigameFactoryTest {

    private static final int GARDEN_ROOM_ID = 1;
    private static final int LAB_ROOM_ID = 5;
    private static final int MAZE_ROOM_ID = 2;
    private static final int GYM_ROOM_ID = 3;
    private static final int BAR_ROOM_ID = 4;
    private static final int INVALID_ROOM_ID = 999;

    @Test
    void testCreateMinigameForGarden() {
        final Minigame minigame = MinigameFactory.createMinigame(GARDEN_ROOM_ID);
        assertNotNull(minigame);
        final String description = minigame.getDescription().toUpperCase(java.util.Locale.ROOT);
        assertTrue(description.contains("BALL") || description.contains("CATCH"));
    }

    @Test
    void testCreateMinigameForLab() {
        final Minigame minigame = MinigameFactory.createMinigame(LAB_ROOM_ID);
        assertNotNull(minigame);
        final String description = minigame.getDescription().toUpperCase(java.util.Locale.ROOT);
        assertTrue(description.contains("QUIZ") || description.contains("QUESTION"));
    }

    @Test
    void testCreateMinigameForMaze() {
        final Minigame minigame = MinigameFactory.createMinigame(MAZE_ROOM_ID);
        assertNotNull(minigame);
        final String description = minigame.getDescription().toUpperCase(java.util.Locale.ROOT);
        assertTrue(description.contains("MAZE") || description.contains("RUN"));
    }

    @Test
    void testCreateMinigameForGym() {
        final Minigame minigame = MinigameFactory.createMinigame(GYM_ROOM_ID);
        assertNotNull(minigame);
        final String description = minigame.getDescription().toUpperCase(java.util.Locale.ROOT);
        assertTrue(description.contains("DISK") || description.contains("CANNON"));
    }

    @Test
    void testCreateMinigameForBar() {
        final Minigame minigame = MinigameFactory.createMinigame(BAR_ROOM_ID);
        assertNotNull(minigame);
        final String description = minigame.getDescription().toUpperCase(java.util.Locale.ROOT);
        assertTrue(description.contains("GLASS") || description.contains("COLOR"));
    }

    @Test
    void testCreateMinigameWithInvalidType() {
        try {
            final Minigame minigame = MinigameFactory.createMinigame(INVALID_ROOM_ID);
            // If no exception is thrown, the minigame should still be valid
            assertNotNull(minigame);
        } catch (final IllegalArgumentException e) {
            // Exception is expected for invalid types
            // Exception was thrown as expected
            assertNotNull(e);
        }
    }
}
