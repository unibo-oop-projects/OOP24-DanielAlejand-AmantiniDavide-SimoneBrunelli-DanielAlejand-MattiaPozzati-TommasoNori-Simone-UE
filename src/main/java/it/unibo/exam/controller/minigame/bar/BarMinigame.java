package it.unibo.exam.controller.minigame.bar;

import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.PuzzleListener;
import it.unibo.exam.view.bar.BarPanel;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.controller.minigame.bar.strategy.RandomShuffleStrategy;


/**
 * A “Sort & Serve” bar‐puzzle minigame.
 * Displays four glasses of mixed colored layers that the player
 * must pour until each glass is uniform.
 * Fires a callback on completion.
 */
public final class BarMinigame {

    private JFrame frame;
    private final MinigameCallback callback;

    /**
     * Creates a new BarMinigame.
     *
     * @param callback the callback to invoke when the puzzle completes
     */
    public BarMinigame(final MinigameCallback callback) {
        this.callback = callback;
    }

    /**
     * Starts the puzzle by building the model, wiring up listeners,
     * and showing the UI in its own JFrame.
     *
     * @param parent the parent window for centering this minigame frame
     */
    public void start(final JFrame parent) {
        final BarModel model = new BarModel.Builder()
            .numGlasses(4)
            .capacity(4)
            .colors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
            .shuffleSeed(System.currentTimeMillis())
            .shuffleStrategy(new RandomShuffleStrategy())
            .build();

        final BarPanel panel = new BarPanel(model);

        // repaint on each pour, notify on completion
        model.addListener(new PuzzleListener() {
            @Override
            public void onPoured(final int from, final int to) {
                SwingUtilities.invokeLater(panel::repaint);
            }
            @Override
            public void onCompleted() {
                callback.onComplete(true, 0);
                stop();
            }
        });

        frame = new JFrame("Sort & Serve");
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Stops the puzzle, disposing its window.
     */
    public void stop() {
        if (frame != null) {
            frame.dispose();
        }
    }
}
