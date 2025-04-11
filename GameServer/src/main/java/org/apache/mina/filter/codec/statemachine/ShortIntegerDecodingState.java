package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class ShortIntegerDecodingState
        implements DecodingState {
    private int highByte;
    private int counter;

    public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        while (in.hasRemaining()) {
            switch (this.counter) {
                case 0:
                    this.highByte = in.getUnsigned();
                    break;
                case 1:
                    this.counter = 0;
                    return finishDecode((short) (this.highByte << 8 | in.getUnsigned()), out);
                default:
                    throw new InternalError();
            }

            this.counter++;
        }
        return this;
    }

    public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
        throw new ProtocolDecoderException("Unexpected end of session while waiting for a short integer.");
    }

    protected abstract DecodingState finishDecode(short paramShort, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

