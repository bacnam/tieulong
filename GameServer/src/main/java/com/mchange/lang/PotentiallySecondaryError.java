package com.mchange.lang;

import java.io.PrintStream;
import java.io.PrintWriter;

public class PotentiallySecondaryError
extends Error
implements PotentiallySecondary
{
static final String NESTED_MSG = ">>>>>>>>>> NESTED THROWABLE >>>>>>>>";
Throwable nested;

public PotentiallySecondaryError(String paramString, Throwable paramThrowable) {
super(paramString);
this.nested = paramThrowable;
}

public PotentiallySecondaryError(Throwable paramThrowable) {
this("", paramThrowable);
}
public PotentiallySecondaryError(String paramString) {
this(paramString, null);
}
public PotentiallySecondaryError() {
this("", null);
}
public Throwable getNestedThrowable() {
return this.nested;
}

public void printStackTrace(PrintWriter paramPrintWriter) {
super.printStackTrace(paramPrintWriter);
if (this.nested != null) {

paramPrintWriter.println(">>>>>>>>>> NESTED THROWABLE >>>>>>>>");
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

