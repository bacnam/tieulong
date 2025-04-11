package com.mchange.v1.identicator;

abstract class IdHashKey {
    Identicator id;

    public IdHashKey(Identicator paramIdenticator) {
        this.id = paramIdenticator;
    }

    public Identicator getIdenticator() {
        return this.id;
    }

    public abstract Object getKeyObj();

    public abstract boolean equals(Object paramObject);

    public abstract int hashCode();
}

