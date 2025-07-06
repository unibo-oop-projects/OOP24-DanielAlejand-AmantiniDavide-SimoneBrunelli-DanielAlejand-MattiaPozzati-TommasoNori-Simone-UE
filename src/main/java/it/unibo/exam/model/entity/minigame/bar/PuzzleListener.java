package it.unibo.exam.model.entity.minigame.bar;

/**
 * Receives notifications from the Sort-&-Serve model.
 */
public interface PuzzleListener {

    /**
     * Invoked after a successful pour from one glass into another.
     *
     * @param from the index of the source glass (0-based)
     * @param to   the index of the target glass (0-based)
     */
    void onPoured(int from, int to);

    /**
     * Invoked once when the puzzle reaches completion.
     */
    void onCompleted();
}
