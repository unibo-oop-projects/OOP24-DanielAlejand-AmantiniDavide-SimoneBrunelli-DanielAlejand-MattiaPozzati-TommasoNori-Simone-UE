package it.unibo.exam;

import it.unibo.exam.utility.AudioManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for AudioManager utility.
 * Tests basic audio functionality including volume control and mute state.
 */
class AudioManagerTest {

    private static final float DEFAULT_MUSIC_VOLUME = 0.7f;
    private static final float DELTA = 0.01f;
    private static final float TEST_VOLUME = 0.5f;
    private static final float OVER_MAX_VOLUME = 1.5f;
    private static final float UNDER_MIN_VOLUME = -0.5f;

    @BeforeEach
    void setUp() {
        // Reset audio state before each test
        AudioManager.setMuted(false);
        AudioManager.setMusicVolume(DEFAULT_MUSIC_VOLUME);
    }

    @AfterEach
    void tearDown() {
        // Cleanup audio resources after each test
        AudioManager.cleanup();
    }

    @Test
    void testInitialAudioState() {
        // Test initial audio state
        assertFalse(AudioManager.isMuted());
        assertEquals(DEFAULT_MUSIC_VOLUME, AudioManager.getMusicVolume(), DELTA);
        assertFalse(AudioManager.isMusicPlaying());
        assertNull(AudioManager.getCurrentMusicPath());
    }

    @Test
    void testVolumeControl() {
        // Test music volume control
        AudioManager.setMusicVolume(TEST_VOLUME);
        assertEquals(TEST_VOLUME, AudioManager.getMusicVolume(), DELTA);

        // Test volume clamping
        AudioManager.setMusicVolume(OVER_MAX_VOLUME);
        assertEquals(1.0f, AudioManager.getMusicVolume(), DELTA);

        AudioManager.setMusicVolume(UNDER_MIN_VOLUME);
        assertEquals(0.0f, AudioManager.getMusicVolume(), DELTA);
    }

    @Test
    void testMuteControl() {
        // Test mute functionality
        AudioManager.setMuted(true);
        assertTrue(AudioManager.isMuted());

        AudioManager.setMuted(false);
        assertFalse(AudioManager.isMuted());
    }

    @Test
    void testInvalidAudioPaths() {
        // Test handling of invalid audio paths
        assertFalse(AudioManager.playBackgroundMusic(null));
        assertFalse(AudioManager.playBackgroundMusic(""));
        assertFalse(AudioManager.playBackgroundMusic("nonexistent_file.wav"));
    }

    @Test
    void testAudioFileExists() {
        // Test that the background music file exists
        final var resource = AudioManager.class.getClassLoader().getResource("audio/backgroundmusic.wav");
        assertNotNull(resource, "Background music file should exist");
    }



    @Test
    void testCleanup() {
        // Test cleanup functionality
        AudioManager.cleanup();
        assertFalse(AudioManager.isMusicPlaying());
        assertNull(AudioManager.getCurrentMusicPath());
    }
}
