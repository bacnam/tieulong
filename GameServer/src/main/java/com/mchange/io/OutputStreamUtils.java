package com.mchange.io;

import com.mchange.util.RobustMessageLogger;
import java.io.IOException;
import java.io.OutputStream;

public final class OutputStreamUtils
{
public static void attemptClose(OutputStream paramOutputStream) {
attemptClose(paramOutputStream, null);
}

public static void attemptClose(OutputStream paramOutputStream, RobustMessageLogger paramRobustMessageLogger) {
try {
paramOutputStream.close();
} catch (IOException iOException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close OutputStream"); 
} catch (NullPointerException nullPointerException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close OutputStream"); 
} 
}
}

