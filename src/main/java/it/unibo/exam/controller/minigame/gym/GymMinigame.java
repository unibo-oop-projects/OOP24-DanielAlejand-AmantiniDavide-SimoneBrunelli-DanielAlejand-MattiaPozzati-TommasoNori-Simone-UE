package it.unibo.exam.controller.minigame.gym;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.model.entity.minigame.gym.GymModel;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.view.gym.GymPanel;
import it.unibo.exam.utility.geometry.Point2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Gym minigame.
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "EI_EXPOSE_REP2", "constructor-calls-overridable-method"}, 
justification = "model and keyHandler are safe for broadcasting and not serialized.")
public class GymMinigame implements Minigame {
    private static final int BONUS_TIME_THRESHOLD_SECONDS = 30;
    private static final int BONUS_POINTS                 = 10;
    private static final int MAX_POINTS_CAP               = 100;
    private static final int FRAME_WIDTH    = 1000; // Preferred width for the minigame window
    private static final int FRAME_HEIGHT   = 600;  // Preferred height for the minigame window
    private static final int ROOM_ID = 3;
    private JFrame gameFrame;
    private GymModel model;
    private MinigameCallback onComplete;
    private boolean gameOver;
    private final ScoringStrategy scoringStrategy;

    /**
     * No‐arg constructor for factory instantiation (uses default scoring).
     */
    public GymMinigame() {
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
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public GymMinigame(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
            "scoringStrategy must not be null");
        this.model = new GymModel(new Point2D(FRAME_WIDTH, FRAME_HEIGHT));
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void start(final JFrame parentFrame, final MinigameCallback onComplete) {
        model.setMinigame(this);
        this.onComplete = onComplete;
        this.gameOver = false;
        final GymPanel gamePanel = new GymPanel(model);
        final GymController controller = new GymController(model, gamePanel);
        controller.attachListeners();
        gameFrame = new JFrame(getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setContentPane(gamePanel);
        gameFrame.setResizable(true);
        gameFrame.setSize(model.getBoardWidth(), model.getBoardHeight());
        gameFrame.setLocationRelativeTo(parentFrame);
        gameFrame.setVisible(true);
        gamePanel.requestFocusInWindow();

        // Aggiorna modello e pannello quando la finestra viene ridimensionata
        gameFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension newSize = gameFrame.getContentPane().getSize();
                model.resize(new Point2D(newSize.width, newSize.height));
                gamePanel.repaint();
            }
        });

        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                if (!gameOver && GymMinigame.this.onComplete != null) {
                    GymMinigame.this.onComplete.onComplete(false, 0, 0);
                }
            }
        });
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void stop() {
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public String getName() {
        return "Gym Minigame";
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public String getDescription() {
        return "Colpisci tutti i dischi con il cannone per vincere! Usa mouse e tastiera.";
    }

    /**
     * Should be called when the minigame is completed (i.e., all disks have been removed).
     * Calculates the elapsed time and final score using the scoring strategy, displays a dialog
     * with the results, and notifies the callback if present.
     */
    public void onGameCompleted() {
        if (!gameOver) {
            gameOver = true;
            int elapsedSeconds = 0;
            if (model != null && model.getStartTimeMillis() > 0) {
                elapsedSeconds = (int) ((System.currentTimeMillis() - model.getStartTimeMillis()) / 1000L);
            }
                final int finalScore = scoringStrategy.calculate(elapsedSeconds, /*roomId*/ ROOM_ID); // 3 = Gym
            if (gameFrame != null) {
                JOptionPane.showMessageDialog(gameFrame,
                    "Minigame completed!\nTime: " + elapsedSeconds + " seconds"
                    + "\nScore: " + finalScore
                );
                gameFrame.dispose();
            }
            if (onComplete != null) {
                onComplete.onComplete(true, elapsedSeconds, finalScore);
            }
        }
    }

    /**
     * Returns the GymModel for this minigame.
     * @return the GymModel instance
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "model is safe for broadcasting in this context.")
    public GymModel getModel() {
        return model;
    }
}
