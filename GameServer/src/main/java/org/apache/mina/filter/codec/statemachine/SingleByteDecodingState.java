package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class SingleByteDecodingState
        implements DecodingState {
    public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (in.hasRemaining()) {
            return finishDecode(in.get(), out);
        }

        return this;
    }

    public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
        throw new ProtocolDecoderException("Unexpected end of session while waiting for a single byte.");
    }

    protected abstract DecodingState finishDecode(byte paramByte, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

