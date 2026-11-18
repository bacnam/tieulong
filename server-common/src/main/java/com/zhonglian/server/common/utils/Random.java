package com.zhonglian.server.common.utils;

import java.util.UUID;

public class Random {
    public static int nextInt(int n) {
        return n == 0 ? 0 : Math.abs(UUID.randomUUID().hashCode()) % n;
    }

    public static int nextInt(int n, int offset) {
        if (n == 0) {
            return offset;
        } else {
            int res = Math.abs(UUID.randomUUID().hashCode()) % n;
            return res + offset;
        }
    }

    public static boolean nextBoolean() {
        return nextInt(2) == 1;
    }

    public static byte nextByte() {
        return (byte)nextInt(256);
    }

    public static long nextLong(long n) {
        if (n == 0L) {
            return 0L;
        } else {
            long head = nextInt(Integer.MAX_VALUE);
            long l = nextInt(Integer.MAX_VALUE);
            long dividend = (head << 32) + l;
            long remain = dividend - dividend / n * n;
            return n < 0L ? 0L - remain : remain;
        }
    }

    public static boolean isTrue(float ratio) {
        float fl = (float)Math.random();
        return fl <= ratio;
    }
}
