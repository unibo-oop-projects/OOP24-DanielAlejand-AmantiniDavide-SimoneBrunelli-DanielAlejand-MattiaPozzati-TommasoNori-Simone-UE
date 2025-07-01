package it.unibo.exam.controller.minigame.bar;

import it.unibo.exam.model.entity.minigame.bar.BarModel;
import it.unibo.exam.model.entity.minigame.bar.PuzzleListener;
import it.unibo.exam.view.bar.BarPanel;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.controller.minigame.bar.strategy.*;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

/**
 * A “Sort & Serve” bar‐puzzle minigame.
 * Displays glasses of mixed colored layers that the player
 * must pour until each glass is uniform.
 * Fires a callback on completion.
 * Allows restart ('R') with the original shuffle for fairness.
 * Now includes a flexible points system via Strategy and Decorator.
 */
public final class BarMinigame implements Minigame {

    private JFrame frame;
    private MinigameCallback callback;
    private long initialSeed;        // The seed used to create the initial puzzle
    private final int capacity = 4;  // Number of layers per glass
    private final int totalGlasses = 6; // Four colors, two empty
    private final int roomID = 1; // Unique identifier for this minigame

    // Score tracking
    private int moveCount = 0;
    private long startTimeMillis = 0;

    // Scoring strategy (can be injected or use default)
    private final ScoringStrategy scoringStrategy;

    /** No‐arg constructor for factory instantiation (uses default scoring) */
    public BarMinigame() {
        this(
        new TimeBonusDecorator(
            new CapDecorator(
                new TieredScoringStrategy(),
                100 // max points
            ),
            30, // seconds for the time bonus
            10  // bonus points if under threshold
        )
    );
    }

    /** Full constructor allows custom scoring strategy */
    public BarMinigame(ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final JFrame parent, final MinigameCallback callback) {
        this.callback = callback;
        this.initialSeed = new Random().nextLong();

        // Build and show the initial puzzle panel using the random seed
        BarPanel panel = buildAndShowPanel(initialSeed);

        // Set up the game window
        frame = new JFrame(getName());
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Track time and moves
        this.moveCount = 0;
        this.startTimeMillis = System.currentTimeMillis();

        // Bind 'R' to restart the puzzle with the same shuffle (not a new random one)
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");
        frame.getRootPane().getActionMap()
            .put("restart", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frame.getContentPane().getComponentCount() > 0) {
                        java.awt.Component current = frame.getContentPane().getComponent(0);
                        if (current instanceof BarPanel) {
                            restart((BarPanel) current);
                        }
                    }
                }
            });
    }

    /**
     * Restarts the puzzle panel with the original shuffle/seed.
     * Keeps the starting state consistent, for fairness and repeatability.
     *
     * @param oldPanel the panel to remove before adding the new one
     */
    private void restart(BarPanel oldPanel) {
        frame.remove(oldPanel);

        // Rebuild the model and panel using the same seed as the original puzzle
        BarPanel panel = buildAndShowPanel(initialSeed);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();

        // Reset score tracking
        this.moveCount = 0;
        this.startTimeMillis = System.currentTimeMillis();
    }

    /**
     * Builds and wires up a BarPanel and its model, with the given seed.
     *
     * @param seed the shuffle seed to use for puzzle state
     * @return a new, ready-to-use BarPanel
     */
    private BarPanel buildAndShowPanel(final long seed) {
        final BarModel model = new BarModel.Builder()
            .numGlasses(totalGlasses)
            .capacity(capacity)
            .colors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
            .shuffleSeed(seed)
            .shuffleStrategy(new RandomShuffleStrategy()) // or SolvableShuffleStrategy()
            .build();

        final BarPanel panel = new BarPanel(model);

        // Listen for glass clicks (selection/pour logic)
        panel.setGlassClickListener(idx -> {
            if (panel.getSelected() < 0) {
                panel.setSelected(idx);
            } else {
                if (model.attemptPour(panel.getSelected(), idx)) {
                    moveCount++; // Only count valid pours
                }
                panel.clearSelection();
            }
            panel.repaint();
        });

        // Listen for model events (Observer pattern)
        model.addListener(new PuzzleListener() {
            @Override
            public void onPoured(int from, int to) {
                SwingUtilities.invokeLater(panel::repaint);
            }
            @Override
            public void onCompleted() {
                long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
                int elapsedSeconds = (int) (elapsedMillis / 1000);
                int score = scoringStrategy.calculate(elapsedSeconds, roomID);
                JOptionPane.showMessageDialog(frame,
                        "Puzzle completed!\nMoves: " + moveCount +
                        "\nTime: " + elapsedSeconds + " seconds" +
                        "\nScore: " + score);
                BarMinigame.this.callback.onComplete(true, score);
                stop();
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
