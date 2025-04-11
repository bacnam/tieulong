package org.junit.runners.model;

import org.junit.internal.Throwables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleFailureException
        extends Exception {
    private static final long serialVersionUID = 1L;
    private final List<Throwable> fErrors;

    public MultipleFailureException(List<Throwable> errors) {
        this.fErrors = new ArrayList<Throwable>(errors);
    }

    public static void assertEmpty(List<Throwable> errors) throws Exception {
        if (errors.isEmpty()) {
            return;
        }
        if (errors.size() == 1) {
            throw Throwables.rethrowAsException((Throwable) errors.get(0));
        }

        throw new org.junit.internal.runners.model.MultipleFailureException(errors);
    }

    public List<Throwable> getFailures() {
        return Collections.unmodifiableList(this.fErrors);
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder(String.format("There were %d errors:", new Object[]{Integer.valueOf(this.fErrors.size())}));

        for (Throwable e : this.fErrors) {
            sb.append(String.format("\n  %s(%s)", new Object[]{e.getClass().getName(), e.getMessage()}));
        }
        return sb.toString();
    }
}

