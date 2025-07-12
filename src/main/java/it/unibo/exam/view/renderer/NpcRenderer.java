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
 * Renders both interactive and roaming NPCs.
 * <p>
 * Interactive NPCs are drawn as orange rectangles with a name label and an
 * "E" interaction indicator. Roaming NPCs are drawn using room-specific
 * sprite images of students, falling back to a rectangle if the image is not found.
 * </p>
 */
public final class NpcRenderer extends EntityRenderer {

    private static final Color NPC_COLOR                  = new Color(255, 165, 0);
    private static final Color NPC_BORDER_COLOR           = new Color(255, 140, 0);
    private static final Color INTERACTION_INDICATOR_COLOR = new Color(255, 255, 0);
    private static final Color NAME_BACKGROUND_COLOR      = new Color(0, 0, 0, 128);
    private static final double SPRITE_SCALE              = 2.5;
    private static final int   INDICATOR_FONT_SIZE        = 10;
    private static final int   NAME_FONT_SIZE             = 8;
    private static final int   NAME_MAX_LENGTH            = 12;
    private static final int   NAME_TRIM_LENGTH           = 9;
    private static final int   TEXT_PADDING               = 2;
    private static final int   NAME_BACKGROUND_HEIGHT     = 9;
    private static final int   INDICATOR_SIZE             = 12;
    private static final int   INDICATOR_OFFSET_X         = 5;
    private static final int   INDICATOR_OFFSET_Y         = 5;
    private static final int   INDICATOR_CIRCLE_MARGIN    = 3;

    /** Maps room names to their roaming-NPC sprite images. */
    private final Map<String, Image> roamingSprites      = new HashMap<>();
    private       String             currentRoomName     = "";

    /**
     * Constructs a new NpcRenderer and loads all roaming-NPC sprites.
     * Keys in the sprite map must match {@code Room.getName()}.
     */
    public NpcRenderer() {
        // student sprites per room
        roamingSprites.put("Hub",      AssetLoader.loadImage("characters/students/hub.png"));
        roamingSprites.put("Garden",   AssetLoader.loadImage("characters/students/garden.png"));
        roamingSprites.put("Lab",      AssetLoader.loadImage("characters/students/lab.png"));
        roamingSprites.put("Gym",      AssetLoader.loadImage("characters/students/gym.png"));
        roamingSprites.put("Bar",      AssetLoader.loadImage("characters/students/bar.png"));
        roamingSprites.put("2.12",     AssetLoader.loadImage("characters/students/2.12.png"));
    }

    /**
     * Informs the renderer of the current room name so that roaming NPCs
     * can use the correct sprite.
     *
     * @param roomName the name of the room
     */
    public void setCurrentRoomName(final String roomName) {
        this.currentRoomName = roomName;
    }

    /**
     * Renders an NPC entity. If the entity is a {@link RoamingNpc}, draws
     * the room-specific student sprite; otherwise, draws an interactive NPC.
     *
     * @param g      the graphics context to draw on
     * @param entity the NPC entity to render
     * @throws IllegalArgumentException if the entity is neither an
     *                                  {@link Npc} nor a {@link RoamingNpc}
     */
    @Override
    public void render(final Graphics2D g, final Entity entity) {
        if (entity instanceof RoamingNpc) {
            renderRoamingNpc(g, (RoamingNpc) entity);
        } else if (entity instanceof Npc) {
            renderInteractiveNpc(g, (Npc) entity);
        } else {
            throw new IllegalArgumentException(
                "Entity must be an Npc or RoamingNpc instance"
            );
        }
    }

    /**
     * Draws a roaming NPC using the student sprite for the current room,
     * scaled and centered within its hitbox. Falls back to a rectangle.
     *
     * @param g  the graphics context
     * @param rn the roaming NPC to render
     */
    private void renderRoamingNpc(final Graphics2D g, final RoamingNpc rn) {
        final Point2D pos = rn.getPosition();
        final Point2D dim = rn.getDimension();
        final int x       = pos.getX();
        final int y       = pos.getY();
        final int w       = dim.getX();
        final int h       = dim.getY();
        final Image sprite = roamingSprites.get(currentRoomName);

        if (sprite != null && sprite.getWidth(null) > 0) {
            final int imgW = sprite.getWidth(null);
            final int imgH = sprite.getHeight(null);
            final double baseScale = Math.min((double) w / imgW, (double) h / imgH);
            final double scale     = baseScale * SPRITE_SCALE;
            final int drawW = (int) (imgW * scale);
            final int drawH = (int) (imgH * scale);
            final int drawX = x + (w - drawW) / 2;
            final int drawY = y + (h - drawH) / 2;
            g.drawImage(sprite, drawX, drawY, drawW, drawH, null);
        } else {
            g.setColor(NPC_COLOR);
            g.fillRect(x, y, w, h);
            g.setColor(NPC_BORDER_COLOR);
            g.drawRect(x, y, w, h);
        }
    }

    /**
     * Draws an interactive NPC as a colored rectangle with a border,
     * a centered "N", an interaction indicator above, and the NPC's name.
     *
     * @param g   the graphics context
     * @param npc the interactive NPC to render
     */
    private void renderInteractiveNpc(final Graphics2D g, final Npc npc) {
        final Point2D position  = npc.getPosition();
        final Point2D dimension = npc.getDimension();
        final int x = position.getX();
        final int y = position.getY();
        final int w = dimension.getX();
        final int h = dimension.getY();

        g.setColor(NPC_COLOR);
        g.fillRect(x, y, w, h);
        g.setColor(NPC_BORDER_COLOR);
        g.drawRect(x, y, w, h);
        drawCenteredText(g, npc, "N", Color.WHITE);
        drawInteractionIndicator(g, npc);
        drawNpcName(g, npc);
    }

    /**
     * Draws a yellow "E" indicator above the NPC to show it is interactable.
     *
     * @param g   the graphics context
     * @param npc the NPC for which to draw the indicator
     */
    private void drawInteractionIndicator(final Graphics2D g, final Npc npc) {
        final Point2D position = npc.getPosition();
        final Point2D dimension = npc.getDimension();
        final int indicatorX = position.getX() + dimension.getX() / 2 - INDICATOR_OFFSET_X;
        final int indicatorY = position.getY() - INDICATOR_OFFSET_Y;

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

    /**
     * Draws the NPC's name above its hitbox, truncating if too long, with a
     * semi-transparent background.
     *
     * @param g   the graphics context
     * @param npc the NPC whose name to draw
     */
    private void drawNpcName(final Graphics2D g, final Npc npc) {
        String name = npc.getName();
        if (name.length() > NAME_MAX_LENGTH) {
            name = name.substring(0, NAME_TRIM_LENGTH) + "...";
        }

        g.setFont(new Font("Arial", Font.PLAIN, NAME_FONT_SIZE));
        final int textWidth = name.length() * NAME_FONT_SIZE / 2;
        final Point2D position = npc.getPosition();
        final Point2D dimension = npc.getDimension();
        final int nameX = position.getX() + (dimension.getX() - textWidth) / 2;
        final int nameY = position.getY() - (TEXT_PADDING + NAME_FONT_SIZE);

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
