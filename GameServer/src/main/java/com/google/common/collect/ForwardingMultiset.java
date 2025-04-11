package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingMultiset<E>
        extends ForwardingCollection<E>
        implements Multiset<E> {
    public int count(Object element) {
        return delegate().count(element);
    }

    public int add(E element, int occurrences) {
        return delegate().add(element, occurrences);
    }

    public int remove(Object element, int occurrences) {
        return delegate().remove(element, occurrences);
    }

    public Set<E> elementSet() {
        return delegate().elementSet();
    }

    public Set<Multiset.Entry<E>> entrySet() {
        return delegate().entrySet();
    }

    public boolean equals(@Nullable Object object) {
        return (object == this || delegate().equals(object));
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    public int setCount(E element, int count) {
        return delegate().setCount(element, count);
    }

    public boolean setCount(E element, int oldCount, int newCount) {
        return delegate().setCount(element, oldCount, newCount);
    }

    @Beta
    protected boolean standardContains(@Nullable Object object) {
        return (count(object) > 0);
    }

    @Beta
    protected void standardClear() {
        Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
        while (entryIterator.hasNext()) {
            entryIterator.next();
            entryIterator.remove();
        }
    }

    @Beta
    protected int standardCount(@Nullable Object object) {
        for (Multiset.Entry<?> entry : entrySet()) {
            if (Objects.equal(entry.getElement(), object)) {
                return entry.getCount();
            }
        }
        return 0;
    }

    @Beta
    protected boolean standardAdd(E element) {
        add(element, 1);
        return true;
    }

    @Beta
    protected boolean standardAddAll(Collection<? extends E> elementsToAdd) {
        return Multisets.addAllImpl(this, elementsToAdd);
    }

    @Beta
    protected boolean standardRemove(Object element) {
        return (remove(element, 1) > 0);
    }

    @Beta
    protected boolean standardRemoveAll(Collection<?> elementsToRemove) {
        return Multisets.removeAllImpl(this, elementsToRemove);
    }

    @Beta
    protected boolean standardRetainAll(Collection<?> elementsToRetain) {
        return Multisets.retainAllImpl(this, elementsToRetain);
    }

    @Beta
    protected int standardSetCount(E element, int count) {
        return Multisets.setCountImpl(this, element, count);
    }

    @Beta
    protected boolean standardSetCount(E element, int oldCount, int newCount) {
        return Multisets.setCountImpl(this, element, oldCount, newCount);
    }

    @Deprecated
    @Beta
    protected Set<E> standardElementSet() {
        return new StandardElementSet();
    }

    @Beta
    protected Iterator<E> standardIterator() {
        return Multisets.iteratorImpl(this);
    }

    @Beta
    protected int standardSize() {
        return Multisets.sizeImpl(this);
    }

    @Beta
    protected boolean standardEquals(@Nullable Object object) {
        return Multisets.equalsImpl(this, object);
    }

    @Beta
    protected int standardHashCode() {
        return entrySet().hashCode();
    }

    @Beta
    protected String standardToString() {
        return entrySet().toString();
    }

    protected abstract Multiset<E> delegate();

    @Beta
    protected class StandardElementSet
            extends Multisets.ElementSet<E> {
        Multiset<E> multiset() {
            return ForwardingMultiset.this;
        }
    }
}

