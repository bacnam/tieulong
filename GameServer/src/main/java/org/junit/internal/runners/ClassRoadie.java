package org.junit.internal.runners;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Deprecated
public class ClassRoadie {
    private final Runnable runnable;
    private RunNotifier notifier;
    private TestClass testClass;
    private Description description;

    public ClassRoadie(RunNotifier notifier, TestClass testClass, Description description, Runnable runnable) {
        this.notifier = notifier;
        this.testClass = testClass;
        this.description = description;
        this.runnable = runnable;
    }

    protected void runUnprotected() {
        this.runnable.run();
    }

    protected void addFailure(Throwable targetException) {
        this.notifier.fireTestFailure(new Failure(this.description, targetException));
    }

    public void runProtected() {

        try {
            runBefores();
            runUnprotected();
        } catch (FailedBefore e) {
        } finally {
            runAfters();
        }

    }

    private void runBefores() throws FailedBefore {
        try {
            try {
                List<Method> befores = this.testClass.getBefores();
                for (Method before : befores) {
                    before.invoke(null, new Object[0]);
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
        List<Method> afters = this.testClass.getAfters();
        for (Method after : afters) {
            try {
                after.invoke(null, new Object[0]);
            } catch (InvocationTargetException e) {
                addFailure(e.getTargetException());
            } catch (Throwable e) {
                addFailure(e);
            }
        }
    }
}

