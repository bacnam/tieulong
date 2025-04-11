package com.google.common.io;

import com.google.common.annotations.Beta;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public final class Closeables {
    private static final Logger logger = Logger.getLogger(Closeables.class.getName());

    public static void close(@Nullable Closeable closeable, boolean swallowIOException) throws IOException {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            if (swallowIOException) {
                logger.log(Level.WARNING, "IOException thrown while closing Closeable.", e);
            } else {

                throw e;
            }
        }
    }

    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            close(closeable, true);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException should not have been thrown.", e);
        }
    }
}

