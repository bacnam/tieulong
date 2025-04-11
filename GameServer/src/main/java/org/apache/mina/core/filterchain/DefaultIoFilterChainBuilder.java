package org.apache.mina.core.filterchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultIoFilterChainBuilder
        implements IoFilterChainBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIoFilterChainBuilder.class);

    private final List<IoFilterChain.Entry> entries;

    public DefaultIoFilterChainBuilder() {
        this.entries = new CopyOnWriteArrayList<IoFilterChain.Entry>();
    }

    public DefaultIoFilterChainBuilder(DefaultIoFilterChainBuilder filterChain) {
        if (filterChain == null) {
            throw new IllegalArgumentException("filterChain");
        }
        this.entries = new CopyOnWriteArrayList<IoFilterChain.Entry>(filterChain.entries);
    }

    public IoFilterChain.Entry getEntry(String name) {
        for (IoFilterChain.Entry e : this.entries) {
            if (e.getName().equals(name)) {
                return e;
            }
        }

        return null;
    }

    public IoFilterChain.Entry getEntry(IoFilter filter) {
        for (IoFilterChain.Entry e : this.entries) {
            if (e.getFilter() == filter) {
                return e;
            }
        }

        return null;
    }

    public IoFilterChain.Entry getEntry(Class<? extends IoFilter> filterType) {
        for (IoFilterChain.Entry e : this.entries) {
            if (filterType.isAssignableFrom(e.getFilter().getClass())) {
                return e;
            }
        }

        return null;
    }

    public IoFilter get(String name) {
        IoFilterChain.Entry e = getEntry(name);
        if (e == null) {
            return null;
        }

        return e.getFilter();
    }

    public IoFilter get(Class<? extends IoFilter> filterType) {
        IoFilterChain.Entry e = getEntry(filterType);
        if (e == null) {
            return null;
        }

        return e.getFilter();
    }

    public List<IoFilterChain.Entry> getAll() {
        return new ArrayList<IoFilterChain.Entry>(this.entries);
    }

    public List<IoFilterChain.Entry> getAllReversed() {
        List<IoFilterChain.Entry> result = getAll();
        Collections.reverse(result);
        return result;
    }

    public boolean contains(String name) {
        return (getEntry(name) != null);
    }

    public boolean contains(IoFilter filter) {
        return (getEntry(filter) != null);
    }

    public boolean contains(Class<? extends IoFilter> filterType) {
        return (getEntry(filterType) != null);
    }

    public synchronized void addFirst(String name, IoFilter filter) {
        register(0, new EntryImpl(name, filter));
    }

    public synchronized void addLast(String name, IoFilter filter) {
        register(this.entries.size(), new EntryImpl(name, filter));
    }

    public synchronized void addBefore(String baseName, String name, IoFilter filter) {
        checkBaseName(baseName);

        for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
            IoFilterChain.Entry base = i.next();
            if (base.getName().equals(baseName)) {
                register(i.previousIndex(), new EntryImpl(name, filter));
                break;
            }
        }
    }

    public synchronized void addAfter(String baseName, String name, IoFilter filter) {
        checkBaseName(baseName);

        for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
            IoFilterChain.Entry base = i.next();
            if (base.getName().equals(baseName)) {
                register(i.nextIndex(), new EntryImpl(name, filter));
                break;
            }
        }
    }

    public synchronized IoFilter remove(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }

        for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
            IoFilterChain.Entry e = i.next();
            if (e.getName().equals(name)) {
                this.entries.remove(i.previousIndex());
                return e.getFilter();
            }
        }

        throw new IllegalArgumentException("Unknown filter name: " + name);
    }

    public synchronized IoFilter remove(IoFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("filter");
        }

        for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
            IoFilterChain.Entry e = i.next();
            if (e.getFilter() == filter) {
                this.entries.remove(i.previousIndex());
                return e.getFilter();
            }
        }

        throw new IllegalArgumentException("Filter not found: " + filter.getClass().getName());
    }

    public synchronized IoFilter remove(Class<? extends IoFilter> filterType) {
        if (filterType == null) {
            throw new IllegalArgumentException("filterType");
        }

        for (ListIterator<IoFilterChain.Entry> i = this.entries.listIterator(); i.hasNext(); ) {
            IoFilterChain.Entry e = i.next();
            if (filterType.isAssignableFrom(e.getFilter().getClass())) {
                this.entries.remove(i.previousIndex());
                return e.getFilter();
            }
        }

        throw new IllegalArgumentException("Filter not found: " + filterType.getName());
    }

    public synchronized IoFilter replace(String name, IoFilter newFilter) {
        checkBaseName(name);
        EntryImpl e = (EntryImpl) getEntry(name);
        IoFilter oldFilter = e.getFilter();
        e.setFilter(newFilter);
        return oldFilter;
    }

    public synchronized void replace(IoFilter oldFilter, IoFilter newFilter) {
        for (IoFilterChain.Entry e : this.entries) {
            if (e.getFilter() == oldFilter) {
                ((EntryImpl) e).setFilter(newFilter);
                return;
            }
        }
        throw new IllegalArgumentException("Filter not found: " + oldFilter.getClass().getName());
    }

    public synchronized void replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter) {
        for (IoFilterChain.Entry e : this.entries) {
            if (oldFilterType.isAssignableFrom(e.getFilter().getClass())) {
                ((EntryImpl) e).setFilter(newFilter);
                return;
            }
        }
        throw new IllegalArgumentException("Filter not found: " + oldFilterType.getName());
    }

    public synchronized void clear() {
        this.entries.clear();
    }

    public void setFilters(Map<String, ? extends IoFilter> filters) {
        if (filters == null) {
            throw new IllegalArgumentException("filters");
        }

        if (!isOrderedMap(filters)) {
            throw new IllegalArgumentException("filters is not an ordered map. Please try " + LinkedHashMap.class.getName() + ".");
        }

        filters = new LinkedHashMap<String, IoFilter>(filters);
        for (Map.Entry<String, ? extends IoFilter> e : filters.entrySet()) {
            if (e.getKey() == null) {
                throw new IllegalArgumentException("filters contains a null key.");
            }
            if (e.getValue() == null) {
                throw new IllegalArgumentException("filters contains a null value.");
            }
        }

        synchronized (this) {
            clear();
            for (Map.Entry<String, ? extends IoFilter> e : filters.entrySet()) {
                addLast(e.getKey(), e.getValue());
            }
        }
    }

    private boolean isOrderedMap(Map map) {
        Map<String, IoFilter> newMap;
        Class<?> mapType = map.getClass();
        if (LinkedHashMap.class.isAssignableFrom(mapType)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(mapType.getSimpleName() + " is an ordered map.");
            }
            return true;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(mapType.getName() + " is not a " + LinkedHashMap.class.getSimpleName());
        }

        Class<?> type = mapType;
        while (type != null) {
            for (Class<?> clazz : type.getInterfaces()) {
                if (clazz.getName().endsWith("OrderedMap")) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(mapType.getSimpleName() + " is an ordered map (guessed from that it " + " implements OrderedMap interface.)");
                    }

                    return true;
                }
            }
            type = type.getSuperclass();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(mapType.getName() + " doesn't implement OrderedMap interface.");
        }

        LOGGER.debug("Last resort; trying to create a new map instance with a default constructor and test if insertion order is maintained.");

        try {
            newMap = (Map) mapType.newInstance();
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failed to create a new map instance of '" + mapType.getName() + "'.", e);
            }
            return false;
        }

        Random rand = new Random();
        List<String> expectedNames = new ArrayList<String>();
        IoFilter dummyFilter = new IoFilterAdapter();
        for (int i = 0; i < 65536; i++) {
            String filterName;
            do {
                filterName = String.valueOf(rand.nextInt());
            } while (newMap.containsKey(filterName));

            newMap.put(filterName, dummyFilter);
            expectedNames.add(filterName);

            Iterator<String> it = expectedNames.iterator();
            for (String key : newMap.keySet()) {
                if (!((String) it.next()).equals(key)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("The specified map didn't pass the insertion order test after " + (i + 1) + " tries.");
                    }

                    return false;
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The specified map passed the insertion order test.");
        }
        return true;
    }

    public void buildFilterChain(IoFilterChain chain) throws Exception {
        for (IoFilterChain.Entry e : this.entries) {
            chain.addLast(e.getName(), e.getFilter());
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");

        boolean empty = true;

        for (IoFilterChain.Entry e : this.entries) {
            if (!empty) {
                buf.append(", ");
            } else {
                empty = false;
            }

            buf.append('(');
            buf.append(e.getName());
            buf.append(':');
            buf.append(e.getFilter());
            buf.append(')');
        }

        if (empty) {
            buf.append("empty");
        }

        buf.append(" }");

        return buf.toString();
    }

    private void checkBaseName(String baseName) {
        if (baseName == null) {
            throw new IllegalArgumentException("baseName");
        }

        if (!contains(baseName)) {
            throw new IllegalArgumentException("Unknown filter name: " + baseName);
        }
    }

    private void register(int index, IoFilterChain.Entry e) {
        if (contains(e.getName())) {
            throw new IllegalArgumentException("Other filter is using the same name: " + e.getName());
        }

        this.entries.add(index, e);
    }

    private class EntryImpl
            implements IoFilterChain.Entry {
        private final String name;
        private volatile IoFilter filter;

        private EntryImpl(String name, IoFilter filter) {
            if (name == null) {
                throw new IllegalArgumentException("name");
            }
            if (filter == null) {
                throw new IllegalArgumentException("filter");
            }

            this.name = name;
            this.filter = filter;
        }

        public String getName() {
            return this.name;
        }

        public IoFilter getFilter() {
            return this.filter;
        }

        private void setFilter(IoFilter filter) {
            this.filter = filter;
        }

        public IoFilter.NextFilter getNextFilter() {
            throw new IllegalStateException();
        }

        public String toString() {
            return "(" + getName() + ':' + this.filter + ')';
        }

        public void addAfter(String name, IoFilter filter) {
            DefaultIoFilterChainBuilder.this.addAfter(getName(), name, filter);
        }

        public void addBefore(String name, IoFilter filter) {
            DefaultIoFilterChainBuilder.this.addBefore(getName(), name, filter);
        }

        public void remove() {
            DefaultIoFilterChainBuilder.this.remove(getName());
        }

        public void replace(IoFilter newFilter) {
            DefaultIoFilterChainBuilder.this.replace(getName(), newFilter);
        }
    }
}

