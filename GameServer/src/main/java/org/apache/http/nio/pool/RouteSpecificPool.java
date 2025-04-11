package org.apache.http.nio.pool;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.nio.reactor.SessionRequest;
import org.apache.http.pool.PoolEntry;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

import java.net.ConnectException;
import java.util.*;

@NotThreadSafe
abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>> {
    private final T route;
    private final Set<E> leased;
    private final LinkedList<E> available;
    private final Map<SessionRequest, BasicFuture<E>> pending;

    RouteSpecificPool(T route) {
        this.route = route;
        this.leased = new HashSet<E>();
        this.available = new LinkedList<E>();
        this.pending = new HashMap<SessionRequest, BasicFuture<E>>();
    }

    public T getRoute() {
        return this.route;
    }

    protected abstract E createEntry(T paramT, C paramC);

    public int getLeasedCount() {
        return this.leased.size();
    }

    public int getPendingCount() {
        return this.pending.size();
    }

    public int getAvailableCount() {
        return this.available.size();
    }

    public int getAllocatedCount() {
        return this.available.size() + this.leased.size() + this.pending.size();
    }

    public E getFree(Object state) {
        if (!this.available.isEmpty()) {
            if (state != null) {
                Iterator<E> iterator = this.available.iterator();
                while (iterator.hasNext()) {
                    PoolEntry poolEntry = (PoolEntry) iterator.next();
                    if (state.equals(poolEntry.getState())) {
                        iterator.remove();
                        this.leased.add((E) poolEntry);
                        return (E) poolEntry;
                    }
                }
            }
            Iterator<E> it = this.available.iterator();
            while (it.hasNext()) {
                PoolEntry poolEntry = (PoolEntry) it.next();
                if (poolEntry.getState() == null) {
                    it.remove();
                    this.leased.add((E) poolEntry);
                    return (E) poolEntry;
                }
            }
        }
        return null;
    }

    public E getLastUsed() {
        if (!this.available.isEmpty()) {
            return this.available.getLast();
        }
        return null;
    }

    public boolean remove(E entry) {
        Args.notNull(entry, "Pool entry");
        if (!this.available.remove(entry) &&
                !this.leased.remove(entry)) {
            return false;
        }

        return true;
    }

    public void free(E entry, boolean reusable) {
        Args.notNull(entry, "Pool entry");
        boolean found = this.leased.remove(entry);
        Asserts.check(found, "Entry %s has not been leased from this pool", entry);
        if (reusable) {
            this.available.addFirst(entry);
        }
    }

    public void addPending(SessionRequest sessionRequest, BasicFuture<E> future) {
        this.pending.put(sessionRequest, future);
    }

    private BasicFuture<E> removeRequest(SessionRequest request) {
        BasicFuture<E> future = this.pending.remove(request);
        Asserts.notNull(future, "Session request future");
        return future;
    }

    public E createEntry(SessionRequest request, C conn) {
        E entry = createEntry(this.route, conn);
        this.leased.add(entry);
        return entry;
    }

    public void completed(SessionRequest request, E entry) {
        BasicFuture<E> future = removeRequest(request);
        future.completed(entry);
    }

    public void cancelled(SessionRequest request) {
        BasicFuture<E> future = removeRequest(request);
        future.cancel(true);
    }

    public void failed(SessionRequest request, Exception ex) {
        BasicFuture<E> future = removeRequest(request);
        future.failed(ex);
    }

    public void timeout(SessionRequest request) {
        BasicFuture<E> future = removeRequest(request);
        future.failed(new ConnectException());
    }

    public void shutdown() {
        for (SessionRequest sessionRequest : this.pending.keySet()) {
            sessionRequest.cancel();
        }
        this.pending.clear();
        for (PoolEntry poolEntry : this.available) {
            poolEntry.close();
        }
        this.available.clear();
        for (PoolEntry poolEntry : this.leased) {
            poolEntry.close();
        }
        this.leased.clear();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[route: ");
        buffer.append(this.route);
        buffer.append("][leased: ");
        buffer.append(this.leased.size());
        buffer.append("][available: ");
        buffer.append(this.available.size());
        buffer.append("][pending: ");
        buffer.append(this.pending.size());
        buffer.append("]");
        return buffer.toString();
    }
}

