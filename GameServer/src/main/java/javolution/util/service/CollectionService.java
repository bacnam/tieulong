package javolution.util.service;

import java.io.Serializable;
import java.util.Collection;
import javolution.util.function.Equality;
import javolution.util.function.Splittable;

public interface CollectionService<E> extends Collection<E>, Splittable<CollectionService<E>>, Serializable, Cloneable {
  CollectionService<E> clone() throws CloneNotSupportedException;

  Equality<? super E> comparator();

  CollectionService<E> threadSafe();
}

