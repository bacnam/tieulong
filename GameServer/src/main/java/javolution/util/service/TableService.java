package javolution.util.service;

import java.util.Deque;
import java.util.List;
import java.util.RandomAccess;

public interface TableService<E> extends CollectionService<E>, List<E>, Deque<E>, RandomAccess {
  TableService<E> subList(int paramInt1, int paramInt2);

  TableService<E> threadSafe();
}

