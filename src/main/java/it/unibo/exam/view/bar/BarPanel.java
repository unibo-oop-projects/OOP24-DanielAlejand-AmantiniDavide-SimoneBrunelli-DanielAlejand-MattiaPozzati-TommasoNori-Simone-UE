package it.unibo.exam.view.bar;

import it.unibo.exam.controller.minigame.bar.GlassClickListener;
import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.Glass;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Panel that renders the Sort & Serve bar puzzle and handles mouse input.
 */
public final class BarPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int GAP                    = 20;
    private static final int CELL_WIDTH             = 120;
    private static final int LAYER_BASE_HEIGHT      = 40;
    private static final int EXTRA_PANEL_HEIGHT_PAD = 60;
    private GlassClickListener clickListener;

    /**
     * The puzzle model this panel visualizes.
     */
    @SuppressFBWarnings(
      value = "EI2",
      justification = "MVC: panel must keep a reference to its model for rendering"
    )
    private final transient BarModel model;
    private int selected = -1;

    /**
     * @param model the puzzle model to render and interact with
     */
    public BarPanel(final BarModel model) {
        this.model = model;
        setFocusable(true);
        requestFocusInWindow();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                final int idx = e.getX() / (getWidth() / model.getNumGlasses());
                if (idx < 0 || idx >= model.getNumGlasses()) {
                    return;
                }
                if (clickListener != null) {
                    clickListener.glassClicked(idx);
                }
            }
        });
    }

    public int getSelected() {
        return selected; 
    }

    public void setSelected(final int idx) {
        selected = idx;
    }

    public void clearSelection() {
        selected = -1;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final int totalGlasses = model.getNumGlasses();
        final int panelWidth   = getWidth();
        final int panelHeight  = getHeight();

        final int cellWidth  = (panelWidth - GAP * (totalGlasses + 1)) / totalGlasses;
        final int cellHeight = panelHeight - EXTRA_PANEL_HEIGHT_PAD;
        final int perLayerHt = cellHeight / model.getCapacity();

        // Cast to Graphics2D to set stroke thickness
        final Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < totalGlasses; i++) {
            final int x = GAP + i * (cellWidth + GAP);
            final int y = GAP;
            final Glass glass = model.getGlasses().get(i);

            // --- DRAW LAYERS FROM BOTTOM TO TOP ---
            List<Color> toDraw = new ArrayList<>(glass.getLayers());
            Collections.reverse(toDraw); // bottom layer first

            int layerIndex = 0;
            for (final Color c : toDraw) {
                g2.setColor(c);
                g2.fillRect(
                    x,
                    y + (model.getCapacity() - 1 - layerIndex) * perLayerHt,
                    cellWidth,
                    perLayerHt
                );
                layerIndex++;
            }

            // Draw the border (selected/thicker if selected)
            if (i == selected) {
                g2.setColor(Color.MAGENTA);
                g2.setStroke(new BasicStroke(5)); // 5 pixels thick
            } else {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1)); // 1 pixel for normal
            }
            g2.drawRect(x, y, cellWidth, cellHeight);
        }
        // Optionally, reset stroke after the loop:
        g2.setStroke(new BasicStroke(1));
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            model.getNumGlasses() * CELL_WIDTH,
            model.getCapacity() * LAYER_BASE_HEIGHT + EXTRA_PANEL_HEIGHT_PAD
        );
    }

        public void setGlassClickListener(final GlassClickListener listener) {
        this.clickListener = listener;
    }

}
