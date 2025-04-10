package org.junit.rules;

import java.util.concurrent.TimeUnit;
import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class Stopwatch
implements TestRule
{
private final Clock clock;
private volatile long startNanos;
private volatile long endNanos;

public Stopwatch() {
this(new Clock());
}

Stopwatch(Clock clock) {
this.clock = clock;
}

public long runtime(TimeUnit unit) {
return unit.convert(getNanos(), TimeUnit.NANOSECONDS);
}

protected void succeeded(long nanos, Description description) {}

protected void failed(long nanos, Throwable e, Description description) {}

protected void skipped(long nanos, AssumptionViolatedException e, Description description) {}

protected void finished(long nanos, Description description) {}

private long getNanos() {
if (this.startNanos == 0L) {
throw new IllegalStateException("Test has not started");
}
long currentEndNanos = this.endNanos;
if (currentEndNanos == 0L) {
currentEndNanos = this.clock.nanoTime();
}

return currentEndNanos - this.startNanos;
}

private void starting() {
this.startNanos = this.clock.nanoTime();
this.endNanos = 0L;
}

private void stopping() {
this.endNanos = this.clock.nanoTime();
}

public final Statement apply(Statement base, Description description) {
return (new InternalWatcher()).apply(base, description);
}

private class InternalWatcher
extends TestWatcher {
protected void starting(Description description) {
Stopwatch.this.starting();
}
private InternalWatcher() {}
protected void finished(Description description) {
Stopwatch.this.finished(Stopwatch.this.getNanos(), description);
}

protected void succeeded(Description description) {
Stopwatch.this.stopping();
Stopwatch.this.succeeded(Stopwatch.this.getNanos(), description);
}

protected void failed(Throwable e, Description description) {
Stopwatch.this.stopping();
Stopwatch.this.failed(Stopwatch.this.getNanos(), e, description);
}

protected void skipped(AssumptionViolatedException e, Description description) {
Stopwatch.this.stopping();
Stopwatch.this.skipped(Stopwatch.this.getNanos(), e, description);
}
}

static class Clock
{
public long nanoTime() {
return System.nanoTime();
}
}
}

