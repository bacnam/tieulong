package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DecodingStateProtocolDecoder
        implements ProtocolDecoder {
    private final DecodingState state;
    private final Queue<IoBuffer> undecodedBuffers = new ConcurrentLinkedQueue<IoBuffer>();

    private IoSession session;

    public DecodingStateProtocolDecoder(DecodingState state) {
        if (state == null) {
            throw new IllegalArgumentException("state");
        }
        this.state = state;
    }

    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (this.session == null) {
            this.session = session;
        } else if (this.session != session) {
            throw new IllegalStateException(getClass().getSimpleName() + " is a stateful decoder.  " + "You have to create one per session.");
        }

        this.undecodedBuffers.offer(in);
        while (true) {
            IoBuffer b = this.undecodedBuffers.peek();
            if (b == null) {
                break;
            }

            int oldRemaining = b.remaining();
            this.state.decode(b, out);
            int newRemaining = b.remaining();
            if (newRemaining != 0) {
                if (oldRemaining == newRemaining) {
                    throw new IllegalStateException(DecodingState.class.getSimpleName() + " must " + "consume at least one byte per decode().");
                }
                continue;
            }
            this.undecodedBuffers.poll();
        }
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
        this.state.finishDecode(out);
    }

    public void dispose(IoSession session) throws Exception {
    }
}

