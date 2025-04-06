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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/service/CollectionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */