package org.junit.validator;

import org.junit.runners.model.TestClass;

import java.util.List;

public interface TestClassValidator {
    List<Exception> validateTestClass(TestClass paramTestClass);
}

