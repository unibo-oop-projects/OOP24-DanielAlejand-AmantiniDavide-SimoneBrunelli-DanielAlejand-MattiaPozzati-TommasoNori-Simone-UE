package it.unibo.exam;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.geometry.Rectangle;
import it.unibo.exam.utility.generator.NpcGenerator;
import it.unibo.exam.utility.generator.RoomGenerator;
import it.unibo.exam.utility.generator.DoorGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UtilityTest {

    private static final double DELTA = 0.001;
    private static final int TEST_X = 200;
    private static final int TEST_Y = 300;
    private static final int ENV_WIDTH = 800;
    private static final int ENV_HEIGHT = 600;
    private static final int RECT_WIDTH = 100;
    private static final int RECT_HEIGHT = 50;
    private static final double EXPECTED_DISTANCE_3_4_5 = 5.0;
    private static final double EXPECTED_DISTANCE_300_400 = 500.0;
    private static final int EDGE_CASE_X = 149;
    private static final int EDGE_CASE_Y = 229;
    private static final int EDGE_CASE_DELTA = 2;
    private static final int EXPECTED_EDGE_X = 151;
    private static final int EXPECTED_EDGE_Y = 231;
    private static final int NEGATIVE_X = -10;
    private static final int NEGATIVE_Y = -20;
    private static final int LARGE_X = 10_000;
    private static final int LARGE_Y = 20_000;
    private static final int LARGE_DELTA = 1;
    private static final int EXPECTED_LARGE_X = 10_001;
    private static final int EXPECTED_LARGE_Y = 20_001;
    private static final int RECT_LARGE_X = 101;
    private static final int RECT_LARGE_Y = 201;
    private static final int RECT_LARGE_SIZE = 500;
    private static final int RECT_CENTER_X = 350;
    private static final int RECT_CENTER_Y = 450;

    @Test
    void testPoint2DCreation() {
        final Point2D point = new Point2D(TEST_X, TEST_Y);
        assertEquals(TEST_X, point.getX());
        assertEquals(TEST_Y, point.getY());
    }

    @Test
    void testPoint2DCopyConstructor() {
        final Point2D original = new Point2D(TEST_X, TEST_Y);
        final Point2D copy = new Point2D(original);
        assertEquals(original.getX(), copy.getX());
        assertEquals(original.getY(), copy.getY());
    }

    @Test
    void testPoint2DSetXY() {
        final Point2D point = new Point2D(0, 0);
        point.setXY(TEST_X, TEST_Y);
        assertEquals(TEST_X, point.getX());
        assertEquals(TEST_Y, point.getY());
    }

    @Test
    void testPoint2DMove() {
        final Point2D point = new Point2D(TEST_X, TEST_Y);
        final int deltaX = 20;
        final int deltaY = 30;
        point.move(deltaX, deltaY);
        assertEquals(TEST_X + deltaX, point.getX());
        assertEquals(TEST_Y + deltaY, point.getY());
    }

    @Test
    void testPoint2DDistance() {
        final Point2D point1 = new Point2D(0, 0);
        final Point2D point2 = new Point2D(300, 400);
        final double distance = point1.distance(point2);
        assertEquals(EXPECTED_DISTANCE_300_400, distance, DELTA);
    }

    @Test
    void testPoint2DEdgeCases() {
        final Point2D point = new Point2D(EDGE_CASE_X, EDGE_CASE_Y);
        point.move(EDGE_CASE_DELTA, EDGE_CASE_DELTA);
        assertEquals(EXPECTED_EDGE_X, point.getX());
        assertEquals(EXPECTED_EDGE_Y, point.getY());
    }

    @Test
    void testRectangleCreation() {
        final Point2D position = new Point2D(TEST_X, TEST_Y);
        final Point2D size = new Point2D(RECT_WIDTH, RECT_HEIGHT);
        final Rectangle rect = new Rectangle(position, size);
        final Point2D testPoint = new Point2D(TEST_X + 50, TEST_Y + 25);
        assertTrue(rect.contains(testPoint));
    }

    @Test
    void testRectangleContains() {
        final Point2D position = new Point2D(TEST_X, TEST_Y);
        final Point2D size = new Point2D(RECT_WIDTH, RECT_HEIGHT);
        final Rectangle rect = new Rectangle(position, size);
        final Point2D insidePoint = new Point2D(TEST_X + 50, TEST_Y + 25);
        final Point2D outsidePoint = new Point2D(TEST_X + 150, TEST_Y + 100);
        assertTrue(rect.contains(insidePoint));
        assertFalse(rect.contains(outsidePoint));
    }

    @Test
    void testRectangleEdgeCases() {
        final Point2D position = new Point2D(TEST_X, TEST_Y);
        final Point2D size = new Point2D(RECT_WIDTH, RECT_HEIGHT);
        final Rectangle rect = new Rectangle(position, size);
        final Point2D edgePoint = new Point2D(TEST_X, TEST_Y);
        final Point2D cornerPoint = new Point2D(TEST_X + RECT_WIDTH, TEST_Y + RECT_HEIGHT);
        assertTrue(rect.contains(edgePoint));
        assertTrue(rect.contains(cornerPoint));
    }

    @Test
    void testDistanceCalculation() {
        final Point2D point1 = new Point2D(0, 0);
        final Point2D point2 = new Point2D(3, 4);
        final double distance = point1.distance(point2);
        assertEquals(EXPECTED_DISTANCE_3_4_5, distance, DELTA);
    }

    @Test
    void testNpcGenerator() {
        final Point2D envSize = new Point2D(ENV_WIDTH, ENV_HEIGHT);
        final NpcGenerator npcGenerator = new NpcGenerator(envSize);
        final var npc = npcGenerator.generate(0);
        assertNotNull(npc);
        assertNotNull(npc.getName());
        assertNotNull(npc.getDescription());
        assertNotNull(npc.getDialogue());
    }

    @Test
    void testDoorGenerator() {
        final Point2D envSize = new Point2D(ENV_WIDTH, ENV_HEIGHT);
        final DoorGenerator doorGenerator = new DoorGenerator(envSize);
        final var doors = doorGenerator.generate(0);
        assertNotNull(doors);
        assertFalse(doors.isEmpty());
    }

    @Test
    void testRoomGenerator() {
        final Point2D envSize = new Point2D(ENV_WIDTH, ENV_HEIGHT);
        final RoomGenerator roomGenerator = new RoomGenerator(envSize);
        final var room = roomGenerator.generate(0);
        assertNotNull(room);
        assertNotNull(room.getName());
        assertTrue(room.getId() >= 0);
    }

    @Test
    void testPoint2DNegativeValues() {
        final Point2D point = new Point2D(NEGATIVE_X, NEGATIVE_Y);
        assertEquals(NEGATIVE_X, point.getX());
        assertEquals(NEGATIVE_Y, point.getY());
    }

    @Test
    void testPoint2DLargeValues() {
        final Point2D point = new Point2D(LARGE_X, LARGE_Y);
        point.move(LARGE_DELTA, LARGE_DELTA);
        assertEquals(EXPECTED_LARGE_X, point.getX());
        assertEquals(EXPECTED_LARGE_Y, point.getY());
    }

    @Test
    void testRectangleLargeDimensions() {
        final Point2D position = new Point2D(RECT_LARGE_X, RECT_LARGE_Y);
        final Point2D size = new Point2D(RECT_LARGE_SIZE, RECT_LARGE_SIZE);
        final Rectangle rect = new Rectangle(position, size);
        final Point2D centerPoint = new Point2D(RECT_CENTER_X, RECT_CENTER_Y);
        assertTrue(rect.contains(centerPoint));
    }
}
