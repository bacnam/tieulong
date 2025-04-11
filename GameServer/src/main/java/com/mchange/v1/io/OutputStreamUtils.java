package com.mchange.v1.io;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

import java.io.IOException;
import java.io.OutputStream;

public final class OutputStreamUtils {
    private static final MLogger logger = MLog.getLogger(OutputStreamUtils.class);

    public static void attemptClose(OutputStream paramOutputStream) {
        try {
            if (paramOutputStream != null) paramOutputStream.close();
        } catch (IOException iOException) {

            if (logger.isLoggable(MLevel.WARNING))
                logger.log(MLevel.WARNING, "OutputStream close FAILED.", iOException);
        }
    }
}

