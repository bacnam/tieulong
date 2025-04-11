package javolution.util.function;

import java.util.Iterator;

public interface Iteration<E> {
    void run(Iterator<E> paramIterator);

    public static interface Sequential<E> extends Iteration<E> {
    }

    public static interface Mutable<E> extends Iteration<E> {
    }
}

