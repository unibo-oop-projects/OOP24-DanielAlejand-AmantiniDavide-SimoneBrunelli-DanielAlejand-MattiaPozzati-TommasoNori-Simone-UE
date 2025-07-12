package it.unibo.exam;

import it.unibo.exam.controller.minigame.lab.MazeMinigame;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

class MazeMinigameTest {
    private static final int BONUS_TIME_THRESHOLD = 30;
    private static final int BONUS_POINTS = 20;
    private static final int MAX_POINTS_CAP = 120;

    @Test
    void testInitialization() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final MazeMinigame minigame = new MazeMinigame(scoring);
        assertNotNull(minigame);
        assertTrue(minigame.getName().toLowerCase(Locale.ROOT).contains("maze"));
        assertTrue(minigame.getDescription().toLowerCase(Locale.ROOT).contains("run")
                   || minigame.getDescription().toLowerCase(Locale.ROOT).contains("square"));
    }

    @Test
    void testStartAndStop() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final MazeMinigame minigame = new MazeMinigame(scoring);
        // Start the minigame with a dummy callback
        minigame.start(null, (success, time, score) -> { });
        // Stop should not throw
        assertDoesNotThrow(minigame::stop);
    }

    @Test
    void testScoringStrategyIntegration() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final MazeMinigame minigame = new MazeMinigame(scoring);
        assertNotNull(minigame);
    }
}
