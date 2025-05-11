package it.unibo.exam.model.Entity;

import it.unibo.exam.utility.Geometry.Point2D;

/**
 * Represents a non-playable character (NPC) in the game.
 */
public final class Npc extends Entity {

    private static final int SCALE_FACTOR = 20; // Reordered static variable
    private final String name;
    private final String description;
    private final String dialogue;

    /**
     * Constructor for Npc.
     *
     * @param position the position of the NPC
     * @param enviromentSize the size of the environment
     * @param name the name of the NPC
     * @param description the description of the NPC
     * @param dialogue the dialogue of the NPC
     */
    public Npc(final Point2D position, final Point2D enviromentSize, final String name, 
               final String description, final String dialogue) {
        super(position, SCALE_FACTOR, enviromentSize);
        this.name = name;
        this.description = description;
        this.dialogue = dialogue;
    }

    /**
     * @return the name of the NPC
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of the NPC
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the dialogue of the NPC
     */
    public String getDialogue() {
        return dialogue;
    }

    /**
     * Defines the interaction behavior with the NPC.
     */
    public void interact() {
        // Interaction logic can be implemented here
    }
}
