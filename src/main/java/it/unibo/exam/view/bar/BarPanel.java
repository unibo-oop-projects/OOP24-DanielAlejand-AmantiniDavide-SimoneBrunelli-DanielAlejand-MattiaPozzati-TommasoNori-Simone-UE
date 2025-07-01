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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final int SELECTED_BORDER_THICKNESS = 5;

    /**
     * The puzzle model this panel visualizes.
     */
    @SuppressFBWarnings(
      value = "EI2",
      justification = "MVC: panel must keep a reference to its model for rendering"
    )
    private final transient BarModel model;

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

            // Draw the border (thicker if selected)
            if (i == selected) {
                g2.setColor(Color.MAGENTA);
                g2.setStroke(new BasicStroke(SELECTED_BORDER_THICKNESS));
            } else {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
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
