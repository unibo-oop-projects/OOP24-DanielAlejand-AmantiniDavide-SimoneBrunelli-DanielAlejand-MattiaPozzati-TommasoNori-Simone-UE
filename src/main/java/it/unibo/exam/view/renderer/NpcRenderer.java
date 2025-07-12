package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.RoamingNpc;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.medialoader.AssetLoader;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.Map;
import java.util.HashMap;

/**
 * Renders both interactive and roaming NPCs, each with its own sprite.
 * The Hub has no interactive NPC, so only the 5 puzzle rooms get interactive sprites.
 */
public final class NpcRenderer extends EntityRenderer {

    private static final Color NPC_COLOR                  = new Color(255, 165, 0);
    private static final Color NPC_BORDER_COLOR           = new Color(255, 140, 0);
    private static final Color INTERACTION_INDICATOR_COLOR = new Color(255, 255, 0);
    private static final Color NAME_BACKGROUND_COLOR      = new Color(0, 0, 0, 128);

    private static final double SPRITE_SCALE              = 2.5;
    private static final int    INDICATOR_FONT_SIZE       = 10;
    private static final int    NAME_FONT_SIZE            = 8;
    private static final int    NAME_MAX_LENGTH           = 12;
    private static final int    NAME_TRIM_LENGTH          = 9;
    private static final int    TEXT_PADDING              = 2;
    private static final int    NAME_BACKGROUND_HEIGHT    = 9;
    private static final int    INDICATOR_SIZE            = 12;
    private static final int    INDICATOR_OFFSET_X        = 5;
    private static final int    INDICATOR_OFFSET_Y        = 5;
    private static final int    INDICATOR_CIRCLE_MARGIN   = 3;

    /** room-name → sprite for roaming NPCs. */
    private final Map<String, Image> roamingSprites     = new HashMap<>();
    /** room-name → sprite for interactive NPCs (only puzzle rooms).*/
    private final Map<String, Image> interactiveSprites = new HashMap<>();
    private       String            currentRoomName     = "";

    /**
     * Constructs a new NpcRenderer and loads all NPC sprites.
     * Keys in the maps must match {@code Room.getName()}.
     */
    public NpcRenderer() {
        // Roaming NPC sprites (6 rooms including Hub)
        roamingSprites.put("Hub",     AssetLoader.loadImage("characters/students/hub.png"));
        roamingSprites.put("Garden",  AssetLoader.loadImage("characters/students/garden.png"));
        roamingSprites.put("Lab",     AssetLoader.loadImage("characters/students/lab.png"));
        roamingSprites.put("Gym",     AssetLoader.loadImage("characters/students/gym.png"));
        roamingSprites.put("Bar",     AssetLoader.loadImage("characters/students/bar.png"));
        roamingSprites.put("2.12",    AssetLoader.loadImage("characters/students/2.12.png"));

        // Interactive NPC sprites (only the 5 puzzle rooms; no Hub)
        interactiveSprites.put("Garden", AssetLoader.loadImage("characters/teachers/Gardener.png"));
        interactiveSprites.put("Lab",    AssetLoader.loadImage("characters/teachers/AM.png"));
        interactiveSprites.put("Gym",    AssetLoader.loadImage("characters/teachers/Andrew.png"));
        interactiveSprites.put("Bar",    AssetLoader.loadImage("characters/teachers/Bartender.png"));
        interactiveSprites.put("2.12",   AssetLoader.loadImage("characters/teachers/Teacher.png"));
    }

    /**
     * Informs the renderer of the current room so that both roaming and
     * interactive NPCs can pick the correct sprite.
     *
     * @param roomName the name of the current room
     */
    public void setCurrentRoomName(final String roomName) {
        this.currentRoomName = roomName;
    }

    /**
     * Renders an NPC entity. If it's a RoamingNpc, draws its room-specific
     * student sprite; if it's an interactive Npc, draws its puzzle-room sprite
     * (or falls back to a rectangle + "N").
     */
    @Override
    public void render(final Graphics2D g, final Entity entity) {
        if (entity instanceof RoamingNpc) {
            renderRoamingNpc(g, (RoamingNpc) entity);
        } else if (entity instanceof Npc) {
            renderInteractiveNpc(g, (Npc) entity);
        } else {
            throw new IllegalArgumentException("Entity must be an Npc or RoamingNpc");
        }
    }

