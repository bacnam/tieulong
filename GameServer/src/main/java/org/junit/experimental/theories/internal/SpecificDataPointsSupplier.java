package org.junit.experimental.theories.internal;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SpecificDataPointsSupplier
        extends AllMembersSupplier {
    public SpecificDataPointsSupplier(TestClass testClass) {
        super(testClass);
    }

    protected Collection<Field> getSingleDataPointFields(ParameterSignature sig) {
        Collection<Field> fields = super.getSingleDataPointFields(sig);
        String requestedName = ((FromDataPoints) sig.getAnnotation(FromDataPoints.class)).value();

        List<Field> fieldsWithMatchingNames = new ArrayList<Field>();

        for (Field field : fields) {
            String[] fieldNames = ((DataPoint) field.<DataPoint>getAnnotation(DataPoint.class)).value();
            if (Arrays.<String>asList(fieldNames).contains(requestedName)) {
                fieldsWithMatchingNames.add(field);
            }
        }

        return fieldsWithMatchingNames;
    }

    protected Collection<Field> getDataPointsFields(ParameterSignature sig) {
        Collection<Field> fields = super.getDataPointsFields(sig);
        String requestedName = ((FromDataPoints) sig.getAnnotation(FromDataPoints.class)).value();

        List<Field> fieldsWithMatchingNames = new ArrayList<Field>();

        for (Field field : fields) {
            String[] fieldNames = ((DataPoints) field.<DataPoints>getAnnotation(DataPoints.class)).value();
            if (Arrays.<String>asList(fieldNames).contains(requestedName)) {
                fieldsWithMatchingNames.add(field);
            }
        }

        return fieldsWithMatchingNames;
    }

    protected Collection<FrameworkMethod> getSingleDataPointMethods(ParameterSignature sig) {
        Collection<FrameworkMethod> methods = super.getSingleDataPointMethods(sig);
        String requestedName = ((FromDataPoints) sig.getAnnotation(FromDataPoints.class)).value();

        List<FrameworkMethod> methodsWithMatchingNames = new ArrayList<FrameworkMethod>();

        for (FrameworkMethod method : methods) {
            String[] methodNames = ((DataPoint) method.getAnnotation(DataPoint.class)).value();
            if (Arrays.<String>asList(methodNames).contains(requestedName)) {
                methodsWithMatchingNames.add(method);
            }
        }

        return methodsWithMatchingNames;
    }

    protected Collection<FrameworkMethod> getDataPointsMethods(ParameterSignature sig) {
        Collection<FrameworkMethod> methods = super.getDataPointsMethods(sig);
        String requestedName = ((FromDataPoints) sig.getAnnotation(FromDataPoints.class)).value();

        List<FrameworkMethod> methodsWithMatchingNames = new ArrayList<FrameworkMethod>();

        for (FrameworkMethod method : methods) {
            String[] methodNames = ((DataPoints) method.getAnnotation(DataPoints.class)).value();
            if (Arrays.<String>asList(methodNames).contains(requestedName)) {
                methodsWithMatchingNames.add(method);
            }
        }

        return methodsWithMatchingNames;
    }
}

