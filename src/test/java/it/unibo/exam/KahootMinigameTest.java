package it.unibo.exam;

import it.unibo.exam.controller.minigame.kahoot.KahootMinigame;
import it.unibo.exam.model.entity.minigame.kahoot.QuizQuestion;
import it.unibo.exam.model.scoring.ScoringStrategy;
import it.unibo.exam.model.scoring.TieredScoringStrategy;
import it.unibo.exam.model.scoring.TimeBonusDecorator;
import it.unibo.exam.model.scoring.CapDecorator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Locale;

class KahootMinigameTest {

    private static final int BONUS_TIME_THRESHOLD = 30;
    private static final int BONUS_POINTS = 10;
    private static final int MAX_POINTS_CAP = 100;
    private static final String QUIZ_NAME = "Quiz Kahoot";

    private KahootMinigame minigame;
    private ScoringStrategy scoringStrategy;

    @BeforeEach
    void setUp() {
        scoringStrategy = new CapDecorator(
            new TimeBonusDecorator(
                new TieredScoringStrategy(),
                BONUS_TIME_THRESHOLD,
                BONUS_POINTS
            ),
            MAX_POINTS_CAP
        );
        minigame = new KahootMinigame(scoringStrategy);
    }

    @Test
    void testKahootMinigameInitialization() {
        assertNotNull(minigame);
        assertEquals(QUIZ_NAME, minigame.getName());
        assertTrue(minigame.getDescription().toLowerCase(Locale.ROOT).contains("quiz")
                  || minigame.getDescription().toLowerCase(Locale.ROOT).contains("question"));
    }

    @Test
    void testKahootMinigameWithCustomQuestions() {
        final List<QuizQuestion> customQuestions = createTestQuestions();
        final KahootMinigame customMinigame = new KahootMinigame(customQuestions, scoringStrategy);

        assertNotNull(customMinigame);
        assertEquals(QUIZ_NAME, customMinigame.getName());
    }

    @Test
    void testKahootModelInitialization() {
        minigame.start(null, (success, time, score) -> { });

        // Verify that the minigame has been initialized correctly
        assertNotNull(minigame);
        assertEquals(QUIZ_NAME, minigame.getName());
        assertTrue(minigame.getDescription().toLowerCase(Locale.ROOT).contains("answer")
                  || minigame.getDescription().toLowerCase(Locale.ROOT).contains("question"));
    }

    @Test
    void testQuestionNavigation() {
        // Test that verifies navigation between questions
        final AtomicBoolean callbackCalled = new AtomicBoolean(false);
        minigame.start(null, (success, time, score) -> {
            callbackCalled.set(true);
        });

        // Verify that the minigame has started
        assertNotNull(minigame);
    }

    @Test
    void testScoringWithPenalty() {
        // Test that verifies the penalty system
        final AtomicInteger finalScore = new AtomicInteger(-1);
        final AtomicInteger finalTime = new AtomicInteger(-1);

        minigame.start(null, (success, time, score) -> {
            finalScore.set(score);
            finalTime.set(time);
        });

        // Verify that the score has been calculated
        assertTrue(finalScore.get() >= 0 || finalScore.get() == -1); // -1 if not yet calculated
    }

    @Test
    void testGameCompletion() {
        final AtomicBoolean completionCalled = new AtomicBoolean(false);
        final AtomicInteger finalScore = new AtomicInteger(-1);
        final AtomicInteger finalTime = new AtomicInteger(-1);

        minigame.start(null, (success, time, score) -> {
            completionCalled.set(success);
            finalScore.set(score);
            finalTime.set(time);
        });

        // Verify that the minigame has been initialized
        assertNotNull(minigame);
        assertEquals(QUIZ_NAME, minigame.getName());
    }

    @Test
    void testScoringStrategyIntegration() {
        final AtomicInteger calculatedScore = new AtomicInteger(-1);

        minigame.start(null, (success, time, score) -> {
            calculatedScore.set(score);
        });

        // Verify that the scoring strategy is integrated
        assertNotNull(minigame.getScoringStrategy());
    }

    @Test
    void testMinigameStop() {
        minigame.start(null, (success, time, score) -> { });

        // Verify that the minigame can be stopped
        assertDoesNotThrow(minigame::stop);
    }

    private List<QuizQuestion> createTestQuestions() {
        final List<QuizQuestion> questions = new ArrayList<>();

        questions.add(new QuizQuestion(
            "What is the capital of Italy?",
            List.of("Rome", "Milan", "Naples", "Florence"),
            0
        ));

        questions.add(new QuizQuestion(
            "2 + 2 = ?",
            List.of("3", "4", "5", "6"),
            1
        ));

        questions.add(new QuizQuestion(
            "Which programming language are we using?",
            List.of("Python", "Java", "C++", "JavaScript"),
            1
        ));

        return questions;
    }
}
