package javolution.util.service;

import java.util.Set;

public interface SetService<E> extends CollectionService<E>, Set<E> {
  SetService<E> threadSafe();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/service/SetService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */