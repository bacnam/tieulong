package javolution.util.service;

import javolution.util.function.Equality;
import javolution.util.function.Splittable;

import java.io.Serializable;
import java.util.Collection;

public interface CollectionService<E> extends Collection<E>, Splittable<CollectionService<E>>, Serializable, Cloneable {
    CollectionService<E> clone() throws CloneNotSupportedException;

    Equality<? super E> comparator();

    CollectionService<E> threadSafe();
}

