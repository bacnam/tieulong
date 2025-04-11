package org.apache.thrift.server;

import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TNonblockingServer extends TServer {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TNonblockingServer.class.getName());
    protected final Options options_;
    private final long MAX_READ_BUFFER_BYTES;
    private volatile boolean stopped_ = true;
    private SelectThread selectThread_;
    private long readBufferBytesAllocated = 0;

    public TNonblockingServer(TProcessor processor,
                              TNonblockingServerTransport serverTransport) {
        this(new TProcessorFactory(processor), serverTransport);
    }

    public TNonblockingServer(TProcessorFactory processorFactory,
                              TNonblockingServerTransport serverTransport) {
        this(processorFactory, serverTransport,
                new TFramedTransport.Factory(),
                new TBinaryProtocol.Factory(), new TBinaryProtocol.Factory());
    }

    public TNonblockingServer(TProcessor processor,
                              TNonblockingServerTransport serverTransport,
                              TProtocolFactory protocolFactory) {
        this(processor, serverTransport,
                new TFramedTransport.Factory(),
                protocolFactory, protocolFactory);
    }

    public TNonblockingServer(TProcessor processor,
                              TNonblockingServerTransport serverTransport,
                              TFramedTransport.Factory transportFactory,
                              TProtocolFactory protocolFactory) {
        this(processor, serverTransport,
                transportFactory,
                protocolFactory, protocolFactory);
    }

    public TNonblockingServer(TProcessorFactory processorFactory,
                              TNonblockingServerTransport serverTransport,
                              TFramedTransport.Factory transportFactory,
                              TProtocolFactory protocolFactory) {
        this(processorFactory, serverTransport,
                transportFactory,
                protocolFactory, protocolFactory);
    }

    public TNonblockingServer(TProcessor processor,
                              TNonblockingServerTransport serverTransport,
                              TFramedTransport.Factory outputTransportFactory,
                              TProtocolFactory inputProtocolFactory,
                              TProtocolFactory outputProtocolFactory) {
        this(new TProcessorFactory(processor), serverTransport,
                outputTransportFactory,
                inputProtocolFactory, outputProtocolFactory);
    }

    public TNonblockingServer(TProcessorFactory processorFactory,
                              TNonblockingServerTransport serverTransport,
                              TFramedTransport.Factory outputTransportFactory,
                              TProtocolFactory inputProtocolFactory,
                              TProtocolFactory outputProtocolFactory) {
        this(processorFactory, serverTransport,
                outputTransportFactory,
                inputProtocolFactory, outputProtocolFactory,
                new Options());
    }

    public TNonblockingServer(TProcessorFactory processorFactory,
                              TNonblockingServerTransport serverTransport,
                              TFramedTransport.Factory outputTransportFactory,
                              TProtocolFactory inputProtocolFactory,
                              TProtocolFactory outputProtocolFactory,
                              Options options) {
        super(processorFactory, serverTransport,
                null, outputTransportFactory,
                inputProtocolFactory, outputProtocolFactory);
        options_ = options;
        options_.validate();
        MAX_READ_BUFFER_BYTES = options.maxReadBufferBytes;
    }

    public void serve() {

        if (!startListening()) {
            return;
        }

        if (!startSelectorThread()) {
            return;
        }

        joinSelector();

        stopListening();
    }

    protected boolean startListening() {
        try {
            serverTransport_.listen();
            return true;
        } catch (TTransportException ttx) {
            LOGGER.error("Failed to start listening on server socket!", ttx);
            return false;
        }
    }

    protected void stopListening() {
        serverTransport_.close();
    }

    protected boolean startSelectorThread() {

        try {
            selectThread_ = new SelectThread((TNonblockingServerTransport) serverTransport_);
            stopped_ = false;
            selectThread_.start();
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to start selector thread!", e);
            return false;
        }
    }

    protected void joinSelector() {

        try {
            selectThread_.join();
        } catch (InterruptedException e) {

        }
    }

    public void stop() {
        stopped_ = true;
        if (selectThread_ != null) {
            selectThread_.wakeupSelector();
        }
    }

    protected boolean requestInvoke(FrameBuffer frameBuffer) {
        frameBuffer.invoke();
        return true;
    }

    protected void requestSelectInterestChange(FrameBuffer frameBuffer) {
        selectThread_.requestSelectInterestChange(frameBuffer);
    }

    public boolean isStopped() {
        return selectThread_.isStopped();
    }

    public static class Options {
        public long maxReadBufferBytes = Long.MAX_VALUE;

        public Options() {
        }

        public void validate() {
            if (maxReadBufferBytes <= 1024) {
                throw new IllegalArgumentException("You must allocate at least 1KB to the read buffer.");
            }
        }
    }

    protected class SelectThread extends Thread {

        private final TNonblockingServerTransport serverTransport;
        private final Selector selector;

        private final Set<FrameBuffer> selectInterestChanges =
                new HashSet<FrameBuffer>();

        public SelectThread(final TNonblockingServerTransport serverTransport)
                throws IOException {
            this.serverTransport = serverTransport;
            this.selector = SelectorProvider.provider().openSelector();
            serverTransport.registerSelector(selector);
        }

        public boolean isStopped() {
            return stopped_;
        }

        public void run() {
            try {
                while (!stopped_) {
                    select();
                    processInterestChanges();
                }
            } catch (Throwable t) {
                LOGGER.error("run() exiting due to uncaught error", t);
            } finally {
                stopped_ = true;
            }
        }

        public void wakeupSelector() {
            selector.wakeup();
        }

        public void requestSelectInterestChange(FrameBuffer frameBuffer) {
            synchronized (selectInterestChanges) {
                selectInterestChanges.add(frameBuffer);
            }

            selector.wakeup();
        }

        private void select() {
            try {

                selector.select();

                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (!stopped_ && selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        cleanupSelectionkey(key);
                        continue;
                    }

                    if (key.isAcceptable()) {
                        handleAccept();
                    } else if (key.isReadable()) {

                        handleRead(key);
                    } else if (key.isWritable()) {

                        handleWrite(key);
                    } else {
                        LOGGER.warn("Unexpected state in select! " + key.interestOps());
                    }
                }
            } catch (IOException e) {
                LOGGER.warn("Got an IOException while selecting!", e);
            }
        }

        private void processInterestChanges() {
            synchronized (selectInterestChanges) {
                for (FrameBuffer fb : selectInterestChanges) {
                    fb.changeSelectInterests();
                }
                selectInterestChanges.clear();
            }
        }

        private void handleAccept() throws IOException {
            SelectionKey clientKey = null;
            TNonblockingTransport client = null;
            try {

                client = (TNonblockingTransport) serverTransport.accept();
                clientKey = client.registerSelector(selector, SelectionKey.OP_READ);

                FrameBuffer frameBuffer = new FrameBuffer(client, clientKey);
                clientKey.attach(frameBuffer);
            } catch (TTransportException tte) {

                LOGGER.warn("Exception trying to accept!", tte);
                tte.printStackTrace();
                if (clientKey != null) cleanupSelectionkey(clientKey);
                if (client != null) client.close();
            }
        }

        private void handleRead(SelectionKey key) {
            FrameBuffer buffer = (FrameBuffer) key.attachment();
            if (!buffer.read()) {
                cleanupSelectionkey(key);
                return;
            }

            if (buffer.isFrameFullyRead()) {
                if (!requestInvoke(buffer)) {
                    cleanupSelectionkey(key);
                }
            }
        }

        private void handleWrite(SelectionKey key) {
            FrameBuffer buffer = (FrameBuffer) key.attachment();
            if (!buffer.write()) {
                cleanupSelectionkey(key);
            }
        }

        private void cleanupSelectionkey(SelectionKey key) {

            FrameBuffer buffer = (FrameBuffer) key.attachment();
            if (buffer != null) {

                buffer.close();
            }

            key.cancel();
        }
    }

    protected class FrameBuffer {

        private static final int READING_FRAME_SIZE = 1;

        private static final int READING_FRAME = 2;

        private static final int READ_FRAME_COMPLETE = 3;

        private static final int AWAITING_REGISTER_WRITE = 4;

        private static final int WRITING = 6;

        private static final int AWAITING_REGISTER_READ = 7;

        private static final int AWAITING_CLOSE = 8;

        private final TNonblockingTransport trans_;

        private final SelectionKey selectionKey_;

        private int state_ = READING_FRAME_SIZE;

        private ByteBuffer buffer_;

        private TByteArrayOutputStream response_;

        public FrameBuffer(final TNonblockingTransport trans,
                           final SelectionKey selectionKey) {
            trans_ = trans;
            selectionKey_ = selectionKey;
            buffer_ = ByteBuffer.allocate(4);
        }

        public boolean read() {
            if (state_ == READING_FRAME_SIZE) {

                if (!internalRead()) {
                    return false;
                }

                if (buffer_.remaining() == 0) {

                    int frameSize = buffer_.getInt(0);
                    if (frameSize <= 0) {
                        LOGGER.error("Read an invalid frame size of " + frameSize
                                + ". Are you using TFramedTransport on the client side?");
                        return false;
                    }

                    if (frameSize > MAX_READ_BUFFER_BYTES) {
                        LOGGER.error("Read a frame size of " + frameSize
                                + ", which is bigger than the maximum allowable buffer size for ALL connections.");
                        return false;
                    }

                    if (readBufferBytesAllocated + frameSize > MAX_READ_BUFFER_BYTES) {
                        return true;
                    }

                    readBufferBytesAllocated += frameSize;

                    buffer_ = ByteBuffer.allocate(frameSize);

                    state_ = READING_FRAME;
                } else {

                    return true;
                }
            }

            if (state_ == READING_FRAME) {
                if (!internalRead()) {
                    return false;
                }

                if (buffer_.remaining() == 0) {

                    selectionKey_.interestOps(0);
                    state_ = READ_FRAME_COMPLETE;
                }

                return true;
            }

            LOGGER.error("Read was called but state is invalid (" + state_ + ")");
            return false;
        }

        public boolean write() {
            if (state_ == WRITING) {
                try {
                    if (trans_.write(buffer_) < 0) {
                        return false;
                    }
                } catch (IOException e) {
                    LOGGER.warn("Got an IOException during write!", e);
                    return false;
                }

                if (buffer_.remaining() == 0) {
                    prepareRead();
                }
                return true;
            }

            LOGGER.error("Write was called, but state is invalid (" + state_ + ")");
            return false;
        }

        public void changeSelectInterests() {
            if (state_ == AWAITING_REGISTER_WRITE) {

                selectionKey_.interestOps(SelectionKey.OP_WRITE);
                state_ = WRITING;
            } else if (state_ == AWAITING_REGISTER_READ) {
                prepareRead();
            } else if (state_ == AWAITING_CLOSE) {
                close();
                selectionKey_.cancel();
            } else {
                LOGGER.error(
                        "changeSelectInterest was called, but state is invalid ("
                                + state_ + ")");
            }
        }

        public void close() {

            if (state_ == READING_FRAME || state_ == READ_FRAME_COMPLETE) {
                readBufferBytesAllocated -= buffer_.array().length;
            }
            trans_.close();
        }

        public boolean isFrameFullyRead() {
            return state_ == READ_FRAME_COMPLETE;
        }

        public void responseReady() {

            readBufferBytesAllocated -= buffer_.array().length;

            if (response_.len() == 0) {

                state_ = AWAITING_REGISTER_READ;
                buffer_ = null;
            } else {
                buffer_ = ByteBuffer.wrap(response_.get(), 0, response_.len());

                state_ = AWAITING_REGISTER_WRITE;
            }
            requestSelectInterestChange();
        }

        public void invoke() {
            TTransport inTrans = getInputTransport();
            TProtocol inProt = inputProtocolFactory_.getProtocol(inTrans);
            TProtocol outProt = outputProtocolFactory_.getProtocol(getOutputTransport());

            try {
                processorFactory_.getProcessor(inTrans).process(inProt, outProt);
                responseReady();
                return;
            } catch (TException te) {
                LOGGER.warn("Exception while invoking!", te);
            } catch (Exception e) {
                LOGGER.error("Unexpected exception while invoking!", e);
            }

            state_ = AWAITING_CLOSE;
            requestSelectInterestChange();
        }

        private TTransport getInputTransport() {
            return new TMemoryInputTransport(buffer_.array());
        }

        private TTransport getOutputTransport() {
            response_ = new TByteArrayOutputStream();
            return outputTransportFactory_.getTransport(new TIOStreamTransport(response_));
        }

        private boolean internalRead() {
            try {
                if (trans_.read(buffer_) < 0) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                LOGGER.warn("Got an IOException in internalRead!", e);
                return false;
            }
        }

        private void prepareRead() {

            selectionKey_.interestOps(SelectionKey.OP_READ);

            buffer_ = ByteBuffer.allocate(4);
            state_ = READING_FRAME_SIZE;
        }

        private void requestSelectInterestChange() {
            if (Thread.currentThread() == selectThread_) {
                changeSelectInterests();
            } else {
                TNonblockingServer.this.requestSelectInterestChange(this);
            }
        }
    }
}
