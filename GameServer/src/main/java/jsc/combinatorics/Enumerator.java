package jsc.combinatorics;

public interface Enumerator {
    double countSelections();

    boolean hasNext();

    Selection nextSelection();

    Selection randomSelection();

    void reset();

    void setSeed(long paramLong);
}

