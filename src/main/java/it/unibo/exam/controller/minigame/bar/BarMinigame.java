package it.unibo.exam.controller.minigame.bar;

import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.PuzzleListener;
import it.unibo.exam.view.bar.BarPanel;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.controller.minigame.bar.strategy.RandomShuffleStrategy;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.model.scoring.TieredScoringStrategy;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Random;

/**
 * A “Sort & Serve” bar‐puzzle minigame.
 * Displays glasses of mixed colored layers that the player
 * must pour until each glass is uniform.
 * Fires a callback on completion.
 * Allows restart ('R') with the original shuffle for fairness.
 * Now includes a flexible points system via Strategy and Decorator.
 */
public final class BarMinigame implements Minigame {

    private static final int BONUS_TIME_THRESHOLD_SECONDS = 30;
    private static final int BONUS_POINTS                 = 10;
    private static final int MAX_POINTS_CAP               = 100;
    private static final int CAPACITY       = 5;
    private static final int TOTAL_GLASSES  = 6;
    private static final int ROOM_ID        = 4;
    private static final int FRAME_WIDTH    = 1000; // Preferred width for the minigame window
    private static final int FRAME_HEIGHT   = 600;  // Preferred height for the minigame window
    private static final Random RNG = new Random();

    private JFrame             frame;
    private MinigameCallback   callback;
    private long               initialSeed;
    private int                moveCount;
    private long               startTimeMillis;
    private final ScoringStrategy scoringStrategy;

    /**
     * No‐arg constructor for factory instantiation (uses default scoring).
     */
    public BarMinigame() {
        this(
            new CapDecorator(
                new TimeBonusDecorator(
                    new TieredScoringStrategy(),
                    BONUS_TIME_THRESHOLD_SECONDS,
                    BONUS_POINTS
                ),
                MAX_POINTS_CAP
            )
        );
    }

    /**
     * Full constructor allows custom scoring strategy.
     *
     * @param scoringStrategy the strategy used to compute final score
     */
    public BarMinigame(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
            "scoringStrategy must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final JFrame parent, final MinigameCallback callback) {
        this.callback    = Objects.requireNonNull(callback, "callback must not be null");
        this.initialSeed = RNG.nextLong();

        final BarPanel panel = buildAndShowPanel(initialSeed);

        frame = new JFrame(getName());
        frame.add(panel);

        // --- Set preferred window size and make it non-resizable ---
        frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setResizable(false);

        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        moveCount       = 0;
        startTimeMillis = System.currentTimeMillis();

        frame.getRootPane()
            .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");
        frame.getRootPane()
            .getActionMap()
            .put("restart", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (frame.getContentPane().getComponentCount() > 0) {
                        final java.awt.Component current =
                            frame.getContentPane().getComponent(0);
                        if (current instanceof BarPanel) {
                            restart((BarPanel) current);
                        }
                    }
                }
            });
    }


    /**
     * Restarts the puzzle panel with the original shuffle/seed.
     *
     * @param oldPanel the panel to remove before adding the new one
     */
    private void restart(final BarPanel oldPanel) {
        frame.remove(oldPanel);
        final BarPanel panel = buildAndShowPanel(initialSeed);
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();

        moveCount       = 0;
        startTimeMillis = System.currentTimeMillis();
    }

    /**
     * Builds and wires up a BarPanel and its model, with the given seed.
     *
     * @param seed the shuffle seed to use for puzzle state
     * @return a new, ready-to-use BarPanel
     */
    private BarPanel buildAndShowPanel(final long seed) {
        final BarModel model = new BarModel.Builder()
            .numGlasses(TOTAL_GLASSES)
            .capacity(CAPACITY)
            .colors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
            .shuffleSeed(seed)
            .shuffleStrategy(new RandomShuffleStrategy())
            .build();

        final BarPanel panel = new BarPanel(model);

        panel.setGlassClickListener(idx -> {
            if (panel.getSelected() < 0) {
                panel.setSelected(idx);
            } else {
                if (model.attemptPour(panel.getSelected(), idx)) {
                    moveCount++;
                }
                panel.clearSelection();
            }
            panel.repaint();
        });

        model.addListener(new PuzzleListener() {
            @Override
            public void onPoured(final int from, final int to) {
                SwingUtilities.invokeLater(panel::repaint);
            }

            @Override
            public void onCompleted() {
                final long elapsedMillis  = System.currentTimeMillis() - startTimeMillis;
                final int  elapsedSeconds = (int) (elapsedMillis / 1_000L);
                final int  score          = scoringStrategy.calculate(elapsedSeconds, ROOM_ID);

                JOptionPane.showMessageDialog(frame,
                    "Puzzle completed!\nMoves: " + moveCount
                    + "\nTime: "   + elapsedSeconds + " seconds"
                    + "\nScore: "  + score
                );
                BarMinigame.this.callback.onComplete(true, elapsedSeconds, score);
                BarMinigame.this.stop();
            }
        });
        return panel;
    }

    /**
     * {@inheritDoc}
     * Disposes of the puzzle window.
     */
    @Override
    public void stop() {
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the name of this minigame
     */
    @Override
    public String getName() {
        return "Sort & Serve";
    }

    /**
     * {@inheritDoc}
     *
     * @return a short description of this minigame
     */
    @Override
    public String getDescription() {
        return "Pour colored layers so each glass is uniform.";
    }
}
