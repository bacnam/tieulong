package org.apache.mina.filter.codec.prefixedstring;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class PrefixedStringCodecFactory
        implements ProtocolCodecFactory {
    private final PrefixedStringEncoder encoder;
    private final PrefixedStringDecoder decoder;

    public PrefixedStringCodecFactory(Charset charset) {
        this.encoder = new PrefixedStringEncoder(charset);
        this.decoder = new PrefixedStringDecoder(charset);
    }

    public PrefixedStringCodecFactory() {
        this(Charset.defaultCharset());
    }

    public int getEncoderMaxDataLength() {
        return this.encoder.getMaxDataLength();
    }

    public void setEncoderMaxDataLength(int maxDataLength) {
        this.encoder.setMaxDataLength(maxDataLength);
    }

    public int getDecoderMaxDataLength() {
        return this.decoder.getMaxDataLength();
    }

    public void setDecoderMaxDataLength(int maxDataLength) {
        this.decoder.setMaxDataLength(maxDataLength);
    }

    public int getDecoderPrefixLength() {
        return this.decoder.getPrefixLength();
    }

    public void setDecoderPrefixLength(int prefixLength) {
        this.decoder.setPrefixLength(prefixLength);
    }

    public int getEncoderPrefixLength() {
        return this.encoder.getPrefixLength();
    }

    public void setEncoderPrefixLength(int prefixLength) {
        this.encoder.setPrefixLength(prefixLength);
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return (ProtocolEncoder) this.encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return (ProtocolDecoder) this.decoder;
    }
}

