package com.mchange.v3.filecache;

import com.mchange.v1.io.InputStreamUtils;
import com.mchange.v1.io.ReaderUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLogger;

import java.io.*;
import java.net.URL;

public enum URLFetchers
        implements URLFetcher {
    DEFAULT {
        public InputStream openStream(URL param1URL, MLogger param1MLogger) throws IOException {
            return param1URL.openStream();
        }
    },
    BUFFERED_WGET {
        public InputStream openStream(URL param1URL, MLogger param1MLogger) throws IOException {
            Process process = (new ProcessBuilder(new String[]{"wget", "-O", "-", param1URL.toString()})).start();

            BufferedInputStream bufferedInputStream = null;

            try {
                bufferedInputStream = new BufferedInputStream(process.getInputStream(), 1048576);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1048576);
                int i;
                for (i = bufferedInputStream.read(); i >= 0; i = bufferedInputStream.read()) {
                    byteArrayOutputStream.write(i);
                }
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            } finally {

                InputStreamUtils.attemptClose(bufferedInputStream);

                if (param1MLogger.isLoggable(MLevel.FINER)) {

                    BufferedReader bufferedReader = null;

                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()), 1048576);
                        StringWriter stringWriter = new StringWriter(1048576);
                        int i;
                        for (i = bufferedReader.read(); i >= 0; i = bufferedReader.read()) {
                            stringWriter.write(i);
                        }
                        param1MLogger.log(MLevel.FINER, "wget error stream for '" + param1URL + "':\n " + stringWriter.toString());
                    } finally {

                        ReaderUtils.attemptClose(bufferedReader);
                    }
                }

                try {
                    int i = process.waitFor();
                    if (i != 0) {
                        throw new IOException("wget process terminated abnormally [return code: " + i + "]");
                    }
                } catch (InterruptedException interruptedException) {

                    if (param1MLogger.isLoggable(MLevel.FINER)) {
                        param1MLogger.log(MLevel.FINER, "InterruptedException while waiting for wget to complete.", interruptedException);
                    }
                    throw new IOException("Interrupted while waiting for wget to complete: " + interruptedException);
                }
            }
        }
    };
}

