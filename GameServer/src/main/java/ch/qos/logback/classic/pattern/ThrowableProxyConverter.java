package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThrowableProxyConverter
extends ThrowableHandlingConverter
{
protected static final int BUILDER_CAPACITY = 2048;
int lengthOption;
List<EventEvaluator<ILoggingEvent>> evaluatorList = null;
List<String> ignoredStackTraceLines = null;

int errorCount = 0;

public void start() {
String lengthStr = getFirstOption();

if (lengthStr == null) {
this.lengthOption = Integer.MAX_VALUE;
} else {
lengthStr = lengthStr.toLowerCase();
if ("full".equals(lengthStr)) {
this.lengthOption = Integer.MAX_VALUE;
} else if ("short".equals(lengthStr)) {
this.lengthOption = 1;
} else {
try {
this.lengthOption = Integer.parseInt(lengthStr);
} catch (NumberFormatException nfe) {
addError("Could not parse [" + lengthStr + "] as an integer");
this.lengthOption = Integer.MAX_VALUE;
} 
} 
} 

List<String> optionList = getOptionList();

if (optionList != null && optionList.size() > 1) {
int optionListSize = optionList.size();
for (int i = 1; i < optionListSize; i++) {
String evaluatorOrIgnoredStackTraceLine = optionList.get(i);
Context context = getContext();
Map evaluatorMap = (Map)context.getObject("EVALUATOR_MAP");
EventEvaluator<ILoggingEvent> ee = (EventEvaluator<ILoggingEvent>)evaluatorMap.get(evaluatorOrIgnoredStackTraceLine);

if (ee != null) {
addEvaluator(ee);
} else {
addIgnoreStackTraceLine(evaluatorOrIgnoredStackTraceLine);
} 
} 
} 
super.start();
}

private void addEvaluator(EventEvaluator<ILoggingEvent> ee) {
if (this.evaluatorList == null) {
this.evaluatorList = new ArrayList<EventEvaluator<ILoggingEvent>>();
}
this.evaluatorList.add(ee);
}

private void addIgnoreStackTraceLine(String ignoredStackTraceLine) {
if (this.ignoredStackTraceLines == null) {
this.ignoredStackTraceLines = new ArrayList<String>();
}
this.ignoredStackTraceLines.add(ignoredStackTraceLine);
}

public void stop() {
this.evaluatorList = null;
super.stop();
}

protected void extraData(StringBuilder builder, StackTraceElementProxy step) {}

public String convert(ILoggingEvent event) {
IThrowableProxy tp = event.getThrowableProxy();
if (tp == null) {
return "";
}

if (this.evaluatorList != null) {
boolean printStack = true;
for (int i = 0; i < this.evaluatorList.size(); i++) {
EventEvaluator<ILoggingEvent> ee = this.evaluatorList.get(i);
try {
if (ee.evaluate(event)) {
printStack = false;
break;
} 
} catch (EvaluationException eex) {
this.errorCount++;
if (this.errorCount < 4) {
addError("Exception thrown for evaluator named [" + ee.getName() + "]", (Throwable)eex);
}
else if (this.errorCount == 4) {
ErrorStatus errorStatus = new ErrorStatus("Exception thrown for evaluator named [" + ee.getName() + "].", this, (Throwable)eex);

errorStatus.add((Status)new ErrorStatus("This was the last warning about this evaluator's errors.We don't want the StatusManager to get flooded.", this));

addStatus((Status)errorStatus);
} 
} 
} 

if (!printStack) {
return "";
}
} 

return throwableProxyToString(tp);
}

protected String throwableProxyToString(IThrowableProxy tp) {
StringBuilder sb = new StringBuilder(2048);

recursiveAppend(sb, (String)null, 1, tp);

return sb.toString();
}

private void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
if (tp == null)
return; 
subjoinFirstLine(sb, prefix, indent, tp);
sb.append(CoreConstants.LINE_SEPARATOR);
subjoinSTEPArray(sb, indent, tp);
IThrowableProxy[] suppressed = tp.getSuppressed();
if (suppressed != null) {
for (IThrowableProxy current : suppressed) {
recursiveAppend(sb, "Suppressed: ", indent + 1, current);
}
}
recursiveAppend(sb, "Caused by: ", indent, tp.getCause());
}

private void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
ThrowableProxyUtil.indent(buf, indent - 1);
if (prefix != null) {
buf.append(prefix);
}
subjoinExceptionMessage(buf, tp);
}

private void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
}

protected void subjoinSTEPArray(StringBuilder buf, int indent, IThrowableProxy tp) {
StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
int commonFrames = tp.getCommonFrames();

boolean unrestrictedPrinting = (this.lengthOption > stepArray.length);

int maxIndex = unrestrictedPrinting ? stepArray.length : this.lengthOption;
if (commonFrames > 0 && unrestrictedPrinting) {
maxIndex -= commonFrames;
}

int ignoredCount = 0;
for (int i = 0; i < maxIndex; i++) {
StackTraceElementProxy element = stepArray[i];
if (!isIgnoredStackTraceLine(element.toString())) {
ThrowableProxyUtil.indent(buf, indent);
printStackLine(buf, ignoredCount, element);
ignoredCount = 0;
buf.append(CoreConstants.LINE_SEPARATOR);
} else {
ignoredCount++;
if (maxIndex < stepArray.length) {
maxIndex++;
}
} 
} 
if (ignoredCount > 0) {
printIgnoredCount(buf, ignoredCount);
buf.append(CoreConstants.LINE_SEPARATOR);
} 

if (commonFrames > 0 && unrestrictedPrinting) {
ThrowableProxyUtil.indent(buf, indent);
buf.append("... ").append(tp.getCommonFrames()).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
} 
}

private void printStackLine(StringBuilder buf, int ignoredCount, StackTraceElementProxy element) {
buf.append(element);
extraData(buf, element);
if (ignoredCount > 0) {
printIgnoredCount(buf, ignoredCount);
}
}

private void printIgnoredCount(StringBuilder buf, int ignoredCount) {
buf.append(" [").append(ignoredCount).append(" skipped]");
}

private boolean isIgnoredStackTraceLine(String line) {
if (this.ignoredStackTraceLines != null) {
for (String ignoredStackTraceLine : this.ignoredStackTraceLines) {
if (line.contains(ignoredStackTraceLine)) {
return true;
}
} 
}
return false;
}
}

