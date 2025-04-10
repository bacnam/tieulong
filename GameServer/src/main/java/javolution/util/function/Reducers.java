package javolution.util.function;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javolution.lang.Parallelizable;
import javolution.lang.Realtime;

public class Reducers
{
@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static <E> Reducer<E> any(Class<? extends E> type) {
return new AnyReducer<E>(type);
}

private static class AnyReducer<E> implements Reducer<E> {
private final Class<? extends E> type;
private volatile E found;

public AnyReducer(Class<? extends E> type) {
this.type = type;
}

public void accept(Collection<E> param) {
Iterator<E> it = param.iterator();
while (it.hasNext() && this.found == null) {
E e = it.next();
if (this.type.isInstance(e)) {
this.found = e;
break;
} 
} 
}

public E get() {
return this.found;
}
}

@Parallelizable(mutexFree = true, comment = "Internal use of AtomicReference")
@Realtime(limit = Realtime.Limit.LINEAR)
public static <E> Reducer<E> max(Comparator<? super E> comparator) {
return new MaxReducer<E>(comparator);
}

private static class MaxReducer<E> implements Reducer<E> {
private final Comparator<? super E> cmp;
private final AtomicReference<E> max = new AtomicReference<E>(null);

public MaxReducer(Comparator<? super E> cmp) {
this.cmp = cmp;
}

public void accept(Collection<E> param) {
Iterator<E> it = param.iterator();
label15: while (it.hasNext()) {
E e = it.next();
E read = this.max.get(); while (true) {
if (read == null || this.cmp.compare(e, read) > 0) {
if (this.max.compareAndSet(read, e))
continue label15;  read = this.max.get();
continue;
} 
continue label15;
} 
} 
} public E get() {
return this.max.get();
}
}

@Parallelizable(mutexFree = true, comment = "Internal use of AtomicReference")
@Realtime(limit = Realtime.Limit.LINEAR)
public static <E> Reducer<E> min(Comparator<? super E> comparator) {
return new MinReducer<E>(comparator);
}

private static class MinReducer<E> implements Reducer<E> {
private final Comparator<? super E> cmp;
private final AtomicReference<E> min = new AtomicReference<E>(null);

public MinReducer(Comparator<? super E> cmp) {
this.cmp = cmp;
}

public void accept(Collection<E> param) {
Iterator<E> it = param.iterator();
label15: while (it.hasNext()) {
E e = it.next();
E read = this.min.get(); while (true) {
if (read == null || this.cmp.compare(e, read) < 0) {
if (this.min.compareAndSet(read, e))
continue label15;  read = this.min.get();
continue;
} 
continue label15;
} 
} 
} public E get() {
return this.min.get();
}
}

@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static Reducer<Boolean> and() {
return new AndReducer();
}

private static class AndReducer
implements Reducer<Boolean> {
volatile boolean result = true;

public void accept(Collection<Boolean> param) {
Iterator<Boolean> it = param.iterator();
while (this.result && it.hasNext()) {
if (!((Boolean)it.next()).booleanValue()) this.result = false;

} 
}

public Boolean get() {
return Boolean.valueOf(this.result);
}

private AndReducer() {}
}

@Parallelizable
@Realtime(limit = Realtime.Limit.LINEAR)
public static Reducer<Boolean> or() {
return new OrReducer();
}

private static class OrReducer
implements Reducer<Boolean> {
volatile boolean result = false;

public void accept(Collection<Boolean> param) {
Iterator<Boolean> it = param.iterator();
while (!this.result && it.hasNext()) {
if (!((Boolean)it.next()).booleanValue()) this.result = true;

} 
}

public Boolean get() {
return Boolean.valueOf(this.result);
}

private OrReducer() {}
}

@Parallelizable(comment = "Internal use of AtomicInteger")
@Realtime(limit = Realtime.Limit.LINEAR)
public static Reducer<Integer> sum() {
return new SumReducer();
}

private static class SumReducer implements Reducer<Integer> {
private final AtomicInteger sum = new AtomicInteger(0);

public void accept(Collection<Integer> param) {
Iterator<Integer> it = param.iterator();
while (it.hasNext()) {
this.sum.getAndAdd(((Integer)it.next()).intValue());
}
}

public Integer get() {
return Integer.valueOf(this.sum.get());
}

private SumReducer() {}
}
}

