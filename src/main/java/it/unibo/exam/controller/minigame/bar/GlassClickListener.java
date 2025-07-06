package it.unibo.exam.controller.minigame.bar;

/**
 * Listener for glass click events in the Sort & Serve puzzle.
 * Receives callbacks when the player clicks on a glass in the UI.
 */
public interface GlassClickListener {

    /**
     * Invoked when the player clicks a glass.
     *
     * @param index the zero-based index of the clicked glass
     */
    void glassClicked(int index);
}
