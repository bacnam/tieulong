package org.junit.internal.runners.model;

import java.util.List;

@Deprecated
public class MultipleFailureException
        extends MultipleFailureException {
    private static final long serialVersionUID = 1L;

    public MultipleFailureException(List<Throwable> errors) {
        super(errors);
    }
}

