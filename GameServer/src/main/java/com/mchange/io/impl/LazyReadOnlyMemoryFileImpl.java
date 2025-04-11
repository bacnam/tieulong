package com.mchange.io.impl;

import com.mchange.io.ReadOnlyMemoryFile;

import java.io.*;

public class LazyReadOnlyMemoryFileImpl
        implements ReadOnlyMemoryFile {
    File file;
    byte[] bytes = null;
    long last_mod = -1L;
    int last_len = -1;

    public LazyReadOnlyMemoryFileImpl(File paramFile) {
        this.file = paramFile;
    }

    public LazyReadOnlyMemoryFileImpl(String paramString) {
        this(new File(paramString));
    }

    public File getFile() {
        return this.file;
    }

    public synchronized byte[] getBytes() throws IOException {
        update();
        return this.bytes;
    }

    void update() throws IOException {
        if (this.file.lastModified() > this.last_mod) {

            if (this.bytes != null)
                this.last_len = this.bytes.length;
            refreshBytes();
        }
    }

    void refreshBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = (this.last_len > 0) ? new ByteArrayOutputStream(2 * this.last_len) : new ByteArrayOutputStream();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));

        for (int i = bufferedInputStream.read(); i >= 0; ) {
            byteArrayOutputStream.write((byte) i);
            i = bufferedInputStream.read();
        }
        this.bytes = byteArrayOutputStream.toByteArray();
        this.last_mod = this.file.lastModified();
    }
}

