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
    private static final int ROWS = 4;
    private static final int COLS = 8;
    private static final int DISK_GAP = 2; // Gap between disks in the grid

    private static final Color[] DISK_COLORS = {
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
    };

    private static final int MIN_DISK_RADIUS = 18;
    private static final int MAX_DISK_RADIUS = 40;

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
        this.diskRadius = Math.max(MIN_DISK_RADIUS, Math.min(MAX_DISK_RADIUS, Math.min(maxRadiusX, maxRadiusY)));
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
        final int totalWidth = COLS * radius * 2;
        final int startX = (env.getX() - totalWidth) / 2;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final int x = startX + col * radius * 2 + (row % 2 == 0 ? 0 : radius + 2);
                final int y = row * radius * 2 + startY;
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
                && current.getPosition().distance(neighbor.getPosition()) <= 2 * r) {
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
            // Usa sempre diskRadius per il proiettile
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
     * Resizes the game environment in a robust way: the cannon is always centered at the bottom with padding,
     * the disks are realigned in a grid at the top, and the projectile is clamped within the new bounds.
     * This ensures the minigame remains playable in fullscreen or with extreme proportions.
     * @param newSize the new environment size
     */
    public void resize(final Point2D newSize) {
        if (newSize != null) {
            final int newWidth = newSize.getX();
            final int newHeight = newSize.getY();
            // Update the environment size
            env.setXY(newWidth, newHeight);
            // Update the disk radius based on the new environment
            updateDiskRadius();
            // Recenter the cannon at the bottom
            if (cannon != null) {
                final int cannonWidth = diskRadius * 2;
                final int cannonHeight = diskRadius * 2;
                final int x = newWidth / 2 - cannonWidth / 2;
                final int y = newHeight - CANNON_PADDING - cannonHeight;
                cannon.setPosition(new Point2D(x, y));
            }
            // Recalculate the grid of disks at the top, preserving color and popped state
            if (disks != null && !disks.isEmpty()) {
                final int radius = diskRadius;
                final int startY = CANNON_PADDING;
                final int totalWidth = COLS * radius * 2;
                final int startX = (newWidth - totalWidth) / 2;
                int diskIdx = 0;
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (diskIdx < disks.size()) {
                            final Disk disk = disks.get(diskIdx);
                            final int x = startX + col * radius * 2 + (row % 2 == 0 ? 0 : radius + 2);
                            final int y = row * radius * 2 + startY;
                            disk.setPosition(new Point2D(x, y));
                            diskIdx++;
                        }
                    }
                }
            }
            // Clamp the projectile within the new bounds if present
            if (projectile != null && projectile.isActive()) {
                final int px = Math.max(0, Math.min(projectile.getPosition().getX(), newWidth - 1));
                final int py = Math.max(0, Math.min(projectile.getPosition().getY(), newHeight - 1));
                projectile.setPosition(new Point2D(px, py));
            }
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
