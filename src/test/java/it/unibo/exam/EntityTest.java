package it.unibo.exam;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.utility.geometry.Point2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EntityTest {

    private static final int ENV_WIDTH = 800;
    private static final int ENV_HEIGHT = 600;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int MOVE_DISTANCE = 20;
    private static final int LARGE_MOVE = 30;
    private static final int BOUNDARY_X = 45;
    private static final int BOUNDARY_Y = 150;
    private static final int SPEED = 5;
    private static final int DELTA = 30;

    private Point2D environmentSize;
    private Entity testEntity;

    @BeforeEach
    void setUp() {
        environmentSize = new Point2D(ENV_WIDTH, ENV_HEIGHT);
        testEntity = new Player(environmentSize);
    }

    @Test
    void testEntityCreation() {
        assertNotNull(testEntity);
        assertNotNull(testEntity.getPosition());
        final Point2D entityEnvSize = testEntity.getEnviromentSize();
        assertEquals(environmentSize.getX(), entityEnvSize.getX());
        assertEquals(environmentSize.getY(), entityEnvSize.getY());
    }

    @Test
    void testEntityPosition() {
        final Point2D position = testEntity.getPosition();
        assertNotNull(position);
        assertTrue(position.getX() >= 0);
        assertTrue(position.getY() >= 0);
    }

    @Test
    void testEntityEnvironmentSize() {
        final Point2D size = testEntity.getEnviromentSize();
        assertEquals(environmentSize.getX(), size.getX());
        assertEquals(environmentSize.getY(), size.getY());
    }

    @Test
    void testEntityMove() {
        final Point2D originalPosition = new Point2D(testEntity.getPosition());
        testEntity.getPosition().move(MOVE_DISTANCE, MOVE_DISTANCE);
        testEntity.updateHitboxPosition();
        final Point2D newPosition = testEntity.getPosition();
        assertEquals(originalPosition.getX() + MOVE_DISTANCE, newPosition.getX());
        assertEquals(originalPosition.getY() + MOVE_DISTANCE, newPosition.getY());
    }

    @Test
    void testEntityMoveNegative() {
        final Point2D originalPosition = new Point2D(testEntity.getPosition());
        testEntity.getPosition().move(-MOVE_DISTANCE, -MOVE_DISTANCE);
        testEntity.updateHitboxPosition();
        final Point2D newPosition = testEntity.getPosition();
        assertEquals(originalPosition.getX() - MOVE_DISTANCE, newPosition.getX());
        assertEquals(originalPosition.getY() - MOVE_DISTANCE, newPosition.getY());
    }

    @Test
    void testEntityMoveLargeDistance() {
        final Point2D originalPosition = new Point2D(testEntity.getPosition());
        testEntity.getPosition().move(LARGE_MOVE, LARGE_MOVE);
        testEntity.updateHitboxPosition();
        final Point2D newPosition = testEntity.getPosition();
        assertEquals(originalPosition.getX() + LARGE_MOVE, newPosition.getX());
        assertEquals(originalPosition.getY() + LARGE_MOVE, newPosition.getY());
    }

    @Test
    void testEntitySetPosition() {
        final Point2D newPosition = new Point2D(TEST_X, TEST_Y);
        testEntity.getPosition().setXY(TEST_X, TEST_Y);
        testEntity.updateHitboxPosition();
        assertEquals(newPosition.getX(), testEntity.getPosition().getX());
        assertEquals(newPosition.getY(), testEntity.getPosition().getY());
    }

    @Test
    void testEntityResize() {
        final Point2D newSize = new Point2D(1000, 800);
        testEntity.resize(newSize);
        final Point2D entitySize = testEntity.getEnviromentSize();
        assertEquals(newSize.getX(), entitySize.getX());
        assertEquals(newSize.getY(), entitySize.getY());
    }

    @Test
    void testPlayerSpecificBehavior() {
        final Player player = new Player(environmentSize);
        assertEquals(0, player.getTotalScore());
        assertTrue(player.getRoomScores().isEmpty());
    }

    @Test
    void testNpcSpecificBehavior() {
        final Npc npc = new Npc(environmentSize, "TestNPC", "Test Description", "Test Dialogue");
        assertEquals("TestNPC", npc.getName());
        assertEquals("Test Description", npc.getDescription());
        assertEquals("Test Dialogue", npc.getDialogue());
    }

    @Test
    void testEntityBoundaryBehavior() {
        testEntity.getPosition().setXY(BOUNDARY_X, BOUNDARY_Y);
        testEntity.updateHitboxPosition();
        testEntity.getPosition().move(SPEED, SPEED);
        testEntity.updateHitboxPosition();
        final Point2D newPosition = testEntity.getPosition();
        assertEquals(BOUNDARY_X + SPEED, newPosition.getX());
        assertEquals(BOUNDARY_Y + SPEED, newPosition.getY());
    }

    @Test
    void testEntityLargeMovement() {
        final Point2D originalPosition = new Point2D(testEntity.getPosition());
        testEntity.getPosition().move(DELTA, DELTA);
        testEntity.updateHitboxPosition();
        final Point2D newPosition = testEntity.getPosition();
        assertEquals(originalPosition.getX() + DELTA, newPosition.getX());
        assertEquals(originalPosition.getY() + DELTA, newPosition.getY());
    }
}
