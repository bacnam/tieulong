package javolution.util.service;

import java.util.Set;

public interface SetService<E> extends CollectionService<E>, Set<E> {
    SetService<E> threadSafe();
}

