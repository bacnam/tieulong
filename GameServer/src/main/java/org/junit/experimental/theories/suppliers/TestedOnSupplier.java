package org.junit.experimental.theories.suppliers;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import java.util.ArrayList;
import java.util.List;

public class TestedOnSupplier
        extends ParameterSupplier {
    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        List<PotentialAssignment> list = new ArrayList<PotentialAssignment>();
        TestedOn testedOn = (TestedOn) sig.getAnnotation(TestedOn.class);
        int[] ints = testedOn.ints();
        for (int i : ints) {
            list.add(PotentialAssignment.forValue("ints", Integer.valueOf(i)));
        }
        return list;
    }
}

