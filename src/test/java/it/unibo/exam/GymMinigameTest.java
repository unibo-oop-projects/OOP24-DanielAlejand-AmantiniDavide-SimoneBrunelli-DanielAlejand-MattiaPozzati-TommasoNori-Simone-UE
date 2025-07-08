package it.unibo.exam;

import it.unibo.exam.controller.minigame.gym.GymMinigame;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.model.scoring.ScoringStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class GymMinigameTest {
    private static final int BONUS_TIME_THRESHOLD = 30;
    private static final int BONUS_POINTS = 10;
    private static final int MAX_POINTS_CAP = 100;
    private static final int COMPLETION_SCORE = 50;

    @Test
    void testInitializationAndCompletion() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final GymMinigame minigame = new GymMinigame(scoring);
        assertNotNull(minigame.getModel());
        assertEquals(0, minigame.getModel().getScore());
    }

    @Test
    void testOnGameCompletedCallback() {
        final ScoringStrategy scoring = new CapDecorator(
            new TimeBonusDecorator(new TieredScoringStrategy(), BONUS_TIME_THRESHOLD, BONUS_POINTS), MAX_POINTS_CAP);
        final GymMinigame minigame = new GymMinigame(scoring);
        final AtomicBoolean called = new AtomicBoolean(false);
        final AtomicInteger score = new AtomicInteger(-1);
        minigame.start(null, (success, value) -> {
            called.set(success);
            score.set(value);
        });
        // Simula completamento
        minigame.onGameCompleted(COMPLETION_SCORE);
        assertTrue(called.get());
        assertTrue(score.get() >= 0);
    }
}
