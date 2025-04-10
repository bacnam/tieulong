package javolution.util.service;

public interface SortedTableService<E> extends TableService<E> {
  boolean addIfAbsent(E paramE);

  int positionOf(E paramE);

  SortedTableService<E> threadSafe();
}

