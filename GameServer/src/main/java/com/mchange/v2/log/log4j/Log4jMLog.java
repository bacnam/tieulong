package com.mchange.v2.log.log4j;

import com.mchange.v2.log.*;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.ResourceBundle;

public final class Log4jMLog
        extends MLog {
    static final String CHECK_CLASS = "org.apache.log4j.Logger";

    public Log4jMLog() throws ClassNotFoundException {
        Class.forName("org.apache.log4j.Logger");
    }

    public MLogger getMLogger(String paramString) {
        Logger logger = Logger.getLogger(paramString);
        if (logger == null) {

            fallbackWarn(" with name '" + paramString + "'");
            return NullMLogger.instance();
        }

        return new Log4jMLogger(logger);
    }

    public MLogger getMLogger(Class paramClass) {
        Logger logger = Logger.getLogger(paramClass);
        if (logger == null) {

            fallbackWarn(" for class '" + paramClass.getName() + "'");
            return NullMLogger.instance();
        }

        return new Log4jMLogger(logger);
    }

    public MLogger getMLogger() {
        Logger logger = Logger.getRootLogger();
        if (logger == null) {

            fallbackWarn(" (root logger)");
            return NullMLogger.instance();
        }

        return new Log4jMLogger(logger);
    }

    private void fallbackWarn(String paramString) {
        FallbackMLog.getLogger().warning("Could not create or find log4j Logger" + paramString + ". " + "Using NullMLogger. All messages sent to this" + "logger will be silently ignored. You might want to fix this.");
    }

    private static final class Log4jMLogger
            implements MLogger {
        static final String FQCN = Log4jMLogger.class.getName();
        final Logger logger;
        MLevel myLevel = null;

        Log4jMLogger(Logger param1Logger) {
            this.logger = param1Logger;
        }

        private static MLevel guessMLevel(Level param1Level) {
            if (param1Level == null)
                return null;
            if (param1Level == Level.ALL)
                return MLevel.ALL;
            if (param1Level == Level.DEBUG)
                return MLevel.FINEST;
            if (param1Level == Level.ERROR)
                return MLevel.SEVERE;
            if (param1Level == Level.FATAL)
                return MLevel.SEVERE;
            if (param1Level == Level.INFO)
                return MLevel.INFO;
            if (param1Level == Level.OFF)
                return MLevel.OFF;
            if (param1Level == Level.WARN) {
                return MLevel.WARNING;
            }
            throw new IllegalArgumentException("Unknown level: " + param1Level);
        }

        private static Level level(MLevel param1MLevel) {
            if (param1MLevel == null)
                return null;
            if (param1MLevel == MLevel.ALL)
                return Level.ALL;
            if (param1MLevel == MLevel.CONFIG)
                return Level.DEBUG;
            if (param1MLevel == MLevel.FINE)
                return Level.DEBUG;
            if (param1MLevel == MLevel.FINER)
                return Level.DEBUG;
            if (param1MLevel == MLevel.FINEST)
                return Level.DEBUG;
            if (param1MLevel == MLevel.INFO)
                return Level.INFO;
            if (param1MLevel == MLevel.OFF)
                return Level.OFF;
            if (param1MLevel == MLevel.SEVERE)
                return Level.ERROR;
            if (param1MLevel == MLevel.WARNING) {
                return Level.WARN;
            }
            throw new IllegalArgumentException("Unknown MLevel: " + param1MLevel);
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

        private void log(Level param1Level, Object param1Object, Throwable param1Throwable) {
            this.logger.log(FQCN, (Priority) param1Level, param1Object, param1Throwable);
        }

        public void log(MLevel param1MLevel, String param1String) {
            log(level(param1MLevel), param1String, (Throwable) null);
        }

        public void log(MLevel param1MLevel, String param1String, Object param1Object) {
            log(level(param1MLevel), (param1String != null) ? MessageFormat.format(param1String, new Object[]{param1Object}) : null, (Throwable) null);
        }

        public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
            log(level(param1MLevel), (param1String != null) ? MessageFormat.format(param1String, param1ArrayOfObject) : null, (Throwable) null);
        }

        public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
            log(level(param1MLevel), param1String, param1Throwable);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, param1String3), (Throwable) null);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, new Object[]{param1Object}) : null), (Throwable) null);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, param1ArrayOfObject) : null), (Throwable) null);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, param1String3), param1Throwable);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)), (Throwable) null);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, new Object[]{param1Object})), (Throwable) null);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, param1ArrayOfObject)), (Throwable) null);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
            log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)), param1Throwable);
        }

        public void entering(String param1String1, String param1String2) {
            log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "entering method."), (Throwable) null);
        }

        public void entering(String param1String1, String param1String2, Object param1Object) {
            log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "entering method... param: " + param1Object.toString()), (Throwable) null);
        }

        public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
            log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "entering method... " + LogUtils.createParamsList(param1ArrayOfObject)), (Throwable) null);
        }

        public void exiting(String param1String1, String param1String2) {
            log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "exiting method."), (Throwable) null);
        }

        public void exiting(String param1String1, String param1String2, Object param1Object) {
            log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "exiting method... result: " + param1Object.toString()), (Throwable) null);
        }

        public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
            log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "throwing exception... "), param1Throwable);
        }

        public void severe(String param1String) {
            log(Level.ERROR, param1String, (Throwable) null);
        }

        public void warning(String param1String) {
            log(Level.WARN, param1String, (Throwable) null);
        }

        public void info(String param1String) {
            log(Level.INFO, param1String, (Throwable) null);
        }

        public void config(String param1String) {
            log(Level.DEBUG, param1String, (Throwable) null);
        }

        public void fine(String param1String) {
            log(Level.DEBUG, param1String, (Throwable) null);
        }

        public void finer(String param1String) {
            log(Level.DEBUG, param1String, (Throwable) null);
        }

        public void finest(String param1String) {
            log(Level.DEBUG, param1String, (Throwable) null);
        }

        public synchronized MLevel getLevel() {
            if (this.myLevel == null)
                this.myLevel = guessMLevel(this.logger.getLevel());
            return this.myLevel;
        }

        public synchronized void setLevel(MLevel param1MLevel) throws SecurityException {
            this.logger.setLevel(level(param1MLevel));
            this.myLevel = param1MLevel;
        }

        public boolean isLoggable(MLevel param1MLevel) {
            return this.logger.isEnabledFor((Priority) level(param1MLevel));
        }

        public String getName() {
            return this.logger.getName();
        }

        public void addHandler(Object param1Object) throws SecurityException {
            if (!(param1Object instanceof Appender))
                throw new IllegalArgumentException("The 'handler' " + param1Object + " is not compatible with MLogger " + this);
            this.logger.addAppender((Appender) param1Object);
        }

        public void removeHandler(Object param1Object) throws SecurityException {
            if (!(param1Object instanceof Appender))
                throw new IllegalArgumentException("The 'handler' " + param1Object + " is not compatible with MLogger " + this);
            this.logger.removeAppender((Appender) param1Object);
        }

        public Object[] getHandlers() {
            LinkedList linkedList = new LinkedList();
            for (Enumeration enumeration = this.logger.getAllAppenders(); enumeration.hasMoreElements(); )
                linkedList.add(enumeration.nextElement());
            return linkedList.toArray();
        }

        public boolean getUseParentHandlers() {
            return this.logger.getAdditivity();
        }

        public void setUseParentHandlers(boolean param1Boolean) {
            this.logger.setAdditivity(param1Boolean);
        }
    }
}

