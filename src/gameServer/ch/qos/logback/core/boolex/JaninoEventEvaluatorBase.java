package ch.qos.logback.core.boolex;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.janino.ScriptEvaluator;

public abstract class JaninoEventEvaluatorBase<E>
extends EventEvaluatorBase<E>
{
static Class<?> EXPRESSION_TYPE = boolean.class;
static Class<?>[] THROWN_EXCEPTIONS = new Class[1];
public static final int ERROR_THRESHOLD = 4;

static {
THROWN_EXCEPTIONS[0] = EvaluationException.class;
}

private String expression;
ScriptEvaluator scriptEvaluator;
private int errorCount = 0;

protected List<Matcher> matcherList = new ArrayList<Matcher>();

public void start() {
try {
assert this.context != null;
this.scriptEvaluator = new ScriptEvaluator(getDecoratedExpression(), EXPRESSION_TYPE, getParameterNames(), getParameterTypes(), THROWN_EXCEPTIONS);

super.start();
} catch (Exception e) {
addError("Could not start evaluator with expression [" + this.expression + "]", e);
} 
}

public boolean evaluate(E event) throws EvaluationException {
if (!isStarted()) {
throw new IllegalStateException("Evaluator [" + this.name + "] was called in stopped state");
}

try {
Boolean result = (Boolean)this.scriptEvaluator.evaluate(getParameterValues(event));
return result.booleanValue();
} catch (Exception ex) {
this.errorCount++;
if (this.errorCount >= 4) {
stop();
}
throw new EvaluationException("Evaluator [" + this.name + "] caused an exception", ex);
} 
}

public String getExpression() {
return this.expression;
}

public void setExpression(String expression) {
this.expression = expression;
}

public void addMatcher(Matcher matcher) {
this.matcherList.add(matcher);
}

public List<Matcher> getMatcherList() {
return this.matcherList;
}

protected abstract String getDecoratedExpression();

protected abstract String[] getParameterNames();

protected abstract Class<?>[] getParameterTypes();

protected abstract Object[] getParameterValues(E paramE);
}

