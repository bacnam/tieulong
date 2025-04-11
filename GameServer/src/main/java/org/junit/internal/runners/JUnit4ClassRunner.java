package org.junit.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.*;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class JUnit4ClassRunner
        extends Runner
        implements Filterable, Sortable {
    private final List<Method> testMethods;
    private TestClass testClass;

    public JUnit4ClassRunner(Class<?> klass) throws InitializationError {
        this.testClass = new TestClass(klass);
        this.testMethods = getTestMethods();
        validate();
    }

    protected List<Method> getTestMethods() {
        return this.testClass.getTestMethods();
    }

    protected void validate() throws InitializationError {
        MethodValidator methodValidator = new MethodValidator(this.testClass);
        methodValidator.validateMethodsForDefaultRunner();
        methodValidator.assertValid();
    }

    public void run(final RunNotifier notifier) {
        (new ClassRoadie(notifier, this.testClass, getDescription(), new Runnable() {
            public void run() {
                JUnit4ClassRunner.this.runMethods(notifier);
            }
        })).runProtected();
    }

    protected void runMethods(RunNotifier notifier) {
        for (Method method : this.testMethods) {
            invokeTestMethod(method, notifier);
        }
    }

    public Description getDescription() {
        Description spec = Description.createSuiteDescription(getName(), classAnnotations());
        List<Method> testMethods = this.testMethods;
        for (Method method : testMethods) {
            spec.addChild(methodDescription(method));
        }
        return spec;
    }

    protected Annotation[] classAnnotations() {
        return this.testClass.getJavaClass().getAnnotations();
    }

    protected String getName() {
        return getTestClass().getName();
    }

    protected Object createTest() throws Exception {
        return getTestClass().getConstructor().newInstance(new Object[0]);
    }

    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        Object test;
        Description description = methodDescription(method);

        try {
            test = createTest();
        } catch (InvocationTargetException e) {
            testAborted(notifier, description, e.getCause());
            return;
        } catch (Exception e) {
            testAborted(notifier, description, e);
            return;
        }
        TestMethod testMethod = wrapMethod(method);
        (new MethodRoadie(test, testMethod, notifier, description)).run();
    }

    private void testAborted(RunNotifier notifier, Description description, Throwable e) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, e));
        notifier.fireTestFinished(description);
    }

    protected TestMethod wrapMethod(Method method) {
        return new TestMethod(method, this.testClass);
    }

    protected String testName(Method method) {
        return method.getName();
    }

    protected Description methodDescription(Method method) {
        return Description.createTestDescription(getTestClass().getJavaClass(), testName(method), testAnnotations(method));
    }

    protected Annotation[] testAnnotations(Method method) {
        return method.getAnnotations();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        for (Iterator<Method> iter = this.testMethods.iterator(); iter.hasNext(); ) {
            Method method = iter.next();
            if (!filter.shouldRun(methodDescription(method))) {
                iter.remove();
            }
        }
        if (this.testMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    public void sort(final Sorter sorter) {
        Collections.sort(this.testMethods, new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                return sorter.compare(JUnit4ClassRunner.this.methodDescription(o1), JUnit4ClassRunner.this.methodDescription(o2));
            }
        });
    }

    protected TestClass getTestClass() {
        return this.testClass;
    }
}

