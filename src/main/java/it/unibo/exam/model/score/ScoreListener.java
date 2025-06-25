package it.unibo.exam.model.score;

/**
 * Notified whenever the player's total score changes.
 */
public interface ScoreListener {
    /**
     * @param newTotal the updated total score
     */
    void onScoreChanged(int newTotal);
}
