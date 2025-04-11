package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class LimitInputStream
        extends FilterInputStream {
    private long left;
    private long mark = -1L;

    public LimitInputStream(InputStream in, long limit) {
        super(in);
        Preconditions.checkNotNull(in);
        Preconditions.checkArgument((limit >= 0L), "limit must be non-negative");
        this.left = limit;
    }

    public int available() throws IOException {
        return (int) Math.min(this.in.available(), this.left);
    }

    public void mark(int readlimit) {
        this.in.mark(readlimit);
        this.mark = this.left;
    }

    public int read() throws IOException {
        if (this.left == 0L) {
            return -1;
        }

        int result = this.in.read();
        if (result != -1) {
            this.left--;
        }
        return result;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.left == 0L) {
            return -1;
        }

        len = (int) Math.min(len, this.left);
        int result = this.in.read(b, off, len);
        if (result != -1) {
            this.left -= result;
        }
        return result;
    }

    public void reset() throws IOException {
        if (!this.in.markSupported()) {
            throw new IOException("Mark not supported");
        }
        if (this.mark == -1L) {
            throw new IOException("Mark not set");
        }

        this.in.reset();
        this.left = this.mark;
    }

    public long skip(long n) throws IOException {
        n = Math.min(n, this.left);
        long skipped = this.in.skip(n);
        this.left -= skipped;
        return skipped;
    }
}

