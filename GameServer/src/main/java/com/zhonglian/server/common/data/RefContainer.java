package com.zhonglian.server.common.data;

import com.zhonglian.server.common.data.ref.RefBase;

import java.util.Random;
import java.util.TreeMap;

public class RefContainer<T extends RefBase>
        extends TreeMap<Object, T> {
    private static final long serialVersionUID = 5170618524149079288L;

    public T random() {
        if (size() <= 0) {
            return null;
        }
        int rand = 0;
        if (size() > 1) {
            rand = (new Random()).nextInt(size());
        }
        return (T) values().toArray()[rand];
    }

    public T last() {
        if (size() <= 0) {
            return null;
        }
        Object k = keySet().toArray()[size() - 1];
        return get(k);
    }

    public T first() {
        if (size() <= 0) {
            return null;
        }
        Object k = keySet().toArray()[0];
        return get(k);
    }
}

