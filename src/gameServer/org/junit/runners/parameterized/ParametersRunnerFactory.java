package org.junit.runners.parameterized;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;

public interface ParametersRunnerFactory {
  Runner createRunnerForTestWithParameters(TestWithParameters paramTestWithParameters) throws InitializationError;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/runners/parameterized/ParametersRunnerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */