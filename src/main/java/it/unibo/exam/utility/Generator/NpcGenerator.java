package it.unibo.exam.utility.Generator;

import it.unibo.exam.model.Entity.Npc;
import it.unibo.exam.utility.Geometry.Point2D;

/**
 * Utility class for generating NPCs.
 */
public final class NpcGenerator {

    private static final Point2D NPC_DEFAULT_POSITION = new Point2D(1, 1);

    private static final String[] NAMES = {
        "Professor Oak",
        "Professor Elm",
        "Professor Birch",
        "Professor Rowan",
        "Professor Sycamore",
        "Professor Kukui",
        "Professor Magnolia",
        "Professor Cerise",
    }; // Added trailing comma

    private static final String[] DESCRIPTIONS = {
        "A wise and knowledgeable professor.",
        "An expert in Pokémon research.",
        "A friendly and helpful professor.",
        "A professor with a passion for Pokémon.",
        "A professor who loves to teach.",
        "A professor with a deep understanding of Pokémon.",
        "A professor who is always ready to help.",
        "A professor with a wealth of knowledge.",
    }; // Added trailing comma

    private static final String[] DIALOGUES = {
        "Hello, trainer! Are you ready for your adventure?",
        "Welcome to the world of Pokémon!",
        "I have a special task for you, trainer.",
        "Are you interested in learning more about Pokémon?",
        "Let's embark on an exciting journey together!",
        "I have some valuable information for you.",
        "Do you want to hear about my latest research?",
        "I'm here to help you on your journey.",
    }; // Added trailing comma

    /**
     * Generates an NPC based on the given ID.
     *
     * @param id the ID of the NPC to generate
     * @param enviromentSize @see Entity
     * @return the generated NPC
     */
  	public static Npc generateNpc(final int id, Point2D enviromentSize) {
        final String name = NAMES[id];
        final String description = DESCRIPTIONS[id];
        final String dialogue = DIALOGUES[id];
        return new Npc(NPC_DEFAULT_POSITION,enviromentSize, name, description, dialogue);
    }
}
