package org.junit.experimental.theories.internal;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import java.util.ArrayList;
import java.util.List;

public class EnumSupplier
        extends ParameterSupplier {
    private Class<?> enumType;

    public EnumSupplier(Class<?> enumType) {
        this.enumType = enumType;
    }

    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        Object[] enumValues = this.enumType.getEnumConstants();

        List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
        for (Object value : enumValues) {
            assignments.add(PotentialAssignment.forValue(value.toString(), value));
        }

        return assignments;
    }
}

