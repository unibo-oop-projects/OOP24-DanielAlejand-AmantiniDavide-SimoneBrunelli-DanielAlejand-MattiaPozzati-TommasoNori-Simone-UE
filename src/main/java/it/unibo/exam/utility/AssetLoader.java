package it.unibo.exam.utility;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Utility class for loading game assets (images, sounds, etc.).
 * Provides centralized asset loading with proper error handling and logging.
 */
public final class AssetLoader {

    private static final Logger LOGGER = Logger.getLogger(AssetLoader.class.getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private AssetLoader() {
        // Utility class should not be instantiated
    }

    /**
     * Loads an image from the resources directory.
     *
     * @param resourcePath the path to the image resource (relative to resources directory)
     * @return the loaded Image, or null if loading failed
     */
    public static Image loadImage(final String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            LOGGER.warning("Resource path is null or empty");
            return null;
        }

        try {
            final var resource = AssetLoader.class.getClassLoader().getResource(resourcePath);
            if (resource == null) {
                LOGGER.warning("Resource not found: " + resourcePath);
                return null;
            }

            LOGGER.info("Loading image from: " + resource);
            final Image image = ImageIO.read(resource);

            if (image == null) {
                LOGGER.warning("Failed to read image from resource: " + resourcePath);
                return null;
            }

            LOGGER.info("Image loaded successfully: " + resourcePath);
            return image;

        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load image: " + resourcePath + " - " + e.getMessage(), e);
            return null;
        } catch (final IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid image format or corrupted file: " + resourcePath + " - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Loads an image from the resources directory with a fallback image.
     *
     * @param resourcePath the path to the image resource (relative to resources directory)
     * @param fallbackPath the path to the fallback image resource
     * @return the loaded Image, or null if both loading attempts failed
     */
    public static Image loadImageWithFallback(final String resourcePath, final String fallbackPath) {
        Image image = loadImage(resourcePath);
        if (image == null && fallbackPath != null) {
            LOGGER.info("Attempting to load fallback image: " + fallbackPath);
            image = loadImage(fallbackPath);
        }
        return image;
    }

    /**
     * Checks if an image resource exists without loading it.
     *
     * @param resourcePath the path to the image resource
     * @return true if the resource exists, false otherwise
     */
    public static boolean imageExists(final String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            return false;
        }

        final var resource = AssetLoader.class.getClassLoader().getResource(resourcePath);
        return resource != null;
    }
}
