package com.mchange.v1.io;

import java.io.IOException;
import java.io.Writer;

public final class WriterUtils
{
public static void attemptClose(Writer paramWriter) {
try {
if (paramWriter != null) paramWriter.close(); 
} catch (IOException iOException) {
iOException.printStackTrace();
} 
}
}

