package it.unibo.exam.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Simple loader for background images.
 */
public class BackgroundLoader {

    /**
     * @param path
     * @return the image
     */
    public BufferedImage loadBackgroundImage(final String path) {
        try {
            final String projectDir = System.getProperty("user.dir");
            final File bgImage = new File(projectDir + "/src/main/java" + path);
            if (bgImage.exists() && bgImage.isFile()) {
                return ImageIO.read(bgImage);
            }
        return createDefaultBackground();

        } catch (final IOException e) {
            return createDefaultBackground();
        }
    }

    /**
     * @return a simple gray iamge
     */
    private BufferedImage createDefaultBackground() {
        final BufferedImage defaultImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < defaultImage.getWidth(); x++) {
            for (int y = 0; y < defaultImage.getHeight(); y++) {
                defaultImage.setRGB(x, y, Color.DARK_GRAY.getRGB());
            }
        }
        return defaultImage;
    }
}
