package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.medialoader.AssetLoader;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;

/**
 * Renders the Player entity using a scaled sprite.
 * <p>
 * The yellow direction dot has been removed.
 * </p>
 */
public final class PlayerRenderer extends EntityRenderer {

    private static final Color PLAYER_FALLBACK_COLOR  = new Color(70, 130, 180);
    private static final Color PLAYER_FALLBACK_BORDER = new Color(25, 25, 112);

    /** Sprite image for the player. */
    private static final Image PLAYER_SPRITE =
        AssetLoader.loadImage("characters/player/player.png");

    /**
     * Scale factor applied to the sprite relative to the model’s hitbox.
     * 1.0 = exactly hitbox size, 2.0 = twice as large, etc.
     */
    private static final double SPRITE_SCALE = 2.5;

    /**
     * Renders the given entity as a Player sprite.
     *
     * @param g      the graphics context to draw on
     * @param entity the entity to render; must be a {@link Player}
     * @throws IllegalArgumentException if {@code g} or {@code entity} is null,
     *                                  or if {@code entity} is not a {@code Player}
     */
    @Override
    public void render(final Graphics2D g, final Entity entity) {
        if (g == null || entity == null) {
            throw new IllegalArgumentException("Graphics and entity must not be null");
        }
        if (!(entity instanceof Player)) {
            throw new IllegalArgumentException("Entity must be a Player");
        }

        final Player player = (Player) entity;
        final Point2D pos    = player.getPosition();
        final Point2D dim    = player.getDimension();
        final int boxX       = pos.getX();
        final int boxY       = pos.getY();
        final int boxW       = dim.getX();
        final int boxH       = dim.getY();

        if (PLAYER_SPRITE != null) {
            final int imgW = PLAYER_SPRITE.getWidth(null);
            final int imgH = PLAYER_SPRITE.getHeight(null);

            if (imgW > 0 && imgH > 0) {
                final double baseScale = Math.min((double) boxW / imgW, (double) boxH / imgH);
                final double scale     = baseScale * SPRITE_SCALE;
                final int drawW        = (int) (imgW * scale);
                final int drawH        = (int) (imgH * scale);
                final int drawX        = boxX + (boxW - drawW) / 2;
                final int drawY        = boxY + (boxH - drawH) / 2;

                g.drawImage(PLAYER_SPRITE, drawX, drawY, drawW, drawH, null);
                return;
            }
        }

        // Fallback if sprite is missing or invalid
        drawFallback(g, boxX, boxY, boxW, boxH, player);
    }

    /**
     * Draws the fallback: a filled rectangle with a 'P' label.
     *
     * @param g      the graphics context
     * @param x      the hitbox X coordinate
     * @param y      the hitbox Y coordinate
     * @param w      the hitbox width
     * @param h      the hitbox height
     * @param player the player entity (for centered text)
     */
    private void drawFallback(
            final Graphics2D g,
            final int x,
            final int y,
            final int w,
            final int h,
            final Player player
    ) {
        g.setColor(PLAYER_FALLBACK_COLOR);
        g.fillRect(x, y, w, h);
        g.setColor(PLAYER_FALLBACK_BORDER);
        g.drawRect(x, y, w, h);
        drawCenteredText(g, player, "P", Color.WHITE);
    }
}
