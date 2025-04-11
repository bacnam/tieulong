package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.*;

@GwtCompatible(emulated = true)
public final class Longs {
    public static final int BYTES = 8;
    public static final long MAX_POWER_OF_TWO = 4611686018427387904L;

    public static int hashCode(long value) {
        return (int) (value ^ value >>> 32L);
    }

    public static int compare(long a, long b) {
        return (a < b) ? -1 : ((a > b) ? 1 : 0);
    }

    public static boolean contains(long[] array, long target) {
        for (long value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(long[] array, long target) {
        return indexOf(array, target, 0, array.length);
    }

    private static int indexOf(long[] array, long target, int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(long[] array, long[] target) {
        Preconditions.checkNotNull(array, "array");
        Preconditions.checkNotNull(target, "target");
        if (target.length == 0) {
            return 0;
        }

        for (int i = 0; i < array.length - target.length + 1; i++) {
            int j = 0;
            while (true) {
                if (j < target.length) {
                    if (array[i + j] != target[j])
                        break;
                    j++;
                    continue;
                }
                return i;
            }

        }
        return -1;
    }

    public static int lastIndexOf(long[] array, long target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    private static int lastIndexOf(long[] array, long target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static long min(long... array) {
        Preconditions.checkArgument((array.length > 0));
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static long max(long... array) {
        Preconditions.checkArgument((array.length > 0));
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static long[] concat(long[]... arrays) {
        int length = 0;
        for (long[] array : arrays) {
            length += array.length;
        }
        long[] result = new long[length];
        int pos = 0;
        for (long[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(long value) {
        return new byte[]{(byte) (int) (value >> 56L), (byte) (int) (value >> 48L), (byte) (int) (value >> 40L), (byte) (int) (value >> 32L), (byte) (int) (value >> 24L), (byte) (int) (value >> 16L), (byte) (int) (value >> 8L), (byte) (int) value};
    }

    @GwtIncompatible("doesn't work")
    public static long fromByteArray(byte[] bytes) {
        Preconditions.checkArgument((bytes.length >= 8), "array too small: %s < %s", new Object[]{Integer.valueOf(bytes.length), Integer.valueOf(8)});

        return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
    }

    @GwtIncompatible("doesn't work")
    public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return (b1 & 0xFFL) << 56L | (b2 & 0xFFL) << 48L | (b3 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b5 & 0xFFL) << 24L | (b6 & 0xFFL) << 16L | (b7 & 0xFFL) << 8L | b8 & 0xFFL;
    }

    public static long[] ensureCapacity(long[] array, int minLength, int padding) {
        Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", new Object[]{Integer.valueOf(minLength)});
        Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", new Object[]{Integer.valueOf(padding)});
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }

    private static long[] copyOf(long[] original, int length) {
        long[] copy = new long[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static String join(String separator, long... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder(array.length * 10);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }

    public static Comparator<long[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static long[] toArray(Collection<Long> collection) {
        if (collection instanceof LongArrayAsList) {
            return ((LongArrayAsList) collection).toLongArray();
        }

        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        long[] array = new long[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Long) Preconditions.checkNotNull(boxedArray[i])).longValue();
        }
        return array;
    }

    public static List<Long> asList(long... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new LongArrayAsList(backingArray);
    }

    private enum LexicographicalComparator implements Comparator<long[]> {
        INSTANCE;

        public int compare(long[] left, long[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = Longs.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }

    @GwtCompatible
    private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0L;
        final long[] array;
        final int start;
        final int end;

        LongArrayAsList(long[] array) {
            this(array, 0, array.length);
        }

        LongArrayAsList(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        public int size() {
            return this.end - this.start;
        }

        public boolean isEmpty() {
            return false;
        }

        public Long get(int index) {
            Preconditions.checkElementIndex(index, size());
            return Long.valueOf(this.array[this.start + index]);
        }

        public boolean contains(Object target) {
            return (target instanceof Long && Longs.indexOf(this.array, ((Long) target).longValue(), this.start, this.end) != -1);
        }

        public int indexOf(Object target) {
            if (target instanceof Long) {
                int i = Longs.indexOf(this.array, ((Long) target).longValue(), this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object target) {
            if (target instanceof Long) {
                int i = Longs.lastIndexOf(this.array, ((Long) target).longValue(), this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }

        public Long set(int index, Long element) {
            Preconditions.checkElementIndex(index, size());
            long oldValue = this.array[this.start + index];
            this.array[this.start + index] = ((Long) Preconditions.checkNotNull(element)).longValue();
            return Long.valueOf(oldValue);
        }

        public List<Long> subList(int fromIndex, int toIndex) {
            int size = size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof LongArrayAsList) {
                LongArrayAsList that = (LongArrayAsList) object;
                int size = size();
                if (that.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != that.array[that.start + i]) {
                        return false;
                    }
                }
                return true;
            }
            return super.equals(object);
        }

        public int hashCode() {
            int result = 1;
            for (int i = this.start; i < this.end; i++) {
                result = 31 * result + Longs.hashCode(this.array[i]);
            }
            return result;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder(size() * 10);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; i++) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }

        long[] toLongArray() {
            int size = size();
            long[] result = new long[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}

