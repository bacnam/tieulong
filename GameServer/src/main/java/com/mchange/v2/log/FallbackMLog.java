package com.mchange.v2.log;

import com.mchange.lang.ThrowableUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class FallbackMLog
        extends MLog {
    static final MLevel DEFAULT_CUTOFF_LEVEL;
    static final String SEP = System.getProperty("line.separator");

    static {
        MLevel mLevel = null;
        String str = MLogConfig.getProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL");
        if (str != null)
            mLevel = MLevel.fromSeverity(str);
        if (mLevel == null)
            mLevel = MLevel.INFO;
        DEFAULT_CUTOFF_LEVEL = mLevel;
    }

    MLogger logger = new FallbackMLogger();

    public MLogger getMLogger(String paramString) {
        return this.logger;
    }

    public MLogger getMLogger() {
        return this.logger;
    }

    private static final class FallbackMLogger implements MLogger {
        MLevel cutoffLevel = FallbackMLog.DEFAULT_CUTOFF_LEVEL;

        private FallbackMLogger() {
        }

        private void formatrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject, Throwable param1Throwable) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(param1String3);
            if (param1String4 != null && resourceBundle != null) {

                String str = resourceBundle.getString(param1String4);
                if (str != null)
                    param1String4 = str;
            }
            format(param1MLevel, param1String1, param1String2, param1String4, param1ArrayOfObject, param1Throwable);
        }

        private void format(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject, Throwable param1Throwable) {
            System.err.println(formatString(param1MLevel, param1String1, param1String2, param1String3, param1ArrayOfObject, param1Throwable));
        }

        private String formatString(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject, Throwable param1Throwable) {
            boolean bool = (param1String2 != null && !param1String2.endsWith(")")) ? true : false;

            StringBuffer stringBuffer = new StringBuffer(256);
            stringBuffer.append(param1MLevel.getLineHeader());
            stringBuffer.append(' ');
            if (param1String1 != null && param1String2 != null) {

                stringBuffer.append('[');
                stringBuffer.append(param1String1);
                stringBuffer.append('.');
                stringBuffer.append(param1String2);
                if (bool)
                    stringBuffer.append("()");
                stringBuffer.append(']');
            } else if (param1String1 != null) {

                stringBuffer.append('[');
                stringBuffer.append(param1String1);
                stringBuffer.append(']');
            } else if (param1String2 != null) {

                stringBuffer.append('[');
                stringBuffer.append(param1String2);
                if (bool)
                    stringBuffer.append("()");
                stringBuffer.append(']');
            }
            if (param1String3 == null) {

                if (param1ArrayOfObject != null) {
                    stringBuffer.append("params: ");
                    byte b;
                    int i;
                    for (b = 0, i = param1ArrayOfObject.length; b < i; b++) {
                        if (b != 0) stringBuffer.append(", ");
                        stringBuffer.append(param1ArrayOfObject[b]);
                    }

                }

            } else if (param1ArrayOfObject == null) {
                stringBuffer.append(param1String3);
            } else {

                MessageFormat messageFormat = new MessageFormat(param1String3);
                stringBuffer.append(messageFormat.format(param1ArrayOfObject));
            }

            if (param1Throwable != null) {

                stringBuffer.append(FallbackMLog.SEP);
                stringBuffer.append(ThrowableUtils.extractStackTrace(param1Throwable));
            }

            return stringBuffer.toString();
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
            warning("Using FallbackMLog -- Filters not supported!");
        }

        public void log(MLevel param1MLevel, String param1String) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, null, null, param1String, null, null);
            }
        }

        public void log(MLevel param1MLevel, String param1String, Object param1Object) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, null, null, param1String, new Object[]{param1Object}, null);
            }
        }

        public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, null, null, param1String, param1ArrayOfObject, null);
            }
        }

        public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, null, null, param1String, null, param1Throwable);
            }
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, param1String1, param1String2, param1String3, null, null);
            }
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, param1String1, param1String2, param1String3, new Object[]{param1Object}, null);
            }
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, param1String1, param1String2, param1String3, param1ArrayOfObject, null);
            }
        }

        public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
            if (isLoggable(param1MLevel)) {
                format(param1MLevel, param1String1, param1String2, param1String3, null, param1Throwable);
            }
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
            if (isLoggable(param1MLevel)) {
                formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, null, null);
            }
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
            if (isLoggable(param1MLevel)) {
                formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, new Object[]{param1Object}, null);
            }
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
            if (isLoggable(param1MLevel)) {
                formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, param1ArrayOfObject, null);
            }
        }

        public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
            if (isLoggable(param1MLevel)) {
                formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, null, param1Throwable);
            }
        }

        public void entering(String param1String1, String param1String2) {
            if (isLoggable(MLevel.FINER)) {
                format(MLevel.FINER, param1String1, param1String2, "Entering method.", null, null);
            }
        }

        public void entering(String param1String1, String param1String2, Object param1Object) {
            if (isLoggable(MLevel.FINER)) {
                format(MLevel.FINER, param1String1, param1String2, "Entering method with argument " + param1Object, null, null);
            }
        }

        public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
            if (isLoggable(MLevel.FINER)) {
                if (param1ArrayOfObject == null) {
                    entering(param1String1, param1String2);
                } else {

                    StringBuffer stringBuffer = new StringBuffer(128);
                    stringBuffer.append("( ");
                    byte b;
                    int i;
                    for (b = 0, i = param1ArrayOfObject.length; b < i; b++) {

                        if (b != 0) stringBuffer.append(", ");
                        stringBuffer.append(param1ArrayOfObject[b]);
                    }
                    stringBuffer.append(" )");
                    format(MLevel.FINER, param1String1, param1String2, "Entering method with arguments " + stringBuffer.toString(), null, null);
                }
            }
        }

        public void exiting(String param1String1, String param1String2) {
            if (isLoggable(MLevel.FINER)) {
                format(MLevel.FINER, param1String1, param1String2, "Exiting method.", null, null);
            }
        }

        public void exiting(String param1String1, String param1String2, Object param1Object) {
            if (isLoggable(MLevel.FINER)) {
                format(MLevel.FINER, param1String1, param1String2, "Exiting method with result " + param1Object, null, null);
            }
        }

        public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
            if (isLoggable(MLevel.FINE)) {
                format(MLevel.FINE, param1String1, param1String2, "Throwing exception.", null, param1Throwable);
            }
        }

        public void severe(String param1String) {
            if (isLoggable(MLevel.SEVERE)) {
                format(MLevel.SEVERE, null, null, param1String, null, null);
            }
        }

        public void warning(String param1String) {
            if (isLoggable(MLevel.WARNING)) {
                format(MLevel.WARNING, null, null, param1String, null, null);
            }
        }

        public void info(String param1String) {
            if (isLoggable(MLevel.INFO)) {
                format(MLevel.INFO, null, null, param1String, null, null);
            }
        }

        public void config(String param1String) {
            if (isLoggable(MLevel.CONFIG)) {
                format(MLevel.CONFIG, null, null, param1String, null, null);
            }
        }

        public void fine(String param1String) {
            if (isLoggable(MLevel.FINE)) {
                format(MLevel.FINE, null, null, param1String, null, null);
            }
        }

        public void finer(String param1String) {
            if (isLoggable(MLevel.FINER)) {
                format(MLevel.FINER, null, null, param1String, null, null);
            }
        }

        public void finest(String param1String) {
            if (isLoggable(MLevel.FINEST))
                format(MLevel.FINEST, null, null, param1String, null, null);
        }

        public synchronized MLevel getLevel() {
            return this.cutoffLevel;
        }

        public void setLevel(MLevel param1MLevel) throws SecurityException {
            this.cutoffLevel = param1MLevel;
        }

        public synchronized boolean isLoggable(MLevel param1MLevel) {
            return (param1MLevel.intValue() >= this.cutoffLevel.intValue());
        }

        public String getName() {
            return "global";
        }

        public void addHandler(Object param1Object) throws SecurityException {
            warning("Using FallbackMLog -- Handlers not supported.");
        }

        public void removeHandler(Object param1Object) throws SecurityException {
            warning("Using FallbackMLog -- Handlers not supported.");
        }

        public Object[] getHandlers() {
            warning("Using FallbackMLog -- Handlers not supported.");
            return new Object[0];
        }

        public boolean getUseParentHandlers() {
            return false;
        }

        public void setUseParentHandlers(boolean param1Boolean) {
            warning("Using FallbackMLog -- Handlers not supported.");
        }
    }
}