    private void renderRoamingNpc(final Graphics2D g, final RoamingNpc rn) {
        final Point2D pos = rn.getPosition();
        final Point2D dim = rn.getDimension();
        final int x = pos.getX(), y = pos.getY(),
                  w = dim.getX(),  h = dim.getY();
        final Image sprite = roamingSprites.get(currentRoomName);

        if (sprite != null && sprite.getWidth(null) > 0) {
            final int imgW = sprite.getWidth(null),
                      imgH = sprite.getHeight(null);
            final double baseScale = Math.min((double) w / imgW, (double) h / imgH);
            final double scale     = baseScale * SPRITE_SCALE;
            final int drawW        = (int) (imgW * scale),
                      drawH        = (int) (imgH * scale);
            final int drawX        = x + (w - drawW) / 2,
                      drawY        = y + (h - drawH) / 2;
            g.drawImage(sprite, drawX, drawY, drawW, drawH, null);
        } else {
            g.setColor(NPC_COLOR);
            g.fillRect(x, y, w, h);
            g.setColor(NPC_BORDER_COLOR);
            g.drawRect(x, y, w, h);
        }
    }

    private void renderInteractiveNpc(final Graphics2D g, final Npc npc) {
        final Point2D pos = npc.getPosition();
        final Point2D dim = npc.getDimension();
        final int x = pos.getX(), y = pos.getY(),
                  w = dim.getX(),  h = dim.getY();
        final Image sprite = interactiveSprites.get(currentRoomName);

        if (sprite != null && sprite.getWidth(null) > 0) {
            final int imgW = sprite.getWidth(null),
                      imgH = sprite.getHeight(null);
            final double baseScale = Math.min((double) w / imgW, (double) h / imgH);
            final double scale     = baseScale * SPRITE_SCALE;
            final int drawW        = (int) (imgW * scale),
                      drawH        = (int) (imgH * scale);
            final int drawX        = x + (w - drawW) / 2,
                      drawY        = y + (h - drawH) / 2;
            g.drawImage(sprite, drawX, drawY, drawW, drawH, null);
        } else {
            g.setColor(NPC_COLOR);
            g.fillRect(x, y, w, h);
            g.setColor(NPC_BORDER_COLOR);
            g.drawRect(x, y, w, h);
            drawCenteredText(g, npc, "N", Color.WHITE);
        }

        drawInteractionIndicator(g, npc);
        drawNpcName(g, npc);
    }

    private void drawInteractionIndicator(final Graphics2D g, final Npc npc) {
        final Point2D pos = npc.getPosition();
        final Point2D dim = npc.getDimension();
        final int indicatorX = pos.getX() + dim.getX() / 2 - INDICATOR_OFFSET_X;
        final int indicatorY = pos.getY() - INDICATOR_OFFSET_Y;

        g.setColor(INTERACTION_INDICATOR_COLOR);
        g.fillOval(
            indicatorX - INDICATOR_CIRCLE_MARGIN,
            indicatorY - INDICATOR_CIRCLE_MARGIN,
            INDICATOR_SIZE,
            INDICATOR_SIZE
        );

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, INDICATOR_FONT_SIZE));
        g.drawString("E", indicatorX, indicatorY);
    }

    private void drawNpcName(final Graphics2D g, final Npc npc) {
        String name = npc.getName();
        if (name.length() > NAME_MAX_LENGTH) {
            name = name.substring(0, NAME_TRIM_LENGTH) + "...";
        }

        g.setFont(new Font("Arial", Font.PLAIN, NAME_FONT_SIZE));
        final int textWidth = name.length() * NAME_FONT_SIZE / 2;
        final Point2D pos    = npc.getPosition();
        final Point2D dim    = npc.getDimension();
        final int nameX      = pos.getX() + (dim.getX() - textWidth) / 2;
        final int nameY      = pos.getY() - (TEXT_PADDING + NAME_FONT_SIZE);

        g.setColor(NAME_BACKGROUND_COLOR);
        g.fillRect(
            nameX - TEXT_PADDING,
            nameY - NAME_FONT_SIZE,
            textWidth + TEXT_PADDING * 2,
            NAME_BACKGROUND_HEIGHT
        );

        g.setColor(Color.WHITE);
        g.drawString(name, nameX, nameY);
    }
}
