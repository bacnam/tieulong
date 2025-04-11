package com.google.protobuf;

import java.util.*;

public class LazyStringArrayList extends AbstractList<String>
        implements LazyStringList, RandomAccess {

    public final static LazyStringList EMPTY = new UnmodifiableLazyStringList(
            new LazyStringArrayList());

    private final List<Object> list;

    public LazyStringArrayList() {
        list = new ArrayList<Object>();
    }

    public LazyStringArrayList(LazyStringList from) {
        list = new ArrayList<Object>(from.size());
        addAll(from);
    }

    public LazyStringArrayList(List<String> from) {
        list = new ArrayList<Object>(from);
    }

    @Override
    public String get(int index) {
        Object o = list.get(index);
        if (o instanceof String) {
            return (String) o;
        } else {
            ByteString bs = (ByteString) o;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                list.set(index, s);
            }
            return s;
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String set(int index, String s) {
        Object o = list.set(index, s);
        return asString(o);
    }

    @Override
    public void add(int index, String element) {
        list.add(index, element);
        modCount++;
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {

        return addAll(size(), c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {

        Collection<?> collection = c instanceof LazyStringList
                ? ((LazyStringList) c).getUnderlyingElements() : c;
        boolean ret = list.addAll(index, collection);
        modCount++;
        return ret;
    }

    @Override
    public String remove(int index) {
        Object o = list.remove(index);
        modCount++;
        return asString(o);
    }

    public void clear() {
        list.clear();
        modCount++;
    }

    public void add(ByteString element) {
        list.add(element);
        modCount++;
    }

    public ByteString getByteString(int index) {
        Object o = list.get(index);
        if (o instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String) o);
            list.set(index, b);
            return b;
        } else {
            return (ByteString) o;
        }
    }

    private String asString(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else {
            return ((ByteString) o).toStringUtf8();
        }
    }

    public List<?> getUnderlyingElements() {
        return Collections.unmodifiableList(list);
    }
}
