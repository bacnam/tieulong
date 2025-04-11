package org.apache.thrift.transport;

import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

abstract class TSaslTransport extends TTransport {

    protected static final int DEFAULT_MAX_LENGTH = 0x7FFFFFFF;
    protected static final int MECHANISM_NAME_BYTES = 1;
    protected static final int STATUS_BYTES = 1;
    protected static final int PAYLOAD_LENGTH_BYTES = 4;
    private static final Logger LOGGER = LoggerFactory.getLogger(TSaslTransport.class);
    private final TByteArrayOutputStream writeBuffer = new TByteArrayOutputStream(1024);
    private final byte[] messageHeader = new byte[STATUS_BYTES + PAYLOAD_LENGTH_BYTES];
    protected TTransport underlyingTransport;

    private SaslParticipant sasl;

    private boolean shouldWrap = false;

    private TMemoryInputTransport readBuffer = new TMemoryInputTransport();

    protected TSaslTransport(TTransport underlyingTransport) {
        this.underlyingTransport = underlyingTransport;
    }

    protected TSaslTransport(SaslClient saslClient, TTransport underlyingTransport) {
        sasl = new SaslParticipant(saslClient);
        this.underlyingTransport = underlyingTransport;
    }

    protected void sendSaslMessage(NegotiationStatus status, byte[] payload) throws TTransportException {
        if (payload == null)
            payload = new byte[0];

        messageHeader[0] = status.getValue();
        EncodingUtils.encodeBigEndian(payload.length, messageHeader, STATUS_BYTES);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(getRole() + ": Writing message with status {} and payload length {}",
                    status, payload.length);
        underlyingTransport.write(messageHeader);
        underlyingTransport.write(payload);
        underlyingTransport.flush();
    }

    protected SaslResponse receiveSaslMessage() throws TTransportException {
        underlyingTransport.readAll(messageHeader, 0, messageHeader.length);

        byte statusByte = messageHeader[0];
        byte[] payload = new byte[EncodingUtils.decodeBigEndian(messageHeader, STATUS_BYTES)];
        underlyingTransport.readAll(payload, 0, payload.length);

        NegotiationStatus status = NegotiationStatus.byValue(statusByte);
        if (status == null) {
            sendAndThrowMessage(NegotiationStatus.ERROR, "Invalid status " + statusByte);
        } else if (status == NegotiationStatus.BAD || status == NegotiationStatus.ERROR) {
            try {
                String remoteMessage = new String(payload, "UTF-8");
                throw new TTransportException("Peer indicated failure: " + remoteMessage);
            } catch (UnsupportedEncodingException e) {
                throw new TTransportException(e);
            }
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(getRole() + ": Received message with status {} and payload length {}",
                    status, payload.length);
        return new SaslResponse(status, payload);
    }

    protected void sendAndThrowMessage(NegotiationStatus status, String message) throws TTransportException {
        try {
            sendSaslMessage(status, message.getBytes());
        } catch (Exception e) {
            LOGGER.warn("Could not send failure response", e);
            message += "\nAlso, could not send response: " + e.toString();
        }
        throw new TTransportException(message);
    }

    abstract protected void handleSaslStartMessage() throws TTransportException, SaslException;

    protected abstract SaslRole getRole();

    @Override
    public void open() throws TTransportException {
        LOGGER.debug("opening transport {}", this);
        if (sasl != null && sasl.isComplete())
            throw new TTransportException("SASL transport already open");

        if (!underlyingTransport.isOpen())
            underlyingTransport.open();

        try {

            handleSaslStartMessage();
            LOGGER.debug("{}: Start message handled", getRole());

            SaslResponse message = null;
            while (!sasl.isComplete()) {
                message = receiveSaslMessage();
                if (message.status != NegotiationStatus.COMPLETE &&
                        message.status != NegotiationStatus.OK) {
                    throw new TTransportException("Expected COMPLETE or OK, got " + message.status);
                }

                byte[] challenge = sasl.evaluateChallengeOrResponse(message.payload);

                if (message.status == NegotiationStatus.COMPLETE &&
                        getRole() == SaslRole.CLIENT) {
                    LOGGER.debug("{}: All done!", getRole());
                    break;
                }

                sendSaslMessage(sasl.isComplete() ? NegotiationStatus.COMPLETE : NegotiationStatus.OK,
                        challenge);
            }
            LOGGER.debug("{}: Main negotiation loop complete", getRole());

            assert sasl.isComplete();

            if (getRole() == SaslRole.CLIENT &&
                    (message == null || message.status == NegotiationStatus.OK)) {
                LOGGER.debug("{}: SASL Client receiving last message", getRole());
                message = receiveSaslMessage();
                if (message.status != NegotiationStatus.COMPLETE) {
                    throw new TTransportException(
                            "Expected SASL COMPLETE, but got " + message.status);
                }
            }
        } catch (SaslException e) {
            try {
                sendAndThrowMessage(NegotiationStatus.BAD, e.getMessage());
            } finally {
                underlyingTransport.close();
            }
        }

        String qop = (String) sasl.getNegotiatedProperty(Sasl.QOP);
        if (qop != null && !qop.equalsIgnoreCase("auth"))
            shouldWrap = true;
    }

    public SaslClient getSaslClient() {
        return sasl.saslClient;
    }

    public SaslServer getSaslServer() {
        return sasl.saslServer;
    }

    protected void setSaslServer(SaslServer saslServer) {
        sasl = new SaslParticipant(saslServer);
    }

    protected int readLength() throws TTransportException {
        byte[] lenBuf = new byte[4];
        underlyingTransport.readAll(lenBuf, 0, lenBuf.length);
        return EncodingUtils.decodeBigEndian(lenBuf);
    }

    protected void writeLength(int length) throws TTransportException {
        byte[] lenBuf = new byte[4];
        TFramedTransport.encodeFrameSize(length, lenBuf);
        underlyingTransport.write(lenBuf);
    }

    @Override
    public void close() {
        underlyingTransport.close();
        try {
            sasl.dispose();
        } catch (SaslException e) {

        }
    }

    @Override
    public boolean isOpen() {
        return underlyingTransport.isOpen() && sasl != null && sasl.isComplete();
    }

    @Override
    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (!isOpen())
            throw new TTransportException("SASL authentication not complete");

        int got = readBuffer.read(buf, off, len);
        if (got > 0) {
            return got;
        }

        try {
            readFrame();
        } catch (SaslException e) {
            throw new TTransportException(e);
        }

        return readBuffer.read(buf, off, len);
    }

