package com.zhonglian.server.common;

public class Scid {
    public static long getScid(int sid, int cid) {
        if (cid == 0)
            return 0L;
        return sid * 100000000000L + cid % 100000000000L;
    }

    public static int getCid(long scid) {
        return (int) (scid % 100000000000L);
    }

    public static int getSid(long scid) {
        return (int) (scid / 100000000000L);
    }

    public static long getSgid(int sid, int gid) {
        if (gid == 0)
            return 0L;
        return sid * 100000000000L + gid % 100000000000L;
    }

    public static int getSidBySgid(long sgid) {
        return (int) (sgid / 100000000000L);
    }

    public static int getGid(long sgid) {
        return (int) (sgid % 100000000000L);
    }
}

