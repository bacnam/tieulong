package org.apache.thrift.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TIOStreamTransport extends TTransport {

    private static final Logger LOGGER = LoggerFactory.getLogger(TIOStreamTransport.class.getName());

    protected InputStream inputStream_ = null;

    protected OutputStream outputStream_ = null;

    protected TIOStreamTransport() {
    }

    public TIOStreamTransport(InputStream is) {
        inputStream_ = is;
    }

    public TIOStreamTransport(OutputStream os) {
        outputStream_ = os;
    }

    public TIOStreamTransport(InputStream is, OutputStream os) {
        inputStream_ = is;
        outputStream_ = os;
    }

    public boolean isOpen() {
        return true;
    }

    public void open() throws TTransportException {
    }

    public void close() {
        if (inputStream_ != null) {
            try {
                inputStream_.close();
            } catch (IOException iox) {
                LOGGER.warn("Error closing input stream.", iox);
            }
            inputStream_ = null;
        }
        if (outputStream_ != null) {
            try {
                outputStream_.close();
            } catch (IOException iox) {
                LOGGER.warn("Error closing output stream.", iox);
            }
            outputStream_ = null;
        }
    }

    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (inputStream_ == null) {
            throw new TTransportException(TTransportException.NOT_OPEN, "Cannot read from null inputStream");
        }
        int bytesRead;
        try {
            bytesRead = inputStream_.read(buf, off, len);
        } catch (IOException iox) {
            throw new TTransportException(TTransportException.UNKNOWN, iox);
        }
        if (bytesRead < 0) {
            throw new TTransportException(TTransportException.END_OF_FILE);
        }
        return bytesRead;
    }

    public void write(byte[] buf, int off, int len) throws TTransportException {
        if (outputStream_ == null) {
            throw new TTransportException(TTransportException.NOT_OPEN, "Cannot write to null outputStream");
        }
        try {
            outputStream_.write(buf, off, len);
        } catch (IOException iox) {
            throw new TTransportException(TTransportException.UNKNOWN, iox);
        }
    }

    public void flush() throws TTransportException {
        if (outputStream_ == null) {
            throw new TTransportException(TTransportException.NOT_OPEN, "Cannot flush null outputStream");
        }
        try {
            outputStream_.flush();
        } catch (IOException iox) {
            throw new TTransportException(TTransportException.UNKNOWN, iox);
        }
    }
}
