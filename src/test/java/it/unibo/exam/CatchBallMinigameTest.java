package it.unibo.exam;

import it.unibo.exam.controller.minigame.garden.CatchBallMinigame;
import it.unibo.exam.model.scoring.LifeScoringStrategy;
import it.unibo.exam.model.scoring.ScoringStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

class CatchBallMinigameTest {
    @Test
    void testInitialization() {
        final ScoringStrategy scoring = new LifeScoringStrategy();
        final CatchBallMinigame minigame = new CatchBallMinigame(scoring);
        assertNotNull(minigame);
        assertEquals("Garden Minigame", minigame.getName());
        assertTrue(minigame.getDescription().toLowerCase(Locale.ROOT).contains("catch")
                   || minigame.getDescription().toLowerCase(Locale.ROOT).contains("ball"));
    }

    @Test
    void testStartAndStop() {
        final ScoringStrategy scoring = new LifeScoringStrategy();
        final CatchBallMinigame minigame = new CatchBallMinigame(scoring);
        // Start the minigame with a dummy callback
        minigame.start(null, (success, time, score) -> { });
        // Stop should not throw
        assertDoesNotThrow(minigame::stop);
    }

    @Test
    void testScoringStrategyIntegration() {
        final ScoringStrategy scoring = new LifeScoringStrategy();
        final CatchBallMinigame minigame = new CatchBallMinigame(scoring);
        assertNotNull(minigame);
    }
}
