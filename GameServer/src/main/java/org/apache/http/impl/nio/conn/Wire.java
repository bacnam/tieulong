package org.apache.http.impl.nio.conn;

import org.apache.commons.logging.Log;

import java.nio.ByteBuffer;

class Wire {
    private final Log log;
    private final String id;

    public Wire(Log log, String id) {
        this.log = log;
        this.id = id;
    }

    private void wire(String header, byte[] b, int pos, int off) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < off; i++) {
            int ch = b[pos + i];
            if (ch == 13) {
                buffer.append("[\\r]");
            } else if (ch == 10) {
                buffer.append("[\\n]\"");
                buffer.insert(0, "\"");
                buffer.insert(0, header);
                this.log.debug(this.id + " " + buffer.toString());
                buffer.setLength(0);
            } else if (ch < 32 || ch > 127) {
                buffer.append("[0x");
                buffer.append(Integer.toHexString(ch));
                buffer.append("]");
            } else {
                buffer.append((char) ch);
            }
        }
        if (buffer.length() > 0) {
            buffer.append('"');
            buffer.insert(0, '"');
            buffer.insert(0, header);
            this.log.debug(this.id + " " + buffer.toString());
        }
    }

    public boolean isEnabled() {
        return this.log.isDebugEnabled();
    }

    public void output(byte[] b, int pos, int off) {
        wire(">> ", b, pos, off);
    }

    public void input(byte[] b, int pos, int off) {
        wire("<< ", b, pos, off);
    }

    public void output(byte[] b) {
        output(b, 0, b.length);
    }

    public void input(byte[] b) {
        input(b, 0, b.length);
    }

    public void output(int b) {
        output(new byte[]{(byte) b});
    }

    public void input(int b) {
        input(new byte[]{(byte) b});
    }

    public void output(ByteBuffer b) {
        if (b.hasArray()) {
            output(b.array(), b.arrayOffset() + b.position(), b.remaining());
        } else {
            byte[] tmp = new byte[b.remaining()];
            b.get(tmp);
            output(tmp);
        }
    }

    public void input(ByteBuffer b) {
        if (b.hasArray()) {
            input(b.array(), b.arrayOffset() + b.position(), b.remaining());
        } else {
            byte[] tmp = new byte[b.remaining()];
            b.get(tmp);
            input(tmp);
        }
    }
}

