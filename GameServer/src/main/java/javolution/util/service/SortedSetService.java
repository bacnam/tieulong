package javolution.util.service;

import java.util.SortedSet;

public interface SortedSetService<E> extends SetService<E>, SortedSet<E> {
    SortedSetService<E> headSet(E paramE);

    SortedSetService<E> subSet(E paramE1, E paramE2);

    SortedSetService<E> tailSet(E paramE);

    SortedSetService<E> threadSafe();
}

