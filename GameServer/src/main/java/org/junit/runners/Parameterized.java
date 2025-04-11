package org.junit.runners;

import org.junit.runner.Runner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import java.lang.annotation.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Parameterized
        extends Suite {
    private static final ParametersRunnerFactory DEFAULT_FACTORY = (ParametersRunnerFactory) new BlockJUnit4ClassRunnerWithParametersFactory();

    private static final List<Runner> NO_RUNNERS = Collections.emptyList();

    private final List<Runner> runners;

    public Parameterized(Class<?> klass) throws Throwable {
        super(klass, NO_RUNNERS);
        ParametersRunnerFactory runnerFactory = getParametersRunnerFactory(klass);

        Parameters parameters = (Parameters) getParametersMethod().getAnnotation(Parameters.class);

        this.runners = Collections.unmodifiableList(createRunnersForParameters(allParameters(), parameters.name(), runnerFactory));
    }

    private static TestWithParameters createTestWithParameters(TestClass testClass, String pattern, int index, Object[] parameters) {
        String finalPattern = pattern.replaceAll("\\{index\\}", Integer.toString(index));

        String name = MessageFormat.format(finalPattern, parameters);
        return new TestWithParameters("[" + name + "]", testClass, Arrays.asList(parameters));
    }

    private ParametersRunnerFactory getParametersRunnerFactory(Class<?> klass) throws InstantiationException, IllegalAccessException {
        UseParametersRunnerFactory annotation = klass.<UseParametersRunnerFactory>getAnnotation(UseParametersRunnerFactory.class);

        if (annotation == null) {
            return DEFAULT_FACTORY;
        }
        Class<? extends ParametersRunnerFactory> factoryClass = annotation.value();

        return factoryClass.newInstance();
    }

    protected List<Runner> getChildren() {
        return this.runners;
    }

    private TestWithParameters createTestWithNotNormalizedParameters(String pattern, int index, Object parametersOrSingleParameter) {
        (new Object[1])[0] = parametersOrSingleParameter;
        Object[] parameters = (parametersOrSingleParameter instanceof Object[]) ? (Object[]) parametersOrSingleParameter : new Object[1];

        return createTestWithParameters(getTestClass(), pattern, index, parameters);
    }

    private Iterable<Object> allParameters() throws Throwable {
        Object parameters = getParametersMethod().invokeExplosively(null, new Object[0]);
        if (parameters instanceof Iterable)
            return (Iterable<Object>) parameters;
        if (parameters instanceof Object[]) {
            return Arrays.asList((Object[]) parameters);
        }
        throw parametersMethodReturnedWrongType();
    }

    private FrameworkMethod getParametersMethod() throws Exception {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Parameters.class);

        for (FrameworkMethod each : methods) {
            if (each.isStatic() && each.isPublic()) {
                return each;
            }
        }

        throw new Exception("No public static parameters method on class " + getTestClass().getName());
    }

    private List<Runner> createRunnersForParameters(Iterable<Object> allParameters, String namePattern, ParametersRunnerFactory runnerFactory) throws InitializationError, Exception {
        try {
            List<TestWithParameters> tests = createTestsForParameters(allParameters, namePattern);

            List<Runner> runners = new ArrayList<Runner>();
            for (TestWithParameters test : tests) {
                runners.add(runnerFactory.createRunnerForTestWithParameters(test));
            }

            return runners;
        } catch (ClassCastException e) {
            throw parametersMethodReturnedWrongType();
        }
    }

    private List<TestWithParameters> createTestsForParameters(Iterable<Object> allParameters, String namePattern) throws Exception {
        int i = 0;
        List<TestWithParameters> children = new ArrayList<TestWithParameters>();
        for (Object parametersOfSingleTest : allParameters) {
            children.add(createTestWithNotNormalizedParameters(namePattern, i++, parametersOfSingleTest));
        }

        return children;
    }

    private Exception parametersMethodReturnedWrongType() throws Exception {
        String className = getTestClass().getName();
        String methodName = getParametersMethod().getName();
        String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", new Object[]{className, methodName});

        return new Exception(message);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Target({ElementType.TYPE})
    public static @interface UseParametersRunnerFactory {
        Class<? extends ParametersRunnerFactory> value() default BlockJUnit4ClassRunnerWithParametersFactory.class;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public static @interface Parameter {
        int value() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Parameters {
        String name() default "{index}";
    }
}

