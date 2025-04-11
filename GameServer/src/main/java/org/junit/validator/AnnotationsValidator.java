package org.junit.validator;

import org.junit.runners.model.Annotatable;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AnnotationsValidator
        implements TestClassValidator {
    private static final List<AnnotatableValidator<?>> VALIDATORS = Arrays.asList((AnnotatableValidator<?>[]) new AnnotatableValidator[]{new ClassValidator(), new MethodValidator(), new FieldValidator()});

    public List<Exception> validateTestClass(TestClass testClass) {
        List<Exception> validationErrors = new ArrayList<Exception>();
        for (AnnotatableValidator<?> validator : VALIDATORS) {
            List<Exception> additionalErrors = validator.validateTestClass(testClass);

            validationErrors.addAll(additionalErrors);
        }
        return validationErrors;
    }

    private static abstract class AnnotatableValidator<T extends Annotatable> {
        private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();

        private AnnotatableValidator() {
        }

        public List<Exception> validateTestClass(TestClass testClass) {
            List<Exception> validationErrors = new ArrayList<Exception>();
            for (Annotatable annotatable : getAnnotatablesForTestClass(testClass)) {
                List<Exception> additionalErrors = validateAnnotatable((T) annotatable);
                validationErrors.addAll(additionalErrors);
            }
            return validationErrors;
        }

        abstract Iterable<T> getAnnotatablesForTestClass(TestClass param1TestClass);

        private List<Exception> validateAnnotatable(T annotatable) {
            List<Exception> validationErrors = new ArrayList<Exception>();
            for (Annotation annotation : annotatable.getAnnotations()) {
                Class<? extends Annotation> annotationType = annotation.annotationType();

                ValidateWith validateWith = annotationType.<ValidateWith>getAnnotation(ValidateWith.class);

                if (validateWith != null) {
                    AnnotationValidator annotationValidator = ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);

                    List<Exception> errors = validateAnnotatable(annotationValidator, annotatable);

                    validationErrors.addAll(errors);
                }
            }
            return validationErrors;
        }

        abstract List<Exception> validateAnnotatable(AnnotationValidator param1AnnotationValidator, T param1T);
    }

    private static class ClassValidator extends AnnotatableValidator<TestClass> {
        private ClassValidator() {
        }

        Iterable<TestClass> getAnnotatablesForTestClass(TestClass testClass) {
            return Collections.singletonList(testClass);
        }

        List<Exception> validateAnnotatable(AnnotationValidator validator, TestClass testClass) {
            return validator.validateAnnotatedClass(testClass);
        }
    }

    private static class MethodValidator
            extends AnnotatableValidator<FrameworkMethod> {
        private MethodValidator() {
        }

        Iterable<FrameworkMethod> getAnnotatablesForTestClass(TestClass testClass) {
            return testClass.getAnnotatedMethods();
        }

        List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkMethod method) {
            return validator.validateAnnotatedMethod(method);
        }
    }

    private static class FieldValidator extends AnnotatableValidator<FrameworkField> {
        private FieldValidator() {
        }

        Iterable<FrameworkField> getAnnotatablesForTestClass(TestClass testClass) {
            return testClass.getAnnotatedFields();
        }

        List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkField field) {
            return validator.validateAnnotatedField(field);
        }
    }
}

