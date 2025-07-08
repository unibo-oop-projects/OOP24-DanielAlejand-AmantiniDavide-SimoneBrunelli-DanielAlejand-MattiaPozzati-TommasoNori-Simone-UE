package it.unibo.exam.model.leaderboard;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single entry in the leaderboard.
 * Contains player information and achievement data.
 */
public final class LeaderboardEntry {

    private final String playerName;
    private final int score;
    private final int totalTime;
    private final LocalDateTime date;

    /**
     * Creates a new leaderboard entry.
     * 
     * @param playerName the name of the player
     * @param score the total score achieved
     * @param totalTime the total time taken in seconds
     * @param date the date and time when the score was achieved
     */
    public LeaderboardEntry(final String playerName, final int score, 
                           final int totalTime, final LocalDateTime date) {
        this.playerName = playerName;
        this.score = score;
        this.totalTime = totalTime;
        this.date = date;
    }

    /**
     * Gets the player's name.
     * 
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the total score.
     * 
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the total time taken.
     * 
     * @return the time in seconds
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * Gets the date when the score was achieved.
     * 
     * @return the date and time
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%s - %d pts", playerName, score);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final LeaderboardEntry other = (LeaderboardEntry) obj;
        return score == other.score 
            && totalTime == other.totalTime 
            && Objects.equals(playerName, other.playerName) 
            && Objects.equals(date, other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, score, totalTime, date);
    }
}
