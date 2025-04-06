package javolution.util.service;

import java.util.SortedSet;

public interface SortedSetService<E> extends SetService<E>, SortedSet<E> {
  SortedSetService<E> headSet(E paramE);
  
  SortedSetService<E> subSet(E paramE1, E paramE2);
  
  SortedSetService<E> tailSet(E paramE);
  
  SortedSetService<E> threadSafe();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/service/SortedSetService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */