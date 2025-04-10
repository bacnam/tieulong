package org.junit.internal.runners.model;

import java.util.List;
import org.junit.runners.model.MultipleFailureException;

@Deprecated
public class MultipleFailureException
extends MultipleFailureException {
public MultipleFailureException(List<Throwable> errors) {
super(errors);
}

private static final long serialVersionUID = 1L;
}

