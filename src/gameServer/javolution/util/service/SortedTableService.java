package javolution.util.service;

public interface SortedTableService<E> extends TableService<E> {
  boolean addIfAbsent(E paramE);
  
  int positionOf(E paramE);
  
  SortedTableService<E> threadSafe();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/service/SortedTableService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */