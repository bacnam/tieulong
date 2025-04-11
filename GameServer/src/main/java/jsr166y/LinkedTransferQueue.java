package jsr166y;

import sun.misc.Unsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LinkedTransferQueue<E>
        extends AbstractQueue<E>
        implements TransferQueue<E>, Serializable {
    static final int SWEEP_THRESHOLD = 32;
    private static final long serialVersionUID = -3223113410248163686L;
    private static final boolean MP = (Runtime.getRuntime().availableProcessors() > 1);
    private static final int FRONT_SPINS = 128;
    private static final int CHAINED_SPINS = 64;
    private static final int NOW = 0;
    private static final int ASYNC = 1;
    private static final int SYNC = 2;
    private static final int TIMED = 3;
    private static final Unsafe UNSAFE = getUnsafe();
    private static final long headOffset = objectFieldOffset(UNSAFE, "head", LinkedTransferQueue.class);
    private static final long tailOffset = objectFieldOffset(UNSAFE, "tail", LinkedTransferQueue.class);
    private static final long sweepVotesOffset = objectFieldOffset(UNSAFE, "sweepVotes", LinkedTransferQueue.class);
    volatile transient Node head;
    private volatile transient Node tail;
    private volatile transient int sweepVotes;

    public LinkedTransferQueue() {
    }

    public LinkedTransferQueue(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    static <E> E cast(Object item) {
        assert item == null || item.getClass() != Node.class;
        return (E) item;
    }

    private static int spinsFor(Node pred, boolean haveData) {
        if (MP && pred != null) {
            if (pred.isData != haveData)
                return 192;
            if (pred.isMatched())
                return 128;
            if (pred.waiter == null)
                return 64;
        }
        return 0;
    }

    static long objectFieldOffset(Unsafe UNSAFE, String field, Class<?> klazz) {
        try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {

            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
        }
    }

    static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (SecurityException se) {
            try {
                return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
                    public Unsafe run() throws Exception {
                        Field f = Unsafe.class.getDeclaredField("theUnsafe");

                        f.setAccessible(true);
                        return (Unsafe) f.get((Object) null);
                    }
                });
            } catch (PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }

    private boolean casTail(Node cmp, Node val) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
    }

    private boolean casHead(Node cmp, Node val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }

    private boolean casSweepVotes(int cmp, int val) {
        return UNSAFE.compareAndSwapInt(this, sweepVotesOffset, cmp, val);
    }

    private E xfer(E e, boolean haveData, int how, long nanos) {
        if (haveData && e == null)
            throw new NullPointerException();
        Node s = null;

        while (true) {
            for (Node h = this.head, p = h; p != null; ) {
                boolean isData = p.isData;
                Object item = p.item;
                if (item != p && ((item != null)) == isData) {
                    if (isData == haveData)
                        break;
                    if (p.casItem(item, e)) {
                        for (Node q = p; q != h; ) {
                            Node node = q.next;
                            if (this.head == h && casHead(h, (node == null) ? q : node)) {
                                h.forgetNext();
                                break;
                            }
                            if ((h = this.head) == null || (q = h.next) == null || !q.isMatched()) {
                                break;
                            }
                        }
                        LockSupport.unpark(p.waiter);
                        this;
                        return cast(item);
                    }
                }
                Node n = p.next;
                p = (p != n) ? n : (h = this.head);
            }

            if (how != 0) {
                if (s == null)
                    s = new Node(e, haveData);
                Node pred = tryAppend(s, haveData);
                if (pred == null)
                    continue;
                if (how != 1)
                    return awaitMatch(s, pred, e, (how == 3), nanos);
            }
            break;
        }
        return e;
    }

    private Node tryAppend(Node s, boolean haveData) {
        Node t = this.tail, p = t;
        while (true) {
            if (p == null && (p = this.head) == null) {
                if (casHead(null, s))
                    return s;
                continue;
            }
            if (p.cannotPrecede(haveData))
                return null;
            Node n;
            if ((n = p.next) != null) {
                Node u;
                p = (p != t && t != (u = this.tail)) ? (t = u) : ((p != n) ? n : null);
                continue;
            }
            if (!p.casNext(null, s)) {
                p = p.next;
                continue;
            }
            break;
        }
        if (p != t) {

            while ((this.tail != t || !casTail(t, s)) && (t = this.tail) != null && (s = t.next) != null && (s = s.next) != null && s != t)
                ;
        }
        return p;
    }

    private E awaitMatch(Node s, Node pred, E e, boolean timed, long nanos) {
        long lastTime = timed ? System.nanoTime() : 0L;
        Thread w = Thread.currentThread();
        int spins = -1;
        ThreadLocalRandom randomYields = null;

        while (true) {
            Object item = s.item;
            if (item != e) {
                assert item != s;
                s.forgetContents();
                this;
                return cast(item);
            }
            if ((w.isInterrupted() || (timed && nanos <= 0L)) && s.casItem(e, s)) {

                unsplice(pred, s);
                return e;
            }

            if (spins < 0) {
                if ((spins = spinsFor(pred, s.isData)) > 0)
                    randomYields = ThreadLocalRandom.current();
                continue;
            }
            if (spins > 0) {
                spins--;
                if (randomYields.nextInt(64) == 0)
                    Thread.yield();
                continue;
            }
            if (s.waiter == null) {
                s.waiter = w;
                continue;
            }
            if (timed) {
                long now = System.nanoTime();
                if ((nanos -= now - lastTime) > 0L) {

                    LockSupport.parkNanos(this, nanos);
                }
                lastTime = now;

                continue;
            }

            LockSupport.park(this);
        }
    }

    final Node succ(Node p) {
        Node next = p.next;
        return (p == next) ? this.head : next;
    }

    private Node firstOfMode(boolean isData) {
        for (Node p = this.head; p != null; p = succ(p)) {
            if (!p.isMatched())
                return (p.isData == isData) ? p : null;
        }
        return null;
    }

    private E firstDataItem() {
        for (Node p = this.head; p != null; p = succ(p)) {
            Object item = p.item;
            if (p.isData) {
                if (item != null && item != p) {
                    this;
                    return cast(item);
                }
            } else if (item == null) {
                return null;
            }
        }
        return null;
    }

    private int countOfMode(boolean data) {
        int count = 0;
        for (Node p = this.head; p != null; ) {
            if (!p.isMatched()) {
                if (p.isData != data)
                    return 0;
                if (++count == Integer.MAX_VALUE)
                    break;
            }
            Node n = p.next;
            if (n != p) {
                p = n;
                continue;
            }
            count = 0;
            p = this.head;
        }

        return count;
    }

    final void unsplice(Node pred, Node s) {
        s.forgetContents();

        if (pred != null && pred != s && pred.next == s) {
            Node n = s.next;
            if (n == null || (n != s && pred.casNext(s, n) && pred.isMatched())) {

                while (true) {
                    Node h = this.head;
                    if (h == pred || h == s || h == null)
                        return;
                    if (!h.isMatched())
                        break;
                    Node hn = h.next;
                    if (hn == null)
                        return;
                    if (hn != h && casHead(h, hn))
                        h.forgetNext();
                }
                if (pred.next != pred && s.next != s) {
                    while (true) {
                        int v = this.sweepVotes;
                        if (v < 32) {
                            if (casSweepVotes(v, v + 1))
                                break;
                            continue;
                        }
                        if (casSweepVotes(v, 0)) {
                            sweep();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void sweep() {
        Node s;
        for (Node p = this.head; p != null && (s = p.next) != null; ) {
            if (p == s) {
                p = this.head;
                continue;
            }
            if (!s.isMatched()) {
                p = s;
                continue;
            }
            Node n;
            if ((n = s.next) == null) {
                break;
            }
            p.casNext(s, n);
        }
    }

    private boolean findAndRemove(Object e) {
        if (e != null) {
            for (Node pred = null, p = this.head; p != null; ) {
                Object item = p.item;
                if (p.isData) {
                    if (item != null && item != p && e.equals(item) && p.tryMatchData()) {
                        unsplice(pred, p);
                        return true;
                    }

                } else if (item == null) {
                    break;
                }
                pred = p;
                if ((p = p.next) == pred) {
                    pred = null;
                    p = this.head;
                }
            }
        }
        return false;
    }

    public void put(E e) {
        xfer(e, true, 1, 0L);
    }

    public boolean offer(E e, long timeout, TimeUnit unit) {
        xfer(e, true, 1, 0L);
        return true;
    }

    public boolean offer(E e) {
        xfer(e, true, 1, 0L);
        return true;
    }

    public boolean add(E e) {
        xfer(e, true, 1, 0L);
        return true;
    }

    public boolean tryTransfer(E e) {
        return (xfer(e, true, 0, 0L) == null);
    }

    public void transfer(E e) throws InterruptedException {
        if (xfer(e, true, 2, 0L) != null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (xfer(e, true, 3, unit.toNanos(timeout)) == null)
            return true;
        if (!Thread.interrupted())
            return false;
        throw new InterruptedException();
    }

    public E take() throws InterruptedException {
        E e = xfer((E) null, false, 2, 0L);
        if (e != null)
            return e;
        Thread.interrupted();
        throw new InterruptedException();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = xfer((E) null, false, 3, unit.toNanos(timeout));
        if (e != null || !Thread.interrupted())
            return e;
        throw new InterruptedException();
    }

    public E poll() {
        return xfer((E) null, false, 0, 0L);
    }

    public int drainTo(Collection<? super E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        E e;
        while ((e = poll()) != null) {
            c.add(e);
            n++;
        }
        return n;
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        E e;
        while (n < maxElements && (e = poll()) != null) {
            c.add(e);
            n++;
        }
        return n;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public E peek() {
        return firstDataItem();
    }

    public boolean isEmpty() {
        for (Node p = this.head; p != null; p = succ(p)) {
            if (!p.isMatched())
                return !p.isData;
        }
        return true;
    }

    public boolean hasWaitingConsumer() {
        return (firstOfMode(false) != null);
    }

    public int size() {
        return countOfMode(true);
    }

    public int getWaitingConsumerCount() {
        return countOfMode(false);
    }

    public boolean remove(Object o) {
        return findAndRemove(o);
    }

    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (E e : this) {
            s.writeObject(e);
        }
        s.writeObject(null);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        while (true) {
            E item = (E) s.readObject();
            if (item == null) {
                break;
            }
            offer(item);
        }
    }

    static final class Node {
        private static final Unsafe UNSAFE = LinkedTransferQueue.getUnsafe();
        private static final long nextOffset = LinkedTransferQueue.objectFieldOffset(UNSAFE, "next", Node.class);
        private static final long itemOffset = LinkedTransferQueue.objectFieldOffset(UNSAFE, "item", Node.class);
        private static final long waiterOffset = LinkedTransferQueue.objectFieldOffset(UNSAFE, "waiter", Node.class);
        private static final long serialVersionUID = -3375979862319811754L;
        final boolean isData;
        volatile Object item;
        volatile Node next;
        volatile Thread waiter;

        Node(Object item, boolean isData) {
            UNSAFE.putObject(this, itemOffset, item);
            this.isData = isData;
        }

        final boolean casNext(Node cmp, Node val) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }

        final boolean casItem(Object cmp, Object val) {
            assert cmp == null || cmp.getClass() != Node.class;
            return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
        }

        final void forgetNext() {
            UNSAFE.putObject(this, nextOffset, this);
        }

        final void forgetContents() {
            UNSAFE.putObject(this, itemOffset, this);
            UNSAFE.putObject(this, waiterOffset, (Object) null);
        }

        final boolean isMatched() {
            Object x = this.item;
            return (x == this || ((x == null)) == this.isData);
        }

        final boolean isUnmatchedRequest() {
            return (!this.isData && this.item == null);
        }

        final boolean cannotPrecede(boolean haveData) {
            boolean d = this.isData;
            Object x;
            return (d != haveData && (x = this.item) != this && ((x != null)) == d);
        }

        final boolean tryMatchData() {
            assert this.isData;
            Object x = this.item;
            if (x != null && x != this && casItem(x, null)) {
                LockSupport.unpark(this.waiter);
                return true;
            }
            return false;
        }
    }

    final class Itr
            implements Iterator<E> {
        private LinkedTransferQueue.Node nextNode;
        private E nextItem;
        private LinkedTransferQueue.Node lastRet;
        private LinkedTransferQueue.Node lastPred;

        Itr() {
            advance(null);
        }

        private void advance(LinkedTransferQueue.Node prev) {
            this.lastPred = this.lastRet;
            this.lastRet = prev;
            LinkedTransferQueue.Node p = (prev == null) ? LinkedTransferQueue.this.head : LinkedTransferQueue.this.succ(prev);
            for (; p != null; p = LinkedTransferQueue.this.succ(p)) {
                Object item = p.item;
                if (p.isData) {
                    if (item != null && item != p) {
                        this.nextItem = LinkedTransferQueue.cast(item);
                        this.nextNode = p;

                        return;
                    }
                } else if (item == null) {
                    break;
                }
            }
            this.nextNode = null;
        }

        public final boolean hasNext() {
            return (this.nextNode != null);
        }

        public final E next() {
            LinkedTransferQueue.Node p = this.nextNode;
            if (p == null) throw new NoSuchElementException();
            E e = this.nextItem;
            advance(p);
            return e;
        }

        public final void remove() {
            LinkedTransferQueue.Node p = this.lastRet;
            if (p == null) throw new IllegalStateException();
            if (p.tryMatchData()) {
                LinkedTransferQueue.this.unsplice(this.lastPred, p);
            }
        }
    }
}

