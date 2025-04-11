package com.mchange.v2.log.jdk14logging;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogConfig;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.util.DoubleWeakHashMap;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.*;

public final class Jdk14MLog
        extends MLog {
    static final String SUPPRESS_STACK_WALK_KEY = "com.mchange.v2.log.jdk14logging.suppressStackWalk";
    private static final String CHECK_CLASS = "java.util.logging.Logger";
    private static final boolean suppress_stack_walk;
    private static String[] UNKNOWN_ARRAY = new String[]{"UNKNOWN_CLASS", "UNKNOWN_METHOD"};

    static {
        String str = MLogConfig.getProperty("com.mchange.v2.log.jdk14logging.suppressStackWalk");
        if (str == null || (str = str.trim()).length() == 0) {
            suppress_stack_walk = false;

        } else if (str.equalsIgnoreCase("true")) {
            suppress_stack_walk = true;
        } else if (str.equalsIgnoreCase("false")) {
            suppress_stack_walk = false;
        } else {

            System.err.println("Bad value for com.mchange.v2.log.jdk14logging.suppressStackWalk: '" + str + "'; defaulting to 'false'.");
            suppress_stack_walk = false;
        }
    }

    private final Map namedLoggerMap = (Map) new DoubleWeakHashMap();
    MLogger global = null;

    public Jdk14MLog() throws ClassNotFoundException {
        Class.forName("java.util.logging.Logger");
    }

    private static String[] findCallingClassAndMethod() {
        StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
        byte b;
        int i;
        for (b = 0, i = arrayOfStackTraceElement.length; b < i; b++) {

            StackTraceElement stackTraceElement = arrayOfStackTraceElement[b];
            String str = stackTraceElement.getClassName();
            if (str != null && !str.startsWith("com.mchange.v2.log.jdk14logging") && !str.startsWith("com.mchange.sc.v1.log"))
                return new String[]{stackTraceElement.getClassName(), stackTraceElement.getMethodName()};
        }
        return UNKNOWN_ARRAY;
    }

    public synchronized MLogger getMLogger(String paramString) {
        paramString = paramString.intern();

        MLogger mLogger = (MLogger) this.namedLoggerMap.get(paramString);
        if (mLogger == null) {

            Logger logger = Logger.getLogger(paramString);
            mLogger = new Jdk14MLogger(logger);
            this.namedLoggerMap.put(paramString, mLogger);
        }
        return mLogger;
    }

    public synchronized MLogger getMLogger() {
        if (this.global == null)
            this.global = new Jdk14MLogger(LogManager.getLogManager().getLogger("global"));
        return this.global;
    }

    private static final class Jdk14MLogger
            implements MLogger {
        final Logger logger;
        final String name;
        final ClassAndMethodFinder cmFinder;

        Jdk14MLogger(Logger param1Logger) {
            this.logger = param1Logger;

            this.name = param1Logger.getName();

            if (Jdk14MLog.suppress_stack_walk == true) {

                this.cmFinder = new ClassAndMethodFinder() {
                    String[] fakedClassAndMethod = new String[]{this.this$0.name, ""};

                    public String[] find() {
                        return this.fakedClassAndMethod;
                    }
                };
            } else {

                this.cmFinder = new ClassAndMethodFinder() {
                    public String[] find() {
                        return Jdk14MLog.findCallingClassAndMethod();
                    }
                };
            }
        }

        private static Level level(MLevel param1MLevel) {
            return (Level) param1MLevel.asJdk14Level();
        }

        public ResourceBundle getResourceBundle() {
            return this.logger.getResourceBundle();
        }

        public String getResourceBundleName() {
            return this.logger.getResourceBundleName();
        }

        public Object getFilter() {
            return this.logger.getFilter();
        }

        public void setFilter(Object param1Object) throws SecurityException {
            if (!(param1Object instanceof Filter)) {
                throw new IllegalArgumentException("MLogger.setFilter( ... ) requires a java.util.logging.Filter. This is not enforced by the compiler only to permit building under jdk 1.3");
            }
            this.logger.setFilter((Filter) param1Object);
        }

        public void log(MLevel param1MLevel, String param1String) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String);
        }

        public void log(MLevel param1MLevel, String param1String, Object param1Object) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String, param1Object);
        }

        public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String, param1ArrayOfObject);
        }

        public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String, param1Throwable);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3, param1Object);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3, param1ArrayOfObject);
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3, param1Throwable);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4, param1Object);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4, param1ArrayOfObject);
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
            if (!this.logger.isLoggable(level(param1MLevel)))
                return;
            if (param1String1 == null && param1String2 == null) {

                String[] arrayOfString = this.cmFinder.find();
                param1String1 = arrayOfString[0];
                param1String2 = arrayOfString[1];
            }
            this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4, param1Throwable);
        }

        public void entering(String param1String1, String param1String2) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            this.logger.entering(param1String1, param1String2);
        }

        public void entering(String param1String1, String param1String2, Object param1Object) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            this.logger.entering(param1String1, param1String2, param1Object);
        }

        public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            this.logger.entering(param1String1, param1String2, param1ArrayOfObject);
        }

        public void exiting(String param1String1, String param1String2) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            this.logger.exiting(param1String1, param1String2);
        }

        public void exiting(String param1String1, String param1String2, Object param1Object) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            this.logger.exiting(param1String1, param1String2, param1Object);
        }

        public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            this.logger.throwing(param1String1, param1String2, param1Throwable);
        }

        public void severe(String param1String) {
            if (!this.logger.isLoggable(Level.SEVERE))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.SEVERE, arrayOfString[0], arrayOfString[1], param1String);
        }

        public void warning(String param1String) {
            if (!this.logger.isLoggable(Level.WARNING))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.WARNING, arrayOfString[0], arrayOfString[1], param1String);
        }

        public void info(String param1String) {
            if (!this.logger.isLoggable(Level.INFO))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.INFO, arrayOfString[0], arrayOfString[1], param1String);
        }

        public void config(String param1String) {
            if (!this.logger.isLoggable(Level.CONFIG))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.CONFIG, arrayOfString[0], arrayOfString[1], param1String);
        }

        public void fine(String param1String) {
            if (!this.logger.isLoggable(Level.FINE))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.FINE, arrayOfString[0], arrayOfString[1], param1String);
        }

        public void finer(String param1String) {
            if (!this.logger.isLoggable(Level.FINER))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.FINER, arrayOfString[0], arrayOfString[1], param1String);
        }

        public void finest(String param1String) {
            if (!this.logger.isLoggable(Level.FINEST))
                return;
            String[] arrayOfString = this.cmFinder.find();
            this.logger.logp(Level.FINEST, arrayOfString[0], arrayOfString[1], param1String);
        }

        public MLevel getLevel() {
            return MLevel.fromIntValue(this.logger.getLevel().intValue());
        }

        public void setLevel(MLevel param1MLevel) throws SecurityException {
            this.logger.setLevel(level(param1MLevel));
        }

        public boolean isLoggable(MLevel param1MLevel) {
            return this.logger.isLoggable(level(param1MLevel));
        }

        public String getName() {
            return this.name;
        }

        public void addHandler(Object param1Object) throws SecurityException {
            if (!(param1Object instanceof Handler)) {
                throw new IllegalArgumentException("MLogger.addHandler( ... ) requires a java.util.logging.Handler. This is not enforced by the compiler only to permit building under jdk 1.3");
            }
            this.logger.addHandler((Handler) param1Object);
        }

        public void removeHandler(Object param1Object) throws SecurityException {
            if (!(param1Object instanceof Handler)) {
                throw new IllegalArgumentException("MLogger.removeHandler( ... ) requires a java.util.logging.Handler. This is not enforced by the compiler only to permit building under jdk 1.3");
            }
            this.logger.removeHandler((Handler) param1Object);
        }

        public Object[] getHandlers() {
            return (Object[]) this.logger.getHandlers();
        }

        public boolean getUseParentHandlers() {
            return this.logger.getUseParentHandlers();
        }

        public void setUseParentHandlers(boolean param1Boolean) {
            this.logger.setUseParentHandlers(param1Boolean);
        }

        static interface ClassAndMethodFinder {
            String[] find();
        }
    }
}

