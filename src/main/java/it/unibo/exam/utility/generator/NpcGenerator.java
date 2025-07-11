package it.unibo.exam.utility.generator;

import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.RoamingNpc;
import it.unibo.exam.model.entity.MovementStrategy;
import it.unibo.exam.model.entity.RandomWalkStrategy;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;

/**
 * NPC generator.
 */
public final class NpcGenerator extends EntityGenerator<Npc> {

    private static final String[] NAMES = {
        "Giardiniere Mario",    // room 1
        "Prof.ssa Bianchi",     // room 2
        "R2D2",                 // room 3
        "Coach Luca",           // room 4
        "Barista Anna",         // room 5
    };

    private static final String[] DESCRIPTIONS = {
        "Il giardiniere dell'università, esperto di piante e natura.",
        "Docente di matematica, appassionata e disponibile.",
        "R2D2 sempre pronto a risolvere problemi.",
        "Allenatore energico e motivante.",
        "Barista simpatica che conosce tutti gli studenti.",
    };

    private static final String[] DIALOGUES = {
        """
        Benvenuto nel giardino! Raccogli tutte le gocce d'acqua con la bottiglia per aiutare le piante.
        Muovi la bottiglia utilizzando i tasti A e D per spostarla a sinistra e a destra,
        ma fai attenzione! Se lasci cadere anche solo 3 gocce avrai perso!
        Raccogliene 10 per completare il minigioco
        """,
        "Ciao! Risolvi il quiz per dimostrare le tue conoscenze.",
        "Bzz bzz! Trova l'uscita dal labirinto del laboratorio informatico.",
        "Pronto per una sfida fisica? Colpisci tutti i dischi per vincere!",
        "Ciao! Mescola i drink correttamente per servire i clienti del bar.",
    };

    /**
     * Constructor for NpcGenerator.
     *
     * @param environmentSize the size of the environment
     */
    public NpcGenerator(final Point2D environmentSize) {
        super(environmentSize);
    }

    /**
     * Generates an interactable NPC based on the given room ID.
     * Expects room IDs 1–5; maps to index ID-1 in the arrays.
     *
     * @param id the ID of the room (1–5)
     * @return the generated NPC
     * @throws IllegalArgumentException if id < 1 or id > NAMES.length
     */
    @Override
    public Npc generate(final int id) {
        if (id < 1 || id > NAMES.length) {
            throw new IllegalArgumentException("Invalid room ID for NPC: " + id);
        }
        final int idx = id - 1;                              // ADJUST INDEX
        final String name        = NAMES[idx];
        final String description = DESCRIPTIONS[idx];
        final String dialogue    = DIALOGUES[idx];
        return new Npc(super.getEnv(), name, description, dialogue);
    }

    /**
     * Generates a single non-interactable, roaming NPC for the given room.
     *
     * @param room the room to populate
     * @return a RoamingNpc that will wander within the environment bounds
     */
    public RoamingNpc generateRoamingNpc(final Room room) {
        Point2D start = new Point2D(
            50 + 100 * room.getId(),
            80
        );
        MovementStrategy strategy = new RandomWalkStrategy(super.getEnv());
        return new RoamingNpc(start, super.getEnv(), strategy);
    }
}
