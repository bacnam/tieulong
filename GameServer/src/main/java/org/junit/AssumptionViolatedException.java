package org.junit;

import org.hamcrest.Matcher;
import org.junit.internal.AssumptionViolatedException;

public class AssumptionViolatedException
extends AssumptionViolatedException
{
private static final long serialVersionUID = 1L;

public <T> AssumptionViolatedException(T actual, Matcher<T> matcher) {
super(actual, matcher);
}

public <T> AssumptionViolatedException(String message, T expected, Matcher<T> matcher) {
super(message, expected, matcher);
}

public AssumptionViolatedException(String message) {
super(message);
}

public AssumptionViolatedException(String assumption, Throwable t) {
super(assumption, t);
}
}

