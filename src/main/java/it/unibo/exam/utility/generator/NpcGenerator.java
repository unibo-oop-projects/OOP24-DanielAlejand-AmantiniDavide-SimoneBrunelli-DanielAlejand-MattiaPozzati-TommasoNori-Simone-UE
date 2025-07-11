package it.unibo.exam.utility.generator;

import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.utility.geometry.Point2D;

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
     * @param enviromentSize the size of the environment
     */
    public NpcGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
    }

    /**
     * Generates an NPC based on the given ID.
     *
     * @param id the ID of the NPC to generate
     * @return the generated NPC
     */
    @Override
    public Npc generate(final int id) {
        final String name = NAMES[id];
        final String description = DESCRIPTIONS[id];
        final String dialogue = DIALOGUES[id];
        return new Npc(super.getEnv(), name, description, dialogue);
    }
}
