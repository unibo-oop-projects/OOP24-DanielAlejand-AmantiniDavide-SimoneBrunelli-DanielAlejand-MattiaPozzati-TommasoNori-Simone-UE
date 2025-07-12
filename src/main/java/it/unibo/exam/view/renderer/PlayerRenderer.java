package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.medialoader.AssetLoader;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;

/**
 * Renderer for the player entity, scaling the sprite up by a fixed factor.
 */
public class PlayerRenderer extends EntityRenderer {

    private static final Color PLAYER_FALLBACK_COLOR     = new Color(70, 130, 180);
    private static final Color PLAYER_FALLBACK_BORDER    = new Color(25, 25, 112);
    private static final Color DIRECTION_INDICATOR_COLOR = Color.YELLOW;

    // Load your player sprite once
    private static final Image PLAYER_SPRITE =
        AssetLoader.loadImage("characters/player/player.png");

    /**
     * How much bigger to draw the sprite relative to the model's hitbox.
     * 1.0 = exactly hitbox size, 2.0 = twice as large, etc.
     */
    private static final double SPRITE_SCALE = 2.5;

    @Override
    public void render(Graphics2D g, Entity entity) {
        if (!(entity instanceof Player)) {
            throw new IllegalArgumentException("Entity must be a Player");
        }
        Player player   = (Player) entity;
        Point2D pos     = player.getPosition();
        Point2D dim     = player.getDimension();
        int boxX        = pos.getX();
        int boxY        = pos.getY();
        int boxW        = dim.getX();
        int boxH        = dim.getY();

        if (PLAYER_SPRITE != null) {
            int imgW = PLAYER_SPRITE.getWidth(null);
            int imgH = PLAYER_SPRITE.getHeight(null);
            if (imgW > 0 && imgH > 0) {
                // base scale to fit sprite in hitbox
                double baseScale = Math.min((double)boxW / imgW, (double)boxH / imgH);
                // enlarge by our factor
                double scale = baseScale * SPRITE_SCALE;

                int drawW = (int)(imgW * scale);
                int drawH = (int)(imgH * scale);

                // center the enlarged sprite on the hitbox
                int drawX = boxX + (boxW - drawW) / 2;
                int drawY = boxY + (boxH - drawH) / 2;

                g.drawImage(PLAYER_SPRITE, drawX, drawY, drawW, drawH, null);
            } else {
                drawFallback(g, boxX, boxY, boxW, boxH, player);
            }
        } else {
            drawFallback(g, boxX, boxY, boxW, boxH, player);
        }

        drawDirectionIndicator(g, player);
    }

    private void drawFallback(Graphics2D g, int x, int y, int w, int h, Player player) {
        g.setColor(PLAYER_FALLBACK_COLOR);
        g.fillRect(x, y, w, h);
        g.setColor(PLAYER_FALLBACK_BORDER);
        g.drawRect(x, y, w, h);
        drawCenteredText(g, player, "P", Color.WHITE);
    }

    private void drawDirectionIndicator(Graphics2D g, Player player) {
        Point2D pos = player.getPosition();
        Point2D dim = player.getDimension();
        int centerX = pos.getX() + dim.getX() / 2;
        int centerY = pos.getY() + dim.getY() / 4;
        g.setColor(DIRECTION_INDICATOR_COLOR);
        g.fillOval(centerX - 2, centerY - 2, 4, 4);
    }
}
