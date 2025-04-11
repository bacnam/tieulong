package com.zhonglian.server.common.utils.secure;

public class XPEncryptGroup {
    public static XPEncryptGroup instance;
    private int[] keys = new int[]{592389130, 374625019, -215789270, -1340029375, 1484193588};

    public XPEncryptGroup(int key) {
        this.keys[0] = key;
    }

    public long encode(long data) {
        Pair p = new Pair(data);
        for (int i = 0; i < this.keys.length; i++) {
            p.round(this.keys[i]);
        }
        p.exchange();

        return p.getValue();
    }

    public long decode(long data) {
        Pair p = new Pair(data);
        for (int i = this.keys.length - 1; i >= 0; i--) {
            p.round(this.keys[i]);
        }
        p.exchange();
        return p.getValue();
    }

    public long decode(int _a, int _b) {
        Pair p = new Pair(_a, _b);

        for (int i = this.keys.length - 1; i >= 0; i--) {
            p.round(this.keys[i]);
        }
        p.exchange();
        return p.getValue();
    }

    class Pair {
        int a;
        int b;

        public Pair(int _a, int _b) {
            this.a = _a;
            this.b = _b;
        }

        public Pair(long data) {
            int a = (int) ((data & 0xFFFFFFFF00000000L) >> 32L), b = (int) (data & 0xFFFFFFFFFFFFFFFFL);
            this.a = a;
            this.b = b;
        }

        public void exchange() {
            int t = this.a;
            this.a = this.b;
            this.b = t;
        }

        public void round(int key) {
            int t = this.a;
            this.a = this.b ^ secretMap(key, this.a);
            this.b = t;
        }

        public int secretMap(int x, int y) {
            return x * y ^ 0x2BFBFBFB;
        }

        public String toString() {
            return String.format("%08X%08X", new Object[]{Integer.valueOf(this.a), Integer.valueOf(this.b)});
        }

        public long getValue() {
            long c = this.a * 1L << 32L;
            long d = c | this.b * 1L & 0xFFFFFFFFL;
            return d;
        }
    }
}

