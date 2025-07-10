package it.unibo.exam;

import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import it.unibo.exam.model.scoring.CapDecorator;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoringStrategyTest {

    private static final int FAST_TIME = 10;
    private static final int BONUS_THRESHOLD = 30;
    private static final int BONUS_POINTS = 30;
    private static final int CAP_LIMIT = 150;
    private static final int ROOM_ID = 1;

    @Test
    void testTieredScoringStrategy() {
        final ScoringStrategy strategy = new TieredScoringStrategy();
        final int score = strategy.calculate(FAST_TIME, ROOM_ID);
        assertTrue(score > 0);
    }

    @Test
    void testCapDecorator() {
        final ScoringStrategy baseStrategy = new TieredScoringStrategy();
        final ScoringStrategy cappedStrategy = new CapDecorator(baseStrategy, CAP_LIMIT);

        final int score = cappedStrategy.calculate(FAST_TIME, ROOM_ID);
        assertTrue(score <= CAP_LIMIT);
    }

    @Test
    void testTimeBonusDecorator() {
        final ScoringStrategy baseStrategy = new TieredScoringStrategy();
        final ScoringStrategy timeBonusStrategy = new TimeBonusDecorator(baseStrategy, BONUS_THRESHOLD, BONUS_POINTS);

        final int score = timeBonusStrategy.calculate(FAST_TIME, ROOM_ID);
        final int baseScore = baseStrategy.calculate(FAST_TIME, ROOM_ID);
        assertEquals(baseScore + BONUS_POINTS, score);
    }

    @Test
    void testCombinedDecorators() {
        final ScoringStrategy baseStrategy = new TieredScoringStrategy();
        final ScoringStrategy timeBonusStrategy = new TimeBonusDecorator(baseStrategy, BONUS_THRESHOLD, BONUS_POINTS);
        final ScoringStrategy finalStrategy = new CapDecorator(timeBonusStrategy, CAP_LIMIT);

        final int score = finalStrategy.calculate(FAST_TIME, ROOM_ID);
        assertTrue(score > 0);
        assertTrue(score <= CAP_LIMIT);
    }
} 
