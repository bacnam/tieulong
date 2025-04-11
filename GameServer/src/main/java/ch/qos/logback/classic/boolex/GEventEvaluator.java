package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
import ch.qos.logback.core.util.FileUtil;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilationFailedException;

public class GEventEvaluator
        extends EventEvaluatorBase<ILoggingEvent> {
    String expression;
    IEvaluator delegateEvaluator;
    Script script;

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void start() {
        int errors = 0;
        if (this.expression == null || this.expression.length() == 0) {
            addError("Empty expression");
            return;
        }
        addInfo("Expression to evaluate [" + this.expression + "]");

        ClassLoader classLoader = getClass().getClassLoader();
        String currentPackageName = getClass().getPackage().getName();
        currentPackageName = currentPackageName.replace('.', '/');

        FileUtil fileUtil = new FileUtil(getContext());
        String scriptText = fileUtil.resourceAsString(classLoader, currentPackageName + "/EvaluatorTemplate.groovy");
        if (scriptText == null) {
            return;
        }

        //scriptText = scriptText.replace("

                GroovyClassLoader gLoader = new GroovyClassLoader(classLoader);
        try {
            Class<GroovyObject> scriptClass = gLoader.parseClass(scriptText);

            GroovyObject goo = scriptClass.newInstance();
            this.delegateEvaluator = (IEvaluator) goo;
        } catch (CompilationFailedException cfe) {
            addError("Failed to compile expression [" + this.expression + "]", (Throwable) cfe);
            errors++;
        } catch (Exception e) {
            addError("Failed to compile expression [" + this.expression + "]", e);
            errors++;
        }
        if (errors == 0)
            super.start();
    }

    public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
        if (this.delegateEvaluator == null) {
            return false;
        }
        return this.delegateEvaluator.doEvaluate(event);
    }
}

