package org.junit.internal.builders;

import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.lang.reflect.Modifier;

public class AnnotatedBuilder
        extends RunnerBuilder {
    private static final String CONSTRUCTOR_ERROR_FORMAT = "Custom runner class %s should have a public constructor with signature %s(Class testClass)";
    private final RunnerBuilder suiteBuilder;

    public AnnotatedBuilder(RunnerBuilder suiteBuilder) {
        this.suiteBuilder = suiteBuilder;
    }

    public Runner runnerForClass(Class<?> testClass) throws Exception {
        for (Class<?> currentTestClass = testClass; currentTestClass != null;
             currentTestClass = getEnclosingClassForNonStaticMemberClass(currentTestClass)) {
            RunWith annotation = currentTestClass.<RunWith>getAnnotation(RunWith.class);
            if (annotation != null) {
                return buildRunner(annotation.value(), testClass);
            }
        }

        return null;
    }

    private Class<?> getEnclosingClassForNonStaticMemberClass(Class<?> currentTestClass) {
        if (currentTestClass.isMemberClass() && !Modifier.isStatic(currentTestClass.getModifiers())) {
            return currentTestClass.getEnclosingClass();
        }
        return null;
    }

    public Runner buildRunner(Class<? extends Runner> runnerClass, Class<?> testClass) throws Exception {
        try {
            return runnerClass.getConstructor(new Class[]{Class.class}).newInstance(new Object[]{testClass});
        } catch (NoSuchMethodException e) {
            try {
                return runnerClass.getConstructor(new Class[]{Class.class, RunnerBuilder.class}).newInstance(new Object[]{testClass, this.suiteBuilder});
            } catch (NoSuchMethodException e2) {
                String simpleName = runnerClass.getSimpleName();
                throw new InitializationError(String.format("Custom runner class %s should have a public constructor with signature %s(Class testClass)", new Object[]{simpleName, simpleName}));
            }
        }
    }
}

