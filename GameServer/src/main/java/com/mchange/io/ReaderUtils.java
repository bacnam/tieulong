package com.mchange.io;

import com.mchange.util.RobustMessageLogger;
import java.io.IOException;
import java.io.Reader;

public final class ReaderUtils
{
public static void attemptClose(Reader paramReader) {
attemptClose(paramReader, null);
}

public static void attemptClose(Reader paramReader, RobustMessageLogger paramRobustMessageLogger) {
try {
paramReader.close();
} catch (IOException iOException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close Reader"); 
} catch (NullPointerException nullPointerException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close Reader"); 
} 
}
}

