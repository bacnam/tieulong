package com.mchange.net;

import com.mchange.util.RobustMessageLogger;
import java.io.IOException;
import java.net.Socket;

public final class SocketUtils
{
public static void attemptClose(Socket paramSocket) {
attemptClose(paramSocket, null);
}

public static void attemptClose(Socket paramSocket, RobustMessageLogger paramRobustMessageLogger) {
try {
paramSocket.close();
} catch (IOException iOException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close Socket"); 
} catch (NullPointerException nullPointerException) {
if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close Socket"); 
} 
}
}

