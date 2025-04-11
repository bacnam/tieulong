package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class ConsumeToTerminatorDecodingState
        implements DecodingState {
    private final byte terminator;
    private IoBuffer buffer;

    public ConsumeToTerminatorDecodingState(byte terminator) {
        this.terminator = terminator;
    }

    public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        int terminatorPos = in.indexOf(this.terminator);

        if (terminatorPos >= 0) {
            IoBuffer product;
            int limit = in.limit();

            if (in.position() < terminatorPos) {
                in.limit(terminatorPos);

                if (this.buffer == null) {
                    product = in.slice();
                } else {
                    this.buffer.put(in);
                    product = this.buffer.flip();
                    this.buffer = null;
                }

                in.limit(limit);

            } else if (this.buffer == null) {
                product = IoBuffer.allocate(0);
            } else {
                product = this.buffer.flip();
                this.buffer = null;
            }

            in.position(terminatorPos + 1);
            return finishDecode(product, out);
        }

        if (this.buffer == null) {
            this.buffer = IoBuffer.allocate(in.remaining());
            this.buffer.setAutoExpand(true);
        }

        this.buffer.put(in);
        return this;
    }

    public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
        IoBuffer product;
        if (this.buffer == null) {
            product = IoBuffer.allocate(0);
        } else {
            product = this.buffer.flip();
            this.buffer = null;
        }
        return finishDecode(product, out);
    }

    protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

