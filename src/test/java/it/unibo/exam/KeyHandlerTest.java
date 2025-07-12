package it.unibo.exam;

import it.unibo.exam.controller.input.KeyHandler;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyHandlerTest {

    private KeyHandler keyHandler;
    private JFrame testFrame;

    @BeforeEach
    void setUp() {
        keyHandler = new KeyHandler();
        testFrame = new JFrame("Test Frame");
    }

    @Test
    void testInitialState() {
        assertFalse(keyHandler.isUpPressed());
        assertFalse(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
        assertFalse(keyHandler.isRightPressed());
        assertFalse(keyHandler.isSpaceBarPressed());
        assertFalse(keyHandler.isInteractPressed());
    }

    @Test
    void testWKeyPress() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(pressEvent);
        assertTrue(keyHandler.isUpPressed());
        assertFalse(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
        assertFalse(keyHandler.isRightPressed());
    }

    @Test
    void testSKeyPress() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        keyHandler.keyPressed(pressEvent);
        assertFalse(keyHandler.isUpPressed());
        assertTrue(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
        assertFalse(keyHandler.isRightPressed());
    }

    @Test
    void testAKeyPress() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        keyHandler.keyPressed(pressEvent);
        assertFalse(keyHandler.isUpPressed());
        assertFalse(keyHandler.isDownPressed());
        assertTrue(keyHandler.isLeftPressed());
        assertFalse(keyHandler.isRightPressed());
    }

    @Test
    void testDKeyPress() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        keyHandler.keyPressed(pressEvent);
        assertFalse(keyHandler.isUpPressed());
        assertFalse(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
        assertTrue(keyHandler.isRightPressed());
    }

    @Test
    void testArrowKeyPress() {
        final KeyEvent upArrow = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        keyHandler.keyPressed(upArrow);
        assertTrue(keyHandler.isUpPressed());
        final KeyEvent downArrow = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, '↓');
        keyHandler.keyPressed(downArrow);
        assertTrue(keyHandler.isDownPressed());
        final KeyEvent leftArrow = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, '←');
        keyHandler.keyPressed(leftArrow);
        assertTrue(keyHandler.isLeftPressed());

        final KeyEvent rightArrow = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, '→');
        keyHandler.keyPressed(rightArrow);
        assertTrue(keyHandler.isRightPressed());
    }

    @Test
    void testSpaceKeyPress() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        keyHandler.keyPressed(pressEvent);
        assertTrue(keyHandler.isSpaceBarPressed());
    }

    @Test
    void testEKeyPress() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_E, 'E');
        keyHandler.keyPressed(pressEvent);
        assertTrue(keyHandler.isInteractPressed());
    }

    @Test
    void testKeyRelease() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        final KeyEvent releaseEvent = new KeyEvent(testFrame, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(pressEvent);
        assertTrue(keyHandler.isUpPressed());
        keyHandler.keyReleased(releaseEvent);
        assertFalse(keyHandler.isUpPressed());
    }

    @Test
    void testMultipleKeysPressed() {
        final KeyEvent upKey = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        final KeyEvent rightKey = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        keyHandler.keyPressed(upKey);
        keyHandler.keyPressed(rightKey);
        assertTrue(keyHandler.isUpPressed());
        assertTrue(keyHandler.isRightPressed());
        assertFalse(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
    }

    @Test
    void testKeyReleaseMultipleKeys() {
        final KeyEvent upKey = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        final KeyEvent rightKey = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        final KeyEvent upRelease = new KeyEvent(testFrame, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        final KeyEvent rightRelease = new KeyEvent(testFrame, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        keyHandler.keyPressed(upKey);
        keyHandler.keyPressed(rightKey);
        keyHandler.keyReleased(upRelease);
        assertFalse(keyHandler.isUpPressed());
        assertTrue(keyHandler.isRightPressed());
        keyHandler.keyReleased(rightRelease);
        assertFalse(keyHandler.isUpPressed());
        assertFalse(keyHandler.isRightPressed());
    }

    @Test
    void testSpaceKeyRelease() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        final KeyEvent releaseEvent = new KeyEvent(testFrame, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        keyHandler.keyPressed(pressEvent);
        assertTrue(keyHandler.isSpaceBarPressed());
        keyHandler.keyReleased(releaseEvent);
        assertFalse(keyHandler.isSpaceBarPressed());
    }

    @Test
    void testEKeyRelease() {
        final KeyEvent pressEvent = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_E, 'E');
        final KeyEvent releaseEvent = new KeyEvent(testFrame, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_E, 'E');
        keyHandler.keyPressed(pressEvent);
        assertTrue(keyHandler.isInteractPressed());
        keyHandler.keyReleased(releaseEvent);
        assertFalse(keyHandler.isInteractPressed());
    }

    @Test
    void testUnknownKeyPress() {
        final KeyEvent unknownKey = new KeyEvent(testFrame, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_F, 'F');
        keyHandler.keyPressed(unknownKey);
        assertFalse(keyHandler.isUpPressed());
        assertFalse(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
        assertFalse(keyHandler.isRightPressed());
        assertFalse(keyHandler.isSpaceBarPressed());
        assertFalse(keyHandler.isInteractPressed());
    }

    @Test
    void testUnknownKeyRelease() {
        final KeyEvent unknownKey = new KeyEvent(testFrame, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_F, 'F');
        keyHandler.keyReleased(unknownKey);
        // Should not cause any issues
        assertFalse(keyHandler.isUpPressed());
        assertFalse(keyHandler.isDownPressed());
        assertFalse(keyHandler.isLeftPressed());
        assertFalse(keyHandler.isRightPressed());
        assertFalse(keyHandler.isSpaceBarPressed());
        assertFalse(keyHandler.isInteractPressed());
    }
}
