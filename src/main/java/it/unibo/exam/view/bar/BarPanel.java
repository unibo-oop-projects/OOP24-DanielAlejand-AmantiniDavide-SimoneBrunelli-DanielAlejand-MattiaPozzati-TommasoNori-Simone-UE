package it.unibo.exam.view.bar;

import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.Glass;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * Panel that renders the Sort & Serve bar puzzle and handles mouse input.
 */
public final class BarPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int GAP                    = 20;
    private static final int CELL_WIDTH             = 120;
    private static final int LAYER_BASE_HEIGHT      = 40;
    private static final int EXTRA_PANEL_HEIGHT_PAD = 60;

    private final transient BarModel model;
    private int selected = -1;

    /**
     * @param model the puzzle model to render and interact with
     */
    public BarPanel(final BarModel model) {
        this.model = model;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                final int idx = e.getX() / (getWidth() / model.getNumGlasses());
                if (idx < 0 || idx >= model.getNumGlasses()) {
                    return;
                }
                if (selected < 0) {
                    selected = idx;
                } else {
                    model.attemptPour(selected, idx);
                    selected = -1;
                    repaint();
                }
            }
        });
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

        for (int i = 0; i < totalGlasses; i++) {
            final int x = GAP + i * (cellWidth + GAP);
            final int y = GAP;
            final Glass glass = model.getGlasses().get(i);

            int layerIndex = 0;
            for (final Color c : glass.getLayers()) {
                g.setColor(c);
                g.fillRect(
                    x,
                    y + (model.getCapacity() - 1 - layerIndex) * perLayerHt,
                    cellWidth,
                    perLayerHt
                );
                layerIndex++;
            }

            g.setColor(i == selected ? Color.MAGENTA : Color.BLACK);
            g.drawRect(x, y, cellWidth, cellHeight);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            model.getNumGlasses() * CELL_WIDTH,
            model.getCapacity() * LAYER_BASE_HEIGHT + EXTRA_PANEL_HEIGHT_PAD
        );
    }
}
