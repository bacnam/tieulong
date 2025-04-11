package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
    @Deprecated
    public static <V> UninterruptibleFuture<V> makeUninterruptible(final Future<V> future) {
        Preconditions.checkNotNull(future);
        if (future instanceof UninterruptibleFuture) {
            return (UninterruptibleFuture<V>) future;
        }
        return new UninterruptibleFuture<V>() {
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            public boolean isCancelled() {
                return future.isCancelled();
            }

            public boolean isDone() {
                return future.isDone();
            }

            public V get(long timeout, TimeUnit unit) throws TimeoutException, ExecutionException {
                return Uninterruptibles.getUninterruptibly(future, timeout, unit);
            }

            public V get() throws ExecutionException {
                return Uninterruptibles.getUninterruptibly(future);
            }
        };
    }

    @Deprecated
    public static <V> ListenableFuture<V> makeListenable(Future<V> future) {
        return JdkFutureAdapters.listenInPoolThread(future);
    }

    @Deprecated
    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(Future<V> future, Function<Exception, X> mapper) {
        return new MappingCheckedFuture<V, X>(makeListenable(future), mapper);
    }

    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<Exception, X> mapper) {
        return new MappingCheckedFuture<V, X>((ListenableFuture<V>) Preconditions.checkNotNull(future), mapper);
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
        SettableFuture<V> future = SettableFuture.create();
        future.set(value);
        return future;
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
        SettableFuture<V> future = SettableFuture.create();
        future.set(value);
        return makeChecked(future, new Function<Exception, X>() {
            public X apply(Exception e) {
                throw new AssertionError("impossible");
            }
        });
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        SettableFuture<V> future = SettableFuture.create();
        future.setException(throwable);
        return future;
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(final X exception) {
        Preconditions.checkNotNull(exception);
        return makeChecked(immediateFailedFuture((Throwable) exception), new Function<Exception, X>() {
            public X apply(Exception e) {
                return (X) exception;
            }
        });
    }

    public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> input, Function<? super I, ? extends ListenableFuture<? extends O>> function) {
        return chain(input, function, MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> input, Function<? super I, ? extends ListenableFuture<? extends O>> function, Executor exec) {
        ChainingListenableFuture<I, O> chain = new ChainingListenableFuture<I, O>(function, input);

        input.addListener(chain, exec);
        return chain;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> future, Function<? super I, ? extends O> function) {
        return transform(future, function, MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> future, final Function<? super I, ? extends O> function, Executor exec) {
        Preconditions.checkNotNull(function);
        Function<I, ListenableFuture<O>> wrapperFunction = new Function<I, ListenableFuture<O>>() {
            public ListenableFuture<O> apply(I input) {
                O output = (O) function.apply(input);
                return Futures.immediateFuture(output);
            }
        };
        return chain(future, wrapperFunction, exec);
    }

    @Beta
    public static <I, O> Future<O> lazyTransform(final Future<I> future, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(function);
        return new Future<O>() {
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            public boolean isCancelled() {
                return future.isCancelled();
            }

            public boolean isDone() {
                return future.isDone();
            }

            public O get() throws InterruptedException, ExecutionException {
                return applyTransformation(future.get());
            }

            public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return applyTransformation(future.get(timeout, unit));
            }

            private O applyTransformation(I input) throws ExecutionException {
                try {
                    return (O) function.apply(input);
                } catch (Throwable t) {
                    throw new ExecutionException(t);
                }
            }
        };
    }

    @Deprecated
    public static <I, O> Future<O> transform(final Future<I> future, final Function<? super I, ? extends O> function) {
        if (future instanceof ListenableFuture) {
            return transform((ListenableFuture<I>) future, function);
        }
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(function);
        return new Future<O>() {

            private final Object lock = new Object();
            private boolean set = false;
            private O value = null;
            private ExecutionException exception = null;

            public O get() throws InterruptedException, ExecutionException {
                return apply(future.get());
            }

            public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return apply(future.get(timeout, unit));
            }

            private O apply(I raw) throws ExecutionException {
                synchronized (this.lock) {
                    if (!this.set) {
                        try {
                            this.value = (O) function.apply(raw);
                        } catch (RuntimeException e) {
                            this.exception = new ExecutionException(e);
                        } catch (Error e) {
                            this.exception = new ExecutionException(e);
                        }
                        this.set = true;
                    }

                    if (this.exception != null) {
                        throw this.exception;
                    }
                    return this.value;
                }
            }

            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            public boolean isCancelled() {
                return future.isCancelled();
            }

            public boolean isDone() {
                return future.isDone();
            }
        };
    }

    private static class ChainingListenableFuture<I, O>
            extends AbstractFuture<O>
            implements Runnable {
        private Function<? super I, ? extends ListenableFuture<? extends O>> function;

        private ListenableFuture<? extends I> inputFuture;

        private volatile ListenableFuture<? extends O> outputFuture;

        private final BlockingQueue<Boolean> mayInterruptIfRunningChannel = new LinkedBlockingQueue<Boolean>(1);

        private final CountDownLatch outputCreated = new CountDownLatch(1);

        private ChainingListenableFuture(Function<? super I, ? extends ListenableFuture<? extends O>> function, ListenableFuture<? extends I> inputFuture) {
            this.function = (Function<? super I, ? extends ListenableFuture<? extends O>>) Preconditions.checkNotNull(function);
            this.inputFuture = (ListenableFuture<? extends I>) Preconditions.checkNotNull(inputFuture);
        }

        public O get() throws InterruptedException, ExecutionException {
            if (!isDone()) {

                ListenableFuture<? extends I> inputFuture = this.inputFuture;
                if (inputFuture != null) {
                    inputFuture.get();
                }

                this.outputCreated.await();

                ListenableFuture<? extends O> outputFuture = this.outputFuture;
                if (outputFuture != null) {
                    outputFuture.get();
                }
            }
            return super.get();
        }

        public O get(long timeout, TimeUnit unit) throws TimeoutException, ExecutionException, InterruptedException {
            if (!isDone()) {

                if (unit != TimeUnit.NANOSECONDS) {
                    timeout = TimeUnit.NANOSECONDS.convert(timeout, unit);
                    unit = TimeUnit.NANOSECONDS;
                }

                ListenableFuture<? extends I> inputFuture = this.inputFuture;
                if (inputFuture != null) {
                    long l = System.nanoTime();
                    inputFuture.get(timeout, unit);
                    timeout -= Math.max(0L, System.nanoTime() - l);
                }

                long start = System.nanoTime();
                if (!this.outputCreated.await(timeout, unit)) {
                    throw new TimeoutException();
                }
                timeout -= Math.max(0L, System.nanoTime() - start);

                ListenableFuture<? extends O> outputFuture = this.outputFuture;
                if (outputFuture != null) {
                    outputFuture.get(timeout, unit);
                }
            }
            return super.get(timeout, unit);
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {

                Uninterruptibles.putUninterruptibly(this.mayInterruptIfRunningChannel, Boolean.valueOf(mayInterruptIfRunning));
                cancel(this.inputFuture, mayInterruptIfRunning);
                cancel(this.outputFuture, mayInterruptIfRunning);
                return true;
            }
            return false;
        }

        private void cancel(@Nullable Future<?> future, boolean mayInterruptIfRunning) {
            if (future != null) {
                future.cancel(mayInterruptIfRunning);
            }
        }

        public void run() {
            try {
                I sourceResult;
                try {
                    sourceResult = Uninterruptibles.getUninterruptibly((Future) this.inputFuture);
                } catch (CancellationException e) {

                    cancel(false);
                    return;
                } catch (ExecutionException e) {

                    setException(e.getCause());

                    return;
                }
                final ListenableFuture<? extends O> outputFuture = this.outputFuture = (ListenableFuture<? extends O>) this.function.apply(sourceResult);

                if (isCancelled()) {

                    outputFuture.cancel(((Boolean) Uninterruptibles.<Boolean>takeUninterruptibly(this.mayInterruptIfRunningChannel)).booleanValue());

                    this.outputFuture = null;
                    return;
                }
                outputFuture.addListener(new Runnable() {

                    public void run() {
                        try {
                            Futures.ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(outputFuture));
                        } catch (CancellationException e) {

                            Futures.ChainingListenableFuture.this.cancel(false);
                            return;
                        } catch (ExecutionException e) {

                            Futures.ChainingListenableFuture.this.setException(e.getCause());
                        } finally {

                            Futures.ChainingListenableFuture.this.outputFuture = null;
                        }
                    }
                }, MoreExecutors.sameThreadExecutor());
            } catch (UndeclaredThrowableException e) {

                setException(e.getCause());
            } catch (RuntimeException e) {

                setException(e);
            } catch (Error e) {

                setException(e);
            } finally {

                this.function = null;
                this.inputFuture = null;

                this.outputCreated.countDown();
            }
        }
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
        return new ListFuture<V>(ImmutableList.copyOf((Object[]) futures), true, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new ListFuture<V>(ImmutableList.copyOf(futures), true, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
        return new ListFuture<V>(ImmutableList.copyOf((Object[]) futures), false, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new ListFuture<V>(ImmutableList.copyOf(futures), false, MoreExecutors.sameThreadExecutor());
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.sameThreadExecutor());
    }

    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        Runnable callbackListener = new Runnable() {

            public void run() {
                try {
                    V value = Uninterruptibles.getUninterruptibly(future);
                    callback.onSuccess(value);
                } catch (ExecutionException e) {
                    callback.onFailure(e.getCause());
                } catch (RuntimeException e) {
                    callback.onFailure(e);
                } catch (Error e) {
                    callback.onFailure(e);
                }
            }
        };
        future.addListener(callbackListener, executor);
    }

    @Beta
    public static <V, X extends Exception> V get(Future<V> future, Class<X> exceptionClass) throws X {
        Preconditions.checkNotNull(future);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", new Object[]{exceptionClass});

        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        } catch (ExecutionException e) {
            wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
            throw (X) new AssertionError();
        }
    }

    @Beta
    public static <V, X extends Exception> V get(Future<V> future, long timeout, TimeUnit unit, Class<X> exceptionClass) throws X {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(unit);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", new Object[]{exceptionClass});

        try {
            return future.get(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        } catch (TimeoutException e) {
            throw newWithCause(exceptionClass, e);
        } catch (ExecutionException e) {
            wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
            throw (X) new AssertionError();
        }
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
        if (cause instanceof Error) {
            throw (X) new ExecutionError((Error) cause);
        }
        if (cause instanceof RuntimeException) {
            throw (X) new UncheckedExecutionException(cause);
        }
        throw newWithCause(exceptionClass, cause);
    }

    @Beta
    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }

    private static void wrapAndThrowUnchecked(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }

        throw new UncheckedExecutionException(cause);
    }

    private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
        List<Constructor<X>> constructors = (List) Arrays.asList(exceptionClass.getConstructors());

        for (Constructor<X> constructor : preferringStrings(constructors)) {
            Exception exception = newFromConstructor(constructor, cause);
            if (exception != null) {
                if (exception.getCause() == null) {
                    exception.initCause(cause);
                }
                return (X) exception;
            }
        }
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
        return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
    }

    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>() {
        public Boolean apply(Constructor<?> input) {
            return Boolean.valueOf(Arrays.<Class<?>>asList(input.getParameterTypes()).contains(String.class));
        }
    }).reverse();

    @Nullable
    private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if (paramType.equals(String.class)) {
                params[i] = cause.toString();
            } else if (paramType.equals(Throwable.class)) {
                params[i] = cause;
            } else {
                return null;
            }
        }
        try {
            return constructor.newInstance(params);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    private static class ListFuture<V>
            extends AbstractFuture<List<V>> {
        ImmutableList<? extends ListenableFuture<? extends V>> futures;

        final boolean allMustSucceed;

        final AtomicInteger remaining;

        List<V> values;

        ListFuture(ImmutableList<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
            this.futures = futures;
            this.values = Lists.newArrayListWithCapacity(futures.size());
            this.allMustSucceed = allMustSucceed;
            this.remaining = new AtomicInteger(futures.size());

            init(listenerExecutor);
        }

        private void init(Executor listenerExecutor) {
            addListener(new Runnable() {

                public void run() {
                    Futures.ListFuture.this.values = null;

                    Futures.ListFuture.this.futures = null;
                }
            }, MoreExecutors.sameThreadExecutor());

            if (this.futures.isEmpty()) {
                set(Lists.newArrayList(this.values));

                return;
            }

            for (int i = 0; i < this.futures.size(); i++) {
                this.values.add(null);
            }

            ImmutableList<? extends ListenableFuture<? extends V>> localFutures = this.futures;
            for (int j = 0; j < localFutures.size(); j++) {
                final ListenableFuture<? extends V> listenable = (ListenableFuture<? extends V>) localFutures.get(j);
                final int index = j;
                listenable.addListener(new Runnable() {
                    public void run() {
                        Futures.ListFuture.this.setOneValue(index, listenable);
                    }
                }, listenerExecutor);
            }
        }

        private void setOneValue(int index, Future<? extends V> future) {
            List<V> localValues = this.values;
            if (isDone() || localValues == null) {

                Preconditions.checkState(this.allMustSucceed, "Future was done before all dependencies completed");

                return;
            }

            try {
                Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");

                localValues.set(index, Uninterruptibles.getUninterruptibly((Future) future));
            } catch (CancellationException e) {
                if (this.allMustSucceed) {

                    cancel(false);
                }
            } catch (ExecutionException e) {
                if (this.allMustSucceed) {

                    setException(e.getCause());
                }
            } catch (RuntimeException e) {
                if (this.allMustSucceed) {
                    setException(e);
                }
            } catch (Error e) {

                setException(e);
            } finally {
                int newRemaining = this.remaining.decrementAndGet();
                Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
                if (newRemaining == 0) {
                    localValues = this.values;
                    if (localValues != null) {
                        set(Lists.newArrayList(localValues));
                    } else {
                        Preconditions.checkState(isDone());
                    }
                }
            }
        }

        public List<V> get() throws InterruptedException, ExecutionException {
            callAllGets();

            return super.get();
        }

        private void callAllGets() throws InterruptedException {
            ImmutableList<? extends ListenableFuture<? extends V>> immutableList = this.futures;
            if (immutableList != null && !isDone()) {
                for (ListenableFuture<? extends V> future : immutableList) {

                    while (!future.isDone()) {
                        try {
                            future.get();
                        } catch (Error e) {
                            throw e;
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Throwable e) {

                            if (this.allMustSucceed) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static class MappingCheckedFuture<V, X extends Exception>
            extends AbstractCheckedFuture<V, X> {
        final Function<Exception, X> mapper;

        MappingCheckedFuture(ListenableFuture<V> delegate, Function<Exception, X> mapper) {
            super(delegate);

            this.mapper = (Function<Exception, X>) Preconditions.checkNotNull(mapper);
        }

        protected X mapException(Exception e) {
            return (X) this.mapper.apply(e);
        }
    }
}

