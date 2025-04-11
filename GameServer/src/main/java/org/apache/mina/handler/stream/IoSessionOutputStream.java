package org.apache.mina.handler.stream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.io.OutputStream;

class IoSessionOutputStream
        extends OutputStream {
    private final IoSession session;
    private WriteFuture lastWriteFuture;

    public IoSessionOutputStream(IoSession session) {
        this.session = session;
    }

    public void close() throws IOException {
        try {
            flush();
        } finally {
            this.session.close(true).awaitUninterruptibly();
        }
    }

    private void checkClosed() throws IOException {
        if (!this.session.isConnected()) {
            throw new IOException("The session has been closed.");
        }
    }

    private synchronized void write(IoBuffer buf) throws IOException {
        checkClosed();
        WriteFuture future = this.session.write(buf);
        this.lastWriteFuture = future;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        write(IoBuffer.wrap((byte[]) b.clone(), off, len));
    }

    public void write(int b) throws IOException {
        IoBuffer buf = IoBuffer.allocate(1);
        buf.put((byte) b);
        buf.flip();
        write(buf);
    }

    public synchronized void flush() throws IOException {
        if (this.lastWriteFuture == null) {
            return;
        }

        this.lastWriteFuture.awaitUninterruptibly();
        if (!this.lastWriteFuture.isWritten())
            throw new IOException("The bytes could not be written to the session");
    }
}

