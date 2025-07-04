package it.unibo.exam.view.bar;

import it.unibo.exam.controller.minigame.bar.GlassClickListener;
import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.Glass;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import java.util.logging.Logger;


/**
 * Panel that renders the Sort & Serve bar puzzle and handles mouse input.
 */
public final class BarPanel extends JPanel {

    private static final long serialVersionUID           = 1L;

    // Centering & Sizing constants
    private static final int MAX_BOTTLE_WIDTH  = 80;  // Max width per bottle
    private static final int MAX_BOTTLE_HEIGHT = 200; // Max height per bottle
    private static final int TOP_BOTTOM_PAD    = 80;  // Padding above/below the row
    private static final int SIDE_PAD          = 80;  // Padding left/right
    private static final int GAP               = 28;  // Gap between bottles
    private static final int GLASS_OVERLAY_RED   = 255;
    private static final int GLASS_OVERLAY_GREEN = 255;
    private static final int GLASS_OVERLAY_BLUE  = 255;
    private static final int GLASS_OVERLAY_ALPHA = 90;   // Opacity for the frosted effect
    private static final int SELECTED_BORDER_THICKNESS = 5;
    private static final Logger LOGGER = Logger.getLogger(BarPanel.class.getName());


    /**
     * The puzzle model this panel visualizes.
     */
    @SuppressFBWarnings(
      value = "EI2",
      justification = "MVC: panel must keep a reference to its model for rendering"
    )
    private final transient BarModel model;

    /**
     * The background image drawn behind the bottles.
     */
    private transient Image backgroundImage;

    /**
     * Called when the user clicks a glass.
     * Marked transient because this panel is never serialized,
     * and the listener need not be Serializable.
     */
    @SuppressFBWarnings(
      value = "SE_BAD_FIELD",
      justification = "Panel is never serialized; listener need not be Serializable"
    )
    private transient GlassClickListener clickListener;
    private int selected = -1;

    /**
     * Constructs a panel for the given BarModel.
     *
     * @param model the puzzle model to render and interact with
     */
    public BarPanel(final BarModel model) {
        this.model = model;

        // --- LOAD BACKGROUND IMAGE ---
        try {
            // Adjust the path as needed. Example: "/img/bar-bg.png"
            backgroundImage = ImageIO.read(getClass().getResource("/bar/backgrounds/barminigame.png"));
        } catch (IOException | IllegalArgumentException e) {
            // LOGGER.info("Background URL: " + url);
            LOGGER.warning("Resource NOT FOUND!");
            backgroundImage = null;
        }

        setFocusable(true);
        requestFocusInWindow();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                final int totalGlasses = model.getNumGlasses();
                final int bottleWidth = getBottleWidth();
                final int totalWidth = totalGlasses * bottleWidth + (totalGlasses - 1) * GAP;
                final int startX = (getWidth() - totalWidth) / 2;

                final int idx = (e.getX() - startX) / (bottleWidth + GAP);
                if (idx < 0 || idx >= totalGlasses) {
                    return;
                }
                if (clickListener != null) {
                    clickListener.glassClicked(idx);
                }
            }
        });
    }

    /**
     * Helper to compute the bottle width based on current panel size and max/min.
     *
     * @return the current bottle width
     */
    private int getBottleWidth() {
        final int totalGlasses = model.getNumGlasses();
        final int available = getWidth() - 2 * SIDE_PAD - GAP * (totalGlasses - 1);
        return Math.min(MAX_BOTTLE_WIDTH, available / totalGlasses);
    }

    /**
     * Helper to compute the bottle height based on current panel size and max/min.
     *
     * @return the current bottle height
     */
    private int getBottleHeight() {
        final int available = getHeight() - 2 * TOP_BOTTOM_PAD;
        return Math.min(MAX_BOTTLE_HEIGHT, available);
    }

    /**
     * Returns the currently selected glass index, or -1 if none.
     *
     * @return the index of the selected glass, or -1
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Highlights the given glass index.
     *
     * @param idx the index to select (0-based)
     */
    public void setSelected(final int idx) {
        selected = idx;
    }

    /**
     * Clears any current selection.
     */
    public void clearSelection() {
        selected = -1;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        // --- DRAW THE BACKGROUND FIRST ---
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        final int totalGlasses = model.getNumGlasses();
        final int bottleWidth  = getBottleWidth();
        final int bottleHeight = getBottleHeight();
        final int perLayerHt   = bottleHeight / model.getCapacity();

        // Compute left margin to center all bottles
        final int totalWidth = totalGlasses * bottleWidth + (totalGlasses - 1) * GAP;
        final int startX     = (getWidth() - totalWidth) / 2;
        final int startY     = (getHeight() - bottleHeight) / 2;

        final Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < totalGlasses; i++) {
            final int x     = startX + i * (bottleWidth + GAP);
            final int y     = startY;
            final Glass glass = model.getGlasses().get(i);

            // --- DRAW TRANSPARENT GLASS OVERLAY BEFORE LAYERS ---
            g2.setColor(new Color(
                GLASS_OVERLAY_RED,
                GLASS_OVERLAY_GREEN,
                GLASS_OVERLAY_BLUE,
                GLASS_OVERLAY_ALPHA)); // White with alpha for "frosted" effect
            g2.fillRect(x, y, bottleWidth, bottleHeight);

            // --- DRAW LAYERS FROM BOTTOM TO TOP ---
            final List<Color> toDraw = new ArrayList<>(glass.getLayers());
            Collections.reverse(toDraw);

            int layerIndex = 0;
            for (final Color c : toDraw) {
                g2.setColor(c);
                g2.fillRect(
                    x,
                    y + (model.getCapacity() - 1 - layerIndex) * perLayerHt,
                    bottleWidth,
                    perLayerHt
                );
                layerIndex++;
            }

            // Draw the border (thicker if selected, otherwise 3px)
            if (i == selected) {
                g2.setColor(Color.MAGENTA);
                g2.setStroke(new BasicStroke(SELECTED_BORDER_THICKNESS));
            } else {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
            }
            g2.drawRect(x, y, bottleWidth, bottleHeight);
        }

        // Reset stroke to default
        g2.setStroke(new BasicStroke(1));
    }

    @Override
    public Dimension getPreferredSize() {
        // Large enough for background, plus nicely spaced bottles
        return new Dimension(
            SIDE_PAD * 2 + model.getNumGlasses() * MAX_BOTTLE_WIDTH + (model.getNumGlasses() - 1) * GAP,
            TOP_BOTTOM_PAD * 2 + MAX_BOTTLE_HEIGHT
        );
    }

    /**
     * Registers a listener to be notified when a glass is clicked.
     *
     * @param listener the listener to add
     */
    public void setGlassClickListener(final GlassClickListener listener) {
        this.clickListener = listener;
    }
}
