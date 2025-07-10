package it.unibo.exam.model.entity.minigame.gym;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.unibo.exam.controller.minigame.gym.GymMinigame;
import it.unibo.exam.utility.geometry.Point2D;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Logical model for the Gym minigame.
 * Manages game logic, disk creation, cannon, projectile, score, and win conditions.
 */
public class GymModel {

    private static final int POINTS_PER_DISK = 10;
    private static final int WIN_SCORE = 500;
    private static final int CANNON_PADDING = 50;
    private static final int DISK_GAP = 10;
    private static final int ROWS = 4;
    private static final int COLS = 8;

    private static final Color[] DISK_COLORS = {
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
    };

    private Cannon cannon;
    private List<Disk> disks;
    private Projectile projectile;
    private int score;
    private boolean gameOver;
    private final Point2D env; 
    private final Random random;
    private GymMinigame minigame;
    private Color nextProjectileColor = Color.RED;
    private long startTimeMillis;
    private int diskRadius;


    /**
     * Constructs the Gym minigame model.
     * @param env the game environment size
     */
    public GymModel(final Point2D env) {
        this.env = new Point2D(env);
        this.random = new Random();
        initializeGame();
    }

    private void initializeGame() {
        updateDiskRadius();
        cannon = createCannon();
        disks = new ArrayList<>();
        createDisks();
        score = 0;
        gameOver = false;
        startTimeMillis = System.currentTimeMillis();
    }

    /**
     * Updates the disk radius based on the current environment size.
     */
    private void updateDiskRadius() {
        final int availableWidth = env.getX() - 2 * CANNON_PADDING;
        final int availableHeight = env.getY() / 2;
        final int maxRadiusX = (availableWidth - (COLS - 1) * DISK_GAP) / (2 * COLS);
        final int maxRadiusY = (availableHeight - (ROWS - 1) * DISK_GAP) / (2 * ROWS);
        this.diskRadius = Math.max(10, Math.min(maxRadiusX, maxRadiusY));
    }

    /**
     * @return the current disk radius.
     */
    public int getCurrentDiskRadius() {
        return diskRadius;
    }

    /**
     * @return new Cannon
     */
    private Cannon createCannon() {
        final int cannonWidth = diskRadius * 2;
        final int cannonHeight = diskRadius * 2;
        final int x = env.getX() / 2 - cannonWidth / 2;
        final int y = env.getY() - CANNON_PADDING - cannonHeight;
        return new Cannon(new Point2D(x, y), Color.BLUE, env);
    }

    /**
     * Updates the game state: moves the projectile, checks collisions and end conditions.
     */
    public void update() {
        if (projectile != null && projectile.isActive()) {
            projectile.update();
            checkProjectileCollisions();
        }
        // Controllo: se almeno un disco è troppo in basso, ricomincia il gioco
        final int thresholdY = env.getY() - 2 * CANNON_PADDING; // 100px sopra il bordo inferiore, puoi regolare
        for (final Disk disk : disks) {
            if (!disk.isPopped() && disk.getPosition().getY() + disk.getRadius() > thresholdY) {
                initializeGame();
                return;
            }
        }
        if (disks.isEmpty() && minigame != null) {
            minigame.onGameCompleted();
        }
    }

    private void checkProjectileCollisions() {
        final Point2D projPos = projectile.getPosition();
        final int projRadius = projectile.getRadius();

        // Collisione con i bordi
        if (projPos.getX() - projRadius <= 0 || projPos.getX() + projRadius >= env.getX() 
            || projPos.getY() - projRadius <= 0) {
            projectile.setActive(false);
            attachDisk();
            return;
        }

        // Collisione con le bolle esistenti
        for (final Disk disk : disks) {
            if (!disk.isPopped() && disk.getPosition().distance(projPos) < projRadius + disk.getRadius()) {
                projectile.setActive(false);
                attachDisk();
                checkForMatches();
                return;
            }
        }
    }

