package com.mchange.v2.log.slf4j;

import com.mchange.v2.log.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class Slf4jMLog
        extends MLog {
    static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
    static final String CHECK_CLASS = "org.slf4j.LoggerFactory";
    static final String DFLT_LOGGER_NAME = "global";
    private static final int ALL_INTVAL = MLevel.ALL.intValue();
    private static final int CONFIG_INTVAL = MLevel.CONFIG.intValue();
    private static final int FINE_INTVAL = MLevel.FINE.intValue();
    private static final int FINER_INTVAL = MLevel.FINER.intValue();
    private static final int FINEST_INTVAL = MLevel.FINEST.intValue();
    private static final int INFO_INTVAL = MLevel.INFO.intValue();
    private static final int OFF_INTVAL = MLevel.OFF.intValue();
    private static final int SEVERE_INTVAL = MLevel.SEVERE.intValue();
    private static final int WARNING_INTVAL = MLevel.WARNING.intValue();

    public Slf4jMLog() throws ClassNotFoundException {
        Class.forName("org.slf4j.LoggerFactory");
    }

    public MLogger getMLogger(String paramString) {
        Logger logger = LoggerFactory.getLogger(paramString);
        if (logger == null) {

            fallbackWarn(" with name '" + paramString + "'");
            return NullMLogger.instance();
        }

        return new Slf4jMLogger(logger);
    }

    public MLogger getMLogger() {
        Logger logger = LoggerFactory.getLogger("global");
        if (logger == null) {

            fallbackWarn(" (default, with name 'global')");
            return NullMLogger.instance();
        }

        return new Slf4jMLogger(logger);
    }

    private void fallbackWarn(String paramString) {
        FallbackMLog.getLogger().warning("Could not create or find slf4j Logger" + paramString + ". " + "Using NullMLogger. All messages sent to this" + "logger will be silently ignored. You might want to fix this.");
    }

    private static final class Slf4jMLogger
            implements MLogger {
        static final String FQCN = Slf4jMLogger.class.getName();

        final Logger logger;

        final LevelLogger traceL;

        final LevelLogger debugL;

        final LevelLogger infoL;

        final LevelLogger warnL;
        final LevelLogger errorL;
        final LevelLogger offL;
        MLevel myLevel = null;

        Slf4jMLogger(Logger param1Logger) {
            this.logger = param1Logger;
            this.traceL = new TraceLogger();
            this.debugL = new DebugLogger();
            this.infoL = new InfoLogger();
            this.warnL = new WarnLogger();
            this.errorL = new ErrorLogger();
            this.offL = new OffLogger();
        }

        private MLevel guessMLevel() {
            if (this.logger.isErrorEnabled())
                return MLevel.SEVERE;
            if (this.logger.isWarnEnabled())
                return MLevel.WARNING;
            if (this.logger.isInfoEnabled())
                return MLevel.INFO;
            if (this.logger.isDebugEnabled())
                return MLevel.FINER;
            if (this.logger.isTraceEnabled()) {
                return MLevel.FINEST;
            }
            return MLevel.OFF;
        }

        private synchronized boolean myLevelIsLoggable(int param1Int) {
            return (this.myLevel == null || param1Int >= this.myLevel.intValue());
        }

        private LevelLogger levelLogger(MLevel param1MLevel) {
            int i = param1MLevel.intValue();

            if (!myLevelIsLoggable(i)) return this.offL;

            if (i >= Slf4jMLog.SEVERE_INTVAL) return this.errorL;
            if (i >= Slf4jMLog.WARNING_INTVAL) return this.warnL;
            if (i >= Slf4jMLog.INFO_INTVAL) return this.infoL;
            if (i >= Slf4jMLog.FINER_INTVAL) return this.debugL;
            if (i >= Slf4jMLog.FINEST_INTVAL) return this.traceL;
            return this.offL;
        }

        public ResourceBundle getResourceBundle() {
            return null;
        }

        public String getResourceBundleName() {
            return null;
        }

        public Object getFilter() {
            return null;
        }

        public void setFilter(Object param1Object) throws SecurityException {
            warning("setFilter() not supported by MLogger " + getClass().getName());
        }

        public void log(MLevel param1MLevel, String param1String) {
            levelLogger(param1MLevel).log(param1String);
        }

        public void log(MLevel param1MLevel, String param1String, Object param1Object) {
            levelLogger(param1MLevel).log(param1String, param1Object);
        }

        public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
            levelLogger(param1MLevel).log(param1String, param1ArrayOfObject);
        }

        public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
            levelLogger(param1MLevel).log(param1String, param1Throwable);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, param1String3));
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, new Object[]{param1Object}) : null));
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, param1ArrayOfObject) : null));
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, param1String3), param1Throwable);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)));
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, new Object[]{param1Object})));
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, param1ArrayOfObject)));
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
            levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)), param1Throwable);
        }

        public void entering(String param1String1, String param1String2) {
            this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "entering method."));
        }

        public void entering(String param1String1, String param1String2, Object param1Object) {
            this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "entering method... param: " + param1Object.toString()));
        }

        public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
            this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "entering method... " + LogUtils.createParamsList(param1ArrayOfObject)));
        }

        public void exiting(String param1String1, String param1String2) {
            this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "exiting method."));
        }

        public void exiting(String param1String1, String param1String2, Object param1Object) {
            this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "exiting method... result: " + param1Object.toString()));
        }

        public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
            this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "throwing exception... "), param1Throwable);
        }

        public void severe(String param1String) {
            this.errorL.log(param1String);
        }

        public void warning(String param1String) {
            this.warnL.log(param1String);
        }

        public void info(String param1String) {
            this.infoL.log(param1String);
        }

        public void config(String param1String) {
            this.debugL.log(param1String);
        }

        public void fine(String param1String) {
            this.debugL.log(param1String);
        }

        public void finer(String param1String) {
            this.debugL.log(param1String);
        }

        public void finest(String param1String) {
            this.traceL.log(param1String);
        }

        public synchronized MLevel getLevel() {
            if (this.myLevel == null)
                this.myLevel = guessMLevel();
            return this.myLevel;
        }

        public synchronized void setLevel(MLevel param1MLevel) throws SecurityException {
            this.myLevel = param1MLevel;
        }

        public boolean isLoggable(MLevel param1MLevel) {
            return (levelLogger(param1MLevel) != this.offL);
        }

        public String getName() {
            return this.logger.getName();
        }

        public void addHandler(Object param1Object) throws SecurityException {
            throw new UnsupportedOperationException("Handlers not supported; the 'handler' " + param1Object + " is not compatible with MLogger " + this);
        }

        public void removeHandler(Object param1Object) throws SecurityException {
            throw new UnsupportedOperationException("Handlers not supported; the 'handler' " + param1Object + " is not compatible with MLogger " + this);
        }

        public Object[] getHandlers() {
            return Slf4jMLog.EMPTY_OBJ_ARRAY;
        }

        public boolean getUseParentHandlers() {
            throw new UnsupportedOperationException("Handlers not supported.");
        }

        public void setUseParentHandlers(boolean param1Boolean) {
            throw new UnsupportedOperationException("Handlers not supported.");
        }

        private static interface LevelLogger {
            void log(String param2String);

            void log(String param2String, Object param2Object);

            void log(String param2String, Object[] param2ArrayOfObject);

            void log(String param2String, Throwable param2Throwable);
        }

        private class OffLogger
                implements LevelLogger {
            private OffLogger() {
            }

            public void log(String param2String) {
            }

            public void log(String param2String, Object param2Object) {
            }

            public void log(String param2String, Object[] param2ArrayOfObject) {
            }

            public void log(String param2String, Throwable param2Throwable) {
            }
        }

        private class TraceLogger implements LevelLogger {
            private TraceLogger() {
            }

            public void log(String param2String) {
                Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String);
            }

            public void log(String param2String, Object param2Object) {
                Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String, param2Object);
            }

            public void log(String param2String, Object[] param2ArrayOfObject) {
                Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String, param2ArrayOfObject);
            }

            public void log(String param2String, Throwable param2Throwable) {
                Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String, param2Throwable);
            }
        }

        private class DebugLogger implements LevelLogger {
            private DebugLogger() {
            }

            public void log(String param2String) {
                Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String);
            }

            public void log(String param2String, Object param2Object) {
                Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String, param2Object);
            }

            public void log(String param2String, Object[] param2ArrayOfObject) {
                Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String, param2ArrayOfObject);
            }

            public void log(String param2String, Throwable param2Throwable) {
                Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String, param2Throwable);
            }
        }

        private class InfoLogger implements LevelLogger {
            private InfoLogger() {
            }

            public void log(String param2String) {
                Slf4jMLog.Slf4jMLogger.this.logger.info(param2String);
            }

            public void log(String param2String, Object param2Object) {
                Slf4jMLog.Slf4jMLogger.this.logger.info(param2String, param2Object);
            }

            public void log(String param2String, Object[] param2ArrayOfObject) {
                Slf4jMLog.Slf4jMLogger.this.logger.info(param2String, param2ArrayOfObject);
            }

            public void log(String param2String, Throwable param2Throwable) {
                Slf4jMLog.Slf4jMLogger.this.logger.info(param2String, param2Throwable);
            }
        }

        private class WarnLogger implements LevelLogger {
            private WarnLogger() {
            }

            public void log(String param2String) {
                Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String);
            }

            public void log(String param2String, Object param2Object) {
                Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String, param2Object);
            }

            public void log(String param2String, Object[] param2ArrayOfObject) {
                Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String, param2ArrayOfObject);
            }

            public void log(String param2String, Throwable param2Throwable) {
                Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String, param2Throwable);
            }
        }

        private class ErrorLogger implements LevelLogger {
            private ErrorLogger() {
            }

            public void log(String param2String) {
                Slf4jMLog.Slf4jMLogger.this.logger.error(param2String);
            }

            public void log(String param2String, Object param2Object) {
                Slf4jMLog.Slf4jMLogger.this.logger.error(param2String, param2Object);
            }

            public void log(String param2String, Object[] param2ArrayOfObject) {
                Slf4jMLog.Slf4jMLogger.this.logger.error(param2String, param2ArrayOfObject);
            }

            public void log(String param2String, Throwable param2Throwable) {
                Slf4jMLog.Slf4jMLogger.this.logger.error(param2String, param2Throwable);
            }
        }
    }
}

