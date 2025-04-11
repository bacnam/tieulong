package com.google.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

final class MultiInputStream
        extends InputStream {
    private Iterator<? extends InputSupplier<? extends InputStream>> it;
    private InputStream in;

    public MultiInputStream(Iterator<? extends InputSupplier<? extends InputStream>> it) throws IOException {
        this.it = it;
        advance();
    }

    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = ((InputSupplier<InputStream>) this.it.next()).getInput();
        }
    }

    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        int result = this.in.read();
        if (result == -1) {
            advance();
            return read();
        }
        return result;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.in == null) {
            return -1;
        }
        int result = this.in.read(b, off, len);
        if (result == -1) {
            advance();
            return read(b, off, len);
        }
        return result;
    }

    public long skip(long n) throws IOException {
        if (this.in == null || n <= 0L) {
            return 0L;
        }
        long result = this.in.skip(n);
        if (result != 0L) {
            return result;
        }
        if (read() == -1) {
            return 0L;
        }
        return 1L + this.in.skip(n - 1L);
    }
}

