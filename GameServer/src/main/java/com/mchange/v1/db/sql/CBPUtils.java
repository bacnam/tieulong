package com.mchange.v1.db.sql;

public class CBPUtils {
    public static void attemptCheckin(ConnectionBundle paramConnectionBundle, ConnectionBundlePool paramConnectionBundlePool) {
        try {
            paramConnectionBundlePool.checkinBundle(paramConnectionBundle);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

