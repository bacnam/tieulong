package com.mchange.io;

import com.mchange.util.RobustMessageLogger;
import java.io.IOException;
import java.io.Writer;

public final class WriterUtils
{
public static void attemptClose(Writer paramWriter) {
attemptClose(paramWriter, null);
}

public static void attemptClose(Writer paramWriter, RobustMessageLogger paramRobustMessageLogger) {
try {
paramWriter.close();
} catch (IOException iOException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close Writer"); 
} catch (NullPointerException nullPointerException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close Writer"); 
} 
}
}

