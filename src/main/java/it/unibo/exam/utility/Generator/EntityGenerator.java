package it.unibo.exam.utility.Generator;

import it.unibo.exam.utility.Geometry.Point2D;

public abstract class EntityGenerator<E> implements  Generator<E>{

    final Point2D enviromentSize;

    public EntityGenerator(final Point2D enviromentSize) {
        this.enviromentSize = enviromentSize;
    }

    public abstract E generate(final int id);
    
}
