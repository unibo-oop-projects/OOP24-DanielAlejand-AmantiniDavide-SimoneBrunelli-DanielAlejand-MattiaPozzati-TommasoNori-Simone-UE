package it.unibo.exam.utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing game background music.
 * Provides centralized audio management with volume control and looping capabilities.
 */
public final class AudioManager {

    private static final Logger LOGGER = Logger.getLogger(AudioManager.class.getName());
    private static final String LOG_SEPARATOR = " - ";
    // Audio settings
    private static final float DEFAULT_MUSIC_VOLUME = 0.7f;
    private static final int MAX_VOLUME = 100;

    // Current audio clips
    private static Clip backgroundMusicClip;
    private static String currentMusicPath;
    private static float musicVolume = DEFAULT_MUSIC_VOLUME;
    private static boolean isMuted;

    /**
     * Private constructor to prevent instantiation.
     */
    private AudioManager() {
        // Utility class should not be instantiated
    }

    /**
     * Loads and plays background music with looping.
     * 
     * @param musicPath the path to the music file (relative to resources directory)
     * @return true if music was loaded and started successfully, false otherwise
     */
    public static boolean playBackgroundMusic(final String musicPath) {
        if (musicPath == null || musicPath.isBlank()) {
            LOGGER.warning("Music path is null or empty");
            return false;
        }

        // Stop current music if playing
        stopBackgroundMusic();

        try {
            final var resource = AudioManager.class.getClassLoader().getResource(musicPath);
            if (resource == null) {
                LOGGER.warning("Music resource not found: " + musicPath);
                return false;
            }

            LOGGER.info("Loading background music from: " + resource);
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resource);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioInputStream);

            // Set volume
            setMusicVolume(musicVolume);

            // Start playing with loop
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentMusicPath = musicPath;
            LOGGER.info("Background music started: " + musicPath);
            return true;

        } catch (final UnsupportedAudioFileException e) {
            LOGGER.log(Level.WARNING, "Unsupported audio format: " + musicPath + LOG_SEPARATOR + e.getMessage(), e);
            return false;
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load music file: " + musicPath + LOG_SEPARATOR + e.getMessage(), e);
            return false;
        } catch (final LineUnavailableException e) {
            LOGGER.log(Level.WARNING, "Audio line unavailable: " + musicPath + LOG_SEPARATOR + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Stops the currently playing background music.
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
            LOGGER.info("Background music stopped: " + currentMusicPath);
        }
        backgroundMusicClip = null;
        currentMusicPath = null;
    }

    /**
     * Pauses the currently playing background music.
     */
    public static void pauseBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            LOGGER.info("Background music paused: " + currentMusicPath);
        }
    }

    /**
     * Resumes the paused background music.
     */
    public static void resumeBackgroundMusic() {
        if (backgroundMusicClip != null && !backgroundMusicClip.isRunning() && currentMusicPath != null) {
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            LOGGER.info("Background music resumed: " + currentMusicPath);
        }
    }

    /**
     * Sets the background music volume.
     * 
     * @param volume volume level (0.0f to 1.0f)
     */
    public static void setMusicVolume(final float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusicClip != null) {
            setClipVolume(backgroundMusicClip, isMuted ? 0.0f : musicVolume);
        }
        LOGGER.info("Music volume set to: " + (musicVolume * MAX_VOLUME) + "%");
    }

    /**
     * Mutes or unmutes all audio.
     * 
     * @param muted true to mute, false to unmute
     */
    public static void setMuted(final boolean muted) {
        isMuted = muted;
        if (backgroundMusicClip != null) {
            setClipVolume(backgroundMusicClip, muted ? 0.0f : musicVolume);
        }
        LOGGER.info("Audio " + (muted ? "muted" : "unmuted"));
    }

    /**
     * Gets the current music volume.
     * 
     * @return current music volume (0.0f to 1.0f)
     */
    public static float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Checks if audio is currently muted.
     * 
     * @return true if muted, false otherwise
     */
    public static boolean isMuted() {
        return isMuted;
    }

    /**
     * Gets the path of the currently playing background music.
     * 
     * @return current music path or null if no music is playing
     */
    public static String getCurrentMusicPath() {
        return currentMusicPath;
    }

    /**
     * Checks if background music is currently playing.
     * 
     * @return true if music is playing, false otherwise
     */
    public static boolean isMusicPlaying() {
        return backgroundMusicClip != null && backgroundMusicClip.isRunning();
    }

    /**
     * Helper method to set volume for a clip.
     * 
     * @param clip the audio clip
     * @param volume volume level (0.0f to 1.0f)
     */
    private static void setClipVolume(final Clip clip, final float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            final float range = gainControl.getMaximum() - gainControl.getMinimum();
            final float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    /**
     * Cleanup method to release audio resources.
     * Should be called when the application is shutting down.
     */
    public static void cleanup() {
        stopBackgroundMusic();
        LOGGER.info("AudioManager cleanup completed");
    }
}
