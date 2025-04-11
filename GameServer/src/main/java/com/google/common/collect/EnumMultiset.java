package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.Iterator;

@GwtCompatible(emulated = true)
public final class EnumMultiset<E extends Enum<E>>
        extends AbstractMapBasedMultiset<E> {
    @GwtIncompatible("Not needed in emulated source")
    private static final long serialVersionUID = 0L;
    private transient Class<E> type;

    private EnumMultiset(Class<E> type) {
        super(WellBehavedMap.wrap(new EnumMap<E, Count>(type)));
        this.type = type;
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
        return new EnumMultiset<E>(type);
    }

    public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
        Iterator<E> iterator = elements.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
        EnumMultiset<E> multiset = new EnumMultiset<E>(((Enum<E>) iterator.next()).getDeclaringClass());
        Iterables.addAll(multiset, elements);
        return multiset;
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.type);
        Serialization.writeMultiset(this, stream);
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();

        Class<E> localType = (Class<E>) stream.readObject();
        this.type = localType;
        setBackingMap(WellBehavedMap.wrap(new EnumMap<E, Count>(this.type)));
        Serialization.populateMultiset(this, stream);
    }
}

