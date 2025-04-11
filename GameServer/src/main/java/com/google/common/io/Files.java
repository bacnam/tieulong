package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.List;
import java.util.zip.Checksum;

@Beta
public final class Files {
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static InputSupplier<FileInputStream> newInputStreamSupplier(final File file) {
        Preconditions.checkNotNull(file);
        return new InputSupplier<FileInputStream>() {
            public FileInputStream getInput() throws IOException {
                return new FileInputStream(file);
            }
        };
    }

    public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file) {
        return newOutputStreamSupplier(file, false);
    }

    public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(final File file, final boolean append) {
        Preconditions.checkNotNull(file);
        return new OutputSupplier<FileOutputStream>() {
            public FileOutputStream getOutput() throws IOException {
                return new FileOutputStream(file, append);
            }
        };
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(File file, Charset charset) {
        return CharStreams.newReaderSupplier((InputSupplier) newInputStreamSupplier(file), charset);
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset) {
        return newWriterSupplier(file, charset, false);
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset, boolean append) {
        return CharStreams.newWriterSupplier((OutputSupplier) newOutputStreamSupplier(file, append), charset);
    }

    public static byte[] toByteArray(File file) throws IOException {
        Preconditions.checkArgument((file.length() <= 2147483647L));
        if (file.length() == 0L) {
            return ByteStreams.toByteArray((InputSupplier) newInputStreamSupplier(file));
        }

        byte[] b = new byte[(int) file.length()];
        boolean threw = true;
        InputStream in = new FileInputStream(file);
        try {
            ByteStreams.readFully(in, b);
            threw = false;
        } finally {
            Closeables.close(in, threw);
        }
        return b;
    }

    public static String toString(File file, Charset charset) throws IOException {
        return new String(toByteArray(file), charset.name());
    }

    public static void copy(InputSupplier<? extends InputStream> from, File to) throws IOException {
        ByteStreams.copy(from, (OutputSupplier) newOutputStreamSupplier(to));
    }

    public static void write(byte[] from, File to) throws IOException {
        ByteStreams.write(from, (OutputSupplier) newOutputStreamSupplier(to));
    }

    public static void copy(File from, OutputSupplier<? extends OutputStream> to) throws IOException {
        ByteStreams.copy((InputSupplier) newInputStreamSupplier(from), to);
    }

    public static void copy(File from, OutputStream to) throws IOException {
        ByteStreams.copy((InputSupplier) newInputStreamSupplier(from), to);
    }

    public static void copy(File from, File to) throws IOException {
        copy((InputSupplier) newInputStreamSupplier(from), to);
    }

    public static <R extends Readable & java.io.Closeable> void copy(InputSupplier<R> from, File to, Charset charset) throws IOException {

    }

    public static void write(CharSequence from, File to, Charset charset) throws IOException {
        write(from, to, charset, false);
    }

    public static void append(CharSequence from, File to, Charset charset) throws IOException {
        write(from, to, charset, true);
    }

    private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException {
        CharStreams.write(from, newWriterSupplier(to, charset, append));
    }

    public static <W extends Appendable & java.io.Closeable> void copy(File from, Charset charset, OutputSupplier<W> to) throws IOException {

    }

    public static void copy(File from, Charset charset, Appendable to) throws IOException {
        CharStreams.copy(newReaderSupplier(from, charset), to);
    }

    public static boolean equal(File file1, File file2) throws IOException {
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }

        long len1 = file1.length();
        long len2 = file2.length();
        if (len1 != 0L && len2 != 0L && len1 != len2) {
            return false;
        }
        return ByteStreams.equal((InputSupplier) newInputStreamSupplier(file1), (InputSupplier) newInputStreamSupplier(file2));
    }

    public static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        for (int counter = 0; counter < 10000; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + '✏' + ')');
    }

    public static void touch(File file) throws IOException {
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to update modification time of " + file);
        }
    }

    public static void createParentDirs(File file) throws IOException {
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return;
        }

        parent.mkdirs();
        if (!parent.isDirectory()) {
            throw new IOException("Unable to create parent directories of " + file);
        }
    }

    public static void move(File from, File to) throws IOException {
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[]{from, to});

        if (!from.renameTo(to)) {
            copy(from, to);
            if (!from.delete()) {
                if (!to.delete()) {
                    throw new IOException("Unable to delete " + to);
                }
                throw new IOException("Unable to delete " + from);
            }
        }
    }

    @Deprecated
    public static void deleteDirectoryContents(File directory) throws IOException {
        Preconditions.checkArgument(directory.isDirectory(), "Not a directory: %s", new Object[]{directory});

        if (!directory.getCanonicalPath().equals(directory.getAbsolutePath())) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Error listing files for " + directory);
        }
        for (File file : files) {
            deleteRecursively(file);
        }
    }

    @Deprecated
    public static void deleteRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryContents(file);
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

    public static String readFirstLine(File file, Charset charset) throws IOException {
        return CharStreams.readFirstLine(newReaderSupplier(file, charset));
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return CharStreams.readLines(newReaderSupplier(file, charset));
    }

    public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
        return CharStreams.readLines(newReaderSupplier(file, charset), callback);
    }

    public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
        return ByteStreams.readBytes((InputSupplier) newInputStreamSupplier(file), processor);
    }

    public static long getChecksum(File file, Checksum checksum) throws IOException {
        return ByteStreams.getChecksum((InputSupplier) newInputStreamSupplier(file), checksum);
    }

    public static byte[] getDigest(File file, MessageDigest md) throws IOException {
        return ByteStreams.getDigest((InputSupplier) newInputStreamSupplier(file), md);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        return map(file, FileChannel.MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        return map(file, mode, file.length());
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws FileNotFoundException, IOException {
        RandomAccessFile raf = new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw");

        boolean threw = true;
        try {
            MappedByteBuffer mbb = map(raf, mode, size);
            threw = false;
            return mbb;
        } finally {
            Closeables.close(raf, threw);
        }
    }

    private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException {
        FileChannel channel = raf.getChannel();

        boolean threw = true;
        try {
            MappedByteBuffer mbb = channel.map(mode, 0L, size);
            threw = false;
            return mbb;
        } finally {
            Closeables.close(channel, threw);
        }
    }

    private static boolean sep(char[] a, int pos) {
        return (pos >= a.length || a[pos] == '/');
    }
}

