package com.mchange.v1.identicator;

import com.mchange.v1.util.IteratorUtils;
import com.mchange.v1.util.ListUtils;
import com.mchange.v1.util.WrapperIterator;

import java.util.*;

public class IdList
        implements List {
    Identicator id;
    List inner;

    public IdList(Identicator paramIdenticator, List paramList) {
        this.id = paramIdenticator;
        this.inner = paramList;
    }

    public int size() {
        return this.inner.size();
    }

    public boolean isEmpty() {
        return this.inner.isEmpty();
    }

    public boolean contains(Object paramObject) {
        StrongIdHashKey strongIdHashKey = new StrongIdHashKey(paramObject, this.id);
        return this.inner.contains(paramObject);
    }

    public Iterator iterator() {
        return (Iterator) new WrapperIterator(this.inner.iterator(), true) {
            protected Object transformObject(Object param1Object) {
                if (param1Object instanceof IdHashKey) {

                    IdHashKey idHashKey = (IdHashKey) param1Object;
                    return idHashKey.getKeyObj();
                }

                return param1Object;
            }
        };
    }

    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    public Object[] toArray(Object[] paramArrayOfObject) {
        return IteratorUtils.toArray(iterator(), size(), paramArrayOfObject);
    }

    public boolean add(Object paramObject) {
        return this.inner.add(new StrongIdHashKey(paramObject, this.id));
    }

    public boolean remove(Object paramObject) {
        return this.inner.remove(new StrongIdHashKey(paramObject, this.id));
    }

    public boolean containsAll(Collection paramCollection) {
        Iterator iterator = paramCollection.iterator();
        while (iterator.hasNext()) {

            StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
            if (!this.inner.contains(strongIdHashKey))
                return false;
        }
        return true;
    }

    public boolean addAll(Collection paramCollection) {
        Iterator iterator = paramCollection.iterator();
        boolean bool = false;
        while (iterator.hasNext()) {

            StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
            bool |= this.inner.add(strongIdHashKey);
        }
        return bool;
    }

    public boolean addAll(int paramInt, Collection paramCollection) {
        Iterator iterator = paramCollection.iterator();
        while (iterator.hasNext()) {

            StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
            this.inner.add(paramInt, strongIdHashKey);
            paramInt++;
        }
        return (paramCollection.size() > 0);
    }

    public boolean removeAll(Collection paramCollection) {
        Iterator iterator = paramCollection.iterator();
        boolean bool = false;
        while (iterator.hasNext()) {

            StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
            bool |= this.inner.remove(strongIdHashKey);
        }
        return bool;
    }

    public boolean retainAll(Collection paramCollection) {
        Iterator<IdHashKey> iterator = this.inner.iterator();
        boolean bool = false;
        while (iterator.hasNext()) {

            IdHashKey idHashKey = iterator.next();
            if (!paramCollection.contains(idHashKey.getKeyObj())) {

                this.inner.remove(idHashKey);
                bool = true;
            }
        }
        return bool;
    }

    public void clear() {
        this.inner.clear();
    }

    public boolean equals(Object paramObject) {
        if (paramObject instanceof List) {
            return ListUtils.equivalent(this, (List) paramObject);
        }
        return false;
    }

    public int hashCode() {
        return ListUtils.hashContents(this);
    }

    public Object get(int paramInt) {
        return ((IdHashKey) this.inner.get(paramInt)).getKeyObj();
    }

    public Object set(int paramInt, Object paramObject) {
        IdHashKey idHashKey = this.inner.set(paramInt, new StrongIdHashKey(paramObject, this.id));
        return idHashKey.getKeyObj();
    }

    public void add(int paramInt, Object paramObject) {
        this.inner.add(paramInt, new StrongIdHashKey(paramObject, this.id));
    }

    public Object remove(int paramInt) {
        IdHashKey idHashKey = this.inner.remove(paramInt);
        return (idHashKey == null) ? null : idHashKey.getKeyObj();
    }

    public int indexOf(Object paramObject) {
        return this.inner.indexOf(new StrongIdHashKey(paramObject, this.id));
    }

    public int lastIndexOf(Object paramObject) {
        return this.inner.lastIndexOf(new StrongIdHashKey(paramObject, this.id));
    }

    public ListIterator listIterator() {
        return (new LinkedList(this)).listIterator();
    }

    public ListIterator listIterator(int paramInt) {
        return (new LinkedList(this)).listIterator(paramInt);
    }

    public List subList(int paramInt1, int paramInt2) {
        return new IdList(this.id, this.inner.subList(paramInt1, paramInt2));
    }
}

