

package com.google.protobuf;

import java.util.AbstractList;
import java.util.RandomAccess;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;

public class UnmodifiableLazyStringList extends AbstractList<String>
    implements LazyStringList, RandomAccess {

  private final LazyStringList list;

  public UnmodifiableLazyStringList(LazyStringList list) {
    this.list = list;
  }

  @Override
  public String get(int index) {
    return list.get(index);
  }

  @Override
  public int size() {
    return list.size();
  }

  public ByteString getByteString(int index) {
    return list.getByteString(index);
  }

  public void add(ByteString element) {
    throw new UnsupportedOperationException();
  }

  public ListIterator<String> listIterator(final int index) {
    return new ListIterator<String>() {
      ListIterator<String> iter = list.listIterator(index);

      public boolean hasNext() {
        return iter.hasNext();
      }

      public String next() {
        return iter.next();
      }

      public boolean hasPrevious() {
        return iter.hasPrevious();
      }

      public String previous() {
        return iter.previous();
      }

      public int nextIndex() {
        return iter.nextIndex();
      }

      public int previousIndex() {
        return iter.previousIndex();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }

      public void set(String o) {
        throw new UnsupportedOperationException();
      }

      public void add(String o) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {
      Iterator<String> iter = list.iterator();

      public boolean hasNext() {
        return iter.hasNext();
      }

      public String next() {
        return iter.next();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public List<?> getUnderlyingElements() {

    return list.getUnderlyingElements();
  }
}
