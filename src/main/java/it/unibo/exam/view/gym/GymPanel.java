package it.unibo.exam.view.gym;

import it.unibo.exam.model.entity.minigame.gym.GymModel;
import it.unibo.exam.model.entity.minigame.gym.Cannon;
import it.unibo.exam.model.entity.minigame.gym.Disk;
import it.unibo.exam.model.entity.minigame.gym.Projectile;
import it.unibo.exam.utility.AssetLoader;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.controller.input.KeyHandler;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.util.List;

/**
 * Graphical panel for the Gym minigame.
 * Handles rendering of disks, cannon, projectile, and score.
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "EI_EXPOSE_REP2", "constructor-calls-overridable-method"}, 
    justification = "model and keyHandler are safe for broadcasting and not serialized.")
@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
public class GymPanel extends JPanel {
    private static final int REFRESH_RATE = 16; // ~60 FPS
    private static final int DISPLACEMENT_FACTOR = 24;
    private static final int PADDING = 15;
    private static final int PREFERRED_WIDTH = 1000;
    private static final int PREFERRED_HEIGHT = 600;
    private static final long serialVersionUID = 1L;

    private final GymModel model;
    private final KeyHandler keyHandler;
    private final Image backgroundImage;

    /**
     * Constructs a new GymPanel associated with the model.
     * @param model logical model of the minigame
     */
    public GymPanel(final GymModel model) {
        this.model = model;
        this.keyHandler = new KeyHandler();
        setFocusable(true);
        this.backgroundImage = AssetLoader.loadImage("gym/background/gback.png");

        final Timer timer = new Timer(REFRESH_RATE, e -> {
            // Handle key input for cannon
            final Cannon cannon = model.getCannon();
            if (keyHandler.isLeftPressed()) {
                cannon.setAngle(cannon.getAngle() + Math.PI / DISPLACEMENT_FACTOR);
            }
            if (keyHandler.isRightPressed()) {
                cannon.setAngle(cannon.getAngle() - Math.PI / DISPLACEMENT_FACTOR);
            }
            if (keyHandler.isSpaceBarPressed()) {
                model.fireProjectile();
            }
            model.update();
            repaint();
        });
        timer.start();

        addKeyListener(keyHandler);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    /**
     * Draws all minigame elements: disks, cannon, projectile, score, and next ball color.
     * @param g graphics context
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

         // --- DRAW THE BACKGROUND FIRST ---
         if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        // Draw disks
        final List<Disk> disks = model.getDisks();
        for (final Disk disk : disks) {
            if (disk != null && !disk.isPopped()) {
                g.setColor(disk.getColor());
                final Point2D pos = disk.getPosition();
                final int r = disk.getRadius();
                g.fillOval(pos.getX() - r, pos.getY() - r, 2 * r, 2 * r);
                g.setColor(Color.BLACK);
                g.drawOval(pos.getX() - r, pos.getY() - r, 2 * r, 2 * r);
            }
        }

        // Draw cannon
        final Cannon cannon = model.getCannon();
        g.setColor(cannon.getColor());
        final Point2D base = cannon.getPosition();
        g.fillRect(base.getX(), base.getY(), cannon.getWidth(), cannon.getHeight());
        final Point2D tip = cannon.getCannonTip();
        g.setColor(Color.DARK_GRAY);
        g.drawLine(base.getX() + cannon.getWidth() / 2, base.getY(), tip.getX(), tip.getY());

        // PROJECTILE
        final Projectile proj = model.getProjectile();
        if (proj != null && proj.isActive()) {
            g.setColor(proj.getColor());
            final Point2D pos = proj.getPosition();
            final int r = proj.getRadius();
            g.fillOval(pos.getX() - r, pos.getY() - r, 2 * r, 2 * r);
            g.setColor(Color.BLACK);
            g.drawOval(pos.getX() - r, pos.getY() - r, 2 * r, 2 * r);
        }

        // NEXT DISK
        final Color nextColor = model.getNextProjectileColor();
        if (nextColor != null) {
            final int size = Disk.defaultRadius() * 2;
            final int x = getWidth() - size - PADDING;
            final int y = getHeight() - size - PADDING;
            g.setColor(nextColor);
            g.fillOval(x, y, size, size);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, size, size);
            g.drawString("Next", x - PADDING, y - PADDING);
        }

        // SCORE
        g.setColor(Color.BLACK);
        g.drawString("Score: " + model.getScore(), PADDING, PADDING);
    }

    /**
     * @return preferred panel size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }
}
