package org.apache.commons.pool.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.*;

class CursorableLinkedList
        implements List, Serializable {
    private static final long serialVersionUID = 8836393098519411393L;
    protected transient int _size = 0;
    protected transient Listable _head = new Listable(null, null, null);
    protected transient int _modCount = 0;
    protected transient List _cursors = new ArrayList();

    public boolean add(Object o) {
        insertListable(this._head.prev(), null, o);
        return true;
    }

    public void add(int index, Object element) {
        if (index == this._size) {
            add(element);
        } else {
            if (index < 0 || index > this._size) {
                throw new IndexOutOfBoundsException(String.valueOf(index) + " < 0 or " + String.valueOf(index) + " > " + this._size);
            }
            Listable succ = isEmpty() ? null : getListableAt(index);
            Listable pred = (null == succ) ? null : succ.prev();
            insertListable(pred, succ, element);
        }
    }

    public boolean addAll(Collection c) {
        if (c.isEmpty()) {
            return false;
        }
        Iterator it = c.iterator();
        while (it.hasNext()) {
            insertListable(this._head.prev(), null, it.next());
        }
        return true;
    }

    public boolean addAll(int index, Collection c) {
        if (c.isEmpty())
            return false;
        if (this._size == index || this._size == 0) {
            return addAll(c);
        }
        Listable succ = getListableAt(index);
        Listable pred = (null == succ) ? null : succ.prev();
        Iterator it = c.iterator();
        while (it.hasNext()) {
            pred = insertListable(pred, succ, it.next());
        }
        return true;
    }

    public boolean addFirst(Object o) {
        insertListable(null, this._head.next(), o);
        return true;
    }

    public boolean addLast(Object o) {
        insertListable(this._head.prev(), null, o);
        return true;
    }

    public void clear() {
        Iterator it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public boolean contains(Object o) {
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            if ((null == o && null == elt.value()) || (o != null && o.equals(elt.value()))) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Collection c) {
        Iterator it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public Cursor cursor() {
        return new Cursor(this, 0);
    }

    public Cursor cursor(int i) {
        return new Cursor(this, i);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List)) {
            return false;
        }
        Iterator it = ((List) o).listIterator();
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            if (!it.hasNext() || ((null == elt.value()) ? (null != it.next()) : !elt.value().equals(it.next()))) {
                return false;
            }
        }
        return !it.hasNext();
    }

    public Object get(int index) {
        return getListableAt(index).value();
    }

    public Object getFirst() {
        try {
            return this._head.next().value();
        } catch (NullPointerException e) {
            throw new NoSuchElementException();
        }
    }

    public Object getLast() {
        try {
            return this._head.prev().value();
        } catch (NullPointerException e) {
            throw new NoSuchElementException();
        }
    }

    public int hashCode() {
        int hash = 1;
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            hash = 31 * hash + ((null == elt.value()) ? 0 : elt.value().hashCode());
        }
        return hash;
    }

    public int indexOf(Object o) {
        int ndx = 0;

        if (null == o) {
            for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
                if (null == elt.value()) {
                    return ndx;
                }
                ndx++;
            }
        } else {

            for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
                if (o.equals(elt.value())) {
                    return ndx;
                }
                ndx++;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return (0 == this._size);
    }

    public Iterator iterator() {
        return listIterator(0);
    }

    public int lastIndexOf(Object o) {
        int ndx = this._size - 1;

        if (null == o) {
            for (Listable elt = this._head.prev(), past = null; null != elt && past != this._head.next(); elt = (past = elt).prev()) {
                if (null == elt.value()) {
                    return ndx;
                }
                ndx--;
            }
        } else {
            for (Listable elt = this._head.prev(), past = null; null != elt && past != this._head.next(); elt = (past = elt).prev()) {
                if (o.equals(elt.value())) {
                    return ndx;
                }
                ndx--;
            }
        }
        return -1;
    }

    public ListIterator listIterator() {
        return listIterator(0);
    }

    public ListIterator listIterator(int index) {
        if (index < 0 || index > this._size) {
            throw new IndexOutOfBoundsException(index + " < 0 or > " + this._size);
        }
        return new ListIter(this, index);
    }

    public boolean remove(Object o) {
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            if (null == o && null == elt.value()) {
                removeListable(elt);
                return true;
            }
            if (o != null && o.equals(elt.value())) {
                removeListable(elt);
                return true;
            }
        }
        return false;
    }

    public Object remove(int index) {
        Listable elt = getListableAt(index);
        Object ret = elt.value();
        removeListable(elt);
        return ret;
    }

    public boolean removeAll(Collection c) {
        if (0 == c.size() || 0 == this._size) {
            return false;
        }
        boolean changed = false;
        Iterator it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    public Object removeFirst() {
        if (this._head.next() != null) {
            Object val = this._head.next().value();
            removeListable(this._head.next());
            return val;
        }
        throw new NoSuchElementException();
    }

    public Object removeLast() {
        if (this._head.prev() != null) {
            Object val = this._head.prev().value();
            removeListable(this._head.prev());
            return val;
        }
        throw new NoSuchElementException();
    }

    public boolean retainAll(Collection c) {
        boolean changed = false;
        Iterator it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    public Object set(int index, Object element) {
        Listable elt = getListableAt(index);
        Object val = elt.setValue(element);
        broadcastListableChanged(elt);
        return val;
    }

    public int size() {
        return this._size;
    }

    public Object[] toArray() {
        Object[] array = new Object[this._size];
        int i = 0;
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            array[i++] = elt.value();
        }
        return array;
    }

    public Object[] toArray(Object[] a) {
        if (a.length < this._size) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), this._size);
        }
        int i = 0;
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            a[i++] = elt.value();
        }
        if (a.length > this._size) {
            a[this._size] = null;
        }
        return a;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        for (Listable elt = this._head.next(), past = null; null != elt && past != this._head.prev(); elt = (past = elt).next()) {
            if (this._head.next() != elt) {
                buf.append(", ");
            }
            buf.append(elt.value());
        }
        buf.append("]");
        return buf.toString();
    }

    public List subList(int i, int j) {
        if (i < 0 || j > this._size || i > j)
            throw new IndexOutOfBoundsException();
        if (i == 0 && j == this._size) {
            return this;
        }
        return new CursorableSubList(this, i, j);
    }

    protected Listable insertListable(Listable before, Listable after, Object value) {
        this._modCount++;
        this._size++;
        Listable elt = new Listable(before, after, value);
        if (null != before) {
            before.setNext(elt);
        } else {
            this._head.setNext(elt);
        }

        if (null != after) {
            after.setPrev(elt);
        } else {
            this._head.setPrev(elt);
        }
        broadcastListableInserted(elt);
        return elt;
    }

    protected void removeListable(Listable elt) {
        this._modCount++;
        this._size--;
        if (this._head.next() == elt) {
            this._head.setNext(elt.next());
        }
        if (null != elt.next()) {
            elt.next().setPrev(elt.prev());
        }
        if (this._head.prev() == elt) {
            this._head.setPrev(elt.prev());
        }
        if (null != elt.prev()) {
            elt.prev().setNext(elt.next());
        }
        broadcastListableRemoved(elt);
    }

    protected Listable getListableAt(int index) {
        if (index < 0 || index >= this._size) {
            throw new IndexOutOfBoundsException(String.valueOf(index) + " < 0 or " + String.valueOf(index) + " >= " + this._size);
        }
        if (index <= this._size / 2) {
            Listable listable = this._head.next();
            for (int j = 0; j < index; j++) {
                listable = listable.next();
            }
            return listable;
        }
        Listable elt = this._head.prev();
        for (int i = this._size - 1; i > index; i--) {
            elt = elt.prev();
        }
        return elt;
    }

    protected void registerCursor(Cursor cur) {
        for (Iterator it = this._cursors.iterator(); it.hasNext(); ) {
            WeakReference ref = it.next();
            if (ref.get() == null) {
                it.remove();
            }
        }

        this._cursors.add(new WeakReference(cur));
    }

    protected void unregisterCursor(Cursor cur) {
        for (Iterator it = this._cursors.iterator(); it.hasNext(); ) {
            WeakReference ref = it.next();
            Cursor cursor = ref.get();
            if (cursor == null) {

                it.remove();
                continue;
            }
            if (cursor == cur) {
                ref.clear();
                it.remove();
                break;
            }
        }
    }

    protected void invalidateCursors() {
        Iterator it = this._cursors.iterator();
        while (it.hasNext()) {
            WeakReference ref = it.next();
            Cursor cursor = ref.get();
            if (cursor != null) {

                cursor.invalidate();
                ref.clear();
            }
            it.remove();
        }
    }

    protected void broadcastListableChanged(Listable elt) {
        Iterator it = this._cursors.iterator();
        while (it.hasNext()) {
            WeakReference ref = it.next();
            Cursor cursor = ref.get();
            if (cursor == null) {
                it.remove();
                continue;
            }
            cursor.listableChanged(elt);
        }
    }

    protected void broadcastListableRemoved(Listable elt) {
        Iterator it = this._cursors.iterator();
        while (it.hasNext()) {
            WeakReference ref = it.next();
            Cursor cursor = ref.get();
            if (cursor == null) {
                it.remove();
                continue;
            }
            cursor.listableRemoved(elt);
        }
    }

    protected void broadcastListableInserted(Listable elt) {
        Iterator it = this._cursors.iterator();
        while (it.hasNext()) {
            WeakReference ref = it.next();
            Cursor cursor = ref.get();
            if (cursor == null) {
                it.remove();
                continue;
            }
            cursor.listableInserted(elt);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(this._size);
        Listable cur = this._head.next();
        while (cur != null) {
            out.writeObject(cur.value());
            cur = cur.next();
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this._size = 0;
        this._modCount = 0;
        this._cursors = new ArrayList();
        this._head = new Listable(null, null, null);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            add(in.readObject());
        }
    }

    static class Listable
            implements Serializable {
        private Listable _prev = null;
        private Listable _next = null;
        private Object _val = null;

        Listable(Listable prev, Listable next, Object val) {
            this._prev = prev;
            this._next = next;
            this._val = val;
        }

        Listable next() {
            return this._next;
        }

        Listable prev() {
            return this._prev;
        }

        Object value() {
            return this._val;
        }

        void setNext(Listable next) {
            this._next = next;
        }

        void setPrev(Listable prev) {
            this._prev = prev;
        }

        Object setValue(Object val) {
            Object temp = this._val;
            this._val = val;
            return temp;
        }
    }

    class ListIter implements ListIterator {
        private final CursorableLinkedList this$0;
        CursorableLinkedList.Listable _cur;
        CursorableLinkedList.Listable _lastReturned;
        int _expectedModCount;
        int _nextIndex;

        ListIter(CursorableLinkedList this$0, int index) {
            this.this$0 = this$0;
            this._cur = null;
            this._lastReturned = null;
            this._expectedModCount = this.this$0._modCount;
            this._nextIndex = 0;
            if (index == 0) {
                this._cur = new CursorableLinkedList.Listable(null, this$0._head.next(), null);
                this._nextIndex = 0;
            } else if (index == this$0._size) {
                this._cur = new CursorableLinkedList.Listable(this$0._head.prev(), null, null);
                this._nextIndex = this$0._size;
            } else {
                CursorableLinkedList.Listable temp = this$0.getListableAt(index);
                this._cur = new CursorableLinkedList.Listable(temp.prev(), temp, null);
                this._nextIndex = index;
            }
        }

        public Object previous() {
            checkForComod();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Object ret = this._cur.prev().value();
            this._lastReturned = this._cur.prev();
            this._cur.setNext(this._cur.prev());
            this._cur.setPrev(this._cur.prev().prev());
            this._nextIndex--;
            return ret;
        }

        public boolean hasNext() {
            checkForComod();
            return (null != this._cur.next() && this._cur.prev() != this.this$0._head.prev());
        }

        public Object next() {
            checkForComod();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Object ret = this._cur.next().value();
            this._lastReturned = this._cur.next();
            this._cur.setPrev(this._cur.next());
            this._cur.setNext(this._cur.next().next());
            this._nextIndex++;
            return ret;
        }

        public int previousIndex() {
            checkForComod();
            if (!hasPrevious()) {
                return -1;
            }
            return this._nextIndex - 1;
        }

        public boolean hasPrevious() {
            checkForComod();
            return (null != this._cur.prev() && this._cur.next() != this.this$0._head.next());
        }

        public void set(Object o) {
            checkForComod();
            try {
                this._lastReturned.setValue(o);
            } catch (NullPointerException e) {
                throw new IllegalStateException();
            }
        }

        public int nextIndex() {
            checkForComod();
            if (!hasNext()) {
                return this.this$0.size();
            }
            return this._nextIndex;
        }

        public void remove() {
            checkForComod();
            if (null == this._lastReturned) {
                throw new IllegalStateException();
            }
            this._cur.setNext((this._lastReturned == this.this$0._head.prev()) ? null : this._lastReturned.next());
            this._cur.setPrev((this._lastReturned == this.this$0._head.next()) ? null : this._lastReturned.prev());
            this.this$0.removeListable(this._lastReturned);
            this._lastReturned = null;
            this._nextIndex--;
            this._expectedModCount++;
        }

        public void add(Object o) {
            checkForComod();
            this._cur.setPrev(this.this$0.insertListable(this._cur.prev(), this._cur.next(), o));
            this._lastReturned = null;
            this._nextIndex++;
            this._expectedModCount++;
        }

        protected void checkForComod() {
            if (this._expectedModCount != this.this$0._modCount)
                throw new ConcurrentModificationException();
        }
    }

    public class Cursor extends ListIter implements ListIterator {
        private final CursorableLinkedList this$0;
        boolean _valid;

        Cursor(CursorableLinkedList this$0, int index) {
            super(this$0, index);
            this.this$0 = this$0;
            this._valid = false;
            this._valid = true;
            this$0.registerCursor(this);
        }

        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        public void add(Object o) {
            checkForComod();
            CursorableLinkedList.Listable elt = this.this$0.insertListable(this._cur.prev(), this._cur.next(), o);
            this._cur.setPrev(elt);
            this._cur.setNext(elt.next());
            this._lastReturned = null;
            this._nextIndex++;
            this._expectedModCount++;
        }

        protected void listableRemoved(CursorableLinkedList.Listable elt) {
            if (null == this.this$0._head.prev()) {
                this._cur.setNext(null);
            } else if (this._cur.next() == elt) {
                this._cur.setNext(elt.next());
            }
            if (null == this.this$0._head.next()) {
                this._cur.setPrev(null);
            } else if (this._cur.prev() == elt) {
                this._cur.setPrev(elt.prev());
            }
            if (this._lastReturned == elt) {
                this._lastReturned = null;
            }
        }

        protected void listableInserted(CursorableLinkedList.Listable elt) {
            if (null == this._cur.next() && null == this._cur.prev()) {
                this._cur.setNext(elt);
            } else if (this._cur.prev() == elt.prev()) {
                this._cur.setNext(elt);
            }
            if (this._cur.next() == elt.next()) {
                this._cur.setPrev(elt);
            }
            if (this._lastReturned == elt) {
                this._lastReturned = null;
            }
        }

        protected void listableChanged(CursorableLinkedList.Listable elt) {
            if (this._lastReturned == elt) {
                this._lastReturned = null;
            }
        }

        protected void checkForComod() {
            if (!this._valid) {
                throw new ConcurrentModificationException();
            }
        }

        protected void invalidate() {
            this._valid = false;
        }

        public void close() {
            if (this._valid) {
                this._valid = false;
                this.this$0.unregisterCursor(this);
            }
        }
    }
}

