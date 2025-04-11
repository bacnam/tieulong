package org.apache.mina.filter.codec.serialization;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ObjectSerializationCodecFactory
        implements ProtocolCodecFactory {
    private final ObjectSerializationEncoder encoder;
    private final ObjectSerializationDecoder decoder;

    public ObjectSerializationCodecFactory() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ObjectSerializationCodecFactory(ClassLoader classLoader) {
        this.encoder = new ObjectSerializationEncoder();
        this.decoder = new ObjectSerializationDecoder(classLoader);
    }

    public ProtocolEncoder getEncoder(IoSession session) {
        return (ProtocolEncoder) this.encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) {
        return (ProtocolDecoder) this.decoder;
    }

    public int getEncoderMaxObjectSize() {
        return this.encoder.getMaxObjectSize();
    }

    public void setEncoderMaxObjectSize(int maxObjectSize) {
        this.encoder.setMaxObjectSize(maxObjectSize);
    }

    public int getDecoderMaxObjectSize() {
        return this.decoder.getMaxObjectSize();
    }

    public void setDecoderMaxObjectSize(int maxObjectSize) {
        this.decoder.setMaxObjectSize(maxObjectSize);
    }
}

