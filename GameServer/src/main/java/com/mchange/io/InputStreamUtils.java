package com.mchange.io;

import com.mchange.util.RobustMessageLogger;

import java.io.*;

public final class InputStreamUtils {
    private static InputStream EMPTY_ISTREAM = new ByteArrayInputStream(new byte[0]);

    public static boolean compare(InputStream paramInputStream1, InputStream paramInputStream2, long paramLong) throws IOException {
        long l;
        for (l = 0L; l < paramLong; l++) {
            int i;
            if ((i = paramInputStream1.read()) != paramInputStream2.read())
                return false;
            if (i < 0)
                break;
        }
        return true;
    }

    public static boolean compare(InputStream paramInputStream1, InputStream paramInputStream2) throws IOException {
        int i = 0;
        while (i) {
            if ((i = paramInputStream1.read()) != paramInputStream2.read())
                return false;
        }
        return true;
    }

    public static byte[] getBytes(InputStream paramInputStream, int paramInt) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(paramInt);
        byte b;
        int i;
        for (b = 0, i = paramInputStream.read(); i >= 0 && b < paramInt; i = paramInputStream.read(), b++)
            byteArrayOutputStream.write(i);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getBytes(InputStream paramInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = paramInputStream.read(); i >= 0; ) {
            byteArrayOutputStream.write(i);
            i = paramInputStream.read();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String getContentsAsString(InputStream paramInputStream, String paramString) throws IOException, UnsupportedEncodingException {
        return new String(getBytes(paramInputStream), paramString);
    }

    public static String getContentsAsString(InputStream paramInputStream) throws IOException {
        try {
            return getContentsAsString(paramInputStream, System.getProperty("file.encoding", "8859_1"));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {

            throw new InternalError("You have no default character encoding, and iso-8859-1 is unsupported?!?!");
        }
    }

    public static String getContentsAsString(InputStream paramInputStream, int paramInt, String paramString) throws IOException, UnsupportedEncodingException {
        return new String(getBytes(paramInputStream, paramInt), paramString);
    }

    public static String getContentsAsString(InputStream paramInputStream, int paramInt) throws IOException {
        try {
            return getContentsAsString(paramInputStream, paramInt, System.getProperty("file.encoding", "8859_1"));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {

            throw new InternalError("You have no default character encoding, and iso-8859-1 is unsupported?!?!");
        }
    }

    public static InputStream getEmptyInputStream() {
        return EMPTY_ISTREAM;
    }

    public static void attemptClose(InputStream paramInputStream) {
        attemptClose(paramInputStream, null);
    }

    public static void attemptClose(InputStream paramInputStream, RobustMessageLogger paramRobustMessageLogger) {
        try {
            paramInputStream.close();
        } catch (IOException iOException) {
            if (paramRobustMessageLogger != null)
                paramRobustMessageLogger.log(iOException, "IOException trying to close InputStream");
        } catch (NullPointerException nullPointerException) {
            if (paramRobustMessageLogger != null)
                paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close InputStream");
        }
    }

    public static void skipFully(InputStream paramInputStream, long paramLong) throws EOFException, IOException {
        long l = 0L;
        while (l < paramLong) {

            long l1 = paramInputStream.skip(paramLong - l);
            if (l1 > 0L) {
                l += l1;
                continue;
            }
            int i = paramInputStream.read();
            if (paramInputStream.read() < 0) {
                throw new EOFException("Skipped only " + l + " bytes to end of file.");
            }
            l++;
        }
    }
}

