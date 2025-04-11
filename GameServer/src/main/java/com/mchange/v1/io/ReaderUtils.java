package com.mchange.v1.io;

import java.io.IOException;
import java.io.Reader;

public final class ReaderUtils {
    public static void attemptClose(Reader paramReader) {
        try {
            if (paramReader != null) paramReader.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

