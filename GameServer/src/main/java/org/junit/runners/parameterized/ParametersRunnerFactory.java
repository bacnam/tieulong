package org.junit.runners.parameterized;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;

public interface ParametersRunnerFactory {
  Runner createRunnerForTestWithParameters(TestWithParameters paramTestWithParameters) throws InitializationError;
}

