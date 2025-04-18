package com.mchange.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ThrowableUtils
{
public static String extractStackTrace(Throwable paramThrowable) {
StringWriter stringWriter = new StringWriter();
PrintWriter printWriter = new PrintWriter(stringWriter);
paramThrowable.printStackTrace(printWriter);
printWriter.flush();
return stringWriter.toString();
}

public static boolean isChecked(Throwable paramThrowable) {
return (paramThrowable instanceof Exception && !(paramThrowable instanceof RuntimeException));
}

public static boolean isUnchecked(Throwable paramThrowable) {
return !isChecked(paramThrowable);
}
}

