package org.junit.internal.runners;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestTimedOutException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.*;

@Deprecated
public class MethodRoadie {
    private final Object test;
    private final RunNotifier notifier;
    private final Description description;
    private TestMethod testMethod;

    public MethodRoadie(Object test, TestMethod method, RunNotifier notifier, Description description) {
        this.test = test;
        this.notifier = notifier;
        this.description = description;
        this.testMethod = method;
    }

    public void run() {
        if (this.testMethod.isIgnored()) {
            this.notifier.fireTestIgnored(this.description);
            return;
        }
        this.notifier.fireTestStarted(this.description);
        try {
            long timeout = this.testMethod.getTimeout();
            if (timeout > 0L) {
                runWithTimeout(timeout);
            } else {
                runTest();
            }
        } finally {
            this.notifier.fireTestFinished(this.description);
        }
    }

    private void runWithTimeout(final long timeout) {
        runBeforesThenTestThenAfters(new Runnable() {
            public void run() {
                ExecutorService service = Executors.newSingleThreadExecutor();
                Callable<Object> callable = new Callable() {
                    public Object call() throws Exception {
                        MethodRoadie.this.runTestMethod();
                        return null;
                    }
                };
                Future<Object> result = service.submit(callable);
                service.shutdown();
                try {
                    boolean terminated = service.awaitTermination(timeout, TimeUnit.MILLISECONDS);

                    if (!terminated) {
                        service.shutdownNow();
                    }
                    result.get(0L, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    MethodRoadie.this.addFailure((Throwable) new TestTimedOutException(timeout, TimeUnit.MILLISECONDS));
                } catch (Exception e) {
                    MethodRoadie.this.addFailure(e);
                }
            }
        });
    }

    public void runTest() {
        runBeforesThenTestThenAfters(new Runnable() {
            public void run() {
                MethodRoadie.this.runTestMethod();
            }
        });
    }

    public void runBeforesThenTestThenAfters(Runnable test) {

        try {
            runBefores();
            test.run();
        } catch (FailedBefore e) {
        } catch (Exception e) {
            throw new RuntimeException("test should never throw an exception to this level");
        } finally {
            runAfters();
        }

    }

    protected void runTestMethod() {
        try {
            this.testMethod.invoke(this.test);
            if (this.testMethod.expectsException()) {
                addFailure(new AssertionError("Expected exception: " + this.testMethod.getExpectedException().getName()));
            }
        } catch (InvocationTargetException e) {
            Throwable actual = e.getTargetException();
            if (actual instanceof AssumptionViolatedException)
                return;
            if (!this.testMethod.expectsException()) {
                addFailure(actual);
            } else if (this.testMethod.isUnexpected(actual)) {
                String message = "Unexpected exception, expected<" + this.testMethod.getExpectedException().getName() + "> but was<" + actual.getClass().getName() + ">";

                addFailure(new Exception(message, actual));
            }
        } catch (Throwable e) {
            addFailure(e);
        }
    }

    private void runBefores() throws FailedBefore {
        try {
            try {
                List<Method> befores = this.testMethod.getBefores();
                for (Method before : befores) {
                    before.invoke(this.test, new Object[0]);
                }
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        } catch (AssumptionViolatedException e) {
            throw new FailedBefore();
        } catch (Throwable e) {
            addFailure(e);
            throw new FailedBefore();
        }
    }

    private void runAfters() {
        List<Method> afters = this.testMethod.getAfters();
        for (Method after : afters) {
            try {
                after.invoke(this.test, new Object[0]);
            } catch (InvocationTargetException e) {
                addFailure(e.getTargetException());
            } catch (Throwable e) {
                addFailure(e);
            }
        }
    }

    protected void addFailure(Throwable e) {
        this.notifier.fireTestFailure(new Failure(this.description, e));
    }
}

