package org.junit.experimental.theories.internal;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.PotentialAssignment;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Assignments {
    private final List<PotentialAssignment> assigned;
    private final List<ParameterSignature> unassigned;
    private final TestClass clazz;

    private Assignments(List<PotentialAssignment> assigned, List<ParameterSignature> unassigned, TestClass clazz) {
        this.unassigned = unassigned;
        this.assigned = assigned;
        this.clazz = clazz;
    }

    public static Assignments allUnassigned(Method testMethod, TestClass testClass) {
        List<ParameterSignature> signatures = ParameterSignature.signatures(testClass.getOnlyConstructor());

        signatures.addAll(ParameterSignature.signatures(testMethod));
        return new Assignments(new ArrayList<PotentialAssignment>(), signatures, testClass);
    }

    public boolean isComplete() {
        return (this.unassigned.size() == 0);
    }

    public ParameterSignature nextUnassigned() {
        return this.unassigned.get(0);
    }

    public Assignments assignNext(PotentialAssignment source) {
        List<PotentialAssignment> assigned = new ArrayList<PotentialAssignment>(this.assigned);

        assigned.add(source);

        return new Assignments(assigned, this.unassigned.subList(1, this.unassigned.size()), this.clazz);
    }

    public Object[] getActualValues(int start, int stop) throws PotentialAssignment.CouldNotGenerateValueException {
        Object[] values = new Object[stop - start];
        for (int i = start; i < stop; i++) {
            values[i - start] = ((PotentialAssignment) this.assigned.get(i)).getValue();
        }
        return values;
    }

    public List<PotentialAssignment> potentialsForNextUnassigned() throws Throwable {
        ParameterSignature unassigned = nextUnassigned();
        List<PotentialAssignment> assignments = getSupplier(unassigned).getValueSources(unassigned);

        if (assignments.size() == 0) {
            assignments = generateAssignmentsFromTypeAlone(unassigned);
        }

        return assignments;
    }

    private List<PotentialAssignment> generateAssignmentsFromTypeAlone(ParameterSignature unassigned) {
        Class<?> paramType = unassigned.getType();

        if (paramType.isEnum())
            return (new EnumSupplier(paramType)).getValueSources(unassigned);
        if (paramType.equals(Boolean.class) || paramType.equals(boolean.class)) {
            return (new BooleanSupplier()).getValueSources(unassigned);
        }
        return Collections.emptyList();
    }

    private ParameterSupplier getSupplier(ParameterSignature unassigned) throws Exception {
        ParametersSuppliedBy annotation = (ParametersSuppliedBy) unassigned.findDeepAnnotation(ParametersSuppliedBy.class);

        if (annotation != null) {
            return buildParameterSupplierFromClass(annotation.value());
        }
        return new AllMembersSupplier(this.clazz);
    }

    private ParameterSupplier buildParameterSupplierFromClass(Class<? extends ParameterSupplier> cls) throws Exception {
        Constructor[] arrayOfConstructor = (Constructor[]) cls.getConstructors();

        for (Constructor<?> constructor : arrayOfConstructor) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(TestClass.class)) {
                return (ParameterSupplier) constructor.newInstance(new Object[]{this.clazz});
            }
        }

        return cls.newInstance();
    }

    public Object[] getConstructorArguments() throws PotentialAssignment.CouldNotGenerateValueException {
        return getActualValues(0, getConstructorParameterCount());
    }

    public Object[] getMethodArguments() throws PotentialAssignment.CouldNotGenerateValueException {
        return getActualValues(getConstructorParameterCount(), this.assigned.size());
    }

    public Object[] getAllArguments() throws PotentialAssignment.CouldNotGenerateValueException {
        return getActualValues(0, this.assigned.size());
    }

    private int getConstructorParameterCount() {
        List<ParameterSignature> signatures = ParameterSignature.signatures(this.clazz.getOnlyConstructor());

        int constructorParameterCount = signatures.size();
        return constructorParameterCount;
    }

    public Object[] getArgumentStrings(boolean nullsOk) throws PotentialAssignment.CouldNotGenerateValueException {
        Object[] values = new Object[this.assigned.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = ((PotentialAssignment) this.assigned.get(i)).getDescription();
        }
        return values;
    }
}

