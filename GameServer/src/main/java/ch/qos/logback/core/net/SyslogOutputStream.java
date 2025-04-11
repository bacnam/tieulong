package ch.qos.logback.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class SyslogOutputStream
        extends OutputStream {
    private static final int MAX_LEN = 1024;
    private final int port;
    private InetAddress address;
    private DatagramSocket ds;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public SyslogOutputStream(String syslogHost, int port) throws UnknownHostException, SocketException {
        this.address = InetAddress.getByName(syslogHost);
        this.port = port;
        this.ds = new DatagramSocket();
    }

    public void write(byte[] byteArray, int offset, int len) throws IOException {
        this.baos.write(byteArray, offset, len);
    }

    public void flush() throws IOException {
        byte[] bytes = this.baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, this.address, this.port);

        if (this.baos.size() > 1024) {
            this.baos = new ByteArrayOutputStream();
        } else {
            this.baos.reset();
        }

        if (bytes.length == 0) {
            return;
        }
        if (this.ds != null) {
            this.ds.send(packet);
        }
    }

    public void close() {
        this.address = null;
        this.ds = null;
    }

    public int getPort() {
        return this.port;
    }

    public void write(int b) throws IOException {
        this.baos.write(b);
    }

    int getSendBufferSize() throws SocketException {
        return this.ds.getSendBufferSize();
    }
}

