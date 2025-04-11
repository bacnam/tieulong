package com.mchange.v2.lang;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

import java.lang.reflect.Method;

public final class ThreadUtils {
    static final Method holdsLock;
    private static final MLogger logger = MLog.getLogger(ThreadUtils.class);

    static {
        try {
            method = Thread.class.getMethod("holdsLock", new Class[]{Object.class});
        } catch (NoSuchMethodException noSuchMethodException) {
            method = null;
        }
        holdsLock = method;
    }

    static {
        Method method;
    }

    public static void enumerateAll(Thread[] paramArrayOfThread) {
        ThreadGroupUtils.rootThreadGroup().enumerate(paramArrayOfThread);
    }

    public static Boolean reflectiveHoldsLock(Object paramObject) {
        try {
            if (holdsLock == null) {
                return null;
            }
            return (Boolean) holdsLock.invoke(null, new Object[]{paramObject});
        } catch (Exception exception) {

            if (logger.isLoggable(MLevel.FINER))
                logger.log(MLevel.FINER, "An Exception occurred while trying to call Thread.holdsLock( ... ) reflectively.", exception);
            return null;
        }
    }
}