    private void readFrame() throws TTransportException, SaslException {
        int dataLength = readLength();

        if (dataLength < 0)
            throw new TTransportException("Read a negative frame size (" + dataLength + ")!");

        byte[] buff = new byte[dataLength];
        LOGGER.debug("{}: reading data length: {}", getRole(), dataLength);
        underlyingTransport.readAll(buff, 0, dataLength);
        if (shouldWrap) {
            buff = sasl.unwrap(buff, 0, buff.length);
            LOGGER.debug("data length after unwrap: {}", buff.length);
        }
        readBuffer.reset(buff);
    }

    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {
        if (!isOpen())
            throw new TTransportException("SASL authentication not complete");

        writeBuffer.write(buf, off, len);
    }

    @Override
    public void flush() throws TTransportException {
        byte[] buf = writeBuffer.get();
        int dataLength = writeBuffer.len();
        writeBuffer.reset();

        if (shouldWrap) {
            LOGGER.debug("data length before wrap: {}", dataLength);
            try {
                buf = sasl.wrap(buf, 0, dataLength);
            } catch (SaslException e) {
                throw new TTransportException(e);
            }
            dataLength = buf.length;
        }
        LOGGER.debug("writing data length: {}", dataLength);
        writeLength(dataLength);
        underlyingTransport.write(buf, 0, dataLength);
        underlyingTransport.flush();
    }

    protected static enum SaslRole {
        SERVER, CLIENT;
    }

    protected static enum NegotiationStatus {
        START((byte) 0x01),
        OK((byte) 0x02),
        BAD((byte) 0x03),
        ERROR((byte) 0x04),
        COMPLETE((byte) 0x05);

        private static final Map<Byte, NegotiationStatus> reverseMap =
                new HashMap<Byte, NegotiationStatus>();

        static {
            for (NegotiationStatus s : NegotiationStatus.class.getEnumConstants()) {
                reverseMap.put(s.getValue(), s);
            }
        }

        private final byte value;

        private NegotiationStatus(byte val) {
            this.value = val;
        }

        public static NegotiationStatus byValue(byte val) {
            return reverseMap.get(val);
        }

        public byte getValue() {
            return value;
        }
    }

    protected static class SaslResponse {
        public NegotiationStatus status;
        public byte[] payload;

        public SaslResponse(NegotiationStatus status, byte[] payload) {
            this.status = status;
            this.payload = payload;
        }
    }

    private static class SaslParticipant {

        public SaslServer saslServer;
        public SaslClient saslClient;

        public SaslParticipant(SaslServer saslServer) {
            this.saslServer = saslServer;
        }

        public SaslParticipant(SaslClient saslClient) {
            this.saslClient = saslClient;
        }

        public byte[] evaluateChallengeOrResponse(byte[] challengeOrResponse) throws SaslException {
            if (saslClient != null) {
                return saslClient.evaluateChallenge(challengeOrResponse);
            } else {
                return saslServer.evaluateResponse(challengeOrResponse);
            }
        }

        public boolean isComplete() {
            if (saslClient != null)
                return saslClient.isComplete();
            else
                return saslServer.isComplete();
        }

        public void dispose() throws SaslException {
            if (saslClient != null)
                saslClient.dispose();
            else
                saslServer.dispose();
        }

        public byte[] unwrap(byte[] buf, int off, int len) throws SaslException {
            if (saslClient != null)
                return saslClient.unwrap(buf, off, len);
            else
                return saslServer.unwrap(buf, off, len);
        }

        public byte[] wrap(byte[] buf, int off, int len) throws SaslException {
            if (saslClient != null)
                return saslClient.wrap(buf, off, len);
            else
                return saslServer.wrap(buf, off, len);
        }

        public Object getNegotiatedProperty(String propName) {
            if (saslClient != null)
                return saslClient.getNegotiatedProperty(propName);
            else
                return saslServer.getNegotiatedProperty(propName);
        }
    }
}
