package org.junit.validator;

import org.junit.runners.model.TestClass;

import java.util.Collections;
import java.util.List;

public class PublicClassValidator
        implements TestClassValidator {
    private static final List<Exception> NO_VALIDATION_ERRORS = Collections.emptyList();

    public List<Exception> validateTestClass(TestClass testClass) {
        if (testClass.isPublic()) {
            return NO_VALIDATION_ERRORS;
        }
        return Collections.singletonList(new Exception("The class " + testClass.getName() + " is not public."));
    }
}

