
package org.apache.thrift;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class TBaseHelper {

  private TBaseHelper(){}

  private static final Comparator comparator = new NestedStructureComparator();

  public static int compareTo(Object o1, Object o2) {
    if (o1 instanceof Comparable) {
      return compareTo((Comparable)o1, (Comparable)o2);
    } else if (o1 instanceof List) {
      return compareTo((List)o1, (List)o2);
    } else if (o1 instanceof Set) {
      return compareTo((Set)o1, (Set)o2);
    } else if (o1 instanceof Map) {
      return compareTo((Map)o1, (Map)o2);
    } else if (o1 instanceof byte[]) {
      return compareTo((byte[])o1, (byte[])o2);
    } else {
      throw new IllegalArgumentException("Cannot compare objects of type " + o1.getClass());
    }
  }

  public static int compareTo(boolean a, boolean b) {
    return Boolean.valueOf(a).compareTo(b);
  }

  public static int compareTo(byte a, byte b) {
    if (a < b) {
      return -1;
    } else if (b < a) {
      return 1;
    } else {
      return 0;
    }
  }

  public static int compareTo(short a, short b) {
    if (a < b) {
      return -1;
    } else if (b < a) {
      return 1;
    } else {
      return 0;
    }
  }

  public static int compareTo(int a, int b) {
    if (a < b) {
      return -1;
    } else if (b < a) {
      return 1;
    } else {
      return 0;
    }
  }

  public static int compareTo(long a, long b) {
    if (a < b) {
      return -1;
    } else if (b < a) {
      return 1;
    } else {
      return 0;
    }
  }

  public static int compareTo(double a, double b) {
    if (a < b) {
      return -1;
    } else if (b < a) {
      return 1;
    } else {
      return 0;
    }
  }

  public static int compareTo(String a, String b) {
    return a.compareTo(b);
  }

  public static int compareTo(byte[] a, byte[] b) {
    int sizeCompare = compareTo(a.length, b.length);
    if (sizeCompare != 0) {
      return sizeCompare;
    }
    for (int i = 0; i < a.length; i++) {
      int byteCompare = compareTo(a[i], b[i]);
      if (byteCompare != 0) {
        return byteCompare;
      }
    }
    return 0;
  }

  public static int compareTo(Comparable a, Comparable b) {
    return a.compareTo(b);
  }

  public static int compareTo(List a, List b) {
    int lastComparison = compareTo(a.size(), b.size());
    if (lastComparison != 0) {
      return lastComparison;
    }
    for (int i = 0; i < a.size(); i++) {
      lastComparison = comparator.compare(a.get(i), b.get(i));
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public static int compareTo(Set a, Set b) {
    int lastComparison = compareTo(a.size(), b.size());
    if (lastComparison != 0) {
      return lastComparison;
    }
    SortedSet sortedA = new TreeSet(comparator);
    sortedA.addAll(a);
    SortedSet sortedB = new TreeSet(comparator);
    sortedB.addAll(b);

    Iterator iterA = sortedA.iterator();
    Iterator iterB = sortedB.iterator();

    while (iterA.hasNext() && iterB.hasNext()) {
      lastComparison = comparator.compare(iterA.next(), iterB.next());
      if (lastComparison != 0) {
        return lastComparison;
      }
    }

    return 0;
  }

  public static int compareTo(Map a, Map b) {
    int lastComparison = compareTo(a.size(), b.size());
    if (lastComparison != 0) {
      return lastComparison;
    }

    SortedMap sortedA = new TreeMap(comparator);
    sortedA.putAll(a);
    Iterator<Map.Entry> iterA = sortedA.entrySet().iterator();
    SortedMap sortedB = new TreeMap(comparator);
    sortedB.putAll(b);
    Iterator<Map.Entry> iterB = sortedB.entrySet().iterator();

    while (iterA.hasNext() && iterB.hasNext()) {
      Map.Entry entryA = iterA.next();
      Map.Entry entryB = iterB.next();
      lastComparison = comparator.compare(entryA.getKey(), entryB.getKey());
      if (lastComparison != 0) {
        return lastComparison;
      }
      lastComparison = comparator.compare(entryA.getValue(), entryB.getValue());
      if (lastComparison != 0) {
        return lastComparison;
      }
    }

    return 0;
  }

  private static class NestedStructureComparator implements Comparator {
    public int compare(Object oA, Object oB) {
      if (oA == null && oB == null) {
        return 0;
      } else if (oA == null) {
        return -1;
      } else if (oB == null) {
        return 1;
      } else if (oA instanceof List) {
        return compareTo((List)oA, (List)oB);
      } else if (oA instanceof Set) {
        return compareTo((Set)oA, (Set)oB);
      } else if (oA instanceof Map) {
        return compareTo((Map)oA, (Map)oB);
      } else if (oA instanceof byte[]) {
        return compareTo((byte[])oA, (byte[])oB);
      } else {
        return compareTo((Comparable)oA, (Comparable)oB);
      }
    }
  }

  public static void toString(ByteBuffer bb, StringBuilder sb) {
    byte[] buf = bb.array();

    int arrayOffset = bb.arrayOffset();
    int origLimit = bb.limit();
    int limit = (origLimit - arrayOffset > 128) ? arrayOffset + 128 : origLimit;

    for (int i = arrayOffset; i < limit; i++) {
      if (i > arrayOffset) {
        sb.append(" ");
      }
      sb.append(paddedByteString(buf[i]));
    }
    if (origLimit != limit) {
      sb.append("...");
    }
  }

  public static String paddedByteString(byte b) {
    int extended = (b | 0x100) & 0x1ff;
    return Integer.toHexString(extended).toUpperCase().substring(1);
  }

  public static byte[] byteBufferToByteArray(ByteBuffer byteBuffer) {
    if (wrapsFullArray(byteBuffer)) {
      return byteBuffer.array();
    }
    byte[] target = new byte[byteBuffer.remaining()];
    byteBufferToByteArray(byteBuffer, target, 0);
    return target;
  }

  public static boolean wrapsFullArray(ByteBuffer byteBuffer) {
    return byteBuffer.hasArray()
      && byteBuffer.position() == 0
      && byteBuffer.arrayOffset() == 0
      && byteBuffer.remaining() == byteBuffer.capacity();
  }

  public static int byteBufferToByteArray(ByteBuffer byteBuffer, byte[] target, int offset) {
    int remaining = byteBuffer.remaining();
    System.arraycopy(byteBuffer.array(),
        byteBuffer.arrayOffset() + byteBuffer.position(),
        target,
        offset,
        remaining);
    return remaining;
  }

  public static ByteBuffer rightSize(ByteBuffer in) {
    if (wrapsFullArray(in)) {
      return in;
    }
    return ByteBuffer.wrap(byteBufferToByteArray(in));
  }

  public static ByteBuffer copyBinary(final ByteBuffer orig) {
    if (orig == null) {
      return null;
    }
    ByteBuffer copy = ByteBuffer.wrap(new byte[orig.remaining()]);
    if (orig.hasArray()) {
      System.arraycopy(orig.array(), orig.arrayOffset() + orig.position(), copy.array(), 0, orig.remaining());
    } else {
      orig.slice().get(copy.array());
    }

    return copy;
  }

  public static byte[] copyBinary(final byte[] orig) {
    if (orig == null) {
      return null;
    }

    byte[] copy = new byte[orig.length];
    System.arraycopy(orig, 0, copy, 0, orig.length);
    return copy;
  }
}
