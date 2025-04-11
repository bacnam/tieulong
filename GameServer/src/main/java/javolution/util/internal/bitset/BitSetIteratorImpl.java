package javolution.util.internal.bitset;

import javolution.util.Index;
import javolution.util.service.BitSetService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class BitSetIteratorImpl
        implements Iterator<Index> {
    private final BitSetService that;
    private int nextIndex;
    private int currentIndex = -1;

    public BitSetIteratorImpl(BitSetService that, int index) {
        this.that = that;
        this.nextIndex = that.nextSetBit(index);
    }

    public boolean hasNext() {
        return (this.nextIndex >= 0);
    }

    public Index next() {
        if (this.nextIndex < 0)
            throw new NoSuchElementException();
        this.currentIndex = this.nextIndex;
        this.nextIndex = this.that.nextSetBit(this.nextIndex);
        return Index.valueOf(this.currentIndex);
    }

    public void remove() {
        if (this.currentIndex < 0)
            throw new IllegalStateException();
        this.that.clear(this.currentIndex);
        this.currentIndex = -1;
    }
}

