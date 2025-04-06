package org.junit.validator;

import java.util.List;
import org.junit.runners.model.TestClass;

public interface TestClassValidator {
  List<Exception> validateTestClass(TestClass paramTestClass);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/validator/TestClassValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */