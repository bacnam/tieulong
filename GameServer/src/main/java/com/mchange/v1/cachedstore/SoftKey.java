package com.mchange.v1.cachedstore;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

final class SoftKey
        extends SoftReference {
    int hash_code;

    SoftKey(Object paramObject, ReferenceQueue<? super T> paramReferenceQueue) {
        super((T) paramObject, paramReferenceQueue);
        this.hash_code = paramObject.hashCode();
    }

    public int hashCode() {
        return this.hash_code;
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) return true;

        T t = get();
        if (t == null)
            return false;
        if (getClass() == paramObject.getClass()) {

            SoftKey softKey = (SoftKey) paramObject;
            return t.equals(softKey.get());
        }

        return false;
    }
}

