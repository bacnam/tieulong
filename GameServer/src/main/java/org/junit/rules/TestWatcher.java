package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.junit.AssumptionViolatedException;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public abstract class TestWatcher
implements TestRule
{
public Statement apply(final Statement base, final Description description) {
return new Statement()
{
public void evaluate() throws Throwable {
List<Throwable> errors = new ArrayList<Throwable>();

TestWatcher.this.startingQuietly(description, errors);
try {
base.evaluate();
TestWatcher.this.succeededQuietly(description, errors);
} catch (AssumptionViolatedException e) {
errors.add(e);
TestWatcher.this.skippedQuietly(e, description, errors);
} catch (Throwable e) {
errors.add(e);
TestWatcher.this.failedQuietly(e, description, errors);
} finally {
TestWatcher.this.finishedQuietly(description, errors);
} 

MultipleFailureException.assertEmpty(errors);
}
};
}

private void succeededQuietly(Description description, List<Throwable> errors) {
try {
succeeded(description);
} catch (Throwable e) {
errors.add(e);
} 
}

private void failedQuietly(Throwable e, Description description, List<Throwable> errors) {
try {
failed(e, description);
} catch (Throwable e1) {
errors.add(e1);
} 
}

private void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {
try {
if (e instanceof AssumptionViolatedException) {
skipped((AssumptionViolatedException)e, description);
} else {
skipped(e, description);
} 
} catch (Throwable e1) {
errors.add(e1);
} 
}

private void startingQuietly(Description description, List<Throwable> errors) {
try {
starting(description);
} catch (Throwable e) {
errors.add(e);
} 
}

private void finishedQuietly(Description description, List<Throwable> errors) {
try {
finished(description);
} catch (Throwable e) {
errors.add(e);
} 
}

protected void succeeded(Description description) {}

protected void failed(Throwable e, Description description) {}

protected void skipped(AssumptionViolatedException e, Description description) {
AssumptionViolatedException assumptionViolatedException = e;
skipped((AssumptionViolatedException)assumptionViolatedException, description);
}

@Deprecated
protected void skipped(AssumptionViolatedException e, Description description) {}

protected void starting(Description description) {}

protected void finished(Description description) {}
}

