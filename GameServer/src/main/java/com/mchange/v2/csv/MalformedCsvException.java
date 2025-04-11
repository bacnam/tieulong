package com.mchange.v2.csv;

import com.mchange.lang.PotentiallySecondaryException;

public class MalformedCsvException
        extends PotentiallySecondaryException {
    public MalformedCsvException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public MalformedCsvException(Throwable paramThrowable) {
        super(paramThrowable);
    }

    public MalformedCsvException(String paramString) {
        super(paramString);
    }

    public MalformedCsvException() {
    }
}

