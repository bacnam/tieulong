package javolution.util.internal.table;

import javolution.util.function.Equality;
import javolution.util.service.TableService;

public class UnmodifiableTableImpl<E>
        extends TableView<E> {
    private static final long serialVersionUID = 1536L;

    public UnmodifiableTableImpl(TableService<E> target) {
        super(target);
    }

    public boolean add(E element) {
        throw new UnsupportedOperationException("Read-Only Collection.");
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    public void clear() {
        throw new UnsupportedOperationException("Read-Only Collection.");
    }

    public int indexOf(Object o) {
        return target().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return target().lastIndexOf(o);
    }

    public E remove(int index) {
        throw new UnsupportedOperationException("Read-Only Collection.");
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException("Read-Only Collection.");
    }

    public TableService<E> threadSafe() {
        return this;
    }

    public E get(int index) {
        return (E) target().get(index);
    }

    public int size() {
        return target().size();
    }

    public Equality<? super E> comparator() {
        return target().comparator();
    }
}

