package it.unibo.exam.controller.minigame.bar;


import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.PuzzleListener;
import it.unibo.exam.view.bar.BarPanel;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.controller.minigame.bar.strategy.RandomShuffleStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;


/**
 * A “Sort & Serve” bar‐puzzle minigame.
 * Displays four glasses of mixed colored layers that the player
 * must pour until each glass is uniform.
 * Fires a callback on completion.
 */
public final class BarMinigame implements Minigame {

    private JFrame frame;
    private MinigameCallback callback;

    /** No‐arg constructor for factory instantiation. */
    public BarMinigame() { }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final JFrame parent, final MinigameCallback callback) {
        this.callback = callback;

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
                BarMinigame.this.callback.onComplete(true, 0);
                stop();
            }
        });

        frame = new JFrame(getName());
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Sort & Serve";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Pour colored layers so each glass is uniform.";
    }
}
