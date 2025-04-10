package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

@GwtCompatible
interface SortedMultiset<E> extends Multiset<E>, SortedIterable<E> {
  Comparator<? super E> comparator();

  Multiset.Entry<E> firstEntry();

  Multiset.Entry<E> lastEntry();

  Multiset.Entry<E> pollFirstEntry();

  Multiset.Entry<E> pollLastEntry();

  SortedSet<E> elementSet();

  Iterator<E> iterator();

  SortedMultiset<E> descendingMultiset();

  SortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);

  SortedMultiset<E> subMultiset(E paramE1, BoundType paramBoundType1, E paramE2, BoundType paramBoundType2);

  SortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);
}

