package com.mchange.io;

import java.io.*;

public final class FileUtils {
    public static byte[] getBytes(File paramFile, int paramInt) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
        try {
            return InputStreamUtils.getBytes(bufferedInputStream, paramInt);
        } finally {
            InputStreamUtils.attemptClose(bufferedInputStream);
        }
    }

    public static byte[] getBytes(File paramFile) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
        try {
            return InputStreamUtils.getBytes(bufferedInputStream);
        } finally {
            InputStreamUtils.attemptClose(bufferedInputStream);
        }
    }

    public static String getContentsAsString(File paramFile, String paramString) throws IOException, UnsupportedEncodingException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
        try {
            return InputStreamUtils.getContentsAsString(bufferedInputStream, paramString);
        } finally {
            InputStreamUtils.attemptClose(bufferedInputStream);
        }
    }

    public static String getContentsAsString(File paramFile) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
        try {
            return InputStreamUtils.getContentsAsString(bufferedInputStream);
        } finally {
            InputStreamUtils.attemptClose(bufferedInputStream);
        }
    }

    public static String getContentsAsString(File paramFile, int paramInt, String paramString) throws IOException, UnsupportedEncodingException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
        try {
            return InputStreamUtils.getContentsAsString(bufferedInputStream, paramInt, paramString);
        } finally {
            InputStreamUtils.attemptClose(bufferedInputStream);
        }
    }

    public static String getContentsAsString(File paramFile, int paramInt) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
        try {
            return InputStreamUtils.getContentsAsString(bufferedInputStream, paramInt);
        } finally {
            InputStreamUtils.attemptClose(bufferedInputStream);
        }
    }
}

