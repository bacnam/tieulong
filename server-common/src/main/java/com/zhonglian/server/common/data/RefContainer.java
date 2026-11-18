package com.zhonglian.server.common.data;

import com.zhonglian.server.common.data.ref.RefBase;
import java.util.Random;
import java.util.TreeMap;

public class RefContainer<T extends RefBase> extends TreeMap<Object, T> {
    private static final long serialVersionUID = 5170618524149079288L;

    public T random() {
        if (this.size() <= 0) {
            return null;
        } else {
            int rand = 0;
            if (this.size() > 1) {
                rand = new Random().nextInt(this.size());
            }

            return (T)this.values().toArray()[rand];
        }
    }

    public T last() {
        if (this.size() <= 0) {
            return null;
        } else {
            Object k = this.keySet().toArray()[this.size() - 1];
            return this.get(k);
        }
    }

    public T first() {
        if (this.size() <= 0) {
            return null;
        } else {
            Object k = this.keySet().toArray()[0];
            return this.get(k);
        }
    }
}
