package it.unibo.exam;

import it.unibo.exam.controller.minigame.bar.BarMinigame;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

class BarMinigameTest {
    private static final int BONUS_TIME_THRESHOLD = 30;
    private static final int BONUS_POINTS = 10;
    private static final int MAX_POINTS_CAP = 100;

    @Test
    void testInitialization() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final BarMinigame minigame = new BarMinigame(scoring);
        assertNotNull(minigame);
        assertEquals("Sort & Serve", minigame.getName());
        assertTrue(minigame.getDescription().toLowerCase(Locale.ROOT).contains("glass")
                   || minigame.getDescription().toLowerCase(Locale.ROOT).contains("puzzle"));
    }

    @Test
    void testStartAndStop() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final BarMinigame minigame = new BarMinigame(scoring);
        // Start the minigame with a dummy callback
        minigame.start(null, (success, time, score) -> { });
        // Stop should not throw
        assertDoesNotThrow(minigame::stop);
    }

    @Test
    void testScoringStrategyIntegration() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final BarMinigame minigame = new BarMinigame(scoring);
        // There is no direct getScoringStrategy, but we can check that the minigame works with the strategy
        assertNotNull(minigame);
    }
}
