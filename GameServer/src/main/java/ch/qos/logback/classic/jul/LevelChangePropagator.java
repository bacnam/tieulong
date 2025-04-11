package ch.qos.logback.classic.jul;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;

public class LevelChangePropagator
        extends ContextAwareBase
        implements LoggerContextListener, LifeCycle {
    boolean isStarted = false;
    boolean resetJUL = false;
    private Set julLoggerSet = new HashSet();

    public void setResetJUL(boolean resetJUL) {
        this.resetJUL = resetJUL;
    }

    public boolean isResetResistant() {
        return false;
    }

    public void onStart(LoggerContext context) {
    }

    public void onReset(LoggerContext context) {
    }

    public void onStop(LoggerContext context) {
    }

    public void onLevelChange(Logger logger, Level level) {
        propagate(logger, level);
    }

    private void propagate(Logger logger, Level level) {
        addInfo("Propagating " + level + " level on " + logger + " onto the JUL framework");
        Logger julLogger = JULHelper.asJULLogger(logger);

        this.julLoggerSet.add(julLogger);
        Level julLevel = JULHelper.asJULLevel(level);
        julLogger.setLevel(julLevel);
    }

    public void resetJULLevels() {
        LogManager lm = LogManager.getLogManager();

        Enumeration<String> e = lm.getLoggerNames();
        while (e.hasMoreElements()) {
            String loggerName = e.nextElement();
            Logger julLogger = lm.getLogger(loggerName);
            if (JULHelper.isRegularNonRootLogger(julLogger) && julLogger.getLevel() != null) {
                addInfo("Setting level of jul logger [" + loggerName + "] to null");
                julLogger.setLevel(null);
            }
        }
    }

    private void propagateExistingLoggerLevels() {
        LoggerContext loggerContext = (LoggerContext) this.context;
        List<Logger> loggerList = loggerContext.getLoggerList();
        for (Logger l : loggerList) {
            if (l.getLevel() != null) {
                propagate(l, l.getLevel());
            }
        }
    }

    public void start() {
        if (this.resetJUL) {
            resetJULLevels();
        }
        propagateExistingLoggerLevels();

        this.isStarted = true;
    }

    public void stop() {
        this.isStarted = false;
    }

    public boolean isStarted() {
        return this.isStarted;
    }
}

