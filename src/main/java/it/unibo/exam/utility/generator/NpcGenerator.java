package it.unibo.exam.utility.generator;

import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.RoamingNpc;                   // ADDED
import it.unibo.exam.model.entity.MovementStrategy;             // ADDED
import it.unibo.exam.model.entity.RandomWalkStrategy;           // ADDED
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;

/**
 * NPC generator.
 */
public final class NpcGenerator extends EntityGenerator<Npc> {

    private static final String[] NAMES = {
        "Giardiniere Mario",         // 1: Giardino
        "Prof.ssa Bianchi",          // 2: Aula universitaria
        "R2D2",                      // 3: Laboratorio informatico
        "Coach Luca",                // 4: Palestra
        "Barista Anna",              // 5: Bar
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
        Raccogline 10 per completare il minigioco
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
     * Generates an interactable NPC based on the given ID.
     *
     * @param id the ID of the NPC to generate
     * @return the generated NPC
     */
    @Override
    public Npc generate(final int id) {
        final String name       = NAMES[id];
        final String description= DESCRIPTIONS[id];
        final String dialogue   = DIALOGUES[id];
        return new Npc(super.getEnv(), name, description, dialogue);
    }

    // ── NEW METHOD ─────────────────────────────────────────────────────────────

    /**
     * Generates a single non-interactable, roaming NPC for the given room.
     *
     * @param room the room to populate
     * @return a RoamingNpc that will wander within the environment bounds
     */
    public RoamingNpc generateRoamingNpc(final Room room) {
        // pick a spawn point (here: staggered by room ID, feel free to customize)
        Point2D start = new Point2D(
            50 + 100 * room.getId(),
            80
        );

        // movement strategy knows the world bounds from getEnv()
        MovementStrategy strategy = new RandomWalkStrategy(super.getEnv());

        return new RoamingNpc(start, super.getEnv(), strategy);
    }
}
