package com.mchange.v2.ser;

public interface Indirector {
    IndirectlySerialized indirectForm(Object paramObject) throws Exception;
}

