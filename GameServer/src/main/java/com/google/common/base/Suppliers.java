package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible
public final class Suppliers
{
public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
Preconditions.checkNotNull(function);
Preconditions.checkNotNull(supplier);
return new SupplierComposition<F, T>(function, supplier);
}

private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
final Function<? super F, T> function;
final Supplier<F> supplier;
private static final long serialVersionUID = 0L;

SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
this.function = function;
this.supplier = supplier;
}

public T get() {
return this.function.apply(this.supplier.get());
}
}

public static <T> Supplier<T> memoize(Supplier<T> delegate) {
return (delegate instanceof MemoizingSupplier) ? delegate : new MemoizingSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
}

@VisibleForTesting
static class MemoizingSupplier<T>
implements Supplier<T>, Serializable
{
final Supplier<T> delegate;
volatile transient boolean initialized;
transient T value;
private static final long serialVersionUID = 0L;

MemoizingSupplier(Supplier<T> delegate) {
this.delegate = delegate;
}

public T get() {
if (!this.initialized) {
synchronized (this) {
if (!this.initialized) {
T t = this.delegate.get();
this.value = t;
this.initialized = true;
return t;
} 
} 
}
return this.value;
}
}

public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
return new ExpiringMemoizingSupplier<T>(delegate, duration, unit);
}

@VisibleForTesting
static class ExpiringMemoizingSupplier<T>
implements Supplier<T>, Serializable {
final Supplier<T> delegate;
final long durationNanos;
volatile transient T value;
volatile transient long expirationNanos;
private static final long serialVersionUID = 0L;

ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
this.durationNanos = unit.toNanos(duration);
Preconditions.checkArgument((duration > 0L));
}

public T get() {
long nanos = this.expirationNanos;
long now = Platform.systemNanoTime();
if (nanos == 0L || now - nanos >= 0L) {
synchronized (this) {
if (nanos == this.expirationNanos) {
T t = this.delegate.get();
this.value = t;
nanos = now + this.durationNanos;

this.expirationNanos = (nanos == 0L) ? 1L : nanos;
return t;
} 
} 
}
return this.value;
}
}

public static <T> Supplier<T> ofInstance(@Nullable T instance) {
return new SupplierOfInstance<T>(instance);
}

private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
final T instance;
private static final long serialVersionUID = 0L;

SupplierOfInstance(@Nullable T instance) {
this.instance = instance;
}

public T get() {
return this.instance;
}
}

public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
return new ThreadSafeSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
}

private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
final Supplier<T> delegate;
private static final long serialVersionUID = 0L;

ThreadSafeSupplier(Supplier<T> delegate) {
this.delegate = delegate;
}

public T get() {
synchronized (this.delegate) {
return this.delegate.get();
} 
}
}

@Beta
public static <T> Function<Supplier<T>, T> supplierFunction() {
return SupplierFunction.INSTANCE;
}

private enum SupplierFunction implements Function<Supplier<?>, Object> {
INSTANCE;

public Object apply(Supplier<?> input) {
return input.get();
}
}
}

