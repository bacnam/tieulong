package com.mchange.lang;

import java.io.PrintStream;
import java.io.PrintWriter;

public class PotentiallySecondaryRuntimeException
extends RuntimeException
implements PotentiallySecondary
{
static final String NESTED_MSG = ">>>>>>>>>> NESTED EXCEPTION >>>>>>>>";
Throwable nested;

public PotentiallySecondaryRuntimeException(String paramString, Throwable paramThrowable) {
super(paramString);
this.nested = paramThrowable;
}

public PotentiallySecondaryRuntimeException(Throwable paramThrowable) {
this("", paramThrowable);
}
public PotentiallySecondaryRuntimeException(String paramString) {
this(paramString, null);
}
public PotentiallySecondaryRuntimeException() {
this("", null);
}
public Throwable getNestedThrowable() {
return this.nested;
}

public void printStackTrace(PrintWriter paramPrintWriter) {
super.printStackTrace(paramPrintWriter);
if (this.nested != null) {

paramPrintWriter.println(">>>>>>>>>> NESTED EXCEPTION >>>>>>>>");
this.nested.printStackTrace(paramPrintWriter);
} 
}

public void printStackTrace(PrintStream paramPrintStream) {
super.printStackTrace(paramPrintStream);
if (this.nested != null) {

paramPrintStream.println("NESTED_MSG");
this.nested.printStackTrace(paramPrintStream);
} 
}

public void printStackTrace() {
printStackTrace(System.err);
}
}

