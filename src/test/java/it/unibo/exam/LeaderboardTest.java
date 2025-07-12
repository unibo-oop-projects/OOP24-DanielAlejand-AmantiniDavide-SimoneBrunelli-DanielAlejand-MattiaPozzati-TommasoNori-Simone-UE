package it.unibo.exam;

import it.unibo.exam.model.leaderboard.LeaderboardEntry;
import it.unibo.exam.model.leaderboard.LeaderboardManage;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeaderboardTest {

    private static final String PLAYER_NAME = "Player1";
    private static final String PLAYER_NAME_2 = "Player2";
    private static final String PLAYER_NAME_3 = "Player3";
    private static final int SCORE_1 = 1000;
    private static final int SCORE_2 = 800;
    private static final int SCORE_3 = 1200;
    private static final int HIGH_SCORE = 900;
    private static final int LOW_SCORE = 80;
    private static final int TIME_1 = 120;
    private static final int TIME_2 = 150;
    private static final int TIME_3 = 100;

    private LeaderboardManage leaderboardManager;

    @BeforeEach
    void setUp() {
        leaderboardManager = new LeaderboardManage();
        leaderboardManager.clear();
    }

    @Test
    void testLeaderboardEntryCreation() {
        final LocalDateTime date = LocalDateTime.now();
        final LeaderboardEntry entry = new LeaderboardEntry(PLAYER_NAME, SCORE_1, TIME_1, date);
        assertEquals(PLAYER_NAME, entry.getPlayerName());
        assertEquals(SCORE_1, entry.getScore());
        assertEquals(TIME_1, entry.getTotalTime());
        assertEquals(date, entry.getDate());
    }

    @Test
    void testLeaderboardEntryComparison() {
        final LocalDateTime date = LocalDateTime.now();
        final LeaderboardEntry entry1 = new LeaderboardEntry(PLAYER_NAME, SCORE_1, TIME_1, date);
        final LeaderboardEntry entry2 = new LeaderboardEntry(PLAYER_NAME_2, SCORE_2, TIME_2, date);
        final LeaderboardEntry entry3 = new LeaderboardEntry(PLAYER_NAME_3, SCORE_3, TIME_3, date);

        // Higher score should be "less than" (for descending order)
        assertTrue(entry3.getScore() > entry1.getScore());
        assertTrue(entry1.getScore() > entry2.getScore());
        assertTrue(entry2.getScore() < entry3.getScore());
    }

    @Test
    void testLeaderboardManagerAddScore() {
        final boolean added = leaderboardManager.addScore(PLAYER_NAME, SCORE_1, TIME_1);
        assertTrue(added);
        final var entries = leaderboardManager.getTop10();
        assertEquals(1, entries.size());
        assertEquals(PLAYER_NAME, entries.get(0).getPlayerName());
        assertEquals(SCORE_1, entries.get(0).getScore());
    }

    @Test
    void testLeaderboardManagerSorting() {
        leaderboardManager.addScore(PLAYER_NAME, SCORE_1, TIME_1);
        leaderboardManager.addScore(PLAYER_NAME_2, SCORE_2, TIME_2);
        leaderboardManager.addScore(PLAYER_NAME_3, SCORE_3, TIME_3);

        final var entries = leaderboardManager.getTop10();
        assertEquals(3, entries.size());

        // Should be sorted in descending order (highest first)
        assertEquals(SCORE_3, entries.get(0).getScore());
        assertEquals(SCORE_1, entries.get(1).getScore());
        assertEquals(SCORE_2, entries.get(2).getScore());
    }

    @Test
    void testLeaderboardManagerTopScores() {
        leaderboardManager.addScore(PLAYER_NAME, HIGH_SCORE, TIME_1);
        leaderboardManager.addScore(PLAYER_NAME_2, LOW_SCORE, TIME_2);

        final var topScores = leaderboardManager.getTop10();
        assertEquals(2, topScores.size());
        assertEquals(HIGH_SCORE, topScores.get(0).getScore());
    }

    @Test
    void testLeaderboardManagerEmpty() {
        final var entries = leaderboardManager.getTop10();
        assertTrue(entries.isEmpty());
    }

    @Test
    void testLeaderboardManagerSize() {
        leaderboardManager.addScore(PLAYER_NAME, SCORE_1, TIME_1);
        leaderboardManager.addScore(PLAYER_NAME_2, SCORE_2, TIME_2);

        assertEquals(2, leaderboardManager.getSize());
    }
}
