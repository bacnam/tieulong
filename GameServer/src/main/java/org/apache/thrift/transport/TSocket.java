package org.apache.thrift.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TSocket extends TIOStreamTransport {

    private static final Logger LOGGER = LoggerFactory.getLogger(TSocket.class.getName());

    private Socket socket_ = null;

    private String host_ = null;

    private int port_ = 0;

    private int timeout_ = 0;

    public TSocket(Socket socket) throws TTransportException {
        socket_ = socket;
        try {
            socket_.setSoLinger(false, 0);
            socket_.setTcpNoDelay(true);
        } catch (SocketException sx) {
            LOGGER.warn("Could not configure socket.", sx);
        }

        if (isOpen()) {
            try {
                inputStream_ = new BufferedInputStream(socket_.getInputStream(), 1024);
                outputStream_ = new BufferedOutputStream(socket_.getOutputStream(), 1024);
            } catch (IOException iox) {
                close();
                throw new TTransportException(TTransportException.NOT_OPEN, iox);
            }
        }
    }

    public TSocket(String host, int port) {
        this(host, port, 0);
    }

    public TSocket(String host, int port, int timeout) {
        host_ = host;
        port_ = port;
        timeout_ = timeout;
        initSocket();
    }

    private void initSocket() {
        socket_ = new Socket();
        try {
            socket_.setSoLinger(false, 0);
            socket_.setTcpNoDelay(true);
            socket_.setSoTimeout(timeout_);
        } catch (SocketException sx) {
            LOGGER.error("Could not configure socket.", sx);
        }
    }

    public void setTimeout(int timeout) {
        timeout_ = timeout;
        try {
            socket_.setSoTimeout(timeout);
        } catch (SocketException sx) {
            LOGGER.warn("Could not set socket timeout.", sx);
        }
    }

    public Socket getSocket() {
        if (socket_ == null) {
            initSocket();
        }
        return socket_;
    }

    public boolean isOpen() {
        if (socket_ == null) {
            return false;
        }
        return socket_.isConnected();
    }

    public void open() throws TTransportException {
        if (isOpen()) {
            throw new TTransportException(TTransportException.ALREADY_OPEN, "Socket already connected.");
        }

        if (host_.length() == 0) {
            throw new TTransportException(TTransportException.NOT_OPEN, "Cannot open null host.");
        }
        if (port_ <= 0) {
            throw new TTransportException(TTransportException.NOT_OPEN, "Cannot open without port.");
        }

        if (socket_ == null) {
            initSocket();
        }

        try {
            socket_.connect(new InetSocketAddress(host_, port_), timeout_);
            inputStream_ = new BufferedInputStream(socket_.getInputStream(), 1024);
            outputStream_ = new BufferedOutputStream(socket_.getOutputStream(), 1024);
        } catch (IOException iox) {
            close();
            throw new TTransportException(TTransportException.NOT_OPEN, iox);
        }
    }

    public void close() {

        super.close();

        if (socket_ != null) {
            try {
                socket_.close();
            } catch (IOException iox) {
                LOGGER.warn("Could not close socket.", iox);
            }
            socket_ = null;
        }
    }

}
