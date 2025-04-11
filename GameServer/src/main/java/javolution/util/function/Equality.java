package javolution.util.function;

import java.util.Comparator;

public interface Equality<T> extends Comparator<T> {
    int hashCodeOf(T paramT);

    boolean areEqual(T paramT1, T paramT2);

    int compare(T paramT1, T paramT2);
}

