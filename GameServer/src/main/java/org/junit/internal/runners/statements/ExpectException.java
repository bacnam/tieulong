package org.junit.internal.runners.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;

public class ExpectException extends Statement {
private final Statement next;
private final Class<? extends Throwable> expected;

public ExpectException(Statement next, Class<? extends Throwable> expected) {
this.next = next;
this.expected = expected;
}

public void evaluate() throws Exception {
boolean complete = false;
try {
this.next.evaluate();
complete = true;
} catch (AssumptionViolatedException e) {
throw e;
} catch (Throwable e) {
if (!this.expected.isAssignableFrom(e.getClass())) {
String message = "Unexpected exception, expected<" + this.expected.getName() + "> but was<" + e.getClass().getName() + ">";

throw new Exception(message, e);
} 
} 
if (complete)
throw new AssertionError("Expected exception: " + this.expected.getName()); 
}
}

