package it.unibo.exam.controller.minigame.bar;

import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.view.bar.BarPanel;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * A “Sort & Serve” puzzle: pour colored layers between glasses
 * until each glass holds only one color.
 */
public final class BarMinigame {

    private JFrame frame;

    /**
     * Launches the bar-puzzle window.
     */
    public void start() {
        final BarModel model = new BarModel.Builder()
            .numGlasses(4)
            .capacity(4)
            .colors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
            .shuffleSeed(System.currentTimeMillis())
            .build();

        final BarPanel panel = new BarPanel(model);

        frame = new JFrame("Sort & Serve");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Stops the puzzle, disposing of its window.
     */
    public void stop() {
        if (frame != null) {
            frame.dispose();
        }
    }
}
