package org.junit.internal;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TextListener
extends RunListener
{
private final PrintStream writer;

public TextListener(JUnitSystem system) {
this(system.out());
}

public TextListener(PrintStream writer) {
this.writer = writer;
}

public void testRunFinished(Result result) {
printHeader(result.getRunTime());
printFailures(result);
printFooter(result);
}

public void testStarted(Description description) {
this.writer.append('.');
}

public void testFailure(Failure failure) {
this.writer.append('E');
}

public void testIgnored(Description description) {
this.writer.append('I');
}

private PrintStream getWriter() {
return this.writer;
}

protected void printHeader(long runTime) {
getWriter().println();
getWriter().println("Time: " + elapsedTimeAsString(runTime));
}

protected void printFailures(Result result) {
List<Failure> failures = result.getFailures();
if (failures.size() == 0) {
return;
}
if (failures.size() == 1) {
getWriter().println("There was " + failures.size() + " failure:");
} else {
getWriter().println("There were " + failures.size() + " failures:");
} 
int i = 1;
for (Failure each : failures) {
printFailure(each, "" + i++);
}
}

protected void printFailure(Failure each, String prefix) {
getWriter().println(prefix + ") " + each.getTestHeader());
getWriter().print(each.getTrace());
}

protected void printFooter(Result result) {
if (result.wasSuccessful()) {
getWriter().println();
getWriter().print("OK");
getWriter().println(" (" + result.getRunCount() + " test" + ((result.getRunCount() == 1) ? "" : "s") + ")");
} else {

getWriter().println();
getWriter().println("FAILURES!!!");
getWriter().println("Tests run: " + result.getRunCount() + ",  Failures: " + result.getFailureCount());
} 
getWriter().println();
}

protected String elapsedTimeAsString(long runTime) {
return NumberFormat.getInstance().format(runTime / 1000.0D);
}
}

