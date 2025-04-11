package org.apache.mina.filter.codec;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoSession;

import java.util.Queue;

public class ProtocolCodecSession
        extends DummySession {
    private final WriteFuture notWrittenFuture = DefaultWriteFuture.newNotWrittenFuture((IoSession) this, new UnsupportedOperationException());

    private final AbstractProtocolEncoderOutput encoderOutput = new AbstractProtocolEncoderOutput() {
        public WriteFuture flush() {
            return ProtocolCodecSession.this.notWrittenFuture;
        }
    };

    private final AbstractProtocolDecoderOutput decoderOutput = new AbstractProtocolDecoderOutput() {
        public void flush(IoFilter.NextFilter nextFilter, IoSession session) {
        }
    };

    public ProtocolEncoderOutput getEncoderOutput() {
        return this.encoderOutput;
    }

    public Queue<Object> getEncoderOutputQueue() {
        return this.encoderOutput.getMessageQueue();
    }

    public ProtocolDecoderOutput getDecoderOutput() {
        return this.decoderOutput;
    }

    public Queue<Object> getDecoderOutputQueue() {
        return this.decoderOutput.getMessageQueue();
    }
}

