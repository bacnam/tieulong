package com.zhonglian.server.common.utils.secure;

public class XPEncryptGroup {
    private int[] keys = new int[]{592389130, 374625019, -215789270, -1340029375, 1484193588};
    public static XPEncryptGroup instance;

    public XPEncryptGroup(int key) {
        this.keys[0] = key;
    }

    public long encode(long data) {
        XPEncryptGroup.Pair p = new XPEncryptGroup.Pair(data);

        for (int i = 0; i < this.keys.length; i++) {
            p.round(this.keys[i]);
        }

        p.exchange();
        return p.getValue();
    }

    public long decode(long data) {
        XPEncryptGroup.Pair p = new XPEncryptGroup.Pair(data);

        for (int i = this.keys.length - 1; i >= 0; i--) {
            p.round(this.keys[i]);
        }

        p.exchange();
        return p.getValue();
    }

    public long decode(int _a, int _b) {
        XPEncryptGroup.Pair p = new XPEncryptGroup.Pair(_a, _b);

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
            int a = (int)((data & -4294967296L) >> 32);
            int b = (int)(data & -1L);
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
            this.a = this.b ^ this.secretMap(key, this.a);
            this.b = t;
        }

        public int secretMap(int x, int y) {
            return x * y ^ 737934331;
        }

        @Override
        public String toString() {
            return String.format("%08X%08X", this.a, this.b);
        }

        public long getValue() {
            long c = this.a * 1L << 32;
            return c | this.b * 1L & 4294967295L;
        }
    }
}
