package com.mchange.lang;

import com.mchange.v2.lang.VersionUtils;

import java.io.PrintStream;
import java.io.PrintWriter;

public class PotentiallySecondaryException
        extends Exception
        implements PotentiallySecondary {
    static final String NESTED_MSG = ">>>>>>>>>> NESTED EXCEPTION >>>>>>>>";
    Throwable nested;

    public PotentiallySecondaryException(String paramString, Throwable paramThrowable) {
        super(paramString);
        this.nested = paramThrowable;
    }

    public PotentiallySecondaryException(Throwable paramThrowable) {
        this("", paramThrowable);
    }

    public PotentiallySecondaryException(String paramString) {
        this(paramString, null);
    }

    public PotentiallySecondaryException() {
        this("", null);
    }

    public Throwable getNestedThrowable() {
        return this.nested;
    }

    private void setNested(Throwable paramThrowable) {
        this.nested = paramThrowable;
        if (VersionUtils.isAtLeastJavaVersion14()) {
            initCause(paramThrowable);
        }
    }

    public void printStackTrace(PrintWriter paramPrintWriter) {
        super.printStackTrace(paramPrintWriter);
        if (!VersionUtils.isAtLeastJavaVersion14() && this.nested != null) {

            paramPrintWriter.println(">>>>>>>>>> NESTED EXCEPTION >>>>>>>>");
            this.nested.printStackTrace(paramPrintWriter);
        }
    }

    public void printStackTrace(PrintStream paramPrintStream) {
        super.printStackTrace(paramPrintStream);
        if (!VersionUtils.isAtLeastJavaVersion14() && this.nested != null) {

            paramPrintStream.println("NESTED_MSG");
            this.nested.printStackTrace(paramPrintStream);
        }
    }

    public void printStackTrace() {
        if (VersionUtils.isAtLeastJavaVersion14()) {
            super.printStackTrace();
        } else {
            printStackTrace(System.err);
        }
    }
}

