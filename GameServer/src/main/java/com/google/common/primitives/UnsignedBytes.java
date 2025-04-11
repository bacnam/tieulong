package com.google.common.primitives;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Comparator;

public final class UnsignedBytes {
    public static final byte MAX_POWER_OF_TWO = -128;

    public static int toInt(byte value) {
        return value & 0xFF;
    }

    public static byte checkedCast(long value) {
        Preconditions.checkArgument((value >> 8L == 0L), "out of range: %s", new Object[]{Long.valueOf(value)});
        return (byte) (int) value;
    }

    public static byte saturatedCast(long value) {
        if (value > 255L) {
            return -1;
        }
        if (value < 0L) {
            return 0;
        }
        return (byte) (int) value;
    }

    public static int compare(byte a, byte b) {
        return toInt(a) - toInt(b);
    }

    public static byte min(byte... array) {
        Preconditions.checkArgument((array.length > 0));
        int min = toInt(array[0]);
        for (int i = 1; i < array.length; i++) {
            int next = toInt(array[i]);
            if (next < min) {
                min = next;
            }
        }
        return (byte) min;
    }

    public static byte max(byte... array) {
        Preconditions.checkArgument((array.length > 0));
        int max = toInt(array[0]);
        for (int i = 1; i < array.length; i++) {
            int next = toInt(array[i]);
            if (next > max) {
                max = next;
            }
        }
        return (byte) max;
    }

    public static String join(String separator, byte... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(toInt(array[0]));
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(toInt(array[i]));
        }
        return builder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparatorHolder.BEST_COMPARATOR;
    }

    @VisibleForTesting
    static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
    }

    enum PureJavaComparator implements Comparator<byte[]> {
        INSTANCE;

        public int compare(byte[] left, byte[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = UnsignedBytes.compare(left[i], right[i]);
                if (result != 0)
                    return result;
            }
            return left.length - right.length;
        }
    }

    @VisibleForTesting
    static class LexicographicalComparatorHolder {
        static final String UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class.getName() + "$UnsafeComparator";

        static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();

        static Comparator<byte[]> getBestComparator() {
            try {
                Class<?> theClass = Class.forName(UNSAFE_COMPARATOR_NAME);

                Comparator<byte[]> comparator = (Comparator<byte[]>) theClass.getEnumConstants()[0];

                return comparator;
            } catch (Throwable t) {
                return UnsignedBytes.lexicographicalComparatorJavaImpl();
            }
        }

        @VisibleForTesting
        enum UnsafeComparator implements Comparator<byte[]> {
            INSTANCE;

            static final int BYTE_ARRAY_BASE_OFFSET;

            static final Unsafe theUnsafe = AccessController.<Unsafe>doPrivileged(new PrivilegedAction() {
                public Object run() {
                    try {
                        Field f = Unsafe.class.getDeclaredField("theUnsafe");
                        f.setAccessible(true);
                        return f.get((Object) null);
                    } catch (NoSuchFieldException e) {

                        throw new Error();
                    } catch (IllegalAccessException e) {
                        throw new Error();
                    }
                }
            });
            static final boolean littleEndian = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);

            static {
                BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);

                if (theUnsafe.arrayIndexScale(byte[].class) != 1)
                    throw new AssertionError();
            }

            public int compare(byte[] left, byte[] right) {
                int minLength = Math.min(left.length, right.length);
                int minWords = minLength / 8;

                int i;

                for (i = 0; i < minWords * 8; i += 8) {
                    long lw = theUnsafe.getLong(left, BYTE_ARRAY_BASE_OFFSET + i);
                    long rw = theUnsafe.getLong(right, BYTE_ARRAY_BASE_OFFSET + i);
                    long diff = lw ^ rw;

                    if (diff != 0L) {
                        if (!littleEndian) {
                            return UnsignedLongs.compare(lw, rw);
                        }

                        int n = 0;

                        int x = (int) diff;
                        if (x == 0) {
                            x = (int) (diff >>> 32L);
                            n = 32;
                        }

                        int y = x << 16;
                        if (y == 0) {
                            n += 16;
                        } else {
                            x = y;
                        }

                        y = x << 8;
                        if (y == 0) {
                            n += 8;
                        }
                        return (int) ((lw >>> n & 0xFFL) - (rw >>> n & 0xFFL));
                    }
                }

                for (i = minWords * 8; i < minLength; i++) {
                    int result = UnsignedBytes.compare(left[i], right[i]);
                    if (result != 0) {
                        return result;
                    }
                }
                return left.length - right.length;
            }
        }

        enum PureJavaComparator implements Comparator<byte[]> {
            INSTANCE;

            public int compare(byte[] left, byte[] right) {
                int minLength = Math.min(left.length, right.length);
                for (int i = 0; i < minLength; i++) {
                    int result = UnsignedBytes.compare(left[i], right[i]);
                    if (result != 0) {
                        return result;
                    }
                }
                return left.length - right.length;
            }
        }
    }
}

