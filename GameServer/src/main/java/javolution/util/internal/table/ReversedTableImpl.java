package javolution.util.internal.table;

import javolution.util.function.Equality;
import javolution.util.service.TableService;

public class ReversedTableImpl<E>
extends TableView<E>
{
private static final long serialVersionUID = 1536L;

public ReversedTableImpl(TableService<E> that) {
super(that);
}

public void add(int index, E element) {
target().add(size() - index - 1, element);
}

public E get(int index) {
return (E)target().get(size() - index - 1);
}

public int indexOf(Object o) {
return size() - target().lastIndexOf(o) - 1;
}

public int lastIndexOf(Object o) {
return size() - target().indexOf(o) - 1;
}

public E remove(int index) {
return (E)target().remove(size() - index - 1);
}

public E set(int index, E element) {
return (E)target().set(size() - index - 1, element);
}

public void clear() {
target().clear();
}

public int size() {
return target().size();
}

public boolean add(E e) {
target().addFirst(e);
return true;
}

public Equality<? super E> comparator() {
return target().comparator();
}
}

