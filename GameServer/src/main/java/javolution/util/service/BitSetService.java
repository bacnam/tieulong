package javolution.util.service;

import javolution.util.Index;

public interface BitSetService extends SetService<Index> {
    int cardinality();

    boolean get(int paramInt);

    BitSetService get(int paramInt1, int paramInt2);

    boolean intersects(BitSetService paramBitSetService);

    int length();

    int nextClearBit(int paramInt);

    int nextSetBit(int paramInt);

    int previousClearBit(int paramInt);

    int previousSetBit(int paramInt);

    void clear(int paramInt);

    void clear(int paramInt1, int paramInt2);

    boolean getAndSet(int paramInt, boolean paramBoolean);

    void set(int paramInt);

    void set(int paramInt, boolean paramBoolean);

    void set(int paramInt1, int paramInt2);

    void set(int paramInt1, int paramInt2, boolean paramBoolean);

    void flip(int paramInt);

    void flip(int paramInt1, int paramInt2);

    void and(BitSetService paramBitSetService);

    void andNot(BitSetService paramBitSetService);

    void or(BitSetService paramBitSetService);

    void xor(BitSetService paramBitSetService);

    long[] toLongArray();
}