    /**
     * Creates the initial grid of disks, centered horizontally.
     */
    private void createDisks() {
        final int radius = diskRadius;
        final int startY = CANNON_PADDING;
        final int totalWidth = COLS * (radius * 2 + DISK_GAP) - DISK_GAP;
        final int startX = (env.getX() - totalWidth) / 2;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final int x = startX + col * (radius * 2 + DISK_GAP) + (row % 2 == 0 ? 0 : radius + 2);
                final int y = row * (radius * 2 + DISK_GAP) + startY;
                final Color color = DISK_COLORS[random.nextInt(DISK_COLORS.length)];
                disks.add(new Disk(new Point2D(x, y), color, radius, env));
            }
        }
    }

    /**
     * Attaches a new disk at the projectile's position, clamping it within the environment bounds.
     */
    private void attachDisk() {
        if (projectile != null) {
            final Point2D pos = projectile.getPosition();
            // Clamp la posizione dentro i limiti dell'ambiente
            final int x = Math.max(0, Math.min(pos.getX(), env.getX() - 1));
            final int y = Math.max(0, Math.min(pos.getY(), env.getY() - 1));
            disks.add(new Disk(new Point2D(x, y), projectile.getColor(), projectile.getRadius(), env));
            projectile = null;
        }
    }

    /**
     * Checks if the new disk creates a winning combination and updates the score.
     * Ends the game if the score threshold is reached.
     * Supporta cluster a catena: dopo ogni scoppio, rilancia la ricerca su tutti i dischi finché non ci sono più cluster validi.
     */
    private void checkForMatches() {
        boolean foundCluster;
        do {
            foundCluster = false;
            // Copia la lista per evitare ConcurrentModificationException
            final List<Disk> currentDisks = new ArrayList<>(disks);
            for (final Disk disk : currentDisks) {
                if (!disk.isPopped()) {
                    final List<Disk> matches = findMatches(disk);
                    if (matches.size() >= 3) {
                        for (final Disk d : matches) {
                            d.pop();
                            score += POINTS_PER_DISK;
                        }
                        removePoppedDisk();
                        foundCluster = true;
                        break; // Ricomincia la ricerca dopo la rimozione
                    }
                }
            }
        } while (foundCluster);

        if (score >= WIN_SCORE && minigame != null) {
            minigame.onGameCompleted();
        }
    }

    /**
     * Finds all adjacent disks of the same color.
     * @param startDisk the starting disk
     * @return list of connected disks
     */
    private List<Disk> findMatches(final Disk startDisk) {
        final List<Disk> matches = new ArrayList<>();
        findMatchesRecursive(startDisk, matches);
        return matches;
    }

    /**
     * Recursively finds adjacent disks of the same color.
     * @param current the current disk
     * @param matches the accumulated list of found disks
     */
    private void findMatchesRecursive(final Disk current, final List<Disk> matches) {
        if (current.isPopped() || matches.contains(current)) {
            return;
        }
        matches.add(current);
        final int r = current.getRadius();

        for (final Disk neighbor : disks) {
            if (!neighbor.isPopped() 
                && !matches.contains(neighbor)
                && current.getColor().equals(neighbor.getColor())
                && current.getPosition().distance(neighbor.getPosition()) <= 2 * r + DISK_GAP) {
                findMatchesRecursive(neighbor, matches);
            }
        }
    }

    /**
     * Removes disks marked as "popped" from the list.
     */
    private void removePoppedDisk() {
        disks.removeIf(Disk::isPopped);
    }

    /**
     * Fires a new projectile from the cannon, using the prepared color.
     */
    public void fireProjectile() {
        if (projectile == null || !projectile.isActive()) {
            final Point2D tip = cannon.getCannonTip();
            final Color color = nextProjectileColor;
            projectile = new Projectile(new Point2D(tip), color, diskRadius, cannon.getAngle(), env);
            nextProjectileColor = DISK_COLORS[random.nextInt(DISK_COLORS.length)];
        }
    }

    /**
     * Links the minigame to the model to notify game end.
     * @param minigame the minigame instance
     */
    public void setMinigame(final GymMinigame minigame) {
        this.minigame = minigame;
    }

    /**
     * Resizes the game environment and restarts the game with the new size.
     * @param newSize the new environment size
     */
    public void resize(final Point2D newSize) {
        if (newSize != null) {
            env.setXY(newSize.getX(), newSize.getY());
            initializeGame();
        }
    }

    // Getters
    /**
     * Returns the list of disks in the game.
     * @return the list of active disks
     */
    public final List<Disk> getDisks() { 
        return disks != null ? new ArrayList<>(disks) : new ArrayList<>(); 
    }

    /**
     * Returns the cannon in the game.
     * @return the cannon
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public final Cannon getCannon() { 
        return cannon;
    }

    /**
     * Returns the active projectile (if present), or null if none.
     * @return the projectile or null
     */
    public Projectile getProjectile() { 
        return projectile == null ? null : new Projectile(projectile);
    }

    /**
     * Returns the environment size.
     * @return the environment Point2D
     */
    public Point2D getEnv() {
        return new Point2D(env);
    }

    /**
     * @return the current score
     */
    public int getScore() { 
        return score; 
    }
    /**
     * @return true if the game is over
     */
    public boolean isGameOver() { 
        return gameOver; 
    }
    /**
     * @return the width of the game environment
     */
    public int getBoardWidth() { 
        return env.getX(); 
    }
    /**
     * @return the height of the game environment
     */
    public int getBoardHeight() { 
        return env.getY(); 
    }
    /**
     * @return the color of the next ball to be fired
     */
    public Color getNextProjectileColor() {
        return nextProjectileColor;
    }
    /**
     * @return the minigame start time in milliseconds
     */
    public long getStartTimeMillis() { 
        return startTimeMillis; 
    }
}
