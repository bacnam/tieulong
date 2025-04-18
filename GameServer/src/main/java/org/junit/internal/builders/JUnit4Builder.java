package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerBuilder;

public class JUnit4Builder
extends RunnerBuilder {
public Runner runnerForClass(Class<?> testClass) throws Throwable {
return (Runner)new BlockJUnit4ClassRunner(testClass);
}
}

